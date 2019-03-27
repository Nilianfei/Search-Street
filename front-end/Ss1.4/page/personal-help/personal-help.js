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
    list2:[],
    currentTab:0,
    currentTab1:1,
    phelptime:[],
    phelptime1:[],
    btnhover:false,
    targetTime:0,
    second:0,
    id:0,
    ifadditioncoin:false,
    ifcomment:false,
    showcoin:[],
    showcomment:[],
    ifmycomment:false,
    helplist:[],
    helpId:0,
    disabled:[],
    disabled1:[],
    appealindex:0,
    starCheckedImgUrl: "../../images/黄色星星.png",
    starUnCheckedImgUrl: "../../images/灰色星星.png",
    List1:[],
    soucoin:[],
    // 建议内容
    opinion: "",

    starMap: [
      '非常差',
      '差',
      '一般',
      '好',
      '非常好',
    ],

    evaluations: [
      {
        id: 0,
        name: "完成度",
        star: 0,
        note: ""
      },
      {
        id: 1,
        name: "帮忙效率",
        star: 0,
        note: ""
      },
      {
        id: 2,
        name: "帮忙态度",
        star: 0,
        note: ""
      }
    ],
  note1:[],
  note2:[],
  note3:[],
  },

 /* 根据导航栏的选择设置目前的key值 */
  handleChange({ detail }) {
    var phelptime = [];
    var that = this;
    var token = null;
    var disabled=[];
    var disabled1=[];
    try {
      const value = wx.getStorageSync('token')
      if (value) {
        token = value;
        that.data.token = value;
      }
    } catch (e) {
      console.log("error");
    }
    this.setData({
      current: detail.key
    });
    if (that.data.current == 'tab2') {
      wx.request({
        url: app.globalData.serviceUrl + "/SearchStreet/appeal/getappeallistbyuserid?token=" + token + "&pageIndex=" + 0 + "&pageSize=" + that.data.pageSize,
        data: {

        },
        method: 'POST',
        success: function (res) {
          console.log(res.data);
          if (res.data.success) {
            console.log(res.data);
            var List = res.data.appealList;
            for (var i = 0; i < List.length; i++) {
              var format = List[i];
              format.endTime = util.formatDate1(List[i].endTime);
              List[i]=format;
            }
            for(var i=0;i<List.length;i++){
              disabled[i]=false;
              disabled1[i]=false;
            }
            that.setData({
              list: List,
              disabled:disabled,
            })
            console.log(that.data.list);
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
  },

  /* 根据导航栏的选择显示目前状态下的求助信息列表 */
  switchTab(e) {
    var that=this;
    this.setData({ 
      currentTab: e.currentTarget.dataset.current ,
      list2:that.data.list2,
      List1:that.data.List1,
      phelptime:that.data.phelptime,
      });
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
  var that=this;
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
    helpId:2
    },
    method:'GET',
    success(res){
      console.log(res.data);
      if(res.data.success){
        that.setData({
          btnhover: true,
        }) 
      }
    }
  })
},

/*点击帮助无效按钮功能 */
unfinishedHelp:function(e){
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
    url: app.globalData.serviceUrl + "/SearchStreet/appeal/disableappeal?token=" + token,       /*完整后台url，将此状态变为已完成（不会将搜币打到对方账户），并告知给提供服务的人 */
    data: {
      appealId: e.target.id,
      //helpId: 1
    },
    method: 'GET',
    success(res) {
      console.log(res.data);
      if(res.data.success){
        this.setData({
          btnhover1: true,
        }) 
      }
    }
  })
},
/* 点击撤销按钮功能 */
withdrawHelp:function(e){
  wx.request({
    url: app.globalData.serviceUrl + "/SeacrhStreet/appeal/cancelappeal?token=" + token,          /*根据求助ID查看是否有人对此求助提供了帮助，若无人接单，才可撤销，将其变为失效单 */  
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
  console.log(e);
  wx.navigateTo({
    url: '../modify-myhelp/modify-myhelp?id='+e.target.id,
  })
},

/*根据给好人评按钮设置评论页面的显示 */
inputComment:function(e){
    console.log(e);
    var that = this;
    this.setData({
      ifcomment: true,
      id: e.target.id,
      appealindex: e.currentTarget.dataset.id,
    })
    wx.request({
      url: app.globalData.serviceUrl + "/SearchStreet/help/gethelplistbyappealid?appealId=" + that.data.id + "&pageIndex=" + 0 + "&pageSize=" + that.data.pageSize,
      data: {

      },
      method: 'GET',
      success(res) {
        console.log(res.data);
        for (var i = 0; i < res.data.helpList.length; i++) {
          if (res.data.helpList[i].helpStatus == 2) {
            that.data.helpId = res.data.helpList[i].helpId;
            console.log(that.data.helpId);
          }
        }
      }
    })
  },

  /**
   * 评分
   */
  chooseStar: function (e) {
    const index = e.currentTarget.dataset.index;
    const star = e.target.dataset.star;
    let evaluations = this.data.evaluations;
    let evaluation = evaluations[index];
    // console.log(evaluation)
    evaluation.star = star;
    evaluation.note = this.data.starMap[star - 1];
    this.setData({
      evaluations: evaluations
    })
    console.log(this.data.evaluations);
  },

/* 点击确认评价按钮的功能 */
  confirmcomment:function(){
    var that = this;
    var disabled = that.data.disabled1;
    if (that.data.evaluations[0].star == 0) {
      wx.showModal({
        title: '提示',
        content: '请为此次帮助的完成度打分',
      })
    } else if (that.data.evaluations[1].star == 0) {
      wx.showModal({
        title: '提示',
        content: '请为此次帮助的效率打分',
      })
    } else if (that.data.evaluations[2].star == 0) {
      wx.showModal({
        title: '提示',
        content: '请为此次帮助的态度打分',
      })
    } else {
      var token = null;
      try {
        const value = wx.getStorageSync('token')
        if (value) {
          token = value;
          console.log(token);
        }
      } catch (e) {
        console.log("error");
      }
      wx.request({
        url: app.globalData.serviceUrl + "/SearchStreet/help/commenthelp?token=" + token +'&helpId='+that.data.helpId,
        data: {
          completion: that.data.evaluations[0].star,
          efficiency: that.data.evaluations[1].star,
          attitude: that.data.evaluations[2].star,
        },
        method: "GET",
        success: res => {
          console.log(res);
          if (res.data.success) {
            disabled[that.data.appealindex] = true;
             that.setData({
               ifcomment:false,
               disabled1:disabled,
             })
          } else {
            if (res.data.errMsg == "token为空" || res.data.errMsg == "token无效") {
              wx.redirectTo({
                url: '../../page/login/login'
              })
            }
          }
        }
      })
    }
  },
      
/*根据追加打赏按钮设置打赏搜币页面的显示 */
  inputReward:function(e){
    console.log(e);
    var that = this;
    this.setData({
      ifName: true,
      id: e.target.id,
      appealindex:e.currentTarget.dataset.id,
    })
    wx.request({
      url: app.globalData.serviceUrl + "/SearchStreet/help/gethelplistbyappealid?appealId=" + that.data.id + "&pageIndex=" + 0 + "&pageSize=" + that.data.pageSize,
      data: {

      },
      method: 'GET',
      success(res) {
        console.log(res.data);
        for (var i = 0; i < res.data.helpList.length; i++) {
          if (res.data.helpList[i].helpStatus == 2) {
            that.data.helpId = res.data.helpList[i].helpId;
            console.log(that.data.helpId);
          }
        }
      }
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
confirm:function(e){
  console.log(e);
  var that=this;
  var disabled=that.data.disabled;
  console.log(disabled);
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
    console.log(that.data.helpId);
    wx.request({
      url: app.globalData.serviceUrl + "/SearchStreet/help/additionsoucoin?token=" + token,         /* 将打赏金额的数值传给后台 */
      data:{
        additionSouCoin:parseInt(this.data.reward),
        appealId:this.data.id,
        helpId:that.data.helpId,
      },
      success(res){
        console.log(res);
        console.log(that.data.appealindex);
        if(res.data.success){
          disabled[that.data.appealindex]=true;
        that.setData({
          ifName:false,
          disabled:disabled,
        })
        }
      }
    })
  }
},
/* 有追赏页面显示 */
  showSoucoin:function(e){
    console.log(e);
    this.setData({
      ifadditioncoin:true,
    })
    console.log(this.data.ifadditioncoin);
  },
  /*查看评价页面显示 */
  showMycomment:function(e){
    var note1=[];
    var note2=[];
    var note3=[];
    console.log(e);
    for(var i=0;i<this.data.List1[e.currentTarget.dataset.id].completion;i++){
      note1.push(i);
    }
    for (var i = 0; i < this.data.List1[e.currentTarget.dataset.id].efficiency;i++){
      note2.push(i);
    }
    for (var i = 0; i < this.data.List1[e.currentTarget.dataset.id].attitude;i++){
      note3.push(i);
    }
    this.setData({
      ifmycomment:true,
      note1:note1,
      note2:note2,
      note3:note3,
    })
  },
  /* 取消查看按钮功能 */
  cancelcomment:function(){
    this.setData({
      ifmycomment:false
    })
  },
  addsouCoin:function(){
      /* 根据token将本次帮助中被帮助人打赏的搜币存入本人账户 */
     /* wx.request({
        url: app.globalData.serviceUrl + "/SearchStreet/help/getappeallistbyuserid?token=" + this.data.token,   //此处url有误
        data:{
          additionCoin:helpList.additionCoin
        },
        method:'GET',
        success(res){
          console.log(res.data);
        }
      }) */
      wx.showToast({
        title: '已成功存入您的钱包',
        icon:'none',
      })
      this.setData({
        ifadditioncoin:false,
        showcoin:true,
      })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    //console.log(options);
    var phelptime1=[];
    var that = this ;    
    var token = null;
    var list3=[];
    var List1=[];
    var showcoin=[];
    var showcomment=[];
    console.log(that.data.list);
    try {
      const value = wx.getStorageSync('token')
      if (value) {
        token = value;
        that.data.token=value;
      }
    } catch (e) {
      console.log("error");
    }
    if(that.data.current=='tab1'){
      wx.request({
        url: app.globalData.serviceUrl + "/SearchStreet/help/gethelplistbyuserid?token=" + token + "&pageIndex=" + 0 + "&pageSize=" + that.data.pageSize,
        data: {
        
        },
        method: 'GET',
        success: function (res) {
          console.log(res.data);
          if (res.data.success) {
            console.log(res.data);
            List1 = res.data.helpList;
            for(var  i=0;i<List1.length;i++){
              if(List1[i].additionalCoin!=0) showcoin[i]=false;
              else  showcoin[i]=true;
              if(List1[i].attitude!=0&&List1[i].completion!=0&&List1[i].efficiency!=0) showcomment[i]=false;
               else showcomment[i]=true;
            }
            for (var i = 0; i < List1.length; i++) {
              wx.request({
                url: app.globalData.serviceUrl + "/SearchStreet/appeal/getappealbyid?appealId=" + List1[i].appealId,
                data: {

                },
                method: 'GET',
                success(res) {
                  console.log(res.data);
                  if(res.data.success){
                  if (res.data.appeal.endTime+3600*24*1000 - new Date().getTime() <= 0) that.data.targetTime=0;      //此处有错误               
                  else {
                  that.data.second = Math.floor((res.data.appeal.endTime+3600*24*1000 - new Date().getTime()) / 1000);
                  console.log(that.data.second);
                  countdown(that);
                  }
                  console.log(that.data.second);
                    var format = res.data.appeal;
                    format.endTime = util.formatDate1(res.data.appeal.endTime);
                    console.log(format.endTime);
                    that.data.list2.push(format);
                }
                }
              })
            }
            //console.log(that.data.phelptime1)
            that.setData({
              List1:List1,
              showcoin:showcoin,
              showcomment:showcomment,
            })
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
    }
    this.setData({
      list2: that.data.list2,
      showcoin:that.data.showcoin,
      showcomment:that.data.showcomment,
      //phelptime1:that.data.phelptime1,
    })
    console.log(this.data.list2);
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