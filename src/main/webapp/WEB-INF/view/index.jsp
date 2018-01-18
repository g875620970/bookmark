<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	    <title>主页</title>
	    <script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
	    <script src="https://cdn.bootcss.com/angular.js/1.6.8/angular.min.js"></script>
	    <!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
		<link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
		<!-- 可选的 Bootstrap 主题文件（一般不用引入） -->
		<link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" rel="stylesheet">
		<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
		<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	</head>
	<body style="margin-top:20px;">
        <div class="container">
			<div class="row">
				<div class="col-sm-6 col-md-4 col-lg-3">
                    <div class="thumbnail">
                        <div class="caption" style="height:200px;">
                            <a href="${basePath}/bookmarks" target="_blank" style="text-decoration:none">
	                            <div style="width:300px;max-width:100%;height:120px;background:#6699FF"></div>
	                            <h3 class="text-center"><span class="glyphicon glyphicon-tags" aria-hidden="true"></span> 书签管理</h3>
                            </a>
                        </div>
                    </div>
                </div>
			</div>
		</div>
    </body>
</html>