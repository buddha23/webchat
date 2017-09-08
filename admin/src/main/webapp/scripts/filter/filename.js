define(['app'], function (app) {
    'use strict';

    app.filter("filename", function () {
        return function (path, hasExt) {
            if (path && typeof path === 'string') {
                var match = path.match(hasExt ? /([^\/]+\.\w+$)/ : /([^\/]+)\.\w+$/);
                if (match) {
                    return match[1];
                }
            }
            return '';
        };
    });
});