//const openIdUrl = require('./config').openIdUrl

App({
  onLaunch: function () {
    var that = this;
    wx.login({
      success: res => {
        wx.request({
          // 这里的 url 是微信官方提供的可以在小程序中直接通过 code 来获取 openid 的
          url: 'https://api.weixin.qq.com/sns/jscode2session?appid=wxe352f27338f7b857&secret=5c2d9aaca4d884c59b948a000ed91b04&js_code=' + res.code + '&grant_type=authorization_code',
          success: res => {
            that.globalData.openid = res.data.openid;
          }
        })
      }
    });
  },
   
  onShow: function () {
    console.log('App Show')
  },
  onHide: function () {
    console.log('App Hide')
  },
  /**
       * 设置全局变量
       */
  globalData: {
    openid: null
  }

})
