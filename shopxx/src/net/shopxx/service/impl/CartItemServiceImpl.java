package net.shopxx.service.impl;

import javax.annotation.Resource;

import net.shopxx.dao.CartItemDao;
import net.shopxx.entity.CartItem;
import net.shopxx.service.CartItemService;

import org.springframework.stereotype.Service;

/**
 * Service实现类 - 品牌
 * ============================================================================
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得SHOP++商业授权之前，您不能将本软件应用于商业用途，否则SHOP++将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.shopxx.net
 * ----------------------------------------------------------------------------
 * KEY: SHOPXXB29936B2E4DBE58A5D88D80B8580D7A5
 * ============================================================================
 */

@Service
public class CartItemServiceImpl extends BaseServiceImpl<CartItem, String> implements CartItemService {

	@Resource
	public void setBaseDao(CartItemDao cartItemDao) {
		super.setBaseDao(cartItemDao);
	}

}