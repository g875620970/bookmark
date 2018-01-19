<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE>
<html ng-app="loginApp">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	    <title>登录</title>
        <%@ include file="/static/taglib.jsp"%>
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
                                swal(data.data.msg,"","error");
                            }
                        }, function errorCallback(error) {
                              swal("服务器异常","","error");
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