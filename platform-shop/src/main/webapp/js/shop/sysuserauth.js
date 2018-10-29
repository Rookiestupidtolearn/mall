$(function () {
    $("#jqGrid").Grid({
        url: '../sysuserauth/list',
        colModel: [
			{label: 'id', name: 'id', index: 'id', key: true, hidden: true},
			{label: '用户ID', name: 'userId', index: 'user_id', width: 80 ,hidden:true},
			{label: '部门ID', name: 'deptId', index: 'dept_id', width: 80  ,hidden:true},
			{label: '账号', name: 'useraccount', index: 'useraccount', width: 80},
			{label: '名称', name: 'name', index: 'name', width: 80},
			{label: '性别', name: 'sex', index: 'sex', width: 80,
				 formatter: function (value){
					 return  setTrans(value);
				 }
			},
			{label: '身份证', name: 'cardId', index: 'card_id', width: 80},
			{label: '公司地址', name: 'addressCompony', index: 'address_compony', width: 80},
			{label: '手机', name: 'phone', index: 'phone', width: 80},
			{label: '创建人', name: 'createUserId', index: 'create_user_id', width: 80 ,hidden:true},
			{label: '创建时间', name: 'createTime', index: 'create_time', width: 80 ,hidden:true},
			{label: '更新人', name: 'updateUserId', index: 'update_user_id', width: 80  ,hidden:true},
			{label: '删除标识', name: 'delflag', index: 'delflag', width: 80 ,hidden:true},
			{label: '更新时间', name: 'updteTime', index: 'updte_time', width: 80 ,hidden:true}]
    });
});


function setTrans(sex){
	if(sex==0) return "男";
	return  "女";
	
}

let vm = new Vue({
	el: '#rrapp',
	data: {
        showList: true,
        title: null,
		sysUserAuth: {},
		ruleValidate: {
			name: [
				{required: true, message: '名称不能为空', trigger: 'blur'}
			]
		},
		q: {
		    name: ''
		},
		btntxt:"获取验证码",
		
		isdisabledFn:false,
		intervalId : null,
		checkcode:""
	},
	methods: {
		getCheckCode:function(){
				
				 Ajax.request({
					    url: "../sysuserauth/getChecCode",
		                type: "GET",
		                params: {"mobile":vm.sysUserAuth.phone},
		                contentType: "application/json",
		                successCallback: function (r) {
		                	console.log(r);
		                	vm.checkcode = r.checkcode;
		                }
					});
			
				var  count = 60;
				vm.intervalId = setInterval(function(){
					if(!intervalId!=null){
						return ;
					}
					if(count>0&&count<=60){
						vm.isdisabledFn=true
						count--;	
						vm.btntxt="获取验证码("+count+")";
					}else{
						vm.btntxt="获取验证码";
						clearInterval(vm.intervalId);
						vm.isdisabledFn=false;
						vm.intervalId=null;
					}
				},1000);
		},
		query: function () {
			vm.reload();
		},
		add: function () {
			vm.showList = false;
			vm.title = "新增";
			vm.sysUserAuth = {};
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
		
			if(checkcode=!vm.sysUserAuth.checkCode){
				alert("验证码有误!")
				return ;
			}
				
            let url = vm.sysUserAuth.id == null ? "../sysuserauth/save" : "../sysuserauth/update";
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.sysUserAuth),
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
				    url: "../sysuserauth/delete",
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
                url: "../sysuserauth/info/"+id,
                async: true,
                successCallback: function (r) {
                    vm.sysUserAuth = r.sysUserAuth;
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