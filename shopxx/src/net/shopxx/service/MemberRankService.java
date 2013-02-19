package net.shopxx.service;

import net.shopxx.entity.MemberRank;

/**
 * Service接口 - 会员分类
 * ============================================================================
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得SHOP++商业授权之前，您不能将本软件应用于商业用途，否则SHOP++将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.shopxx.net
 * ----------------------------------------------------------------------------
 * KEY: SHOPXXC9F59342352D3FB31197AAAE5268EBCF
 * ============================================================================
 */

public interface MemberRankService extends BaseService<MemberRank, String> {
	
	/**
	 * 获取默认会员等级,若无默认会员等级,则获取最先添加的会员等级.
	 * 
	 */
	public MemberRank getDefaultMemberRank();
	
	/**
	 * 根据积分获取会员等级，若不存在则返回null
	 * 
	 */
	public MemberRank getMemberRankByPoint(Integer point);
	
	/**
	 * 根据积分获取符合此积分条件的最高会员等级，若不存在则返回null
	 * 
	 */
	public MemberRank getUpMemberRankByPoint(Integer point);

}