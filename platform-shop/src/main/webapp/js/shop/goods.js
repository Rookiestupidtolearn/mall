$(function () {
    $("#jqGrid").Grid({
        url: '../goods/list',
        colModel: [
            {label: 'id', name: 'id', index: 'id', key: true, hidden: true},
            {label: '商品类型', name: 'categoryName', index: 'category_id', width: 80},
            {label: '名称', name: 'name', index: 'name', width: 160},
            {label: '品牌', name: 'brandName', index: 'brand_id', width: 120},
            {
                label: '上架', name: 'isOnSale', index: 'is_on_sale', width: 50,
                formatter: function (value) {
                    return goodsTransIsNot(value);	
                }
            },
            {
                label: '录入日期', name: 'addTime', index: 'add_time', width: 80, formatter: function (value) {
                    return transDate(value, 'yyyy-MM-dd');
                }
            },
            {label: '属性类别', name: 'attributeCategoryName', index: 'attribute_category', width: 80},
            {label: '结算价', name: 'retailPrice', index: 'retail_price', width: 80},
            {label: '商品库存', name: 'goodsNumber', index: 'goods_number', width: 80},
            {label: '销售量', name: 'sellVolume', index: 'sell_volume', width: 80},
            {label: '指导价', name: 'marketPrice', index: 'market_price', width: 80},
            {
                label: '热销', name: 'isHot', index: 'is_hot', width: 80, formatter: function (value) {
                    return transIsNot(value);
                }
            },
            {label: '配比值', name: 'value', index: 'good_value', width: 80,hidden: true},
            {label: '排序', name: 'sortOrder', index: 'sort_order', width: 80,}
            ]
    });
    $('#goodsDesc').editable({
        inlineMode: false,
        alwaysBlank: true,
        height: '500px', //高度
        minHeight: '200px',
        language: "zh_cn",
        spellcheck: false,
        plainPaste: true,
        enableScript: false,
        imageButtons: ["floatImageLeft", "floatImageNone", "floatImageRight", "linkImage", "replaceImage", "removeImage"],
        allowedImageTypes: ["jpeg", "jpg", "png", "gif"],
        imageUploadURL: '../sys/oss/upload',
        imageUploadParams: {id: "edit"},
        imagesLoadURL: '../sys/oss/queryAll'
    })
});

//0 下架  1上架 2 申请上架  3申请下架  -1编辑状态
function goodsTransIsNot(value) {

    if (value == 0) {
        return '<span class="label label-success">下架 </span>';
    }
    if (value == 1) {
        return '<span class="label label-success">上架 </span>';
    }
    if (value == 2) {
        return '<span class="label label-success">申请上架</span>';
    }
    if (value == 3) {
        return '<span class="label label-success">申请下架</span>';
    }
    return '<span class="label label-danger">编辑状态</span>';
};


var ztree;

var setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "id",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url: "nourl"
        }
    }
};
var vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        showData: false,
        isTrue:false,
        normalMatching:0,
        activityMatching:0,
        showMatching:true,
        title: null,
        uploadList: [],
        imgName: '',
        visible: false,
        goods: {
            primaryPicUrl: '',
            listPicUrl: '',
            categoryId: '',
            isOnSale: 1,
            isNew: 1,
            isAppExclusive: 0,
            isLimited: 0,
            isHot: 0,
            categoryName: ''
        },
        ruleValidate: {
            name: [
                {required: true, message: '名称不能为空', trigger: 'blur'}
            ]
        },
        q: {
            name: '',
            min_retail_price:'',
        	max_retail_price:'',
        	min_pure_interest_rate:'',
        	max_pure_interest_rate:'',
        	status:''
        },
        brands: [],//品牌
        macros: [],//商品单位
        attributeCategories: []//属性类别
    },
    methods: {
        query: function () {
            vm.reload();
        },
        clean:function(){
        	vm.q.name='',
        	vm.q.min_retail_price='',
        	vm.q.max_retail_price='',
        	vm.q.min_pure_interest_rate='',
        	vm.q.max_pure_interest_rate='',
        	vm.q.status=''
        },
        matching: function() {
        	var ids = $("#jqGrid").getGridParam("selarrrow");
        	if(ids.length<=0){
        		alert("请选择需要设置配比的商品");
        		return;
        	}
        	//校验选中的商品是否可以设置配比
        	Ajax.request({
                url: "../goodscouponconfig/verify/"+ids,
                async: true,
                successCallback: function (r) {
                	if(r.code === 0){
                		vm.isTrue = true;
                	}
                	if(!vm.isTrue){
                		return;
                	}else{
	                	vm.isTrue = false;
	                	vm.normalMatching=0;
	                    vm.activityMatching=0;
	                    vm.showList=false;
	                    vm.showData=false;
	                    vm.title = "配比设置";
	                	vm.showMatching = false;
                	}
                }
            });
        },
        add: function () {
        	vm.showList=false;
        	vm.showMatching = true;
            vm.showData = true;
            vm.title = "新增";
            vm.uploadList = [];
            vm.goods = {
                primaryPicUrl: '',
                listPicUrl: '',
                categoryId: '',
                isOnSale: 2,
                isNew: 1,
                isAppExclusive: 0,
                isLimited: 0,
                isHot: 0,
                categoryName: ''
            };
            $('#goodsDesc').editable('setHTML', '');
            vm.getCategory();
            vm.brands = [];
            vm.macros = [];
            vm.attributeCategories = [];
            // vm.attribute = [];
            vm.getBrands();
            vm.getMacro();
            vm.getAttributeCategories();
            // vm.getAttributes('');
        },
        update: function (event) {
            var id = getSelectedRow("#jqGrid");
            if (id == null) {
                return;
            }
            vm.showList=false;
        	vm.showMatching = true;
            vm.showData = true;
            vm.title = "修改";
            vm.uploadList = [];
            vm.getInfo(id);
            vm.getBrands();
            vm.getMacro();
            vm.getAttributeCategories();
            vm.getGoodsGallery(id);
        },
        /**
         * 获取品牌
         */
        getBrands: function () {
            Ajax.request({
                url: "../brand/queryAll",
                async: true,
                successCallback: function (r) {
                    vm.brands = r.list;
                }
            });
        },
        /**
         * 获取单位
         */
        getMacro: function () {
            Ajax.request({
                url: "../sys/macro/queryMacrosByValue?value=goodsUnit",
                async: true,
                successCallback: function (r) {
                    vm.macros = r.list;
                }
            });
        },
        getGoodsGallery: function (id) {//获取商品顶部轮播图
            Ajax.request({
                url: "../goodsgallery/queryAll?goodsId=" + id,
                async: true,
                successCallback: function (r) {
                    vm.uploadList = r.list;
                }
            });
        },
        getAttributeCategories: function () {
            Ajax.request({
                url: "../attributecategory/queryAll",
                async: true,
                successCallback: function (r) {
                    vm.attributeCategories = r.list;
                }
            });
        },
        saveOrUpdate: function (event) {
            var url = vm.goods.id == null ? "../goods/save" : "../goods/update";
            vm.goods.goodsDesc = $('#goodsDesc').editable('getHTML');
            vm.goods.goodsImgList = vm.uploadList;
            $('#goodsName').next('div').remove();
            if($('#goodsName input').val()==""){
            	$('#goodsName').parent().append('<div class="ivu-form-item-error-tip">名称不能为空</div>');
            	$('#goodsName').parent().parent().addClass('ivu-form-item-error');
            	return false;
            }
            Ajax.request({
                type: "POST",
                url: url,
                contentType: "application/json",
                params: JSON.stringify(vm.goods),
                successCallback: function (r) {
                	$('#goodsName').next('div').remove();
                	$('#goodsName').parent().parent().removeClass('ivu-form-item-error');
                    alert('操作成功', function (index) {
                        vm.reload();
                    });
                }
            });
        },
        saveGoodsCouponConfig: function () {
            var url = "../goodscouponconfig/save";
        	var ids = $("#jqGrid").getGridParam("selarrrow");
        	if(ids.length<=0){
        		alert("未获取到需要设置配比的商品信息");
        		return;
        	}
        	var normalMatching = vm.normalMatching;
        	var activityMatching = vm.activityMatching;
        	 Ajax.request({
                 url: "../goodscouponconfig/save/"+normalMatching+"/"+activityMatching+"/"+ids,
                 async: true,
                 successCallback: function (r) {
                	 alert('操作成功', function (index) {
                         vm.reload();
                     });
                 }
             });
        },
        enSale: function () {
        	 /*var id = getSelectedRow("#jqGrid");
             if (id == null) {
                 return;
             }*/
        	var ids = $("#jqGrid").getGridParam("selarrrow");
        	if(ids.length<=0){
        		alert("请选择需要上架的商品");
        		return;
        	}
            confirm('确定要上架选中的商品？', function () {
                Ajax.request({
                    type: "POST",
                    url: "../goods/enSale",
                    params: JSON.stringify(ids),
                    contentType: "application/json",
                    type: 'POST',
                    successCallback: function () {
                        alert('提交成功', function (index) {
                            vm.reload();
                        });
                    }
                });
            });
        },
        //申请上架
        applyEnSale:function() {
        	var ids = $("#jqGrid").getGridParam("selarrrow");
        	if(ids.length<=0){
        		alert("请选择需要申请上架的商品");
        		return;
        	}
            /*var id = getSelectedRow("#jqGrid");
            if (id == null) {
                return;
            }*/
            confirm('确定要申请上架选中的商品？', function () {
                Ajax.request({
                    type: "POST",
                    url: "../goods/applyEnSale",
                    params: JSON.stringify(ids),
                    contentType: "application/json",
                    type: 'POST',
                    successCallback: function () {
                        alert('提交成功', function (index) {
                            vm.reload();
                        });
                    }
                });
            });
        },
        
        //申请上架
        applyUnSale:function() {
        	var ids = $("#jqGrid").getGridParam("selarrrow");
        	if(ids.length<=0){
        		alert("请选择需要申请下架的商品");
        		return;
        	}
            confirm('确定要申请下架选中的商品？', function () {
                Ajax.request({
                    type: "POST",
                    url: "../goods/applyUnSale",
                    params: JSON.stringify(ids),
                    contentType: "application/json",
                    type: 'POST',
                    successCallback: function () {
                        alert('提交成功', function (index) {
                            vm.reload();
                        });
                    }
                });
            });
        },
        
        
        openSpe: function () {
            var id = getSelectedRow("#jqGrid");
            if (id == null) {
                return;
            }
            openWindow({
                type: 2,
                title: '商品规格',
                content: '../shop/goodsspecification.html?goodsId=' + id
            })
        },
        openPro: function () {
            var id = getSelectedRow("#jqGrid");
            if (id == null) {
                return;
            }
            openWindow({
                type: 2,
                title: '产品设置',
                content: '../shop/product.html?goodsId=' + id
            });
        },
        unSale: function () {
        	var ids = $("#jqGrid").getGridParam("selarrrow");
        	if(ids.length<=0){
        		alert("请选择需要下架的商品");
        		return;
        	}
            confirm('确定要下架选中的商品？', function () {
                Ajax.request({
                    type: "POST",
                    url: "../goods/unSale",
                    contentType: "application/json",
                    params: JSON.stringify(ids),
                    successCallback: function (r) {
                        alert('操作成功', function (index) {
                            vm.reload();;
                        });
                    }
                });

            });
        },
        del: function (event) {
            var ids = getSelectedRows("#jqGrid");
            if (ids == null) {
                return;
            }

            confirm('确定要删除选中的记录？', function () {
                Ajax.request({
                    type: "POST",
                    url: "../goods/delete",
                    contentType: "application/json",
                    params: JSON.stringify(ids),
                    successCallback: function (r) {
                        alert('操作成功', function (index) {
                            vm.reload();;
                        });
                    }
                });

            });
        },
        getInfo: function (id) {
            Ajax.request({
                url: "../goods/info/" + id,
                async: true,
                successCallback: function (r) {
                    vm.goods = r.goods;
                    $('#goodsDesc').editable('setHTML', vm.goods.goodsDesc);
                    vm.getCategory();
                }
            });
        },
        reload: function (event) {
            vm.showList = true;
            vm.showMatching = true;
            vm.showData = false;
            console.log(vm.q.min_retail_price+","+vm.q.max_retail_price+","+vm.q.min_pure_interest_rate+","+vm.q.max_pure_interest_rate);
            if(parseInt(vm.q.min_retail_price) < 0 || parseInt(vm.q.max_retail_price) < 0){
        		alert("结算价不能小于0");
        		return;
        	}
            if(vm.q.min_retail_price !== '' && vm.q.max_retail_price !== ''){
            	if(parseInt(vm.q.min_retail_price) > parseInt(vm.q.max_retail_price)){
            		alert("请设置正确查询条件");
            		return;
            	}
            }
            if(vm.q.min_pure_interest_rate !== '' && vm.q.max_pure_interest_rate !== ''){
            	if(parseInt(vm.q.min_pure_interest_rate) > parseInt(vm.q.max_pure_interest_rate)){
            		alert("请设置正确查询条件");
                	return;
            	}
            }
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {'name': vm.q.name,
                	'status':vm.q.status,
         		   'min_retail_price':vm.q.min_retail_price,
        		   'max_retail_price':vm.q.max_retail_price,
        		   'min_pure_interest_rate':vm.q.min_pure_interest_rate,
        		   'max_pure_interest_rate':vm.q.max_pure_interest_rate},
                page: page
            }).trigger("reloadGrid");
            vm.handleReset('formValidate');
        },
        getCategory: function () {
            //加载分类树
            Ajax.request({
                url: "../category/queryAll",
                async: true,
                successCallback: function (r) {
                    ztree = $.fn.zTree.init($("#categoryTree"), setting, r.list);
                    var node = ztree.getNodeByParam("id", vm.goods.categoryId);
                    if (node) {
                        ztree.selectNode(node);
                        vm.goods.categoryName = node.name;
                    } else {
                        node = ztree.getNodeByParam("id", 0);
                        ztree.selectNode(node);
                        vm.goods.categoryName = node.name;
                    }
                }
            });
        },
        categoryTree: function () {
            openWindow({
                title: "选择类型",
                area: ['300px', '450px'],
                content: jQuery("#categoryLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = ztree.getSelectedNodes();
                    //选择上级菜单
                    vm.goods.categoryId = node[0].id;
                    vm.goods.categoryName = node[0].name;

                    layer.close(index);
                }
            });
        },
        handleView(name) {
            this.imgName = name;
            this.visible = true;
        },
        handleRemove(file) {
            // 从 upload 实例删除数据
            const fileList = this.uploadList;
            this.uploadList.splice(fileList.indexOf(file), 1);
        },
        handleSuccess(res, file) {
            // 因为上传过程为实例，这里模拟添加 url
            file.imgUrl = res.url;
            file.name = res.url;
            vm.uploadList.add(file);
        },
        handleBeforeUpload() {
            const check = this.uploadList.length < 5;
            if (!check) {
                this.$Notice.warning({
                    title: '最多只能上传 5 张图片。'
                });
            }
            return check;
        },
        handleSubmit: function (name) {
            handleSubmitValidate(this, name, function () {
                vm.saveOrUpdate()
            });
        },
        handleFormatError: function (file) {
            this.$Notice.warning({
                title: '文件格式不正确',
                desc: '文件 ' + file.name + ' 格式不正确，请上传 jpg 或 png 格式的图片。'
            });
        },
        handleMaxSize: function (file) {
            this.$Notice.warning({
                title: '超出文件大小限制',
                desc: '文件 ' + file.name + ' 太大，不能超过 2M。'
            });
        },
        handleReset: function (name) {
            handleResetForm(this, name);
        },
        handleSuccessPicUrl: function (res, file) {
            vm.goods.primaryPicUrl = file.response.url;
        },
        handleSuccessListPicUrl: function (res, file) {
            vm.goods.listPicUrl = file.response.url;
        },
        eyeImagePicUrl: function () {
            var url = vm.goods.primaryPicUrl;
            eyeImage(url);
        },
        eyeImageListPicUrl: function () {
            var url = vm.goods.listPicUrl;
            eyeImage(url);
        },
        eyeImage: function (e) {
            eyeImage($(e.target).attr('src'));
        }
    },
    mounted() {
    	console.log(this.$refs);
    	console.log(this);
        this.uploadList = this.$refs.upload.fileList;
    }
});