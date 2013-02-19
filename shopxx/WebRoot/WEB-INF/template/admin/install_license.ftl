<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>SHOP++安装程序 - Powered By SHOP++</title>
<meta name="Author" content="SHOP++ Team" />
<meta name="Copyright" content="SHOP++" />
<link rel="icon" href="favicon.ico" type="image/x-icon" />
<#include "/WEB-INF/template/common/include.ftl">
<link href="${base}/template/admin/css/install.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/template/admin/js/install.js"></script>
<script type="text/javascript">
$().ready( function() {
	
	var $isAgreeAgreement = $("input[name='isAgreeAgreement']");
	var $submitButton = $("#inputForm :submit");
	
	$isAgreeAgreement.click( function() {
		if ($isAgreeAgreement.attr("checked") == true) {
			$submitButton.attr("disabled", false);
		} else {
			$submitButton.attr("disabled", true);
		}
	});

});
</script>
</head>
<body class="install">
	<div class="header">
		<div class="title">SHOP++ 安装程序 - 授权协议</div>
		<div class="banner"></div>
	</div>
	<div class="body">
		<form id="inputForm" action="install!check.action" method="post">
			<div class="bodyLeft">
				<ul>
					<li>
						<span class="icon current">&nbsp;</span>授权协议
					</li>
					<li>
						<span class="icon">&nbsp;</span>检查系统环境
					</li>
					<li>
						<span class="icon">&nbsp;</span>系统配置
					</li>
					<li>
						<span class="icon">&nbsp;</span>完成安装
					</li>
				</ul>
			</div>
			<div class="bodyRight">
				<iframe src="${base}/license.html"></iframe>
				<label>
					<input type="checkbox" name="isAgreeAgreement" value="true" />我已经仔细阅读，并同意上述条款中的所有内容
				</label>
			</div>
			<div class="blank"></div>
			<div class="buttonArea">
				<input type="submit" class="formButton" value="下 一 步" disabled hidefocus="true" />
			</div>
		</form>
	</div>
	<div class="footer">
		<p class="copyright">Copyright © 2010 shopxx.net All Rights Reserved.</p>
	</div>
</body>
</html>