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
      if (res.code == 1) {
        for(var i=0; i<res.data.length; i++){
          res.data[i].createTime = that.timestampToTime(res.data[i].createTime);
          if (res.data[i].tranFlag == 1){
            res.data[i].tarnAmount = '+' + res.data[i].tarnAmount
          }else{
            res.data[i].tarnAmount = '-' + res.data[i].tarnAmount
          }
        }
        that.setData({
          boxMount:res.data
        })
      }else{
        util.showSuccessToast(res.data);
      }
    })
  },
  //时间戳转换成日期
  timestampToTime:function (timestamp) {
    var date = new Date(timestamp);
    var Y = date.getFullYear() + '-';
    var M = this.addTo(date.getMonth() + 1) + '-';
    var D = this.addTo(date.getDate()) + ' ';
    var h = date.getHours() + ':';
    var m = date.getMinutes() + ':';
    var s = date.getSeconds();
    return Y + M + D + h + m + s;
  },//月，日格式化
	addTo:function (e) {
    if (e < 10) {
      return '0' + e;
    } else {
      return e;
    }
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