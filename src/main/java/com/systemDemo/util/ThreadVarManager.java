package com.systemDemo.util;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
 

/**
 * 
 * @author  :袁志权 E-mail:joshua.yuan@100cb.cn
 * @version :创建时间：2018年2月28日 下午3:24:09
 * 说明         : 线程变量
 */
public class ThreadVarManager {

	//当前登录portal 的用户
	private static final ThreadLocal<String> userName = new ThreadLocal<String>();
	
	private static final ThreadLocal<String> channel = new ThreadLocal<String>();
	
	//当前请求对象
	private static final ThreadLocal<HttpServletRequest> currRequest = new ThreadLocal<HttpServletRequest>();

	private static final ThreadLocal<String> message = new ThreadLocal<String>();
	
	private static final ThreadLocal<Date> dateTr = new ThreadLocal<Date>();
	
	public static ThreadLocal<String> getMessage() {
		return message;
	}

	public static void setMessage(String message) {
		userName.set(message);
	}
	
	public static Date getDateTr() {
		return dateTr.get();
	}

	public static void setDateTr(Date name) {
		dateTr.set(name);
	}

    public static String getUserName() {
        return userName.get();
    }

    public static void setUserName(String name) {
        userName.set(name);
    }
    
    public static String getChannel() {
        return channel.get();
    }

    public static void setChannel(String ch) {
    	channel.set(ch);
    }
	
	public static HttpServletRequest getCurrRequest() {
		return currRequest.get();
	}

	public static void setCurBeanName(HttpServletRequest request) {
		currRequest.set(request);
	}

}
