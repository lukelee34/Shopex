package net.shopxx.entity;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import net.shopxx.entity.Product.WeightUnit;
import net.shopxx.util.ArithUtil;
import net.shopxx.util.SystemConfigUtil;

/**
 * 实体类 - 配送方式 
 * ============================================================================
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得SHOP++商业授权之前，您不能将本软件应用于商业用途，否则SHOP++将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.shopxx.net
 * ----------------------------------------------------------------------------
 * KEY: SHOPXXD834B4DDADAC88F9991F5CD5611A0797
 * ============================================================================
 */

@Entity
public class DeliveryType extends BaseEntity {

	private static final long serialVersionUID = 5873163245980853245L;

	// 配送类型：先付款后发货、货到付款
	public enum DeliveryMethod {
		deliveryAgainstPayment, cashOnDelivery
	};

	private String name;// 配送方式名称
	private DeliveryMethod deliveryMethod;// 配送类型
	private Double firstWeight;// 首重量
	private Double continueWeight;// 续重量
	private WeightUnit firstWeightUnit;// 首重单位
	private WeightUnit continueWeightUnit;// 续重单位
	private BigDecimal firstWeightPrice;// 首重价格
	private BigDecimal continueWeightPrice;// 续重价格
	private String description;// 介绍
	private Integer orderList;// 排序
	
	private DeliveryCorp defaultDeliveryCorp;// 默认物流公司
	private Set<Order> orderSet;// 订单
	private Set<Shipping> shippingSet;// 发货
	private Set<Reship> reshipSet;// 退货

	@Column(nullable = false, unique = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Enumerated
	@Column(nullable = false)
	public DeliveryMethod getDeliveryMethod() {
		return deliveryMethod;
	}

	public void setDeliveryMethod(DeliveryMethod deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
	}

	@Column(nullable = false)
	public Double getFirstWeight() {
		return firstWeight;
	}

	public void setFirstWeight(Double firstWeight) {
		this.firstWeight = firstWeight;
	}

	@Column(nullable = false)
	public Double getContinueWeight() {
		return continueWeight;
	}

	public void setContinueWeight(Double continueWeight) {
		this.continueWeight = continueWeight;
	}

	@Enumerated
	@Column(nullable = false)
	public WeightUnit getFirstWeightUnit() {
		return firstWeightUnit;
	}

	public void setFirstWeightUnit(WeightUnit firstWeightUnit) {
		this.firstWeightUnit = firstWeightUnit;
	}

	@Enumerated
	@Column(nullable = false)
	public WeightUnit getContinueWeightUnit() {
		return continueWeightUnit;
	}

	public void setContinueWeightUnit(WeightUnit continueWeightUnit) {
		this.continueWeightUnit = continueWeightUnit;
	}

	@Column(precision = 15, scale = 5, nullable = false)
	public BigDecimal getFirstWeightPrice() {
		return firstWeightPrice;
	}

	public void setFirstWeightPrice(BigDecimal firstWeightPrice) {
		this.firstWeightPrice = SystemConfigUtil.getPriceScaleBigDecimal(firstWeightPrice);
	}

	@Column(precision = 15, scale = 5, nullable = false)
	public BigDecimal getContinueWeightPrice() {
		return continueWeightPrice;
	}

	public void setContinueWeightPrice(BigDecimal continueWeightPrice) {
		this.continueWeightPrice = SystemConfigUtil.getPriceScaleBigDecimal(continueWeightPrice);
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	public DeliveryCorp getDefaultDeliveryCorp() {
		return defaultDeliveryCorp;
	}

	public void setDefaultDeliveryCorp(DeliveryCorp defaultDeliveryCorp) {
		this.defaultDeliveryCorp = defaultDeliveryCorp;
	}
	
	@OneToMany(mappedBy = "deliveryType", fetch = FetchType.LAZY)
	public Set<Order> getOrderSet() {
		return orderSet;
	}

	public void setOrderSet(Set<Order> orderSet) {
		this.orderSet = orderSet;
	}

	@OneToMany(mappedBy = "deliveryType", fetch = FetchType.LAZY)
	public Set<Shipping> getShippingSet() {
		return shippingSet;
	}

	public void setShippingSet(Set<Shipping> shippingSet) {
		this.shippingSet = shippingSet;
	}

	@OneToMany(mappedBy = "deliveryType", fetch = FetchType.LAZY)
	public Set<Reship> getReshipSet() {
		return reshipSet;
	}

	public void setReshipSet(Set<Reship> reshipSet) {
		this.reshipSet = reshipSet;
	}

	/**
	 * 根据重量、重量单位转换为单位为g的重量值
	 * 
	 * @return 重量值（单位：g）
	 */
	@Transient
	public static double toWeightGram(double weight, WeightUnit weightUnit) {
		double weightGram = 0D;// 重量（单位：g）
		if (weightUnit == WeightUnit.g) {
			weightGram = weight;
		} else if(weightUnit == WeightUnit.kg) {
			weightGram = ArithUtil.mul(weight, 1000);
		} else {
			weightGram = ArithUtil.mul(weight, 1000000);
		}
		return weightGram;
	}
	
	/**
	 * 根据总重量、重量单位计算配送费用
	 * 
	 * @return 配送费用
	 */
	@Transient
	public BigDecimal getDeliveryFee(double totalWeight, WeightUnit totalWeightUnit) {
		double totalWeightGram = toWeightGram(totalWeight, totalWeightUnit);// 首重量（单位：g）
		double firstWeightGram = toWeightGram(firstWeight, firstWeightUnit);// 首重量（单位：g）
		double contiuneWeightGram = toWeightGram(continueWeight, continueWeightUnit);// 续重量（单位：g）
		BigDecimal deliveryFee = new BigDecimal("0");// 配送费用
		if (totalWeightGram <= firstWeightGram) {
			deliveryFee = firstWeightPrice;
		} else {
			Double contiuneWeightCount = Math.ceil(ArithUtil.div(ArithUtil.sub(totalWeightGram, firstWeightGram), contiuneWeightGram));
			deliveryFee = firstWeightPrice.add(continueWeightPrice.multiply(new BigDecimal(contiuneWeightCount.toString())));
		}
		return SystemConfigUtil.getOrderScaleBigDecimal(deliveryFee);
	}
	
	/**
	 * 根据总重量计算配送费用（重量单位：g）
	 * 
	 * @return 配送费用
	 */
	@Transient
	public BigDecimal getDeliveryFee(double totalWeight) {
		return getDeliveryFee(totalWeight, WeightUnit.g);
	}

}