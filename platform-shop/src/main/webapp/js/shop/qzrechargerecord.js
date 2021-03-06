$(function() {
	$("#jqGrid").Grid({
		url : '../qzrechargerecord/list?state=0',
		colModel : [ {
			label : 'id',
			name : 'id',
			index : 'id',
			key : true,
			hidden : true
		}, {
			label : '会员名称',
			name : 'shopUserName',
			index : 'shop_user_name',
			width : 80
		}, {
			label : '会员id',
			name : 'shopUserId',
			index : 'shop_user_id',
			width : 40
		}, {
			label : '手机号码',
			name : 'mobile',
			index : 'mobile',
			width : 80
		}, {
			label : '充值金额(元)',
			name : 'amount',
			index : 'amount',
			width : 80
		},

		{
			label : '申请时间',
			name : 'createTime',
			index : 'create_time',
			width : 80,
			formatter : function(value) {
				return transDate(value);
			}
		}, {
			label : '充值单号',
			name : 'tradeNo',
			index : 'trade_no',
			width : 80
		}, {
			label : '状态',
			name : 'state',
			index : 'state',
			width : 20,
			formatter : function(value) {
				if (value == 0) {
					return '初始';
				}
				if (value == 1) {
					return '通过';
				}
				if (value == 2) {
					return '拒绝';
				}
			}

		}

		]
	});
});

let
vm = new Vue({
	el : '#rrapp',
	data : {
		showList : true,
		title : null,
		qzRechargeRecord : {},
		ruleValidate : {
			name : [ {
				required : true,
				message : '名称不能为空',
				trigger : 'blur'
			} ]
		},
		q : {
			name : ''
		}
	},
	methods : {
		query : function() {
			vm.reload();
		},

		agree : function() {
			var ids = getSelectedRows("#jqGrid");
			if (ids == null) {
				return;
			}

			Ajax.request({
				url : "../qzrechargerecord/rechargeAuditPass",
				type : "POST",
				contentType : "application/json",
				params : JSON.stringify(ids),
				successCallback : function(r) {
					vm.reload();
				}
			});
		},
		refuse : function() {
			var ids = getSelectedRows("#jqGrid");
			if (ids == null) {
				return;
			}

			Ajax.request({
				url : "../qzrechargerecord/rechargeAuditFall",
				type : "POST",
				contentType : "application/json",
				params : JSON.stringify(ids),
				successCallback : function(r) {
					vm.reload();
				}
			});
		},

		add : function() {
			vm.showList = false;
			vm.title = "新增";
			vm.qzRechargeRecord = {};
		},
		update : function(event) {
			let
			id = getSelectedRow("#jqGrid");
			if (id == null) {
				return;
			}
			vm.showList = false;
			vm.title = "修改";

			vm.getInfo(id)
		},
		saveOrUpdate : function(event) {
			let
			url = vm.qzRechargeRecord.id == null ? "../qzrechargerecord/save"
					: "../qzrechargerecord/update";
			Ajax.request({
				url : url,
				params : JSON.stringify(vm.qzRechargeRecord),
				type : "POST",
				contentType : "application/json",
				successCallback : function(r) {
					alert('操作成功', function(index) {
						vm.reload();
					});
				}
			});
		},
		del : function(event) {
			let
			ids = getSelectedRows("#jqGrid");
			if (ids == null) {
				return;
			}

			confirm('确定要删除选中的记录？', function() {
				Ajax.request({
					url : "../qzrechargerecord/delete",
					params : JSON.stringify(ids),
					type : "POST",
					contentType : "application/json",
					successCallback : function() {
						alert('操作成功', function(index) {
							vm.reload();
						});
					}
				});
			});
		},
		getInfo : function(id) {
			Ajax.request({
				url : "../qzrechargerecord/info/" + id,
				async : true,
				successCallback : function(r) {
					vm.qzRechargeRecord = r.qzRechargeRecord;
				}
			});
		},
		reload : function(event) {
			vm.showList = true;
			let
			page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
				postData : {
					'mobile' : vm.q.mobile
				},
				page : page
			}).trigger("reloadGrid");
			vm.handleReset('formValidate');
		},
		reloadSearch : function() {
			vm.q = {
					mobile : ''
			}
			vm.reload();
		},
		handleSubmit : function(name) {
			handleSubmitValidate(this, name, function() {
				vm.saveOrUpdate()
			});
		},
		handleReset : function(name) {
			handleResetForm(this, name);
		}
	}
});