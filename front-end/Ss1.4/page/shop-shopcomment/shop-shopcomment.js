// page/shop-shopcomment/shop-shopcomment.js
var app = getApp();
var util = require("../../util/util.js");
Page({

  /**
   * 页面的初始数据
   */
  data: {
    current: 0,
    service: [],
    shopComment: [],
    sserviceImg: [],
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
  },
  commentShow: function () {
    var that = this;
    //查询商家的评论
    wx.request({
      url: app.globalData.serviceUrl + '/SearchStreet/shopComment/getshopCommentlistbysid?shopId=' + that.data.shopId,
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
              console.log(img[i])
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
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this
    that.setData({
      shopId:options.shopId
    })
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
          userId: userId,
          shopId:options.shopId
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
    this.commentShow()
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    this.commentShow()
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

  }
})