!function(self) {
  /** @type {HTMLDocument} */
  var doc = document;
  /** @type {string} */
  var key = "querySelectorAll";
  /** @type {string} */
  var byClass = "getElementsByClassName";
  /**
   * @param {string} id
   * @return {?}
   */
  var $ = function(id) {
    return doc[key](id);
  };
  var cfg = {
    type : 0,
    shade : true,
    shadeClose : true,
    fixed : true,
    anim : "scale"
  };
  var data = {
    /**
     * @param {Object} b
     * @return {?}
     */
    extend : function(b) {
      /** @type {*} */
      var a = JSON.parse(JSON.stringify(cfg));
      var prop;
      for (prop in b) {
        a[prop] = b[prop];
      }
      return a;
    },
    timer : {},
    end : {}
  };
  /**
   * @param {HTMLElement} node
   * @param {Function} callback
   * @return {undefined}
   */
  data.touch = function(node, callback) {
    node.addEventListener("click", function(operation) {
      callback.call(this, operation);
    }, false);
  };
  /** @type {number} */
  var idx = 0;
  /** @type {Array} */
  var m = ["layui-m-layer"];
  /**
   * @param {Object} n
   * @return {undefined}
   */
  var f = function(n) {
    var state = this;
    state.config = data.extend(n);
    state.view();
  };
  /**
   * @return {undefined}
   */
  f.prototype.view = function() {
    var params = this;
    var options = params.config;
    /** @type {Element} */
    var text = doc.createElement("div");
    params.id = text.id = m[0] + idx;
    text.setAttribute("class", m[0] + " " + m[0] + (options.type || 0));
    text.setAttribute("index", idx);
    var g = function() {
      /** @type {boolean} */
      var isPost = "object" == typeof options.title;
      return options.title ? '<h3 style="' + (isPost ? options.title[1] : "") + '">' + (isPost ? options.title[0] : options.title) + "</h3>" : "";
    }();
    var j = function() {
      if ("string" == typeof options.btn) {
        /** @type {Array} */
        options.btn = [options.btn];
      }
      var regexString;
      var cnl = (options.btn || []).length;
      return 0 !== cnl && options.btn ? (regexString = '<span yes type="1">' + options.btn[0] + "</span>", 2 === cnl && (regexString = '<span no type="0">' + options.btn[1] + "</span>" + regexString), '<div class="layui-m-layerbtn">' + regexString + "</div>") : "";
    }();
    if (options.fixed || (options.top = options.hasOwnProperty("top") ? options.top : 100, options.style = options.style || "", options.style += " top:" + (doc.body.scrollTop + options.top) + "px"), 2 === options.type && (options.content = '<i></i><i class="layui-m-layerload"></i><i></i><p>' + (options.content || "") + "</p>"), options.skin && (options.anim = "up"), "msg" === options.skin && (options.shade = false), text.innerHTML = (options.shade ? "<div " + ("string" == typeof options.shade ? 'style="' + 
    options.shade + '"' : "") + ' class="layui-m-layershade"></div>' : "") + '<div class="layui-m-layermain" ' + (options.fixed ? "" : 'style="position:static;"') + '><div class="layui-m-layersection"><div class="layui-m-layerchild ' + (options.skin ? "layui-m-layer-" + options.skin + " " : "") + (options.className ? options.className : "") + " " + (options.anim ? "layui-m-anim-" + options.anim : "") + '" ' + (options.style ? 'style="' + options.style + '"' : "") + ">" + g + '<div class="layui-m-layercont">' + 
    options.content + "</div>" + j + "</div></div></div>", !options.type || 2 === options.type) {
      var p = doc[byClass](m[0] + options.type);
      var pl = p.length;
      if (pl >= 1) {
        layer.close(p[0].getAttribute("index"));
      }
    }
    document.body.appendChild(text);
    var content = params.elem = $("#" + params.id)[0];
    if (options.success) {
      options.success(content);
    }
    /** @type {number} */
    params.index = idx++;
    params.action(options, content);
  };
  /**
   * @param {Object} options
   * @param {Object} doc
   * @return {undefined}
   */
  f.prototype.action = function(options, doc) {
    var self = this;
    if (options.time) {
      /** @type {number} */
      data.timer[self.index] = setTimeout(function() {
        layer.close(self.index);
      }, 1E3 * options.time);
    }
    /**
     * @return {undefined}
     */
    var next = function() {
      var ret = this.getAttribute("type");
      if (0 == ret) {
        if (options.no) {
          options.no();
        }
        layer.close(self.index);
      } else {
        if (options.yes) {
          options.yes(self.index);
        } else {
          layer.close(self.index);
        }
      }
    };
    if (options.btn) {
      var arr = doc[byClass]("layui-m-layerbtn")[0].children;
      var l = arr.length;
      /** @type {number} */
      var i = 0;
      for (;l > i;i++) {
        data.touch(arr[i], next);
      }
    }
    if (options.shade && options.shadeClose) {
      var node = doc[byClass]("layui-m-layershade")[0];
      data.touch(node, function() {
        layer.close(self.index, options.end);
      });
    }
    if (options.end) {
      data.end[self.index] = options.end;
    }
  };
  self.layer = {
    v : "2.0",
    index : idx,
    /**
     * @param {Object} opt_async
     * @return {?}
     */
    open : function(opt_async) {
      var o = new f(opt_async || {});
      return o.index;
    },
    /**
     * @param {?} i
     * @return {undefined}
     */
    close : function(i) {
      var bodyElem = $("#" + m[0] + i)[0];
      if (bodyElem) {
        /** @type {string} */
        bodyElem.innerHTML = "";
        doc.body.removeChild(bodyElem);
        clearTimeout(data.timer[i]);
        delete data.timer[i];
        if ("function" == typeof data.end[i]) {
          data.end[i]();
        }
        delete data.end[i];
      }
    },
    /**
     * @return {undefined}
     */
    closeAll : function() {
      var results = doc[byClass](m[0]);
      /** @type {number} */
      var maxLength = 0;
      var iLength = results.length;
      for (;iLength > maxLength;maxLength++) {
        layer.close(0 | results[0].getAttribute("index"));
      }
    }
  };
  if ("function" == typeof define) {
    define(function() {
      return layer;
    });
  } else {
    (function() {
      var scripts = document.scripts;
      var currentScript = scripts[scripts.length - 1];
      var src = currentScript.src;
      var cssPath = src.substring(0, src.lastIndexOf("/") + 1);
      if (!currentScript.getAttribute("merge")) {
        document.head.appendChild(function() {
          /** @type {Element} */
          var link = doc.createElement("link");
          return link.href = cssPath + "layer.css?2.0", link.type = "text/css", link.rel = "styleSheet", link.id = "layermcss", link;
        }());
      }
    })();
  }
}(window);
