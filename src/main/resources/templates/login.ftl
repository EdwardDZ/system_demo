<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>登录--后台管理</title>
    <meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<meta name="apple-mobile-web-app-capable" content="yes">
    <link rel="stylesheet" href="${basePath}/static/layui/css/layui.css" media="all" />
    <link rel="stylesheet" href="${basePath}/static/css/login.css" media="all" />
    <script>  
        var basePath = "${basePath}";  
    </script>
    <script type="text/javascript">
        if(window !=top){
            top.location.href=location.href;
        }
    </script>
</head>
<body>


<div class="video_mask"></div>
<div class="login">
    <h1>管理员登录</h1>
    <form class="layui-form" id="form">
        <div class="layui-form-item">
            <input class="layui-input" name="username" placeholder="用户名" value="admin" lay-verify="required" type="text" autocomplete="off">
        </div>
        <div class="layui-form-item">
            <input class="layui-input" name="password" placeholder="密码" value="123456"  lay-verify="required" type="password" autocomplete="off">
        </div>
        <div class="layui-form-item form_code">
            <input class="layui-input" style="width: 140px;" name="vcode" placeholder="验证码" lay-verify="required" type="text" autocomplete="off" maxlength="4">
            <div class="code"><img id="captcha" src="${basePath}/sys/vcode" width="100" height="36" onclick="refreshCode(this)"></div>
        </div>
        <button class="layui-btn login_btn" lay-submit="" lay-filter="login" id="btn">登录</button>
    </form>
</div>
<script type="text/javascript" src="${basePath}/static/layui/layui.js"></script>
<script type="text/javascript" src="${basePath}/static/js/login.js"></script>
</body>
</html>