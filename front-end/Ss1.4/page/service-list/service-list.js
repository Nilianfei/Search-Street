// page/service-list/service-list.js
var app = getApp();
Page({
  data: {
    //全局变量
    list: [],
    //加载样式是否显示
    loading: true
  },

  onLoad: function (options) {
    var that = this       //很重要，一定要写
    var token = null;
    try {
      const value = wx.getStorageSync('token')
      if (value) {
        token = value;
      }
    } catch (e) {
      console.log("error");
    }
    wx.request({
      url: '',
      data: {},
      method: 'GET',
      success: function (res) {
        var serviceList = res.data.serviceList;//res.data就是从后台接收到的值
        that.setData({
          list: serviceList,
          loading: false
        })
      },
      fail: function (res) {
        console.log('submit fail');
      },
      complete: function (res) {
        console.log('submit complete');
      }
    })
  }
})