define(['app', 'directive/ng-pager', 'directive/ng-file', 'filter/filesize'], function (app) {
    'use strict';

    app.controller("moderatorScoreHistoryCtr", ['$scope', '$rootScope', '$http', function ($scope, $rootScope, $http) {
        $scope.searchKey = '';
        $scope.beginTime = '';
        $scope.endTime = '';
        $scope.params = {
            content: null,
            beginTime: '',
            endTime: '',
            page: 1,
            size: 20
        };

        // 获取数据
        $scope.getData = function () {
            console.log($scope.params);
            $http.get('admin/docCategory/moderatorsScoreHistory', {params: $scope.params}).success(function (data) {
                $scope.data = data;
                $scope.params.page = data.number + 1;
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
            $scope.params.content = $scope.searchKey;
            $scope.params.beginTime = $scope.beginTime;
            $scope.params.endTime = $scope.endTime;
            $scope.params.page = 1;
            $scope.getData();
        };

        $scope.getData();

        $scope.getPage = function (page) {
            $scope.params.page = page;
            $scope.getData();
        };

    }])
});