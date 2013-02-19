package net.shopxx.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.shopxx.bean.HtmlConfig;
import net.shopxx.bean.Pager;
import net.shopxx.bean.ProductImage;
import net.shopxx.bean.Pager.OrderType;
import net.shopxx.dao.ProductDao;
import net.shopxx.entity.Member;
import net.shopxx.entity.Product;
import net.shopxx.entity.ProductCategory;
import net.shopxx.service.HtmlService;
import net.shopxx.service.ProductService;
import net.shopxx.util.TemplateConfigUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.compass.core.Compass;
import org.compass.core.CompassHits;
import org.compass.core.CompassQuery;
import org.compass.core.CompassQueryBuilder;
import org.compass.core.CompassSession;
import org.compass.core.CompassTemplate;
import org.compass.core.CompassQuery.SortDirection;
import org.compass.core.CompassQuery.SortPropertyType;
import org.compass.core.CompassQueryBuilder.CompassBooleanQueryBuilder;
import org.springframework.stereotype.Service;

/**
 * Service实现类 - 商品
 * ============================================================================
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得SHOP++商业授权之前，您不能将本软件应用于商业用途，否则SHOP++将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.shopxx.net
 * ----------------------------------------------------------------------------
 * KEY: SHOPXX603F6052C7BD6E1C6113519369127127
 * ============================================================================
 */

@Service
public class ProductServiceImpl extends BaseServiceImpl<Product, String> implements ProductService {

	@Resource
	private ProductDao productDao;
	@Resource
	private CompassTemplate compassTemplate;
	@Resource
	private HtmlService htmlService;

	@Resource
	public void setBaseDao(ProductDao productDao) {
		super.setBaseDao(productDao);
	}
	
	public List<Product> getProductList(ProductCategory productCategory) {
		return productDao.getProductList(productCategory);
	}
	
	public List<Product> getProductList(int firstResult, int maxResults) {
		return productDao.getProductList(firstResult, maxResults);
	}
	
	public List<Product> getProductList(ProductCategory productCategory, int firstResult, int maxResults) {
		return productDao.getProductList(productCategory, firstResult, maxResults);
	}
	
	public Pager getProductPager(ProductCategory productCategory, Pager pager) {
		return productDao.getProductPager(productCategory, pager);
	}
	
	public List<Product> getBestProductList(int maxResults) {
		return productDao.getBestProductList(maxResults);
	}

	public List<Product> getBestProductList(ProductCategory productCategory, int maxResults) {
		return productDao.getBestProductList(productCategory, maxResults);
	}
	
	public List<Product> getHotProductList(int maxResults) {
		return productDao.getHotProductList(maxResults);
	}

	public List<Product> getHotProductList(ProductCategory productCategory, int maxResults) {
		return productDao.getHotProductList(productCategory, maxResults);
	}
	
	public List<Product> getNewProductList(int maxResults) {
		return productDao.getNewProductList(maxResults);
	}

	public List<Product> getNewProductList(ProductCategory productCategory, int maxResults) {
		return productDao.getNewProductList(productCategory, maxResults);
	}
	
	public List<Product> getProductList(Date beginDate, Date endDate, int firstResult, int maxResults) {
		return productDao.getProductList(beginDate, endDate, firstResult, maxResults);
	}
	
	public Pager search(Pager pager) {
		Compass compass = compassTemplate.getCompass();
		CompassSession compassSession = compass.openSession();
		CompassQueryBuilder compassQueryBuilder = compassSession.queryBuilder();
		CompassBooleanQueryBuilder compassBooleanQueryBuilder = compassQueryBuilder.bool();

		CompassQuery compassQuery = compassBooleanQueryBuilder.addMust(compassQueryBuilder.term("isMarketable", true)).addMust(compassQueryBuilder.queryString("name:*" + pager.getKeyword() + "*").toQuery()).toQuery();
		
		if (StringUtils.isEmpty(pager.getOrderBy()) || pager.getOrderType() == null) {
			compassQuery.addSort("isBest", SortPropertyType.STRING, SortDirection.REVERSE)
			.addSort("isNew", SortPropertyType.STRING, SortDirection.REVERSE)
			.addSort("isHot", SortPropertyType.STRING, SortDirection.REVERSE);
		} else {
			if (pager.getOrderType() == OrderType.asc) {
				if(StringUtils.equalsIgnoreCase(pager.getOrderBy(), "price")) {
					compassQuery.addSort("price", SortPropertyType.FLOAT);
				}
			} else {
				if(StringUtils.equalsIgnoreCase(pager.getOrderBy(), "price")) {
					compassQuery.addSort("price", SortPropertyType.FLOAT, SortDirection.REVERSE);
				}
			}
		}
		
		CompassHits compassHits = compassQuery.hits();

		List<Product> productList = new ArrayList<Product>();

		int firstResult = (pager.getPageNumber() - 1) * pager.getPageSize();
		int maxReasults = pager.getPageSize();
		int totalCount = compassHits.length();

		int end = Math.min(totalCount, firstResult + maxReasults);
		for (int i = firstResult; i < end; i++) {
			Product product = (Product) compassHits.data(i);
			productList.add(product);
		}
		compassSession.close();
		pager.setList(productList);
		pager.setTotalCount(totalCount);
		return pager;
	}
	
	public Pager getFavoriteProductPager(Member member, Pager pager) {
		return productDao.getFavoriteProductPager(member, pager);
	}
	
	public Long getStoreAlertCount() {
		return productDao.getStoreAlertCount();
	}
	
	public Long getMarketableProductCount() {
		return productDao.getMarketableProductCount();
	}
	
	public Long getUnMarketableProductCount() {
		return productDao.getUnMarketableProductCount();
	}
	
	// 重写方法，删除对象的同时删除已生成的HTML静态文件、商品图片文件
	@Override
	public void delete(Product product) {
		File htmlFile = new File(ServletActionContext.getServletContext().getRealPath(product.getHtmlFilePath()));
		if (htmlFile.exists()) {
			htmlFile.delete();
		}
		List<ProductImage> productImageList = product.getProductImageList();
		if (productImageList != null) {
			for (ProductImage productImage : productImageList) {
				File sourceProductImageFile = new File(ServletActionContext.getServletContext().getRealPath(productImage.getSourceProductImagePath()));
				if (sourceProductImageFile.exists()) {
					sourceProductImageFile.delete();
				}
				File bigProductImageFile = new File(ServletActionContext.getServletContext().getRealPath(productImage.getBigProductImagePath()));
				if (bigProductImageFile.exists()) {
					bigProductImageFile.delete();
				}
				File smallProductImageFile = new File(ServletActionContext.getServletContext().getRealPath(productImage.getSmallProductImagePath()));
				if (smallProductImageFile.exists()) {
					smallProductImageFile.delete();
				}
				File thumbnailProductImageFile = new File(ServletActionContext.getServletContext().getRealPath(productImage.getThumbnailProductImagePath()));
				if (thumbnailProductImageFile.exists()) {
					thumbnailProductImageFile.delete();
				}
			}
		}
		productDao.delete(product);
	}

	// 重写方法，删除对象的同时删除已生成的HTML静态文件、商品图片文件
	@Override
	public void delete(String id) {
		Product product = productDao.load(id);
		this.delete(product);
	}

	// 重写方法，删除对象的同时删除已生成的HTML静态文件、商品图片文件
	@Override
	public void delete(String[] ids) {
		for (String id : ids) {
			this.delete(id);
		}
	}

	// 重写方法，保存对象的同时处理价格精度并生成HTML静态文件
	@Override
	public String save(Product product) {
		HtmlConfig htmlConfig = TemplateConfigUtil.getHtmlConfig(HtmlConfig.PRODUCT_CONTENT);
		String htmlFilePath = htmlConfig.getHtmlFilePath();
		product.setHtmlFilePath(htmlFilePath);
		String id = productDao.save(product);
		productDao.flush();
		productDao.evict(product);
		product = productDao.load(id);
		if (product.getIsMarketable()) {
			htmlService.productContentBuildHtml(product);
		}
		return id;
	}

	// 重写方法，更新对象的同时处理价格精度并重新生成HTML静态文件
	@Override
	public void update(Product product) {
		String id = product.getId();
		File htmlFile = new File(ServletActionContext.getServletContext().getRealPath(product.getHtmlFilePath()));
		if (htmlFile.exists()) {
			htmlFile.delete();
		}
		productDao.update(product);
		productDao.flush();
		productDao.evict(product);
		product = productDao.load(id);
		if (product.getIsMarketable()) {
			htmlService.productContentBuildHtml(product);
		}
	}

}