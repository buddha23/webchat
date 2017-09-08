define(['app', 'directive/ng-pager', 'directive/ng-file', 'filter/filesize'], function (app) {
    'use strict';
    app.controller('userScorePayCtrl', ['$scope', '$rootScope', '$http', function ($scope, $rootScope, $http) {

        if (!$rootScope.docParams) {
            $rootScope.docParams = {};
        }

        $scope.beginTime = $rootScope.docParams.beginTime || '';
        $scope.endTime = $rootScope.docParams.endTime || '';

        $scope.params = {
            userId: $rootScope.docParams.userId || null,
            searchkey: $rootScope.docParams.searchkey || '',
            beginTime: $rootScope.docParams.beginTime || null,
            endTime: $rootScope.docParams.endTime || null,
            tradeType: '0',
            page: $rootScope.docParams.page || 1,
            size: 20
        };

        // 获取数据
        $scope.getData = function () {
            $http.get('admin/userreport/scorePay', {params: $scope.params}).success(function (data) {
                $scope.data = data;
                $scope.params.page = data.number + 1;
                $rootScope.docParams.page = $scope.params.page;
            });
        };

        $scope.getTodayCount = function () {
            $http.get('admin/userreport/dailyPayCount').success(function (data) {
                $scope.dailyPay = data;
            });
        };

        $scope.getMonthCount = function () {
            $http.get('admin/userreport/monthPayCount').success(function (data) {
                $scope.monthPay = data;
            });
        };

        $scope.adminGive = function () {
            $http.get('admin/userreport/adminGive').success(function (data) {
                $scope.adminGive = data;
            });
        };

        $scope.doSearch = function () {
            delete $scope.data;
            $scope.params.searchkey = $scope.searchKey;
            $scope.params.beginTime = $scope.beginTime;
            $scope.params.endTime = $scope.endTime;

            $rootScope.docParams.beginTime = $scope.beginTime;
            $rootScope.docParams.endTime = $scope.endTime;
            $rootScope.docParams.searchkey = $scope.searchKey;
            $scope.params.page = 1;

            //console.log($scope.params);
            $scope.getData();
        };

        $scope.onSearchKeyPress = function (e) {
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                $scope.doSearch();
            }
        };

        // 获取初始数据
        $scope.getData();
        $scope.getTodayCount();
        $scope.getMonthCount();
        $scope.adminGive();

        $scope.getPage = function (page) {
            $scope.params.page = page;
            $scope.getData();
        };

        $scope.state2str = function (state) {
            switch (state) {
                case "new":
                    return '新建';
                case 'audited':
                    return '审核';
                case 'succ':
                    return '成功';
                case 'fail':
                    return '失败';
                default:
                    return '-';
            }
        };

        $scope.payway2str = function (payWay) {
            switch (payWay) {
                case 'alipay':
                    return '支付宝';
                case 'wechatpay':
                    return '微信';
                default:
                    return '-';
            }
        };


    }]);
});