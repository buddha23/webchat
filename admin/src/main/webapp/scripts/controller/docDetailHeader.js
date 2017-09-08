define(['app', 'umeditor'], function (app, UM) {
    'use strict';

    app.filter(
        'trusthtml', ['$sce', function ($sce) {
            return function (text) {
                return $sce.trustAsHtml(text);
            }
        }]
    );

    app.controller('docDetailHeaderCtrl', ['$scope', '$http', '$sce', function ($scope, $http, $sce) {
        var editor = UM.getEditor('editor', {
            imageUrl: 'admin/ume/images',
            imagePath: '/files/',
            lang: 'zh-cn',
            langPath: UMEDITOR_CONFIG.UMEDITOR_HOME_URL + "lang/",
            focus: true,
            zIndex: 1600
        });
        editor.ready(function () {
            $http.get('admin/setting?code=CNC_WX_DOC_DETAIL_HEADER').success(function (s) {
                if (s && s.id) {
                    $scope.entity = s;
                    $scope.pageContent = s.content;
                    editor.setContent(s.content);
                }

                editor.addListener('contentchange', function () {
                    $scope.$apply(function () {
                        $scope.pageContent = editor.getContent();
                    });
                });
            });
        });

        $scope.$on("$destroy", function () {
            UM.delEditor('editor');
        });

        $scope.save = function () {
            if (!$scope.entity) {
                $scope.entity = {'name': 'CNC_WX_DOC_DETAIL_HEADER', 'lable': '微信文档详情顶部配置'};
            }
            $scope.entity.content = editor.getContent();
            $http.post('admin/setting', $scope.entity).success(function (s) {
                alert('保存成功！');
            }).error(function (e) {
                console.log(e);
                alert('保存失败！');
            });
        };
    }]);
});