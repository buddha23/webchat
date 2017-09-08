define(['app', 'layer', 'directive/ng-pager', 'directive/ng-file', 'filter/filesize'], function (app, layer) {
    'use strict';
    app.controller('userWithdrawCtrl', ['$scope', '$rootScope', '$http', function ($scope, $rootScope, $http) {

        if (!$rootScope.docParams) {
            $rootScope.docParams = {};
        }

        $scope.beginTime = $rootScope.docParams.beginTime || '';
        $scope.endTime = $rootScope.docParams.endTime || '';

        $scope.params = {
            userId: $rootScope.docParams.userId || null,
            searchkey: $rootScope.docParams.searchkey || '',
            tradeStatus: $rootScope.docParams.tradeStatus || '',
            beginTime: $rootScope.docParams.beginTime || null,
            endTime: $rootScope.docParams.endTime || null,
            page: $rootScope.docParams.page || 1,
            size: 20
        };
        // 获取数据
        $scope.getData = function () {
            $http.get('admin/withdraw/getWithdrawList', {params: $scope.params}).success(function (data) {
                $scope.data = data;
                $scope.params.page = data.number + 1;
                $rootScope.docParams.page = $scope.params.page;
            })
        };

        $scope.doSearch = function () {
            delete $scope.data;
            $scope.params.searchkey = $scope.searchKey;
            $scope.params.beginTime = $scope.beginTime;
            $scope.params.endTime = $scope.endTime;
            $scope.params.tradeStatus = $scope.tradeStatus;

            $rootScope.docParams.beginTime = $scope.beginTime;
            $rootScope.docParams.endTime = $scope.endTime;
            $rootScope.docParams.searchkey = $scope.searchKey;
            $rootScope.docParams.tradeStatus = $scope.tradeStatus;
            $scope.params.page = 1;

            //console.log($scope.params);
            $scope.getData();
        };

        $scope.onSearchKeyPress = function (e) {
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                $scope.doSearch();
            }
        };

        // 获取初始数据
        $scope.getData();

        $scope.getPage = function (page) {
            $scope.params.page = page;
            $scope.getData();
        };
        $scope.reject = function (a) {
            layer.open({
                type: 1,
                title: '审核失败',
                shadeClose: true,
                area: '400px',
                btn: ['确认', '取消'],
                content: '<div style="padding: 10px 20px;"><font style="margin-left: 15px;">失败原因：</font> <textarea class="form-control" style="width: 200px; height: 100px; font-size: 13px; resize: none;margin-left: 20px;" type = "text" id="remark"></textarea> </div>',
                yes: function (index) {
                    var remark = $("#remark").val();
                    $http.post("admin/withdraw/" + a.id + "/reject?remark=" + remark)
                        .success(function () {
                            location.reload();
                            layer.close(index);
                        })
                }
            });
        };
        $scope.doSuccess = function (a) {
            if (confirm('审核通过后，钱会自动打给对方的账号，确认通过？')) {
                $http.get('admin/withdraw/doSuccess/' + a.id).success(function () {
                    location.reload();
                }).error(function (r) {
                    console.log(r);
                    layer.msg(r.message);
                });
            }
        }
    }]);
});