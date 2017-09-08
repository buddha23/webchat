define(['app', 'layer', 'directive/ng-pager', 'directive/ng-file'], function (app, layer) {
    'use strict';

    var userStates = [];
    userStates[-1] = '删除';
    userStates[1] = '正常';
    userStates[-2] = '废弃';

    app.controller('userMemberListCtrl', ['$scope', '$rootScope', '$http', function ($scope, $rootScope, $http) {

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
            $http.get('admin/user/members', {params: $scope.params}).success(function (data) {
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
                content: '牛人币：<input type = "text" class="add_score" onkeyup= "if(/\\D/.test(this.value)){alert(\'只能输入数字\');this.value=\'\';}" /> ',
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

        $scope.wxMsg = function (a) {
            if (confirm('确定给该用户发送会员到期通知吗？')) {
                $http.get('admin/user/memberEndMsg/' + a.id).success(function () {
                    layer.msg("通知成功");
                }).error(function (d) {
                    console.log(d);
                    layer.msg("删除失败！");
                });
            }
        };

        // 获取初始数据
        $scope.getData();

        $scope.getPage = function (page) {
            $scope.params.page = page;
            $scope.getData();
        };

    }]);
});