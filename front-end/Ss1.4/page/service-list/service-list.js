// page/service-list/service-list.js
var app = getApp();
Page({
  data: {
    //全局变量
    list: [],
    token:null,
    //加载样式是否显示
    loading: true,
    current: 1,
    pageNum: 0,
    pageSize: 15,
  },
  handleChange({ detail }) {
    var that = this;

    if (that.data.current <= that.data.pageNum) {
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
        url: "",
        data: {
          token: that.data.token,
          pageIndex: that.data.current,
          pageSize: that.data.pageSize
        },
        method: 'GET',
        success: function (res) {
          var serviceList = res.data.serviceList;//res.data就是从后台接收到的值
          that.setData({
            list: serviceList,
            loading: false,
            pageNum: res.data.pageNum
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
      url: "",
      data: {
        token: that.data.token,
        pageIndex: that.data.current,
        pageSize: that.data.pageSize
      },
      method: 'GET',
      success: function (res) {
        var serviceList = res.data.serviceList;//res.data就是从后台接收到的值
        that.setData({
          list: serviceList,
          loading: false,
          pageNum: res.data.pageNum
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