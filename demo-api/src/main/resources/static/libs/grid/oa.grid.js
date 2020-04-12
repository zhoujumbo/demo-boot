var Grid = function(param) {
	this._option=param;
	
	this.id = Grid.count++;
	this.locId = param.locId;
	Grid.instances[this.id] = this;
	this.gridMsgId = param.locId + '_tHead';
	this.tbodyId = param.locId + '_tBody';
	this.tfootId = param.locId + '_tFoot';
	this.colSortIdPrefix = param.locId + '_col_sort_';
	this.operName = param.operName || '操作';
	this.width = param.width;
	this.cols = param.cols || [];
	this.totalColum = 0;
	this.isDisable = param.isDisable || false;//判断checkbox是否禁用
	this.lineButtons = param.lineButtons || [];
	this.rows = [];
	this.url = param.url || null;
	this.select = param.select || false;
	this.selectThDivId = param.locId + '_selectThDiv';
	this.selectGroupName = param.locId + '_selectGroupName';
	this.page = 1;
	this.total = 0;
	// 当前页
	this.currentPage = 1;
	// 共多少页
	this.pageCount = 0;
	this.refreshBackFn = param.refreshBackFn;
	this.result = {};
	this.isHaveGridFoot = (param.isHaveGridFoot === false ? false : true);
	this.rp = param.rp || (this.isHaveGridFoot === true ? 20 : 0);
	this.data = {};
	this.searchData = {};
	this.gridMsg = param.gridMsg || null;
	this.sortname = param.sortname || null;
	this.sortorder = param.sortorder || 'asc';
	this.draw();
	if(!param.notAutoPost){
		this._postData();
	}
};

Grid.count = 0;
Grid.instances = [];

Grid.prototype.setGridMsg = function(gridMsg) {
	if (gridMsg) {
		this.gridMsg = gridMsg;
		$('#' + this.gridMsgId).html(gridMsg).show();
	}
};

Grid.prototype.clearGridMsg = function() {
	$('#' + this.gridMsgId).html('').hide();
};

Grid.prototype.allSelectChange = function() {
	var $selectThDiv = $('#' + this.selectThDivId);
	if ($selectThDiv.attr("checked")) {
		$("input[name='" + this.selectGroupName + "']:not(:disabled)").attr("checked", "true");
	} else {
		$("input[name='" + this.selectGroupName + "']").removeAttr("checked");
	}
};
Grid.prototype.selectAll = function() {
	var $selectGroup = $("input[name='" + this.selectGroupName + "']");
	$selectGroup.attr("checked", true);
};

Grid.prototype.clearSelected = function() {
	var $selectGroup = $("input[name='" + this.selectGroupName + "']");
	$selectGroup.removeAttr("checked");
};

Grid.prototype.getSelectedDatas = function() {
	var $selectGroup = $("input[name='" + this.selectGroupName + "' ]:checked");
	var data = [];
	var rowIndex = -1;
	var _self = this;
	$selectGroup.each(function() {
		rowIndex = parseInt($(this).attr('row'), 10);
		data.push(_self.getData(rowIndex));
	});
	return data;
};

/**
 * 构建表头
 */
Grid.prototype._createThHtml = function() {
	var cols = this.cols;
	var totalColum = cols.length;
	var thArray = [];
	thArray.push('<thead><tr>');
	if (this.select === true) {
		thArray.push('<th style="width: 5%"><input type="checkbox" id="'
				+ this.selectThDivId + '" onclick="Grid.instances[' + this.id
				+ '].allSelectChange()"/></th>')
	}
	var col;
	for (colIndex in cols) {
		col = cols[colIndex];
		thArray.push('<th' + (col.style ? ' style="' + col.style + '"' : '')
				+ ' class="' + (col.className ? col.className : 'text-center')
				+ '"');
		if (col.needSort === true) {
			var sortname = col.sortName;
			if (!sortname) {
				sortname = col.id;
			}
			thArray.push(' onclick="Grid.instances[' + this.id + '].colSort(\''
					+ col.id + '\',\'' + sortname + '\')" >' + col.html
					+ '<span id="' + this.colSortIdPrefix + col.id
					+ '">↓</span>');
		} else {
			thArray.push(">" + col.html);
		}
		thArray.push('</th>');
	}
	if (this.lineButtons.length > 0) {
		totalColum++;
		thArray.push('<th class="text-center">' + this.operName + '</th>');
	}
	thArray.push('</tr></thead>');
	this.totalColum = totalColum;
	return thArray.join('');
};

Grid.prototype.colSort = function(sortKey, sortname) {
	var $colSort = $('#' + this.colSortIdPrefix + sortKey);
	var $colTh = $colSort.parent();
	var sortorder = null;
	if ($colTh.hasClass('c-danger')) {
		if ($colSort.text() == '↓') {
			sortorder = 'asc';
			$colSort.text('↑');
		} else {
			sortorder = 'desc';
			$colSort.text('↓');
		}
	} else {
		$('#' + this.locId).find('.c-danger').removeClass('c-danger');
		if ($colSort.text() == '↓') {
			sortorder = 'desc';
		} else {
			$colSort.text('↑');
			sortorder = 'asc';
		}
		$colTh.addClass('c-danger');
	}
	if (sortorder) {
		this.sortname = sortname;
		this.sortorder = sortorder;
		this.page = 1;
		this.currentPage = 1;
		this._postData();
	}
};

Grid.prototype._createTbodyHtml = function() {
	var rows = this.rows;
	var cols = this.cols;
	var tbodyArray = [];
	var row, col;
	var lineButtons = this.lineButtons;
	var actionKeyMap = {};
	if(rows.length==0)
	{
		tbodyArray.push('<tr><td colspan="'+cols.length+'" style="text-align:center;">无数据</td></tr>')
	}
	for (rowIndex in rows) {
		row = rows[rowIndex];
		tbodyArray.push('<tr>');
		if (this.select === true) {
			if(this.isDisable && this.isDisable(row)){
				tbodyArray
				.push('<td><input type="checkbox" disabled="disabled" id="'
						+ (this.selectThDivId + rowIndex) + '" name="'
						+ this.selectGroupName + '" row="' + rowIndex
						+ '" /></td>');
			}else{
				tbodyArray
				.push('<td><input type="checkbox" id="'
						+ (this.selectThDivId + rowIndex) + '" name="'
						+ this.selectGroupName + '" row="' + rowIndex
						+ '" /></td>');
			}
		}
		for (colIndex in cols) {
			col = cols[colIndex];
			if (col.cloth) {
				tbodyArray.push('<td'
						+ (col.style ? ' style="' + col.style + '"' : '')
						+ ' class="'
						+ (col.className ? col.className : 'text-center')
						+ '">' + col.cloth(row,rowIndex) + '</td>');
			} else {
				tbodyArray.push('<td'
						+ (col.style ? ' style="' + col.style + '"' : '')
						+ ' class="'
						+ (col.className ? col.className : 'text-center')
						+ '">' + (null == row[col.id] ? '' : row[col.id])
						+ '</td>');
			}
		}
		if (lineButtons && lineButtons.length > 0) {
			tbodyArray.push('<td class="text-center">');
			var operButton, showFn, actionKey;
			for (operButtonIndex in lineButtons) {
				operButton = lineButtons[operButtonIndex];
				showFn = operButton.showFn;
				actionKey = operButton.actionKey;
				if (actionKeyMap[actionKey] == null) {
					actionKeyMap[actionKey] = userAction
							.checkGridBtn(actionKey);
				}
				 if (actionKeyMap[actionKey] === true && (!showFn || showFn(row))) {
					if (operButtonIndex != 0) {
						tbodyArray.push('&nbsp;&nbsp;');
					}
					if (operButton.buildHtml) {
						tbodyArray.push(operButton.buildHtml(row, rowIndex,
								rows.length));
					} else if (operButton.fnName && operButton.text) {
						tbodyArray.push('<a href="javascript:'
								+ operButton.fnName + '(' + rowIndex + ','
								+ rows.length + ')" class="'
								+ operButton.className + '" style="'
							    + operButton.style +'"  >' + operButton.text
								+ '</a>');
					}
				 }
			}
			tbodyArray.push('</td>');
		}
		tbodyArray.push('</tr>');
	}
	return tbodyArray.join('');
};

Grid.prototype._createPagerHtml = function() {
	var total = this.total;
	var rp = this.rp;
	var pagerHtmlArray = [];
	var currentPage = this.currentPage;
	var pageCount = this.pageCount;
	pagerHtmlArray.push('<span class="status-tip"> 第<strong>' + currentPage
			+ '</strong>页/共<strong>' + pageCount + '</strong>页 共<strong>'
			+ total + '</strong>条 </span>');

	pagerHtmlArray.push('<span class="num-record">每页');
	pagerHtmlArray.push('<select class="span1"  onchange="Grid.instances['
			+ this.id + '].pageCountSet(this.value)">');
	pagerHtmlArray.push('<option value="10"'
			+ (rp == 10 ? ' selected="selected"' : '') + '>10</option>');
	pagerHtmlArray.push('<option value="20"'
			+ (rp == 20 ? ' selected="selected"' : '') + '>20</option>');
	pagerHtmlArray.push('<option value="30"'
			+ (rp == 30 ? ' selected="selected"' : '') + '>30</option>');
	pagerHtmlArray.push('</select> 条记录 </span>');

	pagerHtmlArray.push('<span class="right">');
	if (currentPage == 1) {
		pagerHtmlArray
				.push('<span class="home">首页</span> <span class="home">上一页</span>');
	} else {
		pagerHtmlArray.push('<a class="home" href="javascript:Grid.instances['
				+ this.id
				+ '].doFirst()">首页</a> <a href="javascript:Grid.instances['
				+ this.id + '].doPre()" class="prev">上一页</a>');
	}
	pagerHtmlArray.push('<span><strong>' + currentPage + '</strong></span>');
	if (currentPage == pageCount) {
		pagerHtmlArray
				.push('<span class="next">下一页</span> <span class="last">尾页</span>');
	} else {
		pagerHtmlArray.push('<a class="next" href="javascript:Grid.instances['
				+ this.id
				+ '].doNext()" >下一页</a> <a href="javascript:Grid.instances['
				+ this.id + '].doLast()" class="last">尾页</a>');
	}
	pagerHtmlArray
			.push(' 向第 <input type="text" size="6" class="span1" id="'
					+ this.locId
					+ '_jumpInput"> 页 <input type="button" value="跳转" onclick="Grid.instances['
					+ this.id + '].doJump()"></span>');
	return pagerHtmlArray.join("");
};

/**
 * 画出表格
 */
Grid.prototype.draw = function() {
	var gridMsgId = this.gridMsgId;
	var tfootId = this.tfootId;
	var tbodyId = this.tbodyId;
	var thHtml = this._createThHtml();
	var width = this.width;
	$('#' + this.locId)
			.html(
					'<div class="row-table ml0"><div class="alert alert-info" id="'
							+ gridMsgId
							+ '" style="display:none">'
							+ (this.gridMsg || '')
							+ '</div>'
							+ '<div class="table-l"><table class="table table-hover '+this._option.className+'"'
							+ (width ? ' style="width:' + width + '"' : '')
							+ '>'
							+ thHtml
							+ '<tbody id="'
							+ tbodyId
							+ '"></tbody></table></div>'
							+ (this.isHaveGridFoot === true ? '<div class="paginator" id="'
									+ tfootId + '"></div>'
									: '') + '</div>');
};

/**
 * 构建查询参数
 */
Grid.prototype._buildPostData = function() {
	this._buildSchCondition();
	this._buildPageData();
	this._buildSortCondition();
};

/**
 * 构建排序参数
 */
Grid.prototype._buildSortCondition = function() {
	if (this.sortname && this.sortorder) {
		this.data.sortname = this.sortname;
		this.data.sortorder = this.sortorder;
	}
};

/**
 * 构建查询参数
 */
Grid.prototype._buildSchCondition = function() {
	this.data = this.searchData;
};

/**
 * 构建翻页参数
 */
Grid.prototype._buildPageData = function() {
	this.data.rp = this.rp;
	this.data.page = this.page;
};

/**
 * 提交数据
 */
Grid.prototype._postData = function() {
	var ret = null;
	var url = this.url;
	if (typeof url === 'string' && url) {
		this._buildPostData();
		var data = this.data;
		$.ajax({
			type : "POST",
			url : url,
			data : $.param(data, true),
			dataType : "json",
			async : false,
			success : function(e) {
				if (e.expression) {
					eval(e.expression);
					return;
				}
				ret = e;
			},
			error : function(e) {
				window.location.href = Action.buildRefreshUrl();
				ret = null;
			}
		});
		if (!ret) {
			return;
		}
		if (ret.expression) {
			eval(ret.expression);
			return;
		}
		this.result = ret;
		this.rp = ret.rp || this.rp;
		this.total = ret.total || 0;
		this.page = ret.page || 0;
		this.pageCount = parseInt((this.total - 1) / this.rp) + 1;
		this.rows = ret.rows || [];
		if (ret.gridMsg) {
			this.gridMsg = ret.gridMsg;
		}
		this.refresh();
	}
};

/**
 * 刷新表格
 */
Grid.prototype.refresh = function() {
	if (this.gridMsg) {
		$('#' + this.gridMsgId).html(this.gridMsg).show();
	}
	$('#' + this.selectThDivId).removeAttr("checked");
	var tbodyId = this.tbodyId;
	var tbodyHtml = this._createTbodyHtml();
	var pagerHtml = this._createPagerHtml();
	$('#' + tbodyId).html(tbodyHtml);
	if (this.isHaveGridFoot === true) {
		var tfootId = this.tfootId;
		$('#' + tfootId).html(pagerHtml);
	}
	this.refreshBackFn && this.refreshBackFn(this);
};

/**
 * 搜索
 */
Grid.prototype.search = function(schData) {
	this.searchData = schData || {};
	this.page = 1;
	this.currentPage = this.page;
	this._postData();
};

/**
 * 重新加载
 */
Grid.prototype.reloadByData = function(schData) {
	this.searchData = schData || {};
	this._postData();
};

/**
 * 重新加载
 */
Grid.prototype.reload = function() {
	this._postData();
};

/**
 * 设置数据
 */
Grid.prototype.setDatas = function(array) {
	this.rows = array;
	this.refresh();
};

/**
 * 获取所有数据
 */
Grid.prototype.getDatas = function() {
	return this.rows;
};

/**
 * 下一页
 */
Grid.prototype.doNext = function() {
	this.page = (this.page < this.pageCount) ? (this.page + 1) : this.page;
	if (this.currentPage == this.page) {
		return;
	}
	this.currentPage = this.page;
	this._postData();
};

/**
 * 前一页
 */
Grid.prototype.doPre = function() {
	this.page = (this.page > 1) ? (this.page - 1) : this.page;
	if (this.currentPage == this.page) {
		return;
	}
	this.currentPage = this.page;
	this._postData();
};

/**
 * 末页
 */
Grid.prototype.doLast = function() {
	this.page = this.pageCount;
	if (this.currentPage == this.page) {
		return;
	}
	this.currentPage = this.page;
	this._postData();
};

/**
 * 首页
 */
Grid.prototype.doFirst = function() {
	this.page = 1;
	if (this.currentPage == this.page) {
		return;
	}
	this.currentPage = this.page;
	this._postData();
};

Grid.prototype.pageCountSet = function(selectRp) {
	this.page = 1;
	this.rp = selectRp;
	this.currentPage = this.page;
	this._postData();
};

/**
 * 跳转到哪一页
 */
Grid.prototype.doJump = function() {
	var page = $('#' + this.locId + '_jumpInput').val();
	if (page && !isNaN(page)) {
		this.page = (page > this.pageCount || page < 0) ? this.currentPage
				: page;
	} else {
		this.page = this.currentPage;
	}
	if (this.currentPage == this.page) {
		return;
	}
	this.currentPage = this.page;
	this._postData();
};

/**
 * 获得某行的数据
 */
Grid.prototype.getData = function(n) {
	var ret = {};
	if (n > -1 && n < this.rows.length) {
		ret = this.rows[n];
	}
	return ret;
};

/**
 * 获得当前数据的条数
 */
Grid.prototype.getDataLength = function() {
	return this.rows.length;
};

/**
 * 插入数据
 * 
 * 如果n<0,插入到首位 如果n>rows.length,插入到末尾
 * 
 * @param n:
 *            插入到第几行
 * @param obj:
 *            表格数据
 * 
 */
Grid.prototype.insertData = function(n, obj) {
	var rowsLen = this.rows.length;
	if (n < 0) {
		n = 0;
	}
	if (n > rowsLen) {
		n = rowsLen;
	}
	this.rows.splice(n, 0, obj);
	this.refresh();
};
/**
 * 删除所有数据
 */
Grid.prototype.delAllData = function() {
	this.rows = [];
	this.refresh();
};

/**
 * 删除数据
 */
Grid.prototype.delData = function(i) {
	if (this.rows.length > i) {
		this.rows.splice(i, 1);
		this.refresh();
	}
};

/**
 * 删除被选中数据
 */
Grid.prototype.delSelectedData = function() {
	var $selectGroup = $("input[name='" + this.selectGroupName + "' ]");
	var _self = this;
	var data = [];
	$selectGroup.each(function() {
		if (!$(this).attr('checked')) {
			data.push(_self.rows[parseInt($(this).attr('row'), 20)]);
		}
	});
	_self.rows = data;
	this.refresh();
};
