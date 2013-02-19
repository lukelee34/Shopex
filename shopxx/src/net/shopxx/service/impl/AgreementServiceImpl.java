package net.shopxx.service.impl;

import javax.annotation.Resource;

import net.shopxx.dao.AgreementDao;
import net.shopxx.entity.Agreement;
import net.shopxx.service.AgreementService;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springmodules.cache.annotations.CacheFlush;
import org.springmodules.cache.annotations.Cacheable;

/**
 * Service实现类 - 会员注册协议
 * ============================================================================
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得SHOP++商业授权之前，您不能将本软件应用于商业用途，否则SHOP++将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.shopxx.net
 * ----------------------------------------------------------------------------
 * KEY: SHOPXX992A2F1230488510985F95030CEAAFA3
 * ============================================================================
 */

@Service
public class AgreementServiceImpl extends BaseServiceImpl<Agreement, String> implements AgreementService {

	@Resource
	private AgreementDao agreementDao;

	@Resource
	public void setBaseDao(AgreementDao agreementDao) {
		super.setBaseDao(agreementDao);
	}

	@Cacheable(modelId = "caching")
	public Agreement getAgreement() {
		Agreement agreement = agreementDao.getAgreement();
		Hibernate.initialize(agreement);
		return agreement;
	}

	@Override
	@CacheFlush(modelId = "flushing")
	public void delete(Agreement entity) {
		agreementDao.delete(entity);
	}

	@Override
	@CacheFlush(modelId = "flushing")
	public void delete(String id) {
		agreementDao.delete(id);
	}

	@Override
	@CacheFlush(modelId = "flushing")
	public void delete(String[] ids) {
		agreementDao.delete(ids);
	}

	@Override
	@CacheFlush(modelId = "flushing")
	public String save(Agreement entity) {
		return agreementDao.save(entity);
	}

	@Override
	@CacheFlush(modelId = "flushing")
	public void update(Agreement entity) {
		agreementDao.update(entity);
	}

}
