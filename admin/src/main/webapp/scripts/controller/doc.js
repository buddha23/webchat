define(['app', 'directive/ng-pager', 'directive/ng-file', 'filter/filesize'], function (app) {
    'use strict';

    var docStates = '错误,正常,处理中,处理失败,待审核,审核未通过,无法解析'.split(',');
    var docStateColors = 'danger,success,secondary,warning,primary,danger,danger'.split(',');

    app.controller('docListCtrl', ['$scope', '$rootScope', '$http', function ($scope, $rootScope, $http) {
        if (!$rootScope.docParams) {
            $rootScope.docParams = {};
        }
        $scope.ids = [];
        $scope.docStates = docStates;
        $scope.docStateColors = docStateColors;
        $scope.searchKey = $rootScope.docParams.key || '';
        $scope.params = {
            state: $rootScope.docParams.state || 0,
            key: $rootScope.docParams.key || null,
            sortBy: $rootScope.docParams.sortBy || null,
            sortType: $rootScope.docParams.sortType || null,
            page: $rootScope.docParams.page || 1,
            size: 20
        };

        // 获取数据
        $scope.getData = function () {
            $http.get('admin/doc', {params: $scope.params}).success(function (data) {
                $scope.data = data;
                $scope.params.page = data.number + 1;
                $rootScope.docParams.page = $scope.params.page;
                $rootScope.docParams.sortBy = $scope.params.sortBy;
                $rootScope.docParams.sortType = $scope.params.sortType;
                $rootScope.docParams.state = $scope.params.state;
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
            $rootScope.docParams.key = $scope.searchKey;
            $scope.params.page = 1;
            $scope.getData();
        };
        // 删除
        $scope.delete = function (a) {
            if (confirm('管理员删除文档将硬性删除该文档的所有信息且无法找回\n确定要删除【' + a.title + '】吗？')) {
                $http.delete('admin/doc/' + a.id).success(function () {
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

        $scope.reviewsChecked = function () {
            var ids = [];
            $('tbody :checkbox:checked').each(function () {
                ids.push($(this).val());
            });
            if (ids.length > 0 && confirm('确定要通过这【' + ids.length + '】文档吗？')) {
                $.ajax({
                    type: 'post',
                    url: 'admin/doc/review',
                    data: {ids: ids},
                    success: function (result) {
                        if (result.data && result.data.length > 0) {
                            var msg = '【以下文档执行失败】\n';
                            for (var i = 0; i < Math.min(result.data.length, 10); i++) {
                                msg += result.data[i] + '\n';
                            }
                            alert(msg);
                        }
                        $scope.getData();
                    },
                    error: function (e) {
                        console.log(e);
                        alert("处理失败！");
                    }
                });
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
    }]).controller('docDetailCtrl', ['$scope', '$http', '$state', '$stateParams', function ($scope, $http, $state, $stateParams) {
        $http.get('i/doc_categories').success(function (docCategories) {
            $scope.categories = docCategories;
            $scope.vars = {};

            if ($stateParams.id && $stateParams.id > 0) {
                $http.get('admin/doc/' + $stateParams.id).success(function (data) {
                    if (data.tags)
                        $scope.vars.tags = data.tags.join(' ');
                    $scope.data = data;
                    setCategory(docCategories, data);
                });
            }
        });

        $http.get('i/doc_tags', {limit: 20}).success(function (tags) {
            if (tags && tags.length > 0) {
                $scope.tags = tags;
            }
        });

        // 设置category选项
        function setCategory(docCategories, data) {
            if (data.docCategory) {
                for (var i = 0; i < docCategories.length; i++) {
                    if (docCategories[i].id == data.docCategory.fid) {
                        $scope.categoryOne = docCategories[i];
                        for (var j = 0; j < docCategories[i].children.length; j++) {
                            if (docCategories[i].children[j].id == data.docCategory.id) {
                                $scope.categoryTwo = docCategories[i].children[j];
                            }
                        }
                    }
                }
            }
        }

        $scope.addTag = function (tag) {
            if (!$scope.vars.tags) {
                $scope.vars.tags = tag;
            } else {
                var tags = $scope.vars.tags.trim().replace(/\s{2,}/g, ' ').split(' ');
                if (tags.indexOf(tag) == -1) {
                    tags.push(tag);
                    $scope.vars.tags = tags.join(' ');
                }
            }
        };

        $scope.review = function (bool) {
            if (confirm('确定审核' + (bool ? '通过' : '不通过') + '吗？')) {
                $http.post('admin/doc/' + $scope.data.id + '/review?reviewOk=' + bool)
                    .success(function (result) {
                        $scope.data.state = result.data;
                    }).error(function (e) {
                        console.log('当前状态：' + docStates[e.data] || 'null');
                        alert(e.message || '操作失败，请联系系统管理员');
                    });
            }
        };

        $scope.fileChanged = function (file) {
            if (!$scope.data.id || !file) {
                return;
            }
            var form = new FormData();
            form.append('file', file);
            $http({
                url: 'admin/doc/' + $scope.data.id + '/doc',
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
            }).error(function (data) {
                $scope.data.file = null;
                $scope.upPorgress = 0;
                alert(data.message || '文档处理失败');
            });
        };

        $scope.save = function () {
            if ($scope.categoryTwo) {
                $scope.data.categoryId = $scope.categoryTwo.id;
            }
            if ($scope.vars.tags)
                $scope.data.tags = $scope.vars.tags.split(' ');
            if ($scope.data && $scope.data.id) {
                $http.put('admin/doc/' + $scope.data.id, $scope.data)
                    .success(function (data) {
                        $scope.data = data;
                        alert('修改完成！');
                    }).error(function () {
                        alert('修改失败！');
                    });
            } else if ($scope.data && $scope.data.title) {
                $http.post('admin/doc', $scope.data)
                    .success(function (data) {
                        $scope.data = data;
                        alert('添加完成！');
                    }).error(function () {
                        alert('添加失败！');
                    });
            }
        };

        $scope.delete = function () {
            if (confirm('管理员删除文档将硬性删除该文档的所有信息且无法找回\n确定要删除【' + $scope.data.title + '】吗？')) {
                $http.delete('admin/doc/' + $scope.data.id).success(function () {
                    history.back();
                }).error(function (d) {
                    console.log(d);
                    alert("删除失败！");
                });
            }
        };
    }]);
});