// page/appeal/appeal.js
var app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    appeal:{"appealTitle":"appealTitle","appealContent":"西欧从v解耦让老师你们的浪费口水富士康"
    ,"phone":"123445567","province":"广东省","city":"广州市","district":"天河区"
    ,"fullAddress":"华南师范大学","appealMoreInfo":"null","souCoin":1,"startTime":"jklk","endTime":"sdfsdf"},
    bottonText:"立即求助",
    loading:true,
    appealId: null,
    token:null
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    that.setData({
      appealId: 2 //搜搜求助页面写好之后改为options.id
    })
    try {//同步获取与用户信息有关的缓存token
      const value = wx.getStorageSync('token')
      if (value) {
        that.setData({
          token: value
        })
      }
    } catch (e) {
      console.log("error");
    }
    //获取服务信息
    wx.request({
      url: app.globalData.serviceUrl+"/SearchStreet/appeal/getappealbyid?appealId=" + that.data.appealId,
      data: {},
      method: "GET",
      success: res => {
        console.log(res);
        var appeal=res.data.appeal;
        appeal.startTime = app.timeStamp2String(appeal.startTime);
        appeal.endTime = app.timeStamp2String(appeal.endTime);
        that.setData(
          {
            appeal: appeal,//设置页面中的数据
          }
        )
      }
    })
    //查询用户是否预约过服务
    wx.request({
      url: app.globalData.serviceUrl+"/SearchStreet/help/queryishelp?appealId=" + that.data.appealId + "&token=" + that.data.token,
      data: {},
      method: "GET",
      success: res => {
        if(res.data.success){
          that.setData({
            loading: false
          })
          if (res.data.ishelp == true) {
            that.setData({
              bottonText: "已求助"
            })
          }
        } else{
          if (res.data.errMsg == "token为空" || res.data.errMsg == "token无效") {
            wx.redirectTo({
              url: '../../page/login/login'
            })
          }
        }
      }
    })
  },

  helping: function () {
    var that = this;
    if (that.data.bottonText != "已求助") {
      wx.request({
        url: app.globalData.serviceUrl+"/SearchStreet/help/addHelp?token="+that.data.token,
        data: {
          appealId: that.data.appealId,
        },
        method: "POST",
        success: res => {
          that.setData({
            bottonText: "已求助"
          });
          wx.redirectTo({
            url: ''//返回搜搜求助页面
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