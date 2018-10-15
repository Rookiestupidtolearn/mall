$(function () {
    $("#jqGrid").Grid({
        url: '../qzrechargerecord/list?state=0',
        colModel: [
			{label: 'id', name: 'id', index: 'id', key: true, hidden: true},
			{label: '会员id', name: 'shopUserId', index: 'shop_user_id', width: 80},
			{label: '状态', name: 'state', index: 'state', width: 80,formatter: function(value){
				 if(value ==0){
					 return '初始';
				 }
				 if(value ==1){
					 return '通过';
				 }
				 if(value == 2){
					 return '拒绝';
				 }
			}
			
			},
			{label: '审核人id', name: 'operateId', index: 'operate_id', width: 80},
			{label: '审核时间', name: 'operateTime', index: 'operate_time', width: 80},
			{label: '金额', name: 'amount', index: 'amount', width: 80},
			{label: '备注', name: 'memo', index: 'memo', width: 80},
			{label: '创建时间', name: 'createTime', index: 'create_time', width: 80},
			{label: '修改时间', name: 'updateTime', index: 'update_time', width: 80},
			{label: '流水号', name: 'tradeNo', index: 'trade_no', width: 80}]
    });
});

let vm = new Vue({
	el: '#rrapp',
	data: {
        showList: true,
        title: null,
		qzRechargeRecord: {},
		ruleValidate: {
			name: [
				{required: true, message: '名称不能为空', trigger: 'blur'}
			]
		},
		q: {
		    name: ''
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		
		agree: function(){
		    var ids = getSelectedRows("#jqGrid");
            if (ids == null) {
                return;
            }
            
            
            Ajax.request({
                url: "../qzrechargerecord/rechargeAuditPass",
                type: "POST",
                contentType: "application/json",
                params: JSON.stringify(ids),
                successCallback: function (r) {
                	vm.reload();
                }
            });
		},
		refuse: function(){
		    var ids = getSelectedRows("#jqGrid");
            if (ids == null) {
                return;
            }
            
            Ajax.request({
                url: "../qzrechargerecord/rechargeAuditFall",
                type: "POST",
                contentType: "application/json",
                params: JSON.stringify(ids),
                successCallback: function (r) {
                	vm.reload();
                }
            });
		},
		
		add: function () {
			vm.showList = false;
			vm.title = "新增";
			vm.qzRechargeRecord = {};
		},
		update: function (event) {
            let id = getSelectedRow("#jqGrid");
			if (id == null) {
				return;
			}
			vm.showList = false;
            vm.title = "修改";

            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
            let url = vm.qzRechargeRecord.id == null ? "../qzrechargerecord/save" : "../qzrechargerecord/update";
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.qzRechargeRecord),
                type: "POST",
			    contentType: "application/json",
                successCallback: function (r) {
                    alert('操作成功', function (index) {
                        vm.reload();
                    });
                }
			});
		},
		del: function (event) {
            let ids = getSelectedRows("#jqGrid");
			if (ids == null){
				return;
			}

			confirm('确定要删除选中的记录？', function () {
                Ajax.request({
				    url: "../qzrechargerecord/delete",
                    params: JSON.stringify(ids),
                    type: "POST",
				    contentType: "application/json",
                    successCallback: function () {
                        alert('操作成功', function (index) {
                            vm.reload();
                        });
					}
				});
			});
		},
		getInfo: function(id){
            Ajax.request({
                url: "../qzrechargerecord/info/"+id,
                async: true,
                successCallback: function (r) {
                    vm.qzRechargeRecord = r.qzRechargeRecord;
                }
            });
		},
		reload: function (event) {
			vm.showList = true;
            let page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
                postData: {'name': vm.q.name},
                page: page
            }).trigger("reloadGrid");
            vm.handleReset('formValidate');
		},
        reloadSearch: function() {
            vm.q = {
                name: ''
            }
            vm.reload();
        },
        handleSubmit: function (name) {
            handleSubmitValidate(this, name, function () {
                vm.saveOrUpdate()
            });
        },
        handleReset: function (name) {
            handleResetForm(this, name);
        }
	}
});