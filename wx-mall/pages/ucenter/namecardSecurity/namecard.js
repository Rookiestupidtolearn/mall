// pages/ucenter/accountSecurity/accountSecurity.js
var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');
var app = getApp()

Page({

  /**
   * 页面的初始数据
   */
  data: {
    name:'',
    idcard:''
  },
  bindname:function(e){
    this.setData({ name: e.detail.value })
  },
  bindidcard:function(e){
    this.setData({ idcard: e.detail.value })
  },
  confirm: function () {
    
    var name = this.data.name;
    var idcard = this.data.idcard;
    if(name == ""){
      util.showErrorToast('请输入用户姓名');
      return false;
    }
    
    if (idcard == ''){
      util.showErrorToast('请输入身份证号');
      return false;
    } else if (!idcard.match(/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/)){
      util.showErrorToast('身份证号不正确');
      return false;
    } else if (idcard.length == 15) {
      wx.showModal({
        title: '',
        content: '非18位证件号认证请联系客服',
        success: function (res) {
          if (res.confirm) {
            
          }
        }
      });
      return false;
    }

    let that = this;
    //调用接口
    util.request(api.bindUserIdcard, {
      username: that.data.name,
      idcard: that.data.idcard
    },'post','application/json').then(function (res) {
      console.log(res);
      if(res.code == '500'){
        util.showErrorToast(res.data);
      }else{
        wx.switchTab({ //跳转到tabbar页面
          url: '/pages/ucenter/index/index',
        })
      }  
    })

  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function () {

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