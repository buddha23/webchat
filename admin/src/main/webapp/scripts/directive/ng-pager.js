define(['app'], function (app) {
    'use strict';

    app.directive('ngPager', function () {
        return {
            restrict: 'E',
            replace: true,
            templateUrl: 'scripts/directive/views/ng-pager.html',
            scope: {
                pageChange: '&',
                pageData: '='
            },
            link: function (scope, element, attributes) {
                var items = parseInt(attributes.items) || 9;
                scope.onPageChange = function () {
                    var mid = parseInt(items / 2);
                    var min = 0, max = scope.pageData.totalPages;
                    var number = scope.pageData.number;
                    if (scope.pageData.totalPages > items) {
                        if (number > mid) {
                            min = number - mid;
                            max = min + items;
                            if (scope.pageData.totalPages - number < mid) {
                                min = scope.pageData.totalPages - items;
                                max = scope.pageData.totalPages;
                            }
                        } else {
                            max = Math.min(items, scope.pageData.totalPages);
                        }
                    }
                    scope.pages = [];
                    if (max == 0)
                        scope.pages.push(0);
                    else
                        for (var i = min; i < max; i++) {
                            scope.pages.push(i);
                        }
                };
                scope.$watch('pageData.number', function (n, o) {
                    if (scope.pageData)
                        scope.onPageChange();
                });
            }
        }
    });
});