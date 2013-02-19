package net.shopxx.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import net.shopxx.util.SystemConfigUtil;

/**
 * 实体类 - 支付
 * ============================================================================
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得SHOP++商业授权之前，您不能将本软件应用于商业用途，否则SHOP++将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.shopxx.net
 * ----------------------------------------------------------------------------
 * KEY: SHOPXX16C6D1083702AF0F68BEA86BD3E8219D
 * ============================================================================
 */

@Entity
public class Payment extends BaseEntity {

	private static final long serialVersionUID = 6404772131152718534L;
	
	// 支付类型（在线充值、预存款支付、在线支付、线下支付）
	public enum PaymentType {
		recharge, deposit, online, offline
	};
	
	// 支付状态（准备、超时、作废、成功、失败）
	public enum PaymentStatus {
		ready, timeout, invalid, success, failure
	};
	
	private String paymentSn;// 支付编号
	private PaymentType paymentType;// 支付类型
	private String paymentConfigName;// 支付配置名称
	private String bankName;// 收款银行名称
	private String bankAccount;// 收款银行账号
	private BigDecimal totalAmount;// 支付金额
	private BigDecimal paymentFee;// 支付手续费
	private String payer;// 付款人
	private String operator;// 操作员
	private String memo;// 备注
	private PaymentStatus paymentStatus;// 支付状态
	
	private PaymentConfig paymentConfig;// 支付配置
	private Deposit deposit;// 预存款
	private Order order;// 订单
	
	@Column(nullable = false, updatable = false, unique = true)
	public String getPaymentSn() {
		return paymentSn;
	}
	
	public void setPaymentSn(String paymentSn) {
		this.paymentSn = paymentSn;
	}
	
	@Enumerated
	@Column(nullable = false, updatable = false)
	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	@Column(nullable = false, updatable = false)
	public String getPaymentConfigName() {
		return paymentConfigName;
	}

	public void setPaymentConfigName(String paymentConfigName) {
		this.paymentConfigName = paymentConfigName;
	}

	@Column(updatable = false)
	public String getBankName() {
		return bankName;
	}
	
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	@Column(updatable = false)
	public String getBankAccount() {
		return bankAccount;
	}
	
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	
	@Column(nullable = false, updatable = false, precision = 15, scale = 5)
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	
	// 精度处理
	public void setTotalAmount(BigDecimal amount) {
		this.totalAmount = SystemConfigUtil.getPriceScaleBigDecimal(amount);
	}
	
	@Column(nullable = false, updatable = false, precision = 15, scale = 5)
	public BigDecimal getPaymentFee() {
		return paymentFee;
	}
	
	// 精度处理
	public void setPaymentFee(BigDecimal paymentFee) {
		this.paymentFee = SystemConfigUtil.getPriceScaleBigDecimal(paymentFee);
	}

	@Enumerated
	@Column(nullable = false)
	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	@Column(nullable = false, updatable = false)
	public String getPayer() {
		return payer;
	}
	
	public void setPayer(String payer) {
		this.payer = payer;
	}
	
	@Column(updatable = false)
	public String getOperator() {
		return operator;
	}
	
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	@Column(updatable = false, length = 5000)
	public String getMemo() {
		return memo;
	}
	
	public void setMemo(String memo) {
		this.memo = memo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public PaymentConfig getPaymentConfig() {
		return paymentConfig;
	}

	public void setPaymentConfig(PaymentConfig paymentConfig) {
		this.paymentConfig = paymentConfig;
	}

	@OneToOne(fetch = FetchType.LAZY)
	public Deposit getDeposit() {
		return deposit;
	}

	public void setDeposit(Deposit deposit) {
		this.deposit = deposit;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

}