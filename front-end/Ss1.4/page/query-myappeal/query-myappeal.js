var app=getApp();
var util = require('../../util/util.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    value:'',
    pageSize:15,
    appealList:[],
  },

/* 获取用户输入的求助记录关键字 */
  myhelpkey:function(e){
    console.log(e);
    this.setData({
      value:e.detail.detail.value
    })
    console.log(this.data.value)
  },

/* 搜求助按钮功能 */
  handleClick:function(){
    var token = null;
    var that = this;
    try {
      const value = wx.getStorageSync('token')
      if (value) {
        token = value;
      }
    } catch (e) {
      console.log("error");
    }
    wx.request({
      url: app.globalData.serviceUrl + "/SearchStreet/appeal/getappeallistbyuserid?token=" + token + " &pageIndex=" + 0 + "&pageSize=" + that.data.pageSize,
        data: {
           appealTitle:that.data.value
       },
      method: 'POST',
      success: function (res) {
        console.log(res.data);
        if (res.data.success) {
          console.log(res.data);
          var appeallist=res.data.appealList;
          for (var i = 0; i < appeallist.length; i++) {
            var format = appeallist[i];
            format.endTime = util.formatDate1(appeallist[i].endTime);
            appeallist[i] = format;
          }
          console.log(appeallist);
          that.setData({
            appealList:appeallist
          })
          console.log(that.data.appealList);
        }
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    
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
    this.setData({
      appealList:this.data.appealList
    })
    console.log(this.data.appealList)
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