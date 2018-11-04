package com.systemDemo.config;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 读取数据配置的类
 * @author lenovo
 *
 */

public class ConfigUtil {
	
	public static Properties getProperties ( String configFileName ) throws IOException
	{
		BufferedInputStream in = null;
		Properties pros = new Properties ( );

		String fileName = CommonConfig.CONFIG_FILE_PATH + "/" + configFileName;
		File file = new File ( fileName );
		if ( file.exists ( ) ) {
			in = new BufferedInputStream ( new FileInputStream ( fileName ) );
		} else
			in = new BufferedInputStream ( ConfigUtil.class.getResourceAsStream ( "/" + configFileName ) );

		pros.load ( in );
		if ( null != in ) {
			in.close ( );
		}

		return pros;
	}

	public static String getValue(Properties pros, String key, String defaultValue) {
		String value = defaultValue;
		if (pros.containsKey(key))
			value = pros.getProperty(key).trim ( );
		return value;
	}
}
