package net.shopxx.common;

import java.util.LinkedHashMap;
import java.util.Map;

import net.shopxx.entity.Resource;
import net.shopxx.service.ResourceService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.ConfigAttributeDefinition;
import org.springframework.security.ConfigAttributeEditor;
import org.springframework.security.intercept.web.DefaultFilterInvocationDefinitionSource;
import org.springframework.security.intercept.web.FilterInvocationDefinitionSource;
import org.springframework.security.intercept.web.RequestKey;
import org.springframework.security.util.AntUrlPathMatcher;
import org.springframework.security.util.UrlMatcher;
import org.springframework.stereotype.Component;

/**
 * 后台权限、资源对应关系
 * ============================================================================
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得SHOP++商业授权之前，您不能将本软件应用于商业用途，否则SHOP++将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.shopxx.net
 * ----------------------------------------------------------------------------
 * KEY: SHOPXXBAE8DE0098E21D2A46669499BBCBCB78
 * ============================================================================
 */

@Component
public class AdminSecurityDefinitionSource implements FactoryBean {

	@javax.annotation.Resource
	private ResourceService resourceService;

	public boolean isSingleton() {
		return true;
	}

	@SuppressWarnings("unchecked")
	public Class getObjectType() {
		return FilterInvocationDefinitionSource.class;
	}

	protected UrlMatcher getUrlMatcher() {
		return new AntUrlPathMatcher();
	}

	public Object getObject() throws Exception {
		return new DefaultFilterInvocationDefinitionSource(this.getUrlMatcher(), this.buildRequestMap());
	}

	protected LinkedHashMap<RequestKey, ConfigAttributeDefinition> buildRequestMap() throws Exception {
		LinkedHashMap<RequestKey, ConfigAttributeDefinition> resultMap = new LinkedHashMap<RequestKey, ConfigAttributeDefinition>();
		ConfigAttributeEditor configAttributeEditor = new ConfigAttributeEditor();
		Map<String, String> resourceMap = this.getResourceMap();
		for (Map.Entry<String, String> entry : resourceMap.entrySet()) {
			RequestKey key = new RequestKey(entry.getKey(), null);
			configAttributeEditor.setAsText(entry.getValue());
			resultMap.put(key, (ConfigAttributeDefinition) configAttributeEditor.getValue());
		}
		return resultMap;
	}

	protected Map<String, String> getResourceMap() {
		Map<String, String> resourceMap = new LinkedHashMap<String, String>();
		for (Resource resource : resourceService.getAll()) {
			String resourceValue = resource.getValue();
			if (StringUtils.isNotEmpty(resource.getRoleSetString())) {
				resourceMap.put(resourceValue, resource.getRoleSetString());
			}
		}
		return resourceMap;
	}

}