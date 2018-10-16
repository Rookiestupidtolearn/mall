// pages/ucenter/accountSecurity/accountSecurity.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    loginName:'',
    loginUrl:'',
    telephone:'12345678915'
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
            url: '/pages/index/index'
          });
        }
      }
    })

  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    /*手机号加密处理*/ 
    var telephone = this.data.telephone;
    var first = telephone.substr(0,3);
    var last = telephone.substr(telephone.length-4,4);
    var final = first+'****'+last;
    that.setData({
      telephone:final
    })
    /*读取缓存信息*/
    wx.getStorage({
      key: 'userInfo',
      success: function(res) {
        that.setData({
          loginName:res.data.nickName,
          loginUrl:res.data.avatarUrl
        })
      },
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