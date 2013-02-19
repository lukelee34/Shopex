package net.shopxx.dao;

import java.util.List;

import net.shopxx.entity.ProductAttribute;
import net.shopxx.entity.ProductType;

/**
 * Dao接口 - 商品属性
 * ============================================================================
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得SHOP++商业授权之前，您不能将本软件应用于商业用途，否则SHOP++将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.shopxx.net
 * ----------------------------------------------------------------------------
 * KEY: SHOPXX4D051F4491A9917665E897135777FB7C
 * ============================================================================
 */

public interface ProductAttributeDao extends BaseDao<ProductAttribute, String> {
	
	/**
	 * 获取已启用的商品属性.
	 * 
	 * @return 已启用的商品属性集合.
	 */
	public List<ProductAttribute> getEnabledProductAttributeList();
	
	/**
	 * 根据商品类型获取已启用的商品属性.
	 * 
	 * @return 已启用的商品属性集合.
	 */
	public List<ProductAttribute> getEnabledProductAttributeList(ProductType productType);
	
	/**
	 * 根据商品类型、商品名称查找，若不存在则返回null
	 * 
	 * @param productType
	 *            商品类型
	 * 
	 * @param name
	 *            商品属性名称 
	 * 
	 */
	public ProductAttribute getProductAttribute(ProductType productType, String name);

}