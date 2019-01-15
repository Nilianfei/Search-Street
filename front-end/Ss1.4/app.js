//const openIdUrl = require('./config').openIdUrl

App({
  onLaunch: function () {
   
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
