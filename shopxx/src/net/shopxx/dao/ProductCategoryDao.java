package net.shopxx.dao;

import java.util.List;

import net.shopxx.entity.ProductCategory;

/**
 * Dao接口 - 商品分类
 * ============================================================================
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得SHOP++商业授权之前，您不能将本软件应用于商业用途，否则SHOP++将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.shopxx.net
 * ----------------------------------------------------------------------------
 * KEY: SHOPXXD05276A7E5664AF1D9750EF7CEB8D92E
 * ============================================================================
 */

public interface ProductCategoryDao extends BaseDao<ProductCategory, String> {
	
	/**
	 * 获取所有顶级商品分类集合;
	 * 
	 * @return 所有顶级商品分类集合
	 * 
	 */
	public List<ProductCategory> getRootProductCategoryList();
	
	/**
	 * 根据ProductCategory对象获取所有父类集合，若无父类则返回null;
	 * 
	 * @return 父类集合
	 * 
	 */
	public List<ProductCategory> getParentProductCategoryList(ProductCategory productCategory);
	
	/**
	 * 根据ProductCategory对象获取所有子类集合，若无子类则返回null;
	 * 
	 * @return 子类集合
	 * 
	 */
	public List<ProductCategory> getChildrenProductCategoryList(ProductCategory productCategory);

}
