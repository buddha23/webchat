define(['app'], function (app) {
    'use strict';

    app.filter("filesize", function () {
        var sizes = ['KB', 'MB', 'GB', 'TB'];
        var fun = function (size, i) {
            i = i || 0;
            if (!isNaN(size)) {
                if (size < 1024 || i === 3) {
                    return parseFloat(size) + sizes[i];
                } else {
                    return fun((size / 1024).toFixed(2), ++i);
                }
            }
            return size;
        };
        return fun;
    });
});