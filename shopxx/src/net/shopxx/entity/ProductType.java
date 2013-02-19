package net.shopxx.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OrderBy;

/**
 * 实体类 - 商品类型
 * ============================================================================
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得SHOP++商业授权之前，您不能将本软件应用于商业用途，否则SHOP++将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.shopxx.net
 * ----------------------------------------------------------------------------
 * KEY: SHOPXX9205E70ECB8E9EA3CF1669CBB8C6FCAF
 * ============================================================================
 */

@Entity
public class ProductType extends BaseEntity {

	private static final long serialVersionUID = -6173231303962800416L;

	private String name;// 类型名称
	
	private List<ProductAttribute> productAttributeList;// 商品属性
	private Set<Product> productSet;// 商品

	@Column(nullable = false, unique = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "productType")
	@Cascade(value = { CascadeType.DELETE })
	@OrderBy(clause = "orderList asc")
	public List<ProductAttribute> getProductAttributeList() {
		return productAttributeList;
	}

	public void setProductAttributeList(List<ProductAttribute> productAttributeList) {
		this.productAttributeList = productAttributeList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "productType")
	public Set<Product> getProductSet() {
		return productSet;
	}

	public void setProductSet(Set<Product> productSet) {
		this.productSet = productSet;
	}
	
	// 获得已启用的商品属性
	@Transient
	public List<ProductAttribute> getEnabledProductAttributeList() {
		if (productAttributeList == null) {
			return null;
		}
		List<ProductAttribute> enabledProductAttributeList = new ArrayList<ProductAttribute>();
		for (ProductAttribute productAttribute : productAttributeList) {
			if (productAttribute.getIsEnabled()) {
				enabledProductAttributeList.add(productAttribute);
			}
		}
		return enabledProductAttributeList;
	}

}