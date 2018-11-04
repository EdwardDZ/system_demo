package com.systemDemo.util;

import java.util.Map;
import java.util.HashMap;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.systemDemo.config.CommonConfig;

/**
 * 
 * @author :袁志权 E-mail:joshua.yuan@100cb.cn
 * @version :创建时间：2018年1月27日 下午4:07:20 说明 :
 */
public class CookieHelper {

	/**
	 * 添加一个cookie
	 * 
	 * @param res
	 * @param name
	 * @param value
	 * @param maxAge
	 */
	public static void add(HttpServletResponse res, String name, String value, int maxAge) {
		Cookie cookie = new Cookie(name, value);
		if (maxAge > 0) {
			cookie.setMaxAge(maxAge);
		}
		// 在保存cookie前要设置P3P头，主要是在跨域取cookie的时候，防止IE浏览器的阻止cookie的读取
		res.setHeader("P3P", "CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"");
		// cookie有效路径是网站根目录
		cookie.setPath("/");
		// 设置跨域cookie
		if(!CommonConfig.COOKIE_DOMAIN.trim().equals(""))
			cookie.setDomain(CommonConfig.COOKIE_DOMAIN);
		res.addCookie(cookie);
	}

	public static void add(HttpServletResponse res, String name, String value) {
		// add ( res , name , value , 3600 * 7 );
		Cookie cookie = new Cookie(name, value);
		// 在保存cookie前要设置P3P头，主要是在跨域取cookie的时候，防止IE浏览器的阻止cookie的读取
		res.setHeader("P3P", "CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"");
		// cookie有效路径是网站根目录
		cookie.setPath("/");
		// 设置跨域cookie
		if(!CommonConfig.COOKIE_DOMAIN.trim().equals(""))
			cookie.setDomain(CommonConfig.COOKIE_DOMAIN);
		res.addCookie(cookie);
	}

	/**
	 * 获取cookie的值
	 * 
	 * @param req
	 * @param name
	 * @return
	 */
	public static String getName(HttpServletRequest req, String name) {
		Cookie cookie = get(req, name);
		String cookieVal = (null == cookie) ? null : cookie.getValue();
		return cookieVal;
	}

	/**
	 * 获取cookie （键值对）
	 * 
	 * @param req
	 * @param name
	 * @return
	 */
	public static Cookie get(HttpServletRequest req, String name) {
		Map<String, Cookie> cookieMap = _readCookieMap(req);
		if (cookieMap.containsKey(name)) {
			return (Cookie) cookieMap.get(name);
		} else {
			return null;
		}
	}

	/**
	 * 清除指定键的cookie
	 * 
	 * @param req
	 * @param res
	 * @param name
	 */
	public static void remove(HttpServletRequest req, HttpServletResponse res, String name) {
		String cookieName = getName(req, name);
		if (null != cookieName) {
			Cookie cookie = new Cookie(name, null);
			cookie.setMaxAge(0);
			cookie.setPath("/");
			res.addCookie(cookie);
		}
	}

	/**
	 * 清除所有cookie
	 * 
	 * @param req
	 * @param res
	 */
	public static void clear(HttpServletRequest req, HttpServletResponse res) {
		Cookie[] cookies = req.getCookies();
		for (int i = 0, len = cookies.length; i < len; i++) {
			Cookie cookie = new Cookie(cookies[i].getName(), null);
			cookie.setMaxAge(0);
			cookie.setPath("/");
			res.addCookie(cookie);
		}
	}

	private static Map<String, Cookie> _readCookieMap(HttpServletRequest req) {
		Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
		Cookie[] cookies = req.getCookies();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				cookieMap.put(cookie.getName(), cookie);
			}
		}
		return cookieMap;
	}
}
