package net.shopxx.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;

import org.apache.commons.lang.StringUtils;

/**
 * 实体类 - 商品属性
 * ============================================================================
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得SHOP++商业授权之前，您不能将本软件应用于商业用途，否则SHOP++将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.shopxx.net
 * ----------------------------------------------------------------------------
 * KEY: SHOPXX683FD9A1F4936F9B8563BDB221B35259
 * ============================================================================
 */

@Entity
@Table(
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"name", "productType_id"})
	}
)
public class ProductAttribute extends BaseEntity {

	private static final long serialVersionUID = 2989102568413246570L;
	
	// 属性类型：
	public enum AttributeType {
		text, number, alphaint, select, checkbox, date
	}

	private String name;// 属性名称
	private AttributeType attributeType;// 属性类型
	private Boolean isRequired;// 是否必填
	private Boolean isEnabled;// 是否启用
	private Integer orderList;// 排序
	private String attributeOptionStore;// 可选项储存
	
	private ProductType productType;// 商品类型

	@Column(nullable = false)
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
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