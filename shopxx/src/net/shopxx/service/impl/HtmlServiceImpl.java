package net.shopxx.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import net.shopxx.bean.HtmlConfig;
import net.shopxx.bean.SystemConfig;
import net.shopxx.dao.ArticleDao;
import net.shopxx.dao.ProductDao;
import net.shopxx.entity.Article;
import net.shopxx.entity.ArticleCategory;
import net.shopxx.entity.Product;
import net.shopxx.entity.ProductCategory;
import net.shopxx.service.ArticleCategoryService;
import net.shopxx.service.FooterService;
import net.shopxx.service.FriendLinkService;
import net.shopxx.service.HtmlService;
import net.shopxx.service.NavigationService;
import net.shopxx.service.ProductCategoryService;
import net.shopxx.util.SystemConfigUtil;
import net.shopxx.util.TemplateConfigUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.views.freemarker.FreemarkerManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.ResourceBundleModel;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Service实现类 - 生成静态
 * ============================================================================
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得SHOP++商业授权之前，您不能将本软件应用于商业用途，否则SHOP++将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.shopxx.net
 * ----------------------------------------------------------------------------
 * KEY: SHOPXXE7656ABEA5A60D076604DDCE20B403C9
 * ============================================================================
 */

@Service
public class HtmlServiceImpl implements HtmlService {

	@Resource
	private FreemarkerManager freemarkerManager;
	@Resource
	private NavigationService navigationService;
	@Resource
	private FriendLinkService friendLinkService;
	@Resource
	private FooterService footerService;
	@Resource
	private ArticleCategoryService articleCategoryService;
	@Resource
	private ArticleDao articleDao;
	@Resource
	ProductCategoryService productCategoryService;
	@Resource
	private ProductDao productDao;
	
	public void buildHtml(String templateFilePath, String htmlFilePath, Map<String, Object> data) {
		try {
			ServletContext servletContext = ServletActionContext.getServletContext();
			Configuration configuration = freemarkerManager.getConfiguration(servletContext);
			Template template = configuration.getTemplate(templateFilePath);
			File htmlFile = new File(servletContext.getRealPath(htmlFilePath));
			File htmlDirectory = htmlFile.getParentFile();
			if (!htmlDirectory.exists()) {
				htmlDirectory.mkdirs();
			}
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(htmlFile), "UTF-8"));
			template.process(data, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 获取公共数据
	public Map<String, Object> getCommonData() {
		Map<String, Object> commonData = new HashMap<String, Object>();
		ServletContext servletContext = ServletActionContext.getServletContext();
		ResourceBundle resourceBundle = ResourceBundle.getBundle("i18n");
		ResourceBundleModel resourceBundleModel = new ResourceBundleModel(resourceBundle, new BeansWrapper());
		SystemConfig systemConfig = SystemConfigUtil.getSystemConfig();
		
		String priceCurrencyFormat = SystemConfigUtil.getPriceCurrencyFormat();
		String priceUnitCurrencyFormat = SystemConfigUtil.getPriceUnitCurrencyFormat();
		
		String orderCurrencyFormat = SystemConfigUtil.getOrderCurrencyFormat();
		String orderUnitCurrencyFormat = SystemConfigUtil.getOrderUnitCurrencyFormat();
		
		commonData.put("bundle", resourceBundleModel);
		commonData.put("base", servletContext.getContextPath());
		commonData.put("systemConfig", systemConfig);
		commonData.put("priceCurrencyFormat", priceCurrencyFormat);
		commonData.put("priceUnitCurrencyFormat", priceUnitCurrencyFormat);
		commonData.put("orderCurrencyFormat", orderCurrencyFormat);
		commonData.put("orderUnitCurrencyFormat", orderUnitCurrencyFormat);
		commonData.put("topNavigationList", navigationService.getTopNavigationList());
		commonData.put("middleNavigationList", navigationService.getMiddleNavigationList());
		commonData.put("bottomNavigationList", navigationService.getBottomNavigationList());
		commonData.put("friendLinkList", friendLinkService.getAll());
		commonData.put("pictureFriendLinkList", friendLinkService.getPictureFriendLinkList());
		commonData.put("textFriendLinkList", friendLinkService.getTextFriendLinkList());
		commonData.put("footer", footerService.getFooter());
		return commonData;
	}
	
	public void baseJavascriptBuildHtml() {
		Map<String, Object> data = getCommonData();
		HtmlConfig htmlConfig = TemplateConfigUtil.getHtmlConfig(HtmlConfig.BASE_JAVASCRIPT);
		String htmlFilePath = htmlConfig.getHtmlFilePath();
		String templateFilePath = htmlConfig.getTemplateFilePath();
		buildHtml(templateFilePath, htmlFilePath, data);
	}
	
	public void indexBuildHtml() {
		HtmlConfig htmlConfig = TemplateConfigUtil.getHtmlConfig(HtmlConfig.INDEX);
		Map<String, Object> data = getCommonData();
		data.put("rootProductCategoryList", productCategoryService.getRootProductCategoryList());
		data.put("bestProductList", productDao.getBestProductList(Product.MAX_BEST_PRODUCT_LIST_COUNT));
		data.put("hotProductList", productDao.getHotProductList(Product.MAX_HOT_PRODUCT_LIST_COUNT));
		data.put("newProductList", productDao.getNewProductList(Product.MAX_NEW_PRODUCT_LIST_COUNT));
		List<ProductCategory> allProductCategory = productCategoryService.getAll();
		data.put("allProductCategoryList", allProductCategory);
		Map<String, List<ProductCategory>> productCategoryMap = new HashMap<String, List<ProductCategory>>();
		Map<String, List<Product>> bestProductMap = new HashMap<String, List<Product>>();
		Map<String, List<Product>> hotProductMap = new HashMap<String, List<Product>>();
		Map<String, List<Product>> newProductMap = new HashMap<String, List<Product>>();
		for (ProductCategory productCategory : allProductCategory) {
			productCategoryMap.put(productCategory.getId(), productCategoryService.getChildrenProductCategoryList(productCategory));
			bestProductMap.put(productCategory.getId(), productDao.getBestProductList(productCategory, Product.MAX_BEST_PRODUCT_LIST_COUNT));
			hotProductMap.put(productCategory.getId(), productDao.getHotProductList(productCategory, Product.MAX_HOT_PRODUCT_LIST_COUNT));
			newProductMap.put(productCategory.getId(), productDao.getNewProductList(productCategory, Product.MAX_NEW_PRODUCT_LIST_COUNT));
		}
		data.put("productCategoryMap", productCategoryMap);
		data.put("bestProductMap", bestProductMap);
		data.put("hotProductMap", hotProductMap);
		data.put("newProductMap", newProductMap);
		
		data.put("rootArticleCategoryList", articleCategoryService.getRootArticleCategoryList());
		data.put("recommendArticleList", articleDao.getRecommendArticleList(Article.MAX_RECOMMEND_ARTICLE_LIST_COUNT));
		data.put("hotArticleList", articleDao.getHotArticleList(Article.MAX_HOT_ARTICLE_LIST_COUNT));
		data.put("newArticleList", articleDao.getNewArticleList(Article.MAX_NEW_ARTICLE_LIST_COUNT));
		List<ArticleCategory> allArticleCategory = articleCategoryService.getAll();
		data.put("allArticleCategoryList", allArticleCategory);
		Map<String, List<ArticleCategory>> articleCategoryMap = new HashMap<String, List<ArticleCategory>>();
		Map<String, List<Article>> recommendArticleMap = new HashMap<String, List<Article>>();
		Map<String, List<Article>> hotArticleMap = new HashMap<String, List<Article>>();
		Map<String, List<Article>> newArticleMap = new HashMap<String, List<Article>>();
		for (ArticleCategory articleCategory : allArticleCategory) {
			articleCategoryMap.put(articleCategory.getId(), articleCategoryService.getChildrenArticleCategoryList(articleCategory));
			recommendArticleMap.put(articleCategory.getId(), articleDao.getRecommendArticleList(articleCategory, Article.MAX_RECOMMEND_ARTICLE_LIST_COUNT));
			hotArticleMap.put(articleCategory.getId(), articleDao.getHotArticleList(articleCategory, Article.MAX_HOT_ARTICLE_LIST_COUNT));
			newArticleMap.put(articleCategory.getId(), articleDao.getNewArticleList(articleCategory, Article.MAX_NEW_ARTICLE_LIST_COUNT));
		}
		data.put("articleCategoryMap", articleCategoryMap);
		data.put("recommendArticleMap", recommendArticleMap);
		data.put("hotArticleMap", hotArticleMap);
		data.put("newArticleMap", newArticleMap);
		
		String htmlFilePath = htmlConfig.getHtmlFilePath();
		String templateFilePath = htmlConfig.getTemplateFilePath();
		buildHtml(templateFilePath, htmlFilePath, data);
	}
	
	public void loginBuildHtml() {
		HtmlConfig htmlConfig = TemplateConfigUtil.getHtmlConfig(HtmlConfig.LOGIN);
		Map<String, Object> data = getCommonData();
		String htmlFilePath = htmlConfig.getHtmlFilePath();
		String templateFilePath = htmlConfig.getTemplateFilePath();
		buildHtml(templateFilePath, htmlFilePath, data);
	}
	
	@Transactional
	public void articleContentBuildHtml(Article article) {
		HtmlConfig htmlConfig = TemplateConfigUtil.getHtmlConfig(HtmlConfig.ARTICLE_CONTENT);
		ArticleCategory articleCategory = article.getArticleCategory();
		Map<String, Object> data = getCommonData();
		data.put("article", article);
		data.put("pathList", articleCategoryService.getArticleCategoryPathList(article));
		data.put("rootArticleCategoryList", articleCategoryService.getRootArticleCategoryList());
		data.put("recommendArticleList", articleDao.getRecommendArticleList(articleCategory, Article.MAX_RECOMMEND_ARTICLE_LIST_COUNT));
		data.put("hotArticleList", articleDao.getHotArticleList(articleCategory, Article.MAX_HOT_ARTICLE_LIST_COUNT));
		data.put("newArticleList", articleDao.getNewArticleList(articleCategory, Article.MAX_NEW_ARTICLE_LIST_COUNT));
		String htmlFilePath = article.getHtmlFilePath();
		String prefix = StringUtils.substringBeforeLast(htmlFilePath, ".");
		String extension = StringUtils.substringAfterLast(htmlFilePath, ".");
		List<String> pageContentList = article.getPageContentList();
		article.setPageCount(pageContentList.size());
		articleDao.update(article);
		articleDao.flush();
		for (int i = 0; i < pageContentList.size(); i++) {
			data.put("content", pageContentList.get(i));
			data.put("pageNumber", i + 1);
			data.put("pageCount", pageContentList.size());
			String templateFilePath = htmlConfig.getTemplateFilePath();
			String currentHtmlFilePath = null;
			if (i == 0) {
				currentHtmlFilePath = htmlFilePath;
			} else {
				currentHtmlFilePath = prefix + "_" + (i + 1) + "." + extension;
			}
			buildHtml(templateFilePath, currentHtmlFilePath, data);
		}
	}
	
	public void productContentBuildHtml(Product product) {
		HtmlConfig htmlConfig = TemplateConfigUtil.getHtmlConfig(HtmlConfig.PRODUCT_CONTENT);
		ProductCategory productCategory = product.getProductCategory();
		Map<String, Object> data = getCommonData();
		data.put("product", product);
		data.put("pathList", productCategoryService.getProductCategoryPathList(product));
		data.put("rootProductCategoryList", productCategoryService.getRootProductCategoryList());
		data.put("bestProductList", productDao.getBestProductList(productCategory, Product.MAX_BEST_PRODUCT_LIST_COUNT));
		data.put("hotProductList", productDao.getHotProductList(productCategory, Product.MAX_HOT_PRODUCT_LIST_COUNT));
		data.put("newProductList", productDao.getNewProductList(productCategory, Product.MAX_NEW_PRODUCT_LIST_COUNT));
		String htmlFilePath = product.getHtmlFilePath();
		String templateFilePath = htmlConfig.getTemplateFilePath();
		buildHtml(templateFilePath, htmlFilePath, data);
	}
	
	public void errorPageBuildHtml() {
		HtmlConfig htmlConfig = TemplateConfigUtil.getHtmlConfig(HtmlConfig.ERROR_PAGE);
		Map<String, Object> data = getCommonData();
		data.put("errorContent", "系统出现异常，请与管理员联系！");
		String htmlFilePath = htmlConfig.getHtmlFilePath();
		String templateFilePath = htmlConfig.getTemplateFilePath();
		buildHtml(templateFilePath, htmlFilePath, data);
	}
	
	public void errorPageAccessDeniedBuildHtml() {
		HtmlConfig htmlConfig = TemplateConfigUtil.getHtmlConfig(HtmlConfig.ERROR_PAGE);
		Map<String, Object> data = getCommonData();
		data.put("errorContent", "您无此访问权限！");
		String htmlFilePath = htmlConfig.getHtmlFilePath();
		String templateFilePath = htmlConfig.getTemplateFilePath();
		buildHtml(templateFilePath, htmlFilePath, data);
	}
	
	public void errorPage500BuildHtml() {
		HtmlConfig htmlConfig = TemplateConfigUtil.getHtmlConfig(HtmlConfig.ERROR_PAGE_500);
		Map<String, Object> data = getCommonData();
		data.put("errorContent", "系统出现异常，请与管理员联系！");
		String htmlFilePath = htmlConfig.getHtmlFilePath();
		String templateFilePath = htmlConfig.getTemplateFilePath();
		buildHtml(templateFilePath, htmlFilePath, data);
	}
	
	public void errorPage404BuildHtml() {
		HtmlConfig htmlConfig = TemplateConfigUtil.getHtmlConfig(HtmlConfig.ERROR_PAGE_404);
		Map<String, Object> data = getCommonData();
		data.put("errorContent", "您访问的页面不存在！");
		String htmlFilePath = htmlConfig.getHtmlFilePath();
		String templateFilePath = htmlConfig.getTemplateFilePath();
		buildHtml(templateFilePath, htmlFilePath, data);
	}
	
	public void errorPage403BuildHtml() {
		HtmlConfig htmlConfig = TemplateConfigUtil.getHtmlConfig(HtmlConfig.ERROR_PAGE_403);
		Map<String, Object> data = getCommonData();
		data.put("errorContent", "系统出现异常，请与管理员联系！");
		String htmlFilePath = htmlConfig.getHtmlFilePath();
		String templateFilePath = htmlConfig.getTemplateFilePath();
		buildHtml(templateFilePath, htmlFilePath, data);
	}

}