package net.shopxx.dao;

import java.util.List;

import net.shopxx.entity.MemberAttribute;

/**
 * Dao接口 - 会员属性
 * ============================================================================
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得SHOP++商业授权之前，您不能将本软件应用于商业用途，否则SHOP++将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.shopxx.net
 * ----------------------------------------------------------------------------
 * KEY: SHOPXX7A5E44F9B13AA8536A1A807D94A5B481
 * ============================================================================
 */

public interface MemberAttributeDao extends BaseDao<MemberAttribute, String> {

	/**
	 * 获取已启用的会员注册项.
	 * 
	 * @return 已启用的会员注册项集合.
	 */
	public List<MemberAttribute> getEnabledMemberAttributeList();

}
