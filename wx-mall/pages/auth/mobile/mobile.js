var api = require('../../../config/api.js');
var util = require('../../../utils/util.js');
var app = getApp()

Page({
    data: {
        mobile: '',
        userInfo: {
            avatarUrl: '',
            nickName: ''
        },
        loginName: '',
        loginUrl: '',
        telephone: '',
        bindResult:false,
        disableGetMobileCode: false,
        disableSubmitMobileCode: true,
        getCodeButtonText: '获取验证码'
    },
    validateMobile: function (mobile) {
      /*手机号加密处理*/
      var first = mobile.substr(0, 3);
      var last = mobile.substr(mobile.length - 4, 4);
      var finalPhone = first + '****' + last;
      return finalPhone;
    },
    onShow: function () {
      /*获取手机号*/
      var that = this;
      util.request(api.UserMobile).then(function (res) {
        var mobile = res.data.mobile;
        that.setData({
          loginName: res.data.nickname,
          loginUrl: res.data.avatar
        })
        if (mobile == null || mobile == '') {
          that.setData({
            bindResult:true
          })
        } else {
          that.setData({
            telephone: that.validateMobile(mobile),
          })
        }
      })
    },

    onLoad: function () {
        var that = this
        that.setData({userInfo: app.globalData.userInfo})

        if (app.globalData.token) {
        } else {
            var token = wx.getStorageSync('userToken')
            if (token) {
                app.globalData.token = token
            }
        }

    },

    bindCheckMobile: function (mobile) {
        if (!mobile) {
            wx.showModal({
                title: '错误',
                content: '请输入手机号码'
            });
            return false
        }
        if (!mobile.match(/^1[3-9][0-9]\d{8}$/)) {
            wx.showModal({
                title: '错误',
                content: '手机号格式不正确，仅支持国内手机号码'
            });
            return false
        }
        return true
    },

    bindGetPassCode: function (e) {
        var that = this
        that.setData({disableGetMobileCode: true})
    },

    bindInputMobile: function (e) {
        this.setData({
            mobile: e.detail.value,
        })
    },

    countDownPassCode: function () {
        if (!this.bindCheckMobile(this.data.mobile)) {
            return
        }
        util.request(api.SmsCode, {phone: this.data.mobile},'post','application/json')
            .then(function (res) {
                if (res.errno == 0) {
                    wx.showToast({
                        title: '发送成功',
                        icon: 'success',
                        duration: 1000
                    })
                    var pages = getCurrentPages()
                    var i = 60;
                    var intervalId = setInterval(function () {
                        i--
                        if (i <= 0) {
                            pages[pages.length - 1].setData({
                                disableGetMobileCode: false,
                                disableSubmitMobileCode: false,
                                getCodeButtonText: '获取验证码'
                            })
                            clearInterval(intervalId)
                        } else {
                            pages[pages.length - 1].setData({
                                getCodeButtonText: i+'s',
                                disableGetMobileCode: true,
                                disableSubmitMobileCode: false
                            })
                        }
                    }, 1000);
                }else{
                  wx.showModal({
                    title: '提示',
                    content: res.errmsg,
                    showCancel: false
                  })
                }
            });

    },

    bindLoginMobilecode: function (e) {
        var mobile = this.data.mobile;
        if (!this.bindCheckMobile(mobile)) {
            return
        }
        if (!(e.detail.value.code && e.detail.value.code.length === 4)) {
            return
        }
      util.request(api.BindMobile, { mobile_code: e.detail.value.code, mobile: mobile }, 'post', 'application/json')
            .then(function (res) {
                if (res.errno == 0) {
                    wx.switchTab({
                        url: '/pages/index/index'
                    });
                } else {
                    wx.showModal({
                        title: '提示',
                        content: res.errmsg,
                        showCancel: false
                    })
                }
            })
    }
})