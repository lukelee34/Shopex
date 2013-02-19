<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>检查系统环境 - Powered By SHOP++</title>
<meta name="Author" content="SHOP++ Team" />
<meta name="Copyright" content="SHOP++" />
<link rel="icon" href="favicon.ico" type="image/x-icon" />
<#include "/WEB-INF/template/common/include.ftl">
<link href="${base}/template/admin/css/install.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/template/admin/js/install.js"></script>
</head>
<body class="install">
	<div class="header">
		<div class="title">SHOP++ 安装程序 - 检查系统环境</div>
		<div class="banner"></div>
	</div>
	<div class="body">
		<form id="inputForm" action="install!setting.action" method="post">
			<div class="bodyLeft">
				<ul>
					<li>
						<span class="icon">&nbsp;</span>授权协议
					</li>
					<li>
						<span class="icon current">&nbsp;</span>检查系统环境
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
				<table>
					<tr>
						<th colspan="2">
							系统环境
						</th>
					</tr>
					<tr>
						<td>
							Java版本:
						</td>
						<td>
							${statics["java.lang.System"].getProperty("java.version")}
						</td>
					</tr>
					<tr>
						<td>
							操作系统名称:
						</td>
						<td>
							${statics["java.lang.System"].getProperty("os.name")}
						</td>
					</tr>
					<tr>
						<td>
							操作系统构架:
						</td>
						<td>
							${statics["java.lang.System"].getProperty("os.arch")}
						</td>
					</tr>
					<tr>
						<td>
							操作系统版本:
						</td>
						<td>
							${statics["java.lang.System"].getProperty("os.version")}
						</td>
					</tr>
					<tr>
						<td>
							当前工作目录:
						</td>
						<td>
							${statics["java.lang.System"].getProperty("user.dir")}
						</td>
					</tr>
					<tr>
						<td>
							临时文件路径:
						</td>
						<td>
							${statics["java.lang.System"].getProperty("java.io.tmpdir")}
						</td>
					</tr>
				</table>
			</div>
			<div class="blank"></div>
			<div class="buttonArea">
				<input type="button" class="formButton" value="上 一 步" onclick="window.location.href='install!license.action'" hidefocus="true" />&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="submit" class="formButton" value="下 一 步" hidefocus="true" />
			</div>
		</form>
	</div>
	<div class="footer">
		<p class="copyright">Copyright © 2010 shopxx.net All Rights Reserved.</p>
	</div>
</body>
</html>