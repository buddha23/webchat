define(['app', 'layer', 'directive/ng-pager', 'directive/ng-file'], function (app, layer) {
    'use strict';

    app.controller('adminListCtrl', ['$scope', '$rootScope', '$http', function ($scope, $rootScope, $http) {

        $scope.status = [];
        $scope.status[1] = '正常';
        $scope.status[2] = '冻结';

        $scope.getData = function () {
            $http.get('admin/admin/list').success(function (data) {
                $scope.data = data;
            })
        };

        $scope.delete = function (admin) {
            if (confirm('确定要删除【' + admin.name + '】吗？')) {
                $http.delete('admin/admin/' + admin.id).success(function () {
                    $scope.data.removeChild(admin);
                }).error(function (d) {
                    console.log(d);
                    alert("删除失败！");
                });
            }
        };

        $scope.unfreeze = function (admin) {
            if (confirm('确定解冻管理员【' + admin.name + '】吗？')) {
                $http.put('admin/admin/unfreeze/' + admin.id, admin)
                    .success(function (data) {
                        admin.status = 1;
                        alert('解冻成功！');
                    }).error(function () {
                    alert('解冻失败！');
                });
            }
        };

        $scope.freezeAdmin = function (admin) {
            if (confirm('确定冻结管理员【' + admin.name + '】吗？')) {
                $http.put('admin/admin/freeze/' + admin.id, admin)
                    .success(function (data) {
                        admin.status = 2;
                        alert('冻结成功！');
                    }).error(function () {
                    alert('冻结失败！');
                });
            }
        };

        $scope.getData();

    }]).controller('adminDetailCtrl', ['$scope', '$http', '$state', '$stateParams', function ($scope, $http, $state, $stateParams) {

        $scope.userId = $stateParams.id;

        if ($stateParams.id && $stateParams.id > 0) {
            $http.get('admin/admin/' + $stateParams.id).success(function (data) {
                $scope.data = data;
            });
        } else {
            $scope.data = {
                account: '',
                password: '',
                rePassword: '',
                name: '',
                adminRole: {
                    id: 0,
                    description: ''
                }
            }
        }

        $http.get('admin/admin/roles').success(function (data) {
            $scope.roles = data;
        });

        $scope.save = function () {
            if (!checkData($scope.data)) {
                return false;
            }
            if ($scope.data && $scope.data.id) {
                $http.put('admin/admin/' + $scope.data.id, $scope.data)
                    .success(function (data) {
                        alert('修改完成！');
                        location.href = '#/adminList';
                    }).error(function () {
                    alert('修改失败！');
                });
            } else if ($scope.data && $scope.userId == 0) {
                $http.post('admin/admin/create', $scope.data)
                    .success(function (data) {
                        alert('添加完成！');
                        location.href = '#/adminList';
                    }).error(function () {
                    alert('添加失败！');
                });
            }
        };

        $scope.setStyle = function (id) {
            if (id != 0) return 'true';
        };

        function checkData(admin) {
            if (admin.account == '' || admin.account == null || admin.account.length > 20) {
                layer.msg('账号信息错误');
                return false;
            }
            if (admin.id == 0 && !/^\w{6,20}$/.test(admin.password)) {
                layer.msg('密码输入错误');
                return false;
            }
            if (admin.name == '' || admin.name == null) {
                layer.msg('管理员名称不能为空');
                return false;
            }
            return true;
        }

    }]);
});