define(['app', 'umeditor'], function (app, UM) {
    'use strict';

    app.filter(
        'trusthtml', ['$sce', function ($sce) {
            return function (text) {
                return $sce.trustAsHtml(text);
            }
        }]
    );

    app.controller('wxGzhEntryCtrl', ['$scope', '$http', '$sce', function ($scope, $http, $sce) {
        var editor = UM.getEditor('editor', {
            imageUrl: 'admin/ume/images',
            imagePath: '/files/',
            lang: 'zh-cn',
            langPath: UMEDITOR_CONFIG.UMEDITOR_HOME_URL + "lang/",
            focus: true,
            zIndex: 1600
        });

        editor.addListener('contentchange', function () {
            $scope.$apply(function () {
                $scope.pageContent = editor.getContent();
            });
        });

        var getPageContent = function () {
            $http.get('admin/setting?code=' + $scope.type).success(function (s) {
                if (!s || !s.id) {
                    s = {'name': $scope.type, 'lable': 'CNC微信公众平台接入页面', 'content': ''};
                }
                $scope.entity = s;
                $scope.pageContent = s.content;
                editor.setContent(s.content);
            });
        };

        editor.ready(function () {
            $scope.getPcPage();
        });

        // 获取PC版
        $scope.getPcPage = function () {
            if ($scope.type !== 'CNC_WXGZH_ENTRY_PAGE_CONTENT') {
                $scope.type = 'CNC_WXGZH_ENTRY_PAGE_CONTENT';
                getPageContent();
            }
        };

        // 获取手机版
        $scope.getMobilePage = function () {
            if ($scope.type !== 'CNC_WXGZH_ENTRY_MOBILE_PAGE_CONTENT') {
                $scope.type = 'CNC_WXGZH_ENTRY_MOBILE_PAGE_CONTENT';
                getPageContent();
            }
        };

        $scope.$on("$destroy", function () {
            UM.delEditor('editor');
        });

        $scope.save = function () {
            if (!$scope.entity) {
                $scope.entity = {'name': $scope.type, 'lable': 'CNC微信公众平台接入页面'};
            }
            $scope.entity.content = editor.getContent();
            $scope.entity.name = $scope.type;
            $http.post('admin/setting', $scope.entity).success(function (s) {
                alert('保存成功！');
            }).error(function (e) {
                console.log(e);
                alert('保存失败！');
            });
        };
    }]);
});