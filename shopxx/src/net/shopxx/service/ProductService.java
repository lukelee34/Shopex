package net.shopxx.service;

import java.util.Date;
import java.util.List;

import net.shopxx.bean.Pager;
import net.shopxx.entity.Member;
import net.shopxx.entity.Product;
import net.shopxx.entity.ProductCategory;

/**
 * Service接口 - 商品
 * ============================================================================
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得SHOP++商业授权之前，您不能将本软件应用于商业用途，否则SHOP++将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.shopxx.net
 * ----------------------------------------------------------------------------
 * KEY: SHOPXX62F330D4576F1CE472FE4A72A3C1070A
 * ============================================================================
 */

public interface ProductService extends BaseService<Product, String> {

	/**
	 * 根据ProductCategory对象，获取此分类下的所有商品（只包含isMarketable=true的对象，包含子分类商品）
	 * 
	 * @param productCategory
	 *            商品分类
	 * 
	 * @return 此分类下的所有商品集合
	 */
	public List<Product> getProductList(ProductCategory productCategory);
	
	/**
	 * 根据起始结果数、最大结果数，获取所有商品（只包含isMarketable=true的对象）
	 * 
	 * @param firstResult
	 *            起始结果数
	 * 
	 * @param maxResults
	 *            最大结果数
	 * 
	 * @return 此分类下的所有商品集合
	 */
	public List<Product> getProductList(int firstResult, int maxResults);
	
	/**
	 * 根据ProductCategory对象、起始结果数、最大结果数，获取此分类下的所有文章（只包含isMarketable=true的对象，包含子分类商品）
	 * 
	 * @param productCategory
	 *            商品分类
	 * 
	 * @param firstResult
	 *            起始结果数
	 * 
	 * @param maxResults
	 *            最大结果数
	 * 
	 * @return 此分类下的所有商品集合
	 */
	public List<Product> getProductList(ProductCategory productCategory, int firstResult, int maxResults);
	
	/**
	 * 根据起始日期、结束日期、起始结果数、最大结果数，获取商品集合（只包含isMarketable=true的对象）
	 * 
	 * @param beginDate
	 *            起始日期
	 * 
	 * @param endDate
	 *            结束日期
	 * 
	 * @param firstResult
	 *            起始结果数
	 * 
	 * @param maxResults
	 *            最大结果数
	 * 
	 * @return 此分类下的所有商品集合
	 */
	public List<Product> getProductList(Date beginDate, Date endDate, int firstResult, int maxResults);
	
	/**
	 * 根据ProductCategory和Pager对象，获取此分类下的商品分页对象（只包含isMarketable=true的对象，包含子分类商品）
	 * 
	 * @param productCategory
	 *            商品分类
	 * 
	 * @param pager
	 *            分页对象
	 * 
	 * @return Pager
	 */
	public Pager getProductPager(ProductCategory productCategory, Pager pager);
	
	/**
	 * 根据最大返回数获取所有精品商品(只包含isMarketable=true的对象，不限分类)
	 * 
	 * @param maxResults
	 *            最大返回数
	 * 
	 * @return 所有精品商品集合
	 */
	public List<Product> getBestProductList(int maxResults);

	/**
	 * 根据ProductCategory对象和最大返回数获取此分类下的所有精品商品(只包含isMarketable=true的对象，包含子分类商品)
	 * 
	 * @param productCategory
	 *            商品分类
	 *            
	 * @param maxResults
	 *            最大返回数
	 * 
	 * @return 此分类下的所有精品商品集合
	 */
	public List<Product> getBestProductList(ProductCategory productCategory, int maxResults);
	
	/**
	 * 根据最大返回数获取所有热卖商品(只包含isMarketable=true的对象，不限分类)
	 * 
	 * @param maxResults
	 *            最大返回数
	 * 
	 * @return 所有热卖商品集合
	 */
	public List<Product> getHotProductList(int maxResults);

	/**
	 * 根据ProductCategory对象和最大返回数获取此分类下的所有热卖商品(只包含isMarketable=true的对象，包含子分类商品)
	 * 
	 * @param productCategory
	 *            商品分类
	 * 
	 * @param maxResults
	 *            最大返回数
	 * 
	 * @return 此分类下的所有热卖商品集合
	 */
	public List<Product> getHotProductList(ProductCategory productCategory, int maxResults);
	
	/**
	 * 根据最大返回数获取最新商品(只包含isMarketable=true的对象，不限分类)
	 * 
	 * @param maxResults
	 *            最大返回数
	 * 
	 * @return 最新商品集合
	 */
	public List<Product> getNewProductList(int maxResults);

	/**
	 * 根据ProductCategory对象和最大返回数获取此分类下的最新商品(只包含isMarketable=true的对象，包含子分类商品)
	 * 
	 * @param productCategory
	 *            商品分类
	 * 
	 * @param maxResults
	 *            最大返回数
	 * 
	 * @return 此分类下的最新商品集合
	 */
	public List<Product> getNewProductList(ProductCategory productCategory, int maxResults);
	
	/**
	 * 根据分页对象搜索文章
	 * 
	 * @return 分页对象
	 */
	public Pager search(Pager pager);
	
	/**
	 * 根据Member、Pager获取收藏商品分页对象
	 * 
	 * @param member
	 *            Member对象
	 *            
	 * @return 收藏商品分页对象
	 */
	public Pager getFavoriteProductPager(Member member, Pager pager);
	
	/**
	 * 获取商品库存报警数
	 *            
	 * @return 商品库存报警数
	 */
	public Long getStoreAlertCount();
	
	/**
	 * 获取已上架商品数
	 *            
	 * @return 已上架商品数
	 */
	public Long getMarketableProductCount();
	
	/**
	 * 获取已下架商品数
	 *            
	 * @return 已下架商品数
	 */
	public Long getUnMarketableProductCount();

}