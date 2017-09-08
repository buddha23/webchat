define(['app', 'directive/ng-pager', 'directive/ng-file', 'filter/filesize'], function (app) {
    'use strict';

    app.controller('docTagListCtrl', ['$scope', '$rootScope', '$http', function ($scope, $rootScope, $http) {
        if (!$rootScope.docParams) {
            $rootScope.docParams = {};
        }
        $scope.searchKey = $rootScope.docParams.key || '';
        $scope.params = {
            key: $rootScope.docParams.key || null,
            sortBy: $rootScope.docParams.sortBy || null,
            sortType: $rootScope.docParams.sortType || null,
            page: $rootScope.docParams.page || 1,
            size: 20
        };

        // 获取数据
        $scope.getData = function () {
            $http.get('admin/docTag', {params: $scope.params}).success(function (data) {
                $scope.data = data;
                $scope.params.page = data.number + 1;
                $rootScope.docParams.page = $scope.params.page;
                $rootScope.docParams.sortBy = $scope.params.sortBy;
                $rootScope.docParams.sortType = $scope.params.sortType;
            })
        };
        $scope.onSearchKeyPress = function (e) {
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                $scope.doSearch();
            }
        };

        $scope.doSearch = function () {
            $scope.params.key = $scope.searchKey;
            $rootScope.docParams.key = $scope.searchKey;
            $scope.params.page = 1;
            $scope.getData();
        };
        // 删除
        $scope.delete = function (a) {
            if (confirm('确定要删除【' + a.name + '】吗？')) {
                $http.delete('admin/docTag/' + a.id).success(function () {
                    //去除列表删除的记录
                    for (var i = 0; i < $scope.data.content.length; i++) {
                        if (a.id === $scope.data.content[i].id) {
                            $scope.data.content.splice(i, 1);
                            break;
                        }
                    }
                }).error(function (data) {
                    console.log(data);
                    if (data.code == 90500) {
                        alert('该标签已有文库使用,无法删除！');
                    } else {
                        alert('修改失败！');
                    }
                    ;
                });
            }
        };

        // 获取初始数据
        $scope.getData();

        $scope.getPage = function (page) {
            $scope.params.page = page;
            $scope.getData();
        };
    }]).controller('docTagDetailCtrl', ['$scope', '$http', '$state', '$stateParams', function ($scope, $http, $state, $stateParams) {

        if ($stateParams.id && $stateParams.id > 0) {
            $http.get('admin/docTag/' + $stateParams.id).success(function (data) {
                $scope.data = data;

            });
        }

        $scope.save = function () {
            if ($scope.data && $scope.data.id) {
                $http.put('admin/docTag/' + $scope.data.id, $scope.data)
                    .success(function (data) {
                        $scope.data = data;
                        alert('修改完成！');
                    }).error(function (data) {

                        if (data.code == 90500) {
                            alert('标签名已有重复,请重试！');
                        } else {
                            alert('修改失败！');
                        }
                    });
            } else if ($scope.data && $scope.data.name) {

                $http.post('admin/docTag', $scope.data)
                    .success(function (data) {
                        $scope.data = data;
                        alert('添加完成！');
                    }).error(function (data) {
                        if (data.code == 90500) {
                            alert('标签名已有重复,请重试！');
                        } else {
                            alert('修改失败！');
                        }
                    });
            }
        }


    }]);
});