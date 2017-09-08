define(['app', 'layer', 'directive/ng-pager'], function (app, layer) {
    'use strict';

    app.filter('trusthtml', ['$sce', function ($sce) {
        return function (text) {
            return $sce.trustAsHtml(text);
        }
    }]);

    app.controller('postsListCtrl', ['$scope', '$rootScope', '$http', function ($scope, $rootScope, $http) {

        $scope.states = [];
        $scope.states[0] = '待推荐';
        $scope.states[1] = '待支付';
        $scope.states[2] = '已支付';
        $scope.states[3] = '已返还';
        $scope.states[4] = '已推荐';

        $scope.setStates = function (time) {
            return time < new Date().getTime();
        };

        if (!$rootScope.docParams) {
            $rootScope.docParams = {};
        }
        $scope.searchKey = $rootScope.docParams.key || '';
        $scope.params = {
            key: $rootScope.docParams.key || null,
            page: $rootScope.docParams.page || 1,
            size: 20
        };

        // 获取数据
        $scope.getData = function () {
            $http.get('admin/posts', {params: $scope.params}).success(function (data) {
                $scope.data = data;
                $scope.params.page = data.number + 1;
                $rootScope.docParams.page = $scope.params.page;
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
            $rootScope.docParams.key = $scope.searchKey;
            $scope.params.page = 1;
            $scope.getData();
        };
        // 删除
        $scope.delete = function (a) {
            if (confirm('确定要删除【' + a.title + '】吗？')) {
                $http.delete('admin/posts/' + a.id).success(function () {
                    for (var i = 0; i < $scope.data.content.length; i++) {
                        if (a.id === $scope.data.content[i].id) {
                            $scope.data.content.splice(i, 1);
                            break;
                        }
                    }
                }).error(function (d) {
                    console.log(d);
                    alert("删除失败！");
                });
            }
        };

        // 批量删除
        $scope.ids = [];
        $scope.deleteSome = function () {
            if (confirm('确定要删除所选内容吗？')) {
                $http.post("admin/posts/deleteSome", $scope.ids)
                    .success(function () {
                        $scope.getData();
                        $scope.ids.splice(0, $scope.ids.length);
                        alert('删除完成！');
                    }).error(function (d) {
                        alert('删除失败！');
                    }
                )
            }
        };

        $scope.chose = function ($event, m) {
            var checkbox = $event.target;
            var action = (checkbox.checked ? 'add' : 'remove');
            var idx = $scope.ids.indexOf(m);
            if (action == 'add' && idx == -1) {
                $scope.ids.push(m);
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
                    if ($scope.ids.indexOf($scope.data.content[i]) == -1)
                        $scope.ids.push($scope.data.content[i]);
                }
            } else {
                $scope.ids.splice(0, $scope.ids.length);
            }
        };

        var pageContent;
        $scope.getCategories = function () {
            $http.get('admin/posts/categories').success(function (data) {
                pageContent = '<div class="am-margin-left am-selected-content am-margin-right"><label>选择分类:</label><select id="choseCg" class="am-selected-content am-margin-left">';
                $scope.categories = data;
                if (data.length > 0) {
                    data.forEach(function (c) {
                        pageContent = pageContent + '<option value="' + c.id + '">' + c.name + '</option>';
                    });
                }
                pageContent = pageContent + '</select></div>';
            });
        };

        $scope.changeCate = function (p) {
            if (p && p.id) {
                var pagei = layer.open({
                    type: 1,   //0-4的选择,
                    title: '选择分类',
                    border: [0],
                    shadeClose: true,
                    area: ['auto', 'auto'],
                    content: pageContent,
                    btn: ['确认', '取消'],
                    yes: function () {
                        var choseCg = $('#choseCg').val();
                        //console.log(choseCg);
                        //console.log(parseInt(choseCg));
                        if (choseCg != null) {
                            $http.post('admin/posts/changeCategory/' + p.id, parseInt(choseCg))
                                .success(function () {
                                    $scope.getData();
                                    layer.close(pagei);
                                    layer.msg('操作成功');
                                }).error(function () {
                                layer.msg('操作失败');
                            });
                        }
                    },
                    btn2: function () {
                        layer.close(pagei);
                    }
                });
            }
        };


        // 获取初始数据
        $scope.getData();
        $scope.getCategories();

        $scope.getPage = function (page) {
            $scope.params.page = page;
            $scope.getData();
        };
    }]).controller('postsDetailCtrl', ['$scope', '$http', '$state', '$stateParams', function ($scope, $http, $state, $stateParams) {

        $scope.rewardType = [];
        $scope.rewardType[1] = '牛人币';
        $scope.rewardType[2] = '积分';

        $scope.rewardState = [];
        $scope.rewardState[1] = '待支付';
        $scope.rewardState[2] = '已支付';
        $scope.rewardState[3] = '已返还';
        $scope.rewardState[4] = '已推荐';
        $scope.rewardState[5] = '已推荐';

        $scope.needChose = false;

        if ($stateParams.id && $stateParams.id > 0) {
            $http.get('admin/posts/' + $stateParams.id).success(function (data) {
                $scope.data = data;
                if (data.postsReward && data.postsReward.state == 1 && data.postsReward.finishTime < new Date().getTime())
                    $scope.needChose = true;
                if (data.postsReward && data.postsReward.commentId) {
                    $http.get('admin/posts/comment/' + data.postsReward.commentId).success(function (data) {
                        $scope.bestComment = data;
                    });
                }
            });

            $scope.params = {
                page: 1,
                size: 20
            };

            $scope.getComments = function () {
                $http.get('admin/posts/' + $stateParams.id + '/comments', {params: $scope.params}).success(function (data) {
                    $scope.comments = data;
                    $scope.params.page = data.number + 1;
                    $('.admin-content').scrollTop(220);
                });
            };

            $scope.getCommentsPage = function (page) {
                if ($scope.params.page !== page) {
                    $scope.params.page = page;
                    $scope.getComments();
                }
            };

            $scope.getComments();
        }

        $scope.deleteComment = function (c) {
            if (confirm('确定要删除【' + c.userNickname + '】的这条吗？')) {
                $http.delete('admin/posts/comments/' + c.id).success(function () {
                    var idx = $scope.comments.content.indexOf(c);
                    $scope.comments.content.splice(idx, 1);
                }).error(function (d) {
                    console.log(d);
                    alert("删除失败！");
                });
            }
        };

        $scope.delete = function () {
            if (confirm('确定要删除【' + $scope.data.title + '】吗？')) {
                $http.delete('admin/posts/' + $scope.data.id).success(function () {
                    history.back();
                }).error(function (d) {
                    console.log(d);
                    alert("删除失败！");
                });
            }
        };

        $scope.choseBestCom = function (c) {
            if ($scope.data && confirm('确定选择【' + c.userNickname + '】的这条评论作为最佳答案吗？')) {
                $http.post('admin/posts/choseBestComment?postsId=' + $scope.data.id + '&commentId=' + c.id).success(function () {
                    alert('操作成功！');
                    location.reload();
                }).error(function (r) {
                    console.log(r);
                    alert('操作失败！');
                });
            }
        };

        $scope.returnReward = function () {
            if ($scope.data && confirm('确定返还【' + $scope.data.title + '】的悬赏？')) {
                $http.get('admin/posts/returnReward/' + $scope.data.id).success(function () {
                    alert('操作成功！');
                }).error(function () {
                    console.log(r);
                    alert('操作失败！');
                });
            }
        }

    }]);
});