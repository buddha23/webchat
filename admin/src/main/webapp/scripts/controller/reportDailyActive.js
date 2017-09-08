define(['app', 'echarts', 'dataTables', 'directive/ng-pager'], function (app, echarts) {
    'use strict';

    app.controller("reportDailyActiveCtrl", ['$scope', '$rootScope', '$http', '$filter', function ($scope, $rootScope, $http, $filter) {
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
        $scope.docs = [];
        $scope.softs = [];
        $scope.posts = [];
        $scope.video = [];
        $scope.course = [];
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
                x: '30%',
                data: [{
                    name: "文档日活跃",
                    textStyle: {
                        color: '#22ac38'
                    }
                }, {
                    name: "问答日活跃",
                    textStyle: {
                        color: '#f58b41'
                    }
                }, {
                    name: "软件日活跃",
                    textStyle: {
                        color: 'skyblue'
                    }
                }, {
                    name: "视频日活跃",
                    textStyle: {
                        color: 'orangered'
                    }
                }, {
                    name: "教程日活跃",
                    textStyle: {
                        color: 'black'
                    }
                }],
                itemGap: 20,
                borderWidth: 1,
                padding: 10,
                textStyle: {
                    color: 'auto'
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
                name: '文档日活跃',
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
                data: $scope.docs
            }, {
                name: '问答日活跃',
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
                data: $scope.softs
            }, {
                name: '软件日活跃',
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
                data: $scope.posts
            }, {
                name: '视频日活跃',
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
                data: $scope.video
            }, {
                name: '教程日活跃',
                type: 'line',
                showSymbol: true,
                itemStyle: {
                    normal: {
                        label: {
                            show: false
                        },
                        color: 'black'
                    }
                },
                data: $scope.course
            }]
        };
        var mychart = echarts.init(document.getElementById('line'));

        // 数据
        $scope.getData = function () {
            $http.get('admin/dailyReport/list', {params: $scope.params}).success(function (data) {
                $scope.data = data;
                $scope.item.splice(0, $scope.item.length);
                $scope.docs.splice(0, $scope.docs.length);
                $scope.softs.splice(0, $scope.softs.length);
                $scope.posts.splice(0, $scope.posts.length);
                $scope.video.splice(0, $scope.video.length);
                $scope.course.splice(0, $scope.course.length);
                if (data) {
                    data.forEach(function (d) {
                        $scope.item.push(d.lookTime);
                        $scope.docs.push(d.docDailyActive);
                        $scope.softs.push(d.postsDailyActive);
                        $scope.posts.push(d.softDailyActive);
                        $scope.video.push(d.videoDailyActive);
                        $scope.course.push(d.courseDailyActive);
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
    }]);

});