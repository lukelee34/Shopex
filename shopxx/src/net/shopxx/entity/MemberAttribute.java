package net.shopxx.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;

import org.apache.commons.lang.StringUtils;

/**
 * 实体类 - 会员注册项
 * ============================================================================
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得SHOP++商业授权之前，您不能将本软件应用于商业用途，否则SHOP++将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.shopxx.net
 * ----------------------------------------------------------------------------
 * KEY: SHOPXX9F8BFD982E658EA8305195D192FB5E35
 * ============================================================================
 */

@Entity
public class MemberAttribute extends BaseEntity {

	private static final long serialVersionUID = 4513705276569738136L;
	
	// 注册项类型：
	public enum AttributeType {
		text, number, alphaint, email, select, checkbox, name, gender, date, area, address, zipCode, mobile, phone, qq, msn, wangwang, skype
	}

	private String name;// 注册项名称
	private AttributeType attributeType;// 注册项类型
	private Boolean isRequired;// 是否必填
	private Boolean isEnabled;// 是否启用
	private Integer orderList;// 排序
	private String attributeOptionStore;// 可选项储存

	@Column(nullable = false, unique = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Enumerated
	@Column(nullable = false)
	public AttributeType getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(AttributeType attributeType) {
		this.attributeType = attributeType;
	}

	@Column(nullable = false)
	public Boolean getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(Boolean isRequired) {
		this.isRequired = isRequired;
	}

	@Column(nullable = false)
	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	@Column(nullable = false)
	public Integer getOrderList() {
		return orderList;
	}

	public void setOrderList(Integer orderList) {
		this.orderList = orderList;
	}
	
	public String getAttributeOptionStore() {
		return attributeOptionStore;
	}

	public void setAttributeOptionStore(String attributeOptionStore) {
		this.attributeOptionStore = attributeOptionStore;
	}
	
	// 获取可选项
	@SuppressWarnings("unchecked")
	@Transient
	public List<String> getAttributeOptionList() {
		if (StringUtils.isEmpty(attributeOptionStore)) {
			return null;
		}
		JSONArray jsonArray = JSONArray.fromObject(attributeOptionStore);
		return (List<String>) JSONSerializer.toJava(jsonArray);
	}
	
	// 设置可选项
	@Transient
	public void setAttributeOptionList(List<String> attributeOptionList) {
		if (attributeOptionList == null || attributeOptionList.size() == 0) {
			attributeOptionStore = null;
			return;
		}
		JSONArray jsonArray = JSONArray.fromObject(attributeOptionList);
		attributeOptionStore = jsonArray.toString();
	}

}