define(['app', 'directive/ng-pager', 'directive/ng-file', 'filter/filesize'], function (app) {
    'use strict';

    app.controller("reportDocListCtrl", ['$scope', '$rootScope', '$http', '$state', '$stateParams', function ($scope, $rootScope, $http, $state, $stateParams) {
        if (!$rootScope.reportDocParams) {
            $rootScope.reportDocParams = {};
        }
        if (!$rootScope.monthReportParams) {
            $rootScope.monthReportParams = {};
            $rootScope.monthReportParams.time = '';
        } else {
            $rootScope.reportDocParams.beginTime = $rootScope.monthReportParams.time;
            $rootScope.reportDocParams.endTime = $rootScope.monthReportParams.time;
        }
        $scope.searchKey = $rootScope.reportDocParams.content || '';
        $scope.beginTime = $rootScope.reportDocParams.beginTime || '';
        $scope.endTime = $rootScope.reportDocParams.endTime || '';
        $scope.params = {
            content: $rootScope.reportDocParams.content || null,
            beginTime: $rootScope.reportDocParams.beginTime || '',
            endTime: $rootScope.reportDocParams.endTime || '',
            page: $rootScope.reportDocParams.page || 1,
            size: 20
        };

        // 获取数据
        $scope.getData = function () {
            console.log($scope.params);
            $http.get('admin/docreport', {params: $scope.params}).success(function (data) {
                $scope.data = data;
                $scope.params.page = data.number + 1;
                $rootScope.reportDocParams = $scope.params;
            });
            $http.get('admin/docreport/downloads', {params: $scope.params}).success(function (data) {
                $scope.totalDownloads = data;
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
            $scope.params.content = $scope.searchKey;
            $scope.params.beginTime = $scope.beginTime;
            $scope.params.endTime = $scope.endTime;
            $scope.params.page = 1;
            $rootScope.reportDocParams = $scope.params;
            $scope.getData();
        };

        $scope.getData();

        $scope.getPage = function (page) {
            $scope.params.page = page;
            $scope.getData();
        };

    }])
});