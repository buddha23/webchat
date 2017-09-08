define(['app', 'layer', 'directive/ng-pager'], function (app, layer) {
    'use strict';

    app.controller('enterpriseListCtrl', ['$scope', '$rootScope', '$http', function ($scope, $rootScope, $http) {

        if (!$rootScope.docParams) {
            $rootScope.docParams = {};
        }
        $scope.searchKey = $rootScope.docParams.key || '';
        $scope.params = {
            key: $rootScope.docParams.key || null,
            page: $rootScope.docParams.page || 1,
            size: 20
        };

        // 获取数据
        $scope.getData = function () {
            $http.get('admin/enterprise', {params: $scope.params}).success(function (data) {
                $scope.data = data;
                $scope.params.page = data.number + 1;
                $rootScope.docParams.page = $scope.params.page;
            });
        };

        $scope.onSearchKeyPress = function (e) {
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                $scope.doSearch();	
            }
        };

        $scope.doSearch = function () {
            delete $scope.data;
            $scope.params.key = $scope.searchKey;
            $rootScope.docParams.key = $scope.searchKey;
            $scope.params.page = 1;
            $scope.getData();
        };
        
        $scope.pass = function(a){
        	   $http.put('admin/enterprise/pass/'+a.id).success(function () {
                 window.location.href = window.location.href;
               });
        }
        
        $scope.unPass = function(a){
     	   $http.put('admin/enterprise/unpass/'+a.id).success(function () {
              window.location.href = window.location.href;
            });
     }
        
 
        $scope.seeImg = function (a) {
        	var content = "";
        	 a.imgPaths.forEach(function (c) {
        		 content = content + '<img src="/files/'+c+'"/>';
             });
            layer.open({
                type: 1,
                title: '营业执照',
                area: '400px',
                content: content
            });
        };

        // 批量删除
        $scope.ids = [];
  


        $scope.choseAll = function ($event) {
            var checkbox = $event.target;
            var action = (checkbox.checked ? 'add' : 'remove');
            if (action == 'add') {
                for (var i = 0; i < $scope.data.content.length; i++) {
                    if ($scope.ids.indexOf($scope.data.content[i]) == -1)
                        $scope.ids.push($scope.data.content[i]);
                }
            } else {
                $scope.ids.splice(0, $scope.ids.length);
            }
        };


        // 获取初始数据
        $scope.getData();

        $scope.getPage = function (page) {
            $scope.params.page = page;
            $scope.getData();
        };
    }]);
});