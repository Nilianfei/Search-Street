// page/shop-settings/shop-settings.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    shopName: "shopName",
    phone: 111111,
    perCost: 15,
    businessLicenseCode: 111111,
    businessScope: 'food',
    shopMoreInfo: 'shopMoreInfo',
    
    isMobile: [{
      id: 0,
      name: '固定商铺',
    }, {
      id: 1,
      name: '移动商铺'
    }],

    province: "province",
    city: "city",
    district: "district",
    fullAddress: 'fullAddress',
    longitude: null,
    latitude: null,
  },

  handleIsMobileChange({
    detail = {}
  }) {
    this.setData({
      current: detail.value
    });
  },

  bindRegionChange(e) {
    console.log('picker发送选择改变，携带值为', e.detail.value)
    this.setData({
      region: e.detail.value
    })
  },

/**
 * 生命周期函数--监听页面加载
 */
onLoad: function(options) {

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