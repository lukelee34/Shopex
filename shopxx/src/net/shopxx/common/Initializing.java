package net.shopxx.common;
import java.lang.reflect.Method;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.InitializingBean;

import sun.misc.BASE64Decoder;

/**
 * SHOP++ 初始化
 * ============================================================================
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得SHOP++商业授权之前，您不能将本软件应用于商业用途，否则SHOP++将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.shopxx.net
 * ----------------------------------------------------------------------------
 * KEY: SHOPXXF493F4F9639AB767EED179DBF919AD85
 * ============================================================================
 */

public class Initializing implements InitializingBean {
	
	private String keyFile = "key";
	
	@Resource
	private ServletContext servletContext;

	@Override
	public void afterPropertiesSet() throws Exception {
		if (servletContext != null) {
			BASE64Decoder bASE64Decoder = new BASE64Decoder();
			keyFile = new String(bASE64Decoder.decodeBuffer(keyFile + "A=="));
			Method readKey = Class.forName("net.shopxx.common.Key").getMethod("readKeyFile", String.class);
			String content = (String) readKey.invoke(null, servletContext.getRealPath(keyFile));
			servletContext.setAttribute(new String(bASE64Decoder.decodeBuffer("U0hPUFhYX0tFWQ==")), content);
		}
	}

	public String getKeyFile() {
		return keyFile;
	}

	public void setKeyFile(String keyFile) {
		this.keyFile = keyFile;
	}

}