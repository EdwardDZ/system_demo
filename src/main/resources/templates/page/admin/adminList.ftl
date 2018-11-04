<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>管理员列表</title>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="format-detection" content="telephone=no">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache, must-revalidate">
<meta http-equiv="expires" content="Wed, 26 Feb 1997 08:21:57 GMT">
<link rel="stylesheet" href="${basePath}/static/layui/css/layui.css" media="all" />
<link rel="stylesheet" href="${basePath}/static/css/font_eolqem241z66flxr.css"
	media="all" />
<link rel="stylesheet" href="${basePath}/static/css/list.css" media="all" />
<script>
	var basePath = "${basePath}";
</script>
</head>
<body class="childrenBody">
	<input type="hidden" id="adminId" value="${shiro.getId()!''}" />
	<blockquote class="layui-elem-quote list_search">
		<#if shiro.hasPermission("sys:admin:save")>
			<div class="layui-inline">
				<a class="layui-btn layui-btn-normal adminAdd_btn"><i
					class="layui-icon">&#xe608;</i> 添加管理员</a>
			</div>
		</#if>
		<#if shiro.hasPermission("sys:admin:delete")>
			<div class="layui-inline">
				<a class="layui-btn layui-btn-danger batchDel"><i
					class="layui-icon">&#xe640;</i>批量删除</a>
			</div>
		</#if>
		<!-- <div class="layui-inline">
			<div class="layui-form-mid layui-word-aux"></div>
		</div> -->
	</blockquote>
	<!-- 数据表格 -->
	<table id="adminList" lay-filter="test"></table>
	<script type="text/javascript" src="${basePath}/static/layui/layui.js"></script>
	<script type="text/javascript" src="${basePath}/static/js/admin/adminList.js"></script>
	<script type="text/html" id="barEdit">
	<#if shiro.hasPermission("sys:admin:update")>
  		<a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
	</#if>
	<#if shiro.hasPermission("sys:admin:delete")>
  		<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
	</#if>
	</script>
	<script type="text/html" id="sexTpl">
 		 {{#  if(d.sex === '0'){ }}
   		 <span style="color: #F581B1;">女</span>
  		{{#  } else if(d.sex === '1'){ }}
   		 	男
		{{#  } else{ }}
   		 	保密
  		{{#  } }}
	</script>
</body>
</html>