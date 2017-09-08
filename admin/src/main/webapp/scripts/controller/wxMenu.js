define(['app'], function (app) {
    'use strict';

    app.controller('wxMenuCtrl', function ($scope, $http) {
        $http.get('admin/wx/buttons').success(function (data) {
            $scope.buttons = data;
        }).error(function (req) {
            $scope.buttons = [];
            if(req['responseJSON']) {
                alert(req['responseJSON'].message);
            } else {
                alert('获取微信菜单失败！');
            }
        });

        $scope.currentBtn = null;

        $scope.confirmBtn = function () {
            if ($scope.currentBtn.isNew) {
                var result = angular.copy($scope.currentBtn);
                delete result.isNew;
                delete result.parent;
                var parent = $scope.buttons;
                if ($scope.currentBtn.parent) {
                    if (!$scope.currentBtn.parent.sub_button) {
                        $scope.currentBtn.parent.sub_button = [];
                    }
                    parent = $scope.currentBtn.parent.sub_button;
                }
                parent.push(result);
            } else if ($scope.currentBtn.type) {
                $scope.currentBtn.sub_button = [];
            }
            delete $scope.currentBtn.parent;
            $scope.currentBtn = null;
            console.log($scope.buttons);
        };

        $scope.newBtn = function (btn) {
            $scope.currentBtn = {type: 'view', parent: btn, isNew: true};
        };

        $scope.editBtn = function (btn, parent) {
            $scope.currentBtn = btn;
            $scope.currentBtn.parent = parent;
        };

        $scope.delBtn = function ($event, btn1, btn2) {
            if (btn2 !== undefined) {
                for (var i = 0; i < btn1.sub_button.length; i++) {
                    if (btn1.sub_button[i] === btn2) {
                        btn1.sub_button.splice(i, 1);
                        break;
                    }
                }
            } else {
                for (var j = 0; j < $scope.buttons.length; j++) {
                    if ($scope.buttons[j] === btn1) {
                        $scope.buttons.splice(j, 1);
                        break;
                    }
                }
            }
            $scope.currentBtn = null;
            $event.stopPropagation();
        };

        $scope.saveMenu = function () {
            $scope.saving = '保存中……';
            $http.post('admin/wx/buttons', $scope.buttons).success(function (data) {
                if (data.errcode === 0) {
                    alert('微信公众号菜单设置成功');
                } else {
                    alert('errcode: ' + data.errcode + ', errmsg: ' + data.errmsg);
                }
                console.log(data);
                $scope.saving = null;
            }).error(function () {
                alert('保存失败');
                $scope.saving = null;
            });
        };
    });
});