// page/order-detail/order-detail.js
var app = getApp();
var util = require("../../util/util.js");

Page({
  data: {
    current: 0,
    service: null,
    order:null,
    createTime:"",
    overTime:"",
    state: [{
      "id": 0,
      "color": "green",
      "state": "已下单"
    },
    {
      "id": 1,
      "color": "orange",
      "state": "待评价"
    },
    {
      "id": 2,
      "color": "gray",
      "state": "已完成"
    },
    {
      "id": 3,
      "color": "grey",
      "state": "已取消"
    },
    {
      "id": 4,
      "color": "black",
      "state": "已删除"
    }

    ],
    token: null,
    userId: null,
  },
  onLoad: function (options) {
    var that = this
    var service = JSON.parse(options.service);
    var order = JSON.parse(options.order);
    that.setData({
      service: service,
      order: order
    })
    try { //同步获取与用户信息有关的缓存token
      const value = wx.getStorageSync('token');
      const userId = wx.getStorageSync('userId');
      if (value) {
        that.setData({
          token: value
        })
      }
      if (userId) {
        that.setData({
          userId: userId
        })
      } else {
        wx.request({
          url: app.globalData.serviceUrl + '/SearchStreet/wechat/getUserInfo',
          header: {
            token: token,
          },
          data: {

          },
          success: function (res) {
            // 拿到自己后台传过来的数据，自己作处理
            console.log(res.data);
            if (null != res.data.success && res.data.success) {
              //用户登录成功
              wx.setStorage({
                key: 'userId',
                data: res.data.personInfo.userId
              });
              that.setData({
                userId: res.data.personInfo.userId
              })
            }
          },
          fail: function (err) {
            console.log(err)
          }
        })
      }
    } catch (e) {
      console.log("error");
    }
    if(order!=null)
    {
      var time = JSON.stringify(order.createTime);
      var createTime = util.formatDate(time);
      var overTime="";
      if (order.overTime != null) {
        time = JSON.stringify(order.overTime);
        overTime = util.formatDate(time);
       }   
       that.setData({
         createTime:createTime,
         overTime:overTime
       })
    }
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
    if (order != null) {
      var time = JSON.stringify(order.createTime);
      var createTime = util.formatDate(time);
      var overTime = "";
      if (order.overTime != null) {
        time = JSON.stringify(order.overTime);
        overTime = util.formatDate(time);
      }
      that.setData({
        createTime: createTime,
        overTime: overTime
      })
    }
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
  cancelOrder: function (e) {
    wx.showToast({
      title: '正在取消订单，请稍候...',
      icon: 'loading',
      duration: 1000
    })
    var that = this;
    var order = e.target.dataset.item;
    console.log(order)
    order.orderStatus = 3;
    order = JSON.stringify(order);
    wx.request({
      url: app.globalData.serviceUrl + "/SearchStreet/order/changeorderstatus",
      data: order,
      method: 'POST',
      header: {
        "Content-Type": 'application/json'
      },
      success: res => {
        console.log(res);
        if (res.data.success) {
         that.setData({
           order:res.data.order
         })
        }
      }
    })
  },
  finishOrder: function (e) {
    var that = this;
    var order = e.target.dataset.item;
    console.log(order)
    order.orderStatus = 1;
    order = JSON.stringify(order);
    wx.request({
      url: app.globalData.serviceUrl + '/SearchStreet/order/changeorderstatus',
      data: order,
      method: 'POST',
      header: {
        "Content-Type": 'application/json'
      },
      success: res => {
        console.log(res);
        if (res.data.success) {
          that.setData({
            order: res.data.order
          })
        }
      }
    })
  },
})