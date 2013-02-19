package net.shopxx.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.shopxx.dao.ArticleCategoryDao;
import net.shopxx.entity.Article;
import net.shopxx.entity.ArticleCategory;
import net.shopxx.service.ArticleCategoryService;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springmodules.cache.annotations.CacheFlush;
import org.springmodules.cache.annotations.Cacheable;

/**
 * Service实现类 - 文章分类
 * ============================================================================
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得SHOP++商业授权之前，您不能将本软件应用于商业用途，否则SHOP++将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.shopxx.net
 * ----------------------------------------------------------------------------
 * KEY: SHOPXXCD623CA67EE561A340B34E79BEDEE5CE
 * ============================================================================
 */

@Service
public class ArticleCategoryServiceImpl extends BaseServiceImpl<ArticleCategory, String> implements ArticleCategoryService {

	@Resource
	private ArticleCategoryDao articleCategoryDao;

	@Resource
	public void setBaseDao(ArticleCategoryDao articleCategoryDao) {
		super.setBaseDao(articleCategoryDao);
	}
	
	@Cacheable(modelId = "caching")
	public List<ArticleCategory> getRootArticleCategoryList() {
		List<ArticleCategory> rootArticleCategoryList = articleCategoryDao.getRootArticleCategoryList();
		if (rootArticleCategoryList != null) {
			for (ArticleCategory rootArticleCategory : rootArticleCategoryList) {
				Hibernate.initialize(rootArticleCategory);
			}
		}
		return rootArticleCategoryList;
	}
	
	@Cacheable(modelId = "caching")
	public List<ArticleCategory> getParentArticleCategoryList(ArticleCategory articleCategory) {
		List<ArticleCategory> parentArticleCategoryList = articleCategoryDao.getParentArticleCategoryList(articleCategory);
		if (parentArticleCategoryList != null) {
			for (ArticleCategory parentArticleCategory : parentArticleCategoryList) {
				Hibernate.initialize(parentArticleCategory);
			}
		}
		return parentArticleCategoryList;
	}
	
	public List<ArticleCategory> getParentArticleCategoryList(Article article) {
		ArticleCategory articleCategory = article.getArticleCategory();
		List<ArticleCategory> articleCategoryList = new ArrayList<ArticleCategory>();
		articleCategoryList.addAll(this.getParentArticleCategoryList(articleCategory));
		articleCategoryList.add(articleCategory);
		return articleCategoryList;
	}
	
	public List<ArticleCategory> getArticleCategoryPathList(ArticleCategory articleCategory) {
		List<ArticleCategory> articleCategoryPathList = new ArrayList<ArticleCategory>();
		articleCategoryPathList.addAll(this.getParentArticleCategoryList(articleCategory));
		articleCategoryPathList.add(articleCategory);
		return articleCategoryPathList;
	}
	
	public List<ArticleCategory> getArticleCategoryPathList(Article article) {
		ArticleCategory articleCategory = article.getArticleCategory();
		List<ArticleCategory> articleCategoryList = new ArrayList<ArticleCategory>();
		articleCategoryList.addAll(this.getParentArticleCategoryList(articleCategory));
		articleCategoryList.add(articleCategory);
		return articleCategoryList;
	}
	
	@Cacheable(modelId = "caching")
	public List<ArticleCategory> getChildrenArticleCategoryList(ArticleCategory articleCategory) {
		List<ArticleCategory> childrenArticleCategoryList = articleCategoryDao.getChildrenArticleCategoryList(articleCategory);
		if (childrenArticleCategoryList != null) {
			for (ArticleCategory childrenArticleCategory : childrenArticleCategoryList) {
				Hibernate.initialize(childrenArticleCategory);
			}
		}
		return childrenArticleCategoryList;
	}
	
	public List<ArticleCategory> getChildrenArticleCategoryList(Article article) {
		ArticleCategory articleCategory = article.getArticleCategory();
		List<ArticleCategory> articleCategoryList = getChildrenArticleCategoryList(articleCategory);
		if (articleCategoryList == null) {
			articleCategoryList = new ArrayList<ArticleCategory>();
		}
		articleCategoryList.add(articleCategory);
		return articleCategoryList;
	}
	
	@Cacheable(modelId = "caching")
	public List<ArticleCategory> getArticleCategoryTreeList() {
		List<ArticleCategory> allArticleCategoryList = this.getAll();
		return recursivArticleCategoryTreeList(allArticleCategoryList, null, null);
	}
	
	// 递归父类排序分类树
	private List<ArticleCategory> recursivArticleCategoryTreeList(List<ArticleCategory> allArticleCategoryList, ArticleCategory p, List<ArticleCategory> temp) {
		if (temp == null) {
			temp = new ArrayList<ArticleCategory>();
		}
		for (ArticleCategory articleCategory : allArticleCategoryList) {
			ArticleCategory parent = articleCategory.getParent();
			if ((p == null && parent == null) || (articleCategory != null && parent == p)) {
				temp.add(articleCategory);
				if (articleCategory.getChildren() != null && articleCategory.getChildren().size() > 0) {
					recursivArticleCategoryTreeList(allArticleCategoryList, articleCategory, temp);
				}
			}
		}
		return temp;
	}

	@Override
	@Cacheable(modelId = "caching")
	public List<ArticleCategory> getAll() {
		List<ArticleCategory> allArticleCategoryList = articleCategoryDao.getAll();
		if (allArticleCategoryList != null) {
			for (ArticleCategory articleCategory : allArticleCategoryList) {
				Hibernate.initialize(articleCategory);
			}
		}
		return allArticleCategoryList;
	}

	@Override
	@CacheFlush(modelId = "flushing")
	public void delete(ArticleCategory articleCategory) {
		articleCategoryDao.delete(articleCategory);
	}

	@Override
	@CacheFlush(modelId = "flushing")
	public void delete(String id) {
		articleCategoryDao.delete(id);
	}

	@Override
	@CacheFlush(modelId = "flushing")
	public void delete(String[] ids) {
		articleCategoryDao.delete(ids);
	}

	@Override
	@CacheFlush(modelId = "flushing")
	public String save(ArticleCategory articleCategory) {
		return articleCategoryDao.save(articleCategory);
	}

	@Override
	@CacheFlush(modelId = "flushing")
	public void update(ArticleCategory articleCategory) {
		articleCategoryDao.update(articleCategory);
	}

}