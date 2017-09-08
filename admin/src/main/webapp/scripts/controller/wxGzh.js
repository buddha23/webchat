define(['app', 'directive/ng-pager'], function (app) {
    'use strict';

    app.controller('wxGzhCtrl', function ($scope, $http) {
        $scope.serviceType = ['订阅号', '老帐号升级订阅号', '服务号'];
        $scope.verifyType = '微信认证,新浪微博认证,腾讯微博认证,资质认证通过但还未通过名称认证,已资质认证通过、还未通过名称认证，但通过了新浪微博认证,资质认证通过、还未通过名称认证，但通过了腾讯微博认证'.split(',');
        $scope.funcs = ',消息管理,用户管理,帐号服务,网页服务,微信小店,微信多客服,群发与通知,微信卡券,微信扫一扫,微信连WIFI,素材管理,微信摇周边,微信门店,微信支付,自定义菜单'.split(',');

        $http.get('admin/wx/gzh').success(function (data) {
            $scope.data = data;
        }).error(function (req) {
            $scope.data = [];
            alert(req['message'] || '获取微信公众号列表失败！');
        });
    });
});