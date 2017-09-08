define(['app', 'layer', 'directive/ng-pager', 'directive/ng-file'], function (app, layer) {
    'use strict';

    app.controller('roleListCtrl', ['$scope', '$rootScope', '$http', function ($scope, $rootScope, $http) {

        $scope.getData = function () {
            $http.get('admin/admin/roles').success(function (data) {
                $scope.data = data;
            })
        };
        $scope.allpermission = [];

        $scope.getAllPermission = function () {
            $http.get('admin/admin/permission').success(function (data) {
                $scope.allpermission = data;
            });
        };

        $scope.getData();
        $scope.getAllPermission();

        //TODO:页面报错
        $scope.showPermission = function (role) {
            var perStr = '';
            if (role.permissions && role.permissions.length > 0) {
                var per = role.permissions.split(',');
                per.forEach(function (p) {
                    for (var i = 0; i < $scope.allpermission.length; i++) {
                        if (p == $scope.allpermission[i].permit) perStr = perStr + ' ' + $scope.allpermission[i].name;
                    }
                });
            }
            return perStr;
        };

        $scope.delete = function (role) {
            if (confirm('确定要删除【' + role.name + '】吗？')) {
                $http.delete('admin/admin/role/' + role.id).success(function () {
                    $scope.data.removeChild(role);
                }).error(function (d) {
                    console.log(d);
                    alert("删除失败！");
                });
            }
        };

    }]).controller('roleDetailCtrl', ['$scope', '$http', '$state', '$stateParams', function ($scope, $http, $state, $stateParams) {

        if ($stateParams.id && $stateParams.id > 0) {
            $http.get('admin/admin/role/' + $stateParams.id).success(function (data) {
                $scope.data = data;
                if (data.permissions != null) {
                    console.log(data.permissions);
                    $http.get('admin/admin/getPermissionsName?permissions=' + data.permissions).success(function (name) {
                        $scope.permissionsName = name;
                    });
                }
            });
        }

    }]).controller('roleUpdateCtrl', ['$scope', '$http', '$state', '$stateParams', function ($scope, $http, $state, $stateParams) {

        $scope.userId = $stateParams.id;

        $scope.setStyle = function (id) {
            if (id != 0) return 'true';
        };

        //var permStrs = [];
        var pppp = '';

        if ($stateParams.id && $stateParams.id > 0) {
            $http.get('admin/admin/role/' + $stateParams.id).success(function (data) {
                $scope.data = data;
                if (data.permissions && data.permissions.length > 0) {
                    pppp = data.permissions;
                    //var permStrs = data.permissions.split(',');
                }
            });
        }

        $http.get('admin/admin/permission').success(function (data) {
            $scope.permissionList = data;
        });

        $scope.checkClass = function (str) {
            if (pppp.indexOf(str) != -1)
                return 'am-btn am-btn-default am-active';
            else
                return 'am-btn am-btn-default';
        };

        $scope.isSelected = function (str) {
            return pppp.indexOf(str) != -1;
        };

        $scope.save = function () {
            var permStr = '';
            var $cb = $('[name="role-perm"]');
            var checked = [];
            $cb.filter(':checked').each(function () {
                checked.push(this.value);
            });
            console.log(checked);
            if (checked.length > 0) {
                permStr = checked[0];
                for (var i = 1; i < checked.length; i++) permStr = permStr + ',' + checked[i];
                $scope.data.permissions = permStr;
                console.log(permStr);
                if (!$scope.data.id) $scope.data.id = 0;
                $http.put('admin/admin/role/' + $scope.data.id, $scope.data)
                    .success(function (data) {
                        alert('修改完成！');
                        location.href = '#/roleList';
                    }).error(function () {
                    alert('修改失败！');
                });
            } else {
                alert('必须勾选权限!');
                return false;
            }
        };

    }]).controller('permissionCtrl', ['$scope', '$http', '$state', '$stateParams', function ($scope, $http) {

        $http.get('admin/admin/permission').success(function (data) {
            $scope.data = data;
        });

    }]).controller('operateRecordCtrl', ['$scope', '$rootScope', '$http', '$state', '$stateParams', function ($scope, $rootScope, $http) {
        if (!$rootScope.userParams) {
            $rootScope.userParams = {};
        }
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
            $http.get('admin/admin/operateLog', {params: $scope.params}).success(function (data) {
                $scope.data = data;
                $scope.params.page = data.number + 1;
            });
        };
        $scope.getPage = function (page) {
            $scope.params.page = page;
            $scope.getData();
        };
        $scope.showArgs = function (args) {
            layer.alert(args);
        };
        $scope.getData();
    }]);
});