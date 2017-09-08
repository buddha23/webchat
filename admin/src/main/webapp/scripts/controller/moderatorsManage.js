define(['app', 'directive/ng-pager', 'directive/ng-file', 'filter/filesize'], function (app) {
    'use strict';

    app.controller('moderatorsCtrl', ['$scope', '$rootScope', '$http', function ($scope, $rootScope, $http) {

        $scope.params = {
            key: null,
            page: 1,
            size: 20
        };

        // 获取数据
        $scope.getData = function () {
            $http.get('admin/docCategory/moderatorsList', {params: $scope.params}).success(function (data) {
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
            $scope.params.key = $scope.searchKey;
            $scope.params.page = 1;
            $scope.getData();
        };

        $scope.getPage = function (page) {
            $scope.params.page = page;
            $scope.getData();
        };

        $scope.delModerator = function (moderator) {
            if (confirm('确定删除版主【' + moderator.nickname + '】吗？')) {
                $.ajax({
                    type: 'DELETE',
                    url: 'admin/docCategory/' + moderator.categoryId + '/moderators/' + moderator.userId,
                    success: function (result) {
                        $scope.data.content.removeChild(moderator);
                        $scope.$apply($scope.data);
                    },
                    error: function (r) {
                        alert('删除失败！');
                    }
                });
            }
        };

        $scope.getData();

    }]).controller('moderatorManageCtrl', ['$scope', '$rootScope', '$http', function ($scope, $rootScope, $http) {
        if (!$rootScope.docParams) {
            $rootScope.docParams = {};
        }
        $scope.params = {
            key: $rootScope.docParams.key || null,
            page: $rootScope.docParams.page || 1,
            size: 20
        };
        // 获取数据
        $scope.getData = function () {
            $http.get('admin/docCategory/moderatorsApplyList', {params: $scope.params}).success(function (data) {
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
            $scope.params.key = $scope.searchKey;
            $rootScope.docParams.key = $scope.searchKey;
            $scope.params.page = 1;
            $scope.getData();
        };

        // 获取初始数据
        $scope.getData();

        $scope.getPage = function (page) {
            $scope.params.page = page;
            $scope.getData();
        };

        //同意
        $scope.agree = function (a) {
            if (confirm('确认同意该申请？')) {
                //console.log(a);
                $http.post('admin/docCategory/moderatorsAgree', a).success(function () {
                    //去除列表删除的记录
                    var idx = $scope.data.content.indexOf(a);
                    $scope.data.content.splice(idx, 1);
                    alert("修改成功");
                }).error(function (data) {
                    console.log(data);
                    alert("修改失败");
                });
            }
        };

        //不同意并删除该条记录
        $scope.disagree = function (a) {
            if (confirm('确定删除用户【' + a.nickname + '】的版主申请吗？')) {
                $.ajax({
                    type: 'DELETE',
                    url: 'admin/docCategory/' + a.categoryId + '/moderators/' + a.userId,
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

        //批量删除
        $scope.modS = [];
        $scope.deleteSome = function () {
            console.log($scope.modS);
            if ($scope.modS.length > 0 && confirm('确定要删除所选内容吗？')) {
                $http.post("admin/docCategory/deleteSome", $scope.modS)
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

        $scope.chose = function ($event, m) {
            var checkbox = $event.target;
            var action = (checkbox.checked ? 'add' : 'remove');
            var idx = $scope.modS.indexOf(m);
            if (action == 'add' && idx == -1) {
                $scope.modS.push(m);
            }
            if (action == 'remove' && idx != -1) {
                $scope.modS.splice(idx, 1);
            }
        };

        $scope.choseAll = function ($event) {
            var checkbox = $event.target;
            var action = (checkbox.checked ? 'add' : 'remove');
            if (action == 'add') {
                for (var i = 0; i < $scope.data.content.length; i++) {
                    if ($scope.modS.indexOf($scope.data.content[i]) == -1)
                        $scope.modS.push($scope.data.content[i]);
                }
            } else {
                $scope.modS.splice(0, $scope.modS.length);
            }
        };
    }]).controller('moderatorsLogCtrl', ['$scope', '$http', '$stateParams', function ($scope, $http, $stateParams) {
        $scope.typetotext = [0, '审核', '移动版块', '删除'];

        $scope.params = {
            key: null,
            page: 1,
            size: 20
        };

        if ($stateParams.id && $stateParams.id > 0) {
            $scope.params.userId = $stateParams.id;
        }

        $scope.getData = function () {
            $http.get('admin/docCategory/moderatorsLog', {params: $scope.params}).success(function (data) {
                $scope.data = data;
                $scope.params.page = data.number + 1;
            })
        };

        // 搜索
        $scope.doSearch = function () {
            $scope.params.key = $scope.searchKey;
            $scope.params.page = 1;
            $scope.getData();
        };

        $scope.onSearchKeyPress = function (e) {
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                $scope.doSearch();
            }
        };

        // 删除
        $scope.delete = function (a) {
            if (confirm('确定要删除该条记录？')) {
                $http.delete('admin/docCategory/moderatorsLogDelete/' + a.id).success(function () {
                    alert('删除成功');
                    $scope.data.content.removeChild(a);
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
            if ($scope.ids.length > 0 && confirm('确定要删除所选记录？')) {
                $http.post("admin/docCategory/moderatorsLogsDelete", $scope.ids)
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
    }])
});