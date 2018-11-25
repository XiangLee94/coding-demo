package http;

import javax.annotation.Resource;

public class HttpTest {

    private  OkHttpClientUtils okHttpClientUtils = OkHttpClientUtils.getInstance();

    public static void main(String[] args) {
        HttpTest httpTest = new HttpTest();
        String res = httpTest.okHttpClientUtils.doGet("http://192.168.215.3/test-springboot/test");
        System.out.println(res);
    }
}
