// page/appeal/appeal.js
var app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    appeal:{"appealTitle":"","appealContent":""
    ,"phone":"","province":"","city":"","district":""
    ,"fullAddress":"","appealMoreInfo":"","souCoin":0,"startTime":"","endTime":""},
    bottonText:"立即帮忙",
    isHelp:false,
    loading:true,
    isDisabled: false,
    appealId: null,
    token:null,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    that.setData({
      appealId: options.appealId //搜搜求助页面写好之后改为options.id
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
    //查询用户是否提交过帮助该求助的申请
    wx.request({
      url: app.globalData.serviceUrl+"/SearchStreet/help/queryishelp?appealId=" + that.data.appealId + "&token=" + that.data.token,
      data: {},
      method: "GET",
      success: res => {
        console.log(res);
        if(res.data.success){
          that.setData({
            loading: false,
          })
          if (res.data.ishelp == true) {
            that.setData({
              isHelp: true,
              bottonText: "您已经申请，请等待...",
              isDisabled: true,
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
    if (!that.data.isHelp) {
      wx.request({
        url: app.globalData.serviceUrl+"/SearchStreet/help/addHelp?token="+that.data.token,
        data: {
          appealId: that.data.appealId,
          appealTitle: that.data.appeal.appealTitle,
        },
        method: "POST",
        success: res => {
          that.setData({
            bottonText: "您已经申请，请等待...",
          })
          wx.redirectTo({
            url: '../help-map/help-map'//返回搜搜求助页面
          })
        }
      })
    }
  },
  

  previewImg:function (e) {
    var index = e.currentTarget.id;
    console.log(index);
    var _urls=[];
    var _current;
    var appeal=this.data.appeal;
    for (var i=0;i< appeal.appealImgList.length; ++i){
      if(appeal.appealImgList[i].appealImgId==index) {
        console.log(appeal.appealImgList[i].appealImgId);
        _current = app.globalData.imgUrl + appeal.appealImgList[i].imgAddr;
      }
      _urls[i] = app.globalData.imgUrl+appeal.appealImgList[i].imgAddr;
    }
    wx.previewImage({
      urls:  _urls,
      current: _current,
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