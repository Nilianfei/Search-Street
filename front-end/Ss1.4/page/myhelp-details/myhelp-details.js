// page/myhelp-details/myhelp-details.js
var app=getApp();
var util=require("../../util/util.js");
Page({

  /**
   * 页面的初始数据
   */
  data: {
    helpDetail:[],
    targetTime:0,
    clearTimer:false,
    fulladdress:null,
    imgUrl: app.globalData.imgUrl,
  },

/**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that=this;
    console.log(options);
    wx.request({
      url: app.globalData.serviceUrl+"/SearchStreet/appeal/getappealbyid?appealId="+options.id,
      data:{

      },
      method:'GET',
      success(res){
        console.log(res.data);
        if(res.data.success){
          var appeal=res.data.appeal;
          if (appeal.endTime - new Date().getTime() == 0) var time = util.formatTime(0);
          else var time = util.formatTime(Math.round((appeal.endTime - new Date().getTime())/1000));
          var address=appeal.province+appeal.city+appeal.district+appeal.fullAddress;
          console.log(time);
          for (var i = 0; i < appeal.appealImgList.length; i++) {
            appeal.appealImgList[i].imgAddr = that.data.imgUrl + appeal.appealImgList[i].imgAddr;
          }
          if(appeal.appealMoreInfo!=""){
            address = address + ' (' + appeal.appealMoreInfo+' )';
          } 
          that.setData({
             helpDetail:appeal,
             targetTime:time,
             fulladdress:address
          }),
           
          console.log(that.data.targetTime);
        }
      }
    })
  },

  /* 图片预览函数 */
  previewImage: function (e) {
    var current = e.target.dataset.src;
    var imgList=[];
    for (var i = 0; i < this.data.helpDetail.appealImgList.length; i++){
      imgList.push(this.data.helpDetail.appealImgList[i].imgAddr);
    }
    wx.previewImage({
      current: current,
      urls: imgList
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