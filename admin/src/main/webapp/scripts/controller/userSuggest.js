define(['app', 'umeditor', 'directive/ng-pager', 'directive/ng-file'], function (app, UM) {
    'use strict';

    app.filter(
        'trusthtml', ['$sce', function ($sce) {
            return function (text) {
                return $sce.trustAsHtml(text);
            }
        }]
    );

    app.filter(
        'html2str', [function () {
            return function (strs) {
                return strs.replace(/<.*?>/ig, "");
                //console.log(strs.replace(/<.*?>/ig, ""));
            }
        }]
    );

    app.controller('userSuggestCtrl', ['$scope', '$http', function ($scope, $http) {

        $scope.params = {key: '',replyState:'', page: ''};
        // 获取数据
        $scope.getData = function () {
            $http.get('admin/suggest', {params: $scope.params}).success(function (data) {
                $scope.data = data;
                $scope.params.page = data.number + 1;
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
            //$rootScope.docParams.key = $scope.searchKey;
            $scope.params.page = 1;
            $scope.getData();
        };


        $scope.delete = function (a) {
            if (confirm('确定要删除本条记录吗？')) {
                $http.delete('admin/suggest/' + a.id).success(function () {
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
            console.log($scope.ids);
            if (confirm('确定要删除所选内容吗？')) {
                $http.post("admin/suggest/deleteSome", $scope.ids)
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

        // 获取初始数据
        $scope.getData();

        $scope.getPage = function (page) {
            $scope.params.page = page;
            $scope.getData();
        };
    }]).controller('userSuggestDetailCtrl', function ($scope, $http, $state, $stateParams) {
        if ($stateParams.id && $stateParams.id > 0) {
            $http.get('admin/suggest/' + $stateParams.id).success(function (data) {
                $scope.data = data;
            });
        }

        $scope.save = function () {
            if ($scope.data && $scope.data.id) {
                $http.put('admin/suggest/' + $scope.data.id, $scope.data)
                    .success(function (data) {
                        alert('回复成功！');
                    }).error(function () {
                    alert('回复失败！');
                });
            }
        }

    })
});
