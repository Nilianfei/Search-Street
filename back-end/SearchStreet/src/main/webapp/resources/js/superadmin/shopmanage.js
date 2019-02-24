$(function() {
	initializePage();
});

function initializePage() {
	// 加载表格数据
	ajaxTable();
	// 初始化弹出层
	setDialog_edit();
	closeDialog_edit();
	searchShopInfo();
}

function searchShopInfo() {
	var enalbeStatus = $("#enalbeStatusHd").val();
	var searchCondition = $("#searchConditionHd").val();
	if (searchCondition == "byShopName") {
		listShopByName();
	} else if (searchCondition == "byShopId") {
		listShopByShopId();
	} else {
		listShops();
	}
}

function imgFormater(value, row, index) {
	if (value == null || value == '') {
		return '';
	}
	var img;
	if (value == row.profileImg)
		img = row.profileImg;
	else
		img = row.businessLicenseImg;
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
		time = row.lastEditTime;
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

function listShopByName() {
	var shopName = $("#searchInfoHd").val();
	if (shopName == "") {
		return;
	}
	var urlBuffer = new StringBuffer();
	urlBuffer.append("listshops?shopName=");
	urlBuffer.append(encodeURIComponent(encodeURIComponent(shopName)));
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#shopManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}

function listShopByShopId() {
	var shopId = $("#searchInfoHd").val();
	if (shopId == "") {
		return;
	}
	var urlBuffer = new StringBuffer();
	urlBuffer.append("searchshopbyid?shopId=");
	urlBuffer.append(shopId);
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#shopManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}

function listShops() {
	var enalbeStatus = $("#enalbeStatusHd").val();
	var urlBuffer = new StringBuffer();
	urlBuffer.append("listshops?enableStatus=");
	urlBuffer.append(enalbeStatus);
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#shopManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}

/** --------------table------------------* */
/**
 * 加载表格数据
 */
function ajaxTable() {
	// 加载表格
	$('#shopManagementTable')
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
							$('#shopManagementTable').datagrid('unselectRow',
									rowIndex);
						},
						onLoadSuccess : function() {
							var value = $('#shopManagementTable').datagrid(
									'getData')['errorMsg'];
							if (value != null) {
								alert("错误消息:" + value);
							}
						}
					}).datagrid('acceptChanges');
	var enableStatusBuffer = new StringBuffer()
	enableStatusBuffer
			.append('<select id="shopManagementFilter_enableStatus" class="easyui-combobox" style="margin :2px; padding :4px;" ');
	enableStatusBuffer
			.append('onchange="changeFilterStatus(this.options[this.options.selectedIndex].value)">');
	enableStatusBuffer
			.append('<option id="shopManagementFilter_ALL" value="">全部</option>');
	enableStatusBuffer
			.append('<option id="shopManagementFilter_YES" value="1">启用</option>');
	enableStatusBuffer
			.append('<option id="shopManagementFilter_APPLY" value="0">待审核</option>');
	enableStatusBuffer
			.append('<option id="shopManagementFilter_NO" value="2">禁用</option></select>');
	var searchConditionBuffer = new StringBuffer()
	searchConditionBuffer
			.append('<select id="shopManagementSearch_searchCondition" class="easyui-combobox" style="margin :2px; padding :4px;" ');
	searchConditionBuffer
			.append('onchange="changeSearchCondition(this.options[this.options.selectedIndex].value)">');
	searchConditionBuffer
			.append('<option id="shopManagementSearch_ALL" value="ALL">按筛选条件查询</option>');
	searchConditionBuffer
			.append('<option id="shopManagementSearch_NAME" value="byShopName">按商铺名称查询</option>');
	searchConditionBuffer
			.append('<option id="shopManagementSearch_SHOPID" value="byShopId">按商铺ID查询</option></select>');
	var searchBoxBuffer = new StringBuffer();
	searchBoxBuffer.append('<input type="text" ');
	searchBoxBuffer
			.append('id="shopManagementSearch_searchBox" onchange="changeSearchInfo()"');
	searchBoxBuffer
			.append(' style="resize: none; width: 200px" onkeydown="searchInfoKeyDown(e)"></input>');
	searchBoxBuffer.append('<input type="button" id="searchBtn" value="搜索"');
	searchBoxBuffer
			.append('style="margin-right: 0.5em;" onclick="searchInfo()"/>');
	$('.datagrid-toolbar').append(enableStatusBuffer.toString());
	$('.datagrid-toolbar').append(searchConditionBuffer.toString());
	$('.datagrid-toolbar').append(searchBoxBuffer.toString());
	$("#shopManagementSearch_searchBox").hide();
	$("#searchBtn").hide();
	// 获取DataGrid分页组件对象
	var p = $("#shopManagementTable").datagrid("getPager");
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
			$('#shopManagementTable').datagrid('loadData', jsonObj);
		}
	});// ajax
}

function changeFilterStatus(status) {
	$('#enalbeStatusHd').val(status);
	searchShopInfo();
}

function changeSearchCondition(condition) {
	$("#searchConditionHd").val(condition);
	if (condition == "byShopName") {
		$("#enalbeStatusHd").val("");
		$("#shopManagementFilter_enableStatus").hide();
		$("#shopManagementSearch_searchBox").show();
		$("#searchBtn").show();
	} else if (condition == "byShopId") {
		$("#enalbeStatusHd").val("");
		$("#shopManagementFilter_enableStatus").hide();
		$("#shopManagementSearch_searchBox").show();
		$("#searchBtn").show();
	} else {
		$("#enalbeStatusHd").val("ALL");
		$("#searchInfoHd").val("");
		$("#shopManagementFilter_enableStatus").show();
		$("#shopManagementSearch_searchBox").hide();
		$("#searchBtn").hide();
	}
	searchShopInfo();
}

function changeSearchInfo() {
	var info = $('#shopManagementSearch_searchBox').val();
	$('#searchInfoHd').val(info);
}

function searchInfoKeyDown(e) {
	if (e.keyCode == 13) {
		searchInfo();
	}
}

function searchInfo() {
	var info = $('#shopManagementSearch_searchBox').val().trim();
	$('#searchInfoHd').val(info);
	if (info == "") {
		return;
	}
	searchShopInfo();
}
/**
 * 设置操作列的信息 参数说明 value 这个可以不管，但是要使用后面 row 和index 这个参数是必须的 row 当前行的数据 index
 * 当前行的索引 从0 开始
 */
function optFormater(value, row, index) {
	var shopName = row.shopName;
	var shopId = row.shopId;
	var enableStatus = row.enableStatus;
	var params = shopId + "," + enableStatus + ",'" + shopName + "'";
	var edit = '<a href="javascript:openDialog_edit(' + params + ')">编辑</a>';
	return edit;
};

/** --------------编辑操作弹出框------------------* */

// 设置弹出框的属性
function setDialog_edit() {
	$('#shopManagementEdit').dialog({
		title : '商铺编辑',
		modal : true, // 模式窗口：窗口背景不可操作
		collapsible : true, // 可折叠，点击窗口右上角折叠图标将内容折叠起来
		resizable : true
	});
}
// 打开对话框
function openDialog_edit(shopId, enableStatus, shopName) {
	shopManagementEditReset(shopId, enableStatus, shopName);
	$('#shopManagementEdit').dialog('open');
}
// 关闭对话框
function closeDialog_edit() {
	$('#shopManagementEdit').dialog('close');
}
// 根据用户id查询用户的信息
function shopManagementEditReset(shopId, enableStatus, shopName) {
	$("#shopManagementEdit_message").html("");
	$("#shopManagementEdit_shopName").val(shopName);
	$("#shopManagementEdit_shopId").val(shopId);
	$("#shopManagementEdit_enableStatus").val(enableStatus);
}
// 执行用户编辑操作
function shopManagementEdit() {
	var validateResult = true;
	// easyui 表单验证
	$('#table_shopManagementEdit input').each(function() {
		if ($(this).attr('required') || $(this).attr('validType')) {
			if (!$(this).validatebox('isValid')) {
				// 如果验证不通过，则返回false
				validateResult = false;
				return;
			}
		}
	});
	if (validateResult == false) {
		return;
	}
	var shop = {};
	shop.shopId = encodeURIComponent($("#shopManagementEdit_shopId").val());
	shop.enableStatus = encodeURIComponent($("#shopManagementEdit_enableStatus")
			.val());
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		dataType : "json",
		data : {
			shopStr : JSON.stringify(shop)
		},
		url : 'modifyshop',// 请求的action路径
		error : function() {// 请求失败处理函数
			alert('请求失败');
		},
		success : function() {
			var messgage = "修改成功!";
			searchShopInfo();
			$("#shopManagementEdit_message").html(messgage);
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
			searchShopInfo();
		}
	});
}
