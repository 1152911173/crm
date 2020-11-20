<%@ page contentType="text/html;charset=utf-8" language = "java"%>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

</head>
<script type="text/javascript">

	$(function(){

		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "top-right"
		});
		pageList(1,2);

		//定制字段
		$("#definedColumns > li").click(function(e) {
			//防止下拉菜单消失
			e.stopPropagation();
		});

		//为创建按钮绑定事件，打开添加操作的模态窗口
		$("#addBtn").click(function () {
			$.ajax({
				url:"workbench/customer/getUserList.do",
				type:"get",
				dataType:"json",
				success: function (data) {
					var html ="<option></option>";
					$.each(data,function (i,n) {
						html +="<option value='"+n.id+"'>"+n.name+"</option>"

					})
					$("#create-owner").html(html);
					$("#create-owner").val("${user.id}");
					//展现模态窗口
					$("#createCustomerModal").modal("show");
				}
			})
		})

		//为保存按钮绑定事件
		$("#saveBtn").click(function () {
			$.ajax({
				url:"workbench/customer/save.do",
				data:{
					"owner":$.trim($("#create-owner").val()),
					"name":$.trim($("#create-name").val()),
					"website":$.trim($("#create-website").val()),
					"phone":$.trim($("#create-phone").val()),
					"contactSummary":$.trim($("#create-contactSummary").val()),
					"nextContactTime":$.trim($("#create-nextContactTime").val()),
					"description":$.trim($("#create-description").val()),
					"address":$.trim($("#create-address").val())

				},
				type:"post",
				dataType:"json",
				success: function (data) {
					/*
					data: {"success":"true/false"}
					 */
					if(data.success){
						pageList(1,2);
						$("#createCustomerModal").modal("hide");
						$("#CustomerAddForm")[0].reset();
					}else{
						alert("保存客户信息失败");
					}
				}
			})
		})

		//为查询按钮添加事件
		$("#searchBtn").click(function () {

			$("#hidden-owner").val($.trim($("#search-owner").val()));
			$("#hidden-name").val($.trim($("#search-name").val()));
			$("#hidden-phone").val($.trim($("#search-phone").val()));
			$("#hidden-website").val($.trim($("#search-website").val()));
			pageList(1,$("#customerPage").bs_pagination('getOption','rowsPerPage'));
		})

		//为修改按钮添加事件
		$("#editBtn").click(function(){
			var $xz = $("input[name=xz]:checked");
			if($xz.length>1){
				alert("只能选则一项进行修改");
			}else if($xz.length==0){
				alert("请选择修改项");
			}else{
				var id = $xz.val();
				$.ajax({
					url:"workbench/customer/edit.do",
					data:{
						"id":id
					},
					type:"get",
					dataType:"json",
					success:function (data) {
						/*
						data:
							用户列表
							客户对象
							{"uList":[{},{},{}],"c":{客户对象}}
						*/
						var html = "<option></option>";
						$.each(data.uList,function (i,n) {
							html +="<option value='"+n.id+"'>"+n.name+"</option>"
						})
						$("#edit-owner").html(html);
						$("#edit-owner").val("${user.id}");

						//展示客户信息
						$("#edit-name").val(data.c.name);
						$("#edit-website").val(data.c.website);
						$("#edit-phone").val(data.c.phone);
						$("#edit-description").val(data.c.description);
						$("#edit-contactSummary").val(data.c.contactSummary);
						$("#edit-nextContactTime").val(data.c.nextContactTime);
						$("#edit-address").val(data.c.address);
						$("#edit-id").val(data.c.id);

						//打开模态窗口
						$("#editCustomerModal").modal("show");
					}
				})
			}
		})

		//为更新按钮添加事件
		$("#updateBtn").click(function () {
			$.ajax({
				url:"workbench/customer/update.do",
				data:{
					"id":$.trim($("#edit-id").val()),
					"owner":$.trim($("#edit-owner").val()),
					"name":$.trim($("#edit-name").val()),
					"website":$.trim($("#edit-website").val()),
					"phone":$.trim($("#edit-phone").val()),
					"description":$.trim($("#edit-description").val()),
					"contactSummary":$.trim($("#edit-contactSummary").val()),
					"nextContactTime":$.trim($("#edit-nextContactTime").val()),
					"address":$.trim($("#edit-address").val())
				},
				type:"post",
				dataType:"json",
				success:function (data) {
					if(data.success){
						pageList($("#customerPage").bs_pagination('getOption','currentPage')
								,$("#customerPage").bs_pagination('getOption','rowsPerPage'));
						$("#editCustomerModal").modal("hide");
					}else{
						alert("更新信息失败");
					}
				}
			})
		})

		//为删除按钮添加事件
		$("#deleteBtn").click(function () {
			var $xz = $("input[name=xz]:checked");
			if($xz.length==0){
				alert("请选择删除的客户");
			}else{
				if(confirm("确定要删除所选中的记录吗？")){

				var param ="";
				for(var i=0;i<$xz.length;i++){
					param += "id="+$xz[i].value;
					if(i<$xz.length-1){
						param +="&";
					}
				}

				$.ajax({
					url:"workbench/customer/delete.do",
					data:param,
					type:"post",
					dataType:"json",
					success:function (data) {
						if(data.success){
							//删除成功，返回第一页，维持每页展现的记录数
							pageList(1,$("#customerPage").bs_pagination('getOption','rowsPerPage'));
						}else{
							alert("删除失败");
						}
					}
				});

				}
			}

		})

		//为全选按钮绑定事件，触发全选操作
		$("#qx").click(function(){
			$("input[name=xz]").prop("checked",this.checked);
		})
		// 		动态生成的元素，以on方法的形式来触发事件
		// 		语法：
        //         $(需要绑定元素的的有效的外层元素).on(绑定事件的方式，需要绑定元素的jquery对象，回调函数)
		$("#activityBody").on("click",$("input[name=xz]"),function () {
			$("#qx").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length);
		})


	});
	function pageList(pageNo,pageSize){

		//将全选的复选框去掉
		$("#qx").prop("checked",false);
		$("#search-name").val($.trim($("#hidden-name").val()));
		$("#search-owner").val($.trim($("#hidden-owner").val()));
		$("#search-phone").val($.trim($("#hidden-phone").val()));
		$("#search-website").val($.trim($("#hidden-website").val()));
		$.ajax({
			url:"workbench/customer/pageList.do",
			data:{
				"pageNo":pageNo,
				"pageSize":pageSize,
				"name":$.trim($("#search-name").val()),
				"owner":$.trim($("#search-owner").val()),
				"phone":$.trim($("#search-phone").val()),
				"website":$.trim($("#search-website").val())
			},
			type:"get",
			dataType:"json",
			success: function (data) {
				/*
					data:{"total":total,
							"dataList":[{客户对象1},{客户对象},{客户对象}]}
				 */
				var html ='';
				$.each(data.dataList,function (i, n) {

					html +='<tr class="active">';
					html +='<td><input type="checkbox" name="xz" value="'+n.id+'"/></td>';
					html +='<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/customer/detail.do?id='+n.id+'\';">'+n.name+'</a></td>';
					html +='<td>'+n.owner+'</td>';
					html +='<td>'+n.phone+'</td>';
					html +='<td>'+n.website+'</td>';
					html +='</tr>';
				})
				$("#customerBody").html(html);

				var totalPages = data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1;
				$("#customerPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数

					visiblePageLinks: 3, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,

					//该回调函数是在，点击分页组件的时候触发的
					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});


			}
		})
	}

</script>
<body>
	<input type="hidden" id="hidden-name"/>
	<input type="hidden" id="hidden-owner"/>
	<input type="hidden" id="hidden-phone"/>
	<input type="hidden" id="hidden-website"/>

	<!-- 创建客户的模态窗口 -->
	<div class="modal fade" id="createCustomerModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建客户</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form" id="CustomerAddForm">
					
						<div class="form-group">
							<label for="create-customerOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">
								  <%--<option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>--%>
								</select>
							</div>
							<label for="create-customerName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-name">
							</div>
						</div>
						
						<div class="form-group">
                            <label for="create-website" class="col-sm-2 control-label">公司网站</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-website">
                            </div>
							<label for="create-phone" class="col-sm-2 control-label">公司座机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-phone">
							</div>
						</div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>

                        <div style="position: relative;top: 15px;">
                            <div class="form-group">
                                <label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="create-nextContactTime" class="col-sm-2 control-label ">下次联系时间</label>
                                <div class="col-sm-10" style="width: 300px;">
                                    <input type="text" class="form-control time" id="create-nextContactTime">
                                </div>
                            </div>
                        </div>

                        <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                        <div style="position: relative;top: 20px;">
                            <div class="form-group">
                                <label for="create-address1" class="col-sm-2 control-label">详细地址</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="create-address"></textarea>
                                </div>
                            </div>
                        </div>
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改客户的模态窗口 -->
	<div class="modal fade" id="editCustomerModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">修改客户</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">

						<input type="hidden" id="edit-id"/>
						<div class="form-group">
							<label for="edit-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">

								</select>
							</div>
							<label for="edit-name" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-name" >
							</div>
						</div>
						
						<div class="form-group">
                            <label for="edit-website" class="col-sm-2 control-label">公司网站</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-website" >
                            </div>
							<label for="edit-phone" class="col-sm-2 control-label">公司座机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-phone">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description" ></textarea>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>

                        <div style="position: relative;top: 15px;">
                            <div class="form-group">
                                <label for="edit-contactSummary" class="col-sm-2 control-label">联系纪要</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="3" id="edit-contactSummary" ></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="edit-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
                                <div class="col-sm-10" style="width: 300px;">
                                    <input type="text" class="form-control time" id="edit-nextContactTime" readonly>
                                </div>
                            </div>
                        </div>

                        <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                        <div style="position: relative;top: 20px;">
                            <div class="form-group">
                                <label for="edit-address" class="col-sm-2 control-label">详细地址</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="edit-address" ></textarea>
                                </div>
                            </div>
                        </div>
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>客户列表</h3>
			</div>
		</div>
	</div>
	
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
	
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">公司座机</div>
				      <input class="form-control" type="text" id="search-phone">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">公司网站</div>
				      <input class="form-control" type="text" id="search-website">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="searchBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx"/></td>
							<td>名称</td>
							<td>所有者</td>
							<td>公司座机</td>
							<td>公司网站</td>
						</tr>
					</thead>
					<tbody id="customerBody">
						<%--<tr>
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">动力节点</a></td>
							<td>zhangsan</td>
							<td>010-84846003</td>
							<td>http://www.bjpowernode.com</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">动力节点</a></td>
                            <td>zhangsan</td>
                            <td>010-84846003</td>
                            <td>http://www.bjpowernode.com</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">
				<div id="customerPage">

				</div>
			</div>
			
		</div>
		
	</div>
</body>
</html>