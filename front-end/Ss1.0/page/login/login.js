var app = getApp()
const APP_ID = 'wxe352f27338f7b857'//输入小程序appid  
const APP_SECRET = '5c2d9aaca4d884c59b948a000ed91b04'//输入小程序app_secret  
var OPEN_ID = ''//储存获取到openid  
var SESSION_KEY = ''//储存获取到session_key  
Page({
  onLoad: function () {
    this.setData({
      hasLogin: app.globalData.hasLogin
    })
  },
  data: {},
  login:function(){
    var that = this;
    wx.login({
      success: function (res) {
        
        wx.request({
          //获取openid接口  
          url: 'https://api.weixin.qq.com/sns/jscode2session',
          data: {
            appid: APP_ID,
            secret: APP_SECRET,
            js_code: res.code,
            grant_type: 'authorization_code'
          },
          method: 'GET',
          success: function (res) {
            console.log(res.data)
            OPEN_ID = res.data.openid;//获取到的openid  
            SESSION_KEY = res.data.session_key;//获取到session_key  
            wx.showModal({
              title: '提示注册',         
              content: '您的商铺当前还未注册，请先注册',
              success: function (res) {
                if (res.confirm) {//这里是点击了确定以后
                 // console.log('用户点击确定')
                 wx.redirectTo({
                   url: '../register/register',
                 })
                } else {//这里是点击了取消以后
                  console.log('用户点击取消')
                }
              }
            })
          }
        })
      },
      fail: function (err) {
        console.log('wx.login 接口调用失败，将无法正常使用开放接口等服务', err)
        callback(err)
      }
    })
  }  
})  
