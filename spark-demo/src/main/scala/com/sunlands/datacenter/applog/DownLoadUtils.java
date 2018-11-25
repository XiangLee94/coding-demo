package com.sunlands.datacenter.applog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DownLoadUtils {

    public static List<String> downLoadByHttpConn(String path) throws IOException {
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        List<String> lines = new ArrayList<>();
        try {
            System.out.println("地址是" + path);
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(20 * 1000);
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            inputStream = conn.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
                System.out.println(line);
            }
        }catch (Exception e){
            System.out.println("下载文件出错");
            e.printStackTrace();
        }finally {
            try{
                if(bufferedReader != null ) bufferedReader.close();
                if(inputStreamReader != null) inputStreamReader.close();
                if(inputStream != null) inputStream.close();
            }catch (Exception e){
                System.out.println("关闭流出错");
                e.printStackTrace();
            }

        }
        return lines;
    }
    public static  List<DataObject> transFormToObject(List<String> lines){
        List<DataObject> objects = new ArrayList<>(lines.size());
        String[] lineArr = null;
        for(String line : lines){
            lineArr = line.split(",");
            DataObject dataObject = new DataObject(lineArr);
            objects.add(dataObject);
        }
        return objects;
    }
}
