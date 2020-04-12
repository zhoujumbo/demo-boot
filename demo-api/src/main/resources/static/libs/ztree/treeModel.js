;(function(){
	'use strict';
	var ZtreeModal = function(el,opts){
		var options = {

		};
		this.element = el;
		this.opts = opts?opts:options;
		this.dblFlage = true; // 控制双击重复点击
		this.ztreemodal;
		this.topTreeNode = null;
		this.tree = null;
	};

	ZtreeModal.prototype.version = "1.0.1";

	ZtreeModal.prototype.init = function(){

		buildTree.call(this);
		// 绑定事件
		bindEvents.call(this);
	};// init

	function bindEvents (){
		var _this = this,
			_el = this.element,
			_opt = this.opts;

		$("body").bind(
			//鼠标点击事件不在节点上时隐藏右键菜单
			"mousedown",
			function(event) {
				//if (!(event.target.id == "rMenu" || $(event.target)
				//		.parents("#rMenu").length > 0)) {
				//	$("#rMenu").hide();
				//}
			});

	};
	/**
	 * 构造树
	 */
	function buildTree(){
		var _this = this,
			_el = this.element,
			_opt = this.opts;

		_this.topTreeNode = [{
			id: '0',							//可自定义的key,用于保存自定义属性到该节点
			name: _opt.topNode.name?_opt.topNode.name:'ZTREE',		                //一般的key,一般用于保存节点名称
			// isParent: true,				    	//具有特殊意义的key,true表示该节点为父节点,false表示为子节点,每个节点都应该赋予该属性resources/popzTree/img/org.gif
			// isLeaf: false,						//具有特殊意义的key,节点锁,若为true,所有isParent=false的节点都不能添加子节点
			icon: _opt.topNode.ico?_opt.topNode.ico:'back/img/organization.png',           //具有特殊意义的key,用于定义节点前方的特殊图标路径
			open:true,						//具有特殊意义的key,true默认该节点展开
			pId: '',				     	//可自定义的key,用于保存自定义属性到该节点
			isHidden: false,
			// nocheck: '',
		}];

		_this.tree = {
			_zTree:'',           //随意定义的属性,可不定义,可用于后方callback属性设置
			_thisNode:'',		  //随意定义的属性,可不定义,可用于后方callback属性设置
			setting: {
				async: {
					enable: true,      //启用异步加载
					dataType:"json",	//ajax获取的数据类型,默认text  可是指为json,  enable = true时生效
					contentType:"application/json", //ajax提交参数的数据类型 ,可为application/x-www-form-urlencoded或application/json  (满足.net)
					// autoParam:["id"],   //每次加载时自动提交的参数,这个参数对应 各个节点对象(例如_topTreeNode)的'id'属性
					dataFilter:ajaxDataFilter.bind(_this),  //异步加载返回函数预处理,如果后台没处理好,可以在前端处理
					type:"post",        //ajax  http请求模式,可为get
					url: _opt.url,	//请求的action ,可在后面跟?传参     get方法  user
				},														//
				data: {
					simpleData: {
						enable: true,
						idKey: 'id',
						pIdKey: 'pId',
						//rootPid:'org0'
					},
					key: {
						name:"name",		//默认值name ,与节点的属性name对应,节点显示的名称
						title:"name",    		//setting.view.showTitile=true时生效,默认值"",当设置为""时,与上方name保持一致。可设置为对应的节点其他属性值。
					}
				},
				check: {
					enable:true,			//设置是否为单选框或者多选框
					autoCheckTrigger: true,
					// chkStyle:"checkbox",    //复选框     'radio'单选框
					// chkboxType:{ "Y": "ps", "N": "ps" },

				},
				view: {
					autoCancleSelected: true,   //按下Ctrl或者Cmd键 ，点击节点时，取消该节点的选取操作
					dblClickExpand: false,      //默认值为true,当为true时，双击自动展开或者收缩节点 ...若需要单击实现展开收缩,请使用callback的单击事件onClick
					//expandSpeed:"fast",    	//展开收缩速度  fast,normal,slow.
					//fontCss: {color:"red"},	//设置节点样式
					//nameIsHTML: false,
					//removeHoverDom: null,
					selectedMulti: false,   	//是否允许同时选中多个节点,默认ture
					//txtSelectedEnable: false
				},
				/**
				 * 节点编辑拖拽等操作设置
				 * 这下面的设置要配合setting.callback中的回调函数使用,如果对树没有特殊操作  edit内的属性可以不设置
				 */
				edit:{
//							drag功能,setting.edit.enable=true生效
					drag:{
						autoExpandTrigger:true,//默认值为false,拖拽时自动展开是否触发onExpand事件,
						isCopy:true,//默认值true，与isMove配合使用
						isMove:true,//
						prev:true,//默认true,允许移动到目标节点前面
						next:true,//默认true,允许移动到目标节点后面
						inner:true,//默认true,允许成为目标节点的子节点
						borderMax:10,//拖拽节点成为根节点时的Tree内边界范围，默认10,单位px
						borderMin:-5,//拖拽节点成为根节点时的Tree外便捷范围,默认-5px
						minMoveSize:5,//判定拖拽操作的最小位移值,默认5
						maxShowNodeNum:5,//拖拽多个兄弟节点时，浮动图层中显示的最大节点数,多余节点用...代替，默认5
						autoOpenTime:500 //拖拽时父节点自动展开的延时间隔  默认500毛秒
					},
					enable:true,	//节点是否可编辑 ,默认为false..拖拽移动编辑删除修改
					editNameSelectAll:false, // 节点编辑名称input初次显示时,设置txt内容是否为全选状态,true为全选,setting.edit.enable=ture时生效
					showRemoveBtn:false,
					showRenameBtn:false
				},
				/**
				 * 异步加载后回调函数
				 */
				callback: {
					onClick: singleClick.bind(_this),
					onDblClick: dblClick.bind(_this),
					onRightClick: rightClick.bind(_this),
					onCheck: chooseCheck.bind(_this),
					onAsyncSuccess: zTreeAsyncSuccess.bind(_this),
					success: zTreeAsyncSuccess.bind(_this)
				}
			}
		};

		reloadTree.call(_this);

	};// build end

	/**
	 * 异步加载返回函数预处理
	 * @treeId    树对象唯一标志
	 * @parentNode	被点击异步加载的父节点对象
	 * @responseData  返回的数据
	 */
	function ajaxDataFilter(treeId,parentNode,responseData){
		var _this = this;
		var rpDate = null;
		if(_this.opts.ajaxDataFilter){
			rpDate = _this.opts.ajaxDataFilter(treeId,parentNode,responseData)
		}else{
			rpDate = responseData;
		}
		return rpDate;
	}

	/**
	 * 多选框单选框勾选事件
	 * @event	事件对象
	 * @treeId    树对象唯一标志
	 * @treeNode  被选择节点对象
	 */
	function chooseCheck(event, treeId, treeNode) {
		var _this = this;
		_this.opts.onCheck && _this.opts.onCheck(event, treeId, treeNode);
	}

	/**
	 * 右键单击事件
	 * @event	事件对象
	 * @treeId    树对象唯一标志
	 * @treeNode  被选择节点对象
	 */
	function rightClick(event, treeId, treeNode) {
		var _this = this;
		_this.opts.onRightClick && _this.opts.onRightClick(event, treeId, treeNode);
	}

	/**
	 * 左键单击事件
	 * @event	事件对象
	 * @treeId    树对象唯一标志
	 * @treeNode  被选择节点对象
	 */
	function singleClick(event, treeId, treeNode) {
		var _this = this;
		_this.opts.onClick && _this.opts.onClick(event, treeId, treeNode);

	};

	/**
	 * 左键双击时间
	 * @event	事件对象
	 * @treeId    树对象唯一标志
	 * @treeNode  被选择节点对象
	 */
	function dblClick(event, treeId, treeNode) {
		var _this = this;
		_this.opts.onDblClick && _this.opts.onDblClick(event, treeId, treeNode);

	};

	/**
	 * 异步加载成功结束后回调函数
	 * @event	事件对象
	 * @treeId    树对象唯一标志
	 * @treeNode  被选择节点对象
	 * @msg		信息
	 */
	function zTreeAsyncSuccess(event, treeId, treeNode, msg) {
		var _this = this;
		_this.opts.success && _this.opts.success(event, treeId, treeNode, msg);
	};

	/**
	 * 树节点展开函数
	 * @_thisNode	节点对象
	 */
	ZtreeModal.prototype.expandNodes = function(_thisNode) {
		var _this = this;
		$.each(_thisNode, function(index, value) {
			if (this.isParent && _this.tree.zTree.expandNode) {
				_this.tree.zTree.expandNode(this, true, false);
			}
		});
		$('.zTreeDiv').find('a').tooltip();
	}

	/*  zTree初始化  */
	function reloadTree(){
		var _this = this;
		$.fn.zTree.init($("#"+_this.element), _this.tree.setting, _this.topTreeNode);   //ztree初始化
		_this.tree.zTree = $.fn.zTree.getZTreeObj("zTree");               //对象设置
	}

	/**
	 * 获取鼠标在屏幕上的位置
	 * @param ev
	 * @returns {*}
	 */
	function mouseCoords(ev){
		if (ev.pageX || ev.pageY) {
			return { x: ev.pageX, y: ev.pageY };
		}
		return {
			x: ev.clientX + document.body.scrollLeft - document.body.clientLeft,
			y: ev.clientY + document.body.scrollTop - document.body.clientTop
		};
	}


	function Plugin(option,methdOpt){
		var data =  $(this).data('zhou.ztreemodal');
		if(typeof option == 'string') {
			return data[option].call(data,methdOpt);
		}
		return this.each(function(){
			var _this = $(this);
			var data = _this.data('zhou.ztreemodal');
			var options = typeof option == 'object' && option;
			if(!data){
				_this.data('zhou.ztreemodal',(data = new ZtreeModal(_this,options)));
				data.init();
			} else {
				data.opts = options?options:{};
				data.init();
			}
		});
	}
	var old = $.fn.ztreemodal;
	$.fn.ztreemodal = Plugin;
	$.fn.ztreemodal.Constructor = ZtreeModal;

	//解决冲突
	$.fn.ztreemodal.noConflect = function(){
		$.fn.ztreemodal = old;
		return this;
	};

})(jQuery);




