var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');

Page({
  data:{
    orderList: [],
    page: 1,
    size: 10,
    loadmoreText: '正在加载更多数据',
    nomoreText: '全部加载完成',
    nomore: false,
    totalPages: 1
  },
  onLoad:function(options){
    // 页面初始化 options为页面跳转所带来的参数
    // 页面显示
    wx.showLoading({
      title: '加载中...'
    });
  },
  getOrderList(){
    let that = this;
    util.request(api.OrderList, {page: that.data.page, size: that.data.size}).then(function (res) {
      if(that.data.page > res.totalPages){
        that.setData({
          nomore:true
        })
      }
      if (res.errno === 0) {
        console.log(res.data);
        that.setData({
          orderList: that.data.orderList.concat(res.data.data),
          page: res.data.currentPage + 1,
          totalPages: res.data.totalPages
        });
        wx.hideLoading();
      }
    });
  },
  payOrder(event){
      let that = this;
      let orderIndex = event.currentTarget.dataset.orderIndex;
      let order = that.data.orderList[orderIndex];
      wx.redirectTo({
          url: '/pages/pay/pay?orderId=' + order.id + '&actualPrice=' + order.actual_price,
      })
  },
  onReady:function(){
    // 页面渲染完成
  },
  onShow:function(){
    this.getOrderList();
  },
  onHide:function(){
    // 页面隐藏
  },
  onUnload:function(){
    // 页面关闭
  }
})