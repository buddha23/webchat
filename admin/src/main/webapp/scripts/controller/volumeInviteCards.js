define(['app', 'layer', 'directive/ng-pager', 'directive/ng-file'], function (app, layer) {
    'use strict';

    var inviteState = [];
    inviteState[1] = '未使用';
    inviteState[2] = '已使用';
    inviteState[3] = '已删除';

    app.controller('vodInviteCardsCtrl', ['$scope', '$rootScope', '$http', function ($scope, $rootScope, $http) {

        $scope.typeName = [];
        $scope.typeName[1] = '视频集';
        $scope.typeName[2] = '文档';
        $scope.typeName[3] = '软件';
        $scope.typeName[4] = '会员';

        $scope.states = inviteState;
        $scope.ids = [];
        $scope.searchKey = '';
        $scope.params = {
            key: '',
            state: 0,
            type: 0,
            page: 1,
            size: 15
        };

        // 获取数据
        $scope.getData = function () {
            $http.get('admin/inviteCard/volume', {params: $scope.params}).success(function (data) {
                $scope.data = data;
                $scope.params.page = data.number + 1;
            }).error(function (data) {
                console.log(data);
                layer.msg('查询数据失败!');
            });
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
            $scope.params.page = 1;
            $scope.getData();
        };
        // 删除
        $scope.delete = function (a) {
            if (confirm('确定要删除该条邀请卡/记录吗？')) {
                $http.delete('admin/inviteCard/' + a.id).success(function () {
                    layer.msg("删除成功！");
                    a.state = 3;
                }).error(function (d) {
                    console.log(d);
                    layer.msg("删除失败！");
                });
            }
        };

        var prm = {
            type: 0,
            objectId: 0,
            cardNum: 0
        };

        function checkPrm() {
            if ((/(^[1-9]\d*$)/.test(prm.cardNum)) && (/(^[1-9]\d*$)/.test(prm.cardNum))) {
                return true;
            } else {
                layer.msg('ID和生成张数都必须为正整数');
                return false;
            }
        }

        $scope.createCard = function () {
            layer.open({
                type: 1,
                title: '创建邀请卡',
                area: '400px',
                btn: ['确认', '取消'],
                content: $('#createDiv'),
                yes: function (index) {
                    prm.type = $('#cardType').val();
                    prm.objectId = $('#cardObjectId').val();
                    prm.cardNum = $('#cardNum').val();
                    console.log(prm);
                    if (checkPrm())
                        $.ajax({
                            url: 'admin/inviteCard/create',
                            type: 'post',
                            data: prm,
                            success: function () {
                                layer.close(index);
                                $scope.getData();
                                layer.msg('创建成功');
                            },
                            error: function () {
                                layer.msg('创建失败');
                            }

                        });

                }
            });
        };

        $('#checkObject').click(function () {
            var type = $('#cardType').val();
            var objectId = $('#cardObjectId').val();
            if (type == 1) {
                $http.get('admin/video/' + objectId).success(function (data) {
                    if (data != null && data.name != null) {
                        $('#objectMsg').html('《' + data.name + '》');
                    } else {
                        $('#objectMsg').html('无效的文件ID');
                    }
                }).error(function () {
                    $('#objectMsg').html('无效的文件ID');
                });

            } else if (type == 2) {
                $http.get('admin/doc/' + objectId).success(function (data) {
                    if (data != null && data.title != null)
                        $('#objectMsg').html('《' + data.title + '》');
                    else
                        $('#objectMsg').html('无效的文件ID');
                }).error(function () {
                    $('#objectMsg').html('无效的文件ID');
                });
            } else if (type == 3) {
                $http.get('admin/soft/' + objectId).success(function (data) {
                    if (data != null && data.title != null)
                        $('#objectMsg').html('《' + data.title + '》');
                    else
                        $('#objectMsg').html('无效的文件ID');
                }).error(function () {
                    $('#objectMsg').html('无效的文件ID');
                });
            }
        });

        //批量删除
        $scope.ids = [];
        $scope.deleteSome = function () {
            //console.log($scope.ids);
            if ($scope.ids.length > 0 && confirm('确定要删除所选教程吗？')) {
                $http.post("admin/inviteCard/deleteSome", $scope.ids)
                    .success(function (data) {
                        $scope.getData();
                        alert('删除完成！');
                    }).error(function (d) {
                        console.log(d);
                        alert('删除失败！');
                    }
                )
            }
        };

        $scope.chose = function ($event, id) {
            var checkbox = $event.target;
            var action = (checkbox.checked ? 'add' : 'remove');
            var idx = $scope.ids.indexOf(id);
            if (action == 'add' && idx == -1) {
                $scope.ids.push(id);
            }
            if (action == 'remove' && idx != -1) {
                $scope.ids.splice(idx, 1);
            }
        };

        $scope.choseAll = function ($event) {
            var checkbox = $event.target;
            var action = (checkbox.checked ? 'add' : 'remove');
            if (action == 'add') {
                for (var i = 0; i < $scope.data.content.length; i++) {
                    if ($scope.ids.indexOf($scope.data.content[i].id) == -1)
                        $scope.ids.push($scope.data.content[i].id);
                }
            } else {
                $scope.ids.splice(0, $scope.ids.length);
            }
        };

        $scope.getData();

        $scope.getPage = function (page) {
            $scope.params.page = page;
            $scope.getData();
        };

    }]);

});
