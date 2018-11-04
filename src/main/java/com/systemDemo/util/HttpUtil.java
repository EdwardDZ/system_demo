package com.systemDemo.util;

import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;


/**
 * 
 * @author  :袁志权 E-mail:joshua.yuan@100cb.cn
 * @version :创建时间：2018年1月27日 下午4:06:14
 * 说明         :
 */
public  class HttpUtil {
 
//	@Autowired
//	private static JdbcTemplate dbHelper;
	
    // 直接页面输出字符串
    public static void setResponse(HttpServletResponse response, Map<String, Object> result) {
        String contentType = "text/html; charset=utf-8";
        if (result.containsKey("contentType")) {
            if (((String) result.get("contentType")).toLowerCase().equals("json"))
                contentType = "application/json;charset=utf-8";
        }
        response.setContentType(contentType);

        java.io.Writer out;
        try {
            out = response.getWriter();
            out.write((String) result.get("content"));
            out.flush();
        } catch (Exception e) {
        }
    }

    public static void setResponse(HttpServletResponse response, String content) {
        setResponse(response, content, "text/html; charset=utf-8");
    }

    public static void setResponse(HttpServletResponse response, String content, String contentType) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("content", content);
        result.put("contentType", contentType);
        setResponse(response, result);
    }

    /**
     * 将所有请求参数的值转小写
     *
     * @param paramMap
     * @return
     */
    public static Map<String, String> convertKeyToLower(Map<String, String[]> paramMap) {
    	Map<String, String> params = new HashMap<String, String>();
        for (String key : paramMap.keySet()) {
            params.put(key.toLowerCase(), ((String[]) paramMap.get(key))[0]);
        }
        return params;
    }

    /**
     * 判断请求的来源IP是否为局域网IP
     * 10.0.0.0~10.255.255.255
     * 172.16.0.0~172.31.255.255
     * 192.168.0.0~192.168.255.255
     *
     * @param ipAddress
     * @return
     */
    public static boolean isIPAllow(String ipAddress) {
        if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1"))
            return true;
        try {
            String reg = "(10|172|192)\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})";
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(reg);
            java.util.regex.Matcher matcher = p.matcher(ipAddress);
            return matcher.find();
        } catch (Exception e) {
        }
        return true;
    }


    public static void setHost(HttpServletRequest request, Map<String, Object> root) {
        String requestUrl = request.getRequestURL().toString().split("\\?")[0].split("//")[1].split("/")[0];

        root.put("host", requestUrl.split(":")[0]);
        if (requestUrl.indexOf(":") >= 0) {
            root.put("port", requestUrl.split(":")[1]);
        } else {
            root.put("port", "80");
        }
    }

    public static String getUserId(HttpServletRequest request) {
        Random rand = new Random();
        String userId = "";
        userId = String.valueOf(System.currentTimeMillis()) + request.getRequestedSessionId() + String.valueOf(rand.nextInt(10000));
        return MD5Util.MD5(userId);
    }


    public static JSONObject httpGetRequestToJson(String url, Map<String, String> header, Cookie[] cookie) {
        HttpClientBuilder clientBuilder = HttpClients.custom();

        try {
            HttpGet get = new HttpGet(url);

            if (null != header) {
                for (String key : header.keySet()) {
                    get.addHeader(new BasicHeader(key, header.get(key)));
                }
            }
 
            
            clientBuilder.setDefaultHeaders(defaultHeader(cookie));

            RequestConfig config = RequestConfig.custom().setConnectTimeout(10 * 1000).build();
            get.setConfig(config);

            CloseableHttpClient client = clientBuilder.build();

            CloseableHttpResponse response = client.execute(get, HttpClientContext.create());

            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream inputStream = response.getEntity().getContent();
                String str = convertStreamToString(inputStream);
                return JSON.parseObject(str);

            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
	private static List<Header> defaultHeader(Cookie[] cookie) {
		ArrayList<Header> headers = new ArrayList<Header>();
		
		//设置跨域
		Header header = new BasicHeader("Access-Control-Allow-Headers", "*");
		headers.add(header);
		headers.add(new BasicHeader("Access-Control-Allow-Origin", "*"));
		headers.add(new BasicHeader("Access-Control-Allow-Methods", "GET,PUT,POST,OPTIONS,DELETE"));

		//设置cookie 
		StringBuilder cookieStr = new StringBuilder();
		BasicClientCookie[] cookies = convertCookies(cookie);
 
		if (null != cookies) {
			for (BasicClientCookie tmpCookie : cookies) {
				cookieStr.append(tmpCookie.getName() + "=" + tmpCookie.getValue() + ";");
			}
			if (cookieStr.length() > 0){
				cookieStr.setLength(cookieStr.length() - 1);
				headers.add(new BasicHeader("Cookie", cookieStr.toString()));
			}
		}

		return headers;
	}

 

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static BasicClientCookie[] convertCookies(javax.servlet.http.Cookie[] javaCookies) {
        if (null == javaCookies) {
            return null;
        }

        BasicClientCookie[] baseCookies = new BasicClientCookie[javaCookies.length];

        for (int i = 0; i < javaCookies.length; i++) {
            baseCookies[i] = convertCookie(javaCookies[i]);
        }

        return baseCookies;
    }

    public static BasicClientCookie convertCookie(javax.servlet.http.Cookie javaCookie) {
        if (null == javaCookie) {
            return null;
        }

        BasicClientCookie cookie = new BasicClientCookie(javaCookie.getName(), javaCookie.getValue());

        // set the domain
        String value = javaCookie.getDomain();
        if (value != null) {
            cookie.setDomain(value);
        } else {
            cookie.setDomain("surfond.com");
        }

        // path
        value = javaCookie.getPath();
        if (value != null) {
            cookie.setPath(value);
        }

        // comment
        value = javaCookie.getComment();
        if (value != null) {
            cookie.setComment(value);
        }

        cookie.setSecure(javaCookie.getSecure());

        // version
        cookie.setVersion(javaCookie.getVersion());

        // Reverse this to get the actual max age
        long expiryTime = javaCookie.getMaxAge();
        if (expiryTime > 0) {
            cookie.setExpiryDate(new Date((expiryTime + System.currentTimeMillis()) * 1000L));
        }

        // return the servlet cookie
        return cookie;
    }

//    public static CloseableHttpResponse doRequest(String url) {
//        CloseableHttpResponse response = null;
//        try {
//            CloseableHttpClient client = HttpClientBuilder.create().disableRedirectHandling().build();
//            URI uri = new URIBuilder(url).build();
//            HttpGet get = new HttpGet(uri);
//            response = client.execute(get, HttpClientContext.create());
//        } catch (Exception e) {
//            Log.error(e);
//        }
//        return response;
//    }
    
    
	public static void parseHeaders(HttpServletRequest request, Map<String, String> root) {

		Set<String> headParams= new HashSet<String>();
		headParams.add("access-token");
		headParams.add("platform");
		headParams.add("version");
		headParams.add("ip");
		
		Enumeration enu = request.getHeaderNames();// 取得全部头信息
		while (enu.hasMoreElements()) {// 以此取出头信息
			String headerName = (String) enu.nextElement();
			String headerValue = request.getHeader(headerName);// 取出头信息内容
			if(headParams.contains(headerName))
				root.put(headerName.toLowerCase(), headerValue);
		}
	}
	
	public static void  parseBody(HttpServletRequest request, Map<String, String> root) throws Exception {
		String bodyStr =  getRequestBody(request);
//		if (!bodyStr.equals("")) {
//			try {
//				JSONObject bodyJson = (JSONObject) JSONObject.parse(DESUtil.getDesString(bodyStr));
//				root.put("bodyParam", bodyJson);
//			} catch (Exception ex) {
//			}
//		}
		root.put("bodyParam", bodyStr);

	}
	
	public static String getRequestBody(HttpServletRequest request) throws IOException {
		BufferedReader br = request.getReader();
		String str, body = "";
		while ((str = br.readLine()) != null) {
			body += str;
		}
		return body;
	}
	
	
	/**
	 * 将map转换成url参数
	 * 
	 * @param map
	 * @return
	 */
	public static String getUrlParamsByMap(Map<String, String> map) {
		if (map == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			sb.append(entry.getKey() + "=" + entry.getValue());
			sb.append("&");
		}
		String s = sb.toString();
		if (s.endsWith("&")) {
			s = org.apache.commons.lang.StringUtils.substringBeforeLast(s, "&");
		}
		return s;
	}

}
