// page/myservice-detail/myservice-detail.js
var app = getApp();
var util = require("../../util/util.js");
var showinfo='你已经被选定为帮助者'
function countdown(that) {
  that.setData({
    targetTime: util.formatTime(that.data.clock),
  })
  var second = that.data.clock;
  //console.log(second);
  if (second == 0) {

    return;
  }
  var time = setTimeout(function () {
    that.data.clock -= 1;
    countdown(that);
  }
    , 1000)
}
Page({

  /**
   * 页面的初始数据
   */
  data: {
    helpDetail: [],
    targetTime: 0,
    fulladdress: null,
    imgUrl: app.globalData.imgUrl,
    clock: 0,
    id: 0,
    imgfilepath: [],
    helpid:0,
    showinfo:'',
    help:[],
  },

  /**
     * 生命周期函数--监听页面加载
     */
  onLoad: function (options) {
    var that = this;
    var helperlistall = [];
    console.log(options);
    that.setData({
      id: options.id,
      helpid:options.helpid,
    })
    console.log(that.data.helpid);
    wx.request({
      url: app.globalData.serviceUrl + "/SearchStreet/appeal/getappealbyid?appealId=" + options.id,
      data: {

      },
      method: 'GET',
      success(res) {
        console.log(res.data);
        if (res.data.success) {
          var appeal = res.data.appeal;
          if (appeal.endTime - new Date().getTime() <= 0) that.data.clock = 0;      //此处有错误
          else that.data.clock = Math.floor((appeal.endTime - new Date().getTime()) / 1000);
          var address = appeal.province + appeal.city + appeal.district + appeal.fullAddress;
          console.log(that.data.clock);
          countdown(that);
          for (var i = 0; i < appeal.appealImgList.length; i++) {
            appeal.appealImgList[i].imgAddr = that.data.imgUrl + appeal.appealImgList[i].imgAddr;
          }
          if (appeal.appealMoreInfo != "") {
            address = address + ' (' + appeal.appealMoreInfo + ' )';
          }
          that.setData({
            helpDetail: appeal,
            // clock:util.formatTime(that.data.clock),
            fulladdress: address

          }),

            console.log(that.data.clock);
        }
      }
    })
    wx.request({
      url: app.globalData.serviceUrl+"/SearchStreet/help/gethelpbyhelpid",
      data:{
        helpId:that.data.helpid
      },
      method:'GET',
      success(res){
        console.log(res.data);
        if(res.data.success){
          var help=res.data.help;
          if(help.helpStatus==1){
            that.setData({
              showinfo:showinfo,
            })
          }
          that.setData({
            help:help
          })
        }
      }
    })
},

  /* 图片预览函数 */
  previewImage: function (e) {
    var current = e.target.dataset.src;
    var imgList = [];
    for (var i = 0; i < this.data.helpDetail.appealImgList.length; i++) {
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