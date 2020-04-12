var TreeView = (function () {
  var TreeView = function (options) {
    var setting = {
      el: '', // elelemt ID
      checkedEl: '', // checkedIds elelemt ID  必须设置iscCheckedEl=true有效
      iscCheckedEl: false, //是否记录选中节点数据id  默认为false
      ajaxParam: {  // ajax参数
        data: {},
        dataType: 'json',
        type: 'POST'
      },
      isSetCheckedIds: true, //是否执行该方法
      formatData: null, // 格式化数据
      treeParam: {  // ztree参数
        check: {
          enable: false,
          autoCheckTrigger: false, //是否自动关联勾选
          chkStyle:"Disable",    //复选框checkbox     'radio'单选框
        },
        view: {
          // showIcon: true,
          // selectedMulti: false,
            //定制树的显示内容
          // addDiyDom: function (treeId, treeNode) {
          //   var aObj = $("#" + treeNode.tId + "_a");
          //
          //     // var editStr = "<p>" + treeNode["name"].split("|")[0] + "</p>";
          //     var editStr = "<p>" + treeNode["name"] + "</p>";
          //     aObj.html(editStr);
          // }
        },
        data: {
          simpleData: {
            enable: true,
            idKey: 'id',
            pIdKey: 'pId'
          },
          key: {
              name:"name",		//默认值name ,与节点的属性name对应,节点显示的名称
              title:"name",    		//setting.view.showTitile=true时生效,默认值"",当设置为""时,与上方name保持一致。可设置为对应的节点其他属性值。
          }
        },
        callback: {
          //用于捕获节点被折叠的事件回调函数
          onCollapse: function (event, treeId, treeNode) {
            if (parent.initDetailFrameHeight != undefined) {
              $('body').trigger('resetFrame');
            }
              $('body').trigger('resetFrame');
          },
          //用于捕获节点被展开的事件回调函数
          onExpand: function (event, treeId, treeNode) {
            if (parent.initHeight != undefined) {
              $('body').trigger('resetFrame');
            }
              $('body').trigger('resetFrame');
          },
          beforeClick: function (treeId, treeNode, clickFlag) {
            return false;
          },
          onCheck: this.saveCheckedIds.bind(this)
        }
      },
      callback: null,
      allCallBack:null
    },
      opts = $.extend(true, setting, options);
    this.options = opts;
    this.isTreeEvent = false;
    this.init();
  }

  TreeView.prototype = {
    init: function () {
      this.fetchData();
    },
    fetchData: function () {
      var self = this,
          options = self.options,
        ajaxParam = self.options.ajaxParam;

      $.getJSON("data/tree.json", {}, function (data) {
        self.initTreeView(data);
      });
      //$.ajax(ajaxParam).done(function (result) {
      //  if (!result.isError) {
      //    self.initTreeView(result.message);
      //    if(options.isSetCheckedIds){
      //      self.setCheckedIds();
      //    }
      //  } else {
      //    console.warn("ZTREE 数据获取失败！")
      //  }
      //  options.allCallBack && options.allCallBack();
      //}).fail(function () {
      //  alert('网络繁忙请稍候再试');
      //});
    },
    initTreeView: function (data) {
      var self = this,
        options = self.options,
        el = options.el,
        setting = options.treeParam;

      options.formatData && (data = options.formatData(data));

      // 初始化ztree
      $.fn.zTree.init($("#" + el), setting, data);

      options.callback && options.callback();

      if (parent.initHeight != undefined) {
        parent.initHeight();
      }
    },
    //清空选中状态
    removeChecked: function () {
      var self = this,
        options = self.options,
        el = options.el,
        checkedEl = options.checkedEl,
        zTreeObj = $.fn.zTree.getZTreeObj(el),
        nodes = zTreeObj.getCheckedNodes(true);
      for (var i in nodes) {
        zTreeObj.checkNode(nodes[i], false, true);
      }
      if(options.iscCheckedEl && checkedEl){
        $("#" + checkedEl).val('');
      }
    },
    //保存选择状态
    saveCheckedIds: function () {
      var self = this,
        options = self.options,
        el = options.el,
        checkedEl = options.checkedEl,
        zTreeObj = $.fn.zTree.getZTreeObj(el),
        nodes = zTreeObj.getCheckedNodes(true);
      var params = new Array();
      for (var i in nodes) {
        if (!nodes[i].children && nodes[i].id) {
          params.push(parseInt(nodes[i].id))
        }
      }
      params = unique(params);
      if(options.iscCheckedEl && checkedEl){
        $("#" + checkedEl).val(params.join(";"));
      }
    },
    getCheckedIds: function () {
      var self = this,
        options = self.options,
        checkedEl = options.checkedEl;

      if(options.iscCheckedEl && checkedEl){
        return $("#" + checkedEl).val();
      }else{
        return "iscCheckedEl is false";
      }
    },
    // 设置选中状态
    setCheckedIds: function () {
      var self = this,
        options = self.options,
        el = options.el,
        checkedEl = options.checkedEl,
        treeObj = $.fn.zTree.getZTreeObj(el),
        checkedIds = '';


      if(options.iscCheckedEl && checkedEl){
           checkedIds = $("#" + checkedEl).val();
        }

      if (checkedIds == "") { return false; }
      checkedIds = checkedIds.split(';');
      $.each(checkedIds, function (index, item) {
        var node = treeObj.getNodeByParam('id', item);
        if (node) {
          treeObj.checkNode(node, true, true);
        }
      });
    }
  };
  return TreeView;

  function unique(array) {
    var temp = []; //一个新的临时数组
    for (var i = 0; i < array.length; i++) {
      if (temp.indexOf(array[i]) == -1) {
        temp.push(array[i]);
      }
    }
    return temp;
  }
})();