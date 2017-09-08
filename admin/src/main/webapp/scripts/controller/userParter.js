define(['app', 'layer', 'directive/ng-pager', 'directive/ng-file'], function (app, layer) {
    'use strict';

    var userStates = [];
    userStates[-1] = '删除';
    userStates[1] = '正常';
    userStates[-2] = '废弃';

    app.controller('userParterCtrl', ['$scope', '$rootScope', '$http', function ($scope, $rootScope, $http) {
        if (!$rootScope.userParams) {
            $rootScope.userParams = {};
        }
        $scope.states = userStates;
        $scope.searchKey = $rootScope.userParams.key || '';
        $scope.params = {
            key: $rootScope.userParams.key || null,
            sortBy: $rootScope.userParams.sortBy || null,
            sortType: $rootScope.userParams.sortType || null,
            page: $rootScope.userParams.page || 1,
            size: 20
        };

        // 获取数据
        $scope.getData = function () {
            $http.get('admin/user/parter', {params: $scope.params}).success(function (data) {
                $scope.data = data;
                $scope.params.page = data.number + 1;
                $rootScope.userParams.page = $scope.params.page;
                $rootScope.userParams.sortBy = $scope.params.sortBy;
                $rootScope.userParams.sortType = $scope.params.sortType;
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
            $rootScope.userParams.key = $scope.searchKey;
            $scope.params.page = 1;
            $scope.getData();
        };

        // 删除
        $scope.deleteParter = function (a) {
            if (confirm('确定要删除合伙人【' + a.name + '】吗？')) {
                $http.delete('admin/user/parter/' + a.id).success(function () {
                    a.state = 2;
                }).error(function (d) {
                    console.log(d);
                    alert("删除失败！");
                });
            }
        };

        var sortTypes = ['', 'desc', 'asc'];
        var scoreCounts = 0;

        $scope.sortBy = function (column, e) {
            $scope.params.sortBy = column;
            if (column == "total_score") {
                scoreCounts++;
                $scope.params.sortType = sortTypes[scoreCounts % 3];
            }
            $scope.getData();
        };

        // 获取初始数据
        $scope.getData();

        $scope.getPage = function (page) {
            $scope.params.page = page;
            $scope.getData();
        };
    }]);

});