

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

public class DownloadFile {
    public static void main(String[] args) throws IOException {
        downLoadByHttpConn();
    }

    public static void downLoadByHttpConn() throws IOException{
        URL url = new URL("http://store.sunlands.com/original/20180606/1528286403593.csv");
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(3*1000);
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        InputStream inputStream = conn.getInputStream();
        List<String> lines = new ArrayList<>();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = null;
        while((line =  bufferedReader.readLine()) !=null){
            lines.add(line);
            System.out.println(line);
        }
        bufferedReader.close();
        inputStream.close();
    }

    public void downLoadByChannel() throws IOException{
        URL website = new URL("http://www.website.com/information.asp");
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());

//        FileOutputStream fos = new FileOutputStream("information.html");
//        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    }
}
