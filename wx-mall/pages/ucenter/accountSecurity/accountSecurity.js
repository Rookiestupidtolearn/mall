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
    telephone:'',
    name:'',
    idCard:''
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
  validatename: function (name) {
    /*姓名加密处理*/
    var last = name.substr(name.length - 4, 1);
    var finalname = '*' + last;
    return finalname;
  },
  validateidCard: function (idcard) {
    /*身份证号加密处理*/
    var first = idcard.substr(0, 1);
    var last = idcard.substr(idcard.length - 4, 1);
    var finalidcard = first + '***********' + last;
    return finalidcard;
  },
  bindPhone:function(){
    wx.navigateTo({
      url: '/pages/auth/mobile/mobile',
    })
  },
  bindname:function(){
    wx.navigateTo({
      url: '/pages/ucenter/namecardSecurity/namecard',
    })
  },
  bindcard:function(){
    wx.navigateTo({
      url: '/pages/ucenter/namecardSecurity/namecard',
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;

    /*获取手机号*/
    util.request(api.UserMobile).then(function (res) {
      var mobile = res.data.mobile; //绑定手机号
      var name = res.data.username; //姓名 
      var idCard = res.data.idcard; //身份证号
      that.setData({
        loginName: res.data.nickname,
        loginUrl: res.data.avatar
      })
      if (mobile == null || mobile == ''){
        that.setData({
          telephone: '去绑定>'
        })
      }else{
        that.setData({
          telephone: that.validateMobile(mobile)
        })
      }
      if (name == null || name == '') {
        that.setData({
          name: '未认证>'
        })
      } else {
        // that.setData({
        //   name: '未认证>'
        // })
        that.setData({
          name: that.validatename(name)
        })
      }
      if (idCard == null || idCard == '') {
        that.setData({
          idCard: '未认证>'
        })
      } else {
        that.setData({
          idCard: that.validateidCard(idCard)
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