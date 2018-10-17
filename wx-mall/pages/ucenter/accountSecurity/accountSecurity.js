// pages/ucenter/accountSecurity/accountSecurity.js
var api = require('../../../config/api.js');
var util = require('../../../utils/util.js');
var app = getApp()

Page({

  /**
   * 页面的初始数据
   */
  data: {
    loginName:'',
    loginUrl:'',
    telephone:''
  },
  exitLogin: function () {
    wx.showModal({
      title: '',
      confirmColor: '#b4282d',
      content: '退出登录？',
      success: function (res) {
        if (res.confirm) {
          wx.removeStorageSync('token');
          wx.removeStorageSync('userInfo');
          wx.switchTab({
            url: '/pages/ucenter/index/index'
          });
        }
      }
    })

  },
  validateMobile:function(mobile){
    /*手机号加密处理*/
    var first = mobile.substr(0, 3);
    var last = mobile.substr(mobile.length - 4, 4);
    var finalPhone = first + '****' + last;
    return finalPhone;
  },
  bindPhone:function(){
    wx.navigateTo({
      url: '/pages/auth/mobile/mobile',
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;

    /*获取手机号*/
    util.request(api.UserMobile).then(function (res) {
      var mobile = res.data.mobile;
      that.setData({
        loginName: res.data.nickname,
        loginUrl: res.data.avatar
      })
      if (mobile == null || mobile == ''){
        that.setData({
          telephone: '去绑定'
        })
      }else{
        that.setData({
          telephone: that.validateMobile(mobile)
        })
      }
    })
    
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})