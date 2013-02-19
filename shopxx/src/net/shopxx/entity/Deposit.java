package net.shopxx.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import net.shopxx.util.SystemConfigUtil;

/**
 * 实体类 - 预存款
 * ============================================================================
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得SHOP++商业授权之前，您不能将本软件应用于商业用途，否则SHOP++将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.shopxx.net
 * ----------------------------------------------------------------------------
 * KEY: SHOPXX9CDAB54D4C875D425F65ACF3175FEDF0
 * ============================================================================
 */

@Entity
public class Deposit extends BaseEntity {

	private static final long serialVersionUID = 4527727387983423232L;
	
	public static final int DEFAULT_DEPOSIT_LIST_PAGE_SIZE = 15;// 充值记录列表默认每页显示数
	
	// 预存款操作类型（会员充值、会员支付、后台代支付、后台代扣费、后台代充值、后台退款）
	public enum DepositType {
		memberRecharge, memberPayment, adminRecharge, adminChargeback, adminPayment, adminRefund
	};
	
	private DepositType depositType;// 预存款操作类型
	private BigDecimal credit;// 存入金额
	private BigDecimal debit;// 支出金额
	private BigDecimal balance;// 当前余额
	
	private Member member;// 会员
	private Payment payment;// 收款
	private Refund refund;// 退款
	
	@Enumerated
	@Column(nullable = false, updatable = false)
	public DepositType getDepositType() {
		return depositType;
	}

	public void setDepositType(DepositType depositType) {
		this.depositType = depositType;
	}
	
	@Column(nullable = false, updatable = false)
	public BigDecimal getCredit() {
		return credit;
	}
	
	// 精度处理
	public void setCredit(BigDecimal credit) {
		this.credit = SystemConfigUtil.getOrderScaleBigDecimal(credit);
	}
	
	@Column(nullable = false, updatable = false)
	public BigDecimal getDebit() {
		return debit;
	}
	
	// 精度处理
	public void setDebit(BigDecimal debit) {
		this.debit = SystemConfigUtil.getOrderScaleBigDecimal(debit);
	}
	
	@Column(nullable = false, updatable = false)
	public BigDecimal getBalance() {
		return balance;
	}

	// 精度处理
	public void setBalance(BigDecimal balance) {
		this.balance = SystemConfigUtil.getOrderScaleBigDecimal(balance);
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "deposit")
	@Cascade(value = { CascadeType.DELETE })
	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "deposit")
	@Cascade(value = { CascadeType.DELETE })
	public Refund getRefund() {
		return refund;
	}

	public void setRefund(Refund refund) {
		this.refund = refund;
	}

}