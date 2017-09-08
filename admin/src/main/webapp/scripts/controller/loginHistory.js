define(['app', 'directive/ng-pager'], function (app) {
    'use strict';

    app.filter('zero2t', function () {
        return function (a) {
            return a == 0 ? '-' : a;
        }
    });

    app.controller("loginHistoryCtrl", ['$scope', '$rootScope', '$http', function ($scope, $rootScope, $http) {

        $scope.searchKey = '';
        $scope.beginTime = '';
        $scope.endTime = '';
        $scope.params = {
            beginTime: '',
            endTime: '',
            searchkey: '',
            sortBy: null,
            sortType: '',
            page: 1,
            size: 20
        };

        // 获取数据
        $scope.getData = function () {
            $http.get('admin/userreport/longinHistory', {params: $scope.params}).success(function (data) {
                $scope.data = data;
                $scope.params.page = data.number + 1;
            })
        };
        $scope.doSearch = function () {
            delete $scope.data;
            $scope.params.beginTime = $scope.beginTime;
            $scope.params.endTime = $scope.endTime;
            $scope.params.searchKey = $scope.searchKey;
            $scope.params.page = 1;
            $scope.getData();
        };
        $scope.onSearchKeyPress = function (e) {
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                $scope.doSearch();
            }
        };
        $scope.getData();

        $scope.getPage = function (page) {
            $scope.params.page = page;
            $scope.getData();
        };

        var sortTypes = ['asc', 'desc'];
        var totleCounts = 0;
        $scope.sortBy = function (column, e) {
            $scope.params.sortBy = column;
            if (column == "totle") {
                totleCounts++;
                $scope.params.sortType = sortTypes[totleCounts % 2];
            }
            $scope.getData();
        };


    }]);
});