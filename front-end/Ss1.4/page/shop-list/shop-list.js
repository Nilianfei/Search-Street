// page/shop-list/shop-list.js
var app = getApp();
Page({
  data: {
    //全局变量
    list: [],
    //加载样式是否显示
    loading: true,
    token:null,
    isMobile: ["固定商铺","移动商铺"],
    enablestatus:["审核中","已许可","不许可"],
    current: 1,
    pageNum:0,
    pageSize: 15,
  },
  handleChange({ detail }) {
    var that= this;
    
    if(that.data.current<=that.data.pageNum){
      that.setData({
        loading: true
      })
      const type = detail.type;
      if (type === 'next') {
        that.setData({
          current: that.data.current + 1
        });
      } else if (type === 'prev') {
        that.setData({
          current: that.data.current - 1
        });
      }
      wx.request({
        url: app.globalData.serviceUrl+"/SearchStreet/shopadmin/getshoplistbyuserid",
        data: {
          token: that.data.token,
          pageIndex: that.data.current,
          pageSize: that.data.pageSize
        },
        method: 'GET',
        success: function (res) {
          if(res.data.success){
            var shopList = res.data.shopList;//res.data就是从后台接收到的值
            for (var i = 0; i < shopList.length; i++) {
              shopList[i].isMobile = that.data.isMobile[shopList[i].isMobile];
              shopList[i].enableStatus = that.data.enablestatus[shopList[i].enableStatus];
            }
            that.setData({
              list: shopList,
              loading: false,
              pageNum: res.data.pageNum
            })
          } else{
            if (res.data.errMsg == "token为空" || res.data.errMsg == "token无效") {
              wx.redirectTo({
                url: '../../page/login/login'
              })
            }
          }
        },
        fail: function (res) {
          console.log('submit fail');
        },
        complete: function (res) {
          console.log('submit complete');
        }
      })
    }
    
  },
  onLoad: function (options) {
    var that = this       //很重要，一定要写
    try {
      const value = wx.getStorageSync('token')
      if (value) {
        that.setData({
          token:value
        })
      }
    } catch (e) {
      console.log("error");
    }
    wx.request({
      url: app.globalData.serviceUrl+"/SearchStreet/shopadmin/getshoplistbyuserid",
      data: {
        token: that.data.token,
        pageIndex: 0,
        pageSize: that.data.pageSize
      },
      method: 'GET',
      success: function (res) {
        if (res.data.success) {
          var shopList = res.data.shopList;//res.data就是从后台接收到的值
          for (var i = 0; i < shopList.length; i++) {
            shopList[i].isMobile = that.data.isMobile[shopList[i].isMobile];
            shopList[i].enableStatus = that.data.enablestatus[shopList[i].enableStatus];
          }
          that.setData({
            list: shopList,
            loading: false,
            pageNum: res.data.pageNum
          })
        } else {
          if (res.data.errMsg == "token为空" || res.data.errMsg == "token无效") {
            wx.redirectTo({
              url: '../../page/login/login'
            })
          }
        }
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