<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE>
<html ng-app="loginApp">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	    <title>登录</title>
	    <script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
	    <script src="https://cdn.bootcss.com/angular.js/1.6.8/angular.min.js"></script>
	    <!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
		<link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
		<!-- 可选的 Bootstrap 主题文件（一般不用引入） -->
		<link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" rel="stylesheet">
		<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
		<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
		<link href="${basePath}/static/css/sweetalert.css" rel="stylesheet">
		<script src="${basePath}/static/js/sweetalert.min.js"></script>
	    <script>
		    (function(angular) {
			    angular.module("loginApp",[]).controller("loginController",['$scope','$http',function ($scope,$http) {
		            $scope.userInfo = {};
		            //显示注册页
                    $scope.showRegister = function(){
                        location.href="${basePath}/register";
                    };
		            //登录
		            $scope.login = function(){
	                    $http({
                            url: "${basePath}/login",
                            method: "POST",
                            params:$scope.userInfo,
                            paramSerializer: '$httpParamSerializerJQLike'
                        }).then(function successCallback(data) {
                            if(data.data.code==0){
                                location.href="${basePath}/index";
                            }else{
                                swal({
                                    title:data.data.msg,
                                    type:"error",
                                    confirmButtonText: "OK",
                                    closeOnConfirm: true
                                });
                            }
                        }, function errorCallback(error) {
                            swal({
                                title:"服务器异常",
                                type:"error",
                                confirmButtonText: "OK",
                                closeOnConfirm: true
                            });
                        });
		            };
			    }]);
		    })(angular);
	    </script>
	</head>
	<body ng-controller="loginController" style="background:#009966">
        <div class="container">
	        <!-- 登录 -->
            <div class="bs-example bs-example-modal" data-example-id="login-modal">
                <div class="modal show" id="login-modal" tabindex="-1" role="dialog">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h4 class="modal-title">登录</h4>
                            </div>
                            <div class="modal-body">
                                <form class="form-horizontal">
                                    <div class="form-group">
                                        <label class="col-sm-3 control-label">用户名：</label>
                                        <div class="col-sm-7">
                                            <input type="text" class="form-control" ng-model="userInfo.userName" required="required" maxlength="20" minlength="6">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-3 control-label">密  码：</label>
                                        <div class="col-sm-7">
                                            <input type="password" class="form-control" ng-model="userInfo.pwd" required="required" maxlength="16">
                                        </div>
                                    </div>
                                </form>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn" ng-click="login()">登录</button>
                                <a href="#" style="padding-left:10px;" ng-click="showRegister()">注册</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
		</div>
    </body>
</html>