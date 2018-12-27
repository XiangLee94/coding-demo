import java.io.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.InternalCardinality;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

public class EsDAO {
    private String clusterName = "ElasticSearchETL";
    // ElasticSearch的host
    private String nodeHost = "192.168.0.251";
    // ElasticSearch的端口（Java API用的是Transport端口，也就是TCP）
    private int nodePort = 9300;
    private TransportClient client = null;

    private static int RES_MAX_COUNT = 10000;

    /**
     * init ES client
     */
    public EsDAO() {
        init();
    }

    private void init() {
        if (this.client == null) {
            synchronized (EsDAO.class) {
                if (this.client == null) {

                    Settings settings = Settings.builder().put("cluster.name", this.clusterName).put("client.transport.sniff", true).build();

                    try {
                        this.client = new PreBuiltTransportClient(settings).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(nodeHost), nodePort));
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public TransportClient initEsClient() {
        return this.client;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public void setNodeHost(String nodeHost) {
        this.nodeHost = nodeHost;
    }

    public void setNodePort(int nodePort) {
        this.nodePort = nodePort;
    }

    public static  String readToString(String fileName) {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        EsDAO esutil = new EsDAO();
        esutil.get5();
    }

    public String get() {
        SearchRequestBuilder r = initEsClient().prepareSearch("app_user_course-2018-07");
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.termQuery("cf.action.raw", "viewing"));
        boolQuery.must(QueryBuilders.rangeQuery("cf.actionTime").gt("2018-07-10 12:00:00").lte("2018-07-10 13:00:00"));
        TermsAggregationBuilder typeAggre = AggregationBuilders.terms("type").field("cf.type.raw").size(100);
        TermsAggregationBuilder courseIdAgggre = AggregationBuilders.terms("courseId").field("cf.courseId.raw").size(10000);
        TermsAggregationBuilder userIdAggre = AggregationBuilders.terms("userId").field("cf.userId.raw").size(10000);
        courseIdAgggre.subAggregation(userIdAggre);
        typeAggre.subAggregation(courseIdAgggre);
        r.addAggregation(typeAggre);
        r.setQuery(boolQuery).setSize(0);
        SearchResponse resp = r.get();
        Terms typeTerms = resp.getAggregations().get("type");
        for (Terms.Bucket t : typeTerms.getBuckets()) {
            final String type = t.getKeyAsString();
            Terms courseIdTerms = t.getAggregations().get("courseId");
            for (Terms.Bucket c : courseIdTerms.getBuckets()) {
                final String courseId = c.getKeyAsString();
                Terms userIdTerms = c.getAggregations().get("userId");
                for (Terms.Bucket u : userIdTerms.getBuckets()) {
                    final String userId = u.getKeyAsString();
                    System.out.println(type + "  " + courseId + "  " + userId + "  " + u.getDocCount());
                }
            }
        }
        return null;
    }

    public void get2() {
        SearchRequestBuilder r = initEsClient().prepareSearch("f_mid_order_details");
        //r.setFetchSource("cf.pm_account", null);
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //boolQuery.must(QueryBuilders.rangeQuery("cf.payment_time").gt("2018-06-01 00:00:00").lte("2018-07-23 13:00:00"));
        // boolQuery.must(QueryBuilders.termQuery("cf.call_seat_em","zhanglijing"));
        TermsAggregationBuilder typeAggre = AggregationBuilders.terms("callSeatEm").field("cf.resp_account.raw").size(10000);
        //CardinalityAggregationBuilder stuIdSet = AggregationBuilders.cardinality("stus").field("cf.resp_account.raw");
        r.addAggregation(typeAggre);
        // r.addAggregation(stuIdSet).setSize(10000);
        r.setQuery(boolQuery).setSize(0);
        SearchResponse resp = r.get();
        InternalCardinality typeTerms = resp.getAggregations().get("stus");
        long a = typeTerms.getValue();
        System.out.println(a);
    }


    public void get3() {
        SearchRequestBuilder r = initEsClient().prepareSearch("f_mid_order_details");
        String respAccountStrs = "anming,wuqi21,wangsubing,zhuohao,kangliuyong,lihuan40,zhangzihao,yuankai,liuchang52,luoqingjun52,ouyangpan,wangning93,fanhuanhuan,zhaoyiping,mouxiaolei,zhoutao,wuyong,wuwenkai,yangdong,huangyunchun,jiaxiaoyan,tongrunze,wuchunwa,liye17,mengxiangyue,xiezhiqiang,tangzongmei,matianwen,qijing,liuxiaoxi,lixintong,weiyu,zhangyujun,liuqiang17,mayu,lishibin,xiemeimei,pengguixiang,chenziqian,haochun,xiashuhua,zhaoman,mabinbin,luoting,huhuien,zhangding,yekaixin68,lijiayin,liguilong,huangshifeng,hanziwei,yuanyuan76,zhangbo94,xingweimeng,lisimin,yanglili78,xiongzhifen24,miyinuo,xiangxuyang,jingjunhua,chensiqi,linmeng,zhangli80,jiangdameng,wangjianan,nieyun,caixiaoru,zhengjianchao,mengchenyang,lishuo,meixueyu,zhangchi01,zhangkuochuan,weichunpeng,xiacaigui,tianxinyi,liuxuejie,tanguofang,xushihua,chenhaitong,jipengfei,dingpingli,xiexin,mashaobo,linxuanyi,liangshaobi,lida01,wangjian,chenxiaoyun,huangkeke,wangxia,zhangrusheng13,wangyiming,xingxinxi,chenbo01,tangyifan,wangtaixuan,wangchaoyang01,zengruishu,xujian,xuyuesheng,suiyanxin,wanghan,libenyuan,zhanglei02,chenzhihao,renlingfeng,huangchao,zhuzhixian,liuyi03,chenjingna,yangruyi01,kongxiangmin,zhusainan,hujin,wuhao01,huangruiqi,heminglun,shusong,chenjianfei,zhangzijian,hanshanxu,wangdi03,chengwangyuan,yechenxu,sunyiyang,laowentao,zhuyu,liujialu,zhangzhiping,lvwenlong,chenyuanyuan01,xiahaiyang,hukai01,liangzhifeng,wanganning,chenbaoqin,pengjin,yuansheng,huaguoliang,weiming,zhaokangchao,songmeina,panlingzhan,chenxiaosheng,leizhou,zhaozongbo,zhaokaiyun,taogan,yezhiming,zhangmengjie01,zhongmin,yujingping,tangjingjing,zhuangziliang,weihanfan,liurunlin,liuxinyan01,dingqin,luowenli,yizijian,yuanwenge,weizheng,shenweiqian,yangyao,chengxinyue,mayibo,zhuxin01,huwenjie,wangshengrui,xueyuan,lichuqiao,niuxuelin,tanxiao,zhaoyuting,dingwenxing,xiaoqian01,libing01,zhouxian,liuhanzhong,zhangzhi04,yuzhihao,dengchaohui,shaozhi,aiyucheng,chuoning,yuhan,huangshanshan,sunhui02,shichang01,zhouerdongfei,liuning,fangsimin,zhouhaibo,luqian,liuyachen,luli,wanghuan03,meili";
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.filter(QueryBuilders.rangeQuery("cf.payment_time").gt("2018-08-01 00:00:00").lte("2018-08-23 13:00:00"));
        boolQuery.filter(QueryBuilders.termsQuery("cf.resp_account.raw", respAccountStrs.split(",")));
        TermsAggregationBuilder typeAggre = AggregationBuilders.terms("respAccount").field("cf.resp_account.raw").size(10000);
        typeAggre.subAggregation(AggregationBuilders.sum("amount").field("cf.educate_amount"));
        r.addAggregation(typeAggre);
        r.setQuery(boolQuery).setSize(0);
        SearchResponse resp = r.get();
        Aggregations amounts = resp.getAggregations();
        Terms typeTerms = amounts.get("respAccount");
        for (Terms.Bucket t : typeTerms.getBuckets()) {
            String respAccount = t.getKeyAsString();
            Sum sum = t.getAggregations().get("amount");
            Double amount = sum.getValue();
            System.out.println(respAccount + " : " + amount);
        }
    }

    public void get4() {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        SearchRequestBuilder r = client.prepareSearch("f_mid_order_details");
        boolQuery.filter(QueryBuilders.termQuery("cf.delete_flag.raw", 0));
        boolQuery.filter(QueryBuilders.rangeQuery("cf.payment_time").gte("2018-08-01 00:00:00").lte("2018-08-13 00:00:00"));
        r.addAggregation(AggregationBuilders.sum("educateAmount").field("cf.educate_amount"));
        r.addAggregation(AggregationBuilders.sum("loanAmount").field("cf.loan_amount"));

        r.setQuery(boolQuery).setSize(0);
        SearchResponse s = r.get();
        long a = r.get().getHits().totalHits();

        System.out.println(a);
    }

    private static String[] orderFetch =
            {"cf.call_legion_name", "cf.call_group_name", "cf.call_seat_em", "cf.call_seat_id", "cf.call_seat_name", "cf.serial_no", "cf.student_id", "cf.username", "cf.mobile", "cf.payment_date", "cf.payment_time", "cf.educate_amount", "cf.school_name", "cf.first_proj_name", "cf.sec_proj_name", "cf.third_proj_name", "cf.status_code"};

    private static Map<String, String> busiNameMap = new HashMap<>();

    static {
        busiNameMap.put("", "");
    }

    private static final String[] STATUS_CODE = {"PAID", "PRODCHANGED", "CANCELED", "FREEZED", "STUCHANGED", "EXPIRED", "PARTIAL_PAY", "PRODUCT_CHANGED", "STU_CHANGED", "ORD_TERM", "POSTPONED", "FROZEN", "FINISHED"};

    public void get5() {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        SearchRequestBuilder r = client.prepareSearch("f_mid_order_details");
        r.setFetchSource(orderFetch, null);
        boolQuery.filter(QueryBuilders.termQuery("cf.delete_flag.raw", 0));
        boolQuery.must(
                QueryBuilders.boolQuery().should(QueryBuilders.termQuery("cf.call_business_id.raw", "")).should(QueryBuilders.boolQuery().mustNot(QueryBuilders.existsQuery("cf.call_business_id.raw"))));
//        boolQuery.should(QueryBuilders.boolQuery().mustNot(QueryBuilders.existsQuery("cf.call_business_id.raw")));
        boolQuery.filter(QueryBuilders.rangeQuery("cf.payment_time").gte("2018-09-01 00:00:00").lte("2018-10-03 00:00:00"));
//        boolQuery.filter(QueryBuilders.termsQuery("cf.status_code.raw", STATUS_CODE));
        r.setQuery(boolQuery).setSize(10000);
        r.setScroll(new TimeValue(5L, TimeUnit.SECONDS));
        SearchResponse s = r.get();
        long a = r.get().getHits().totalHits();

        String scrollId = s.getScrollId();
        String projFirst = "一级项目";
        String projSec = "二级项目";
        while (s.getHits().getHits().length >= RES_MAX_COUNT) {
            for (SearchHit hit : s.getHits().getHits()) {
                Map<String, Object> source = hit.getSource();
                Map<String, Object> map = (Map<String, Object>) source.get("cf");
                String callLegionName = (String) map.get("call_legion_name");
                String callGroupName = (String) map.get("call_group_name");
                String callSeatEm = (String) map.get("call_seat_em");
                String callSeatId = (String) map.get("call_seat_id");
                String callSeatName = (String) map.get("call_seat_name");
                String serialNo = (String) map.get("serial_no");
                String studentId = (String) map.get("student_id");
                String userName = (String) map.get("username");
                String mobile = (String) map.get("mobile");
                String paymentDate = (String) map.get("payment_date");
                String paymentTime = (String) map.get("payment_time");
                String eudcateAmount = (String) map.get("educate_amount");
                String schoolName = (String) map.get("school_name");
                String firstPojName = (String) map.get("first_proj_name");
                String secondProjName = (String) map.get("sec_proj_name");
                String thirdProjName = (String) map.get("third_proj_name");
                String statusCode = (String) map.get("status_code");

                System.out.println(
                                  (callLegionName == null ? "" : callLegionName) + ","
                                + (callGroupName == null ? "" : callGroupName) + ","
                                + (callSeatEm == null ? "" : callSeatEm) + ","
                                + (callSeatId == null ? "" : callSeatId) + ","
                                + (callSeatName == null ? "" : callSeatName) + ","
                                + (serialNo == null ? "" : serialNo) + ","
                                + (studentId == null ? "" : studentId) + ","
                                + (userName == null ? "" : userName) + ","
                                + (mobile == null ? "" : mobile) + ","
                                + (paymentDate == null ? "" : paymentDate) + ","
                                + (paymentTime == null ? "" : paymentTime) + ","
                                + (eudcateAmount == null ? "" : eudcateAmount) + ","
                                + (schoolName == null ? "" : schoolName) + ","
                                + (firstPojName == null ? "" : firstPojName) + ","
                                + (projFirst) + ","
                                + (secondProjName == null ? "" : secondProjName) + ","
                                + (projSec) + ","
                                + (thirdProjName == null ? "" : thirdProjName) + ","
                                + (statusCode == null ? "" : statusCode)
                );
            }
            SearchScrollRequestBuilder sb = client.prepareSearchScroll(scrollId);
            sb.setScroll(new TimeValue(5L, TimeUnit.SECONDS));
            s = sb.get();
        }
        for (SearchHit hit : s.getHits().getHits()) {
            Map<String, Object> source = hit.getSource();
            Map<String, Object> map = (Map<String, Object>) source.get("cf");
            String callLegionName = (String) map.get("call_legion_name");
            String callGroupName = (String) map.get("call_group_name");
            String callSeatEm = (String) map.get("call_seat_em");
            String callSeatId = (String) map.get("call_seat_id");
            String callSeatName = (String) map.get("call_seat_name");
            String serialNo = (String) map.get("serial_no");
            String studentId = (String) map.get("student_id");
            String userName = (String) map.get("username");
            String mobile = (String) map.get("mobile");
            String paymentDate = (String) map.get("payment_date");
            String paymentTime = (String) map.get("payment_time");
            String eudcateAmount = (String) map.get("educate_amount");
            String schoolName = (String) map.get("school_name");
            String firstPojName = (String) map.get("first_proj_name");
            String secondProjName = (String) map.get("sec_proj_name");
            String thirdProjName = (String) map.get("third_proj_name");
            String statusCode = (String) map.get("status_code");

            System.out.println(
                    (callLegionName == null ? "" : callLegionName) + ","
                            + (callGroupName == null ? "" : callGroupName) + ","
                            + (callSeatEm == null ? "" : callSeatEm) + ","
                            + (callSeatId == null ? "" : callSeatId) + ","
                            + (callSeatName == null ? "" : callSeatName) + ","
                            + (serialNo == null ? "" : serialNo) + ","
                            + (studentId == null ? "" : studentId) + ","
                            + (userName == null ? "" : userName) + ","
                            + (mobile == null ? "" : mobile) + ","
                            + (paymentDate == null ? "" : paymentDate) + ","
                            + (paymentTime == null ? "" : paymentTime) + ","
                            + (eudcateAmount == null ? "" : eudcateAmount) + ","
                            + (schoolName == null ? "" : schoolName) + ","
                            + (firstPojName == null ? "" : firstPojName) + ","
                            + (projFirst) + ","
                            + (secondProjName == null ? "" : secondProjName) + ","
                            + (projSec) + ","
                            + (thirdProjName == null ? "" : thirdProjName) + ","
                            + (statusCode == null ? "" : statusCode)
            );
        }

        System.out.println(a);
    }

    private static String[] logfetch = new String[] {"cf.appVersion","cf.actionTime","cf.city","cf.userAuth","cf.source","cf.type","cf.deviceId","cf.province","cf.osVersion","cf.provider","cf.broadcastId","cf.action","cf.courseId","cf.addition","cf.netState","cf.os","cf.netType","cf.ip","cf.userAgent","cf.updateTime","cf.userId","cf.appChannel","cf.actionType","cf.createTime","cf.deviceModel"};
    private static String[] userId = new String[] {"1620262","1624141","1502698","967438","1597576","1115345","1622167","1516978","781212","1516934","1527292","1597584","1593058","1602517","1438162","362907","1629390","1516879","1476842","907458","1572111","1609084","1554666","1623576","1627532","1556899","1612307","899324","290037","1532074","1542027","1102280","1166546","1474375","1526724","1636938","858731","1581497","1632622","360018","1099687","1518535","1556273","1619431","1543187","1597512","262197","727161","828849","1453893","1091295","285454","650619","1142344","1497524","1586958","933897","1628147","1615282","354586","819069","821724","1487309","1630016","1542163","1559791","1636639","141331","693347","693445","704011","735896","878638","1524933","1559423","1575904","372504","940812","1113622","1448173","1524852","1528889","1591608","1598789","1621653","1630061","203015","420115","649378","740522","1451486","1483623","1486769","1511385","1609407","1626705","245820","780662","1473740","1496231","1584030","1584697","1603554","366854","1435765","1444453","1518352","1570799","1595708","1613210","1623664","1629030","98434","254865","356500","685232","1469344","1473459","1500941","1524773","1536853","1540511","1545488","1559163","1589982","1595041","1606527","1612947","1614645","1625578","1634252","1634348","1595337","1595502","1595513","1595659","1595886","1595962","1596450","1596486","1596620","1596637","1596674","1596707","1596718","1596738","1596747","1596878","1596947","1597119","1597178","1597246","1597301","1597541","1597580","1597605","1597944","1598045","1598932","1598940","1599094","1599164","1599168","1599216","1599431","1599474","1599511","1599615","1600002","1600016","1600187","1600237","1600283","1600412","1600481","1600992","1601047","1601307","1601570","1601599","1601822","1601925","1601981","1602553","1602589","1602671","1602850","1602874","1602902","1603057","1603100","1603289","1603722","1603853","1604045","1604167","1604332","1604378","1604469","1604563","1604764","1604864","1605022","1605230","1605446","1605672","1605740","1606315","1606637","1606733","1606816","1606864","1607273","1607557","1607602","1607648","1607761","1607766","1607835","1607877","1607907","1608137","1608356","1608528","1608639","1608798","1608803","1609094","1609131","1609444","1609995","1610081","1610121","1610286","1610347","1610487","1610569","1610865","1610912","1610922","1611055","1611094","1611176","1611450","1611491","1611729","1611825","1612093","1612126","1612218","1612452","1612559","1612712","1612728","1613284","1613388","1613464","1614102","1614116","1614143","1614273","1614274","1614389","1614407","1614744","1615158","1615322","1615402","1615523","1615524","1615683","1615783","1615820","1616083","1616125","1616218","1616378","1616543","1616691","1616756","1616875","1616964","1617024","1617162","1617262","1617395","1617778","1618044","1618084","1618117","1618148","1618443","1618512","1618663","1618750","1618793","1618981","1619213","1619371","1619381","1619529","1619630","1619853","1619919","1619931","1619953","1620259","1620322","1621056","1621073","1621203","1621308","1621453","1621478","1621531","1621750","1621850","1622061","1622074","1622085","1622229","1622295","1622316","1622650","1622749","1623344","1623355","1623399","1623476","1623643","1623825","1624018","1624233","1624236","1624414","1624569","1624984","1625046","1625218","1625273","1625408","1625580","1625625","1625775","1625861","1625879","1626342","1626596","1626783","1627227","1627347","1627416","1627459","1627540","1627549","1627645","1627871","1628023","1628122","1628240","1628326","1628399","1628416","1628722","1628965","1628967","1629074","1629301","1629447","1629849","1630317","1630586","1630728","1630986","1631325","1631340","1631515","1631576","1631624","1631891","1631987","1632187","1632552","1632712","1632734","1632850","1632872","1632996","1633049","1633125","1633142","1633763","1633839","1633916","1633937","1634239","1634356","1634576","1634614","1634652"};
    public void get6() {
        System.out.println("appVersion,actionTime,city,userAuth,source,type,deviceId,province,osVersion,provider,broadcastId,action,courseId,addition,netState,os,netType,ip,userAgent,updateTime,userId,appChannel,actionType,createTime,deviceModel");
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        SearchRequestBuilder r = client.prepareSearch("app_user_course-2018-10");
        r.setFetchSource(logfetch, null);
        boolQuery.filter(QueryBuilders.termsQuery("cf.userId.raw", userId));
        boolQuery.filter(QueryBuilders.rangeQuery("cf.actionTime").gte("2018-10-01 00:00:00").lte("2018-10-10 00:00:00"));
        r.setQuery(boolQuery).setSize(10000);
        r.setScroll(new TimeValue(5L, TimeUnit.SECONDS));
        SearchResponse s = r.get();
        long a = r.get().getHits().totalHits();

        String scrollId = s.getScrollId();
        while (s.getHits().getHits().length >= RES_MAX_COUNT) {
            for (SearchHit hit : s.getHits().getHits()) {
                Map<String, Object> source1 = hit.getSource();
                Map<String, Object> map = (Map<String, Object>) source1.get("cf");
                String appVersion = (String) map.get("appVersion");
                String actionTime = (String) map.get("actionTime");
                String city = (String) map.get("city");
                String userAuth = (String) map.get("userAuth");
                String source = (String) map.get("source");
                String type = (String) map.get("type");
                String deviceId = (String) map.get("deviceId");
                String province = (String) map.get("province");
                String osVersion = (String) map.get("osVersion");
                String provider = (String) map.get("provider");
                String broadcastId = (String) map.get("broadcastId");
                String action = (String) map.get("action");
                String courseId = (String) map.get("courseId");
                String addition = (String) map.get("addition");
                String netState = (String) map.get("netState");
                String os = (String) map.get("os");
                String netType = (String) map.get("netType");
                String ip = (String) map.get("ip");
                String userAgent = (String) map.get("userAgent");
                String updateTime = (String) map.get("updateTime");
                String userId = (String) map.get("userId");
                String appChannel = (String) map.get("appChannel");
                String actionType = (String) map.get("actionType");
                String createTime = (String) map.get("createTime");
                String deviceModel = (String) map.get("deviceModel");

                System.out.println(
                                  (appVersion == null ? "" : appVersion) + ","
                                + (actionTime == null ? "" : actionTime) + ","
                                + (city == null ? "" : city) + ","
                                + (userAuth == null ? "" : userAuth) + ","
                                + (source == null ? "" : source) + ","
                                + (type == null ? "" : type) + ","
                                + (deviceId == null ? "" : deviceId) + ","
                                + (province == null ? "" : province) + ","
                                + (osVersion == null ? "" : osVersion) + ","
                                + (provider == null ? "" : provider) + ","
                                + (broadcastId == null ? "" : broadcastId) + ","
                                + (action == null ? "" : action) + ","
                                + (courseId == null ? "" : courseId) + ","
                                + (addition == null ? "" : addition) + ","
                                + (netState == null ? "" : netState) + ","
                                + (os == null ? "" : os) + ","
                                + (netType == null ? "" : netType) + ","
                                + (ip == null ? "" : ip)+","
                                          + (userAgent == null ? "" : userAgent) + ","
                                          + (updateTime == null ? "" : updateTime) + ","
                                          + (userId == null ? "" : userId) + ","
                                          + (appChannel == null ? "" : appChannel) + ","
                                          + (actionType == null ? "" : actionType) + ","
                                          + (createTime == null ? "" : createTime) + ","
                                          + (deviceModel == null ? "" : deviceModel)
                );
            }
            SearchScrollRequestBuilder sb = client.prepareSearchScroll(scrollId);
            sb.setScroll(new TimeValue(5L, TimeUnit.SECONDS));
            s = sb.get();
        }
        for (SearchHit hit : s.getHits().getHits()) {
            Map<String, Object> source1 = hit.getSource();
            Map<String, Object> map = (Map<String, Object>) source1.get("cf");
            String appVersion = (String) map.get("appVersion");
            String actionTime = (String) map.get("actionTime");
            String city = (String) map.get("city");
            String userAuth = (String) map.get("userAuth");
            String source = (String) map.get("source");
            String type = (String) map.get("type");
            String deviceId = (String) map.get("deviceId");
            String province = (String) map.get("province");
            String osVersion = (String) map.get("osVersion");
            String provider = (String) map.get("provider");
            String broadcastId = (String) map.get("broadcastId");
            String action = (String) map.get("action");
            String courseId = (String) map.get("courseId");
            String addition = (String) map.get("addition");
            String netState = (String) map.get("netState");
            String os = (String) map.get("os");
            String netType = (String) map.get("netType");
            String ip = (String) map.get("ip");
            String userAgent = (String) map.get("userAgent");
            String updateTime = (String) map.get("updateTime");
            String userId = (String) map.get("userId");
            String appChannel = (String) map.get("appChannel");
            String actionType = (String) map.get("actionType");
            String createTime = (String) map.get("createTime");
            String deviceModel = (String) map.get("deviceModel");

            System.out.println(
                    (appVersion == null ? "" : appVersion) + ","
                            + (actionTime == null ? "" : actionTime) + ","
                            + (city == null ? "" : city) + ","
                            + (userAuth == null ? "" : userAuth) + ","
                            + (source == null ? "" : source) + ","
                            + (type == null ? "" : type) + ","
                            + (deviceId == null ? "" : deviceId) + ","
                            + (province == null ? "" : province) + ","
                            + (osVersion == null ? "" : osVersion) + ","
                            + (provider == null ? "" : provider) + ","
                            + (broadcastId == null ? "" : broadcastId) + ","
                            + (action == null ? "" : action) + ","
                            + (courseId == null ? "" : courseId) + ","
                            + (addition == null ? "" : addition) + ","
                            + (netState == null ? "" : netState) + ","
                            + (os == null ? "" : os) + ","
                            + (netType == null ? "" : netType) + ","
                            + (ip == null ? "" : ip)+","
                            + (userAgent == null ? "" : userAgent) + ","
                            + (updateTime == null ? "" : updateTime) + ","
                            + (userId == null ? "" : userId) + ","
                            + (appChannel == null ? "" : appChannel) + ","
                            + (actionType == null ? "" : actionType) + ","
                            + (createTime == null ? "" : createTime) + ","
                            + (deviceModel == null ? "" : deviceModel)
            );
        }

        System.out.println(a);
    }


    private static String[] busiFetch =
            {"cf.educate_amount","cf.serial_no","cf.call_seat_em"};
    public void get8(String aa) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        SearchRequestBuilder r = client.prepareSearch("f_mid_order_details");
        r.setFetchSource(busiFetch, null);
        boolQuery.filter(QueryBuilders.termQuery("cf.delete_flag.raw", 0));
        boolQuery.filter(QueryBuilders.existsQuery("cf.call_seat_em.raw"));
        boolQuery.filter(QueryBuilders.termsQuery("cf.call_seat_em.raw",aa.split(",")));
        boolQuery.filter(QueryBuilders.rangeQuery("cf.payment_time").gte("2018-08-01 00:00:00").lte("2018-08-31 23:59:59"));
        boolQuery.filter(QueryBuilders.termsQuery("cf.status_code.raw", STATUS_CODE));
        r.setQuery(boolQuery).setSize(10000);
        r.setScroll(new TimeValue(5L, TimeUnit.SECONDS));
        r.addAggregation(AggregationBuilders.sum("amount").field("cf.educate_amount"));
        SearchResponse s = r.get();
        long a = r.get().getHits().totalHits();
        String scrollId = s.getScrollId();
        while (s.getHits().getHits().length >= RES_MAX_COUNT) {
            for (SearchHit hit : s.getHits().getHits()) {
                Map<String, Object> source = hit.getSource();
                Map<String, Object> map = (Map<String, Object>) source.get("cf");
                String serial_no = (String) map.get("educate_amount");
                String call_business_id = (String) map.get("serial_no");
                String call_business_name = (String) map.get("call_seat_em");

                System.out.println(
                        (serial_no == null ? "" : serial_no) + ","
                                + (call_business_id == null ? "" : call_business_id) + ","
                                + (call_business_name == null ? "" : call_business_name)
                );
            }
            SearchScrollRequestBuilder sb = client.prepareSearchScroll(scrollId);
            sb.setScroll(new TimeValue(5L, TimeUnit.SECONDS));
            s = sb.get();
        }
        for (SearchHit hit : s.getHits().getHits()) {
            Map<String, Object> source = hit.getSource();
            Map<String, Object> map = (Map<String, Object>) source.get("cf");
            String serial_no = (String) map.get("educate_amount");
            String call_business_id = (String) map.get("serial_no");
            String call_business_name = (String) map.get("call_seat_em");

            System.out.println(
                    (serial_no == null ? "" : serial_no) + ","
                            + (call_business_id == null ? "" : call_business_id) + ","
                            + (call_business_name == null ? "" : call_business_name)
            );
        }

        System.out.println(a);
    }


    public void getWebEventLog(){
        MultiSearchRequestBuilder multiSearch = client.prepareMultiSearch();
        String systemId = "10007";
        String oppid = "34493389";
        int MAX_SIZE = 10000;
        String[] eventTypes = new String[]{"coupon_pay_success","coupon_to_using"};
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        {
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            SearchRequestBuilder r = client.prepareSearch("web_event_log");
            boolQuery.filter(QueryBuilders.termQuery("cf.system_id",systemId));
            boolQuery.filter(QueryBuilders.termQuery("cf.oppId.raw",oppid));
            TermsAggregationBuilder typeAggre = AggregationBuilders.terms("respAccount").field("cf.resp_account.raw").size(10000);
            typeAggre.subAggregation(AggregationBuilders.max("send_time").field("cf.send_time"));
            r.addAggregation(typeAggre);
            r.setFetchSource("cf.send_time", null).setSize(0);
            boolQuery.filter(QueryBuilders.termQuery("cf.event_id_str.raw","liveroom_heartbeat"));
            r.setQuery(boolQuery);
            multiSearch.add(r);
        }

        {
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            SearchRequestBuilder r = client.prepareSearch("f_mid_order_details");
            boolQuery.filter(QueryBuilders.termQuery("cf.call_seat_id","1500827"));
            TermsAggregationBuilder typeAggre = AggregationBuilders.terms("channel").field("cf.channel_code.raw").size(1000);
            typeAggre.subAggregation(AggregationBuilders.max("create_time").field("cf.create_time"));
            r.addAggregation(typeAggre);
            r.setFetchSource("cf.create_time", null);
            r.setQuery(boolQuery);
            r.addSort("cf.create_time", SortOrder.DESC);
            multiSearch.add(r);
        }

        {
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            SearchRequestBuilder r = client.prepareSearch("web_event_log");
            boolQuery.filter(QueryBuilders.termQuery("cf.system_id",systemId));
            boolQuery.filter(QueryBuilders.termQuery("cf.oppId.raw",oppid));
            boolQuery.filter(QueryBuilders.termsQuery("cf.event_id_str.raw",eventTypes));
            r.setQuery(boolQuery);
            r.setSize(MAX_SIZE);
            multiSearch.add(r);
        }

        {
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            SearchRequestBuilder r = client.prepareSearch("f_stu_live_teach_unit");
            boolQuery.filter(QueryBuilders.termsQuery("ord_id",new String [] {"5885889"}));
            r.setQuery(boolQuery);
            multiSearch.add(r);
        }

        MultiSearchResponse.Item[] items = multiSearch.get().getResponses();

        SearchResponse response0 = items[0].getResponse();
        long heartCount = response0.getHits().totalHits();
        String sendTime = null;
        for (SearchHit hit : response0.getHits().getHits()) {
            Map<String, Object> source = hit.getSource();
            Map<String, Object> map = (Map<String, Object>) source.get("cf");
            sendTime = (String) map.get("send_time");
        }

        SearchResponse response1 = items[1].getResponse();
        Aggregations aggregations = response1.getAggregations();
        Terms typeTerms = aggregations.get("channel");
        for(Terms.Bucket bucket : typeTerms.getBuckets()){
            Long a = bucket.getDocCount();
            Max max = bucket.getAggregations().get("create_time");
            String time = max.getValueAsString();
            System.out.println(a+time);
        }

    }



}

