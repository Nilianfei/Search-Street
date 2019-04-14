// page/service-list/service-list.js
var app = getApp();
Page({
  data: {
    //全局变量
    list: [],
    token:null,
    shopId:0,
    //加载样式是否显示
    loading: true,
    current: 1,
    pageNum: 0,
    pageSize:6,
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
        url: app.globalData.serviceUrl + '/SearchStreet/service/getservicelistbyshopid?shopId='+that.data.shopId+'&pageIndex='+that.data.current+'&pageSize='+that.data.pageSize,
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
    that.setData({
      shopId: options.shopId,
    });
    wx.request({
      url: app.globalData.serviceUrl + '/SearchStreet/service/getservicelistbyshopid?shopId=' + that.data.shopId + '&pageIndex=1' + '&pageSize=' + that.data.pageSize,
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
  },
   /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    // 页面渲染完成

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    var that = this       //很重要，一定要写
    wx.request({
      url: app.globalData.serviceUrl + '/SearchStreet/service/getservicelistbyshopid?shopId=' + that.data.shopId + '&pageIndex=1' + '&pageSize=' + that.data.pageSize,
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
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },
})