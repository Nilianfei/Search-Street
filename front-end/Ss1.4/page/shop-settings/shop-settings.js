// page/shop-settings/shop-settings.js
var app = getApp();
var QQMapWX = require('../../util/qqmap-wx-jssdk.min.js');
var qqmapsdk;

Page({

  /**
   * 页面的初始数据
   */
  data: {
    shop: {},
    latitude: 23.139317,
    longitude: 113.352354,
    isMobile: [{
      id: 0,
      name: '固定商铺',
    }, {
      id: 1,
      name: '移动商铺'
    }],
    current: "固定商铺",
  },

  handleIsMobileChange({
    detail = {}
  }) {
    var p="shop.isMobile";
    this.setData({
      current: detail.value,
    });
    
    if(this.data.current=="固定商铺"){
      this.setData({
        [p]: 0  
      })
    }
    else if(this.data.current=="移动商铺") {
      this.setData({
        [p]: 1
      })
    }
    else {
      console.log("error!");
    }
    console.log(this.data.shop.isMobile);
  },
  /**
   * bind:change事件集合
   */
  inputShopName:function(e) {
    var p="shop.shopName";
    this.setData({
      [p]:e.detail.detail.value
    });
    console.log(this.data.shop.shopName);
  },

  inputPhone:function(e) {
    var p = "shop.phone";
    this.setData({
      [p]: e.detail.detail.value
    });
    console.log(this.data.shop.phone);
  },

  inputBusinessScope:function(e) {
    var p = "shop.businessScope";
    this.setData({
      [p]: e.detail.detail.value
    });
    console.log(this.data.shop.businessScope);
  },

  inputPerCost:function(e) {
    var p = "shop.perCost";
    this.setData({
      [p]: e.detail.detail.value
    });
    console.log(this.data.shop.perCost);
  },

  inputBusinessLicenceCode:function(e) {
    var p = "shop.businessLicenceCode";
    this.setData({
      [p]: e.detail.detail.value
    });
    console.log(this.data.shop.businessLicenceCode);
  },

  inputprovince: function (e) {
    var p = "shop.province";
    this.setData({
      [p]: e.detail.detail.value
    });
    console.log(this.data.shop.province);
  },

  inputcity: function (e) {
    var p = "shop.city";
    this.setData({
      [p]: e.detail.detail.value
    });
    console.log(this.data.shop.city);
  },

  inputdistrict: function (e) {
    var p = "shop.district";
    this.setData({
      [p]: e.detail.detail.value
    });
    console.log(this.data.shop.district);
  },

  inputFullAddress: function (e) {
    var p = "shop.fullAddress";
    this.setData({
      [p]: e.detail.detail.value
    });
    console.log(this.data.shop.fullAddress);
  },

  inputShopMoreInfo: function (e) {
    var p = "shop.shopMoreInfo";
    this.setData({
      [p]: e.detail.detail.value
    });
    console.log(this.data.shop.shopMoreInfo);
  },

/**
 * 生命周期函数--监听页面加载
 */
  onLoad: function(options) {
    var that = this;
    console.log("get" + options.shopId);
    that.setData({
      shopId: options.shopId
    })
    //获取商铺信息
    wx.request({
      url: app.globalData.serviceUrl + "/SearchStreet/shopadmin/getshopbyid?shopId=" + that.data.shopId,
      data: {},
      method: "GET",
      success: res => {
        console.log(res);
        var shop = res.data.shop;
        shop.profileImg = that.data.imgUrl + shop.profileImg;
        that.setData(
          {
            shop: res.data.shop,//设置页面中的数据
            longitude: res.data.shop.longitude,
            latitude: res.data.shop.latitude,
            markers: [{
              id: 0,
              latitude: this.data.latitude,
              longitude: this.data.longitude,
              iconPath: '../../images/round-pushpin_1f4cd.png',//图标路径
              width: 30,
              height: 30,
            }],
          }
        )
      }
    })

    this.mapCtx = wx.createMapContext('myMap');
    this.mapCtx.moveToLocation();

    //实体化qqmap(API)核心类
    qqmapsdk = new QQMapWX({
      key: 'DQYBZ-AQFK6-6JDSI-EHRZV-EFRCJ-TDFZU'
    });


  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function() {
    
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function() {

  }
})