package net.shopxx.service.impl;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;

import net.shopxx.dao.FriendLinkDao;
import net.shopxx.entity.FriendLink;
import net.shopxx.service.FriendLinkService;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springmodules.cache.annotations.CacheFlush;
import org.springmodules.cache.annotations.Cacheable;

/**
 * Service实现类 - 友情链接
 * ============================================================================
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得SHOP++商业授权之前，您不能将本软件应用于商业用途，否则SHOP++将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.shopxx.net
 * ----------------------------------------------------------------------------
 * KEY: SHOPXXD710B9580A96A2B957C605ADE37619E2
 * ============================================================================
 */

@Service
public class FriendLinkServiceImpl extends BaseServiceImpl<FriendLink, String> implements FriendLinkService {
	
	@Resource
	FriendLinkDao friendLinkDao;

	@Resource
	public void setBaseDao(FriendLinkDao friendLinkDao) {
		super.setBaseDao(friendLinkDao);
	}
	
	@Cacheable(modelId = "caching")
	public List<FriendLink> getPictureFriendLinkList() {
		List<FriendLink> pictureFriendLinkList = friendLinkDao.getPictureFriendLinkList();
		if (pictureFriendLinkList != null) {
			for (FriendLink pictureFriendLink : pictureFriendLinkList) {
				Hibernate.initialize(pictureFriendLink);
			}
		}
		return pictureFriendLinkList;
	}
	
	@Cacheable(modelId = "caching")
	public List<FriendLink> getTextFriendLinkList() {
		List<FriendLink> textFriendLinkList = friendLinkDao.getTextFriendLinkList();
		if (textFriendLinkList != null) {
			for (FriendLink textFriendLink : textFriendLinkList) {
				Hibernate.initialize(textFriendLink);
			}
		}
		return textFriendLinkList;
	}
	
	@Override
	@Cacheable(modelId = "caching")
	public List<FriendLink> getAll() {
		List<FriendLink> allFriendLink = friendLinkDao.getAll();
		if (allFriendLink != null) {
			for (FriendLink friendLink : allFriendLink) {
				Hibernate.initialize(friendLink);
			}
		}
		return allFriendLink;
	}

	// 重写方法，删除同时删除Logo文件
	@Override
	@CacheFlush(modelId = "flushing")
	public void delete(FriendLink friendLink) {
		if (friendLink.getLogo() != null) {
			File logoFile = new File(ServletActionContext.getServletContext().getRealPath(friendLink.getLogo()));
			if (logoFile.exists()) {
				logoFile.delete();
			}
		}
		friendLinkDao.delete(friendLink);
	}

	// 重写方法，删除同时删除Logo文件
	@Override
	@CacheFlush(modelId = "flushing")
	public void delete(String id) {
		FriendLink friendLink = friendLinkDao.load(id);
		this.delete(friendLink);
	}

	// 重写方法，删除同时删除Logo文件
	@Override
	@CacheFlush(modelId = "flushing")
	public void delete(String[] ids) {
		for (String id : ids) {
			this.delete(id);
		}
	}

	@Override
	@CacheFlush(modelId = "flushing")
	public String save(FriendLink friendLink) {
		return friendLinkDao.save(friendLink);
	}

	@Override
	@CacheFlush(modelId = "flushing")
	public void update(FriendLink friendLink) {
		friendLinkDao.update(friendLink);
	}

}