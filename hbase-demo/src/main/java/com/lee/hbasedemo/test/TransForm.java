package com.lee.hbasedemo.test;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;


@Component("TransForm")
public class TransForm implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Autowired
    private JdbcTemplate datacenterDao;

    public List<WebLogDTO> getDataList(){
        List<WebLogDTO> list = datacenterDao.query("SELECT * from web_event_log_201812 where create_time >= '2018-12-10 21:00:00' and create_time < '2018-12-10 21:30:00';",new WebLogMapper());

        return list;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(TransForm.applicationContext == null){
            TransForm.applicationContext = applicationContext;
        }
    }

    public void putIntoHbase(List<WebLogDTO> list){
        int count = 0;
        for(WebLogDTO weblog : list){
            try {
                Table htable = HbaseDAO.connection.getTable(TableName.valueOf("web_event_log"));
                String familyName = "cf";
                String rowKey = weblog.getId();

                Put put = new Put(Bytes.toBytes(rowKey));
                if(weblog.getUnfixed_param() != null) {
                    String[] params = weblog.getUnfixed_param().split(",");
                    for(int i=0; i<params.length; i++) {
                        String param = params[i];
                        if(StringUtils.isNotEmpty(param)) {
                            String[] unfixParams = param.split("=");
                            if(null != unfixParams && unfixParams.length>1) {
                                put.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(unfixParams[0]), Bytes.toBytes( URLDecoder.decode(unfixParams[1], "UTF-8")));
                            }else {
//                                System.out.println("web_event_log  unfixparam error:"+ param);
                            }
                        }
                    }
                }
              if(!put.isEmpty()) {
                  List<Put> listPuts = Arrays.asList(put);
                  htable.put(listPuts);
                  count++;
                  System.out.println("Insert:      "+rowKey);
              }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        System.out.println("入库数"+count);

    }

    public void transForm(){
        List<WebLogDTO> list = getDataList();
        putIntoHbase(list);
        System.out.println("mysql查询到"+list.size());
    }


    private static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }

}
