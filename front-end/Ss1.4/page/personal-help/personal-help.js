Page({

  /**
   * 页面的初始数据
   */
  data: {
    current:'tab1',
    ifName:false,
    reward:null,
    list1:[{shelpTitle:'帮忙领票',timeLimit:50,shelpCost:80}],
    list2: [{ shelpTitle: '帮忙领票', timeLimit: 50, shelpCost: 80 }],
    list3: [{ shelpTitle: '帮忙领票', timeLimit: 50, shelpCost: 80 }],
    currentTab:1
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
    var List1,List2,List3
    wx.request({
      url: '',             //请求我的求助订单信息
      data:{

      },
      method:"POST",
      success:function(res){             //根据求助信息的状态设置显示
        console.log(res);
        var List=res.data.appealList;
        var j=0,m=0,k=0;
        for(var i=0;i<List.length;i++){          
          if(List[i].appealStatus==0||List[i].appealStatus==1){
            List1[j++]=List[i];
          }
          else if(List[i].appealStatus==2) List2[m++]=List[i];
          else List3[k++]
        }
        this.setData({
         list1:List1,
         list2:List2,
         list3:List3
        })
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