define(['app', 'layer', 'directive/ng-pager', 'directive/ng-file', 'filter/filesize', 'filter/filename'], function (app, layer) {
    'use strict';

    app.controller('volumeListCtrl', ['$scope', '$rootScope', '$http', function ($scope, $rootScope, $http) {
        var sort = ['desc', 'asc', null];
        if (!$rootScope.volumeParams) {
            $rootScope.volumeParams = {};
        }
        $scope.ids = [];
        $scope.searchKey = $rootScope.volumeParams.key || '';
        $scope.params = {
            key: $rootScope.volumeParams.key || null,
            page: $rootScope.volumeParams.page || 1,
            size: 10,
            tagId: 0
        };

        // 获取数据
        $scope.getData = function () {
            $http.get('admin/video/list', {params: $scope.params}).success(function (data) {
                $scope.data = data;
                $scope.params.page = data.number + 1;
                $rootScope.volumeParams = $scope.params;
                $scope.getAllTag();
            }).error(function (data) {
                console.log(data);
                layer.msg('查询数据失败!');
            });
        };

        $scope.getAllTag = function () {
            $http.get('admin/video/getAllTag').success(function (data) {
                $scope.allTag = data;
            }).error(function () {
                layer.alert('获取标签失败');
            });
        };

        $scope.getPage = function (page) {
            $scope.params.page = page;
            $scope.getData();
        };

        $scope.sortBy = function (col) {
            if ($scope.params.sortBy && $scope.params.sortCol === col) {
                $scope.params.sortBy = sort[(sort.indexOf($scope.params.sortBy) + 1) % sort.length];
            } else {
                $scope.params.sortBy = sort[0];
            }
            if ($scope.params.sortBy)
                $scope.params.sortCol = col;
            else
                delete $scope.params.sortCol;
            $scope.params.page = 1;
            $scope.getData();
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
            $rootScope.volumeParams.key = $scope.searchKey;
            $scope.params.page = 1;
            $scope.getData();
        };
        // 删除
        $scope.delete = function (a) {
            if (confirm('确定要删除【' + a.name + '】吗？')) {
                $http.delete('admin/video/' + a.id).success(function () {
                    var idx = $scope.data.content.indexOf(a);
                    if (idx > -1) {
                        $scope.data.content.splice(idx, 1);
                    }
                }).error(function (d) {
                    console.log(d);
                    alert("删除失败！");
                });
            }
        };

        //通知
        $scope.vodBeginMsg = function (a) {
            if (confirm('确定发送课程【' + a.name + '】开课提醒吗？')) {
                $http.get('admin/video/vodBeginMsg/' + a.id).success(function () {
                    layer.msg("通知成功");
                }).error(function (d) {
                    console.log(d);
                    alert("删除失败！");
                });
            }
        };

        $scope.vodUpdateMsg = function (a) {
            if (confirm('确定发送课程【' + a.name + '】更新通知吗？')) {
                $http.get('admin/video/vodUpdateMsg/' + a.id).success(function () {
                    layer.msg("通知成功");
                }).error(function (d) {
                    console.log(d);
                    alert("删除失败！");
                });
            }
        };

        // 获取初始数据
        $scope.getData();
    }]);
});