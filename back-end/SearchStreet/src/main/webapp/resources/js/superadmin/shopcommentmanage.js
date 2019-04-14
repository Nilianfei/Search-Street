$(function() {
	initializePage();
});

function initializePage() {
	// 加载表格数据
	ajaxTable();
	// 初始化弹出层
	setDialog_edit();
	closeDialog_edit();
	searchShopCommentInfo();
}

function searchShopCommentInfo() {
	var searchCondition = $("#searchConditionHd").val();
	if (searchCondition == "byShopId") {
		listShopCommentByShopId();
	} else if (searchCondition == "byOrderId") {
		listShopCommentByOrderId();
	}
	else if (searchCondition == "byUserId") {
		listShopCommentByUserId();
	}
	else if (searchCondition == "byCommentContent") {
		listShopCommentByCommentContent();
	}
	else if (searchCondition == "byCommentReply") {
		listShopCommentByCommentReply();
	}
	else {
		listShopComment();
	}
}

function listShopCommentByCommentContent() {
	var commentContent = $("#searchInfoHd").val();
	if (commentContent== "") {
		return;
	}
	var urlBuffer = new StringBuffer();
	urlBuffer.append("listShopCommentByCommentContent?commentContent=");
	urlBuffer.append(encodeURIComponent(encodeURIComponent(commentContent)));
	urlBuffer.append("&");
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#shopCommentManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}
function listShopCommentByCommentReply() {
	var commentReply = $("#searchInfoHd").val();
	if (commentReply== "") {
		return;
	}
	var urlBuffer = new StringBuffer();
	urlBuffer.append("listShopCommentByCommentReply?commentReply=");
	urlBuffer.append(encodeURIComponent(encodeURIComponent(commentReply)));
	urlBuffer.append("&");
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#shopCommentManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}
function listShopCommentByShopId() {
	var shopId = $("#searchInfoHd").val();
	if (shopId == "") {
		return;
	}
	var urlBuffer = new StringBuffer();
	urlBuffer.append("listShopCommentByShopId?shopId=");
	urlBuffer.append(shopId);
	urlBuffer.append("&");
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#shopCommentManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}
function listShopCommentByOrderId() {
	var orderId = $("#searchInfoHd").val();
	if (orderId == "") {
		return;
	}
	var urlBuffer = new StringBuffer();
	urlBuffer.append("listShopCommentByOrderId?orderId=");
	urlBuffer.append(orderId);
	urlBuffer.append("&");
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#shopCommentManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}
function listShopCommentByUserId() {
	var userId = $("#searchInfoHd").val();
	if (userId == "") {
		return;
	}
	var urlBuffer = new StringBuffer();
	urlBuffer.append("listShopCommentByUserId?userId=");
	urlBuffer.append(userId);
	urlBuffer.append("&");
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#shopCommentManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}

function listShopComment() {
	var urlBuffer = new StringBuffer();
	urlBuffer.append("listShopComment?");
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#shopCommentManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}

/** --------------table------------------* */
/**
 * 加载表格数据
 */
function ajaxTable() {
	// 加载表格
	$('#shopCommentManagementTable')
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
							$('#shopCommentManagementTable').datagrid('unselectRow',
									rowIndex);
						},
						onLoadSuccess : function() {
							var value = $('#shopCommentManagementTable').datagrid(
									'getData')['errorMsg'];
							if (value != null) {
								alert("错误消息:" + value);
							}
						}
					}).datagrid('acceptChanges');
	var searchConditionBuffer = new StringBuffer()
	searchConditionBuffer
			.append('<select id="shopCommentManagementSearch_searchCondition" class="easyui-combobox" style="margin :2px; padding :4px;" ');
	searchConditionBuffer
			.append('onchange="changeSearchCondition(this.options[this.options.selectedIndex].value)">');
	searchConditionBuffer
			.append('<option id="shopCommentManagementSearch_ALL" value="ALL">全部</option>');
	searchConditionBuffer
			.append('<option id="shopCommentManagementSearch_SHOPID" value="byShopId">按店铺ID查询</option>');
	searchConditionBuffer
			.append('<option id="shopCommentManagementSearch_ORDERID" value="byOrderId">按订单ID查询</option>');
	searchConditionBuffer
			.append('<option id="shopCommentManagementSearch_USERID" value="byUserId">按用户ID查询</option>');
	searchConditionBuffer
			.append('<option id="shopCommentManagementSearch_COMMENTCONTENT" value="byCommentContent">按评论内容查询</option>');
    searchConditionBuffer
			.append('<option id="shopCommentManagementSearch_COMMENTREPLY" value="byCommentReply">按商家回复查询</option></select>');
	var searchBoxBuffer = new StringBuffer();
	searchBoxBuffer.append('<input type="text" ');
	searchBoxBuffer
			.append('id="shopCommentManagementSearch_searchBox" onchange="changeSearchInfo()"');
	searchBoxBuffer
			.append(' style="resize: none; width: 200px"></input>');
	searchBoxBuffer.append('<input type="button" id="searchBtn" value="搜索"');
	searchBoxBuffer
			.append('style="margin-right: 0.5em;" onclick="searchInfo()"/>');
	var addButtonBuffer = new StringBuffer();
	addButtonBuffer
			.append('<input type="button" id="addShopComment" value="添加评论信息"');
	addButtonBuffer
			.append('style="margin-right: 0.5em;" onclick="openDialog()"/>');
	$('.datagrid-toolbar').append(searchConditionBuffer.toString());
	$('.datagrid-toolbar').append(searchBoxBuffer.toString());
	$('.datagrid-toolbar').append(addButtonBuffer.toString());
	$("#shopCommentManagementSearch_searchBox").hide();
	$("#searchBtn").hide();
	// 获取DataGrid分页组件对象
	var p = $("#shopCommentManagementTable").datagrid("getPager");
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
			$('#shopCommentManagementTable').datagrid('loadData', jsonObj);
		}
	});// ajax
}


function changeSearchCondition(condition) {
	$("#searchConditionHd").val(condition);
	if (condition == "byShopId") {
		$("#shopCommentManagementSearch_searchBox").show();
		$("#searchBtn").show();
	} else if (condition == "byOrderId") {
		$("#shopCommentManagementSearch_searchBox").show();
		$("#searchBtn").show();
	} else if (condition == "byUserId") {
		$("#shopCommentManagementSearch_searchBox").show();
		$("#searchBtn").show();
	} else if (condition == "byCommentContent") {
		$("#shopCommentManagementSearch_searchBox").show();
		$("#searchBtn").show();
	} 
	else if (condition == "byCommentReply") {
		$("#shopCommentManagementSearch_searchBox").show();
		$("#searchBtn").show();
	} 
	else {
		$("#searchInfoHd").val("");
		$("#shopCommentManagementSearch_searchBox").hide();
		$("#searchBtn").hide();
	}
	searchShopCommentInfo();
}

function changeSearchInfo() {
	var info = $('#shopCommentManagementSearch_searchBox').val();
	$('#searchInfoHd').val(info);
}


function searchInfo() {
	var info = $('#shopCommentManagementSearch_searchBox').val().trim();
	$('#searchInfoHd').val(info);
	if (info == "") {
		return;
	}
	searchShopCommentInfo();
}
/**
 * 设置操作列的信息 参数说明 value 这个可以不管，但是要使用后面 row 和index 这个参数是必须的 row 当前行的数据 index
 * 当前行的索引 从0 开始
 */
function optFormater(value, row, index) {
	var shopCommentId = row.shopCommentId;
	var shopId = row.shopId;
	var orderId = row.orderId;
	var userId = row.userId;
	var serviceRating = row.serviceRating;
	var starRating = row.starRating;
	var commentContent = row.commentContent;
	var commentReply = row.commentReply;
	var params = shopCommentId + "," +shopId+"," +orderId+"," + userId +"," + serviceRating+"," +starRating+",'" + commentContent + "','" +commentReply+"'" ;
	var edit = '<a href="javascript:openDialog_edit(' + params + ')">编辑</a>'+"    "+'<a href="javascript:doDel(' + shopCommentId+ ')">删除</a>';
	return edit;
};
function doDel(shopCommentId) {
	$.messager.confirm('删除提示', '你确定永久删除该数据吗?', function(r) {
		if (r) {
			var url = 'deleteshopcomment?shopCommentId=' + shopCommentId;
			changeStatus(url);
		}
	});
}

/** --------------编辑操作弹出框------------------* */

// 设置弹出框的属性
function setDialog_edit() {
	$('#shopCommentManagementEdit').dialog({
		title : '评论编辑',
		modal : true, // 模式窗口：窗口背景不可操作
		collapsible : true, // 可折叠，点击窗口右上角折叠图标将内容折叠起来
		resizable : true
	});
}
// 打开对话框
function openDialog_edit(shopCommentId, shopId,orderId,userId,serviceRating,starRating,commentContent,commentReply) {
	shopCommentManagementEditReset(shopCommentId, shopId,orderId,userId,serviceRating,starRating,commentContent,commentReply);
	$('#shopCommentManagementEdit').dialog('open');
}
function openDialog() {
	shopCommentManagementEditReset(null, null, null, null, null, null, "", "");
	$('#shopCommentManagementEdit').dialog('open');
}
// 关闭对话框
function closeDialog_edit() {
	$('#shopCommentManagementEdit').dialog('close');
}
// 根据用户id查询用户的信息
function shopCommentManagementEditReset(shopCommentId, shopId,orderId,userId,serviceRating,starRating,commentContent,commentReply) {
	$("#shopCommentManagementEdit_message").html("");
	$('#form_shopCommentManagementEdit').form('load', {
		shopCommentId : shopCommentId,
		shopId:shopId,
		orderId:orderId,
		userId:userId,
		serviceRating:serviceRating,
		starRating:starRating,
		commentContent:commentContent,
		commentReply:commentReply
	});
}
// 执行用户编辑操作
function shopCommentManagementEdit() {
	var shopCommentId = $('#shopCommentId').val()
	if (shopCommentId != '') {
		$('#form_shopCommentManagementEdit').form('submit', {
			url : 'modifyShopComment',
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
					searchShopCommentInfo();
					$("#shopCommentManagementEdit_message").html(messgage);
				} else {
					var messgage = data.errMsg;
					$("#shopCommentManagementEdit_message").html(messgage);
				}
			}
		});
	} else {
		$('#form_shopCommentManagementEdit').form('submit', {
			url : 'addShopComment',
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
					searchShopCommentInfo();
					$("#shopCommentManagementEdit_message").html(messgage);
				} else {
					var messgage = data.errMsg;
					$("#shopCommentManagementEdit_message").html(messgage);
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
			searchShopCommentInfo();
		}
	});
}

