// page/service/service.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    service:null,
    bottonText:"立即预约",
    token:null,
    serviceId:null
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that= this;
    that.setData({
      serviceId:options.id
    })
    try {//同步获取与用户信息有关的缓存token
      const value = wx.getStorageSync('token')
      if (value) {
        that.setData({
          token:value
        })
      }
    } catch (e) {
      console.log("error");
    }
    //获取服务信息
    wx.request({
      url: "" + that.data.serviceId,
      data: {},
      method: "GET",
      success: res => {
        console.log(res);   
        that.setData(
          {
            service: res.data.service,//设置页面中的数据
          }
        )
      }
    })
    //查询用户是否预约过服务
    wx.request({
      url: "" + that.data.serviceId+"&token="+that.data.token,
      data: {},
      method: "GET",
      success: res => {
        console.log(res);
        if(res.isbook){
          that.setData({
            bottonText:"已预约"
          })
        }
      }
    })
  },

  booking:function(){
    var that = this;
    if(bottonText!="已预约"){
      wx.request({
        url: "" + that.data.serviceId + "&token=" + that.data.token,
        data: {},
        method: "GET",
        success: res => {
          that.setData({
            bottonText: "已预约"
          });
          var shopId = null;
          try {//同步获取缓存shopId
            const value = wx.getStorageSync('shopId')
            if (value) {
              shopId = value;
            }
          } catch (e) {
            console.log("error");
          }
          wx.redirectTo({
            url: '../page/service/service?id=shopId'//返回服务列表页面
          })
        }
      })
    }
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