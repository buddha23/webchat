define(['app', 'layer', 'directive/ng-pager', 'directive/ng-file', 'filter/filesize', 'filter/filename'], function (app, layer) {
    'use strict';

    app.controller('volumeBuyListCtrl', ['$scope', '$rootScope', '$http', function ($scope, $rootScope, $http) {

        $scope.buytype = [];
        $scope.buytype[1] = "牛人币购买";
        $scope.buytype[2] = "现金购买";
        $scope.buytype[3] = "邀请卡购买";

        if (!$rootScope.volumeParams) {
            $rootScope.volumeParams = {};
        }
        $scope.searchKey = $rootScope.volumeParams.key || '';
        $scope.params = {
            key: $rootScope.volumeParams.key || null,
            page: $rootScope.volumeParams.page || 1,
            size: 10,
            buyType: 0
        };

        // 获取数据
        $scope.getData = function () {
            $http.get('admin/video/volumeBuys', {params: $scope.params}).success(function (data) {
                $scope.data = data;
                $scope.params.page = data.number + 1;
                $rootScope.volumeParams = $scope.params;
            }).error(function (data) {
                console.log(data);
                layer.msg('查询数据失败!');
            });
        };

        $scope.getPage = function (page) {
            $scope.params.page = page;
            $scope.getData();
        };

        $scope.doSearch = function () {
            delete $scope.data;
            $scope.params.key = $scope.searchKey;
            $rootScope.volumeParams.key = $scope.searchKey;
            $scope.params.page = 1;
            $scope.getData();
        };

        // 获取初始数据
        $scope.getData();

    }]);
});
