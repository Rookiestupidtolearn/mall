$(function () {
    $("#jqGrid").Grid({
        url: '../qzmoneyrecord/list',
        colModel: [
			{label: 'id', name: 'id', index: 'id', key: true, hidden: true},
			{
				label : '会员名称',
				name : 'shopUserName',
				index : 'shop_user_name',
				width : 80
			},
			{label: '会员id', name: 'shopUserId', index: 'shop_user_id', width: 80},
			{
				label : '手机号码',
				name : 'userPhone',
				index : 'user_phone',
				width : 80
			},
			{
				label : '身份证件号',
				name : 'cardId',
				index : 'card_id',
				width : 80
			},
			{label: '资金变动类型 ', name: 'tranType', index: 'tran_type', width: 80,formatter: function (value){
				if(value == '1'){
					return "平台充值";
				}else if(value == '2'){
					return "消费";
				}
			}
			},
			{label: '金额变动标志', name: 'tranFlag', index: 'tran_flag', width: 80,formatter: function (value){
				if(value == '0'){
					return "支出";
				}else if(value == '1'){
					return "收入";
				}
			}},
			{label: '交易类型', name: 'rechargeType', index: 'recharge_type', width: 80,formatter: function (value){
				if(value == '1'){
					return "后台充值";
				}else if(value == '2'){
					return "速有钱充值";
				}else{
					return "其他充值";
				}
			}},
			{label: '变动金额', name: 'tarnAmount', index: 'tarn_amount', width: 80},
			{label: '冻结金额', name: 'lockAmount', index: 'lock_amount', width: 80},
			{label: '当前余额', name: 'currentAmount', index: 'current_amount', width: 80},
			{label: '创建时间', name: 'createTime', index: 'create_time', width: 80,formatter: function (value) {
                return transDate(value, 'yyyy-MM-dd hh:mm:ss');
            }},
			{label: '交易流水号(关联各资金订单)', name: 'tradeNo', index: 'trade_no', width: 80}]
    });
});

let vm = new Vue({
	el: '#rrapp',
	data: {
        showList: true,
        title: null,
		qzMoneyRecord: {},
		ruleValidate: {
			name: [
				{required: true, message: '名称不能为空', trigger: 'blur'}
			]
		},
		q: {
		    name: '',
		    start_time:'',
		    end_time:'',
		    recharge_type:''
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function () {
			vm.showList = false;
			vm.title = "新增";
			vm.qzMoneyRecord = {};
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
            let url = vm.qzMoneyRecord.id == null ? "../qzmoneyrecord/save" : "../qzmoneyrecord/update";
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.qzMoneyRecord),
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
				    url: "../qzmoneyrecord/delete",
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
                url: "../qzmoneyrecord/info/"+id,
                async: true,
                successCallback: function (r) {
                    vm.qzMoneyRecord = r.qzMoneyRecord;
                }
            });
		},
		reload: function (event) {
			vm.showList = true;
            let page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
                postData: {
                	'name': vm.q.name,
                	'start_time': vm.q.start_time,
                	'end_time': vm.q.end_time,
                	'recharge_type':vm.q.recharge_type
                	},
                page: page
            }).trigger("reloadGrid");
            vm.handleReset('formValidate');
		},
        reloadSearch: function() {
            vm.q = {
            		name: '',
         		    start_time:'',
         		    end_time:'',
         		   recharge_type:''
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
        },
        exportUser : function() {
			exportFile('#rrapp', '../qzmoneyrecord/export', {
				'name': vm.q.name,
				'start_time': vm.q.start_time,
				'end_time': vm.q.end_time,
				'recharge_type':vm.q.recharge_type
			});
		}
	}
});