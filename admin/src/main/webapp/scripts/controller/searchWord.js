define(['app', 'directive/ng-pager'], function (app) {
    'use strict';

    app.controller('searchWordListCtrl', ['$scope', '$rootScope', '$http', function ($scope, $rootScope, $http) {
        if (!$rootScope.searchWordParams) {
            $rootScope.searchWordParams = {};
        }
        $scope.searchKey = $rootScope.searchWordParams.content || '';
        $scope.params = {
            content: $rootScope.searchWordParams.content || null,
            page: $rootScope.searchWordParams.page || 1,
            size: 20
        };
        // 获取数据
        $scope.getData = function () {
            $http.get('admin/searchword', {params: $scope.params}).success(function (data) {
                $scope.choseArr=[""];
                flag='';//是否点击了全选，是为a
                $scope.isChecked=false;//默认未选中
                $scope.data = data;
                $scope.params.page = data.number + 1;
                $rootScope.searchWordParams.page = $scope.params.page;
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
            $scope.params.content = $scope.searchKey;
            $rootScope.searchWordParams.content = $scope.searchKey;
            $scope.params.page = 1;
            $scope.getData();
        };

        // 获取初始数据
        $scope.getData();

        $scope.getPage = function (page) {
            $scope.params.page = page;
            $scope.getData();
        }

        // 批量清零
        $scope.doBatchClearZero = function () {

            //alert($scope.choseArr);
            if (confirm('确定要批量清零吗？')) {
                $http.get('admin/searchword/doBatchClearZero?idsStr='+$scope.choseArr, {params: $scope.params}).success(function (data) {

                    $scope.choseArr=[""];
                    flag='';//是否点击了全选，是为a
                    $scope.isChecked=false;//默认未选中

                    $scope.getData();


                })

            }
        };


        // 单个清零
        $scope.doOneClearZero = function (a) {
            if (confirm('确定要清零【' + a.name + '】吗？')) {
                $http.get('admin/searchword/doOneClearZero/' + a.id).success(function () {

                    $scope.getData();


                }).error(function (data) {
                    console.log(data);

                    alert('修改失败！');


                });
            }
        };


        //复选框js处理
        $scope.choseArr=[""];//定义数组用于存放前端显示
        var str="";//
        var flag='';//是否点击了全选，是为a
        $scope.isChecked=false;//默认未选中

        $scope.all= function (isCheckedAll,seacherWordlist) {//全选

            if(isCheckedAll==false){
                //alert("取消全选");
                $scope.isChecked=false;
                $scope.choseArr=[""];
                //flag='';

            }else{
                //alert("全选");
                $scope.isChecked=true;
                for(var i=0;i<seacherWordlist.length;i++){
                    $scope.choseArr[i]=''+seacherWordlist[i].id+'';
                }
                flag='a';

            }

            //flag='a';
            console.log($scope.choseArr);
            //alert($scope.choseArr);

        };

        $scope.chk= function (id,isChecked) {//单选或者多选

            if(flag=='a') {//在全选的基础上操作
                str = $scope.choseArr.join(',') + ',';
            }
            if (isChecked == true) {//选中
                str = str + id + ',';
            } else {
                str = str.replace(id + ',', '');//取消选中
            }

            $scope.choseArr=(str.substr(0,str.length-1)).split(',');
            console.log($scope.choseArr);
            //alert($scope.choseArr);

        };





    }])
});