$(function() {
	initializePage();
});
var os=-1;
function initializePage() {
	// 加载表格数据
	ajaxTable();
	// 初始化弹出层
	setDialog_edit();
	closeDialog_edit();
	searchOrderInfo();
}

function searchOrderInfo() {
	var searchCondition = $("#searchConditionHd").val();
	$('#enalbeStatusHd').val(os);
	if (searchCondition == "byServiceName") {
		listOrderByServiceName();
	} else if (searchCondition == "byOrderId") {
		listOrderByOrderId();
	} else if (searchCondition == "byServiceId") {
		listOrderByServiceId();
	}  else if (searchCondition == "byUserId") {
		listOrderByUserId();
	}  else {
		listOrders();
	}
}

function statusFormater(value, row, index) {
	if (row.orderStatus == 0)
		return '进行中';
	else if (row.orderStatus == 1)
		return '待评价';
	else if (row.orderStatus == 2)
		return '已完成';
	else if (row.orderStatus == 3)
		return '已取消';
	else
		return "全部";
}

function timeFormater(value, row, index) {
	if (value == null || value == '') {
		return '';
	}
	var time;
	if (value == row.createTime) {
		time = row.createTime;
	} else {
		time = row.overTime;
	}
	//return timeChange(time);
	return formatDate(time);
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
/*
 * 时间戳转换为yyyy-MM-dd hh:mm:ss 格式  formatDate()
 * inputTime   时间戳
 */
function formatDate(inputTime) {
  var time=JSON.stringify(inputTime);
  if (time == null) {
		return '';
	}
  var strlist=time.split(",");
  var y = strlist[0].substr(1,4);
  var m = strlist[1];
  m = m < 10 ? ('0' + m) : m;
  var d = strlist[2];
  d = d < 10 ? ('0' + d) : d;
  var h = strlist[3];
  h = h < 10 ? ('0' + h) : h;
  var minute='0';
  var second = '0';
  //它会自动把秒等于0的时间传值过程中忽略掉
  if(strlist.length==5)
  {
    minute = strlist[4].substr(0, strlist[4].length - 1);
  }
  else
  {
    minute = strlist[4];
    second = strlist[5].substr(0, strlist[5].length - 1);
  }   
  minute = minute < 10 ? ('0' + minute) : minute;
  second = second < 10 ? ('0' + second) : second;
  return y + '-' + m + '-' + d + ' ' + h + ':' + minute + ':' + second;
}

function listOrderByServiceName() {
	var enalbeStatus = $("#enalbeStatusHd").val();
	var urlBuffer = new StringBuffer();
	urlBuffer.append("listorders?orderStatus=");
	urlBuffer.append(enalbeStatus);
	var serviceName = $("#searchInfoHd").val();
	if (serviceName == "") {
		return;
	}
	urlBuffer.append("&serviceName=");
	urlBuffer.append(encodeURIComponent(encodeURIComponent(serviceName)));
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#orderManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}

function listOrderByOrderId() {
	var orderId = $("#searchInfoHd").val();
	if (orderId == "") {
		return;
	}
	var urlBuffer = new StringBuffer();
	urlBuffer.append("searchorderbyid?orderId=");
	urlBuffer.append(orderId);
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#orderManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}
function listOrderByServiceId() {
	var enalbeStatus = $("#enalbeStatusHd").val();
	var urlBuffer = new StringBuffer();
	urlBuffer.append("searchorderbyserviceid?orderStatus=");
	urlBuffer.append(enalbeStatus);
	var serviceId = $("#searchInfoHd").val();
	if (serviceId  == "") {
		return;
	};
	urlBuffer.append("&serviceId=");
	urlBuffer.append(serviceId);
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#orderManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}
function listOrderByUserId() {
	var enalbeStatus = $("#enalbeStatusHd").val();
	var urlBuffer = new StringBuffer();
	urlBuffer.append("searchorderbyuserid?orderStatus=");
	urlBuffer.append(enalbeStatus);
	var userId = $("#searchInfoHd").val();
	if (userId  == "") {
		return;
	}
	urlBuffer.append("&userId=");
	urlBuffer.append(userId);
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#orderManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}

function listOrders() {
	var enalbeStatus = $("#enalbeStatusHd").val();
	var urlBuffer = new StringBuffer();
	urlBuffer.append("listorders?orderStatus=");
	urlBuffer.append(enalbeStatus);
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#orderManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}

/** --------------table------------------* */
/**
 * 加载表格数据
 */
function ajaxTable() {
	// 加载表格
	$('#orderManagementTable')
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
							$('#orderManagementTable').datagrid('unselectRow',
									rowIndex);
						},
						onLoadSuccess : function() {
							var value = $('#orderManagementTable').datagrid(
									'getData')['errorMsg'];
							if (value != null) {
								alert("错误消息:" + value);
							}
						}
					}).datagrid('acceptChanges');
	var orderStatusBuffer = new StringBuffer()
	orderStatusBuffer
			.append('<select id="orderManagementFilter_orderStatus" class="easyui-combobox" style="margin :2px; padding :4px;" ');
	orderStatusBuffer
			.append('onchange="changeFilterStatus(this.options[this.options.selectedIndex].value)">');
	orderStatusBuffer
			.append('<option id="orderManagementFilter_ALL" value="">全部</option>');
	orderStatusBuffer
			.append('<option id="orderManagementEdit_ING" value="0">进行中</option>');
	orderStatusBuffer
			.append('<option id="orderManagementEdit_TOBECOMMENTED" value="1">待评价</option>');
	orderStatusBuffer
			.append('<option id="orderManagementEdit_FINISH" value="2">已完成</option>');
	orderStatusBuffer
			.append('<option id="orderManagementEdit_CANCEL" value="3">已取消</option>');
	var searchConditionBuffer = new StringBuffer()
	searchConditionBuffer
			.append('<select id="orderManagementSearch_searchCondition" class="easyui-combobox" style="margin :2px; padding :4px;" ');
	searchConditionBuffer
			.append('onchange="changeSearchCondition(this.options[this.options.selectedIndex].value)">');
	searchConditionBuffer
			.append('<option id="orderManagementSearch_ALL" value="ALL">按筛选条件查询</option>');
	searchConditionBuffer
			.append('<option id="orderManagementSearch_SERVICENAME" value="byServiceName">按服务名称查询</option>');
	searchConditionBuffer
	.append('<option id="orderManagementSearch_SERVICEID" value="byServiceId">按服务ID查询</option>');
	searchConditionBuffer
	.append('<option id="orderManagementSearch_USERID" value="byUserId">按用户ID查询</option>');
	searchConditionBuffer
			.append('<option id="orderManagementSearch_ORDERID" value="byOrderId">按订单ID查询</option></select>');
	var searchBoxBuffer = new StringBuffer();
	searchBoxBuffer.append('<input type="text" ');
	searchBoxBuffer
			.append('id="orderManagementSearch_searchBox" onchange="changeSearchInfo()"');
	searchBoxBuffer
			.append(' style="resize: none; width: 200px" ></input>');
	searchBoxBuffer.append('<input type="button" id="searchBtn" value="搜索"');
	searchBoxBuffer
			.append('style="margin-right: 0.5em;" onclick="searchInfo()"/>');
	var addButtonBuffer = new StringBuffer();
	addButtonBuffer
			.append('<input type="button" id="addOrder" value="添加订单信息"');
	addButtonBuffer
			.append('style="margin-right: 0.5em;" onclick="openDialog()"/>');
	$('.datagrid-toolbar').append(orderStatusBuffer.toString());
	$('.datagrid-toolbar').append(searchConditionBuffer.toString());
	$('.datagrid-toolbar').append(searchBoxBuffer.toString());
	$('.datagrid-toolbar').append(addButtonBuffer.toString());
	$("#orderManagementSearch_searchBox").hide();
	$("#searchBtn").hide();
	// 获取DataGrid分页组件对象
	var p = $("#orderManagementTable").datagrid("getPager");
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
			$('#orderManagementTable').datagrid('loadData', jsonObj);
		}
	});// ajax
}

function changeFilterStatus(status) {
	os=status;
	searchOrderInfo();
	
}

function changeSearchCondition(condition) {
	$("#searchConditionHd").val(condition);
	if (condition == "byServiceName") {
		$("#enalbeStatusHd").val("");
		$("#orderManagementFilter_orderStatus").show();
		$("#orderManagementSearch_searchBox").show();
		$("#searchBtn").show();
	} else if (condition == "byOrderId") {
		$("#enalbeStatusHd").val("");
		$("#orderManagementFilter_orderStatus").hide();
		$("#orderManagementSearch_searchBox").show();
		$("#searchBtn").show();
	}else if (condition == "byServiceId") {
		$("#enalbeStatusHd").val("");
		$("#orderManagementFilter_orderStatus").show();
		$("#orderManagementSearch_searchBox").show();
		$("#searchBtn").show();
	}else if (condition == "byUserId") {
		$("#enalbeStatusHd").val("");
		$("#orderManagementFilter_orderStatus").show();
		$("#orderManagementSearch_searchBox").show();
		$("#searchBtn").show();
	} else {
		$("#enalbeStatusHd").val("ALL");
		$("#searchInfoHd").val("");
		$("#orderManagementFilter_orderStatus").show();
		$("#orderManagementSearch_searchBox").hide();
		$("#searchBtn").hide();
	}
	searchOrderInfo();
}

function changeSearchInfo() {
	var info = $('#orderManagementSearch_searchBox').val();
	$('#searchInfoHd').val(info);
}


function searchInfo() {
	var info = $('#orderManagementSearch_searchBox').val().trim();
	$('#searchInfoHd').val(info);
	if (info == "") {
		return;
	}
	searchOrderInfo();
}
/**
 * 设置操作列的信息 参数说明 value 这个可以不管，但是要使用后面 row 和index 这个参数是必须的 row 当前行的数据 index
 * 当前行的索引 从0 开始
 */
function optFormater(value, row, index) {
	var orderId = row.orderId;
	var serviceId=row.serviceId;
	var userId = row.userId;
	var serviceName= row.serviceName;
	var serviceCount= row.serviceCount;
	var orderPrice = row.orderPrice;
	var orderStatus = row.orderStatus;
	var createTime = formatDate(row.createTime);
	var overTime = formatDate(row.overTime);
	var params = orderId + "," +serviceId+","+ userId + ",'" + serviceName + "'," + serviceCount
			+ "," + orderPrice+","+orderStatus +",'"+ createTime + "','" + overTime + "'";
	var edit = '<a href="javascript:openDialog_edit(' + params + ')">编辑</a>'+"    "+'<a href="javascript:doDel(' + orderId+ ')">删除</a>';
	return edit;
};
function doDel(orderId) {
	$.messager.confirm('删除提示', '你确定永久删除该数据吗?', function(r) {
		if (r) {
			var url = 'deleteorder?orderId=' + orderId;
			changeStatus(url);
		}
	});
}
/** --------------编辑操作弹出框------------------* */

// 设置弹出框的属性
function setDialog_edit() {
	$('#orderManagementEdit').dialog({
		title : '订单编辑',
		modal : true, // 模式窗口：窗口背景不可操作
		collapsible : true, // 可折叠，点击窗口右上角折叠图标将内容折叠起来
		resizable : true
	});
}
// 打开对话框
function openDialog_edit(orderId, serviceId,userId,serviceName,serviceCount,orderPrice,orderStatus,createTime,overTime) {
	orderManagementEditReset(orderId, serviceId,userId,serviceName,serviceCount,orderPrice,orderStatus,createTime,overTime);
	$('#orderManagementEdit').dialog('open');
}
function openDialog() {
	orderManagementEditReset(null, null, null, null, null, null, null, null,
			null);
	$('#orderManagementEdit').dialog('open');
}
// 关闭对话框
function closeDialog_edit() {
	$('#orderManagementEdit').dialog('close');
}
// 根据用户id查询用户的信息
function orderManagementEditReset(orderId, serviceId,userId,serviceName,serviceCount,orderPrice,orderStatus,createTime,overTime) {
	$("#orderManagementEdit_message").html("");
	/*
	 * $("#orderManagementEdit_orderTitle").val(orderTitle);
	 * $("#orderManagementEdit_orderId").val(orderId);
	 * $("#orderManagementEdit_orderStatus").val(orderStatus);
	 */
	$('#form_orderManagementEdit').form('load', {
		orderId : orderId,
		serviceId:serviceId,
		userId : userId,
		serviceName : serviceName,
		serviceCount:serviceCount,
		orderPrice:orderPrice,
		orderStatus : orderStatus
	});
	$('#createTime').datetimebox('setValue', createTime);
	$('#overTime').datetimebox('setValue', overTime);
}
// 执行用户编辑操作
function orderManagementEdit() {
	var orderId = $('#orderId').val()
	if (orderId != '') {
		$('#form_orderManagementEdit').form('submit', {
			url : 'modifyorder',
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
					searchOrderInfo();
					$("#orderManagementEdit_message").html(messgage);
				} else {
					var messgage = data.errMsg;
					$("#orderManagementEdit_message").html(messgage);
				}
			}
		});
	} else {
		$('#form_orderManagementEdit').form('submit', {
			url : 'addorder',
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
					searchOrderInfo();
					$("#orderManagementEdit_message").html(messgage);
				} else {
					var messgage = data.errMsg;
					$("#orderManagementEdit_message").html(messgage);
				}
			}
		});
	}
}

/** --------------执行删除操作------------------* 
function doDel(orderId) {
	$.messager.confirm('删除提示', '你确定永久删除该数据吗?', function(r) {
		if (r) {
			var url = 'deleteorder?orderId=' + orderId;
			changeStatus(url);
		}
	});
}
*/
/**
 * 批量操作
 * 
 * @return
 
function batch(flag) {
	if ($('#orderManagementTable').datagrid('getSelected')) {
		// 首先如果用户选择了数据，则获取选择的数据集合
		var tempList = [];
		var selectedRow = $('#orderManagementTable').datagrid('getSelections');
		var jsonList = [];
		for (var i = 0; i < selectedRow.length; i++) {
			jsonList.push(selectedRow[i].orderId);
		}
		// 删除操作
		$.messager.confirm('删除提示', '你确定永久删除下列区域吗?<br/>',
				function(r) {
					if (r) {
						var url = 'deleteorder?orderIdListStr='
								+ JSON.stringify(jsonList);
						changeStatus(url);
					}
				});

	}
}
*/
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
			searchOrderInfo();
		}
	});
}

