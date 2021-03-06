var search_money='40';
var mymoney='2';
var app=getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    search_money:search_money,
    mymoney:mymoney
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log(options);
    var token = null;
    var that=this;
    try {                    //同步获取与用户信息有关的缓存token
      const value = wx.getStorageSync('token')
      if (value) {
        token = value;
      }
    } catch (e) {
      console.log("error");
    }
    wx.request({
      url: app.globalData.serviceUrl + '/SearchStreet/wechat/getUserInfo',
      data: {
        //token: token
      },
      header: {
        'content-type': 'application/json',
        'token': token
      },
      success: function (res) {
        // 拿到自己后台传过来的数据，自己作处理
        console.log(res.data);
        if (res.data.success) {
        that.setData({
          search_money: res.data.personInfo.souCoin,
          mymoney: res.data.personInfo.souCoin/20,
        })
      }
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