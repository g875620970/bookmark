package com.clearbill.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTP 工具类
 *
 * @version 1.0 各方法未允许charset参数设置，默认均采用UTF-8,如有特殊需要可重载相应方法
 */
public class HttpClientUtil {

    private final static Logger log = LoggerFactory.getLogger(HttpClientUtil.class);

    /**
     * 发送HTTP_GET请求
     *
     * @param requestURL 请求地址(含参数)
     * @param headers    http请求header
     * @param timeout    http请求超时时限（毫秒）
     * @return 远程主机响应正文
     * @see 该方法会自动关闭连接,释放资源
     */
    public static String sendGetRequest(String reqURL, Map<String, String> headers, int timeout) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(reqURL);
        setHttpConfig(headers, timeout, httpGet);
        return executeHttpRequest(httpClient, httpGet);
    }

    /**
     * 发送HTTP_GET请求
     *
     * @param requestURL 请求地址(含参数)
     * @param headers    http请求header
     * @param timeout    http请求超时时限（毫秒）
     * @return 远程主机响应正文
     * @see 该方法会自动关闭连接,释放资源
     */
    public static String getRedirectUrl(String reqURL, Map<String, String> headers, int timeout) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(reqURL);
        //设置http请求超时时间,是否重定向,头信息
        if (timeout >= 0) {
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout).setSocketTimeout(timeout).setRedirectsEnabled(false).build();
            httpGet.setConfig(requestConfig);
        } else {
            RequestConfig requestConfig = RequestConfig.custom().setRedirectsEnabled(false).build();
            httpGet.setConfig(requestConfig);
        }
        if (headers != null) {
            for (String name : headers.keySet()) {
                httpGet.setHeader(name, headers.get(name));
            }
        }
        return getRedirectUrl(httpClient, httpGet);
    }

    /**
     * 发送HTTP_GET请求
     *
     * @param requestURL 请求地址(含参数)
     * @param headers    http请求header
     * @return 远程主机响应正文
     * @see 该方法会自动关闭连接,释放资源
     */
    public static String sendGetRequest(String reqURL, Map<String, String> headers) {
        return sendGetRequest(reqURL, headers, -1);
    }

    public static String getRedirectUrl(String reqURL, Map<String, String> headers) {
        return getRedirectUrl(reqURL, headers, -1);
    }

    /**
     * 发送HTTP_GET请求
     *
     * @param requestURL 请求地址(含参数)
     * @return 远程主机响应正文
     * @see 该方法会自动关闭连接,释放资源
     */
    public static String sendGetRequest(String reqURL) {
        return sendGetRequest(reqURL, null);
    }

    /**
     * 发送HTTP_POST请求
     *
     * @param reqURL   请求地址
     * @param sendData 请求参数
     * @param headers  http请求header
     * @param timeout  http请求超时时限（毫秒）
     * @return 远程主机响应正文
     * @see 该方法会自动关闭连接,释放资源
     */
    public static String sendPostRequest(String reqURL, String sendData, Map<String, String> headers, int timeout) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(reqURL);
        setHttpConfig(headers, timeout, httpPost);
        if (sendData != null && !"".equals(sendData)) {
            try {
                httpPost.setEntity(new StringEntity(sendData, "UTF-8"));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return executeHttpRequest(httpClient, httpPost);
    }

    /**
     * 发送HTTP_POST请求
     *
     * @param reqURL   请求地址
     * @param sendData 请求参数
     * @param headers  http请求header
     * @return 远程主机响应正文
     * @see 该方法会自动关闭连接,释放资源
     */
    public static String sendPostRequest(String reqURL, String sendData, Map<String, String> headers) {
        return sendPostRequest(reqURL, sendData, headers, -1);
    }

    /**
     * 发送HTTP_POST请求
     *
     * @param reqURL   请求地址
     * @param sendData 请求参数
     * @return 远程主机响应正文
     * @see 该方法会自动关闭连接,释放资源
     */
    public static String sendPostRequest(String reqURL, String sendData) {
        return sendPostRequest(reqURL, sendData, null);
    }

    /**
     * 发送HTTP_POST请求
     *
     * @param reqURL  请求地址
     * @param params  请求参数
     * @param headers http请求header
     * @param timeout http请求超时时限（毫秒）
     * @return 远程主机响应正文
     * @see 该方法会自动关闭连接,释放资源
     * @see 该方法会自动对<code>params</code>中的[中文][|][ ]等特殊字符进行
     * <code>URLEncoder.encode(string,encodeCharset)</code>
     */
    public static String sendPostRequest(String reqURL, Map<String, String> params, Map<String, String> headers, int timeout) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(reqURL);
        setHttpConfig(headers, timeout, httpPost);
        if (params != null && !params.isEmpty()) {
            List<NameValuePair> formParams = new ArrayList<NameValuePair>(); // 创建参数队列
            for (Map.Entry<String, String> entry : params.entrySet()) {
                formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8"));
            } catch (UnsupportedEncodingException e1) {
                log.error("httpPost 设置Entity error:", e1);
            }
        }
        return executeHttpRequest(httpClient, httpPost);
    }

    /**
     * 发送HTTP_POST请求
     *
     * @param reqURL  请求地址
     * @param params  请求参数
     * @param headers http请求header
     * @return 远程主机响应正文
     * @see 该方法会自动关闭连接,释放资源
     * @see 该方法会自动对<code>params</code>中的[中文][|][ ]等特殊字符进行
     * <code>URLEncoder.encode(string,encodeCharset)</code>
     */
    public static String sendPostRequest(String reqURL, Map<String, String> params, Map<String, String> headers) {
        return sendPostRequest(reqURL, params, headers, -1);
    }

    /**
     * 发送HTTP_POST请求
     *
     * @param reqURL 请求地址
     * @param params 请求参数
     * @return 远程主机响应正文
     * @see 该方法会自动关闭连接,释放资源
     * @see 该方法会自动对<code>params</code>中的[中文][|][ ]等特殊字符进行
     * <code>URLEncoder.encode(string,encodeCharset)</code>
     */
    public static String sendPostRequest(String reqURL, Map<String, String> params) {
        return sendPostRequest(reqURL, params, null);
    }

    /**
     * 发送HTTP_PUT请求
     *
     * @param reqURL   请求地址
     * @param sendData 发送到远程主机的正文数据
     * @param headers  请求头部
     * @param timeOut  失效时间
     * @return 远程主机响应正文`HTTP状态码,如<code>"SUCCESS`200"</code>若通信过程中发生异常则返回"Failed`HTTP状态码",如
     * <code>"Failed`500"</code>
     */
    public static String sendPutRequest(String reqURL, String sendData, Map<String, String> headers, int timeout) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(reqURL);
        setHttpConfig(headers, timeout, httpPut);
        if (sendData != null && !"".equals(sendData)) {
            try {
                httpPut.setEntity(new StringEntity(sendData, "UTF-8"));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return executeHttpRequest(httpClient, httpPut);
    }

    /**
     * 发送HTTP_PUT请求
     *
     * @param reqURL   请求地址
     * @param sendData 发送到远程主机的正文数据
     * @param headers  请求头部
     * @return 远程主机响应正文`HTTP状态码,如<code>"SUCCESS`200"</code>若通信过程中发生异常则返回"Failed`HTTP状态码",如
     * <code>"Failed`500"</code>
     */
    public static String sendPutRequest(String reqURL, String sendData, Map<String, String> headers) {
        return sendPutRequest(reqURL, sendData, headers, -1);
    }

    /**
     * 发送HTTP_PUT请求
     *
     * @param reqURL   请求地址
     * @param sendData 发送到远程主机的正文数据
     * @param headers  请求头部
     * @return 远程主机响应正文`HTTP状态码,如<code>"SUCCESS`200"</code>若通信过程中发生异常则返回"Failed`HTTP状态码",如
     * <code>"Failed`500"</code>
     */
    public static String sendPutRequest(String reqURL, String sendData) {
        return sendPutRequest(reqURL, sendData, null);
    }

    /**
     * 发送HTTP_PUT请求
     *
     * @param reqURL   请求地址
     * @param sendData 发送到远程主机的正文数据
     * @return 远程主机响应正文`HTTP状态码,如<code>"SUCCESS`200"</code>若通信过程中发生异常则返回"Failed`HTTP状态码",如
     * <code>"Failed`500"</code>
     */
    public static String sendPutRequest(String reqURL) {
        return sendPutRequest(reqURL, null);
    }

    /**
     * 发送HTTP_Delete请求
     *
     * @param reqURL   请求地址
     * @param sendData 发送到远程主机的正文数据
     * @param headers  请求头部
     * @param timeOut  失效时间
     * @return 远程主机响应正文`HTTP状态码,如<code>"SUCCESS`200"</code>若通信过程中发生异常则返回"Failed`HTTP状态码",如
     * <code>"Failed`500"</code>
     */
    public static String sendDeleteRequest(String reqURL, Map<String, String> headers, int timeout) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(reqURL);
        setHttpConfig(headers, timeout, httpDelete);
        return executeHttpRequest(httpClient, httpDelete);
    }

    /**
     * 发送HTTP_Delete请求
     *
     * @param reqURL   请求地址
     * @param sendData 发送到远程主机的正文数据
     * @param headers  请求头部
     * @param timeOut  失效时间
     * @return 远程主机响应正文`HTTP状态码,如<code>"SUCCESS`200"</code>若通信过程中发生异常则返回"Failed`HTTP状态码",如
     * <code>"Failed`500"</code>
     */
    public static String sendDeleteRequest(String reqURL, Map<String, String> headers) {
        return sendDeleteRequest(reqURL, headers, -1);
    }

    /**
     * 设置http请求的header和config
     *
     * @param headers
     * @param timeout
     * @param httpRequest
     */
    private static void setHttpConfig(Map<String, String> headers, int timeout, HttpRequestBase httpRequest) {
        if (timeout >= 0) {
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout).setSocketTimeout(timeout).build();
            httpRequest.setConfig(requestConfig);
        }
        if (headers != null) {
            for (String name : headers.keySet()) {
                httpRequest.setHeader(name, headers.get(name));
            }
        }
    }


    /**
     * 执行http请求并返回responseContent
     *
     * @param
     * @param httpClient
     * @param httpRequest
     * @return
     */
    private static String executeHttpRequest(CloseableHttpClient httpClient, HttpRequestBase httpRequest) {
        String responseContent = null;
        try {
            HttpResponse response = httpClient.execute(httpRequest); // 执行GET请求
            //处理http返回码302的情况
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY) {
                String locationUrl = response.getLastHeader("Location").getValue();
                sendGetRequest(locationUrl);//跳转到重定向的url
            } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity(); // 获取响应实体
                if (null != entity) {
                    responseContent = EntityUtils.toString(entity, "UTF-8");
                    EntityUtils.consume(entity); // Consume response content
                }
            }
        } catch (ClientProtocolException e) {
            log.error("该异常通常是协议错误导致,比如构造HttpGet对象时传入的协议不对(将'http'写成'htp')或者服务器端返回的内容不符合HTTP协议要求等,堆栈信息如下", e);
            e.printStackTrace();
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error("该异常通常是网络原因引起的,如HTTP服务器未启动等,堆栈信息如下", e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseContent;
    }

    /**
     * 执行302跳转 返回302跳转url
     *
     * @param
     * @param httpClient
     * @param httpRequest
     * @return
     */
    private static String getRedirectUrl(CloseableHttpClient httpClient, HttpRequestBase httpRequest) {
        String responseContent = null;
        try {
            HttpResponse response = httpClient.execute(httpRequest); // 执行GET请求
            //处理http返回码302的情况
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY) {
                String locationUrl = response.getLastHeader("Location").getValue();
                return locationUrl;
            }
        } catch (ClientProtocolException e) {
            log.error("该异常通常是协议错误导致,比如构造HttpGet对象时传入的协议不对(将'http'写成'htp')或者服务器端返回的内容不符合HTTP协议要求等,堆栈信息如下", e);
            e.printStackTrace();
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error("该异常通常是网络原因引起的,如HTTP服务器未启动等,堆栈信息如下", e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseContent;
    }

}