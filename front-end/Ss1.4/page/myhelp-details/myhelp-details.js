// page/myhelp-details/myhelp-details.js
var app=getApp();
var util=require("../../util/util.js");

function countdown(that) {
  that.setData({
    targetTime:util.formatTime(that.data.clock),
  })
  var second = that.data.clock;
  //console.log(second);
  if (second == 0) {
    
    return;
  }
  var time = setTimeout(function () {
    that.data.clock-=1;
    countdown(that);
  }
  , 1000)
}
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
    btn_helper:'选择他',
    disabled:false,
    clock:0,
    id:0,
  },

/**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that=this;
    console.log(options);
    that.setData({
      id:options.id
    })
    wx.request({
      url: app.globalData.serviceUrl+"/SearchStreet/appeal/getappealbyid?appealId="+options.id,
      data:{

      },
      method:'GET',
      success(res){
        console.log(res.data);
        if(res.data.success){
          var appeal=res.data.appeal;
          if (appeal.endTime - new Date().getTime() <= 0)  that.data.clock=3600*24;      //此处有错误
          else that.data.clock= Math.floor((appeal.endTime - new Date().getTime())/1000);
          var address=appeal.province+appeal.city+appeal.district+appeal.fullAddress;
          console.log(that.data.clock);
          countdown(that);
          for (var i = 0; i < appeal.appealImgList.length; i++) {
            appeal.appealImgList[i].imgAddr = that.data.imgUrl + appeal.appealImgList[i].imgAddr;
          }
          if(appeal.appealMoreInfo!=""){
            address = address + ' (' + appeal.appealMoreInfo+' )';
          } 
          that.setData({
             helpDetail:appeal,
            // clock:util.formatTime(that.data.clock),
             fulladdress:address
             
          }),
           
          console.log(that.data.clock);
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

/* 点击"选择他"按钮功能函数 */

choosehelper:function(e){
  var token = null;
  try {
    const value = wx.getStorageSync('token')
    if (value) {
      token = value;
    }
  } catch (e) {
    console.log("error");
  }                               
  /*同时给后台发一个消息，让其告知提供帮助的人，他已经被选定 */
  wx.request({
    url: app.globalData.serviceUrl+"/SearchStreet/help/selectHelper?token="+token,
    data:{
     appealId:this.data.id,
     helpId:1
    },
    method:'GET',
    success(res){
      console.log(res.data);
      if(res.data.success){
        this.setData({
          disabled: true,
          btn_helper: '已选择'
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