package net.shopxx.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.servlet.ServletContext;

import net.shopxx.bean.MailConfig;
import net.shopxx.bean.SystemConfig;
import net.shopxx.entity.Member;
import net.shopxx.service.MailService;
import net.shopxx.util.SystemConfigUtil;
import net.shopxx.util.TemplateConfigUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.views.freemarker.FreemarkerManager;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.ResourceBundleModel;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Service实现类 - 邮件服务
 * ============================================================================
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得SHOP++商业授权之前，您不能将本软件应用于商业用途，否则SHOP++将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.shopxx.net
 * ----------------------------------------------------------------------------
 * KEY: SHOPXX002914E795CA825D58C64186BDBFF7A2
 * ============================================================================
 */

@Service
public class MailServiceImpl implements MailService {

	@Resource
	private FreemarkerManager freemarkerManager;
	@Resource
	private JavaMailSender javaMailSender;
	@Resource
	private TaskExecutor taskExecutor;
	
	public boolean isMailConfigComplete() {
		SystemConfig systemConfig = SystemConfigUtil.getSystemConfig();
		if (StringUtils.isEmpty(systemConfig.getSmtpFromMail()) || StringUtils.isEmpty(systemConfig.getSmtpHost()) || systemConfig.getSmtpPort() == null || StringUtils.isEmpty(systemConfig.getSmtpUsername()) || StringUtils.isEmpty(systemConfig.getSmtpPassword())) {
			return false;
		} else {
			return true;
		}
	}
	
	// 增加邮件发送任务
	public void addSendMailTask(final MimeMessage mimeMessage) {
		try {
			taskExecutor.execute(new Runnable() {
				public void run(){
					javaMailSender.send(mimeMessage);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendMail(String subject, String templateFilePath, Map<String, Object> data, String toMail) {
		try {
			SystemConfig systemConfig = SystemConfigUtil.getSystemConfig();
			ServletContext servletContext = ServletActionContext.getServletContext();
			JavaMailSenderImpl javaMailSenderImpl = (JavaMailSenderImpl)javaMailSender;
			javaMailSenderImpl.setHost(systemConfig.getSmtpHost());
			javaMailSenderImpl.setPort(systemConfig.getSmtpPort());
			javaMailSenderImpl.setUsername(systemConfig.getSmtpUsername());
			javaMailSenderImpl.setPassword(systemConfig.getSmtpPassword());
			MimeMessage mimeMessage = javaMailSenderImpl.createMimeMessage();
			Configuration configuration = freemarkerManager.getConfiguration(servletContext);
			Template template = configuration.getTemplate(templateFilePath);
			String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, data);
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "utf-8");
			mimeMessageHelper.setFrom(MimeUtility.encodeWord(systemConfig.getShopName()) + " <" + systemConfig.getSmtpFromMail() + ">");
			mimeMessageHelper.setTo(toMail);
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setText(text, true);
			addSendMailTask(mimeMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 获取公共数据
	public Map<String, Object> getCommonData() {
		Map<String, Object> commonData = new HashMap<String, Object>();
		ServletContext servletContext = ServletActionContext.getServletContext();
		ResourceBundle resourceBundle = ResourceBundle.getBundle("i18n");
		ResourceBundleModel resourceBundleModel = new ResourceBundleModel(resourceBundle, new BeansWrapper());
		commonData.put("bundle", resourceBundleModel);
		commonData.put("base", servletContext.getContextPath());
		commonData.put("systemConfig", SystemConfigUtil.getSystemConfig());
		return commonData;
	}
	
	public void sendSmtpTestMail(String smtpFromMail, String smtpHost, Integer smtpPort, String smtpUsername, String smtpPassword, String toMail) {
		SystemConfig systemConfig = SystemConfigUtil.getSystemConfig();
		Map<String, Object> data = getCommonData();
		MailConfig mailConfig = TemplateConfigUtil.getMailConfig(MailConfig.SMTP_TEST);
		String subject = mailConfig.getSubject();
		String templateFilePath = mailConfig.getTemplateFilePath();
		ServletContext servletContext = ServletActionContext.getServletContext();
		try {
			JavaMailSenderImpl javaMailSenderImpl = (JavaMailSenderImpl)javaMailSender;
			javaMailSenderImpl.setHost(smtpHost);
			javaMailSenderImpl.setPort(smtpPort);
			javaMailSenderImpl.setUsername(smtpUsername);
			javaMailSenderImpl.setPassword(smtpPassword);
			MimeMessage mimeMessage = javaMailSenderImpl.createMimeMessage();
			Configuration configuration = freemarkerManager.getConfiguration(servletContext);
			Template template = configuration.getTemplate(templateFilePath);
			String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, data);
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "utf-8");
			mimeMessageHelper.setFrom(MimeUtility.encodeWord(systemConfig.getShopName()) + " <" + smtpFromMail + ">");
			mimeMessageHelper.setTo(toMail);
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setText(text, true);
			javaMailSender.send(mimeMessage);
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	public void sendPasswordRecoverMail(Member member) {
		Map<String, Object> data = getCommonData();
		data.put("member", member);
		MailConfig mailConfig = TemplateConfigUtil.getMailConfig(MailConfig.PASSWORD_RECOVER);
		String subject = mailConfig.getSubject();
		String templateFilePath = mailConfig.getTemplateFilePath();
		sendMail(subject, templateFilePath, data, member.getEmail());
	}

}