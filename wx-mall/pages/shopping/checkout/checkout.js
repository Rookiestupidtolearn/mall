var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');
const pay = require('../../../services/pay.js');

var app = getApp();

Page({
  data: {
    payorder:'',
    checkedGoodsList: [],
    checkedAddress: {},
    checkedCoupon: [],
    couponList: [],
    goodsTotalPrice: 0.00, //商品总价
    freightPrice: 0.00,    //快递费
    couponPrice: 0.00,     //优惠券的价格
    orderTotalPrice: 0.00,  //订单总价
    actualPrice: 0.00,     //实际需要支付的总价
    addressId: 0,
    couponId: 0,
    isBuy: false,
    couponDesc: '',
    couponCode: '',
    buyType: '',
    isBuyType:''
  },
  onLoad: function (options) {

    console.log(options.isBuy)
    this.setData({
      isBuyType: options.isBuy
    })
    // 页面初始化 options为页面跳转所带来的参数
    if (options.isBuy!="false") {
      this.data.isBuy = options.isBuy
    }
    this.data.buyType = this.data.isBuy?'buy':'cart'
    //每次重新加载界面，清空数据
    app.globalData.userCoupon = 'NO_USE_COUPON'
    app.globalData.courseCouponCode = {}
  },
  getCheckoutInfo: function () {
    let that = this;
    var url = api.CartCheckout
    let buyType = this.data.isBuy ? 'buy' : 'cart'
    util.request(url, { addressId: that.data.addressId, couponId: that.data.couponId, type: buyType },'post','application/json').then(function (res) {
      if (res.errno === 0) {
        console.log(res.data);
        that.setData({
          checkedGoodsList: res.data.checkedGoodsList,
          checkedAddress: res.data.checkedAddress,
          actualPrice: res.data.actualPrice,
          checkedCoupon: res.data.checkedCoupon,
          couponList: res.data.couponList,
          couponPrice: res.data.couponPrice,
          freightPrice: res.data.freightPrice,
          goodsTotalPrice: res.data.goodsTotalPrice,
          orderTotalPrice: res.data.orderTotalPrice
        });
        //设置默认收获地址
        if (that.data.checkedAddress){
            let addressId = that.data.checkedAddress.id;
            if (addressId) {
                that.setData({ addressId: addressId });
            }
        }else{
            wx.showModal({
                title: '',
                content: '请添加默认收货地址!',
                success: function (res) {
                    if (res.confirm) {
                        that.selectAddress();
                        console.log('用户点击确定')
                    }
                }
            })
        }
      }else{
        wx.showToast({
          image: '/static/images/icon_error.png',
          title: res.msg,
          mask: true
        });
      }
      wx.hideLoading();
    });
  },
  selectAddress() {
    wx.navigateTo({
      url: '/pages/shopping/address/address',
    })
  },
  addAddress() {
    wx.navigateTo({
      url: '/pages/shopping/addressAdd/addressAdd',
    })
  },
  onReady: function () {
    // 页面渲染完成

  },
  onShow: function () {
    this.getCouponData();

    // 页面显示
    wx.showLoading({
      title: '加载中...',
    })
    
    try {
      var addressId = wx.getStorageSync('addressId');
      if (addressId) {
        this.setData({
          'addressId': addressId
        });
      }
    } catch (e) {
      // Do something when catch error
    }
    this.getCheckoutInfo();
  },

  /**
   * 获取优惠券
   */
  getCouponData: function () {
    if (app.globalData.userCoupon == 'USE_COUPON') {
      this.setData({
        couponDesc: app.globalData.courseCouponCode.name,
        couponId: app.globalData.courseCouponCode.user_coupon_id,
      })
    } else if (app.globalData.userCoupon == 'NO_USE_COUPON') {
      this.setData({
        couponDesc: "平台抵扣券",
        couponId: '',
      })
    }
  },

  onHide: function () {
    // 页面隐藏

  },
  onUnload: function () {
    // 页面关闭
    util.request(api.CancelAddBuyCoupons, { isBuy: this.data.isBuyType}, 'post', 'application/json').then(res => {})
  },

  /**
   * 选择可用优惠券
   */
  tapCoupon: function () {
    let that = this
  
      wx.navigateTo({
        url: '../selCoupon/selCoupon?buyType=' + that.data.buyType,
      })
  },

  submitOrder: function () {
    if (this.data.addressId <= 0) {
      util.showErrorToast('请选择收货地址');
      return false;
    }
    util.request(api.OrderSubmit, { addressId: this.data.addressId, couponId: this.data.couponId, type: this.data.buyType },'post','application/json').then(res => {
      if (res.errno === 0) {
        const orderId = res.data.orderInfo.id;
        wx.setStorage({
          key: "keyOrder",
          data: res.payurl
        })
        this.payorder = res.payurl;
        wx.navigateTo({
          url: '/pages/webView/webView'
        })
      } else {
        util.showErrorToast('下单失败');
      }
    });
  }
})