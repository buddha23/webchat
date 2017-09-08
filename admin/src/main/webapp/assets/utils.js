(function () {
    Array.prototype.removeChild = function (c) {
        var idx = this.indexOf(c);
        if (idx > -1) {
            this.splice(idx, 1);
        }
    };
})();