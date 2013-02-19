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
 * 实体类 - 退款
 * ============================================================================
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得SHOP++商业授权之前，您不能将本软件应用于商业用途，否则SHOP++将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.shopxx.net
 * ----------------------------------------------------------------------------
 * KEY: SHOPXX7F96199874A941DAF2B4817C397C676A
 * ============================================================================
 */

@Entity
public class Refund extends BaseEntity {

	private static final long serialVersionUID = -2533117666249761057L;

	// 退款类型（预存款支付、在线支付、线下支付）
	public enum RefundType {
		deposit, online, offline
	};
	
	private String refundSn;// 退款编号
	private RefundType refundType;// 退款类型
	private String paymentConfigName;// 支付配置名称
	private String bankName;// 退款银行名称
	private String bankAccount;// 退款银行账号
	private BigDecimal totalAmount;// 退款金额
	private String payee;// 收款人
	private String operator;// 操作员
	private String memo;// 备注
	
	private PaymentConfig paymentConfig;// 支付配置
	private Deposit deposit;// 预存款
	private Order order;// 订单
	
	@Column(nullable = false, updatable = false, unique = true)
	public String getRefundSn() {
		return refundSn;
	}

	public void setRefundSn(String refundSn) {
		this.refundSn = refundSn;
	}

	@Enumerated
	@Column(nullable = false, updatable = false)
	public RefundType getRefundType() {
		return refundType;
	}

	public void setRefundType(RefundType refundType) {
		this.refundType = refundType;
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
		this.totalAmount = SystemConfigUtil.getOrderScaleBigDecimal(amount);
	}

	@Column(nullable = false, updatable = false)
	public String getPayee() {
		return payee;
	}

	public void setPayee(String payee) {
		this.payee = payee;
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