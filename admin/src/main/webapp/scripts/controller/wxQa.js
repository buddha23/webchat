define(['app', 'directive/ng-pager'], function (app) {
    'use strict';

    app.controller('wxqaListCtrl', ['$scope', '$rootScope', '$http', function ($scope, $rootScope, $http) {
        if (!$rootScope.wxQaParams) {
            $rootScope.wxQaParams = {};
        }
        $scope.searchKey = $rootScope.wxQaParams.key || '';
        $scope.params = {
            q: $rootScope.wxQaParams.key || null,
            page: $rootScope.wxQaParams.page || 1,
            size: 20
        };
        // 获取数据
        $scope.getData = function () {
            $http.get('admin/wxqa', {params: $scope.params}).success(function (data) {
                $scope.data = data;
                $scope.params.page = data.number + 1;
                $rootScope.wxQaParams.page = $scope.params.page;
            })
        };
        $scope.doSearch = function () {
            $scope.params.q = $scope.searchKey;
            $rootScope.wxQaParams.key = $scope.searchKey;
            $scope.params.page = 1;
            $scope.getData();
        };

        // 删除
        $scope.delete = function (a) {
            if (confirm('确定要删除【' + a.q + '】吗？')) {
                $http.delete('admin/wxqa/' + a.id).success(function (data) {
                    for (var i = 0; i < $scope.data.content.length; i++) {
                        if ($scope.data.content[i].id === a.id) {
                            $scope.data.content.splice(i, 1);
                            break;
                        }
                    }
                    alert('删除完成！');
                }).error(function () {
                    alert('删除失败！');
                });
            }
        };
        // 获取初始数据
        $scope.getData();

        $scope.getPage = function (page) {
            $scope.params.page = page;
            $scope.getData();
        }
    }]).controller('wxqaDetailCtrl', ['$scope', '$http', '$state', '$stateParams', function ($scope, $http, $state, $stateParams) {
        if ($stateParams.id && $stateParams.id > 0) {
            $http.get('admin/wxqa/' + $stateParams.id).success(function (data) {
                $scope.data = data;
            });
        }
        $scope.save = function () {
            if ($scope.data && $scope.data.q) {
                $http({
                    method: $scope.data.id ? 'PUT' : 'POST',
                    url: 'admin/wxqa',
                    data: $scope.data
                }).success(function (data) {
                    if (!$scope.data.id) {
                        $scope.data = null;
                    }
                    alert('处理完成！');
                }).error(function (e) {
                    alert(e && e.message || '处理失败！');
                });
            }
        }
    }]);
});