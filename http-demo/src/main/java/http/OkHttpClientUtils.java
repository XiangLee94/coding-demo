package http;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import okhttp3.*;
import org.apache.http.HttpStatus;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OkHttpClientUtils {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OkHttpClientUtils.class);

    private static OkHttpClientUtils ins;
    private static Integer TIMEOUT_SECONDS = 15;

    private OkHttpClient client;

    private OkHttpClientUtils() {
        if (client == null) {
            client = new OkHttpClient.Builder().connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS).build();
        }
    }

    public static OkHttpClientUtils getInstance() {
        if (ins == null) {
            synchronized (OkHttpClientUtils.class) {
                if (ins == null) {
                    ins = new OkHttpClientUtils();
                }
            }
        }
        return ins;
    }

    /**
     * 返回一个单例Client，使用默认参数
     *
     * @return
     */

    public OkHttpClient getClient() {
        return client;
    }

    /**
     * 返回一个衍生的可自定义的client，可定制连接超时时间，读取/写入超时时间
     *
     * @param connectTimeOutMilliseconds
     * @param readTimeOutMilliseconds
     * @param writeTimeOutMilliseconds
     * @return
     */

    public OkHttpClient getNewClient(int connectTimeOutMilliseconds, int readTimeOutMilliseconds,
                                     int writeTimeOutMilliseconds) {
        return client.newBuilder()
                .connectTimeout(connectTimeOutMilliseconds, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeOutMilliseconds, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeOutMilliseconds, TimeUnit.MILLISECONDS).build();
    }

    /**
     * 返回一个衍生的client构造器，可自定义参数
     *
     * @return
     */
    public OkHttpClient.Builder getNewClientBuilder() {
        return client.newBuilder();
    }

    /**
     * 带query string 的get请求
     *
     * @param httpClient 自定义参数的OkHttpClient
     * @param uri
     * @param queryMap   封装query参数
     * @return
     */
    public String doGetByQueryString(OkHttpClient httpClient, String uri,
                                     Map<String, String> queryMap) {
        log.info("doGetByQueryString [{}] param [{}] - start", uri, queryMap);
        String retString = "";
        HttpUrl.Builder builder = HttpUrl.parse(uri).newBuilder();
        for (String key : queryMap.keySet()) {
            builder.addQueryParameter(key, queryMap.get(key));
        }
        HttpUrl httpUrl = builder.build();
        Request request = new Request.Builder().url(httpUrl).build();
        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            if (response.code() == HttpStatus.SC_OK) {
                retString = response.body().string();
            } else {
                log.error("doGetByQueryString bad response [{}] errorMessage [{}] url [{}] param [{}]", response.code(), response.message(), uri, queryMap);
                return retString;
            }
        } catch (IOException e) {
            log.error("doGetByQueryString [{}] param [{}] errorMessage [{}]", uri, queryMap, e);
            return retString;
        }
        log.debug("doGetByQueryString code [{}] body [{}]", response.code(), retString);
        log.info("doGetByQueryString [{}] - end", uri);
        return retString;
    }

    /**
     * 带query string 的get请求
     * 使用默认的OkHttpClient
     *
     * @param uri
     * @param queryMap 封装query参数
     * @return
     */
    public String doGetByQueryString(String uri, Map<String, String> queryMap) {
        return doGetByQueryString(client, uri, queryMap);
    }

    /**
     * get请求
     *
     * @param httpClient 自定义参数的OkHttpClient
     * @param uri
     * @return
     */
    public String doGet(OkHttpClient httpClient, String uri) {
        log.info("doGet [{}] - start ", uri);
        String retString = "";
        Request request = new Request.Builder().url(uri).build();
        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            if (response.code() == HttpStatus.SC_OK) {
                retString = response.body().string();
            } else {
                log.error("doGet bad response [{}] errorMessage [{}] url [{}]", response.code(), response.message(), uri);
                return retString;
            }
        } catch (IOException e) {
            log.error("doGet [{}] error [{}]", uri, e);
            return retString;
        }
        log.debug("doGet code [{}] body [{}]", response.code(), retString);
        log.info("doGet [{}] - end", uri);
        return retString;
    }

    /**
     * get请求 使用默认的OkHttpClient
     *
     * @param uri
     * @return
     */
    public String doGet(String uri) {
        return doGet(client, uri);
    }

    /**
     * Post请求
     * MediaType：application/json
     *
     * @param httpClient 自定义参数的OkHttpClient
     * @param uri
     * @param param
     * @return
     */
    public String doPostWithJson(OkHttpClient httpClient, String uri,
                                 Map<String, Object> param) {
        log.info("doPostWithJson [{}] param [{}] - start", uri, param);
        String retString = "";
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, JSONObject.toJSONString(param));
        Request request = new Request.Builder().url(uri).post(body).build();
        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            if (response.code() == HttpStatus.SC_OK) {
                retString = response.body().string();
            } else {
                log.error("doPostWithJson bad response [{}] errorMessage [{}] url [{}] param [{}]",
                        response.code(), response.message(), uri, param);
                return retString;
            }

        } catch (IOException e) {
            log.error("doPostWithJson [{}] param [{}] errorMessage [{}]", uri, param, e);
            return retString;
        }
        log.debug("doPostWithJson code [{}] body [{}]", response.code(), retString);
        log.info("doPostWithJson [{}] - end", uri);
        return retString;
    }

    public String doPostWithJson(String uri, Map<String, Object> param) {
        return doPostWithJson(client, uri, param);
    }

    /**
     * From 表单 Post请求
     * MediaType：application/x-www-form-urlencoded
     *
     * @param httpClient 自定义参数的OkHttpClient
     * @param uri
     * @param param
     * @return
     */
    public String doPostForm(OkHttpClient httpClient, String uri,
                             Map<String, String> param) {
        log.info("doPostForm [{}] param [{}] - start", uri, param);
        String retString = "";

        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        for (String key : param.keySet()) {
            formBodyBuilder.add(key, param.get(key).toString());
        }
        FormBody formBody = formBodyBuilder.build();
        Request request = new Request.Builder().url(uri).post(formBody).build();
        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            if (response.code() == HttpStatus.SC_OK) {
                retString = response.body().string();
            } else {
                log.error("doPostForm bad response [{}] errorMessage [{}] url [{}] param [{}]",
                        response.code(), response.message(), uri, param);
                return retString;
            }

        } catch (IOException e) {
            log.error("doPostForm [{}] param [{}] errorMessage [{}]", uri, param, e);
            return retString;
        }
        log.debug("doPostForm code [{}] body [{}]", response.code(), retString);
        log.info("doPostForm [{}] - end ", uri);
        return retString;
    }

    /**
     * From 表单 Post请求
     * MediaType：application/x-www-form-urlencoded
     * 使用默认的OkHttpClient
     *
     * @param uri
     * @param param
     * @return
     */
    public String doPostForm(String uri, Map<String, String> param) {
        return doPostForm(client, uri, param);
    }


    /**
     * Post请求
     * MediaType：application/json
     *
     * @param httpClient 自定义参数的OkHttpClient
     * @param uri
     * @param paramJson
     * @return
     */
    public String doPostWithJsonResult(OkHttpClient httpClient, String uri,
                                       String paramJson) {
        log.info("doPostWithJsonResult [{}] param [{}] - start", uri, paramJson);
        String retString = "";
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, paramJson);
        Request request = new Request.Builder().url(uri).post(body).build();
        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            if (response.code() == HttpStatus.SC_OK) {
                retString = response.body().string();
            } else {
                log.error("doPostWithJsonResult bad response [{}] errorMessage [{}] url [{}] param [{}]",
                        response.code(), response.message(), uri, paramJson);
                return retString;
            }

        } catch (IOException e) {
            log.error("doPostWithJsonResult [{}] param [{}] errorMessage [{}]", uri, paramJson, e);
            return retString;
        }
        log.debug("doPostWithJsonResult code [{}] body [{}]", response.code(), retString);
        log.info("doPostWithJsonResult [{}] - end ", uri);
        return retString;
    }

    /**
     * Post请求
     * MediaType：application/json
     *
     * @param uri
     * @param paramJson
     * @return
     */
    public String doPostWithJsonResult(String uri, String paramJson) {
        return doPostWithJsonResult(client, uri, paramJson);
    }

    public String post(String url, Map<String, String> params) throws IOException {
        log.info("post [{}] param [{}] - start", url, params);
        FormBody.Builder builder = new FormBody.Builder();

        for (String key : params.keySet()) {
            builder.add(key, params.get(key));
        }

        FormBody body = builder.build();

        Request request = new Request.Builder().url(url).post(body).build();


        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();

        } else {
            throw new IOException("Unexpected code " + response);
        }
    }
}
