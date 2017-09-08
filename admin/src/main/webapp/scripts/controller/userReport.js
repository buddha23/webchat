define(['app', 'echarts', 'directive/ng-pager'], function (app, echarts) {
    'use strict';

    var userStates = [];
    userStates[-1] = '删除';
    userStates[1] = '正常';

    app.controller("reportUserListCtrl", ['$scope', '$rootScope', '$http', function ($scope, $rootScope, $http) {
        if (!$rootScope.reportUserParams) {
            $rootScope.reportUserParams = {};
        }
        $scope.states = userStates;
        $scope.searchKey = $rootScope.reportUserParams.key || '';
        $scope.beginTime = $rootScope.reportUserParams.beginTime || '';
        $scope.endTime = $rootScope.reportUserParams.endTime || '';
        $scope.params = {
            content: $rootScope.reportUserParams.key || null,
            beginTime: $rootScope.reportUserParams.beginTime || '',
            endTime: $rootScope.reportUserParams.endTime || '',
            page: $rootScope.reportUserParams.page || 1,
            size: 20
        };

        // 获取数据
        $scope.getData = function () {
            $http.get('admin/userreport', {params: $scope.params}).success(function (data) {
                $scope.data = data;
                $scope.params.page = data.number + 1;
                $rootScope.reportUserParams.page = $scope.params.page;
                $rootScope.reportUserParams.beginTime = $scope.params.beginTime;
                $rootScope.reportUserParams.endTime = $scope.params.endTime;
            })
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
            $scope.params.beginTime = $scope.beginTime;
            $scope.params.endTime = $scope.endTime;
            $rootScope.reportUserParams.key = $scope.searchKey;
            $rootScope.reportUserParams.beginTime = $scope.beginTime;
            $rootScope.reportUserParams.endTime = $scope.endTime;
            $scope.params.page = 1;
            $scope.getData();
        };

        $scope.getData();

        $scope.getPage = function (page) {
            $scope.params.page = page;
            $scope.getData();
        };
    }]);
});