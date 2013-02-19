package net.shopxx.service;

import java.util.Map;

import net.shopxx.entity.Article;
import net.shopxx.entity.Product;

/**
 * Service接口 - 生成静态
 * ============================================================================
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得SHOP++商业授权之前，您不能将本软件应用于商业用途，否则SHOP++将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.shopxx.net
 * ----------------------------------------------------------------------------
 * KEY: SHOPXX768EA68E875E50CB2965E6DFD607D2CA
 * ============================================================================
 */

public interface HtmlService {
	
	/**
	 * 根据Freemarker模板文件路径、Map数据生成HTML静态文件
	 * 
	 * @param templateFilePath
	 *            Freemarker模板文件路径
	 *            
	 * @param htmlFilePath
	 *            生成HTML静态文件存放路径
	 * 
	 * @param data
	 *            Map数据
	 * 
	 */
	public void buildHtml(String templateFilePath, String htmlFilePath, Map<String, Object> data);
	
	/**
	 * 生成baseJavascript文件
	 * 
	 */
	public void baseJavascriptBuildHtml();
	
	/**
	 * 生成首页HTML静态文件
	 * 
	 */
	public void indexBuildHtml();
	
	/**
	 * 生成登录HTML静态文件
	 * 
	 */
	public void loginBuildHtml();
	
	/**
	 * 根据Article对象生成文章内容HTML静态文件
	 * 
	 * @param article
	 *            文章
	 */
	public void articleContentBuildHtml(Article article);
	
	/**
	 * 根据Product对象生成产品内容HTML静态文件
	 * 
	 * @param product
	 *            商品
	 */
	public void productContentBuildHtml(Product product);
	
	/**
	 * 错误页HTML静态文件
	 */
	public void errorPageBuildHtml();
	
	/**
	 * 权限错误页HTML静态文件
	 */
	public void errorPageAccessDeniedBuildHtml();
	
	/**
	 * 错误页500 HTML静态文件
	 */
	public void errorPage500BuildHtml();
	
	/**
	 * 错误页404 HTML静态文件
	 */
	public void errorPage404BuildHtml();
	
	/**
	 * 错误页403 HTML静态文件
	 */
	public void errorPage403BuildHtml();
	
}