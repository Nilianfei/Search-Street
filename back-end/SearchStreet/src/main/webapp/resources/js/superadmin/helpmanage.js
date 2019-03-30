$(function() {
	initializePage();
});

function initializePage() {
	// 加载表格数据
	ajaxTable();
	// 初始化弹出层
	setDialog_edit();
	setDialog_create()
	closeDialog_edit();
	closeDialog_create()
	searchHelpInfo();
}

function searchHelpInfo() {
	var helpStatus = $("#helpStatusHd").val();
	var searchCondition = $("#searchConditionHd").val();
	if (searchCondition == "byAppealId") {
		listHelpByAppealId();
	} else {
		listHelps();
	}
}

function statusFormater(value, row, index) {
	if (row.helpStatus == 0)
		return '未被接受帮助';
	else if (row.helpStatus == 1)
		return '已被接受帮助';
	else if (row.helpStatus == 2)
		return '已完成';
	else if (row.helpStatus == 3)
		return '已失效';
	else if (row.helpStatus == 4)
		return '禁止';
}

function timeFormater(value, row, index) {
	if (value == null || value == '') {
		return '';
	}
	var time = row.endTime;
	return timeChange(time);
}
function timeChange(time) {
	if (time == null) {
		return '';
	}
	var datetime = new Date();
	datetime.setTime(time);
	var year = datetime.getFullYear();
	var month = datetime.getMonth() + 1;
	var date = datetime.getDate();
	var hour = datetime.getHours();
	if (hour <= 9) {
		hour = "0" + hour;
	}
	var minute = datetime.getMinutes();
	if (minute <= 9) {
		minute = "0" + minute;
	}
	var second = datetime.getSeconds();
	if (second <= 9) {
		second = "0" + second;
	}
	// var mseconds = datetime.getMilliseconds();
	return year + "-" + month + "-" + date + " " + hour + ":" + minute + ":"
			+ second;// +"."+mseconds;
}
function listHelpByAppealId() {
	var appealId = $("#searchInfoHd").val();
	if (appealId == "") {
		return;
	}
	var urlBuffer = new StringBuffer();
	urlBuffer.append("listhelps?appealId=");
	urlBuffer.append(appealId);
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#helpManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}

function listHelps() {
	var enalbeStatus = $("#enalbeStatusHd").val();
	var urlBuffer = new StringBuffer();
	urlBuffer.append("listhelps?helpStatus=");
	urlBuffer.append(enalbeStatus);
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#helpManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}

/** --------------table------------------* */
/**
 * 加载表格数据
 */
function ajaxTable() {
	// 加载表格
	$('#helpManagementTable')
			.datagrid(
					{
						toolbar : [ {
							text : '查询条件'
						} ],
						pageNumber : 1,
						loadMsg : '数据加载中,请稍后...',
						pageList : [ 10, 30, 50, 100 ], // 设置每页显示多少条
						onLoadError : function() {
							alert('数据加载失败!');
						},
						queryParams : {// 查询条件
						},
						onClickRow : function(rowIndex, rowData) {
							// 取消选择某行后高亮
							$('#helpManagementTable').datagrid('unselectRow',
									rowIndex);
						},
						onLoadSuccess : function() {
							var value = $('#helpManagementTable').datagrid(
									'getData')['errorMsg'];
							if (value != null) {
								alert("错误消息:" + value);
							}
						}
					}).datagrid('acceptChanges');
	var helpStatusBuffer = new StringBuffer()
	helpStatusBuffer
			.append('<select id="helpManagementFilter_helpStatus" class="easyui-combobox" style="margin :2px; padding :4px;" ');
	helpStatusBuffer
			.append('onchange="changeFilterStatus(this.options[this.options.selectedIndex].value)">');
	helpStatusBuffer
			.append('<option id="helpManagementFilter_ALL" value="">全部</option>');
	helpStatusBuffer
			.append('<option id="helpManagementEdit_FIND" value="0">未被接受帮助</option>');
	helpStatusBuffer
			.append('<option id="helpManagementEdit_SELECED" value="1">已被接受帮助</option>');
	helpStatusBuffer
			.append('<option id="helpManagementEdit_FINISH" value="2">已完成</option>');
	helpStatusBuffer
			.append('<option id="helpManagementEdit_DISABLE" value="3">已失效</option>');
	helpStatusBuffer
			.append('<option id="helpManagementEdit_NO" value="4">禁止</option>');
	var searchConditionBuffer = new StringBuffer()
	searchConditionBuffer
			.append('<select id="helpManagementSearch_searchCondition" class="easyui-combobox" style="margin :2px; padding :4px;" ');
	searchConditionBuffer
			.append('onchange="changeSearchCondition(this.options[this.options.selectedIndex].value)">');
	searchConditionBuffer
			.append('<option id="helpManagementSearch_ALL" value="ALL">按筛选条件查询</option>');
	searchConditionBuffer
			.append('<option id="helpManagementSearch_APPEALID" value="byAppealId">按求助ID查询</option></select>');
	var searchBoxBuffer = new StringBuffer();
	searchBoxBuffer.append('<input type="text" ');
	searchBoxBuffer
			.append('id="helpManagementSearch_searchBox" onchange="changeSearchInfo()"');
	searchBoxBuffer
			.append(' style="resize: none; width: 200px" onkeydown="searchInfoKeyDown(e)"></input>');
	searchBoxBuffer.append('<input type="button" id="searchBtn" value="搜索"');
	searchBoxBuffer
			.append('style="margin-right: 0.5em;" onclick="searchInfo()"/>');
	var addButtonBuffer = new StringBuffer();
	addButtonBuffer.append('<input type="button" id="addHelp" value="添加帮助信息"');
	addButtonBuffer
			.append('style="margin-right: 0.5em;" onclick="openDialog()"/>');
	$('.datagrid-toolbar').append(helpStatusBuffer.toString());
	$('.datagrid-toolbar').append(searchConditionBuffer.toString());
	$('.datagrid-toolbar').append(searchBoxBuffer.toString());
	$('.datagrid-toolbar').append(addButtonBuffer.toString());
	$("#helpManagementSearch_searchBox").hide();
	$("#searchBtn").hide();
	// 获取DataGrid分页组件对象
	var p = $("#helpManagementTable").datagrid("getPager");
	// 设置分页组件参数
	$(p).pagination({
		onSelectPage : function(pageNumber, pageSize) {
			getDataByPageRows(pageNumber, pageSize);
		}
	});
}

function getDataByPageRows(pageNum, rowsLimit) {
	pageNum = pageNum || 1; // 设置默认的页号
	rowsLimit = rowsLimit || 10;// 设置默认的每页记录数
	var urlBuffer = new StringBuffer();
	urlBuffer.append($("#urlHd").val());
	urlBuffer.append("&page=");
	urlBuffer.append(pageNum);
	urlBuffer.append("&rows=");
	urlBuffer.append(rowsLimit);
	$.ajax({
		contentType : "application/x-www-form-urlencoded; charset=utf-8",
		type : "post",
		dataType : 'json', // 注意格式是html，不是json
		url : urlBuffer.toString(),
		error : function() { // ajax请求失败
			$.messager.show({
				title : '失败信息',
				msg : '加载内容失败',
				timeout : 0,
				showType : 'fade'
			});
		},
		success : function(jsonObj, textStatus, jqXHR) { // 请求成功，将返回的数据（一页的记录数）绑定到
			// datagrid控件
			$('#helpManagementTable').datagrid('loadData', jsonObj);
		}
	});// ajax
}

function changeFilterStatus(status) {
	$('#enalbeStatusHd').val(status);
	searchHelpInfo();
}

function changeSearchCondition(condition) {
	$("#searchConditionHd").val(condition);
	if (condition == "byAppealId") {
		$("#enalbeStatusHd").val("");
		$("#helpManagementFilter_helpStatus").hide();
		$("#helpManagementSearch_searchBox").show();
		$("#searchBtn").show();
	} else {
		$("#enalbeStatusHd").val("ALL");
		$("#searchInfoHd").val("");
		$("#helpManagementFilter_helpStatus").show();
		$("#helpManagementSearch_searchBox").hide();
		$("#searchBtn").hide();
	}
	searchHelpInfo();
}

function changeSearchInfo() {
	var info = $('#helpManagementSearch_searchBox').val();
	$('#searchInfoHd').val(info);
}

function searchInfo() {
	var info = $('#helpManagementSearch_searchBox').val().trim();
	$('#searchInfoHd').val(info);
	if (info == "") {
		return;
	}
	searchHelpInfo();
}
/**
 * 设置操作列的信息 参数说明 value 这个可以不管，但是要使用后面 row 和index 这个参数是必须的 row 当前行的数据 index
 * 当前行的索引 从0 开始
 */
function optFormater(value, row, index) {
	var helpId = row.helpId;
	/*
	 * var appealTitle = row.appealTitle; var userId = row.userId; var appealId =
	 * row.appealId;
	 */
	var completion = row.completion;
	var efficiency = row.efficiency;
	var attitude = row.attitude;
	/*
	 * var avgCompletion = row.avgCompletion; var avgEfficiency =
	 * row.avgEfficiency; var avgAttitude = row.avgAttitude;
	 */
	var allCoin = row.allCoin;
	var additionalCoin = row.additionalCoin;
	var helpStatus = row.helpStatus;
	// var endTime = timeChange(row.endTime);
	var params = helpId + "," + completion + "," + efficiency + "," + attitude
			+ "," + allCoin + "," + additionalCoin + "," + helpStatus;
	var edit = '<a href="javascript:openDialog_edit(' + params + ')">编辑</a>';
	return edit;
};

/** --------------编辑操作弹出框------------------* */

// 设置弹出框的属性
function setDialog_edit() {
	$('#helpManagementEdit').dialog({
		title : '帮助编辑',
		modal : true, // 模式窗口：窗口背景不可操作
		collapsible : true, // 可折叠，点击窗口右上角折叠图标将内容折叠起来
		resizable : true
	});
}
function setDialog_create() {
	$('#helpManagementCreate').dialog({
		title : '帮助添加',
		modal : true, // 模式窗口：窗口背景不可操作
		collapsible : true, // 可折叠，点击窗口右上角折叠图标将内容折叠起来
		resizable : true
	});
}
// 打开对话框
function openDialog_edit(helpId, completion, efficiency, attitude, allCoin,
		additionalCoin, helpStatus) {
	helpManagementEditReset(helpId, completion, efficiency, attitude, allCoin,
			additionalCoin, helpStatus);
	$('#helpManagementEdit').dialog('open');
}
function openDialog() {
	helpManagementCreateReset();
	$('#helpManagementCreate').dialog('open');
}
// 关闭对话框
function closeDialog_edit() {
	$('#helpManagementEdit').dialog('close');
}
function closeDialog_create() {
	$('#helpManagementCreate').dialog('close');
}
// 根据用户id查询用户的信息
function helpManagementEditReset(helpId, completion, efficiency, attitude,
		allCoin, additionalCoin, helpStatus) {
	$("#helpManagementEdit_message").html("");
	$('#form_helpManagementEdit').form('load', {
		helpId : helpId,
		helpStatus : helpStatus,
		completion : completion,
		efficiency : efficiency,
		attitude : attitude,
		allCoin : allCoin,
		additionalCoin : additionalCoin
	});
}
function helpManagementCreateReset() {
	$("#helpManagementCreate_message").html("");
	$('#form_helpManagementCreate').form('load', {
		userId : null,
		appealId : null,
		appealTitle : null,
	});
}
// 执行用户编辑操作
function helpManagementCreate() {
	$('#form_helpManagementCreate').form('submit', {
		url : 'addhelp',
		onSubmit : function() {
			var isValid = $(this).form('validate');
			if (!isValid) {
				$.messager.progress('close'); // hide progress bar while
				// the
				// form is invalid
			}
			return isValid; // return false will stop the form submission
		},
		success : function(data) {
			var data = eval('(' + data + ')'); // change the JSON string to
			// javascript object
			if (data.success) {
				var messgage = "添加成功!";
				searchHelpInfo();
				$("#helpManagementCreate_message").html(messgage);
			} else {
				var messgage = data.errMsg;
				$("#helpManagementCreate_message").html(messgage);
			}
		}
	});
}
function helpManagementEdit() {
	$('#form_helpManagementEdit').form('submit', {
		url : 'modifyhelp',
		onSubmit : function() {
			var isValid = $(this).form('validate');
			if (!isValid) {
				$.messager.progress('close'); // hide progress bar while
				// the
				// form is invalid
			}
			return isValid; // return false will stop the form submission
		},
		success : function(data) {
			var data = eval('(' + data + ')'); // change the JSON string to
			// javascript object
			if (data.success) {
				var messgage = "修改成功!";
				searchHelpInfo();
				$("#helpManagementEdit_message").html(messgage);
			} else {
				var messgage = data.errMsg;
				$("#helpManagementEdit_message").html(messgage);
			}
		}
	});
}

/**
 * 修改状态的Ajax
 * 
 * @param url
 * @return
 */
function changeStatus(url) {
	$.ajax({
		async : false,
		cache : false,
		type : 'post',
		dataType : "json",
		url : url,// 请求的action路径
		error : function() {// 请求失败处理函数
			alert('请求失败');
		},
		success : function() {
			alert("操作成功");
			searchHelpInfo();
		}
	});
}