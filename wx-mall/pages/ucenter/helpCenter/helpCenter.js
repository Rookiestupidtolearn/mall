// pages/ucenter/helpCenter/helpCenter.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    helpImg:'../../../static/images/go.png',
    listArray: [
      {
        show: false,
        question: "如何注册会员?",
        answer: "登录微信小程序，点击登录授权使用读取微信信息，即可完成初步注册，用户完成手机绑定操作后方才完成注册;"
      }
    ]
  },
  showList:function(e){
    var userClickIndex = e.currentTarget.dataset.index;
    this.data.listArray[userClickIndex].show = !this.data.listArray[userClickIndex].show;
    this.setData({
      listArray: this.data.listArray
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
   
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