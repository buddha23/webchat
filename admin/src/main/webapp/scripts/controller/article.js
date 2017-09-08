define(['app', 'directive/ng-pager', 'directive/ng-file'], function (app) {
    'use strict';

    app.controller('articleListCtrl', function ($scope, $rootScope, $http) {
        if (!$rootScope.articleParams) {
            $rootScope.articleParams = {};
        }
        $scope.searchKey = $rootScope.articleParams.key || '';
        $scope.params = {
            key: $rootScope.articleParams.key || null,
            sortBy: $rootScope.articleParams.sortBy || null,
            sortType: $rootScope.articleParams.sortType || null,
            page: $rootScope.articleParams.page || 1,
            size: 20
        };
        // 获取数据
        $scope.getData = function () {
            $http.get('admin/article', {params: $scope.params}).success(function (data) {
                $scope.data = data;
                $scope.params.page = data.number + 1;
                $rootScope.articleParams.page = $scope.params.page;
                $rootScope.articleParams.sortBy = $scope.params.sortBy;
                $rootScope.articleParams.sortType = $scope.params.sortType;
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
            $rootScope.articleParams.key = $scope.searchKey;
            $scope.params.page = 1;
            $scope.getData();
        };
        // 删除
        $scope.delete = function (a) {
            if (confirm('确定要删除【' + a.title + '】吗？')) {
                $http.delete('admin/article/' + a.id).success(function () {
                    for (var i = 0; i < $scope.data.content.length; i++) {
                        if (a.id === $scope.data.content[i].id) {
                            $scope.data.content.splice(i, 1);
                            break;
                        }
                    }
                }).error(function (d) {
                    console.log(d);
                    alert("删除失败！");
                });
            }
        };

        //批量删除
        $scope.ids = [];
        $scope.deleteSome = function () {
            //console.log($scope.ids);
            if (confirm('确定要删除所选内容吗？')) {
                $http.post("admin/article/deleteSome", $scope.ids)
                    .success(function (data) {
                        $scope.getData();
                        alert('删除完成！');
                    }).error(function (d) {
                        console.log(d);
                        alert('删除失败！');
                    }
                )
            }
        };

        $scope.chose = function ($event, id) {
            var checkbox = $event.target;
            var action = (checkbox.checked ? 'add' : 'remove');
            var idx = $scope.ids.indexOf(id);
            if (action == 'add' && idx == -1) {
                $scope.ids.push(id);
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
        };

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
        // 获取初始数据
        $scope.getData();

        $scope.getPage = function (page) {
            $scope.params.page = page;
            $scope.getData();
        };
    }).controller('articleDetailCtrl', function ($scope, $http, $state, $stateParams) {
        if ($stateParams.id && $stateParams.id > 0) {
            $http.get('admin/article/' + $stateParams.id).success(function (data) {
                $scope.data = data;
            });
        }

        $scope.fileChanged = function (file) {
            if (!file) {
                return;
            }
            var form = new FormData();
            form.append('file', file);
            $http({
                url: 'admin/article/' + $scope.data.id + '/pdf',
                method: 'POST',
                data: form,
                headers: {
                    'Content-Type': undefined
                },
                uploadEventHandlers: {
                    progress: function (e) {
                        $scope.upPorgress = (e.loaded / e.total).toFixed(2) * 100;
                    }
                }
            }).success(function (data) {
                $scope.data = data;
                $scope.upPorgress = 0;
            }).error(function () {
                $scope.data.file = null;
                $scope.upPorgress = 0;
                alert('文档处理失败');
            });
        };

        $scope.save = function () {
            if ($scope.data && $scope.data.id) {
                $http.put('admin/article', $scope.data)
                    .success(function (data) {
                        $scope.data = data;
                        alert('修改完成！');
                    }).error(function () {
                    alert('修改失败！');
                });
            } else if ($scope.data && $scope.data.title) {
                $http.post('admin/article', $scope.data)
                    .success(function (data) {
                        $scope.data = data;
                        alert('添加完成！');
                    }).error(function () {
                    alert('添加失败！');
                });
            }
        }
    }).controller('articleDownloadCtrl', function ($scope, $http) {
        $scope.doDownload = function () {
            $http.get('admin/article/download').success(function (data) {
                $scope.result = data.message;
            }).error(function () {
                $scope.result = '执行失败！';
            });
        }
    });
});