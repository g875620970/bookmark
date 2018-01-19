<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE>
<html ng-app="bookmarksApp">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	    <title>书签</title>
	    <%@ include file="/static/taglib.jsp"%>
	    <script>
		    (function(angular) {
			    angular.module("bookmarksApp",[]).controller("bookmarksController",['$scope','$http',function ($scope,$http) {
		            $scope.userName = "${sessionScope.userName}";
		            $scope.bookmark = {};
		            $scope.bookmarkList = [];
		            //获取所有书签
		            $scope.getBookmarks = function(){
			            $http({
	                        url: "${basePath}/getBookmarks",
	                        method: "GET",
	                        params:{userName:$scope.userName},
	                        paramSerializer: '$httpParamSerializerJQLike'
	                    }).then(function successCallback(data) {
	                        if(data.data.code==0){
	                            $scope.bookmarkList = data.data.data;
	                        }else{
                                swal(data.data.msg,"","error");
	                        }
	                    }, function errorCallback(error) {
	                            swal("服务器异常","","error");
	                    });
	                };
	                $scope.getBookmarks();
                    //编辑书签信息
                    $scope.editBookmark = function(bookmarkInfo){
                        $.each(bookmarkInfo,function(key,value){
                            $scope.bookmark[key]=value;
                        });
                        $("#bookmarkModal").modal("show");
                    };
                    //删除书签信息
                    $scope.delBookmark = function(id){
                        swal({
                            title:"确定要删除这个书签吗?",
                            type: 'warning',
                            showCancelButton: true,
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                        },function(isConfirm) {
                            if(isConfirm){
	                            $http({
                                    url: "${basePath}/delBookmark",
                                    method: "POST",
                                    params:{userName:$scope.userName,id:id},
                                    paramSerializer: '$httpParamSerializerJQLike'
                                }).then(function successCallback(data) {
                                    if(data.data.code==0){
                                        $(".modal").modal("hide");
                                        $scope.getBookmarks();
                                    }else{
                                        swal(data.data.msg,"","error");
                                    }
                                }, function errorCallback(error) {
                                      swal("服务器异常","","error");
                                }); 
                            }
                        });
                    };
	                //保存书签信息
	                $scope.saveBookmark = function(){
                        $scope.bookmark.userName = $scope.userName;
	                    var id = $scope.bookmark.id;
	                    var url = id!=null?"${basePath}/updateBookmark":"${basePath}/addBookmark";
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
	<body ng-controller="bookmarksController">
        <div style="margin:10px 25px;">
            <!-- modal -->
            <div style="margin:5px 0px;width:100%;height:30px;">
		        <button type="button" class="btn btn-sm btn-primary" data-toggle="modal" data-target="#bookmarkModal">新增书签</button>
	        </div>
			<div class="panel panel-default" ng-repeat="bookmarkCategory in bookmarkList">
				<!-- Default panel contents -->
				<div class="panel-heading">{{bookmarkCategory.category}}</div>
				<!-- List group -->
				<ol class="list-group">
					<li class="list-group-item" ng-repeat="bookmark in bookmarkCategory.bookmarkList | orderBy:'name'">
					   <b>{{$index+1}}.</b><a href="{{bookmark.url}}" target="_blank">{{bookmark.name}}</a>
					   <a href="#" class="text-danger" style="float:right;padding-left:5px;" ng-click="delBookmark(bookmark.id)">删除</a> 
					   <a href="#" style="float:right;" ng-click="editBookmark(bookmark)">编辑</a>
					</li>
				</ol>
			</div>
		</div>
		<!-- 书签管理 -->
		<div class="modal fade" id="bookmarkModal" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                        <h4 class="modal-title">书签信息</h4>
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