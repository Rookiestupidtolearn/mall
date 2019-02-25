$(function () {
    let shippingStatus = getQueryString("shippingStatus");
    let payStatus = getQueryString("payStatus");
    let orderStatus = getQueryString("orderStatus");
    let orderType = getQueryString("orderType");
    let url = '../order/orderAfterSale';
    if (shippingStatus) {
        url += '?shippingStatus=' + shippingStatus;
    }
    if (payStatus) {
        url += '?payStatus=' + payStatus;
    }
    if (orderStatus) {
        url += '?orderStatus=' + orderStatus;
    }
    if (orderType) {
        url += '?orderType=' + orderType;
    }
    $("#jqGrid").Grid({
        url: url,
        datatype: "json",
        colModel: [
            {label: 'id', name: 'id', index: 'id', key: true, hidden: true},
            {label: '订单号', name: 'orderSn', index: 'order_sn', width: 100},
            {label: '会员', name: 'userName', index: 'user_name', width: 80},
            {
                label: '异常类型', name: 'orderType', index: 'order_type', width: 80, formatter: function (value) {
                    if (value == '404') {
                        return '支付异常';
                    } 
                    return '-';
                }
            },
            {
                label: '订单状态', name: 'orderStatus', index: 'order_status', width: 80, formatter: function (value) {
                    if (value == '0') {
                        return '待付款';
                    } else if (value == '101') {
                        return '订单已取消';
                    } else if (value == '102') {
                        return '订单已删除';
                    }  else if (value == '200') {
                        return '已付款，等待创建三方订单';
                    }else if (value == '201') {
                        return '订单已付款';
                    } else if (value == '300') {
                        return '订单已发货';
                    } else if (value == '301') {
                        return '用户确认收货';
                    } else if (value == '401') {
                        return '退款';
                    } else if (value == '103') {
                        return '订单失效';
                    } else if (value == '402') {
                        return '完成';
                    }
                    return value;
                }
            },
            {
                label: '发货状态',
                name: 'shippingStatus',
                index: 'shipping_status',
                width: 60,
                formatter: function (value) {
                    if (value == '0') {
                        return '未发货';
                    } else if (value == '1') {
                        return '已发货';
                    } else if (value == '2') {
                        return '已收货';
                    } else if (value == '4') {
                        return '退货';
                    }
                    return value;
                }
            },
            {
                label: '付款状态', name: 'payStatus', index: 'pay_status', width: 80,
                formatter: function (value) {
                    if (value == '0') {
                        return '未付款';
                    } else if (value == '1') {
                        return '已付款';
                    } 
                    return value;
                }
            },

            {label: '应支付金额', name: 'actualPrice', index: 'actual_price', width: 80},
            {label: '订单总价', name: 'orderPrice', index: 'order_price', width: 60},
            {label: '商品总价', name: 'goodsPrice', index: 'goods_price', width: 60},
            {label: '快递费', name: 'shippingFee', index: 'shipping_fee', width: 60},
            {
                label: '下单时间', name: 'addTime', index: 'add_time', width: 80,
                formatter: function (value) {
                    return transDate(value);
                }
            },
            {
                label: '操作', width: 160, align: 'center', sortable: false, formatter: function (value, col, row) {
                    return '<button class="btn btn-outline btn-info" onclick="vm.lookDetail(' + row.id + ')"><i class="fa fa-info-circle"></i>&nbsp;详情</button>' ;
                }
            }
        ]
    });
});

let vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        detail: false,
        title: null,
        order: {},
        shippings: [],
        q: {
            orderSn: '',
            orderStatus: '',
            orderType: ''
        }
    },
    methods: {
        query: function () {
            vm.reload();
        },
        sendGoods: function (event) {
            let id = getSelectedRow("#jqGrid");
            if (id == null) {
                return;
            }
            vm.showList = false;
            vm.title = "发货";
            Ajax.request({
                url: "../order/info/" + id,
                async: true,
                successCallback: function (r) {
                    vm.order = r.order;
                }
            });
        },
        confirm: function (event) {
            let id = getSelectedRow("#jqGrid");
            if (id == null) {
                return;
            }
            confirm('确定收货？', function () {
                Ajax.request({
                    type: "POST",
                    url: "../order/confirm",
                    contentType: "application/json",
                    params: JSON.stringify(id),
                    successCallback: function (r) {
                        if (r.code == 0) {
                            alert('操作成功', function (index) {
                                vm.reload();
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        saveOrUpdate: function (event) {
            Ajax.request({
                type: "POST",
                url: "../order/sendGoods",
                contentType: "application/json",
                params: JSON.stringify(vm.order),
                successCallback: function (r) {
                    vm.reload();
                }
            });
        },
        reload: function (event) {
            vm.showList = true;
            vm.detail = false;
            let page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {
                    'orderSn': vm.q.orderSn,
                    'orderStatus': vm.q.orderStatus,
                    'orderType': vm.q.orderType
                },
                page: page
            }).trigger("reloadGrid");
        },
        lookDetail: function (rowId) { //第三步：定义编辑操作
            vm.detail = true;
            vm.title = "详情";
            Ajax.request({
                url: "../order/info/" + rowId,
                async: true,
                successCallback: function (r) {
                    vm.order = r.order;
                }
            });
        },
        printDetail: function (rowId) {
            openWindow({
                type: 2,
                title: '<i class="fa fa-print"></i>打印票据',
                content: '../shop/orderPrint.html?orderId=' + rowId
            })
        }
    },
    created: function () {
        let vue = this;
        Ajax.request({
            url: "../shipping/queryAll",
            async: true,
            successCallback: function (r) {
                vue.shippings = r.list;
            }
        });
    }
});