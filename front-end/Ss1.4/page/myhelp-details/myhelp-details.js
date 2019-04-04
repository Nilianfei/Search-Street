// page/myhelp-details/myhelp-details.js
var app = getApp();
var util = require("../../util/util.js");

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
    clearTimer: false,
    fulladdress: null,
    imgUrl: app.globalData.imgUrl,
    btn_helper: [],
    disabled: [],
    clock: 0,
    id: 0,
    pageNum:0,
    pageSize:15,
    helperlist:[],
    imgfilepath:[],
    temp:0,
  },

  /**
     * 生命周期函数--监听页面加载
     */
  onLoad: function (options) {
    var that = this;
    var helperlistall=[];
    console.log(options);
    that.setData({
      id: options.id
    })
    wx.request({
      url: app.globalData.serviceUrl + "/SearchStreet/appeal/getappealbyid?appealId=" + options.id,
      data: {

      },
      method: 'GET',
      success(res) {
        console.log(res.data);
        if (res.data.success) {
          var appeal = res.data.appeal;
          if (appeal.endTime - new Date().getTime() <= 0) that.data.clock = 0;      
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
      url: app.globalData.serviceUrl + "/SearchStreet/help/gethelplistbyappealid?appealId=" + that.data.id + "&pageIndex=" + 0 + "&pageSize=" + that.data.pageSize,
      data:{

      },
      method:'GET',
      success(res){
        console.log(res.data);
        if(res.data.success){
          for(var i=0;i<res.data.helpList.length;i++){
            helperlistall[i]=res.data.helpList[i];
          }
          that.setData({
            helperlist: helperlistall
          })
        }
          console.log(helperlistall);
         for(var i=0;i<helperlistall.length;i++){
           if(helperlistall[i].helpStatus==0||helperlistall[i].helpStatus==3) {   
            // that.data.disabled[i]=false;
             //that.data.btn_helper[i]='选择他';
            helperlistall[i]=[{disabled:false,btn_helper:'选择他'}].concat(helperlistall[i]);
         }else if(helperlistall[i].helpStatus==1||helperlistall[i].helpStatus==2){
           //that.data.disabled[i]=true;
           //that.data.btn_helper[i]='已选择';
           helperlistall[i] = [{ disabled: true, btn_helper: '已选择' }].concat(helperlistall[i]);
         } 
         }
         console.log(helperlistall);
        that.setData({
          helperlist: helperlistall
        })
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

  /* 点击"选择他"按钮功能函数 */

  choosehelper: function (e) {
    console.log(e);
    this.setData({
      temp:e.target.id,
    })
    var token = null;
    var that = this;
    var helperlistall=that.data.helperlist;
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
      url: app.globalData.serviceUrl + "/SearchStreet/help/selectHelper",
      data: {
        appealId: this.data.id,
        helpId: e.currentTarget.dataset.id,
      },
      method: 'GET',
      header: {
        'content-type': 'application/json',
        'token': token
      },
      success(res) {
        console.log(res.data);
        if (res.data.success) {
         helperlistall[e.target.id][0].disabled = true;
         helperlistall[e.target.id][0].btn_helper = '已选择'
         that.setData({
           helperlist:helperlistall,
         })
          let pages = getCurrentPages();  // 当前页的数据，可以输出来看看有什么东西
          let prevPage = pages[pages.length - 2];  // 上一页的数据，也可以输出来看看有什么东西
          /** 设置数据 这里面的 value 是上一页你想被携带过去的数据，后面是本方法里你得到的数据，我这里是detail.value，根据自己实际情况设置 */
          prevPage.setData({
            helpId: e.target.id,
          })
          console.log(prevPage);
          /** 返回上一页 这个时候数据就传回去了 可以在上一页的onShow方法里把 value 输出来查看是否已经携带完成 */
         wx.navigateBack({
           delta:2
         });
         
        }
        console.log(that.data.helperlist);
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
   var that=this;
  that.setData({
    helperlist:that.data.helperlist,
  })
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