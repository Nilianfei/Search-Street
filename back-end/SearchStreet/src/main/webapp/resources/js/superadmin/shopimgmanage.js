$(function() {
	initializePage();
});

function initializePage() {
	// 加载表格数据
	ajaxTable();
	// 初始化弹出层
	setDialog_add();
	closeDialog_add();
	searchShopImgInfo();
}

function searchShopImgInfo() {
	var shopImgId = $("#searchInfoHd").val();
	if (shopImgId != '') {
		listShopImgByShopId();
	} else {
		listShopImgs();
	}
}

function imgFormater(value, row, index) {
	if (value == null || value == '') {
		return '';
	}
	var img;
	if (value == row.imgAddr)
		img = row.imgAddr;
	return '<img src="/image' + img + '" width="100px" height="100px">';
}

function timeFormater(value, row, index) {
	if (value == null || value == '') {
		return '';
	}
	var time;
	if (value == row.createTime) {
		time = row.createTime;
	} else {
		time = row.lastAddTime;
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

function listShopImgByShopId() {
	var shopId = $("#searchInfoHd").val();
	if (shopId == "") {
		return;
	}
	var urlBuffer = new StringBuffer();
	urlBuffer.append("searchshopimgsbyshopid?shopId=");
	urlBuffer.append(shopId+"&");
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#shopImgManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}

function listShopImgs() {
	var urlBuffer = new StringBuffer();
	urlBuffer.append("listshopimgs?");
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#shopImgManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}

/** --------------table------------------* */
/**
 * 加载表格数据
 */
function ajaxTable() {
	// 加载表格
	$('#shopImgManagementTable').datagrid(
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
					$('#shopImgManagementTable').datagrid('unselectRow',
							rowIndex);
				},
				onLoadSuccess : function() {
					var value = $('#shopImgManagementTable')
							.datagrid('getData')['errorMsg'];
					if (value != null) {
						alert("错误消息:" + value);
					}
				}
			}).datagrid('acceptChanges');
	var searchBoxBuffer = new StringBuffer();
	searchBoxBuffer.append('<input type="text" ');
	searchBoxBuffer
			.append('id="shopImgManagementSearch_searchBox" onchange="changeSearchInfo()"');
	searchBoxBuffer.append(' style="resize: none; width: 200px"></input>');
	searchBoxBuffer
			.append('<input type="button" id="searchBtn" value="按商铺ID搜索"');
	searchBoxBuffer
			.append('style="margin-right: 0.5em;" onclick="searchInfo()"/>');
	var addButtonBuffer = new StringBuffer();
	addButtonBuffer
			.append('<input type="button" id="addShopImg" value="添加商铺图片"');
	addButtonBuffer
			.append('style="margin-right: 0.5em;" onclick="openDialog_add()"/>');
	$('.datagrid-toolbar').append(searchBoxBuffer.toString());
	$('.datagrid-toolbar').append(addButtonBuffer.toString());
	// 获取DataGrid分页组件对象
	var p = $("#shopImgManagementTable").datagrid("getPager");
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
			$('#shopImgManagementTable').datagrid('loadData', jsonObj);
		}
	});// ajax
}

function changeSearchInfo() {
	var info = $('#shopImgManagementSearch_searchBox').val();
	$('#searchInfoHd').val(info);
}

function searchInfo() {
	var info = $('#shopImgManagementSearch_searchBox').val().trim();
	$('#searchInfoHd').val(info);
	if (info == "") {
		searchShopImgInfo();
	}
	searchShopImgInfo();
}
/**
 * 设置操作列的信息 参数说明 value 这个可以不管，但是要使用后面 row 和index 这个参数是必须的 row 当前行的数据 index
 * 当前行的索引 从0 开始
 */
function optFormater(value, row, index) {
	var shopImgId = row.shopImgId;
	var add = '<a href="javascript:delShopImg(' + shopImgId + ')">删除</a>';
	return add;
};

/** --------------编辑操作弹出框------------------* */

// 设置弹出框的属性
function setDialog_add() {
	$('#shopImgManagementAdd').dialog({
		title : '商铺图片添加',
		modal : true, // 模式窗口：窗口背景不可操作
		collapsible : true, // 可折叠，点击窗口右上角折叠图标将内容折叠起来
		resizable : true
	});
}
// 打开对话框
function openDialog_add() {
	shopImgManagementAddReset();
	$('#shopImgManagementAdd').dialog('open');
}
// 关闭对话框
function closeDialog_add() {
	$('#shopImgManagementAdd').dialog('close');
}
// 根据用户id查询用户的信息
function shopImgManagementAddReset() {
	$("#shopImgManagementAdd_message").html("");
	$('#form_shopImgManagementAdd').form('load', {
		shopImgId : null,
		shopId : null,
		imgAddr : null
	});
}
// 执行用户编辑操作
function shopImgManagementAdd() {
	$('#form_shopImgManagementAdd').form('submit', {
		url : 'addshopimg',
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
				searchShopImgInfo();
				$("#shopImgManagementAdd_message").html(messgage);
			} else {
				var messgage = data.errMsg;
				$("#shopImgManagementAdd_message").html(messgage);
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
function delShopImg(shopImgId) {
	$.ajax({
		async : false,
		cache : false,
		type : 'post',
		dataType : "json",
		url : 'delshopimg?shopImgId=' + shopImgId,// 请求的action路径
		error : function() {// 请求失败处理函数
			alert('请求失败');
		},
		success : function(data) {
			var data = eval('(' + data + ')'); // change the JSON string to
			// javascript object
			if (data.success) {
				alert("操作成功");
				searchShopImgInfo();
			} else {
				alert(data.errMsg);
			}
		}
	});
	searchShopImgInfo();
}