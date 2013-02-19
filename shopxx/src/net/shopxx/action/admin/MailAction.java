package net.shopxx.action.admin;

import javax.annotation.Resource;

import net.shopxx.service.MailService;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;

import com.opensymphony.xwork2.interceptor.annotations.InputConfig;
import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

/**
 * 后台Action类 - 邮箱
 * ============================================================================
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得SHOP++商业授权之前，您不能将本软件应用于商业用途，否则SHOP++将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.shopxx.net
 * ----------------------------------------------------------------------------
 * KEY: SHOPXX72C2E9AA7ADCD7AA59BF9EC2590B1829
 * ============================================================================
 */

@ParentPackage("admin")
public class MailAction extends BaseAdminAction {

	private static final long serialVersionUID = -601752481727227841L;

	private String smtpFromMail;
	private String smtpHost;
	private Integer smtpPort;
	private String smtpUsername;
	private String smtpPassword;
	private String smtpToMail;

	@Resource
	private MailService mailService;
	
	// 发送SMTP测试邮件
	@Validations(
		requiredStrings = { 
			@RequiredStringValidator(fieldName = "smtpFromMail", message = "发件人邮箱不允许为空!"),
			@RequiredStringValidator(fieldName = "smtpHost", message = "SMTP服务器地址不允许为空!"),
			@RequiredStringValidator(fieldName = "smtpUsername", message = "SMTP用户名不允许为空!"),
			@RequiredStringValidator(fieldName = "smtpPassword", message = "SMTP密码不允许为空!"),
			@RequiredStringValidator(fieldName = "smtpToMail", message = "收件人邮箱不允许为空!")
		}, 
		requiredFields = {
			@RequiredFieldValidator(fieldName = "smtpPort", message = "SMTP服务器端口不允许为空!")
		}, 
		intRangeFields = {
			@IntRangeFieldValidator(fieldName = "smtpPort", min = "0", message = "SMTP端口必须为零正整数!")
		},
		emails = {
			@EmailValidator(fieldName = "smtpFromMail", message = "发件人邮箱格式错误!"),
			@EmailValidator(fieldName = "smtpToMail", message = "收件人邮箱格式错误!")
		}
	)
	@InputConfig(resultName = "error")
	public String ajaxSendSmtpTest() {
		try {
			mailService.sendSmtpTestMail(smtpFromMail, smtpHost, smtpPort, smtpUsername, smtpPassword, smtpToMail);
		} catch (MailAuthenticationException e) {
			return ajaxJsonErrorMessage("权限验证失败，请检查SMTP用户名、密码！");
		} catch (MailSendException e) {
			return ajaxJsonErrorMessage("邮件发送失败，请检查发件人邮箱、SMTP服务器地址、端口！");
		} catch (Exception e) {
			return ajaxJsonErrorMessage("邮件发送失败！");
		}
		return ajaxJsonSuccessMessage("测试邮件发送成功，请查收邮件！");
	}

	public String getSmtpFromMail() {
		return smtpFromMail;
	}

	public void setSmtpFromMail(String smtpFromMail) {
		this.smtpFromMail = smtpFromMail;
	}

	public String getSmtpHost() {
		return smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	public Integer getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(Integer smtpPort) {
		this.smtpPort = smtpPort;
	}

	public String getSmtpUsername() {
		return smtpUsername;
	}

	public void setSmtpUsername(String smtpUsername) {
		this.smtpUsername = smtpUsername;
	}

	public String getSmtpPassword() {
		return smtpPassword;
	}

	public void setSmtpPassword(String smtpPassword) {
		this.smtpPassword = smtpPassword;
	}

	public String getSmtpToMail() {
		return smtpToMail;
	}

	public void setSmtpToMail(String smtpToMail) {
		this.smtpToMail = smtpToMail;
	}

}