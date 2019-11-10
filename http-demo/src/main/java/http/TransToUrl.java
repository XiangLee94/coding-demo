package http;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

public class TransToUrl {
    public static void main(String[] args) {
        OkHttpClientUtils okHttpClientUtils = OkHttpClientUtils.getInstance();
        File file = new File(args[0]);
        BufferedReader reader = null;
        String temp = null;
        int line = 1;
        int a = 10000;
        try {
            reader = new BufferedReader(new FileReader(file));
            while ((temp = reader.readLine()) != null) {
                a ++;
                line++;
                try {
                    String device  = "";
                    Dto dto = JSON.parseObject(temp,Dto.class);
                    switch (dto.getiDevice()){
                        case 1:device = "AndroidApp";break;
                        case 2:device = "IosApp";break;
                        case 3:device = "Web";break;
                    }

                    String live = dto.getbIsLive() ? "live" :"video";
                    StringBuilder url  = new StringBuilder("http://datacenter.sunlands.com/datacenter_web/dataService/webBaseData.jpg?eid=user_attendance&sid=10012&uid=");
                    url.append(dto.getlUserId()).append("&r=").append(a).append("&stype=4&ps=watchDuration=").append(dto.getlDuration())
                    .append(",enterTimestamp=").append(dto.getlBeginTimestamp()).append(",leaveTimestamp=").append(dto.getlEndTimestamp()).append(",device=")
                    .append(device).append(",liveId=").append(dto.getlLiveId()).append(",type=").append(live).append(",ip=").append(dto.getsIp()).append(",userAgent=").append(dto.getsUserAgent())
                    .append(",location=UnKnown");
                    System.out.println(url.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("处理条数" + line);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getRealUrl(String str) {
        try {
            int index = str.indexOf("?");
            if (index < 0) return str;
            String query = str.substring(0, index);
            String params = str.substring(index + 1);
            Map<String, String> map = GetArgs(params);
            //Map map=TransStringToMap(params);
            String encodeParams = TransMapToString(map);
            return query + "?" + encodeParams;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return "";
    }

    //将url参数格式转化为map
    public static Map GetArgs(String params) throws Exception {
        Map<String, String> map = new HashMap();
        String[] pairs = params.split("&");
        for (int i = 0; i < pairs.length; i++) {
            int pos = pairs[i].indexOf("=");
            if (pos == -1) continue;
            String argname = pairs[i].substring(0, pos);
            String value = pairs[i].substring(pos + 1);
            value = URLEncoder.encode(value, "utf-8");
            map.put(argname, value);
        }
        return map;
    }

    //将map转化为指定的String类型
    public static String TransMapToString(Map map) {
        Map.Entry entry;
        StringBuffer sb = new StringBuffer();
        for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
            entry = (Map.Entry) iterator.next();
            sb.append(entry.getKey().toString()).append("=").append(null == entry.getValue() ? "" :
                    entry.getValue().toString()).append(iterator.hasNext() ? "&" : "");
        }
        return sb.toString();
    }

    //将String类型按一定规则转换为Map
    public static Map TransStringToMap(String mapString) {
        Map<String, String> map = new HashMap();
        StringTokenizer items;
        for (StringTokenizer entrys = new StringTokenizer(mapString, "&"); entrys.hasMoreTokens();
             map.put(items.nextToken(), items.hasMoreTokens() ? (items.nextToken()) : null))
            items = new StringTokenizer(entrys.nextToken(), "=");
        return map;
    }
}
