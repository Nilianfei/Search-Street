// page/shop-list/shop-list.js
var app = getApp();
Page({
  data: {
    //全局变量
    list: [],
    //加载样式是否显示
    loading: true,
    isMobile: ["固定商铺","移动商铺"],
    enablestatus:["审核中","已许可","不许可"]
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
      url: "http://localhost:8080/ss/shopadmin/getshoplistbyuserid?token=" + token,
      data: {},
      method: 'GET',
      success: function (res) {
        var shopList = res.data.shopList;//res.data就是从后台接收到的值
        for (var i = 0; i < shopList.length; i++) {
          shopList[i].isMobile = that.data.isMobile[shopList[i].isMobile];
          shopList[i].enableStatus = that.data.enablestatus[shopList[i].enableStatus];
        }
        that.setData({
          list: shopList,
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