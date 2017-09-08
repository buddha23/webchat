define(['app', 'layer', 'umeditor', 'directive/ng-pager', 'directive/ng-file', 'filter/filesize', 'filter/filename', 'ztree'], function (app, layer, UM) {
    'use strict';

    app.controller('volumeDetailCtrl', ['$scope', '$http', '$state', '$stateParams', function ($scope, $http, $state, $stateParams) {

        var namePrefixPatten = /第\d+[章节] /;
        var nodesCount = 1;
        var zTreeSetting = {
            edit: {
                enable: true,
                showRemoveBtn: true,
                showRenameBtn: true,
                drag: {
                    prev: function (treeId, treeNodes, targetNode) {
                        return targetNode != null && treeNodes[0].isParent === targetNode.isParent;
                    },
                    next: function (treeId, treeNodes, targetNode) {
                        return targetNode != null && treeNodes[0].isParent === targetNode.isParent;
                    },
                    inner: function (treeId, treeNodes, targetNode) {
                        return targetNode != null && !treeNodes[0].isParent && targetNode.isParent;
                    }
                }
            },
            callback: {
                beforeEditName: function (id, node) {
                    var match = node.sname.match(namePrefixPatten);
                    if (match) {
                        node.tempname = match[0];
                    }
                    node.sname = node.sname.replace(namePrefixPatten, '');
                    if (!node.isParent) {
                        $('#nodeName').val(node.sname);
                        $('#nodeUrl').val(node.key);
                        $('#nodeDuration').val(node.duration || 0.0);
                        $('#isFree').prop('checked', !!node.free);

                        layer.open({
                            type: 1,
                            area: '680px',
                            title: '编辑小结信息（必填）',
                            content: $('#nodeForm'),
                            btn: ['确定', '取消'],
                            yes: function (index) {
                                node.name = $('#nodeName').val();
                                node.sname = node.tempname + node.name;
                                node.duration = $('#nodeDuration').val();
                                node.free = $('#isFree').prop('checked');
                                zTree.updateNode(node);
                                delete node.tempname;
                                layer.close(index);
                            },
                            cancel: function () {
                                $('#nodeForm input').val('');
                            }
                        });
                        return false;
                    }
                },
                onRename: function (e, id, node, isCancel) {
                    if (node.tempname) {
                        node.name = node.sname;
                        node.sname = node.tempname + node.sname;
                        zTree.updateNode(node);
                        delete node.tempname;
                    }
                },
                onDrop: function (e, treeId, treeNodes, targetNode, moveType) {
                    updateNodeName();
                },
                onMouseUp: function (e, treeId, treeNode) {
                    if ($tmpDom) {
                        $tmpDom.remove();
                        $tmpDom = null;
                    }
                    if (!$target || !ngItem || !treeNode)return;
                    var parentNode = treeNode.isParent ? treeNode : treeNode.getParentNode();
                    if (parentNode) {
                        var id = isNaN(ngItem.id) ? ('n' + nodesCount++) : ngItem.id;
                        var nodes = zTree.addNodes(parentNode, {
                            id: id,
                            name: $target.attr('data-name'),
                            key: ngItem.key,
                            duration: ngItem.duration || 0,
                            item: ngItem,
                            free: !!ngItem.free,
                            isParent: false
                        });
                        zTree.selectNode(nodes[0]);
                        updateNodeName();
                    } else {
                        ngItem.show = 1;
                    }
                    $target = null;
                    ngItem = null;
                },
                beforeRemove: function (id, node) {
                    if (node.isParent && zTree.getNodes().length <= 1) {
                        layer.alert('最少必须保留一个章节。');
                        return false;
                    }
                    return true;
                },
                onRemove: function (e, id, node) {
                    if (node.isParent) {
                        if (node.children) {
                            node.children.forEach(removeTreeNode);
                        }
                    } else if (node.id) {
                        removeTreeNode(node);
                    }
                    $scope.$apply($scope.ossDatas);
                    updateNodeName();
                }
            },
            data: {
                key: {
                    name: 'sname',
                    title: 'key'
                },
                keep: {
                    parent: true,
                    leaf: true
                },
                simpleData: {
                    enable: true
                }
            },
            view: {
                selectedMulti: false,
                addHoverDom: function (id, node) {
                    if (node.editNameFlag || $("#addBtn_" + node.tId).length > 0) return;
                    if (node.isParent) {
                        var $addBtn = $('<span class="button add" id="addBtn_' + node.tId + '" title="添加新章"></span>');
                        $("#" + node.tId + "_span").after($addBtn);
                        $addBtn.bind("click", function () {
                            var n = zTree.addNodes(null, (zTree.getNodeIndex(node) + 1), {
                                id: ('n' + nodesCount++),
                                pId: 0,
                                name: '新学习章',
                                key: '',
                                isParent: true
                            });
                            updateNodeName();
                            return false;
                        });
                    }
                },
                removeHoverDom: function (id, node) {
                    $("#addBtn_" + node.tId).unbind().remove();
                }
            }
        };

        var zTree = null;
        var $target = null;
        var ngItem = null;
        var $tmpDom = null;
        var $doc = $(document);

        $scope.ossDatas = [];

        function removeTreeNode(n) {
            if (n.item) {
                $scope.ossDatas.forEach(function (i) {
                    if (i.$$hashKey === n.item.$$hashKey) {
                        i.show = 1;
                        i.duration = n.duration || 0;
                        i.free = !!n.free;
                        return false;
                    }
                });
            } else {
                $scope.ossDatas.push({
                    id: n.id,
                    name: n.name,
                    key: n.url,
                    size: 0,
                    free: !!n.free,
                    duration: n.duration || 0,
                    show: 1
                });
            }
        }

        // 更新节点名称
        function updateNodeName() {
            var nodes = zTree.getNodes();
            for (var i = 0; i < nodes.length; i++) {
                nodes[i].sname = '第' + (i + 1) + '章 ' + nodes[i].name;
                var num = nodes[i].children ? nodes[i].children.length : 0;
                for (var j = 0; j < num; j++) {
                    nodes[i].children[j].sname = '第' + (j + 1) + '节 ' + nodes[i].children[j].name;
                    zTree.updateNode(nodes[i].children[j]);
                }
                zTree.updateNode(nodes[i]);
            }
        }

        function convertVolume2ChapterTree(v) {
            var cs = [];
            if (v && v.chapters) {
                for (var i = 0; i < v.chapters.length; i++) {
                    var c = v.chapters[i];
                    c.name = c.name.replace(namePrefixPatten, '');
                    c.isParent = true;
                    c.children = c.vodSections;
                    c.key = c.name;
                    if (!c.children) c.children = [];
                    for (var j = 0; j < c.children.length; j++) {
                        c.children[j].name = c.children[j].name.replace(namePrefixPatten, '');
                        c.children[j].key = c.children[j].url;
                        var s = c.children[j];
                    }
                    cs.push(c);
                }
            }
            return cs;
        }

        function initZTree() {
            zTree = $.fn.zTree.init($("#volumeTree"), zTreeSetting, chapterTree);
            updateNodeName();
        }

        var chapterTree = [{id: 0, pId: null, name: "默认", isParent: true, open: true}];

        $http.get('i/vod_categories').success(function (docCategories) {
            $scope.c1s = docCategories;
            $scope.vodTag = [];
            if ($stateParams.id && $stateParams.id > 0) {
                $http.get('admin/video/' + $stateParams.id).success(function (data) {
                    $scope.volume = data;
                    editor.ready(function () {
                        editor.setContent(data.introduction);
                    });
                    if (data.caterogyId) {
                        for (var i = 0; i < $scope.c1s.length; i++) {
                            var c1 = $scope.c1s[i];
                            if (!c1.children) continue;
                            for (var j = 0; j < c1.children.length; j++) {
                                if (data.caterogyId === c1.children[j].id) {
                                    $scope.c1 = c1;
                                    break;
                                }
                            }
                        }
                    }
                    if (data.tags.length > 0) {
                        data.tags.forEach(function (t) {
                            $scope.vodTag.push(t.id);
                        });
                        //console.log($scope.vodTag);
                    }
                    chapterTree = convertVolume2ChapterTree(data);
                    initZTree();
                }).error(function (r) {
                    layer.alert('获取视频集内容失败：id=' + $stateParams.id);
                });
                // 获取视频集标签
                //$http.get('admin/video/getVodTag' + $stateParams.id).success(function (data) {
                //    $scope.vodTag = data;
                //}).error(function () {
                //    console.log('获取视频已有标签失败');
                //});
            } else {
                $scope.volume = {
                    id: 0,
                    name: '新增数控视频',
                    costScore: 0,
                    introduction: '视频介绍',
                    content: '视频内容（章节介绍）',
                    lecturerId: 0,
                    lecturerProfit: 0
                };
                initZTree();
            }
            if (!$scope.c1 && $scope.c1s.length > 0) $scope.c1 = $scope.c1s[0];
            $scope.getAllTag();
        });

        $scope.bindToTree = function (e, item) {
            var target = e.target;
            if (target != null && $(target).hasClass('ossItem')) {
                $target = $(target);
                item.show = 0;
                // $target.parent('li').hide();
                ngItem = item;

                $tmpDom = $('<div class="drop"></div>').html(target.innerHTML).css({
                    "top": (e.clientY + $doc.scrollTop() + 3) + "px",
                    "left": (e.clientX + $doc.scrollLeft() + 3) + "px"
                }).appendTo('body');

                $doc.bind("mousemove", function (em) {
                    try {
                        window.getSelection ? window.getSelection().removeAllRanges() : document.selection.empty();
                        if ($tmpDom) {
                            $tmpDom.css({
                                "top": (em.clientY + $doc.scrollTop() + 3) + "px",
                                "left": (em.clientX + $doc.scrollLeft() + 3) + "px"
                            });
                        }
                    } catch (ex) {
                    }
                    return false;
                });
                $doc.bind("mouseup", function (eu) {
                    $doc.unbind("mousemove");
                    $doc.unbind("mouseup");
                    if ($(eu.target).parents("#volumeTree").length == 0) {
                        if ($target) {
                            item.show = 1;
                            $scope.$apply(item);
                        }
                        if ($tmpDom) {
                            $tmpDom.remove();
                        }
                        $target = null;
                        $tmpDom = null;
                        ngItem = null;
                    }
                });
            }
            if (e.preventDefault) {
                e.preventDefault();
            }
        };

        $scope.getOssList = function () {
            var dir = $('#ossDir').val();
            $http.get('admin/oss/list', {params: {dir: dir}}).success(function (data) {
                var urls = [];
                var cs = zTree.getNodes();
                for (var m = 0; m < cs.length; m++) {
                    if (!cs[m].children) continue;
                    for (var n = 0; n < cs[m].children.length; n++) {
                        urls.push(cs[m].children[n].url || cs[m].children[n].key);
                    }
                }

                $scope.ossDatas = [];
                var c = 0;
                for (var i = 0; i < data.length; i++) {
                    var exists = false;
                    for (var j = 0; j < urls.length; j++) {
                        if (urls[j] === data[i].key) {
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) {
                        data[i].show = 1;
                        data[i].id = data[i].etag;
                        delete data[i].etag;
                        $scope.ossDatas.push(data[i]);
                        c++;
                    }
                }
                layer.msg('查询获取到【' + data.length + '】条数据，<br>排除后列表新增【' + c + '】条。');
            }).error(function () {
                layer.alert('获取OSS文件列表失败！');
            });
        };

        var editor = UM.getEditor('editor', {
            imageUrl: 'admin/ume/images',
            imagePath: '/files/',
            lang: 'zh-cn',
            langPath: UMEDITOR_CONFIG.UMEDITOR_HOME_URL + "lang/",
            focus: false,
            zIndex: 1600
        });

        $scope.$on("$destroy", function () {
            UM.delEditor('editor');
        });

        $scope.fileChanged = function (file) {
            if (!file) {
                return;
            }
            var form = new FormData();
            form.append('cover', file);
            $http({
                url: 'admin/video/cover',
                method: 'POST',
                data: form,
                headers: {
                    'Content-Type': undefined
                }
            }).success(function (data) {
                $scope.volume.cover = data.relativePath;
            }).error(function () {
                $scope.data.file = null;
                layer.alert('上传头像失败');
            });
        };

        $scope.save = function () {
            if (!zTree || zTree.getNodes().length < 1) {
                return false;
            }
            $scope.volume.introduction = editor.getContent();
            $scope.volume.chapters = [];
            var cs = zTree.getNodes();
            for (var i = 0; i < cs.length; i++) {
                var c = {
                    id: (isNaN(cs[i].id) ? null : cs[i].id) || null,
                    name: cs[i].sname,
                    sorting: i + 1,
                    volumeId: $scope.volume.id
                };
                if (cs[i].children) {
                    c.vodSections = [];
                    var ss = cs[i].children;
                    for (var j = 0; j < ss.length; j++) {
                        var s = {
                            id: isNaN(ss[j].id) ? null : ss[j].id,
                            name: ss[j].sname,
                            sorting: j + 1,
                            url: ss[j].url || ss[j].key,
                            duration: ss[j].duration || 0.0,
                            free: typeof ss[j].free === 'boolean' ? ss[j].free : false
                        };
                        c.vodSections.push(s);
                    }
                }
                $scope.volume.chapters.push(c);
            }
            $http.post('admin/video', $scope.volume).success(function (volume) {
                var $cb = $('[name="vod-tag"]');
                var checked = [];
                $cb.filter(':checked').each(function () {
                    checked.push(this.value);
                });
                if (checked.length > 0) {
                    //console.log(checked);
                    $http.post('admin/video/addVodTag/' + volume.id, checked).success(function () {
                        console.log("添加标签成功");
                    }).error(function (r) {
                        console.log('添加标签失败:' + r);
                    });
                }
                $scope.volume = volume;
                chapterTree = convertVolume2ChapterTree(volume);
                initZTree();
                //console.log(volume);
                layer.msg('处理完成');
            }).error(function (error) {
                console.log(error);
                layer.alert('处理失败');
            });
        };

        $scope.getAllTag = function () {
            $http.get('admin/video/getAllTag').success(function (data) {
                $scope.allTag = data;
            }).error(function () {
                layer.alert('获取标签失败');
            });
        };

        $scope.getAllLecturer = function () {
            $http.get('admin/video/getAllLecturer').success(function (data) {
                $scope.allLecturer = data;
            }).error(function () {
                layer.alert('获取讲师失败');
            });
        };

        $scope.isSelected = function (id) {
            return $scope.vodTag.indexOf(id) != -1;
        };

        $scope.checkClass = function (id) {
            if ($scope.vodTag.indexOf(id) != -1)
                return 'am-btn am-btn-default am-active';
            else
                return 'am-btn am-btn-default';
        };

        $scope.getAllLecturer();

        //$scope.queding = function () {
        //    var $cb = $('[name="vod-tag"]');
        //    var checked = [];
        //    $cb.filter(':checked').each(function () {
        //        checked.push(this.value);
        //    });
        //    console.log('复选框选中的是：', checked.join(' | '));
        //};

    }]);
});