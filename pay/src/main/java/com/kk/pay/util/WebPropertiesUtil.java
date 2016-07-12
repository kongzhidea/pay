package com.kk.pay.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * 操作web.properties文件
 * 代码中的配置为测试环境配置，上线后会被替换
 */
public class WebPropertiesUtil {
	private static final Log logger = LogFactory.getLog(WebPropertiesUtil.class);
	private static WebPropertiesUtil instance = new WebPropertiesUtil();

	public static final String ENVIRONMENT = "environment";// 当前环境 test/online
	public static final String ONLINE_ENVIRONMENT = "online";

	private Properties props = new Properties();

	public static WebPropertiesUtil getInstance() {
		return instance;
	}

	private WebPropertiesUtil() {
		initConfigProperties("web.properties");		
	}

	private void initConfigProperties(String filePath) {
		String path = getClass().getResource("/").getPath();
		InputStream in;
		try {
			in = new BufferedInputStream(new FileInputStream(path + filePath));
			props.load(in);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public String getValue(String key) {
		return props.getProperty(key);
	}
	
	
	public Properties getProperty(String filePath) {
		Properties propTmp = new Properties();
		String path = getClass().getResource("/").getPath();
		InputStream in;
		try {
			in = new BufferedInputStream(new FileInputStream(path + filePath));
			propTmp.load(in);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return propTmp;
	}

	public boolean isOnline(){
		String env = getInstance().getValue(ENVIRONMENT);
		if (env != null && env.equals(ONLINE_ENVIRONMENT)){
			return true;
		}

		return false;
	}
}
