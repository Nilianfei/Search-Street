var app=getApp();
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
    phelptime:[]
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
  },

  /* 点击跳到求助详情页 */
  toDetails(e){
    console.log(e.currentTarget.id);
   wx.navigateTo({
     url: '../myhelp-details/myhelp-details?id='+e.currentTarget.id,
   })
  },

/*根据追加打赏按钮设置打赏搜币页面的显示 */
  inputReward:function(){
   this.setData({
     ifName:true
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
      reward:e.detail,
    })
  },

/* 追加打赏弹框的提交按钮功能 */
confirm:function(){
  if(this.data.reward==null){
    wx.showToast({
      title: '请填写打赏金额',
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
      url: app.globalData.serviceUrl + "/SearchStreet/appeal/additionsoucoin?token=" + token,         /* 将打赏金额的数值传给后台，后台据此金额改变被打赏人钱包中的搜币 */
      data:{
        additionSouCoin:this.data.reward,
      },
      success(res){
        console.log(res.data)
        this.setData({
          ifName:false
        })
      }
    })
  }
},
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
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
          that.setData({
          list: List,
          phelptime:phelptime,
         // list2: List2, 
          //list3: List3	         
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