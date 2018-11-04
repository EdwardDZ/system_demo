package com.systemDemo.config;

import java.util.Properties;

/**
 *  存储从配置文件读得的配置变量
 * 
 * @author zhiquan
 *
 */

public class CommonConfig {


	// 配置文件夹路径
	public static String CONFIG_FILE_PATH = "config";

	// 日志路径
	public static String LOG_PATH	= "";

	public static String LOG_NAME	= "saas_gateway";
	
	// 事件日志文件个数
	public static int INFO_LOG_NUM = 1;
 
	//模板文件路径
	public static String TEMPLATE_DIR_PATH = "";
	
	//模板的缓存时间
	public static int TEMPLATE_CACHE_TIME	= 1;
	
	
	//设置cookie 的域名
	public static String COOKIE_DOMAIN	= "saas.com";

	//模板路径
    public static String FTL_TEMPLATE_PATH;
    
    //加密，解密秘钥路径
    public static String DES_KEY_PATH	="d:/keystore";
	
    
    //发送短信验证码的URL
    public static String VCODE_URL	= "";
    
    public static String OPERATION_PACKAGE = "bcb.saas.bas.eagle.rule.operation.";
    
    //系统端口
    public static String SERVER_PORT = "2002";
    
    //redis缓存用户名
    public static String LOGIN_USER_REDIS_PREFIX = "saas_mps_login_";
    
    //cookie
    public static String USER_LOGIN_COOKIE_NAME = "mps_sid";
    
   //用户权限的 key  
  	public static String USER_AUTHORITY_REDIS_PREFIX = "bcb_portal_authority_";
    
    //是否需要鉴权
    public static boolean AUTHORITY_CHECK	= true;
 
	static {
		try {
			Properties pros = ConfigUtil.getProperties("application.properties");
			LOG_PATH = ConfigUtil.getValue(pros, "log.path", "");
			LOG_NAME = ConfigUtil.getValue(pros, "log.name", "");
			if (!ConfigUtil.getValue(pros, "info.log.num", "").equals("")) {
				INFO_LOG_NUM = Integer.parseInt(ConfigUtil.getValue(pros, "info.log.num", "").replace(" ", ""));
			}
			
			FTL_TEMPLATE_PATH = ConfigUtil.getValue(pros, "spring.freemarker.template-loader-path", "");
			
			SERVER_PORT =   ConfigUtil.getValue(pros, "server.port", "2002") ;
			
			AUTHORITY_CHECK = !ConfigUtil.getValue(pros, "authority.check", "true").equals("false") ? true :false ;
			
			pros.clear();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
