$(function () {
    $("#jqGrid").Grid({
        url: '../goodscouponconfig/list',
        colModel: [
			{label: 'id', name: 'id', index: 'id', key: true, hidden: true},
			/*{label: '商品ID', name: 'goodsId', index: 'goods_id', width: 80}*/
		
			{label: '商户', name: 'deptName', index: 'dept_id', width: 80},
			
			{label: '商品', name: 'name', index: 'goods_id', width: 80},
			
			{label: '类型', name: 'categoryName', index: 'category_id', width: 80},
			
			{label: '品牌', name: 'brandName', index: 'brand_id', width: 80},
			
			{label: '配比值', name: 'goodValue', index: 'good_value', width: 80}
			
			
	/*		{label: '创建用户ID', name: 'createUserId', index: 'create_user_id', width: 80},
			{label: '更新用户ID', name: 'updateUserId', index: 'update_user_id', width: 80},
			{label: '更新时间', name: 'updateTime', index: 'update_time', width: 80},
			{label: '创建商户ID', name: 'createUserDeptId', index: 'create_user_dept_id', width: 80},
			{label: '删除标识', name: 'delFlag', index: 'del_flag', width: 80}*/
			]
    });
});

let vm = new Vue({
	el: '#rrapp',
	data: {
        showList: true,
        title: null,
       
		goodsCouponConfig: {},
		ruleValidate: {
			name: [
				{required: true, message: '名称不能为空', trigger: 'blur'}
			]
		},
		q: {
		    name: ''
		},
		 goodss: []
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function () {
			vm.showList = false;
			vm.title = "新增";
		    vm.getGoodss();
			vm.goodsCouponConfig = {};
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
		  getGoodss: function () {
	            Ajax.request({
	                url: "../goods/queryAll/",
	                async: true,
	                successCallback: function (r) {
	                    vm.goodss = r.list;
	                }
	            });
	        },
		
		saveOrUpdate: function (event) {
            let url = vm.goodsCouponConfig.id == null ? "../goodscouponconfig/save" : "../goodscouponconfig/update";
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.goodsCouponConfig),
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
				    url: "../goodscouponconfig/delete",
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
                url: "../goodscouponconfig/info/"+id,
                async: true,
                successCallback: function (r) {
                    vm.goodsCouponConfig = r.goodsCouponConfig;
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