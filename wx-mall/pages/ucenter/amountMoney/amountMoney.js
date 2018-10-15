// pages/ucenter/amountMoney/amountMoney.js
var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');
var app = getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {
      boxMount:[]
  },

  getUserInfoMoney: function () {
    var that = this;
    util.request(api.UserAccountDetail).then(function(res){
      for(var i=0; i<res.data.length; i++){
        if (res.data[i].tranFlag == 1){
          res.data[i].tarnAmount = '+' + res.data[i].tarnAmount
        }else{
          res.data[i].tarnAmount = '-' + res.data[i].tarnAmount
        }
      }
      if(res.code == 1){
        that.setData({
          boxMount:res.data
        })
      }else{
        util.showSuccessToast(res.data);
      }
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
      this.getUserInfoMoney();
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