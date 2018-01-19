<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE>
<html ng-app="registerApp">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	    <title>注册</title>
	    <%@ include file="/static/taglib.jsp"%>
	    <script>
		    (function(angular) {
			    angular.module("registerApp",[]).controller("registerController",['$scope','$http',function ($scope,$http) {
		            $scope.userInfo = {};
		            //显示登录页
		            $scope.showLogin = function(){
	                    location.href="${basePath}/login";
		            };
		            //注册
		            $scope.register = function(){
                        $http({
                            url: "${basePath}/register",
                            method: "POST",
                            params:$scope.userInfo,
                            paramSerializer: '$httpParamSerializerJQLike'
                        }).then(function successCallback(data) {
                            if(data.data.code==0){
                                swal({
                                    title:"注册成功",
                                    type: 'success'
                                },function(){
                                    location.href="${basePath}/login";
                                });
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
	<body ng-controller="registerController" style="background:#009999">
        <div class="container">
            <!-- 注册 -->
            <div class="bs-example bs-example-modal" data-example-id="register-modal">
                <div class="modal show" id="register-modal" tabindex="-1" role="dialog">
	                <div class="modal-dialog" role="document">
	                    <div class="modal-content">
	                        <div class="modal-header">
	                            <h4 class="modal-title">注册</h4>
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
	                            <button type="button" class="btn" ng-click="register()">注册</button>
	                            <a href="#" style="padding-left:10px;" ng-click="showLogin()">登录</a>
	                        </div>
	                    </div>
	                </div>
                </div>
            </div>
		</div>
    </body>
</html>