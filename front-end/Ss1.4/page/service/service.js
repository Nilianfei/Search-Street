// page/service/service.js
var app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    f:false,                //判断要不要执行onShow()里的方法
    visible:false,
    service:null,
    bottonText:"立即预约",
    token:null,
    serviceId:null,
    imgUrl: app.globalData.imgUrl ,
    order: {
      "createTime": "",
      "orderId": 0,
      "orderPrice": 0,
      "orderStatus": 0,
      "overTime": "",
      "serviceCount": 1,
      "serviceId": 0,
      "serviceName": "string",
      "userId": 0
    },
    shopId:null,
    userId:null,
    value1: 1,
    serviceCount:1,
    // input默认是1 
    // 使用data数据对象设置样式名  
    minusStatus: 'disabled' ,
    totalprice:0 ,
    flag:false,
    serviceImgShow:"",

  },
  handleOpen() {
    this.setData({
      visible: true
    });
  },
  handleClose() {
    this.setData({
      visible: false
    });
    wx.navigateBack({
      url: '../user-service-list/user-service-list',//返回服务列表页面
    })
  },
  /* 点击减号 */
  bindMinus: function () {
    var num = this.data.serviceCount;
    // 如果大于1时，才可以减  
    if (num > 1) {
      num--;
    }
    var servicePrice = Number(this.data.service.servicePrice) * num;
    // 只有大于一件的时候，才能normal状态，否则disable状态  
    var minusStatus = num <= 1 ? 'disabled' : 'normal';
    // 将数值与状态写回  
    this.setData({
      serviceCount: num,
      minusStatus: minusStatus,
      totalprice:servicePrice
    });
  },
  /* 点击加号 */
  bindPlus: function () {
    var num = this.data.serviceCount;
    // 不作过多考虑自增1  
    num++;
    // 只有大于一件的时候，才能normal状态，否则disable状态  
    var minusStatus = num < 1 ? 'disabled' : 'normal';
    var servicePrice=Number(this.data.service.servicePrice)*num;
    // 将数值与状态写回  
    this.setData({
      serviceCount: num,
      minusStatus: minusStatus,
      totalprice: servicePrice
    });
  },
  /* 输入框事件 */
  bindManual: function (e) {
    var num = e.detail.value;
    var servicePrice = Number(this.data.service.servicePrice) * num;
    // 将数值与状态写回  
    this.setData({
      serviceCount: num,
      totalprice: servicePrice
    });
  } ,
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that= this;
    var service = JSON.parse(options.service);
    console.log(options);
    that.setData({
      service:service,
      shopId: service.shopId,
      serviceId: service.serviceId,
      totalprice:service.servicePrice
    })

    if(service.serviceImgAddr)
    {
      that.setData({
        serviceImgShow:that.data.imgUrl+that.data.service.serviceImgAddr,
      })
    }
    else{
      that.setData({
        serviceImgShow:"/images/nophoto.png",
      })
    }
    try {//同步获取与用户信息有关的缓存token
      const value = wx.getStorageSync('token');
      const userId=wx.getStorageSync('userId');
      if (value) {
        that.setData({
          token:value
        })
      }
      if (userId) {
        that.setData({
          userId: userId
        })
      }
      else {
        wx.request({
          url: app.globalData.serviceUrl + '/SearchStreet/wechat/getUserInfo',
          header: {
            token: token,
          },
          success: function (res) {
            // 拿到自己后台传过来的数据，自己作处理
            console.log(res.data);
            if (null != res.data.success && res.data.success) {
              //用户登录成功
              wx.setStorage({
                key: 'userId',
                data: res.data.personInfo.userId
              });
              that.setData(
                {
                  userId: res.data.personInfo.userId
                }
              )
            }
          }
          , fail: function (err) {
            console.log(err)
          }
        })
      }
    } catch (e) {
      console.log("error");
    }
    //查询用户是否预约过服务
    wx.request({
      url: app.globalData.serviceUrl + '/SearchStreet/order/getOrderlistbyus?serviceId=' + that.data.serviceId+"&userId="+that.data.userId,
      data: {},
      method: "GET",
      success: res => {
        console.log(res);
        if(res.data.isbook){
          that.setData({
            bottonText:"已预约",
            order:res.data.order,
            flag:true
          })
        }
      }
    })
    that.setData({
      f:true
    })
    console.log("onLoad")
  },
  handleChange1({ detail }) {
    this.setData({
      value1: detail.value
    })
  },
 checkorder:function(e)
 {
   //查看现在正在进行的订单
      var that=this;
      var service =that.data.service;
      var order=that.data.order;
      wx.navigateTo({
        url: '../order-detail/order-detail?service=' + JSON.stringify(service) + '&order=' + JSON.stringify(order),
      })
 },
  booking:function(e){
    var that = this;
    if(that.data.bottonText!="已预约"){
      that.data.order.serviceId=that.data.serviceId;
     // that.data.order.createTime = app.timeStamp2String(new Date());
      that.data.order.serviceName=that.data.service.serviceName;
      that.data.order.orderPrice=that.data.totalprice;
      that.data.order.userId=that.data.userId;
      that.data.order.serviceCount = that.data.serviceCount;
      console.log(that.data.order.serviceCount);
      wx.request({
        url: app.globalData.serviceUrl + '/SearchStreet/order/addOrder',
        data: JSON.stringify(that.data.order),
        method: 'POST',
        header: {
          "Content-Type": 'application/json'
        },
        success: function (res) {
          that.handleOpen() 
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
    var that = this;
     //查询用户是否预约过服务
     wx.request({
       url: app.globalData.serviceUrl + '/SearchStreet/order/getOrderlistbyus?serviceId=' + that.data.serviceId + "&userId=" + that.data.userId,
       data: {},
       method: "GET",
       success: res => {
         console.log(res);
         if (res.data.isbook) {
           that.setData({
             bottonText: "已预约",
             order: res.data.order,
             flag: true
           })
         }
         else {
           that.setData({
             bottonText: "立即预约",
             flag: false
           })
         }
       }
     })
     console.log("onShow")
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