define(['app', 'directive/ng-file'], function (app) {
    'use strict';

    app.controller('docCategoryCtrl', ['$scope', '$http', function ($scope, $http) {
        $http.get('i/doc_categories').success(function (data) {
            $scope.data = data;
            if (data && data.length) {
                $scope.cg1 = data[0];
            }
        });

        $scope.fileChanged = function (file) {
            if (file && FileReader) {
                if (file.size > 1024 * 1024) {
                    delete $scope.t.file;
                    return alert('图片大小不能大于1M');
                }
                var reader = new FileReader();
                reader.onload = function (e) {
                    angular.element('#iconImg').attr('src', e.target.result);
                };
                reader.readAsDataURL(file);
            }
        };

        $scope.add = function (l) {
            delete $scope.c;
            delete $scope['cg' + l];
            $scope.t = {};
            if (l === 2 && $scope.cg1) {
                $scope.t.fid = $scope.cg1.id;
            }
        };

        $scope.edit = function (c, l) {
            if (l === 1) {
                $scope.cg1 = c;
                delete $scope.cg2;
                $http.get('admin/docCategory/' + c.id + '/moderators').success(function (moderators) {
                    $scope.moderators = moderators;
                });
            } else if (l === 2)
                $scope.cg2 = c;
            $scope.c = c;
            $scope.t = $.extend({}, c);
            delete $scope.t.file;
        };

        $scope.save = function () {
            var form = new FormData();
            if ($scope.t.file)
                form.append('file', $scope.t.file);
            if ($scope.t.id)
                form.append('id', $scope.t.id);
            if ($scope.t.fid)
                form.append('fid', $scope.t.fid);
            if ($scope.t.icon)
                form.append('icon', $scope.t.icon);
            form.append('name', $scope.t.name);
            if ($scope.t.description)
                form.append('description', $scope.t.description);
            $http({
                url: 'admin/docCategory/save',
                method: 'POST',
                data: form,
                headers: {
                    'Content-Type': undefined
                }
            }).success(function (data) {
                if ($scope.t.id) {
                    $.extend($scope.c, data);
                } else {
                    if (!data.fid) {
                        $scope.data.push(data);
                    } else if (data.fid === $scope.cg1.id) {
                        $scope.cg1.children.push(data);
                    }
                }
                $scope.t = data;
            }).error(function (r) {
                alert('保存失败');
            });
        };

        $scope.delete = function (c) {
            if (c.id) {
                if (confirm('删除类型将导致该类型下所有的文档处于无类型状态，确认删除吗？')) {
                    $http.delete('admin/docCategory/' + c.id).success(function (data) {
                        if (c.fid) {
                            $scope.cg1.children.removeChild($scope.cg2);
                            delete $scope.cg2;
                        } else {
                            $scope.data.removeChild($scope.cg1);
                            delete $scope.cg2;
                            delete $scope.cg1;
                        }
                        delete $scope.t;
                        delete $scope.c;
                    }).error(function (r) {
                        console.log(r);
                        alert('删除失败！');
                    });
                }
            }
        };

        $scope.addModerator = function () {
            if ($scope.c && !$scope.c.fid) {
                var mobile = $('#userMobile').val();
                if (/^1\d{10}$/.test(mobile)) {
                    $.post('admin/docCategory/' + $scope.c.id + '/moderators', {'mobile': mobile}, function (result) {
                        $scope.moderators.push(result);
                        $scope.$apply($scope.moderators);
                        $('#userMobile').val('');
                    }).error(function (r) {
                        alert(r['responseJSON'].message);
                    });
                } else {
                    alert('手机号填写不正确！');
                }
            }
        };

        $scope.delModerator = function (moderator) {
            if (confirm('确定删除版主【' + moderator.nickname + '】吗？')) {
                $.ajax({
                    type: 'DELETE',
                    url: 'admin/docCategory/' + $scope.c.id + '/moderators/' + moderator.userId,
                    success: function (result) {
                        $scope.moderators.removeChild(moderator);
                        $scope.$apply($scope.moderators);
                    },
                    error: function (r) {
                        alert('删除失败！');
                    }
                });
            }
        };

    }]);
});