define(['app'], function (app) {
    'use strict';

    app.directive("ngFile", function () {
        return {
            scope: {
                ngFile: '=',
                filechange: '&'
            },
            link: function (scope, element, attributes) {
                element.bind("change", function (changeEvent) {
                    scope.ngFile = changeEvent.target.files[0];
                    scope.$apply(scope.filechange);
                });
            }
        }
    });
});