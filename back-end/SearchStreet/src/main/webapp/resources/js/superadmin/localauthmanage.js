$(function() {
	initializePage();
});

function initializePage() {
	// 加载表格数据
	ajaxTable();
	// 初始化弹出层
	setDialog_edit();
	setDialog_create();
	closeDialog_edit();
	closeDialog();
	searchAccountInfo()
}

function searchAccountInfo() {
	var userId = $("#searchInfoHd").val();
	if (userId == "") {
		listLocalAuths();
	} else {
		listLocalAuthsByUserId();
	}
}

function listLocalAuths() {
	var urlBuffer = new StringBuffer();
	urlBuffer.append("listlocalauths?");
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#localAuthManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}

function listLocalAuthsByUserId() {
	var userId = $("#searchInfoHd").val();
	if (userId == "") {
		return;
	}
	var urlBuffer = new StringBuffer();
	urlBuffer.append("searchlocalauthbyuserid?userId=");
	urlBuffer.append(userId + "&");
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#localAuthManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}

/** --------------table------------------* */
/**
 * 加载表格数据
 */
function ajaxTable() {
	// 加载表格
	$('#localAuthManagementTable').datagrid(
			{
				toolbar : [ {
					text : '操作列表'
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
					$('#localAuthManagementTable').datagrid('unselectRow',
							rowIndex);
				},
				onLoadSuccess : function() {
					var value = $('#localAuthManagementTable').datagrid(
							'getData')['errorMsg'];
					if (value != null) {
						alert("错误消息:" + value);
					}
				}
			}).datagrid('acceptChanges');
	var searchBoxBuffer = new StringBuffer();
	searchBoxBuffer.append('<input type="text" ');
	searchBoxBuffer
			.append('id="localAuthManagementSearch_searchBox" onchange="changeSearchInfo()"');
	searchBoxBuffer
			.append(' style="resize: none; width: 200px"></input>');
	searchBoxBuffer.append('<input type="button" id="searchBtn" value="按用户ID搜索"');
	searchBoxBuffer
			.append('style="margin-right: 0.5em;" onclick="searchInfo()"/>');
	var addButtonBuffer = new StringBuffer();
	addButtonBuffer
			.append('<input type="button" id="addLocalAuth" value="添加本地账号信息"');
	addButtonBuffer
			.append('style="margin-right: 0.5em;" onclick="openDialog()"/>');
	$('.datagrid-toolbar').append(searchBoxBuffer.toString());
	$('.datagrid-toolbar').append(addButtonBuffer.toString());
	// 获取DataGrid分页组件对象
	var p = $("#localAuthManagementTable").datagrid("getPager");
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
	urlBuffer.append("page=");
	urlBuffer.append(pageNum);
	urlBuffer.append("&rows=");
	urlBuffer.append(rowsLimit);
	$.ajax({
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
		success : function(data, textStatus, jqXHR) { // 请求成功，将返回的数据（一页的记录数）绑定到
			// datagrid控件
			$('#localAuthManagementTable').datagrid('loadData', data);
		}
	});// ajax
}

function changeSearchInfo() {
	var info = $('#localAuthManagementSearch_searchBox').val();
	$('#searchInfoHd').val(info);
}

function searchInfo() {
	var info = $('#localAuthManagementSearch_searchBox').val().trim();
	$('#searchInfoHd').val(info);
	searchAccountInfo();
}
/**
 * 设置操作列的信息 参数说明 value 这个可以不管，但是要使用后面 row 和index 这个参数是必须的 row 当前行的数据 index
 * 当前行的索引 从0 开始
 */
function optFormater(value, row, index) {
	var localAuthId = row.localAuthId;
	var userId = row.userId;
	var userName = row.userName;
	var params = localAuthId + "," + userId + ",'" + userName + "'";
	var edit = '<a href="javascript:openDialog_edit(' + params + ')">编辑</a>';
	return edit;
};

/** --------------弹出框------------------* */
// 设置弹出框的属性
function setDialog_edit() {
	$('#localAuthManagementEdit').dialog({
		title : '更换密码',
		modal : true, // 模式窗口：窗口背景不可操作
		collapsible : true, // 可折叠，点击窗口右上角折叠图标将内容折叠起来
		resizable : true
	// 可拖动边框大小
	});
}
function setDialog_create() {
	$('#localAuthManagementCreate').dialog({
		title : '本地帐号添加',
		modal : true, // 模式窗口：窗口背景不可操作
		collapsible : true, // 可折叠，点击窗口右上角折叠图标将内容折叠起来
		resizable : true
	// 可拖动边框大小
	});
}
// 打开对话框
function openDialog_edit(localAuthId, userId, userName) {
	localAuthManagementEditReset(localAuthId, userId, userName);
	$('#localAuthManagementEdit').dialog('open');
}

function openDialog() {
	localAuthManagementCreateReset();
	$('#localAuthManagementCreate').dialog('open');
}
// 关闭对话框
function closeDialog_edit() {
	$('#localAuthManagementEdit').dialog('close');
}
function closeDialog() {
	$('#localAuthManagementCreate').dialog('close');
}
// 根据用户id查询用户的信息
function localAuthManagementCreateReset(){
	$("#localAuthManagementCreate_message").html("");
	$('#form_localAuthManagementCreate').form('load', {
		localAuthId : null,
		userId : null,
		userName : null,
		password : null
	});
}
function localAuthManagementEditReset(localAuthId, userId, userName,
		createTime, lastEditTime) {
	$("#localAuthManagementEdit_message").html("");
	$('#form_localAuthManagementEdit').form('load', {
		localAuthId : localAuthId,
		userId : userId,
		userName : userName,
		password : null,
		newPassword : null
	});
}
// 执行用户编辑操作
function localAuthManagementEdit() {
	$('#form_localAuthManagementEdit').form('submit', {
		url : 'modifylocalauth',
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
				searchAccountInfo();
				$("#localAuthManagementEdit_message").html(messgage);
			} else {
				var messgage = data.errMsg;
				$("#localAuthManagementEdit_message").html(messgage);
			}
		}
	});

}
function localAuthManagementCreate() {
	$('#form_localAuthManagementCreate').form('submit', {
		url : 'register',
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
				searchAccountInfo();
				$("#localAuthManagementCreate_message").html(messgage);
			} else {
				var messgage = data.errMsg;
				$("#localAuthManagementCreate_message").html(messgage);
			}
		}
	});
}
function timeFormater(value, row, index) {
	if (value == null || value == '') {
		return '';
	}
	var time;
	if (value == row.createTime) {
		time = row.createTime;
	} else if (value == row.lastEditTime) {
		time = row.lastEditTime;
	}
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
			searchAccountInfo();
		}
	});
}
