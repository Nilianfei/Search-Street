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
  //处理后端传来的时间
  timeStamp2String: function(time){

    var datetime = new Date();
    datetime.setTime(time);
    var year = datetime.getFullYear();
    var month = datetime.getMonth() + 1;
    var date = datetime.getDate();
    var hour = datetime.getHours();
    if(hour<= 9){
      hour = "0" + hour;
    }
    var minute = datetime.getMinutes();
    if (minute <= 9) {
      minute = "0" + minute;
    }
    var second = datetime.getSeconds();
    if (second <= 9) {
      second = "0" + second;
    }
    // var mseconds = datetime.getMilliseconds();
    return year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;//+"."+mseconds;
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
    imgUrl: "http://139.196.101.84:8080/image",
    //serviceUrl: "http://139.196.101.84:8080"
    serviceUrl: "http://localhost:8080"
  }

})
