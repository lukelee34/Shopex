package net.shopxx.entity;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.shopxx.bean.TenpayConfig;
import net.shopxx.util.SystemConfigUtil;

import org.apache.commons.lang.StringUtils;

/**
 * 实体类 - 支付配置
 * ============================================================================
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得SHOP++商业授权之前，您不能将本软件应用于商业用途，否则SHOP++将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.shopxx.net
 * ----------------------------------------------------------------------------
 * KEY: SHOPXXDA3F0780A7AFA10368BF915483346760
 * ============================================================================
 */

@Entity
public class PaymentConfig extends BaseEntity {

	private static final long serialVersionUID = -7950849648189504426L;
	
	// 支付配置类型（预存款、线下支付、财付通）
	public enum PaymentConfigType {
		deposit, offline, tenpay
	};
	
	// 支付手续费类型（按比例收费、固定费用）
	public enum PaymentFeeType {
		scale, fixed
	}
	
	private PaymentConfigType paymentConfigType;// 支付配置类型
	private String name;// 支付方式名称
	private PaymentFeeType paymentFeeType;// 支付手续费类型
	private BigDecimal paymentFee;// 支付费用
	private String description;// 介绍
	private Integer orderList;// 排序
	private String configObjectStore;// 配置对象信息储存
	
	private Set<Order> orderSet;// 订单
	private Set<Payment> paymentSet;// 支付
	private Set<Refund> refundSet;// 退款
	
	@Enumerated
	@Column(nullable = false)
	public PaymentConfigType getPaymentConfigType() {
		return paymentConfigType;
	}

	public void setPaymentConfigType(PaymentConfigType paymentConfigType) {
		this.paymentConfigType = paymentConfigType;
	}
	
	@Column(nullable = false, unique = true)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Enumerated
	@Column(nullable = false)
	public PaymentFeeType getPaymentFeeType() {
		return paymentFeeType;
	}

	public void setPaymentFeeType(PaymentFeeType paymentFeeType) {
		this.paymentFeeType = paymentFeeType;
	}

	@Column(precision = 15, scale = 5)
	public BigDecimal getPaymentFee() {
		return paymentFee;
	}

	// 精度处理
	public void setPaymentFee(BigDecimal paymentFee) {
		this.paymentFee = SystemConfigUtil.getPriceScaleBigDecimal(paymentFee);
	}

	@Column(length = 10000)
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(nullable = false)
	public Integer getOrderList() {
		return orderList;
	}
	
	public void setOrderList(Integer orderList) {
		this.orderList = orderList;
	}
	
	public String getConfigObjectStore() {
		return configObjectStore;
	}

	public void setConfigObjectStore(String configObjectStore) {
		this.configObjectStore = configObjectStore;
	}
	
	@OneToMany(mappedBy = "paymentConfig", fetch = FetchType.LAZY)
	public Set<Order> getOrderSet() {
		return orderSet;
	}

	public void setOrderSet(Set<Order> orderSet) {
		this.orderSet = orderSet;
	}

	@OneToMany(mappedBy = "paymentConfig", fetch = FetchType.LAZY)
	public Set<Payment> getPaymentSet() {
		return paymentSet;
	}

	public void setPaymentSet(Set<Payment> paymentSet) {
		this.paymentSet = paymentSet;
	}

	@OneToMany(mappedBy = "paymentConfig", fetch = FetchType.LAZY)
	public Set<Refund> getRefundSet() {
		return refundSet;
	}

	public void setRefundSet(Set<Refund> refundSet) {
		this.refundSet = refundSet;
	}

	// 获取配置对象
	@Transient
	public Object getConfigObject() {
		if (StringUtils.isEmpty(configObjectStore)) {
			return null;
		}
		JsonConfig jsonConfig = new JsonConfig();
		JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(configObjectStore);
		if (paymentConfigType == PaymentConfigType.deposit) {
			return null;
		} else if (paymentConfigType == PaymentConfigType.offline) {
			return null;
		} else if (paymentConfigType == PaymentConfigType.tenpay) {
			jsonConfig.setRootClass(TenpayConfig.class);
			return (TenpayConfig) JSONSerializer.toJava(jsonObject, jsonConfig);
		}
		return null;
	}
	
	// 设置配置对象
	@Transient
	public void setConfigObject(Object object) {
		if (object == null) {
			configObjectStore = null;
			return;
		}
		JSONObject jsonObject = JSONObject.fromObject(object);
		if (paymentConfigType == PaymentConfigType.deposit) {
			configObjectStore = null;
		} else if (paymentConfigType == PaymentConfigType.offline) {
			configObjectStore = null;
		} else if (paymentConfigType == PaymentConfigType.tenpay) {
			configObjectStore = jsonObject.toString();
		}
	}
	
	/**
	 * 根据总金额计算支付费用
	 * 
	 * @return 支付费用
	 */
	@Transient
	public BigDecimal getPaymentFee(BigDecimal totalAmount) {
		BigDecimal paymentFee = new BigDecimal("0");// 支付费用
		if (paymentFeeType == PaymentFeeType.scale){
			paymentFee = totalAmount.multiply(new BigDecimal(paymentFee.toString()).divide(new BigDecimal("100")));
		} else {
			paymentFee = this.paymentFee;
		}
		return SystemConfigUtil.getOrderScaleBigDecimal(paymentFee);
	}

}