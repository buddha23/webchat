define(['app', 'layer', 'directive/ng-pager'], function (app, layer) {
    'use strict';

    app.filter('trusthtml', ['$sce', function ($sce) {
        return function (text) {
            return $sce.trustAsHtml(text);
        }
    }]);

    app.controller('commodityListCtrl', ['$scope', '$rootScope', '$http', function ($scope, $rootScope, $http) {

        $scope.type = [];
        $scope.type[1] = '出售';
        $scope.type[2] = '求购';

        $scope.searchKey = '';
        $scope.params = {
            content: null,
            page: 1,
            size: 20
        };

        // 获取数据
        $scope.getData = function () {
            $http.get('admin/commodity/list', {params: $scope.params}).success(function (data) {
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
            $scope.params.page = 1;
            $scope.getData();
        };

        // 删除
        $scope.delete = function (a) {
            if (confirm('确定要删除【' + a.name + '】吗？')) {
                $http.delete('admin/commodity/' + a.id).success(function () {
                    for (var i = 0; i < $scope.data.content.length; i++) {
                        if (a.id === $scope.data.content[i].id) {
                            $scope.data.content.splice(i, 1);
                            break;
                        }
                    }
                    layer.alert('删除成功');
                }).error(function (d) {
                    console.log(d);
                    layer.alert("删除失败！");
                });
            }
        };

        // 批量删除
        //$scope.ids = [];
        //$scope.deleteSome = function () {
        //    if (confirm('确定要删除所选内容吗？')) {
        //        $http.post("admin/posts/deleteSome", $scope.ids)
        //            .success(function () {
        //                $scope.getData();
        //                $scope.ids.splice(0, $scope.ids.length);
        //                alert('删除完成！');
        //            }).error(function (d) {
        //                alert('删除失败！');
        //            }
        //        )
        //    }
        //};

        //$scope.chose = function ($event, m) {
        //    var checkbox = $event.target;
        //    var action = (checkbox.checked ? 'add' : 'remove');
        //    var idx = $scope.ids.indexOf(m);
        //    if (action == 'add' && idx == -1) {
        //        $scope.ids.push(m);
        //    }
        //    if (action == 'remove' && idx != -1) {
        //        $scope.ids.splice(idx, 1);
        //    }
        //};
        //
        //$scope.choseAll = function ($event) {
        //    var checkbox = $event.target;
        //    var action = (checkbox.checked ? 'add' : 'remove');
        //    if (action == 'add') {
        //        for (var i = 0; i < $scope.data.content.length; i++) {
        //            if ($scope.ids.indexOf($scope.data.content[i]) == -1)
        //                $scope.ids.push($scope.data.content[i]);
        //        }
        //    } else {
        //        $scope.ids.splice(0, $scope.ids.length);
        //    }
        //};

        var pageContent;

        $scope.getCategories = function () {
            $http.get('admin/commodity/categories').success(function (data) {
                pageContent = '<div class="am-margin-left am-selected-content am-margin-right"><label>选择分类:</label><select id="choseCg" class="am-selected-content am-margin-left">';
                $scope.categories = data;
                if (data.length > 0) {
                    data.forEach(function (c) {
                        pageContent = pageContent + '<option value="' + c.id + '">' + c.name + '</option>';
                    });
                }
                pageContent = pageContent + '</select></div>';
            });
        };

        $scope.changeCate = function (p) {
            if (p && p.id) {
                var pagei = layer.open({
                    type: 1,   //0-4的选择,
                    title: '选择分类',
                    border: [0],
                    shadeClose: true,
                    area: ['auto', 'auto'],
                    content: pageContent,
                    btn: ['确认', '取消'],
                    yes: function () {
                        var choseCg = $('#choseCg').val();
                        console.log(parseInt(choseCg));
                        if (choseCg != null) {
                            $http.post('admin/commodity/changeCategory/' + p.id, parseInt(choseCg))
                                .success(function () {
                                    $scope.getData();
                                    layer.close(pagei);
                                    layer.msg('操作成功');
                                }).error(function () {
                                layer.msg('操作失败');
                            });
                        }
                    },
                    btn2: function () {
                        layer.close(pagei);
                    }
                });
            }
        };


        // 获取初始数据
        $scope.getData();
        $scope.getCategories();

        $scope.getPage = function (page) {
            $scope.params.page = page;
            $scope.getData();
        };
    }]).controller('commodityDetailCtrl', ['$scope', '$http', '$state', '$stateParams', function ($scope, $http, $state, $stateParams) {

        $scope.type = [];
        $scope.type[1] = '出售';
        $scope.type[2] = '求购';

        $scope.degree = [];
        $scope.degree[1] = '全新';
        $scope.degree[2] = '二手';

        if ($stateParams.id && $stateParams.id > 0) {
            $http.get('admin/commodity/' + $stateParams.id).success(function (data) {
                $scope.data = data;
            });
        }

    }]);
});