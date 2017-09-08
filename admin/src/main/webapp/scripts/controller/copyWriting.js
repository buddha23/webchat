define(['app', 'umeditor', 'directive/ng-pager', 'directive/ng-file'], function (app, UM) {
    'use strict';
    app.controller('copyWritingListCtr', ['$scope', '$http', function ($scope, $http) {

        $scope.params = {searchkey: '', page: ''};
        // 获取数据
        $scope.getData = function () {
            $http.get('admin/copyWriting', {params: $scope.params}).success(function (data) {
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
            $scope.params.searchkey = $scope.searchKey;
            $scope.params.page = 1;
            $scope.getData();
        };
        // 删除
        $scope.delete = function (a) {
            if (confirm('确定要删除【' + a.title + '】吗？')) {
                $http.delete('admin/copyWriting/' + a.id).success(function () {
                    for (var i = 0; i < $scope.data.content.length; i++) {
                        if (a.id === $scope.data.content[i].id) {
                            $scope.data.content.splice(i, 1);
                            break;
                        }
                    }
                    alert("删除成功！");
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
            if ($scope.ids.length > 0 && confirm('确定要删除所选教程吗？')) {
                $http.post("admin/copyWriting/deleteSome", $scope.ids)
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
    }]).controller('copyWritingDetailCtr', ['$scope', '$http', '$state', '$stateParams', function ($scope, $http, $state, $stateParams) {

        var editor = UM.getEditor('editor', {
            imageUrl: 'admin/ume/images',
            imagePath: '/files/',
            lang: 'zh-cn',
            langPath: UMEDITOR_CONFIG.UMEDITOR_HOME_URL + "lang/",
            focus: true,
            zIndex: 1600
        });

        $scope.userId = $stateParams.id;

        if ($stateParams.id && $stateParams.id > 0) {
            $http.get('admin/copyWriting/' + $stateParams.id).success(function (data) {
                $scope.data = data;
                editor.ready(function () {
                    editor.setContent(data.content);
                });
            });
        }
        $scope.$on("$destroy", function () {
            UM.delEditor('editor');
        });

        $scope.save = function () {
            $scope.data.content = editor.getContent();
            if ($scope.data && $scope.data.id) {
                delete $scope.data.file;
                $http.put('admin/copyWriting/update', $scope.data)
                    .success(function (data) {
                        $scope.data = data;
                        alert('修改完成！');
                    }).error(function (r) {
                    console.log(r);
                    alert(r.message || '添加失败！');
                });
            } else if ($scope.data && $scope.data.title) {
                $http.post('admin/copyWriting/create', $scope.data)
                    .success(function (data) {
                        $scope.data = data;
                        alert('添加完成！请联系开发人员开发展示页面');
                    }).error(function (r) {
                    console.log(r);
                    alert(r.message || '添加失败！');
                });
            }
        };

        $scope.setStyle = function (id) {
            if (id != 0) return 'true';
        }
    }]);
});