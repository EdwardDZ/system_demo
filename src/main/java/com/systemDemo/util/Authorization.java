package com.systemDemo.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.util.TextUtils;

import com.systemDemo.config.CommonConfig;
import com.systemDemo.data.JedisUtil;
import com.systemDemo.util.MD5Util;

public class Authorization {
	
    /**
     * 对访问子服务的用户进行鉴权
     * @param appId
     * @param target
     * @param request
     * @param response
     * @return
     */
    public  String[] identify(String appId, String target, HttpServletRequest request, HttpServletResponse response) {
    	       
		if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(target)) {
            return null;
        }

        if (null == request.getCookies() || 0 >= request.getCookies().length) {
            return null;
        }

        Map<String, String> cookieMap = new HashMap<String, String>();
        for (Cookie cookie : request.getCookies()) {
            cookieMap.put(cookie.getName().toLowerCase(), cookie.getValue());
        }

        if (!cookieMap.containsKey(CommonConfig.USER_LOGIN_COOKIE_NAME) ) {
        		return null;
      	}

        String[] result = new String[6];
        Arrays.fill(result, "");
		boolean authority = false;

        Map<String, String> tmpMap = JedisUtil.getMap(CommonConfig.LOGIN_USER_REDIS_PREFIX + cookieMap.get(CommonConfig.USER_LOGIN_COOKIE_NAME));
		if (null != tmpMap && !TextUtils.isEmpty(tmpMap.get("userId"))) {	
			result[0] = tmpMap.get("userId");
			result[1] = tmpMap.get("userName");
			result[3] = "";
			if(tmpMap.containsKey("channel") && tmpMap.get("channel") != null)
				result[3] = tmpMap.get("channel");
			result[4] = tmpMap.get("roleRank");

			if(!CommonConfig.AUTHORITY_CHECK){
				authority = true;
			}
			else if (tmpMap.get("roleid").equals("0")) {
				//空角色
				authority = false;
			} else if (result[4].equals("1")) {
				// 超级管理员，直接赋予权限
				authority = true;
			} else {

				//by zhiquan 
				String authorityCacheKey = CommonConfig.USER_AUTHORITY_REDIS_PREFIX + result[0];
				Map<String, String> userAuthority = JedisUtil.getMap(authorityCacheKey);
				if (userAuthority == null || userAuthority.isEmpty()) {
					// 取得用户角色(岗位)
					//Map<String, Object> roleMap = dbHelper.queryForMap("select role_id from user where id=?", result[0]);
					// 取得角色对应的权限
					//getUserAuthority( String.valueOf( roleMap.get("role_id")) , authorityCacheKey, userAuthority, result[0]);
				}

				final String authorityDetail = MD5Util.MD5(appId.toLowerCase() + "_sysinfo_" + target.toLowerCase());
				if (userAuthority.containsKey(authorityDetail)) {
					// 有权限
					authority = true;
				} else {
					authority = false;
				}

			}
		}
		result[2] = authority ? "YES" : "NO";
        return result;
    }
}
