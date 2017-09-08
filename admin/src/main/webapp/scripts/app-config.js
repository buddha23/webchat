define(['app', 'layer'], function (app, layer) {
    'use strict';

    app.config(['$stateProvider', '$urlRouterProvider', '$httpProvider', function ($stateProvider, $urlRouterProvider, $httpProvider) {

        $httpProvider.interceptors.push(function ($q) {
            return {
                'responseError': function (rejection) {
                    if (rejection.status === 401) {
                        location.href = "login.html";
                    } else if (rejection.status === 403) {
                        alert('没有权限操作');
                        history.back();
                        return $q.reject(rejection);
                    } else {
                        return $q.reject(rejection);
                    }
                }
            };
        });

        $urlRouterProvider.otherwise('/index');
        $stateProvider.state('index', {
            url: '/index',
            templateUrl: 'scripts/controller/views/index.html'
        }).state('admins', {       // adminList
            url: '/adminList',
            templateUrl: 'scripts/controller/views/adminList.html',
            controllerUrl: 'controller/admin'
        }).state('adminDetail', {       // 添加admin
            url: '/admin/:id',
            templateUrl: 'scripts/controller/views/adminDetail.html',
            controllerUrl: 'controller/admin'
        }).state('roles', {       // 角色role
            url: '/roleList',
            templateUrl: 'scripts/controller/views/adminRoleList.html',
            controllerUrl: 'controller/role'
        }).state('roleDetail', {       // 角色role
            url: '/role/:id',
            templateUrl: 'scripts/controller/views/adminRoleDetail.html',
            controllerUrl: 'controller/role'
        }).state('roleUpdate', {       // 角色role
            url: '/roleUpdate/:id',
            templateUrl: 'scripts/controller/views/adminRoleUpdate.html',
            controllerUrl: 'controller/role'
        }).state('permission', {       // 权限
            url: '/permission',
            templateUrl: 'scripts/controller/views/adminPermission.html',
            controllerUrl: 'controller/role'
        }).state('operateRecord', {       // 权限
            url: '/operateRecord',
            templateUrl: 'scripts/controller/views/adminOperateRecord.html',
            controllerUrl: 'controller/role'
        }).state('articleDownload', {       // 文章
            url: '/articles/download',
            templateUrl: 'scripts/controller/views/articleDownload.html',
            controllerUrl: 'controller/article'
        }).state('articleList', {
            url: '/articles',
            templateUrl: 'scripts/controller/views/articleList.html',
            controllerUrl: 'controller/article'
        }).state('articleDetail', {
            url: '/articles/:id',
            templateUrl: 'scripts/controller/views/articleDetail.html',
            controllerUrl: 'controller/article'
        }).state('docList', {               // 文库
            url: '/doc',
            templateUrl: 'scripts/controller/views/docList.html',
            controllerUrl: 'controller/doc'
        }).state('docDetail', {
            url: '/doc/:id',
            templateUrl: 'scripts/controller/views/docDetail.html',
            controllerUrl: 'controller/doc'
        }).state('docTagList', {
            url: '/doctag',
            templateUrl: 'scripts/controller/views/docTagList.html',
            controllerUrl: 'controller/docTag'
        }).state('docTagDetail', {
            url: '/doctag/:id',
            templateUrl: 'scripts/controller/views/docTagDetail.html',
            controllerUrl: 'controller/docTag'
        }).state('docCategory', {
            url: '/docCategory',
            templateUrl: 'scripts/controller/views/docCategory.html',
            controllerUrl: 'controller/docCategory'
        }).state('softList', {
            url: '/soft',
            templateUrl: 'scripts/controller/views/softList.html',
            controllerUrl: 'controller/soft'
        }).state('softDetail', {
            url: '/soft/:id',
            templateUrl: 'scripts/controller/views/softDetail.html',
            controllerUrl: 'controller/soft'
        }).state('softCategory', {
            url: '/softCategory',
            templateUrl: 'scripts/controller/views/softCategory.html',
            controllerUrl: 'controller/softCategory'
        }).state('moderatorsList', {        //版主相关
            url: '/moderatorsList',
            templateUrl: 'scripts/controller/views/moderatorsList.html',
            controllerUrl: 'controller/moderatorsManage'
        }).state('moderatorsManage', {
            url: '/moderatorsManage',
            templateUrl: 'scripts/controller/views/moderatorsManage.html',
            controllerUrl: 'controller/moderatorsManage'
        }).state('moderatorsLog', {
            url: '/moderatorsLog/{id}',
            templateUrl: 'scripts/controller/views/moderatorsLog.html',
            controllerUrl: 'controller/moderatorsManage'
        }).state('moderatorScoreHistory', {
            url: '/moderatorScoreHistory',
            templateUrl: 'scripts/controller/views/moderatorScoreHistory.html',
            controllerUrl: 'controller/moderatorScoreHistory'
        }).state('userList', {              // 用户
            url: '/user',
            templateUrl: 'scripts/controller/views/userList.html',
            controllerUrl: 'controller/user'
        }).state('userMemberList', {              // 会员
            url: '/userMember',
            templateUrl: 'scripts/controller/views/userMemberList.html',
            controllerUrl: 'controller/userMember'
        }).state('userDetail', {
            url: '/user/:id',
            templateUrl: 'scripts/controller/views/userDetail.html',
            controllerUrl: 'controller/user'
        }).state('userParterList', {              // 用户合伙人
            url: '/userParter',
            templateUrl: 'scripts/controller/views/userParterList.html',
            controllerUrl: 'controller/userParter'
        }).state('userScoresList', {
            url: '/user/:id/scores',
            templateUrl: 'scripts/controller/views/userScoresList.html',
            controllerUrl: 'controller/user'
        }).state('userPointHistory', {              // 用户积分历史
            url: '/userPointHistory/:id',
            templateUrl: 'scripts/controller/views/userPointHistory.html',
            controllerUrl: 'controller/user'
        }).state('userSuggest', {              // 用户反馈
            url: '/userSuggest',
            templateUrl: 'scripts/controller/views/userSuggest.html',
            controllerUrl: 'controller/userSuggest'
        }).state('userSuggestDetail', {
            url: '/userSuggest/:id',
            templateUrl: 'scripts/controller/views/userSuggestDetail.html',
            controllerUrl: 'controller/userSuggest'
        }).state('wxqaList', {              // 微信
            url: '/wxqa',
            templateUrl: 'scripts/controller/views/wxqaList.html',
            controllerUrl: 'controller/wxQa'
        }).state('wxqaDetail', {
            url: '/wxqa/:id',
            templateUrl: 'scripts/controller/views/wxqaDetail.html',
            controllerUrl: 'controller/wxQa'
        }).state('wxHelpage', {
            url: '/wxhelpage',
            templateUrl: 'scripts/controller/views/wxHelpage.html',
            controllerUrl: 'controller/wxHelpage'
        }).state('wxMenu', {
            url: '/wxmenu',
            templateUrl: 'scripts/controller/views/wxMenu.html',
            controllerUrl: 'controller/wxMenu'
        }).state('userWithdraw', {
            url: '/withdraw',
            templateUrl: 'scripts/controller/views/userWithdraw.html',
            controllerUrl: 'controller/userWithdraw'
        }).state('wxArticle', {
            url: '/wxarticle',
            templateUrl: 'scripts/controller/views/wxArticle.html',
            controllerUrl: 'controller/wxArticle'
        }).state('searchWord', {              // 报表
            url: '/searchword',
            templateUrl: 'scripts/controller/views/searchWordList.html',
            controllerUrl: 'controller/searchWord'
        }).state('wxGzh', {              // 微信公众号
            url: '/gzh',
            templateUrl: 'scripts/controller/views/wxGzhList.html',
            controllerUrl: 'controller/wxGzh'
        }).state('wxGzhEntry', {
            url: '/gzhentry',
            templateUrl: 'scripts/controller/views/wxGzhEntry.html',
            controllerUrl: 'controller/wxGzhEntry'
        }).state('docReport', {
            url: '/docreport',
            templateUrl: 'scripts/controller/views/reportDocList.html',
            controllerUrl: 'controller/docReport'
        }).state('monthReport', {
            url: '/monthreport',
            templateUrl: 'scripts/controller/views/monthReportList.html',
            controllerUrl: 'controller/monthReport'
        }).state('userReport', {
            url: '/userreport',
            templateUrl: 'scripts/controller/views/reportUserList.html',
            controllerUrl: 'controller/userReport'
        }).state('loginreport', {           //  登录&注册折线报表
            url: '/loginreport',
            templateUrl: 'scripts/controller/views/reportLogin.html',
            controllerUrl: 'controller/reportLogin'
        }).state('loginHistory', {          //  用户登录历史列表
            url: '/loginHistory',
            templateUrl: 'scripts/controller/views/reportLoginHistory.html',
            controllerUrl: 'controller/loginHistory'
        }).state('postsreport', {          //  贴吧折线报表
            url: '/postsreport',
            templateUrl: 'scripts/controller/views/reportPosts.html',
            controllerUrl: 'controller/reportPosts'
        }).state('categoryreport', {          //  板块折线报表
            url: '/categoryreport',
            templateUrl: 'scripts/controller/views/reportCategory.html',
            controllerUrl: 'controller/reportCategory'
        }).state('dailyActiveReport', {          //  日活跃人数折线
            url: '/dailyActiveReport',
            templateUrl: 'scripts/controller/views/reportDailyActive.html',
            controllerUrl: 'controller/reportDailyActive'
        }).state('setting', {
            url: '/setting',
            templateUrl: 'scripts/controller/views/setting.html',
            controllerUrl: 'controller/setting'
        }).state('docDetailFooter', {
            url: '/docDetailFooter',
            templateUrl: 'scripts/controller/views/docDetailFooter.html',
            controllerUrl: 'controller/docDetailFooter'
        }).state('docDetailHeader', {
            url: '/docDetailHeader',
            templateUrl: 'scripts/controller/views/docDetailHeader.html',
            controllerUrl: 'controller/docDetailHeader'
        }).state('imgSetting', {
            url: '/imgSetting',
            templateUrl: 'scripts/controller/views/indexImgSetting.html',
            controllerUrl: 'controller/indexImgSetting'
        }).state('advertiseImg', {
            url: '/advertiseImg',
            templateUrl: 'scripts/controller/views/advertiseImgSetting.html',
            controllerUrl: 'controller/advertiseImgSetting'
        }).state('wapIndexImg', {
            url: '/wapIndexImg',
            templateUrl: 'scripts/controller/views/wapIndexImgs.html',
            controllerUrl: 'controller/wapIndexImg'
        }).state('commodityImg', {
            url: '/commodityImg',
            templateUrl: 'scripts/controller/views/commodityImg.html',
            controllerUrl: 'controller/commodityImg'
        }).state('courseList', {               // 精品教程
            url: '/course',
            templateUrl: 'scripts/controller/views/courseList.html',
            controllerUrl: 'controller/course'
        }).state('courseDetail', {
            url: '/course/:id',
            templateUrl: 'scripts/controller/views/courseDetail.html',
            controllerUrl: 'controller/course'
        }).state('postsList', {               // 帖子
            url: '/posts',
            templateUrl: 'scripts/controller/views/postsList.html',
            controllerUrl: 'controller/posts'
        }).state('postsDetail', {
            url: '/posts/:id',
            templateUrl: 'scripts/controller/views/postsDetail.html',
            controllerUrl: 'controller/posts'
        }).state('postsCategory', {               // 帖子
            url: '/postsCategory',
            templateUrl: 'scripts/controller/views/postsCategory.html',
            controllerUrl: 'controller/postsCategory'
        }).state('copyWritingList', {               // 产品文案
            url: '/copyWritingList',
            templateUrl: 'scripts/controller/views/copyWritingList.html',
            controllerUrl: 'controller/copyWriting'
        }).state('copyWritingDetail', {
            url: '/copyWritingDetail/:id',
            templateUrl: 'scripts/controller/views/copyWritingDetail.html',
            controllerUrl: 'controller/copyWriting'
        }).state('systemLogList', {
            url: '/systemLogList',
            templateUrl: 'scripts/controller/views/systemLogList.html',
            controllerUrl: 'controller/systemLog'
        }).state('systemLogDetail', {
            url: '/systemLog/:id',
            templateUrl: 'scripts/controller/views/systemLogDetail.html',
            controllerUrl: 'controller/systemLog'
        }).state('userScorePay', {
            url: '/userScorePay',
            templateUrl: 'scripts/controller/views/userScorePay.html',
            controllerUrl: 'controller/userScorePay'
        }).state('volumeList', {
            url: '/volumeList',
            templateUrl: 'scripts/controller/views/volumeList.html',
            controllerUrl: 'controller/volume'
        }).state('volumeDetail', {
            url: '/volume/:id',
            templateUrl: 'scripts/controller/views/volumeDetail.html',
            controllerUrl: 'controller/volumeDetail'
        }).state('vodCategory', {
            url: '/vodCategory',
            templateUrl: 'scripts/controller/views/vodCategory.html',
            controllerUrl: 'controller/vodCategory'
        }).state('volumeInviteCards', {
            url: '/volumeInviteCards',
            templateUrl: 'scripts/controller/views/volumeInviteCards.html',
            controllerUrl: 'controller/volumeInviteCards'
        }).state('volumeBuyList', {
            url: '/volumeBuyList',
            templateUrl: 'scripts/controller/views/volumeBuyList.html',
            controllerUrl: 'controller/volumeBuyList'
        }).state('wxfwUserDown', {      //微信服务号users
            url: '/wxfwUserDown',
            templateUrl: 'scripts/controller/views/wxfwUserDown.html',
            controllerUrl: 'controller/wxfwUser'
        }).state('commodity', {      //微信服务号users
            url: '/commodity',
            templateUrl: 'scripts/controller/views/commodityList.html',
            controllerUrl: 'controller/commodity'
        }).state('commodityDetail', {
            url: '/commodity/:id',
            templateUrl: 'scripts/controller/views/commodityDetail.html',
            controllerUrl: 'controller/commodity'
        }).state('commodityCategory', {
            url: '/commodityCategory',
            templateUrl: 'scripts/controller/views/commodityCategory.html',
            controllerUrl: 'controller/commodityCategory'
        }).state('enterprise',{
        	url:'/enterprise',
        	templateUrl: 'scripts/controller/views/enterpriseList.html',
            controllerUrl: 'controller/enterprise'
        });
    }
    ]).run(['$rootScope', '$http', '$state', '$stateParams', function ($rootScope, $http, $state, $stateParams) {

        $http.get('i/config').success(function (config) {
            angular.extend($rootScope, config);
        });

        $http.get('admin/login').success(function (admin) {
            $rootScope.admin = admin;
            $rootScope.admin.isRoot = (admin.adminRole.id === 1);
            $rootScope.adminPermissions = admin.adminRole.permissions;
        });

        $rootScope.$state = $state;
        $rootScope.$stateParams = $stateParams;

        $rootScope.logout = function () {
            $http.get('admin/logout').success(function () {
                location.href = "login.html";
            }).error(function () {
                location.href = "login.html";
            })
        };

        $rootScope.updatePwd = function () {
            var cha = layer.open({
                type: 1,
                title: '修改密码',
                area: '400px',
                btn: ['确认', '取消'],
                content: '<div class="am-margin-left am-margin-top">输入密码：<input type = "password" class="changePwd" onkeyup= "value=value.replace(/[\\W]/g,\'\')"/></div>',
                yes: function (index) {
                    var password = $('.changePwd').val();
                    var admin = {
                        rePassword: password
                    };
                    if (!/^\w{6,20}$/.test(password)) {
                        layer.msg('错误的密码');
                    } else {
                        //$http.post('admin/changePwd', password)
                        $http.post('admin/changePwd', admin)
                            .success(function () {
                                layer.close(cha);
                                layer.msg('修改成功!');
                            }).error(function (r) {
                            layer.close(cha);
                            console.log(r);
                            layer.msg('修改密码失败~')
                        });
                    }
                }
            });
        };

    }]);
});