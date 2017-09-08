define(['app', 'layer', 'directive/ng-pager', 'directive/ng-file'], function (app, layer) {
    'use strict';

    var userStates = [];
    userStates[-1] = '删除';
    userStates[1] = '正常';
    userStates[-2] = '废弃';

    app.controller('userListCtrl', ['$scope', '$rootScope', '$http', function ($scope, $rootScope, $http) {
        if (!$rootScope.userParams) {
            $rootScope.userParams = {};
        }
        $scope.states = userStates;
        $scope.searchKey = $rootScope.userParams.key || '';
        $scope.params = {
            key: $rootScope.userParams.key || null,
            sortBy: $rootScope.userParams.sortBy || null,
            sortType: $rootScope.userParams.sortType || null,
            page: $rootScope.userParams.page || 1,
            size: 20
        };
        // 获取数据
        $scope.getData = function () {
            $http.get('admin/user', {params: $scope.params}).success(function (data) {
                $scope.data = data;
                $scope.params.page = data.number + 1;
                $rootScope.userParams.page = $scope.params.page;
                $rootScope.userParams.sortBy = $scope.params.sortBy;
                $rootScope.userParams.sortType = $scope.params.sortType;
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
            $rootScope.userParams.key = $scope.searchKey;
            $scope.params.page = 1;
            $scope.getData();
        };
        // 删除
        $scope.delete = function (a) {
            if (confirm('确定要删除【' + a.name + '】吗？')) {
                $http.delete('admin/user/' + a.id).success(function () {
                    a.state = -1;
                }).error(function (d) {
                    console.log(d);
                    alert("删除失败！");
                });
            }
        };


        $scope.addScore = function (userId) {
            layer.open({
                type: 1,
                title: '增加牛人币',
                area: '400px',
                btn: ['确认', '取消'],
                content: '<label class="am-margin-left am-margin-top">牛人币：</label><input type = "text" class="add_score" onkeyup= "if(/\\D/.test(this.value)){alert(\'只能输入数字\');this.value=\'\';}" /> ',
                yes: function (index) {
                    var score = $(".add_score").val();
                    $http.put("admin/user/" + userId + "/" + score)
                        .success(function () {
                            layer.close(index);
                            $scope.getData();
                        });
                }
            });
        };

        //批量删除
        $scope.ids = [];
        $scope.deleteSome = function () {
            if ($scope.ids.length > 0 && confirm('确定要删除所选记录？')) {
                $http.post("admin/user/deleteSome", $scope.ids)
                    .success(function () {
                        $scope.getData();
                        $scope.ids.splice(0, $scope.ids.length);
                        alert('操作完成！');
                    }).error(function (d) {
                        console.log(d);
                        alert('操作失败！');
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

        var sortTypes = ['', 'desc', 'asc'];
        var scoreCounts = 0;

        $scope.sortBy = function (column, e) {
            $scope.params.sortBy = column;
            if (column == "total_score") {
                scoreCounts++;
                $scope.params.sortType = sortTypes[scoreCounts % 3];
            }
            $scope.getData();
        };

        // 获取初始数据
        $scope.getData();

        $scope.getPage = function (page) {
            $scope.params.page = page;
            $scope.getData();
        };
    }]).controller('userDetailCtrl', ['$scope', '$http', '$state', '$stateParams', function ($scope, $http, $state, $stateParams) {
        if ($stateParams.id && $stateParams.id > 0) {
            $http.get('admin/user/' + $stateParams.id).success(function (data) {
                $scope.data = data;
                if (data.userInvite != null)
                    $scope.isParter = data.userInvite.isParter;
            });
        }

        $scope.states = userStates;

        $scope.fileChanged = function (file) {
            if (!$scope.data.id || !file) {
                return;
            }
            var form = new FormData();
            form.append('file', file);
            $http({
                url: 'admin/user/' + $scope.data.id + '/avatar',
                method: 'POST',
                data: form,
                headers: {
                    'Content-Type': undefined
                }
            }).success(function (data) {
                $scope.data.avatar = data.avatar + '?t=' + new Date().getTime();
                //alert('上传头像成功');
            }).error(function () {
                $scope.data.file = null;
                alert('上传头像失败');
            });
        };

        $scope.save = function () {
            if ($scope.data && $scope.data.id) {
                $http.put('admin/user/' + $scope.data.id, $scope.data)
                    .success(function (data) {
                        alert('修改完成！');
                    }).error(function () {
                    alert('修改失败！');
                });
            }
        };

        $scope.mark = function () {
            if ($scope.data && $scope.data.id && confirm("标记该用户为区域合伙人？")) {
                $http.put('admin/user/parter/' + $scope.data.id).success(function () {
                    $scope.isParter = true;
                    alert("操作成功!");
                }).error(function (r) {
                    //console.log(r);
                    alert('修改失败！');
                });
            }
        };

        $scope.createLecturer = function () {
            if ($scope.data && $scope.data.id && confirm("标记该用户为课程讲师？")) {
                $http.put('admin/user/lecturer/' + $scope.data.id).success(function () {
                    $scope.data.isLecturer = true;
                    alert("操作成功!");
                }).error(function (r) {
                    //console.log(r);
                    alert('修改失败！');
                });
            }
        };

        $scope.deleteLecturer = function () {
            if ($scope.data && $scope.data.id && confirm("取消该用户课程讲师身份(取消后讲师为该角色的视频集将变为无讲师状态)？")) {
                $http.delete('admin/user/lecturer/' + $scope.data.id).success(function () {
                    $scope.data.isLecturer = false;
                    alert("操作成功!");
                }).error(function (r) {
                    //console.log(r);
                    alert('修改失败！');
                });
            }
        };

    }]).controller('userScoresListCtrl', ['$scope', '$http', '$state', '$stateParams', function ($scope, $http, $state, $stateParams) {
        $scope.params = {
            page: 1,
            size: 20
        };

        $scope.types = '未知类型,上传加分,下载扣分,文档售出,注册赠送,第三方绑定,充值购买,充值赠送,文档删除,每日签到,上传软件,下载软件,软件售出,软件删除,版主月贡献值兑换,管理员增加'.split(',');
        $scope.types[16] = "转账提现";
        $scope.types[17] = "发布问题悬赏";
        $scope.types[18] = "获得悬赏";
        $scope.types[19] = "悬赏返还";
        $scope.types[20] = "受邀用户消费返利";

        if ($stateParams.id && $stateParams.id > 0) {
            $scope.userId = $stateParams.id;
        }

        $scope.getData = function () {
            if ($scope.userId) {
                $http.get('admin/user/' + $stateParams.id + '/scores', {params: $scope.params}).success(function (data) {
                    $scope.data = data;
                    $scope.params.page = data.number + 1;
                });
            }
        };


        // 获取初始数据
        $scope.getData();

        $scope.getPage = function (page) {
            $scope.params.page = page;
            $scope.getData();
        };
    }]).controller('userPointHistoryCtrl', ['$scope', '$http', '$state', '$stateParams', function ($scope, $http, $state, $stateParams) {
        // TODO:积分历史
        $scope.params = {
            key: null,
            sortBy: null,
            sortType: null,
            page: 1,
            size: 20
        };

        $scope.types = [];
        $scope.types[1] = '上传文档加分';
        $scope.types[9] = '签到';
        $scope.types[10] = '上传软件加分';
        $scope.types[31] = '充值赠送积分';
        $scope.types[32] = '下载文档加分';
        $scope.types[33] = '下载软件加分';
        $scope.types[34] = '购买视频加分';
        $scope.types[35] = '发布问题加分';
        $scope.types[36] = '发布评论加分';
        $scope.types[37] = '问题悬赏扣分';
        $scope.types[38] = '回答被采纳加分';
        $scope.types[39] = '悬赏返还';

        if ($stateParams.id && $stateParams.id > 0) {
            $scope.userId = $stateParams.id;
        } else {
            $stateParams.id = 0;
        }

        $scope.getData = function () {
            $http.get('admin/user/point/' + $stateParams.id, {params: $scope.params}).success(function (data) {
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
            $scope.params.key = $scope.searchKey;
            $scope.params.page = 1;
            $scope.getData();
        };

        var sortTypes = ['', 'desc', 'asc'];
        var scoreCounts = 0;

        $scope.sortBy = function (column, e) {
            $scope.params.sortBy = column;
            if (column == "point_change") {
                scoreCounts++;
                $scope.params.sortType = sortTypes[scoreCounts % 3];
            }
            $scope.getData();
        };

        // 获取初始数据
        $scope.getData();

        $scope.getPage = function (page) {
            $scope.params.page = page;
            $scope.getData();
        };
    }]);
});