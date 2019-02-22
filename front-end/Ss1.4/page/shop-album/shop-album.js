// page/shop-album/shop-album.js

var app=getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {

    current: 'tab1',
    shopId: null,
    shopImgList:null,
    businessLicenseImg:null,
    imgUrl: app.globalData.imgUrl,
    loading:true
  },

  handleChange({ detail }) {
    this.setData({
      current: detail.key
    });
  },
  
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    that.setData({
      shopId: options.id
    })
    //获取商铺信息
    wx.request({
      url: app.globalData.serviceUrl + "/SearchStreet/shopadmin/getshopbyid?shopId=" + that.data.shopId,
      data: {},
      method: "GET",
      success: res => {
        var shop = res.data.shop;
        if (shop.businessLicenseImg!=null)
          shop.businessLicenseImg = that.data.imgUrl + shop.businessLicenseImg;
        for (var i = 0; i < shop.shopImgList.length; i++) {
          shop.shopImgList[i].imgAddr = that.data.imgUrl + shop.shopImgList[i].imgAddr;
        }
        that.setData({
          businessLicenseImg:shop.businessLicenseImg,
          shopImgList:shop.shopImgList,
          loading:false
        })
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