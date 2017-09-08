define(['app'], function (app) {
    'use strict';

    app.controller('settingCtrl', ['$scope', '$http', function ($scope, $http) {
        $http.get('admin/setting/settings').success(function (data) {
            $scope.data = data;
            if (data && data.length) {
                $scope.cg = data[0];
            }
        }).error(function (req) {
            $scope.buttons = [];
            if (req['responseJSON']) {
                alert(req['responseJSON'].message);
            } else {
                alert('获取设置菜单失败！');
            }
        });

        $scope.add = function () {
            delete $scope.c;
            delete $scope['cg'];
            $scope.t = {};
        };


        $scope.edit = function (set) {
            $scope.cg = set;
            $scope.t = $.extend({}, set);
        };

        $scope.save = function () {
            $http.post('admin/setting', $scope.t).success(function (data) {
                $.extend($scope.cg, data);
                $scope.t = data;
                alert('保存成功！');
            }).error(function (r) {
                alert('保存失败');
            });
        };

        $scope.delete = function (c) {
            if (c.name) {
                if (confirm('删除该配置会导致该功能无法正常使用，确认删除吗？')) {
                    $http.delete('admin/setting/' + c.name).success(function (data) {
                        $scope.data.removeChild($scope.cg);
                        delete $scope.t;
                        delete $scope.c;
                    }).error(function (r) {
                        console.log(r);
                        alert('删除失败！');
                    });
                }
            }
        };
    }
    ])
    ;
})
;