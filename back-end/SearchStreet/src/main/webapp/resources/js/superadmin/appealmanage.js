$(function() {
	initializePage();
});

function initializePage() {
	// 加载表格数据
	ajaxTable();
	// 初始化弹出层
	setDialog_edit();
	closeDialog_edit();
	searchAppealInfo();
}

function searchAppealInfo() {
	var appealStatus = $("#appealStatusHd").val();
	var searchCondition = $("#searchConditionHd").val();
	if (searchCondition == "byAppealTitle") {
		listAppealByTitle();
	} else if (searchCondition == "byAppealId") {
		listAppealByAppealId();
	} else {
		listAppeals();
	}
}

function statusFormater(value, row, index) {
	if (row.appealStatus == 0)
		return '未确定帮助者';
	else if (row.appealStatus == 1)
		return '已确定帮助者';
	else if (row.appealStatus == 2)
		return '已完成';
	else if (row.appealStatus == 3)
		return '已失效';
	else if (row.appealStatus == 4)
		return '禁止';
}

function timeFormater(value, row, index) {
	if (value == null || value == '') {
		return '';
	}
	var time;
	if (value == row.startTime) {
		time = row.startTime;
	} else {
		time = row.endTime;
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
function listAppealByTitle() {
	var appealTitle = $("#searchInfoHd").val();
	if (appealTitle == "") {
		return;
	}
	var urlBuffer = new StringBuffer();
	urlBuffer.append("listappeals?appealTitle=");
	urlBuffer.append(encodeURIComponent(encodeURIComponent(appealTitle)));
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#appealManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}

function listAppealByAppealId() {
	var appealId = $("#searchInfoHd").val();
	if (appealId == "") {
		return;
	}
	var urlBuffer = new StringBuffer();
	urlBuffer.append("searchappealbyid?appealId=");
	urlBuffer.append(appealId);
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#appealManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}

function listAppeals() {
	var enalbeStatus = $("#enalbeStatusHd").val();
	var urlBuffer = new StringBuffer();
	urlBuffer.append("listappeals?appealStatus=");
	urlBuffer.append(enalbeStatus);
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#appealManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}

/** --------------table------------------* */
/**
 * 加载表格数据
 */
function ajaxTable() {
	// 加载表格
	$('#appealManagementTable')
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
							$('#appealManagementTable').datagrid('unselectRow',
									rowIndex);
						},
						onLoadSuccess : function() {
							var value = $('#appealManagementTable').datagrid(
									'getData')['errorMsg'];
							if (value != null) {
								alert("错误消息:" + value);
							}
						}
					}).datagrid('acceptChanges');
	var appealStatusBuffer = new StringBuffer()
	appealStatusBuffer
			.append('<select id="appealManagementFilter_appealStatus" class="easyui-combobox" style="margin :2px; padding :4px;" ');
	appealStatusBuffer
			.append('onchange="changeFilterStatus(this.options[this.options.selectedIndex].value)">');
	appealStatusBuffer
			.append('<option id="appealManagementFilter_ALL" value="">全部</option>');
	appealStatusBuffer
			.append('<option id="appealManagementEdit_FIND" value="0">未确定帮助者</option>');
	appealStatusBuffer
			.append('<option id="appealManagementEdit_SELECED" value="1">已确定帮助者</option>');
	appealStatusBuffer
			.append('<option id="appealManagementEdit_FINISH" value="2">已完成</option>');
	appealStatusBuffer
			.append('<option id="appealManagementEdit_DISABLE" value="3">已失效</option>');
	appealStatusBuffer
			.append('<option id="appealManagementEdit_NO" value="4">禁止</option>');
	var searchConditionBuffer = new StringBuffer()
	searchConditionBuffer
			.append('<select id="appealManagementSearch_searchCondition" class="easyui-combobox" style="margin :2px; padding :4px;" ');
	searchConditionBuffer
			.append('onchange="changeSearchCondition(this.options[this.options.selectedIndex].value)">');
	searchConditionBuffer
			.append('<option id="appealManagementSearch_ALL" value="ALL">按筛选条件查询</option>');
	searchConditionBuffer
			.append('<option id="appealManagementSearch_TITLE" value="byAppealTitle">按求助标题查询</option>');
	searchConditionBuffer
			.append('<option id="appealManagementSearch_APPEALID" value="byAppealId">按求助ID查询</option></select>');
	var searchBoxBuffer = new StringBuffer();
	searchBoxBuffer.append('<input type="text" ');
	searchBoxBuffer
			.append('id="appealManagementSearch_searchBox" onchange="changeSearchInfo()"');
	searchBoxBuffer
			.append(' style="resize: none; width: 200px" onkeydown="searchInfoKeyDown(e)"></input>');
	searchBoxBuffer.append('<input type="button" id="searchBtn" value="搜索"');
	searchBoxBuffer
			.append('style="margin-right: 0.5em;" onclick="searchInfo()"/>');
	var addButtonBuffer = new StringBuffer();
	addButtonBuffer
			.append('<input type="button" id="addAppeal" value="添加求助信息"');
	addButtonBuffer
			.append('style="margin-right: 0.5em;" onclick="openDialog()"/>');
	$('.datagrid-toolbar').append(appealStatusBuffer.toString());
	$('.datagrid-toolbar').append(searchConditionBuffer.toString());
	$('.datagrid-toolbar').append(searchBoxBuffer.toString());
	$('.datagrid-toolbar').append(addButtonBuffer.toString());
	$("#appealManagementSearch_searchBox").hide();
	$("#searchBtn").hide();
	// 获取DataGrid分页组件对象
	var p = $("#appealManagementTable").datagrid("getPager");
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
			$('#appealManagementTable').datagrid('loadData', jsonObj);
		}
	});// ajax
}

function changeFilterStatus(status) {
	$('#enalbeStatusHd').val(status);
	searchAppealInfo();
}

function changeSearchCondition(condition) {
	$("#searchConditionHd").val(condition);
	if (condition == "byAppealTitle") {
		$("#enalbeStatusHd").val("");
		$("#appealManagementFilter_appealStatus").hide();
		$("#appealManagementSearch_searchBox").show();
		$("#searchBtn").show();
	} else if (condition == "byAppealId") {
		$("#enalbeStatusHd").val("");
		$("#appealManagementFilter_appealStatus").hide();
		$("#appealManagementSearch_searchBox").show();
		$("#searchBtn").show();
	} else {
		$("#enalbeStatusHd").val("ALL");
		$("#searchInfoHd").val("");
		$("#appealManagementFilter_appealStatus").show();
		$("#appealManagementSearch_searchBox").hide();
		$("#searchBtn").hide();
	}
	searchAppealInfo();
}

function changeSearchInfo() {
	var info = $('#appealManagementSearch_searchBox').val();
	$('#searchInfoHd').val(info);
}

function searchInfoKeyDown(e) {
	if (e.keyCode == 13) {
		searchInfo();
	}
}

function searchInfo() {
	var info = $('#appealManagementSearch_searchBox').val().trim();
	$('#searchInfoHd').val(info);
	if (info == "") {
		return;
	}
	searchAppealInfo();
}
/**
 * 设置操作列的信息 参数说明 value 这个可以不管，但是要使用后面 row 和index 这个参数是必须的 row 当前行的数据 index
 * 当前行的索引 从0 开始
 */
function optFormater(value, row, index) {
	var appealTitle = row.appealTitle;
	var appealId = row.appealId;
	var userId = row.userId;
	var phone = row.phone;
	var appealContent = row.appealContent;
	var province = row.province;
	var city = row.city;
	var district = row.district;
	var fullAddress = row.fullAddress;
	var appealMoreInfo = row.appealMoreInfo;
	var souCoin = row.souCoin;
	var latitude = row.latitude;
	var longitude = row.longitude;
	var appealStatus = row.appealStatus;
	var startTime = timeChange(row.startTime);
	var endTime = timeChange(row.endTime);
	var params = appealId + "," + userId + ",'" + appealTitle + "','" + phone
			+ "','" + appealContent + "','" + province + "','" + city + "','"
			+ district + "','" + fullAddress + "','" + appealMoreInfo + "',"
			+ souCoin + "," + appealStatus + "," + latitude + "," + longitude
			+ ",'" + startTime + "','" + endTime + "'";
	var edit = '<a href="javascript:openDialog_edit(' + params + ')">编辑</a>';
	return edit;
};

/** --------------编辑操作弹出框------------------* */

// 设置弹出框的属性
function setDialog_edit() {
	$('#appealManagementEdit').dialog({
		title : '求助编辑',
		modal : true, // 模式窗口：窗口背景不可操作
		collapsible : true, // 可折叠，点击窗口右上角折叠图标将内容折叠起来
		resizable : true
	});
}
// 打开对话框
function openDialog_edit(appealId, userId, appealTitle, phone, appealContent,
		province, city, district, fullAddress, appealMoreInfo, souCoin,
		appealStatus, latitude, longitude, startTime, endTime) {
	appealManagementEditReset(appealId, userId, appealTitle, phone,
			appealContent, province, city, district, fullAddress,
			appealMoreInfo, souCoin, appealStatus, latitude, longitude,
			startTime, endTime);
	$('#appealManagementEdit').dialog('open');
}
function openDialog() {
	appealManagementEditReset(null, null, null, null, null, null, null, null,
			null, null, null, null, null, null, null, null);
	$('#appealManagementEdit').dialog('open');
}
// 关闭对话框
function closeDialog_edit() {
	$('#appealManagementEdit').dialog('close');
}
// 根据用户id查询用户的信息
function appealManagementEditReset(appealId, userId, appealTitle, phone,
		appealContent, province, city, district, fullAddress, appealMoreInfo,
		souCoin, appealStatus, latitude, longitude, startTime, endTime) {
	$("#appealManagementEdit_message").html("");
	/*
	 * $("#appealManagementEdit_appealTitle").val(appealTitle);
	 * $("#appealManagementEdit_appealId").val(appealId);
	 * $("#appealManagementEdit_appealStatus").val(appealStatus);
	 */
	$('#form_appealManagementEdit').form('load', {
		appealId : appealId,
		userId : userId,
		appealTitle : appealTitle,
		phone : phone,
		appealContent : appealContent,
		province : province,
		city : city,
		district : district,
		fullAddress : fullAddress,
		appealMoreInfo : appealMoreInfo,
		souCoin : souCoin,
		appealStatus : appealStatus,
		latitude : latitude,
		longitude : longitude
	});
	$('#startTime').datetimebox('setValue', startTime);
	$('#endTime').datetimebox('setValue', endTime);
}
// 执行用户编辑操作
function appealManagementEdit() {
	var appealId = $('#appealId').val()
	if (appealId != '') {
		$('#form_appealManagementEdit').form('submit', {
			url : 'modifyappeal',
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
					searchAppealInfo();
					$("#appealManagementEdit_message").html(messgage);
				} else {
					var messgage = data.errMsg;
					$("#appealManagementEdit_message").html(messgage);
				}
			}
		});
	} else {
		$('#form_appealManagementEdit').form('submit', {
			url : 'addappeal',
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
					searchAppealInfo();
					$("#appealManagementEdit_message").html(messgage);
				} else {
					var messgage = data.errMsg;
					$("#appealManagementEdit_message").html(messgage);
				}
			}
		});
	}
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
			searchAppealInfo();
		}
	});
}