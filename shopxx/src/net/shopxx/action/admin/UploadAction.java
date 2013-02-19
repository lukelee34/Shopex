package net.shopxx.action.admin;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.shopxx.bean.SystemConfig;
import net.shopxx.util.CommonUtil;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;

/**
 * 后台Action类 - 文件上传
 * ============================================================================
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得SHOP++商业授权之前，您不能将本软件应用于商业用途，否则SHOP++将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.shopxx.net
 * ----------------------------------------------------------------------------
 * KEY: SHOPXX1C482CDF2F9EE58F12758866EF3538E6
 * ============================================================================
 */

@ParentPackage("admin")
public class UploadAction extends BaseAdminAction {

	private static final long serialVersionUID = 6614132059804452558L;

	private File upload;// 上传文件
	private String uploadFileName;// 上传文件名

	// 图片文件上传
	public String image() throws Exception {
		if (upload == null) {
			return ajaxJsonErrorMessage("请选择上传文件!");
		}
		String allowedUploadImageExtension = getSystemConfig().getAllowedUploadImageExtension().toLowerCase();
		if (StringUtils.isEmpty(allowedUploadImageExtension)){
			return ajaxJsonErrorMessage("不允许上传图片文件!");
		}
		String imageExtension =  StringUtils.substringAfterLast(uploadFileName, ".").toLowerCase();
		String[] imageExtensionArray = allowedUploadImageExtension.split(SystemConfig.EXTENSION_SEPARATOR);
		if (!ArrayUtils.contains(imageExtensionArray, imageExtension)) {
			return ajaxJsonErrorMessage("只允许上传图片文件类型: " + allowedUploadImageExtension + "!");
		}
		int uploadLimit = getSystemConfig().getUploadLimit() * 1024;
		if (uploadLimit != 0) {
			if (upload != null && upload.length() > uploadLimit) {
				return ajaxJsonErrorMessage("文件大小超出限制!");
			}
		}
		File uploadImageDir = new File(ServletActionContext.getServletContext().getRealPath(SystemConfig.UPLOAD_IMAGE_DIR));
		if (!uploadImageDir.exists()) {
			uploadImageDir.mkdirs();
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
		String dateString = simpleDateFormat.format(new Date());
		String uploadImagePath = SystemConfig.UPLOAD_IMAGE_DIR + dateString + "/" + CommonUtil.getUUID() + "." + imageExtension;
		File file = new File(ServletActionContext.getServletContext().getRealPath(uploadImagePath));
		FileUtils.copyFile(upload, file);
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put(STATUS, SUCCESS);
		jsonMap.put("url", ServletActionContext.getServletContext().getContextPath() + uploadImagePath);
		return ajaxJson(jsonMap);
	}
	
	// 媒体文件上传
	public String media() throws Exception {
		if (upload == null) {
			return ajaxJsonErrorMessage("请选择上传文件!");
		}
		String allowedUploadMediaExtension = getSystemConfig().getAllowedUploadMediaExtension().toLowerCase();
		if (StringUtils.isEmpty(allowedUploadMediaExtension)){
			return ajaxJsonErrorMessage("不允许上传媒体文件!");
		}
		String mediaExtension =  StringUtils.substringAfterLast(uploadFileName, ".").toLowerCase();
		String[] mediaExtensionArray = allowedUploadMediaExtension.split(SystemConfig.EXTENSION_SEPARATOR);
		if (!ArrayUtils.contains(mediaExtensionArray, mediaExtension)) {
			return ajaxJsonErrorMessage("只允许上传媒体文件类型: " + allowedUploadMediaExtension + "!");
		}
		int uploadLimit = getSystemConfig().getUploadLimit() * 1024;
		if (uploadLimit != 0) {
			if (upload != null && upload.length() > uploadLimit) {
				return ajaxJsonErrorMessage("文件大小超出限制!");
			}
		}
		File uploadMediaDir = new File(ServletActionContext.getServletContext().getRealPath(SystemConfig.UPLOAD_MEDIA_DIR));
		if (!uploadMediaDir.exists()) {
			uploadMediaDir.mkdirs();
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
		String dateString = simpleDateFormat.format(new Date());
		String uploadMediaPath = SystemConfig.UPLOAD_MEDIA_DIR + dateString + "/" + CommonUtil.getUUID() + "." + mediaExtension;
		File file = new File(ServletActionContext.getServletContext().getRealPath(uploadMediaPath));
		FileUtils.copyFile(upload, file);
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put(STATUS, SUCCESS);
		jsonMap.put("url", ServletActionContext.getServletContext().getContextPath() + uploadMediaPath);
		return ajaxJson(jsonMap);
	}
	
	// 其它文件上传
	public String file() throws Exception {
		if (upload == null) {
			return ajaxJsonErrorMessage("请选择上传文件!");
		}
		String allowedUploadFileExtension = getSystemConfig().getAllowedUploadFileExtension().toLowerCase();
		if (StringUtils.isEmpty(allowedUploadFileExtension)){
			return ajaxJsonErrorMessage("不允许上传文件!");
		}
		String fileExtension =  StringUtils.substringAfterLast(uploadFileName, ".").toLowerCase();
		String[] fileExtensionArray = allowedUploadFileExtension.split(SystemConfig.EXTENSION_SEPARATOR);
		if (!ArrayUtils.contains(fileExtensionArray, fileExtension)) {
			return ajaxJsonErrorMessage("只允许上传文件类型: " + allowedUploadFileExtension + "!");
		}
		int uploadLimit = getSystemConfig().getUploadLimit() * 1024;
		if (uploadLimit != 0) {
			if (upload != null && upload.length() > uploadLimit) {
				return ajaxJsonErrorMessage("文件大小超出限制!");
			}
		}
		File uploadFileDir = new File(ServletActionContext.getServletContext().getRealPath(SystemConfig.UPLOAD_FILE_DIR));
		if (!uploadFileDir.exists()) {
			uploadFileDir.mkdirs();
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
		String dateString = simpleDateFormat.format(new Date());
		String uploadFilePath = SystemConfig.UPLOAD_FILE_DIR + dateString + "/" + CommonUtil.getUUID() + "." + fileExtension;
		File file = new File(ServletActionContext.getServletContext().getRealPath(uploadFilePath));
		FileUtils.copyFile(upload, file);
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put(STATUS, SUCCESS);
		jsonMap.put("url", ServletActionContext.getServletContext().getContextPath() + uploadFilePath);
		return ajaxJsonErrorMessage("文件大小超出限制!");
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

}