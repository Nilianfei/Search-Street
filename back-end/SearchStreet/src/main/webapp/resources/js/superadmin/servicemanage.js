$(function() {
	initializePage();
});

function initializePage() {
	// 加载表格数据
	ajaxTable();
	// 初始化弹出层
	setDialog_edit();
	closeDialog_edit();
	searchServiceInfo();
}

function searchServiceInfo() {
	var serviceStatus = $("#serviceStatusHd").val();
	var searchCondition = $("#searchConditionHd").val();
	if (searchCondition == "byServiceName") {
		listServiceByServiceName();
	} 
	else if (searchCondition == "byServiceContent") {
		listServiceByServiceContent();
	} else if (searchCondition == "byServiceId") {
		listServiceByServiceId();
	}
	else if (searchCondition == "byShopId") {
		listServiceByShopId();
	}else {
		listServices();
	}
}

function listServiceByServiceName() {
	var serviceName = $("#searchInfoHd").val();
	if (serviceName == "") {
		return;
	}
	var urlBuffer = new StringBuffer();
	urlBuffer.append("listservices?serviceName=");
	urlBuffer.append(encodeURIComponent(encodeURIComponent(serviceName)));
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#serviceManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}
function listServiceByServiceContent() {
	var serviceContent = $("#searchInfoHd").val();
	if (serviceContent == "") {
		return;
	}
	var urlBuffer = new StringBuffer();
	urlBuffer.append("listservices?serviceContent=");
	urlBuffer.append(encodeURIComponent(encodeURIComponent(serviceContent)));
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#serviceManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}
function listServiceByShopId() {
	var shopId = $("#searchInfoHd").val();
	if (shopId == "") {
		return;
	}
	var urlBuffer = new StringBuffer();
	urlBuffer.append("searchservicebyshopid?shopId=");
	urlBuffer.append(shopId);
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#serviceManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}
function listServiceByServiceId() {
	var serviceId = $("#searchInfoHd").val();
	if (serviceId == "") {
		return;
	}
	var urlBuffer = new StringBuffer();
	urlBuffer.append("searchservicebyid?serviceId=");
	urlBuffer.append(serviceId);
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#serviceManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}
function listServices() {
	var urlBuffer = new StringBuffer();
	urlBuffer.append("listservices?id=");
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#serviceManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}

/** --------------table------------------* */
/**
 * 加载表格数据
 */
function ajaxTable() {
	// 加载表格
	$('#serviceManagementTable')
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
							$('#serviceManagementTable').datagrid('unselectRow',
									rowIndex);
						},
						onLoadSuccess : function() {
							var value = $('#serviceManagementTable').datagrid(
									'getData')['errorMsg'];
							if (value != null) {
								alert("错误消息:" + value);
							}
						}
					}).datagrid('acceptChanges');
	var searchConditionBuffer = new StringBuffer()
	searchConditionBuffer
			.append('<select id="serviceManagementSearch_searchCondition" class="easyui-combobox" style="margin :2px; padding :4px;" ');
	searchConditionBuffer
			.append('onchange="changeSearchCondition(this.options[this.options.selectedIndex].value)">');
	searchConditionBuffer
			.append('<option id="serviceManagementSearch_ALL" value="ALL">全部</option>');
	searchConditionBuffer
			.append('<option id="serviceManagementSearch_SERVICENAME" value="byServiceName">按服务名称查询</option>');
	searchConditionBuffer
		.append('<option id="serviceManagementSearch_SERVICECONTENT" value="byServiceContent">按服务内容查询</option>');
	searchConditionBuffer
		.append('<option id="serviceManagementSearch_SHOPID" value="byShopId">按店铺ID查询</option>');
	searchConditionBuffer
			.append('<option id="serviceManagementSearch_SERVICEID" value="byServiceId">按服务ID查询</option></select>');
	var searchBoxBuffer = new StringBuffer();
	searchBoxBuffer.append('<input type="text" ');
	searchBoxBuffer
			.append('id="serviceManagementSearch_searchBox" onchange="changeSearchInfo()"');
	searchBoxBuffer
			.append(' style="resize: none; width: 200px"></input>');
	searchBoxBuffer.append('<input type="button" id="searchBtn" value="搜索"');
	searchBoxBuffer
			.append('style="margin-right: 0.5em;" onclick="searchInfo()"/>');
	var addButtonBuffer = new StringBuffer();
	addButtonBuffer
			.append('<input type="button" id="addService" value="添加服务信息"');
	addButtonBuffer
			.append('style="margin-right: 0.5em;" onclick="openDialog()"/>');
	$('.datagrid-toolbar').append(searchConditionBuffer.toString());
	$('.datagrid-toolbar').append(searchBoxBuffer.toString());
	$('.datagrid-toolbar').append(addButtonBuffer.toString());
	$("#serviceManagementSearch_searchBox").hide();
	$("#searchBtn").hide();
	// 获取DataGrid分页组件对象
	var p = $("#serviceManagementTable").datagrid("getPager");
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
			$('#serviceManagementTable').datagrid('loadData', jsonObj);
		}
	});// ajax
}



function changeSearchCondition(condition) {
	$("#searchConditionHd").val(condition);
	if (condition == "byServiceName") {
		$("#serviceManagementSearch_searchBox").show();
		$("#searchBtn").show();
	}
	else if (condition == "byServiceContent") {
		$("#serviceManagementSearch_searchBox").show();
		$("#searchBtn").show();
	} 
	else if (condition == "byServiceId") {
		$("#serviceManagementSearch_searchBox").show();
		$("#searchBtn").show();
	} 
	else if (condition == "byShopId") {
		$("#serviceManagementSearch_searchBox").show();
		$("#searchBtn").show();
	}else {
		$("#searchInfoHd").val("");
		$("#serviceManagementSearch_searchBox").hide();
		$("#searchBtn").hide();
	}
	searchServiceInfo();
}

function changeSearchInfo() {
	var info = $('#serviceManagementSearch_searchBox').val();
	$('#searchInfoHd').val(info);
}



function searchInfo() {
	var info = $('#serviceManagementSearch_searchBox').val().trim();
	$('#searchInfoHd').val(info);
	if (info == "") {
		return;
	}
	searchServiceInfo();
}
/**
 * 设置操作列的信息 参数说明 value 这个可以不管，但是要使用后面 row 和index 这个参数是必须的 row 当前行的数据 index
 * 当前行的索引 从0 开始
 */
function optFormater(value, row, index) {
	var serviceId=row.serviceId;
	var shopId = row.shopId;
	var serviceName= row.serviceName;
	var servicePrice = row.servicePrice;
	var servicePriority= row.servicePriority;
	var serviceDesc= row.serviceDesc;
	var serviceContent= row.serviceContent;
	var params = serviceId+","+ shopId + ",'" + serviceName + "'," + servicePrice
			+ "," + servicePriority+",'"+serviceDesc+"','"+serviceContent+ "'" ;
	var edit = '<a href="javascript:openDialog_edit(' + params + ')">编辑</a>'+"    "+'<a href="javascript:doDel(' + serviceId+ ')">删除</a>';
	return edit;
};
function doDel(serviceId) {
	$.messager.confirm('删除提示', '你确定永久删除该数据吗?', function(r) {
		if (r) {
			var url = 'deleteservice?serviceId=' + serviceId;
			changeStatus(url);
		}
	});
}

/** --------------编辑操作弹出框------------------* */

// 设置弹出框的属性
function setDialog_edit() {
	$('#serviceManagementEdit').dialog({
		title : '服务编辑',
		modal : true, // 模式窗口：窗口背景不可操作
		collapsible : true, // 可折叠，点击窗口右上角折叠图标将内容折叠起来
		resizable : true
	});
}
// 打开对话框
function openDialog_edit(serviceId,shopId,serviceName,servicePrice,servicePriority,serviceDesc, serviceContent) {
	serviceManagementEditReset(serviceId,shopId,serviceName,servicePrice,servicePriority,serviceDesc, serviceContent);
	$('#serviceManagementEdit').dialog('open');
}
function openDialog() {
	serviceManagementEditReset(null, null, null, null, null, null, "", "");
	$('#serviceManagementEdit').dialog('open');
}
// 关闭对话框
function closeDialog_edit() {
	$('#serviceManagementEdit').dialog('close');
}
// 根据用户id查询用户的信息
function serviceManagementEditReset(serviceId,shopId,serviceName,servicePrice,servicePriority,serviceDesc, serviceContent) {
	$("#serviceManagementEdit_message").html("");
	/*
	 * $("#serviceManagementEdit_serviceTitle").val(serviceTitle);
	 * $("#serviceManagementEdit_serviceId").val(serviceId);
	 * $("#serviceManagementEdit_serviceStatus").val(serviceStatus);
	 */
	$('#form_serviceManagementEdit').form('load', {
		serviceId:serviceId,
		shopId : shopId,
		serviceName : serviceName,
		servicePrice:servicePrice,
		servicePriority:servicePriority,
		serviceDesc:serviceDesc,
		serviceContent:serviceContent
	});
}
// 执行用户编辑操作
function serviceManagementEdit() {
	var serviceId = $('#serviceId').val()
	if (serviceId != '') {
		$('#form_serviceManagementEdit').form('submit', {
			url : 'modifyservice',
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
					searchServiceInfo();
					$("#serviceManagementEdit_message").html(messgage);
				} else {
					var messgage = data.errMsg;
					$("#serviceManagementEdit_message").html(messgage);
				}
			}
		});
	} else {
		$('#form_serviceManagementEdit').form('submit', {
			url : 'addservice',
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
					searchServiceInfo();
					$("#serviceManagementEdit_message").html(messgage);
				} else {
					var messgage = data.errMsg;
					$("#serviceManagementEdit_message").html(messgage);
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
			searchServiceInfo();
		}
	});
}
