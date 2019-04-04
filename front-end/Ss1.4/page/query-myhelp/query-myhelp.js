// page/query-myhelp/query-myhelp.js
var app = getApp();
var util = require('../../util/util.js');

Page({

  /**
   * 页面的初始数据
   */
  data: {
    value: '',
    pageSize: 15,
    helpList: [],
    helpstatus:['','进行中','已完成','已失效'],
    statusindex:0,
    minDate: new Date().getTime(),
    maxDate: new Date(2019, 10, 1).getTime(),
    currentDate1:'',
    currentDate2:'',
    show1: false,
    show2:false,
    starttime: '',
    endtime: '',
    costnum:'',
    showcoin: [],
    showcomment: [],
    list2:[],
    targetTime:0,
    second:0,
  },
  choosetime1() {
    this.setData({ show1: true });
  },
  choosetime2() {
    this.setData({ show2: true });
  },
  /* 弹出组件取消 */
  onClose1() {
    this.setData({ show1: false });
  },
  onClose2() {
    this.setData({ show2: false });
  },
  /*时间选择器 */
  onInput1(e) {
    console.log(e.detail);
    this.setData({
      currentDate1: e.detail
    });
  },
  onInput2(e) {
    console.log(e.detail);
    this.setData({
      currentDate2: e.detail
    });
  },
  onConfirm1(e) {
    console.log(e);
    this.setData({
      show1: false,
      starttime: app.timeStamp2String(e.detail),
    })
  },
  onConfirm2(e) {
    console.log(e);
    this.setData({
      show2: false,
      endtime: app.timeStamp2String(e.detail),
    })
  },
 cancel1(){
   this.setData({
     show1:false,
     starttime:'',
   })
 },
  cancel2() {
    this.setData({
      show2: false,
      endtime: '',
    })
  },
  /*  接受triggerEvent 方法触发的自定义组件事件来更新同步数据 */
  getStatus: function (e) {
    var text=e.detail.text;
    var index;
    var helpstatus=this.data.helpstatus;
    for(var i=0;i<helpstatus.length;i++)
    {
       if(text==helpstatus[i]) index=i;
    }
    console.log(index)
    this.setData({
       statusindex:index,
    })
  },
  /*获取输入搜币值 */
  inputcost:function(e){
    console.log(this.data.costnum);
    console.log(e);
    this.setData({
      costnum:e.detail.value,
    })
    console.log(this.data.costnum);
  },
  /* 搜服务按钮功能 */
  handleClick: function () {
    var token = null;
    var that = this; 
    var List1 = [];
    var List2=[];
    var showcoin = [];
    var showcomment = [];
    if(that.data.starttime=='') that.data.starttime==null;
    if(that.data.statusindex==0) that.data.statusindex=null;
    if(that.data.endtime=='') that.data.endtime=null;
    if(that.data.costnum=='') that.data.costnum=null;
    console.log(that.data.starttime);
    console.log(that.data.endtime);
    console.log(that.data.costnum);
    console.log(that.data.statusindex);
    try {
      const value = wx.getStorageSync('token')
      if (value) {
        token = value;
      }
    } catch (e) {
      console.log("error");
    }
    wx.request({
      url: app.globalData.serviceUrl + "/SearchStreet/help/gethelplistbyuserid?pageIndex=" + 0 + "&pageSize=" + that.data.pageSize,
      data: {
        helpStatus:that.data.statusindex,
        startTime:that.data.starttime,
        endTime:that.data.endtime,
        souCoin:that.data.costnum,
      },
      method: 'GET',
      header: {
        'content-type': 'application/json',
        'token': token
      },
      success: function (res) {
        console.log(res.data);
        if (res.data.success) {
          console.log(res.data);
            List1 = res.data.helpList;
            console.log(List1.length);
            that.data.helpList=res.data.helpList;
            for(var i=0;i<List1.length;i++){
              if(List1[i].additionalCoin!=0) showcoin[i]=false;
              else  showcoin[i]=true;
              if(List1[i].attitude!=0&&List1[i].completion!=0&&List1[i].efficiency!=0) showcomment[i]=false;
               else showcomment[i]=true;
               var format = List1[i];
               format.endTime = util.formatDate1(List1[i].endTime);
               //console.log(format.endTime);
               List2.push(format);
              }
           that.setData({
             helpList:List2,
             showcoin: showcoin,
             showcomment: showcomment,
          })
        console.log(that.data.helpList);
           } 
          
           else  if (res.data.errMsg == "token为空" || res.data.errMsg == "token无效") {
              wx.redirectTo({
                url: '../../page/login/login'
              })
            }
          
        },
        fail: function (res) {
          console.log('submit fail');
        },
        complete: function (res) {
          console.log('submit complete');
        }
      })
    console.log(this.data.helpList);
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
   var that=this;
   this.setData({
     helpList:that.data.helpList
   })
   console.log(this.data.helpList);
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