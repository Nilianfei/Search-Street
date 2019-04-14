// page/shop/shop.js
// var score = 80;
// var score_rank = 5;
// score_rank = score_rank.toFixed(1);
// var score_rate = 0.8;
var app=getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    shop :{},
    shopId:null,
    imgUrl: app.globalData.imgUrl,
    score: 0,
    score_rank: 0,
    score_rate: 0,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that=this;
    console.log("get"+options.shopId);
    that.setData({
      shopId:options.shopId
    })
    //获取商铺信息
    wx.request({
      url: app.globalData.serviceUrl+"/SearchStreet/shopadmin/getshopbyid?shopId=" + that.data.shopId,
      data: {},
      method: "GET",
      success: res => {
        console.log(res);
        var shop=res.data.shop;
        shop.profileImg=that.data.imgUrl+shop.profileImg;
        that.setData(
          {
            shop: res.data.shop,//设置页面中的数据
          }
        )
      }
    })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    var that = this;
    //获取商铺信息
    wx.request({
      url: app.globalData.serviceUrl + "/SearchStreet/shopComment/getAvgScorebyshopid?shopId=" + that.data.shopId, //获取评分信息
      data: {},
      method: "GET",
      success: res => {
        console.log(res);
        that.setData({
          score: res.data.serviceAvg.toFixed(1),
          score_rank: res.data.starAvg.toFixed(1),
          score_rate: res.data.successRate
        })
      }
    });

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