package net.shopxx.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 实体类 - 日志
 * ============================================================================
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得SHOP++商业授权之前，您不能将本软件应用于商业用途，否则SHOP++将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.shopxx.net
 * ----------------------------------------------------------------------------
 * KEY: SHOPXXF75093F3168097AE8B8496F61E075DA3
 * ============================================================================
 */

@Entity
public class Log extends BaseEntity {

	private static final long serialVersionUID = -4494144902110236826L;

	private String operationName;// 操作名称
	private String operator;// 操作员
	private String actionClassName;// 操作Action名称
	private String actionMethodName;// 操作方法名称
	private String ip;// IP
	private String info;// 日志信息
	
	@Column(nullable = false)
	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	@Column(nullable = false)
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	@Column(nullable = false)
	public String getActionClassName() {
		return actionClassName;
	}

	public void setActionClassName(String actionClassName) {
		this.actionClassName = actionClassName;
	}

	@Column(nullable = false)
	public String getActionMethodName() {
		return actionMethodName;
	}

	public void setActionMethodName(String actionMethodName) {
		this.actionMethodName = actionMethodName;
	}

	@Column(nullable = false)
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(length = 5000)
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}