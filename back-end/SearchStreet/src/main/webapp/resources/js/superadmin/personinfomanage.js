$(function() {
	initializePage();
});

function initializePage() {
	// 加载表格数据
	ajaxTable();
	// 初始化弹出层
	setDialog_edit();
	closeDialog_edit();
	searchAccountInfo()
}

function searchAccountInfo() {
	var userName = $("#searchInfoHd").val();
	if (userName == "") {
		listPersonInfos();
	} else {
		listPersonInfosByUserName();
	}
}

function listPersonInfos() {
	var enalbeStatus = $("#enalbeStatusHd").val();
	var urlBuffer = new StringBuffer();
	urlBuffer.append("listpersonInfos?enableStatus=");
	urlBuffer.append(enalbeStatus);
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#personinfoManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}

function listPersonInfosByUserName() {
	var userName = $("#searchInfoHd").val();
	if (userName == "") {
		return;
	}
	var enalbeStatus = $("#enalbeStatusHd").val();
	var urlBuffer = new StringBuffer();
	urlBuffer.append("listpersonInfos?enableStatus=");
	urlBuffer.append(enalbeStatus);
	urlBuffer.append("&name=");
	urlBuffer.append(userName);
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#personinfoManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}

/** --------------table------------------* */
/**
 * 加载表格数据
 */
function ajaxTable() {
	// 加载表格
	$('#personinfoManagementTable').datagrid(
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
					$('#personinfoManagementTable').datagrid('unselectRow',
							rowIndex);
				},
				onLoadSuccess : function() {
					var value = $('#personinfoManagementTable').datagrid(
							'getData')['errorMsg'];
					if (value != null) {
						alert("错误消息:" + value);
					}
				}
			}).datagrid('acceptChanges');
	var enableStatusBuffer = new StringBuffer()
	enableStatusBuffer
			.append('<select id="personinfoManagementFilter_enableStatus" class="easyui-combobox" style="margin :2px; padding :4px;" ');
	enableStatusBuffer
			.append('onchange="changeFilterStatus(this.options[this.options.selectedIndex].value)">');
	enableStatusBuffer
			.append('<option id="personinfoManagementFilter_ALL" value="">全部</option>');
	enableStatusBuffer
			.append('<option id="personinfoManagementFilter_YES" value="1">启用</option>');
	enableStatusBuffer
			.append('<option id="personinfoManagementFilter_NO" value="0">禁用</option></select>');
	var searchBoxBuffer = new StringBuffer();
	searchBoxBuffer.append('<input type="text" ');
	searchBoxBuffer
			.append('id="personinfoManagementSearch_searchBox" onchange="changeSearchInfo()"');
	searchBoxBuffer
			.append(' style="resize: none; width: 200px" onkeydown="searchInfoKeyDown(e)"></input>');
	searchBoxBuffer.append('<input type="button" id="searchBtn" value="搜索"');
	searchBoxBuffer
			.append('style="margin-right: 0.5em;" onclick="searchInfo()"/>');
	var addButtonBuffer = new StringBuffer();
	addButtonBuffer
			.append('<input type="button" id="addPersonInfo" value="添加用户信息"');
	addButtonBuffer
			.append('style="margin-right: 0.5em;" onclick="openDialog()"/>');
	$('.datagrid-toolbar').append(enableStatusBuffer.toString());
	$('.datagrid-toolbar').append(searchBoxBuffer.toString());
	$('.datagrid-toolbar').append(addButtonBuffer.toString());
	// 获取DataGrid分页组件对象
	var p = $("#personinfoManagementTable").datagrid("getPager");
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
			$('#personinfoManagementTable').datagrid('loadData', data);
		}
	});// ajax
}

function changeFilterStatus(status) {
	$('#enalbeStatusHd').val(status);
	searchAccountInfo();
}

function changeSearchInfo() {
	var info = $('#personinfoManagementSearch_searchBox').val();
	$('#searchInfoHd').val(info);
}

function searchInfoKeyDown(e) {
	if (e.keyCode == 13) {
		searchInfo();
	}
}

function searchInfo() {
	var info = $('#personinfoManagementSearch_searchBox').val().trim();
	$('#searchInfoHd').val(info);
	if (info == "") {
		return;
	}
	searchAccountInfo();
}
/**
 * 设置操作列的信息 参数说明 value 这个可以不管，但是要使用后面 row 和index 这个参数是必须的 row 当前行的数据 index
 * 当前行的索引 从0 开始
 */
function optFormater(value, row, index) {
	var userId = row.userId;
	var userName = row.userName;
	var email = row.email == null ? '' : row.email;
	var sex = row.sex;
	var birth = timeChange(row.birth);
	var phone = row.phone == null ? '' : row.phone;
	var souCoin = row.souCoin == null ? '' : row.souCoin;
	var userType = row.userType;
	var enableStatus = row.enableStatus;

	var params = userId + ",'" + userName + "','" + email + "'," + sex + ",'"
			+ birth + "','" + phone + "'," + souCoin + "," + userType + ","
			+ enableStatus;
	var edit = '<a href="javascript:openDialog_edit(' + params + ')">编辑</a>';
	return edit;
};

/** --------------编辑操作弹出框------------------* */
// 设置弹出框的属性
function setDialog_edit() {
	$('#personinfoManagementEdit').dialog({
		title : '用户信息编辑',
		modal : true, // 模式窗口：窗口背景不可操作
		collapsible : true, // 可折叠，点击窗口右上角折叠图标将内容折叠起来
		resizable : true
	// 可拖动边框大小
	});
}
// 打开对话框
function openDialog_edit(userId, userName, email, sex, birth, phone, souCoin,
		userType, enableStatus) {
	personinfoManagementEditReset(userId, userName, email, sex, birth, phone,
			souCoin, userType, enableStatus);
	$('#personinfoManagementEdit').dialog('open');
}

function openDialog() {
	personinfoManagementEditReset(null, null, null, null, null, null, null,
			null, null);
	$('#personinfoManagementEdit').dialog('open');
}
// 关闭对话框
function closeDialog_edit() {
	$('#personinfoManagementEdit').dialog('close');
}
// 根据用户id查询用户的信息
function personinfoManagementEditReset(userId, userName, email, sex, birth,
		phone, souCoin, userType, enableStatus) {
	$("#personinfoManagementEdit_message").html("");
	/*
	 * $("#personinfoManagementEdit_name").val(userName);
	 * $("#personinfoManagementEdit_userId").val(userId);
	 * $("#personinfoManagementEdit_enableStatus").val(enableStatus);
	 */
	$('#form_personinfoManagementEdit').form('load', {
		userId : userId,
		userName : userName,
		profileImg : null,
		email : email,
		sex : sex,
		phone : phone,
		souCoin : souCoin,
		userType : userType,
		enableStatus : enableStatus,
	});
	$('#birth').datetimebox('setValue', birth);
}
// 执行用户编辑操作
function personinfoManagementEdit() {
	var userId = $('#userId').val()
	if (userId != '') {
		$('#form_personinfoManagementEdit').form('submit', {
			url : 'modifypersonInfo',
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
					$("#personinfoManagementEdit_message").html(messgage);
				} else {
					var messgage = data.errMsg;
					$("#personinfoManagementEdit_message").html(messgage);
				}
			}
		});
	} else {
		$('#form_personinfoManagementEdit').form('submit', {
			url : 'addpersoninfo',
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
					$("#personinfoManagementEdit_message").html(messgage);
				} else {
					var messgage = data.errMsg;
					$("#personinfoManagementEdit_message").html(messgage);
				}
			}
		});
	}
}
function sexFormatter(value, row, index) {
	if (row.sex == 1)
		return '男性';
	else if (row.sex == 2)
		return '女性';
	else
		return '未知';
}
function statusFormatter(value, row, index) {
	if (row.enableStatus == 1)
		return '合法';
	else
		return '非法';
}
function userTypeFormater(value, row, index) {
	if (row.userType == 1)
		return '管理员';
	else
		return '普通用户';
}
function imgFormater(value, row, index) {
	if (value == null || value == '') {
		return '';
	}
	var profileImg = row.profileImg;
	return '<img src="' + profileImg + '" width="100px" height="100px">';
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
	} else {
		time = row.birth
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
