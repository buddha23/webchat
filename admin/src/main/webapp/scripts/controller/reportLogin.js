define(['app', 'echarts', 'dataTables', 'directive/ng-pager'], function (app, echarts) {
    'use strict';

    app.filter('zero2t', function () {
        return function (a) {
            return a == 0 ? '-' : a;
        }
    });

    app.controller("reportLoginCtrl", ['$scope', '$rootScope', '$http', function ($scope, $rootScope, $http) {
        // 参数
        var url = '';
        $scope.beginTime = '';
        $scope.endTime = '';
        $scope.params = {
            beginTime: '',
            endTime: '',
            unit: ''
            //searchTime: ''
        };
        $scope.legend = [];
        $scope.item = [];
        $scope.pcNum = [];
        $scope.mbNum = [];
        // 分页
        $scope.pData = {
            totalPages: 0,
            number: 0
        };
        $scope.showData = [];
        // 折线
        var option = {
            title: {
                text: '注册折线趋势图:'
            },
            // 提示框，鼠标悬浮交互时的信息提示
            tooltip: {
                show: true,
                trigger: 'axis'
            },
            // 图例
            legend: {
                top: 0,
                x: '75%',
                data: [{
                    name: "网站注册人数",
                    textStyle: {
                        color: '#22ac38'
                    }
                }, {
                    name: "手机注册人数",
                    textStyle: {
                        color: '#f58b41'
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
                name: '网站注册人数',
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
                data: $scope.pcNum
            }, {
                name: '手机注册人数',
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
                data: $scope.mbNum
            }]
        };
        var mychart = echarts.init(document.getElementById('line'));
        // 数据
        $scope.getData = function () {
            $http.get(url, {params: $scope.params}).success(function (data) {
                $scope.data = data;
                $scope.showData = $scope.data.slice(0, 10);
                $scope.setPage();
                $scope.item.splice(0, $scope.item.length);
                $scope.pcNum.splice(0, $scope.pcNum.length);
                $scope.mbNum.splice(0, $scope.mbNum.length);
                if (data) {
                    data.forEach(function (d) {
                        $scope.item.push(d.lookTime);
                        $scope.pcNum.push(d.pcNums);
                        $scope.mbNum.push(d.mobileNum);
                    });
                }
                mychart.setOption(option);
            })
        };
        $scope.getTotleData = function () {
            $http.get("admin/userreport/userRegisterNumer").success(function (data) {
                //console.log(data);
                $scope.mobileTotle = data[0];
                $scope.qrcodeTotle = data[1];
                $scope.webTotle = data[2];
            });
        };
        // 选择年月日
        var unit = {
            'DATE': '天',
            'MONTH': '月',
            'YEAR': '年'
        };
        $scope.doSearch = function () {
            delete $scope.data;
            $scope.params.unit = $("#timeUnit").val();
            $scope.params.beginTime = $scope.beginTime;
            $scope.params.endTime = $scope.endTime;
            $scope.getData();
        };
        $scope.getPage = function (page) {
            if (page > 0 && page < $scope.pData.totalPages + 1) {
                $scope.page = page;
                $scope.pData.number = page - 1;
                $scope.showData = $scope.data.slice((page - 1) * 10, 10 * page);
            }
        };
        $scope.setPage = function () {
            var length = $scope.data.length;
            if (length > 10) {
                $scope.pData.totalPages = Math.ceil(length / 10);
            }
        };
        // 切换
        $scope.lookregist = function () {
            $('#lookregist').addClass('am-btn-primary');
            $('#looklogin').removeClass('am-btn-primary');

            $scope.legend = ["网站注册人数", "手机注册人数"];
            option.title.text = '注册折线趋势图:';
            option.legend.data[0].name = "网站注册人数";
            option.legend.data[1].name = "手机注册人数";
            option.series[0].name = "网站注册人数";
            option.series[1].name = "手机注册人数";

            url = 'admin/userreport/userRegistReport';
            $scope.getData();
            $scope.getTotleData();
        };
        $scope.looklogin = function () {
            $('#looklogin').addClass('am-btn-primary');
            $('#lookregist').removeClass('am-btn-primary');

            $scope.legend = ["网站登录人数", "手机登录人数"];
            option.title.text = '登录折线趋势图:';
            option.legend.data[0].name = "网站登录人数";
            option.legend.data[1].name = "手机登录人数";
            option.series[0].name = "网站登录人数";
            option.series[1].name = "手机登录人数";

            url = 'admin/userreport/userLoginReport';
            $scope.getData();
        };

        $scope.lookregist();

    }]);

});