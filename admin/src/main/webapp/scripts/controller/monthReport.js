define(['app', 'directive/ng-pager', 'directive/ng-file', 'filter/filesize'], function (app) {
    'use strict';

    app.controller("monthRecordListCtrl", ['$scope', '$rootScope', '$http', '$state', function ($scope, $rootScope, $http, $state) {
        if (!$rootScope.monthReportParams) {
            $rootScope.monthReportParams = {};
        }

        $scope.beginTime = $rootScope.monthReportParams.beginTime || '';
        $scope.endTime = $rootScope.monthReportParams.endTime || '';

        $scope.params = {
            beginTime: $rootScope.monthReportParams.beginTime || null,
            endTime: $rootScope.monthReportParams.endTime || null,
            sortBy: $rootScope.monthReportParams.sortBy || null,
            sortType: $rootScope.monthReportParams.sortType || null,
            page: $rootScope.monthReportParams.page || 1,
            size: 20
        };

        // 获取数据
        $scope.getData = function () {
            $http.get('admin/monthreport', {params: $scope.params}).success(function (data) {
                $scope.data = data;
                $scope.params.page = data.number + 1;
                $rootScope.monthReportParams.sortBy = $scope.params.sortBy;
                $rootScope.monthReportParams.sortType = $scope.params.sortType;
                $rootScope.monthReportParams.page = $scope.params.page;
            });


            $http.get('admin/monthreport/totalSum', {params: $scope.params}).success(function (data) {
                $scope.articleViewsSum = getTotalArticleViews(data);
                $scope.docViewsSum = getTotalDocViews(data);
                $scope.articleDownloadsSum = getTotalArticleDownloads(data);
                $scope.docDownloadsSum = getTotalDocDownloads(data);
            })
        };

        $scope.doSearch = function () {
            $scope.params.beginTime = $scope.beginTime;
            $scope.params.endTime = $scope.endTime;

            $rootScope.monthReportParams.beginTime = $scope.beginTime;
            $rootScope.monthReportParams.endTime = $scope.endTime;
            $scope.params.page = 1;
            $scope.getData();
        };

        // 跳转某日下载详情
        $scope.seeDetail = function (time) {
            $rootScope.monthReportParams.time = time;
            $state.go('docReport');
        };

        $scope.getData();

        $scope.getPage = function (page) {
            $scope.params.page = page;
            $scope.getData();
        };


        //计算资料浏览总量
        var getTotalArticleViews = function (data) {
            var sum = 0;
            var docs = data.content;
            sum = docs[0]['articleViews'];
            return sum;
        };


        //计算文库浏览总量
        var getTotalDocViews = function (data) {
            var sum = 0;
            var docs = data.content;
            sum = docs[0]['docViews'];
            return sum;
        };


        //计算资料下载总量
        var getTotalArticleDownloads = function (data) {
            var sum = 0;
            var docs = data.content;
            sum = docs[0]['articleDownloads'];
            return sum;
        };


        //计算文库下载总量
        var getTotalDocDownloads = function (data) {
            var sum = 0;
            var docs = data.content;
            sum = docs[0]['docDownloads'];
            return sum;
        };

        // 排序搜索
        var sortTypes = ['', 'asc', 'desc'];
        var viewsCounts = 0;
        var downloadsCounts = 0;
        var createTimeCounts = 0;
        $scope.sortBy = function (column, e) {
            $scope.params.sortBy = column;
            if (column == "views") {
                viewsCounts++;
                $scope.params.sortType = sortTypes[viewsCounts % 3];
            } else if (column == "downloads") {
                downloadsCounts++;
                $scope.params.sortType = sortTypes[downloadsCounts % 3];
            } else if (column == 'createTime') {
                createTimeCounts++;
                $scope.params.sortType = sortTypes[createTimeCounts % 3];
            }
            $scope.getData();
        };

    }])
});