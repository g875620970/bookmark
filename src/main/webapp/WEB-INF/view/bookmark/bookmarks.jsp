<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE>
<html ng-app="bookmarksApp">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	    <title>书签</title>
	    <script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
	    <script src="https://cdn.bootcss.com/angular.js/1.6.8/angular.min.js"></script>
	    <!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
		<link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
		<!-- 可选的 Bootstrap 主题文件（一般不用引入） -->
		<link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" rel="stylesheet">
		<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
		<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
		<link href="static/css/sweetalert.css" rel="stylesheet">
		<script src="static/js/sweetalert.min.js"></script>
	    <script>
		    (function(angular) {
			    angular.module("bookmarksApp",[]).controller("bookmarksController",['$scope','$http',function ($scope,$http) {
		            $scope.userName = "${sessionScope.userName}";
		            $scope.bookmark = {};
		            $scope.bookmarkList = [];
		            //获取所有书签
		            $scope.getBookmarks = function(){
			            $http({
	                        url: "/bookmark/getBookmarks",
	                        method: "GET",
	                        params:{userName:$scope.userName},
	                        paramSerializer: '$httpParamSerializerJQLike'
	                    }).then(function successCallback(data) {
	                        if(data.data.code==0){
	                            $scope.bookmarkList = data.data.data;
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
	                $scope.getBookmarks();
	                //新增书签信息
                    $scope.addBookmark = function(){
                        $scope.bookmark = {};
                        $(".modal").modal("show");
                    };
                    //编辑书签信息
                    $scope.editBookmark = function(bookmarkInfo){
                        $.each(bookmarkInfo,function(key,value){
                            $scope.bookmark[key]=value;
                        });
                        $(".modal").modal("show");
                    };
                    //删除书签信息
                    $scope.delBookmark = function(id){
                        swal({
                            title:"删除操作",
                            text: '确定要删除这个书签吗?',
                            type: 'warning',
                            html: true,
                            showCancelButton: true,
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                        },function(isConfirm) {
                            if(isConfirm){
	                            $http({
                                    url: "/bookmark/delBookmark",
                                    method: "POST",
                                    params:{userName:$scope.userName,id:id},
                                    paramSerializer: '$httpParamSerializerJQLike'
                                }).then(function successCallback(data) {
                                    if(data.data.code==0){
                                        $(".modal").modal("hide");
                                        $scope.getBookmarks();
                                    }else{
                                        swal({
                                            title:data.data.msg,
                                            type:"error",
                                            confirmButtonText: "OK",
                                            closeOnConfirm: true
                                        });
                                    }
                                }, function errorCallback(error) {
                                    console.log("服务器异常");
                                }); 
                            }
                        });
                    };
	                //保存书签信息
	                $scope.saveBookmark = function(){
                        $scope.bookmark.userName = $scope.userName;
	                    var id = $scope.bookmark.id;
	                    var url = id!=null?"/bookmark/updateBookmark":"/bookmark/addBookmark";
                        $http({
                            url: url,
                            method: "POST",
                            params:$scope.bookmark,
                            paramSerializer: '$httpParamSerializerJQLike'
                        }).then(function successCallback(data) {
                            if(data.data.code==0){
                                $(".modal").modal("hide");
                                $scope.getBookmarks();
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
	<body ng-controller="bookmarksController">
        <div style="margin:10px 25px;">
            <!-- modal -->
            <div style="margin:5px 0px;width:100%;height:30px;">
		        <button ng-if="userName!=null" type="button" class="btn btn-sm btn-primary" ng-click="addBookmark()">新增书签</button>
	        </div>
			<div class="panel panel-default" ng-repeat="bookmarkCategory in bookmarkList">
				<!-- Default panel contents -->
				<div class="panel-heading">{{bookmarkCategory.category}}</div>
				<!-- List group -->
				<ol class="list-group">
					<li class="list-group-item" ng-repeat="bookmark in bookmarkCategory.bookmarkList | orderBy:'name'">
					   <b>{{$index+1}}.</b><a href="{{bookmark.url}}" target="_blank">{{bookmark.name}}</a>
					   <a ng-if="userName!=null" href="#" class="text-danger" style="float:right;padding-left:5px;" ng-click="delBookmark(bookmark.id)">删除</a> 
					   <a ng-if="userName!=null" href="#" style="float:right;" ng-click="editBookmark(bookmark)">编辑</a>
					</li>
				</ol>
			</div>
		</div>
		<div class="modal fade" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                        <h4 class="modal-title" id="myModalLabel">书签信息</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal">
                            <div class="form-group">
                                <label class="col-sm-3 control-label">类别：</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control" ng-model="bookmark.category" maxlength="6">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">书签名称<i class="text-danger">*</i>：</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control" ng-model="bookmark.name" required="required" maxlength="20">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">书签地址<i class="text-danger">*</i>：</label>
                                <div class="col-sm-8">
                                    <input type="url" class="form-control" ng-model="bookmark.url" required="required">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">备注：</label>
                                <div class="col-sm-8">
                                    <textarea class="form-control" ng-model="bookmark.remark" maxlength="100"></textarea>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                        <button type="button" class="btn btn-primary" ng-click="saveBookmark()">保存</button>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>