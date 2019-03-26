// page/user-order-list/user-order-list.js
var app = getApp();
var util = require("../../util/util.js");

Page({
  data: {
    current: 0,
    service: [],
    shopComment: [],
    sserviceImg: [],
    orderlist: [],
    worderlist: [],
    corderlist: [],
    serviceImg: [],
    imgId: [],
    wserviceImg: [],
    wimgId: [],
    cserviceImg: [],
    cimgId: [],
    state: [{
      "id": 0,
      "state": "已下单"
    },
    {
      "id": 1,
      "state": "待评价"
    },
    {
      "id": 2,
      "state": "已完成"
    },
    {
      "id": 3,
      "state": "已取消"
    }],
    token: null,
    userId: null,
    currtab: 0,
    swipertab: [{ name: '全部', index: 0 }, { name: '待评价', index: 1 }, { name: '已取消', index: 2 }, { name: '我的评价', index: 3 }],
  },
  onLoad: function () {
    var that = this
    try {//同步获取与用户信息有关的缓存token
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
      }
      else {
        wx.request({
          url: app.globalData.serviceUrl + '/SearchStreet/wechat/getUserInfo',
          data: {
            token: token
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
              that.setData(
                {
                  userId: res.data.personInfo.userId
                }
              )
            }
          }
          , fail: function (err) {
            console.log(err)
          }
        })
      }
    } catch (e) {
      console.log("error");
    }
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    // 页面渲染完成
    this.getDeviceInfo()
    this.orderShow()
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    this.orderShow()
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
  getDeviceInfo: function () {
    let that = this
    wx.getSystemInfo({
      success: function (res) {
        that.setData({
          deviceW: res.windowWidth,
          deviceH: res.windowHeight
        })
      }
    })
  },

  /**
  * 选项卡点击切换
  */
  tabSwitch: function (e) {
    var that = this
    if (this.data.currtab === e.target.dataset.current) {
      return false
    } else {
      that.setData({
        currtab: e.target.dataset.current
      })
    }
  },

  tabChange: function (e) {
    this.setData({ currtab: e.detail.current })
    this.orderShow()
  },

  orderShow: function () {
    let that = this
    switch (this.data.currtab) {
      case 0:
        that.allShow()
        break
      case 1:
        that.waitShow()
        break
      case 2:
        that.cancelShow()
        break
      case 3:
        that.commentShow()
        break
    }
  },
  allShow: function () {
    var that = this;
    //查询用户的订单
    wx.request({
      url: app.globalData.serviceUrl + '/SearchStreet/order/getOrderlistAndServicebyuo?userId=' + that.data.userId,
      data: {},
      method: "GET",
      success: function (res) {
        console.log(res);
        if (res.data.success) {
          var order = res.data.OrderList;
          var img = [];
          var id = [];
          var service = res.data.serviceList;
          for (var i = 0; i < order.length; i++) {
            id[i] = order[i].orderId;
            if (service[i].serviceImgAddr != null)
              img[i] = app.globalData.imgUrl + service[i].serviceImgAddr;
            var time = JSON.stringify(order[i].createTime);
            order[i].createTime = util.formatDate(time);
            if (order[i].overTime != null) {
              time = JSON.stringify(order[i].overTime);
              order[i].overTime = util.formatDate(time);
            }
            else
              order[i].overTime = '';
          }
          that.setData({
            orderlist: order,
            serviceImg: img,
            imgId: id,
            service: service
          })
        }
      }
    })
  },

  waitShow: function () {
    var that = this;
    //查询用户待评价订单
    wx.request({
      url: app.globalData.serviceUrl + '/SearchStreet/order/getOrderlistbyuo?userId=' + that.data.userId + '&orderStatus=1',
      data: {},
      method: "GET",
      success: res => {
        console.log(res);
        if (res.data.success) {
          var order = res.data.OrderList;
          var img = that.data.serviceImg;
          var simg = [];
          var simgid = [];
          var id = that.data.imgId;
          for (var i = 0; i < order.length; i++) {
            var time = JSON.stringify(order[i].createTime);
            order[i].createTime = util.formatDate(time);
            if (order[i].overTime != null) {
              time = JSON.stringify(order[i].overTime);
              order[i].overTime = util.formatDate(time);
            }
            else
              order[i].overTime = '';
            for (var j = 0; j < img.length; j++) {
              if (id[j] == order[i].orderId) {
                simgid[i] = id[j];
                if (img[j] != null) {
                  simg[i] = img[j];
                }
              }
            }
          }
          that.setData({
            worderlist: res.data.OrderList,
            wserviceImg: simg,
            wimgId: simgid
          })
        }
      }
    })
  },

  cancelShow: function () {
    var that = this;
    //查询用户已取消的订单
    wx.request({
      url: app.globalData.serviceUrl + '/SearchStreet/order/getOrderlistbyuo?userId=' + that.data.userId + '&orderStatus=3',
      data: {},
      method: "GET",
      success: res => {
        console.log(res);
        if (res.data.success) {
          {
            var order = res.data.OrderList;
            var img = that.data.serviceImg;
            var cimg = [];
            var id = that.data.imgId;
            var cimgid = [];
            for (var i = 0; i < order.length; i++) {
              var time = JSON.stringify(order[i].createTime);
              order[i].createTime = util.formatDate(time);
              if (order[i].overTime != null) {
                time = JSON.stringify(order[i].overTime);
                order[i].overTime = util.formatDate(time);
              }
              else
                order[i].overTime = '';
              for (var j = 0; j < img.length; j++) {
                if (id[j] == order[i].orderId) {
                  cimgid[i] = id[j];
                  if (img[j] != null) {
                    cimg[i] = img[j];
                  }
                }
              }
            }
            that.setData({
              corderlist: res.data.OrderList,
              cserviceImg: cimg,
              cimgId: cimgid
            })
          }
        }
      }
    })
  },

  commentShow: function () {
    var that = this;
    //查询用户的评论
    wx.request({
      url: app.globalData.serviceUrl + '/SearchStreet/shopComment/getshopCommentlistbyuid?userId=' + that.data.userId,
      data: {},
      method: "GET",
      success: res => {
        console.log(res);
        if (res.data.success) {
          var shopComment = res.data.shopCommentList;
          var service = res.data.serviceList;
          var img = [];
          for (var i = 0; i < shopComment.length; i++) {
            if (service[i].serviceImgAddr != null) {
              img[i] = app.globalData.imgUrl + service[i].serviceImgAddr;
            }
          }

          that.setData({
            shopComment: shopComment,
            sserviceImg: img,
            service: service
          })
        }
      }
    })
  },
  cancelOrder: function (e) {
    wx.showToast({
      title: '正在取消订单，请稍候...',
      icon: 'loading',
      duration: 2000
    })
    var that = this;
    var order = e.target.dataset.item;
    console.log(order)
    order.orderStatus = 3;
    order.createTime = new Date(order.createTime);
    if (order.overTime != '')
      order.overTime = new Date(order.overTime);
    order = JSON.stringify(order);
    wx.request({
      url: app.globalData.serviceUrl + "/SearchStreet/order/modifyOrder",
      data: order,
      method: 'POST',
      header: {
        "Content-Type": 'application/json'
      },
      success: res => {
        console.log(res);
        if (res.data.success) {
          that.allShow();
        }
      }
    })
  },
  finishOrder: function (e) {
    var that = this;
    var order = e.target.dataset.item;
    console.log(order)
    order.orderStatus = 1;
    order.createTime = new Date(order.createTime);
    if (order.overTime != '')
      order.overTime = new Date(order.overTime);
    order = JSON.stringify(order);
    wx.request({
      url: app.globalData.serviceUrl + '/SearchStreet/order/modifyOrder',
      data: order,
      method: 'POST',
      header: {
        "Content-Type": 'application/json'
      },
      success: res => {
        console.log(res);
        if (res.data.success) {
          that.allShow();
        }
      }
    })
  },
  addComment: function (e) {
    var that = this;
    var service = that.data.service[e.target.dataset.id];
    var order = e.target.dataset.item;
    wx.navigateTo({
      url: '../comment/comment?service=' + JSON.stringify(service) + '&order=' + JSON.stringify(order),
    })
    that.allShow()
  },
  serviceDetail: function (e) {
    var that = this;
    var service = that.data.service[e.currentTarget.dataset.id];
    wx.navigateTo({
      url: '../service/service?service=' + JSON.stringify(service),
    })
  }
})