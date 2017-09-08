define(['app', 'directive/ng-pager', 'directive/ng-file', 'filter/filesize'], function (app) {
    'use strict';

    var docStates = '错误,正常,处理中,处理失败,待审核,审核未通过'.split(',');
    var docStateColors = 'danger,success,secondary,warning,primary,danger'.split(',');

    app.controller('softListCtrl', ['$scope', '$rootScope', '$http', function ($scope, $rootScope, $http) {
        if (!$rootScope.docParams) {
            $rootScope.docParams = {};
        }
        $scope.ids = [];
        $scope.docStates = docStates;
        $scope.docStateColors = docStateColors;
        $scope.searchKey = $rootScope.docParams.key || '';
        $scope.params = {
            state: $rootScope.docParams.state || 0,
            key: $rootScope.docParams.key || null,
            sortBy: $rootScope.docParams.sortBy || null,
            sortType: $rootScope.docParams.sortType || null,
            page: $rootScope.docParams.page || 1,
            size: 20
        };

        // 获取数据
        $scope.getData = function () {
            $http.get('admin/soft', {params: $scope.params}).success(function (data) {
                $scope.data = data;
                $scope.params.page = data.number + 1;
                $rootScope.docParams.page = $scope.params.page;
                $rootScope.docParams.sortBy = $scope.params.sortBy;
                $rootScope.docParams.sortType = $scope.params.sortType;
                $rootScope.docParams.state = $scope.params.state;
            })
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
                $http.delete('admin/soft/' + a.uuId).success(function () {
                    for (var i = 0; i < $scope.data.content.length; i++) {
                        if (a.uuId === $scope.data.content[i].uuId) {
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

        $scope.reviewsChecked = function () {
            var ids = [];
            $('tbody :checkbox:checked').each(function () {
                ids.push($(this).val());
            });
            if (ids.length > 0 && confirm('确定要通过这【' + ids.length + '】文档吗？')) {
                $.ajax({
                    type: 'post',
                    url: 'admin/soft/review',
                    data: {ids: ids},
                    success: function (result) {
                        if (result.data && result.data.length > 0) {
                            var msg = '【以下文档执行失败】\n';
                            for (var i = 0; i < Math.min(result.data.length, 10); i++) {
                                msg += result.data[i] + '\n';
                            }
                            alert(msg);
                        }
                        $scope.getData();
                    },
                    error: function (e) {
                        console.log(e);
                        alert("处理失败！");
                    }
                });
            }
        };

        var sortTypes = ['', 'asc', 'desc'];
        var viewsCounts = 0;
        var downloadsCounts = 0;
        var createTimeCounts = 0;

        $scope.sortBy = function (column, e) {
            $scope.params.sortBy = column;
            if (column == "views") {
                viewsCounts++;
                $scope.params.sortType = sortTypes[viewsCounts % 3];
            } else if (column == "downloads") {
                downloadsCounts++;
                $scope.params.sortType = sortTypes[downloadsCounts % 3];
            } else if (column == 'createTime') {
                createTimeCounts++;
                $scope.params.sortType = sortTypes[createTimeCounts % 3];
            }
            $scope.getData();
        };

        // 获取初始数据
        $scope.getData();

        $scope.getPage = function (page) {
            $scope.params.page = page;
            $scope.getData();
        };
    }]).controller('softDetailCtrl', ['$scope', '$http', '$state', '$stateParams', function ($scope, $http, $state, $stateParams) {
        $http.get('i/soft_categories').success(function (docCategories) {
            $scope.categories = docCategories;
            $scope.vars = {};

            if ($stateParams.id && $stateParams.id > 0) {
                $http.get('admin/soft/' + $stateParams.id).success(function (data) {
                    if (data.tags)
                        $scope.vars.tags = data.tags.join(' ');
                    $scope.data = data;
                    setCategory(docCategories, data);
                });
            }
        });

        $scope.downloadIteam = function(data){
            $http.get('admin/soft/downloadIteam/' + data).success(function (data) {
                window.location.href = data["data"];
            });

        };

        // 设置category选项
        function setCategory(docCategories, data) {
            if (data.softCategory) {
                for (var i = 0; i < docCategories.length; i++) {
                    if (docCategories[i].id == data.softCategory.fid) {
                        $scope.categoryOne = docCategories[i];
                        for (var j = 0; j < docCategories[i].children.length; j++) {
                            if (docCategories[i].children[j].id == data.softCategory.id) {
                                $scope.categoryTwo = docCategories[i].children[j];
                            }
                        }
                    }
                }
            }
        }


        $scope.review = function (bool) {
            if (confirm('确定审核' + (bool ? '通过' : '不通过') + '吗？')) {
                $http.post('admin/soft/' + $scope.data.uuId + '/review?reviewOk=' + bool)
                    .success(function (result) {
                        $scope.data.state = result.data;
                    }).error(function (e) {
                    console.log('当前状态：' + docStates[e.data] || 'null');
                    alert(e.message || '操作失败，请联系系统管理员');
                });
            }
        };

        $scope.download = function(){
            $http.get('admin/soft/dl/' + $scope.data.uuId)
                .success(function(result){
                    //window.location.href = result['data'];
                    var downloadhtml = '<ul class="file-list clearfix">';
                    result['data'].forEach(function(element, index, array){
                        downloadhtml += '<li class="clearfix"><div class="file-name">'+element.soft.fileName +'</div>'+
                            '<a class="file-download" href="'+element.url+'"><i class="fa fa-download"></i> 下载</a></li>';
                    });
                    downloadhtml +='</ul>'
                    document.getElementById('downloadDialog').innerHTML += downloadhtml;
                    var pagei = layer.open({
                        type: 1,   //0-4的选择,
                        title: "下载附件",
                        border: [0],
                        shadeClose: true,
                        area: ['auto', 'auto'],
                        content:  $('#downloadDialog')
                    });
                })
        }

        $scope.save = function () {
            if ($scope.categoryTwo) {
                $scope.data.softCategoryId = $scope.categoryTwo.id;
            }
            if ($scope.vars.tags)
                $scope.data.tags = $scope.vars.tags.split(' ');
            if ($scope.data && $scope.data.uuId) {
                $http.put('admin/soft/' + $scope.data.uuId, $scope.data)
                    .success(function (data) {
                        $scope.data = data;
                        alert('修改完成！');
                    }).error(function () {
                    alert('修改失败！');
                });
            } else if ($scope.data && $scope.data.title) {
                $http.post('admin/soft', $scope.data)
                    .success(function (data) {
                        $scope.data = data;
                        alert('添加完成！');
                    }).error(function () {
                    alert('添加失败！');
                });
            }
        };

        $scope.delete = function () {
            if (confirm('确定要删除【' + $scope.data.title + '】吗？')) {
                $http.delete('admin/soft/' + $scope.data.uuId).success(function () {
                    history.back();
                }).error(function (d) {
                    console.log(d);
                    alert("删除失败！");
                });
            }
        };
    }]);
});