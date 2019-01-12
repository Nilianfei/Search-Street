//const openIdUrl = require('./config').openIdUrl

App({
  onLaunch: function () {
    var that = this;
    wx.checkSession({
      success() {
        // 查看是否授权
        wx.getSetting({
          success: function (res) {
            if (res.authSetting['scope.userInfo']) {
              wx.getUserInfo({
                success: function (res) {
                  //授权成功后，跳转进入小程序首页
                  wx.redirectTo({
                    url: '../index/index'
                  })
                }
              });
            }
          }
        })
      }
    })
  },
   
  onShow: function () {
    console.log('App Show')
  },
  onHide: function () {
    console.log('App Hide')
  },
  uploadAImg:function(data){
    var that= this;
    wx.uploadFile({
      url: data.url,
      filePath: data.filePath,
      name: data.fileName,
      success: function (res) {
        console.log("upload"+ data.fileName + res.data);
      }, fail: function (error) {
        console.error(error + "上传"+data.fileName+"失败");
      }
    })
  },
  /**
       * 设置全局变量
       */
  globalData: {
    
  }

})
