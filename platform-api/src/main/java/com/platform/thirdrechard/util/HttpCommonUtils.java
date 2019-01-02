package com.platform.thirdrechard.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wanghui
 * http  公共类
 *
 */
public class HttpCommonUtils {

	private static final Logger logger = LoggerFactory.getLogger(HttpCommonUtils.class);

	public static String sendPostRequest(String url, Map<String, String> params) {
		String returnOriginalMessage = null ;
		HttpClient httpclient = new HttpClient();
		httpclient.getParams().setConnectionManagerTimeout(3000);
		httpclient.getParams().setSoTimeout(3000);
		PostMethod postMethod = new PostMethod(url);
		postMethod.setRequestBody(convertMapToNameValuePairs(params));
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		postMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		int statusCode;
		try {
			httpclient.executeMethod(postMethod);
			statusCode = postMethod.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				logger.error("Http Post Request 状态异常，状态码为：" + statusCode);
				throw new Exception(" Http Post Request 状态异常，状态码为：" + statusCode);
			}
			InputStream inputStream = null;
			try {
				inputStream = postMethod.getResponseBodyAsStream();
				returnOriginalMessage = convertInputStreamToString(inputStream);
			} catch (Exception e) {
				logger.error(" Http Post Response 异常.{}", e);
				throw e;
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (Exception e) {
						logger.error(" IO ClOSE 异常.{}", e);
					}
				}
				try {
					postMethod.releaseConnection();
				} catch (Exception e) {
					logger.error("HTTP RELEASE CONNECTION  异常.{}", e);
				}
			}
			logger.info("http Post ResPonse .{}",returnOriginalMessage);
			return returnOriginalMessage;

		} catch (Exception e) {
			logger.error("HTTP 异常.{}", e);
		} 
		return  returnOriginalMessage;

	}
	public static  String sendPost(String url, String data) {
		   String response = null;
		   try {
		       CloseableHttpClient httpclient = null;
		       CloseableHttpResponse httpresponse = null;
		       HttpPost httppost = new HttpPost(url);
		       try {
		           httpclient = HttpClients.createDefault();
		           httppost.setHeader(HttpHeaders.CONNECTION, "close");
		           StringEntity stringentity = new StringEntity(data,
		                   ContentType.create("application/json", "UTF-8"));
		           httppost.setEntity(stringentity);
		           httpresponse = httpclient.execute(httppost);
		           response = EntityUtils
		                   .toString(httpresponse.getEntity());
		       } finally {
		           if (httpclient != null) {
		               httpclient.close();
		           }
		           if (httpresponse != null) {
		               httpresponse.close();
		           }
		           if(httppost != null) {
		        	   httppost.releaseConnection();
		  	     }
		       }
		   } catch (Exception e) {
		       e.printStackTrace();
		   }
		   return response;
		}
	public static String sendPostJsonRequest(String url, Map<String, String> params) {
		String returnOriginalMessage = null ;
		HttpClient httpclient = new HttpClient();
		httpclient.getParams().setConnectionManagerTimeout(3000);
		httpclient.getParams().setSoTimeout(3000);
		PostMethod postMethod = new PostMethod(url);
		postMethod.setRequestBody(convertMapToNameValuePairs(params));
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		postMethod.addRequestHeader("Content-Type", "application/json; charset=UTF-8");
		int statusCode;
		try {
			httpclient.executeMethod(postMethod);
			statusCode = postMethod.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				logger.error("Http Post Request 状态异常，状态码为：" + statusCode);
				throw new Exception(" Http Post Request 状态异常，状态码为：" + statusCode);
			}
			InputStream inputStream = null;
			try {
				inputStream = postMethod.getResponseBodyAsStream();
				returnOriginalMessage = convertInputStreamToString(inputStream);
			} catch (Exception e) {
				logger.error(" Http Post Response 异常.{}", e);
				throw e;
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (Exception e) {
						logger.error(" IO ClOSE 异常.{}", e);
					}
				}
				try {
					postMethod.releaseConnection();
				} catch (Exception e) {
					logger.error("HTTP RELEASE CONNECTION  异常.{}", e);
				}
			}
			logger.info("http Post ResPonse .{}",returnOriginalMessage);
			return returnOriginalMessage;

		} catch (Exception e) {
			logger.error("HTTP 异常.{}", e);
		} 
		return  returnOriginalMessage;

	}

	private static NameValuePair[] convertMapToNameValuePairs(Map<String, String> map) {
		NameValuePair[] pairs = new NameValuePair[map.size()];
		Set<Map.Entry<String, String>> entrySet = map.entrySet();
		Iterator<Map.Entry<String, String>> iterator = entrySet.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			Map.Entry<String, String> entry = iterator.next();
			pairs[i] = new NameValuePair(entry.getKey(), entry.getValue());
			i++;
		}
		return pairs;
	}

	private static String convertInputStreamToString(InputStream inputStream) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();
	}
}