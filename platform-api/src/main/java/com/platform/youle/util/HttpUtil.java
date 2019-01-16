package com.platform.youle.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import com.alibaba.fastjson.JSON;
import com.platform.youle.entity.RequestProductEntity;

import org.apache.log4j.Logger;
import com.alibaba.fastjson.JSONObject;
import springfox.documentation.service.ApiListing;

public class HttpUtil {
	
	//超时时间
	private static final int TIMEOUT = 45000;
	//设置编码
    public static final String ENCODING = "UTF-8";
    
    protected static final Logger logger = Logger.getLogger(HttpUtil.class);
    /**
     * 创建HTTP连接
     * 
     * @param url
     *            地址
     * @param method
     *            方法
     * @param headerParameters
     *            头信息
     * @param body
     *            请求内容
     * @return
     * @throws Exception
     */
    private static HttpURLConnection createConnection(String url,String method, Map<String, String> headerParameters, String body)
            throws Exception {
        URL Url = new URL(url);
        trustAllHttpsCertificates();
        HttpURLConnection httpConnection = (HttpURLConnection) Url.openConnection();
        System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
        System.setProperty("sun.net.client.defaultReadTimeout", "30000");
        // 设置请求时间
        httpConnection.setConnectTimeout(TIMEOUT);
        // 设置 header
        if (headerParameters != null) {
            Iterator<String> iteratorHeader = headerParameters.keySet().iterator();
            while (iteratorHeader.hasNext()) {
                String key = iteratorHeader.next();
                httpConnection.setRequestProperty(key,headerParameters.get(key));
            }
        }
        httpConnection.setRequestProperty("ContentType","application/x-www-form-urlencoded");
        httpConnection.setRequestProperty("Accept-Charset", "UTF-8");
        // 设置请求方法
        httpConnection.setRequestMethod(method);
        httpConnection.setDoOutput(true);
        httpConnection.setDoInput(true);
        // 写query数据流
        if (!(body == null || body.trim().equals(""))) {
            OutputStream writer = httpConnection.getOutputStream();
            try {
                writer.write(body.getBytes(ENCODING));
            } finally {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
            }
        }

        // 请求结果
        int responseCode = httpConnection.getResponseCode();
        if (responseCode != 200) {
            throw new Exception(responseCode +":"+ inputStream2String(httpConnection.getErrorStream(),ENCODING));
        }
        return httpConnection;
    }

    /**
     * POST请求
     * @param address 请求地址
     * @param headerParameters 参数
     * @param body
     * @return
     * @throws Exception
     */
    public static String post(String address,Map<String,Object> params) throws Exception {
        return proxyHttpRequest(address, "POST", null,getRequestBody(params));
    }

    /**
     * GET请求
     * @param address
     * @param headerParameters
     * @param body
     * @return
     * @throws Exception
     */
    public static String get(String address,Map<String,Object> param) throws Exception {
        return proxyHttpRequest(address + "?"+ getRequestBody(param), "GET", null, null);
    }

    /**
     * 读取网络文件
     * @param address
     * @param headerParameters
     * @param body
     * @param file
     * @return
     * @throws Exception
     */
    public static String getFile(String address,Map<String,Object> param, File file) throws Exception {
        String result = "fail";
        HttpURLConnection httpConnection = null;
        try {
            httpConnection = createConnection(address, "POST", null,getRequestBody(param));
            result = readInputStream(httpConnection.getInputStream(), file);
        } catch (Exception e) {
            throw e;
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }

        }

        return result;
    }


    public static byte[] getFileByte(String address,Map<String,Object>  param) throws Exception {
        byte[] result = null;
        HttpURLConnection httpConnection = null;
        try {
            httpConnection = createConnection(address, "POST", null,getRequestBody(param));
            result = readInputStreamToByte(httpConnection.getInputStream());
        } catch (Exception e) {
            throw e;
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }
        return result;
    }

    /**
     * 读取文件流
     * @param in
     * @return
     * @throws Exception
     */
    public static String readInputStream(InputStream in, File file)throws Exception {
        FileOutputStream out = null;
        ByteArrayOutputStream output = null;
        try {
            output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                output.write(buffer, 0, len);
            }
            out = new FileOutputStream(file);
            out.write(output.toByteArray());
        } catch (Exception e) {
            throw e;
        } finally {
            if (output != null) {
                output.close();
            }
            if (out != null) {
                out.close();
            }
        }
        return "success";
    }

    public static byte[] readInputStreamToByte(InputStream in) throws Exception {
        FileOutputStream out = null;
        ByteArrayOutputStream output = null;
        byte[] byteFile = null;

        try {
            output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                output.write(buffer, 0, len);
            }
            byteFile = output.toByteArray();
        } catch (Exception e) {
            throw e;
        } finally {
            if (output != null) {
                output.close();
            }
            if (out != null) {
                out.close();
            }
        }

        return byteFile;
    }

    /**
     * HTTP请求
     * 
     * @param address
     *            地址
     * @param method
     *            方法
     * @param headerParameters
     *            头信息
     * @param body
     *            请求内容
     * @return
     * @throws Exception
     */
    public static String proxyHttpRequest(String address, String method,Map<String, String> headerParameters, String body) throws Exception {
        String result = null;
        HttpURLConnection httpConnection = null;
        try {
            httpConnection = createConnection(address, method,headerParameters, body);

            String encoding = "UTF-8";
            if (httpConnection.getContentType() != null && httpConnection.getContentType().indexOf("charset=") >= 0) {
                encoding = httpConnection.getContentType().substring(httpConnection.getContentType().indexOf("charset=") + 8);
            }


            result = inputStream2String(httpConnection.getInputStream(),encoding);
            // logger.info("HTTPproxy response: {},{}", address,
            // result.toString());
        } catch (Exception e) {
            // logger.info("HTTPproxy error: {}", e.getMessage());
            throw e;
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }
        return result;
    }

    /**
     * 将参数化为 body
     * @param params
     * @return
     */
    public static String getRequestBody(Map<String,Object> param) {
        return getRequestBody(param, true);
    }

    /**
     * 将参数化为 body
     * @param params
     * @return
     * @throws UnsupportedEncodingException 
     */
    public static String getRequestBody(Map<String,Object> params,boolean urlEncode){
        StringBuilder body = new StringBuilder();
        Iterator<String> iteratorHeader = params.keySet().iterator();
        while (iteratorHeader.hasNext()) {
            String key = iteratorHeader.next();
            Object value = params.get(key);

            if (urlEncode) {
                try {
                    body.append(key + "=" + URLEncoder.encode(value.toString(), ENCODING)
                            + "&");
                } catch (UnsupportedEncodingException e) {
                    // e.printStackTrace();
                }
            } else {
                body.append(key + "=" + value + "&");
            }
        }

        return body.toString();
    }

    /**
     * 读取inputStream 到 string
     * @param input
     * @param encoding
     * @return
     * @throws IOException
     */
    private static String inputStream2String(InputStream input, String encoding)throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input,encoding));
        StringBuilder result = new StringBuilder();
        String temp = null;
        while ((temp = reader.readLine()) != null) {
            result.append(temp);
        }
        return result.toString();
    }


    /**
     * 设置 https 请求
     * @throws Exception
     */
    private static void trustAllHttpsCertificates() throws Exception {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String str, SSLSession session) {
                return true;
            }
        });
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }


    //设置 https 请求证书
    static class miTM implements javax.net.ssl.TrustManager,javax.net.ssl.X509TrustManager {

        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }

        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }


    }

    //====================================================================
    //============================= 测试调用   ============================
    //====================================================================
    public static void main(String[] args) {

            try {
                String  wid = TokenUtil.wid ;
                String  token = TokenUtil.token;
                Long timestamp = Calendar.getInstance().getTimeInMillis();
                RequestProductEntity RequestProductEntity = new RequestProductEntity();
                RequestProductEntity.setPage(1);
                RequestProductEntity.setTimestamp(timestamp.toString());
                RequestProductEntity.setToken(token);
                RequestProductEntity.setWid(wid);
                String jsonString  =  JSONObject.toJSONString(RequestProductEntity);
                Map<String,Object> map1 = (Map<String,Object>) JSON.parse(jsonString);
                //请求地址
                String address = "http://open.fygift.com/api/product/getAllProductIds.php";
                //请求参数
                // 调用 post 请求
                String res = post(address, map1);
                System.out.println("返回参数"+res);//打印返回参数

            } catch (Exception e) {
                // TODO 异常
                e.printStackTrace();
            }

    }

}
