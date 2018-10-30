
let vm = new Vue({
	el: '#rrapp',
	data: {
        showList: false,
        title: "完善商户信息",
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
		intervalId:null,
		checkcode:""
	},
		created:function(){
		
/*			 Ajax.request({
					url: "../sysuserauth/getLoginUserInfo",
					type: "POST",
					contentType: "application/json",
					successCallback: function (r) {
						debugger;
					    this.sysUserAuth = r.sysUserAuth;
					}
					});*/
			
		},
	
	methods: {
		getCheckCode:function(){
				if(vm.intervalId!=null){
					return ;
				}
				 Ajax.request({
					    url: "../sysuserauth/getChecCode",
		                type: "GET",
		                params: {"mobile":vm.sysUserAuth.phone},
		                contentType: "application/json",
		                successCallback: function (r) {
		                	vm.checkcode = r.checkcode;
		                }
					});
			
				var  count = 60;
				vm.intervalId = setInterval(function(){
					
					if(count>0&&count<=60){
						vm.isdisabledFn=true
						count--;	
						vm.btntxt="获取验证码("+count+")";
					}else{
						vm.btntxt="获取验证码";
						clearInterval(this.intervalId);
						vm.isdisabledFn=false;
						vm.intervalId=null;
					}
				},1000);
		},
		query: function () {
			vm.reload();
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
                vm.saveOrUpdate();
                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引 (layui)
            	parent.layer.close(index); //再执行关闭   
            });
        },
        handleReset: function (name) {
            handleResetForm(this, name);
        }
	},

});

