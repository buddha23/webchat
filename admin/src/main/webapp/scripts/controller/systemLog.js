define(['app', 'directive/ng-pager', 'directive/ng-file', 'filter/filesize'], function (app) {
    'use strict';

    app.filter(
        'trusthtml', ['$sce', function ($sce) {
            return function (text) {
                return $sce.trustAsHtml(text);
            }
        }]
    );

    app.controller('systemLogCtrl', function ($scope, $rootScope, $http) {
        if (!$rootScope.docParams) {
            $rootScope.docParams = {};
        }
        $scope.params = {
            p: $rootScope.docParams.p || null,
            page: $rootScope.docParams.page || 1,
            size: 20
        };
        // 获取数据
        $scope.getData = function () {
            $http.get('admin/systemLog', {params: $scope.params}).success(function (data) {
                $scope.data = data;
                $scope.params.page = data.number + 1;
                $rootScope.docParams.page = $scope.params.page;
            })
        };
        $scope.onSearchKeyPress = function (e) {
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                $scope.doSearch();
            }
        };

        $scope.doSearch = function () {
            $scope.params.p = $scope.searchKey;
            $rootScope.docParams.p = $scope.searchKey;
            $scope.params.page = 1;
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

        $scope.getPage = function (page) {
            $scope.params.page = page;
            $scope.getData();
        };


        // 删除该条记录
        $scope.delete = function (a) {
            if (confirm('确定删除该条记录？')) {
                $.ajax({
                    type: 'DELETE',
                    url: 'admin/systemLog/' + a.id,
                    success: function (result) {
                        alert("删除成功");
                        $scope.data.content.removeChild(a);
                        $scope.$apply($scope.data);
                    },
                    error: function (r) {
                        alert('删除失败！');
                    }
                });
            }
        };

        // 批量删除
        $scope.ids = [];
        $scope.deleteSome = function () {
            if (confirm('确定要删除所选内容吗？') && $scope.ids.length > 0) {
                $http.post("admin/systemLog/deleteSome", $scope.ids)
                    .success(function () {
                        location.reload();
                        //$scope.getData();
                        $scope.ids.splice(0, $scope.ids.length);
                        alert('删除完成！');
                    }).error(function (d) {
                        alert('删除失败！');
                    }
                )
            }
        };

        $scope.chose = function ($event, m) {
            var checkbox = $event.target;
            var action = (checkbox.checked ? 'add' : 'remove');
            var idx = $scope.ids.indexOf(m);
            if (action == 'add' && idx == -1) {
                $scope.ids.push(m);
            }
            if (action == 'remove' && idx != -1) {
                $scope.ids.splice(idx, 1);
            }
        };

        $scope.choseAll = function ($event) {
            var checkbox = $event.target;
            var action = (checkbox.checked ? 'add' : 'remove');
            if (action == 'add') {
                for (var i = 0; i < $scope.data.content.length; i++) {
                    if ($scope.ids.indexOf($scope.data.content[i].id) == -1)
                        $scope.ids.push($scope.data.content[i].id);
                }
            } else {
                $scope.ids.splice(0, $scope.ids.length);
            }
            console.log($scope.ids);
        };

    }).controller('logDetailCtrl', ['$scope', '$http', '$state', '$stateParams', function ($scope, $http, $state, $stateParams) {
        if ($stateParams.id && $stateParams.id > 0) {
            $http.get('admin/systemLog/' + $stateParams.id).success(function (data) {
                $scope.data = data;
            });

            $scope.params = {
                page: 1,
                size: 20
            };
        }

        // 删除该条记录
        $scope.delete = function () {
            if (confirm('确定删除该条记录？')) {
                $.ajax({
                    type: 'DELETE',
                    url: 'admin/systemLog/' + $scope.data.id,
                    success: function () {
                        history.back();
                    },
                    error: function (r) {
                        console.log(r);
                        alert('删除失败！');
                    }
                });
            }
        };
    }]);

});