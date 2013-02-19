<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>系统配置 - Powered By SHOP++</title>
<meta name="Author" content="SHOP++ Team" />
<meta name="Copyright" content="SHOP++" />
<link rel="icon" href="favicon.ico" type="image/x-icon" />
<#include "/WEB-INF/template/common/include.ftl">
<link href="${base}/template/admin/css/install.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/template/admin/js/install.js"></script>
<script type="text/javascript">
$().ready( function() {

	var $inputForm = $("#inputForm");
	var $installStatus = $("#installStatus");
	var $status = $("#status");
	var $statusMessage = $("#statusMessage");
	var $submitButton = $("#inputForm :submit");

	$inputForm.ajaxForm({
		dataType: "json",
		beforeSubmit: function(data) {
			$submitButton.attr("disabled", true);
			if ($installStatus.val() == "") {
				$statusMessage.text("正在检测数据填写状态，请稍后...");
				$status.show();
			}
		},
		success: function(data) {
			if (data.status == "requiredCheckFinish") {
				$statusMessage.text("正在检测数据库连接状态，请稍后...");
				$status.show();
				$installStatus.val("databaseCheck");
				$inputForm.submit();
			} else if (data.status == "databaseCheckFinish") {
				$statusMessage.text("正在导入数据库文件，请勿刷新本页面...");
				$status.show();
				$installStatus.val("databaseCreate");
				$inputForm.submit();
			} else if (data.status == "error") {
				$installStatus.val("");
				$status.hide();
				$.message(data.status, data.message);
				$submitButton.attr("disabled", false);
			} else if (data.status == "success") {
				$installStatus.val("");
				$status.hide();
				$.message(data.status, data.message);
				$submitButton.val("安装完成");
			}
		}
	});

});
</script>
</head>
<body class="install">
	<div class="header">
		<div class="title">SHOP++ 安装程序 - 系统配置</div>
		<div class="banner"></div>
	</div>
	<div class="body">
		<form id="inputForm" action="install!save.action" method="post">
			<input type="hidden" id="installStatus" name="installStatus" value="" />
			<div class="bodyLeft">
				<ul>
					<li>
						<span class="icon">&nbsp;</span>授权协议
					</li>
					<li>
						<span class="icon">&nbsp;</span>检查系统环境
					</li>
					<li>
						<span class="icon current">&nbsp;</span>系统配置
					</li>
					<li>
						<span class="icon">&nbsp;</span>完成安装
					</li>
				</ul>
			</div>
			<div class="bodyRight">
				<table>
					<tr>
						<th colspan="2">
							数据库设置
						</th>
					</tr>
					<tr>
						<td width="120">
							数据库主机:
						</td>
						<td>
							<input type="text" name="databaseHost" class="formText" value="localhost" />
						</td>
					</tr>
					<tr>
						<td>
							数据库端口:
						</td>
						<td>
							<input type="text" name="databasePort" class="formText" value="3306" />
						</td>
					</tr>
					<tr>
						<td>
							用户名:
						</td>
						<td>
							<input type="text" name="databaseUsername" class="formText" />
						</td>
					</tr>
					<tr>
						<td>
							密码:
						</td>
						<td>
							<input type="password" name="databasePassword" class="formText" />
						</td>
					</tr>
					<tr>
						<td>
							数据库名称:
						</td>
						<td>
							<input type="text" name="databaseName" class="formText" />
						</td>
					</tr>
				</table>
				<div class="blank"></div>
				<table>
					<tr>
						<th colspan="2">
							管理员设置
						</th>
					</tr>
					<tr>
						<td width="120">
							用户名:
						</td>
						<td>
							<input type="text" name="adminUsername" class="formText" value="admin" />
						</td>
					</tr>
					<tr>
						<td>
							密码:
						</td>
						<td>
							<input type="password" name="adminPassword" class="formText" />
						</td>
					</tr>
				</table>
				<div class="blank"></div>
				<table>
					<tr>
						<th colspan="2">
							安装状态
						</th>
					</tr>
					<tr>
						<td colspan="2">
							<div id="status" class="hidden">
								<span class="loadingBar">&nbsp;</span>
								<p id="statusMessage" class="gray"></p>
							</div>&nbsp;
						</td>
					</tr>
				</table>
			</div>
			<div class="blank"></div>
			<div class="buttonArea">
				<input type="button" class="formButton" value="上 一 步" onclick="window.location.href='install!check.action?isAgreeAgreement=true'" hidefocus="true" />&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="submit" class="formButton" value="立即安装" hidefocus="true" />
			</div>
		</form>
	</div>
	<div class="footer">
		<p class="copyright">Copyright © 2010 shopxx.net All Rights Reserved.</p>
	</div>
</body>
</html>