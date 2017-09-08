define(['app', 'directive/ng-pager', 'directive/ng-file'], function (app) {
    'use strict';

    app.controller('wxfwUserDownCtrl', function ($scope, $http) {
        $scope.doDownload = function () {
            $http.get('admin/wx/user/download').success(function (data) {
                $scope.result = data.message;
            }).error(function () {
                $scope.result = '执行失败！';
            });
        };

        $scope.getUserInfo = function () {
            $http.get('admin/wx/user/getUserInfo').success(function (data) {
                $scope.result = data.message;
            }).error(function () {
                $scope.result = '执行失败！';
            });
        }
    });
});