var app=getApp();
var util=require('../../util/util.js');
function countdown(that) {
  that.setData({
    targetTime: util.formatTime(that.data.second),
  })
  var second = that.data.second;
 // console.log(second);
  if (second == 0) {

    return;
  }
  var time = setTimeout(function () {
    that.data.second -= 1;
    countdown(that);
  }
    , 1000)
}

Page({

  /**
   * 页面的初始数据
   */
  data: {
    current:'tab1',
    ifName:false,
    reward:null,
    token:null,
    current1: 1,
    pageNum: 0,
    pageSize: 15,
    list:[],
    currentTab:1,
    currentTab1:1,
    phelptime:[],
    btnhover:false,
    targetTime:0,
    second:0,
    id:0,
    ifadditioncoin:false,
  },

 /* 根据导航栏的选择设置目前的key值 */
  handleChange({ detail }) {
    this.setData({
      current: detail.key
    });
  },

  /* 根据导航栏的选择显示目前状态下的求助信息列表 */
  switchTab(e) {
    this.setData({ 
      currentTab: e.currentTarget.dataset.current 
      });
      //console.log(this.data.currentTab);
  },
  switchTab1(e) {
    this.setData({
      currentTab1: e.currentTarget.dataset.current
    });
    //console.log(this.data.currentTab1);
  },

  /* 点击跳到求助详情页 */
  toDetails(e){
    console.log(e.currentTarget.id);
   wx.navigateTo({
     url: '../myhelp-details/myhelp-details?id='+e.currentTarget.id,
   })
  },
/* 点击确认已被帮助按钮功能 */
finishedHelp:function(e){
  var detail=this.data.list;
  this.setData({
    btnhover:true,
    list:detail,                 //此处逻辑有错
  }) 
  var token = null;
  try {
    const value = wx.getStorageSync('token')
    if (value) {
      token = value;
    }
  } catch (e) {
    console.log("error");
  }
  wx.request({
    url: app.globalData.serviceUrl+"/SearchStreet/appeal/competeappeal?token="+token,
    data:{
    appealId:e.target.id,
    helpId:1
    },
    method:'GET',
    success(res){
      console.log(res.data);
    }
  })
},

/*点击帮助无效按钮功能 */
unfinishedHelp:function(e){
  this.setData({
    btnhover1: true,
  }) 
  var token = null;
  try {
    const value = wx.getStorageSync('token')
    if (value) {
      token = value;
    }
  } catch (e) {
    console.log("error");
  }
  wx.request({
    url: app.globalData.serviceUrl + "/SearchStreet/appeal/?token=" + token,       /*完整后台url，将此状态变为已完成（不会将搜币打到对方账户），并告知给提供服务的人 */
    data: {
      appealId: e.target.id,
      helpId: 1
    },
    method: 'GET',
    success(res) {
      console.log(res.data);
    }
  })
},
/* 点击撤销按钮功能 */
withdrawHelp:function(e){
  wx.request({
    url: app.globalData.serviceUrl+"/SeacrhStreet/appeal/",          /*根据求助ID查看是否有人对此求助提供了帮助，若无人接单，才可撤销，将其变为失效单 */  
    data:{
      appealId:e.target.id
    },
    method:'GET',
   success(res){
     console.log(res.data);
   }
  })
},

/* 点击修改按钮功能 */
modifyHelp:function(e){
  wx.navigateTo({
    url: '../modify-myhelp/modify-myhelp',
  })
},
/*根据追加打赏按钮设置打赏搜币页面的显示 */
  inputReward:function(e){
   this.setData({
     ifName:true,
     id:e.target.id
   })
  },
  
  /*追加打赏弹框的取消按钮功能*/
  cancel:function(){
    this.setData({
      ifName:false
    })
  },

/*获取input中的输入值 */
  setValue:function(e){
    console.log(e.detail);
    this.setData({
      reward:e.detail.value,
    })
    console.log(this.data.reward);
  },

/* 追加打赏弹框的提交按钮功能 */
confirm:function(){
  var that=this;
  console.log(this.data.reward);
  if(this.data.reward==null){
    wx.showToast({
      title: '请填写打赏金额',
      icon:'none'
    })
  }
  else{
    var token = null;
    try {
      const value = wx.getStorageSync('token')
      if (value) {
        token = value;
      }
    } catch (e) {
      console.log("error");
    }
    wx.request({
      url: app.globalData.serviceUrl + "/SearchStreet/help/additionsoucoin?token=" + token,         /* 将打赏金额的数值传给后台 */
      data:{
        additionSouCoin:parseInt(this.data.reward),
        appealId:this.data.id,
        helpId:1,
      },
      success(res){
        console.log(res)
        that.setData({
          ifName:false
        })
      }
    })
  }
},
  showSoucoin:function(){
    this.setData({
      ifadditioncoin:true,
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    //console.log(options);
    var phelptime=[];
    var that = this ;      
    var token = null;
    try {
      const value = wx.getStorageSync('token')
      if (value) {
        token = value;
      }
    } catch (e) {
      console.log("error");
    }
    if(that.data.current=='tab1')
    {
      wx.request({
      url: app.globalData.serviceUrl + "/SearchStreet/appeal/getappeallistbyuserid?token=" + token +"&pageIndex="+0+"&pageSize="+that.data.pageSize,
      data:{

      },
      method: 'POST',
      success: function (res) {
        console.log(res.data);
        if (res.data.success) {
          console.log(res.data);
          var List = res.data.appealList;
          for(var i=0;i<List.length;i++){
            phelptime.push(Math.round((List[i].endTime-List[i].startTime)/1000/60));
          }
          if (List[0].endTime - new Date().getTime() <= 0) that.data.second = 3600 * 24;      //此处有错误
          else that.data.second = Math.floor((List[0].endTime - new Date().getTime()) / 1000);
          console.log(that.data.second);
          countdown(that);
         console.log(that.data.second);
          that.setData({
          list: List,
          phelptime:phelptime,
        })
        console.log(that.data.list);
         //var detail = that.data.list;
          //detail[0].appealStatus = 2;
          //that.setData({
          //  list:detail
         // }) 
      } else {
          if (res.data.errMsg == "token为空" || res.data.errMsg == "token无效") {
            wx.redirectTo({
              url: '../../page/login/login'
            })
          }
        }
      },
      fail: function (res) {
        console.log('submit fail');
      },
      complete: function (res) {
        console.log('submit complete');
      }
    })
    }
    else if(that.data.current=='tab2'){
      wx.request({
        url: app.globalData.serviceUrl + "/SearchStreet/help/gethelplistbyuserid?token=" + token + "&pageIndex=" + 0 + "&pageSize=" + that.data.pageSize,
        data: {
          
        },
        method: 'POST',
        success: function (res) {
          console.log(res.data);
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