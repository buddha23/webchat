define(['app', 'echarts', 'dataTables', 'directive/ng-pager'], function (app, echarts) {
    'use strict';

    app.filter('zero2t', function () {
        return function (a) {
            return a == 0 ? '-' : a;
        }
    });

    app.controller("reportCategoryCtrl", ['$scope', '$rootScope', '$http', function ($scope, $rootScope, $http) {
        // 查询参数
        $scope.beginTime = '';
        $scope.endTime = '';
        $scope.params = {
            beginTime: '',
            endTime: '',
            unit: '',
            ids: []
        };
        // 分页参数
        $scope.pData = {
            totalPages: 0,
            number: 0
        };
        $scope.totalData = [];
        $scope.showData = [];
        // 折线
        $scope.item = [];
        $scope.option = {
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
                x: '20%',
                data: [],
                itemGap: 20,
                borderWidth: 1,
                padding: 10,
                textStyle: {
                    color: 'black'
                }
            },
            // 横轴坐标轴
            xAxis: [{
                type: 'category',
                data: []
            }],
            // 纵轴坐标轴
            yAxis: [{
                type: 'value',
                splitLine: {
                    show: true
                }
            }],
            // 数据内容数组
            series: []
        };
        var mychart = echarts.init(document.getElementById('line'));

        // 数据
        $scope.getCategorys = function () {
            $http.get('admin/dailyReport/categoryList').success(function (data) {
                if (data) {
                    $scope.categorys = data;
                    $scope.params.ids.push(data[0].id, data[1].id);
                    $scope.getData();
                }
            });
        };
        $scope.getCategorys();

        $scope.getData = function () {
            if ($scope.params.ids.length > 10) {
                alert("勾选板块太多,最多10个");
                return false;
            }
            $http.get('admin/dailyReport/categoryReport', {
                params: $scope.params
            }).success(function (dataList) {
                $scope.dataList = dataList;
                $scope.item = [];
                $scope.option.xAxis[0].data = [];
                if (dataList && dataList.length > 0) {
                    // 横轴
                    var list1 = dataList[0];
                    if (list1.length > 0) {
                        list1.forEach(function (data) {
                            $scope.item.push(data.lookTime);
                            $scope.option.xAxis[0].data.push(data.lookTime);
                        });
                        $scope.lookviews();
                    }
                }
            }).error(function (r) {
                console.log(r);
            });
        };

        // 选择年月日
        var unit = {
            'DATE': '天',
            'MONTH': '月',
            'YEAR': '年'
        };
        $scope.doSearch = function () {
            delete $scope.dataList;
            $scope.params.unit = $("#timeUnit").val();
            $scope.params.beginTime = $scope.beginTime;
            $scope.params.endTime = $scope.endTime;
            $scope.getData();
        };
        // 分页
        $scope.getPage = function (page) {
            if (page > 0 && page < $scope.pData.totalPages + 1) {
                $scope.page = page;
                $scope.pData.number = page - 1;
                $scope.showData = $scope.totalData.slice((page - 1) * 15, 15 * page);
            }
        };
        $scope.setPage = function () {
            var length = $scope.totalData.length;
            if (length > 15) {
                $scope.pData.totalPages = Math.ceil(length / 15);
            }
        };

        // 勾选
        $scope.chose = function ($event, m) {
            var checkbox = $event.target;
            var action = (checkbox.checked ? 'add' : 'remove');
            var idx = $scope.params.ids.indexOf(m);
            if (action == 'add' && idx == -1) {
                $scope.params.ids.push(m);
            }
            if (action == 'remove' && idx != -1) {
                $scope.params.ids.splice(idx, 1);
            }
        };

        $scope.isSelected = function (id) {
            return $scope.params.ids.indexOf(id) != -1;
        };

        // 查看浏览量
        $scope.lookviews = function () {
            $('#lookviews').addClass('am-btn-primary');
            $('#lookviews').siblings().removeClass('am-btn-primary');
            $scope.option.title.text = "浏览量折线趋势图:";

            $scope.totalData = [];
            $scope.option.series = [];
            $scope.option.legend.data = [];
            // series数据
            for (var i = 0; i < $scope.dataList.length; i++) {
                var seri = {
                    name: '',
                    type: 'line',
                    showSymbol: true,
                    data: []
                };
                $scope.option.series.push(seri);
            }
            for (var i = 0; i < $scope.option.series.length; i++) {
                var dailyReport = $scope.dataList[i];
                $scope.option.legend.data.push(dailyReport[0].categoryName);
                $scope.option.series[i].name = dailyReport[0].categoryName;
                $scope.option.series[i].data = [];
                dailyReport.forEach(function (report) {
                    $scope.option.series[i].data.push(report.dailyViews);
                });
            }
            mychart.setOption($scope.option);
            // 列表数据
            for (var i = 0; i < $scope.dataList[0].length; i++) {
                var trdata = {
                    lookTime: '',
                    data: []
                };
                trdata.lookTime = $scope.item[i];
                for (var j = 0; j < $scope.dataList.length; j++) {
                    trdata.data.push($scope.dataList[j][i].dailyViews);
                }
                $scope.totalData.push(trdata);
            }
            // 第一页数据
            $scope.showData = $scope.totalData.slice(0, 15);
            $scope.setPage();
        };

        // 查看上传量
        $scope.lookuploads = function () {
            $('#lookuploads').addClass('am-btn-primary');
            $('#lookuploads').siblings().removeClass('am-btn-primary');
            $scope.option.title.text = "上传量折线趋势图:";
            $scope.totalData = [];
            $scope.option.series = [];
            $scope.option.legend.data = [];
            // series数据
            for (var i = 0; i < $scope.dataList.length; i++) {
                var seri = {
                    name: '',
                    type: 'line',
                    showSymbol: true,
                    data: []
                };
                $scope.option.series.push(seri);
            }
            for (var i = 0; i < $scope.option.series.length; i++) {
                var dailyReport = $scope.dataList[i];
                $scope.option.legend.data.push(dailyReport[0].categoryName);
                $scope.option.series[i].name = dailyReport[0].categoryName;
                $scope.option.series[i].data = [];
                dailyReport.forEach(function (report) {
                    $scope.option.series[i].data.push(report.dailyUploads);
                });
            }
            mychart.setOption($scope.option);
            // 列表数据
            for (var i = 0; i < $scope.dataList[0].length; i++) {
                var trdata = {
                    lookTime: '',
                    data: []
                };
                trdata.lookTime = $scope.item[i];
                for (var j = 0; j < $scope.dataList.length; j++) {
                    trdata.data.push($scope.dataList[j][i].dailyUploads);
                }
                $scope.totalData.push(trdata);
            }
            // 第一页数据
            $scope.showData = $scope.totalData.slice(0, 15);
            $scope.setPage();
        };

        // 查看下载量
        $scope.lookdownloads = function () {
            $('#lookdownloads').addClass('am-btn-primary');
            $('#lookdownloads').siblings().removeClass('am-btn-primary');
            $scope.option.title.text = "下载量折线趋势图:";
            $scope.totalData = [];
            $scope.option.series = [];
            $scope.option.legend.data = [];
            // series数据
            for (var i = 0; i < $scope.dataList.length; i++) {
                var seri = {
                    name: '',
                    type: 'line',
                    showSymbol: true,
                    data: []
                };
                $scope.option.series.push(seri);
            }
            for (var i = 0; i < $scope.option.series.length; i++) {
                var dailyReport = $scope.dataList[i];
                $scope.option.legend.data.push(dailyReport[0].categoryName);
                $scope.option.series[i].name = dailyReport[0].categoryName;
                $scope.option.series[i].data = [];
                dailyReport.forEach(function (report) {
                    $scope.option.series[i].data.push(report.dailyDownloads);
                });
            }
            mychart.setOption($scope.option);
            // 列表数据
            for (var i = 0; i < $scope.dataList[0].length; i++) {
                var trdata = {
                    lookTime: '',
                    data: []
                };
                trdata.lookTime = $scope.item[i];
                for (var j = 0; j < $scope.dataList.length; j++) {
                    trdata.data.push($scope.dataList[j][i].dailyDownloads);
                }
                $scope.totalData.push(trdata);
            }
            // 第一页数据
            $scope.showData = $scope.totalData.slice(0, 15);
            $scope.setPage();
        };

    }]);

});