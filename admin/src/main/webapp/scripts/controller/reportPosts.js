define(['app', 'echarts', 'dataTables', 'directive/ng-pager'], function (app, echarts) {
    'use strict';

    //app.filter('zero2t', function () {
    //    return function (a) {
    //        return a == 0 ? '-' : a;
    //    }
    //});

    app.controller("reportPostsCtrl", ['$scope', '$rootScope', '$http', '$filter', function ($scope, $rootScope, $http, $filter) {
        // 参数
        $scope.beginTime = '';
        $scope.endTime = '';
        $scope.params = {
            beginTime: '',
            endTime: '',
            unit: ''
        };
        // 选择年月日
        var unit = {
            'DATE': '天',
            'MONTH': '月',
            'YEAR': '年'
        };

        $scope.item = [];
        $scope.views = [];
        $scope.posts = [];
        $scope.answers = [];
        $scope.course = [];
        $scope.video = [];
        // 折线
        var option = {
            title: {
                text: '折线趋势图:'
            },
            // 提示框，鼠标悬浮交互时的信息提示
            tooltip: {
                show: true,
                trigger: 'axis'
            },
            // 图例
            legend: {
                top: 0,
                x: '55%',
                data: [{
                    name: "帖吧访问量",
                    textStyle: {
                        color: '#22ac38'
                    }
                }, {
                    name: "帖吧提问量",
                    textStyle: {
                        color: '#f58b41'
                    }
                }, {
                    name: "帖吧回答量",
                    textStyle: {
                        color: 'skyblue'
                    }
                }, {
                    name: "教程浏览量",
                    textStyle: {
                        color: 'orangered'
                    }
                }, {
                    name: "课程浏览量",
                    textStyle: {
                        color: 'blue'
                    }
                }],
                itemGap: 20,
                borderWidth: 1,
                padding: 10,
                textStyle: {
                    color: 'auto'
                },
                selected: {
                    '教程浏览量': false,
                    '课程浏览量': false
                }
            },
            // 横轴坐标轴
            xAxis: [{
                type: 'category',
                data: $scope.item
            }],
            // 纵轴坐标轴
            yAxis: [{
                type: 'value',
                splitLine: {
                    show: true
                }
            }],
            // 数据内容数组
            series: [{
                name: '帖吧访问量',
                type: 'line',
                showSymbol: true,
                itemStyle: {
                    normal: {
                        label: {
                            show: false
                        },
                        color: '#22ac38'
                    }
                },
                data: $scope.views
            }, {
                name: '帖吧提问量',
                type: 'line',
                showSymbol: true,
                itemStyle: {
                    normal: {
                        label: {
                            show: false
                        },
                        color: '#f58b41'
                    }
                },
                data: $scope.posts
            }, {
                name: '帖吧回答量',
                type: 'line',
                showSymbol: true,
                itemStyle: {
                    normal: {
                        label: {
                            show: false
                        },
                        color: 'skyblue'
                    }
                },
                data: $scope.answers
            }, {
                name: '教程浏览量',
                type: 'line',
                showSymbol: true,
                itemStyle: {
                    normal: {
                        label: {
                            show: false
                        },
                        color: 'orangered'
                    }
                },
                data: $scope.course
            }, {
                name: '课程浏览量',
                type: 'line',
                showSymbol: true,
                itemStyle: {
                    normal: {
                        label: {
                            show: false
                        },
                        color: 'blue'
                    }
                },
                data: $scope.video
            }]
        };
        var mychart = echarts.init(document.getElementById('line'));

        // 数据
        $scope.getData = function () {
            $http.get('admin/dailyReport/list', {params: $scope.params}).success(function (data) {
                $scope.data = data;
                $scope.item.splice(0, $scope.item.length);
                $scope.views.splice(0, $scope.views.length);
                $scope.posts.splice(0, $scope.posts.length);
                $scope.answers.splice(0, $scope.answers.length);
                $scope.course.splice(0, $scope.course.length);
                $scope.video.splice(0, $scope.video.length);
                if (data) {
                    data.forEach(function (d) {
                        $scope.item.push(d.lookTime);
                        $scope.views.push(d.postDailyViews);
                        $scope.posts.push(d.postDailyIncrements);
                        $scope.answers.push(d.commentDailyIncrement);
                        $scope.course.push(d.courseDailyViews);
                        $scope.video.push(d.videoDailyViews);
                    });
                }
                mychart.setOption(option);
            })
        };
        $scope.doSearch = function () {
            delete $scope.data;
            $scope.params.unit = $("#timeUnit").val();
            $scope.params.beginTime = $scope.beginTime;
            $scope.params.endTime = $scope.endTime;
            $scope.getData();
        };

        $scope.getData();

        $scope.lookposts = function () {
            $('#lookposts').addClass('am-btn-primary');
            $('#lookcourse').removeClass('am-btn-primary');
            $('#lookvideo').removeClass('am-btn-primary');
            option.legend.selected = {
                '帖吧访问量': true,
                '帖吧提问量': true,
                '帖吧回答量': true,
                '教程浏览量': false,
                '课程浏览量': false
            };
            mychart.setOption(option);
        };

        $scope.lookcourse = function () {
            $('#lookcourse').addClass('am-btn-primary');
            $('#lookposts').removeClass('am-btn-primary');
            $('#lookvideo').removeClass('am-btn-primary');
            option.legend.selected = {
                '帖吧访问量': true,
                '帖吧提问量': false,
                '帖吧回答量': false,
                '教程浏览量': true,
                '课程浏览量': false
            };
            mychart.setOption(option);
        };

        $scope.lookvideo = function () {
            $('#lookvideo').addClass('am-btn-primary');
            $('#lookposts').removeClass('am-btn-primary');
            $('#lookcourse').removeClass('am-btn-primary');
            option.legend.selected = {
                '帖吧访问量': false,
                '帖吧提问量': false,
                '帖吧回答量': false,
                '教程浏览量': false,
                '课程浏览量': true
            };
            mychart.setOption(option);
        };

    }]);

});