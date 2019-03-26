// page/addservice/addservice.js
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    isadd:undefined,
   flag:true,
   serviceId:undefined,
   shopId:0,
    service: {
      "serviceContent": "",
      "serviceDesc": "",
      "serviceId": '',
      "serviceImgAddr": "",
      "serviceName": "",
      "servicePrice": '',
      "servicePriority": '',
      "shopId": ''
    },
    serviceName: '',
    servicePrice: '',
    servicePriority: '',
    serviceDesc: '',
    serviceContent: '',
    serviceImg: [],
    addormodify:'更新',
    disabletodelete:true,
    basePath :"D:\\projectdev\\image",
    //basePath :"\\home\\SearchStreet\\image",
    addUrl:app.globalData.serviceUrl + "/SearchStreet/service/addservice",
    modifyUrl:app.globalData.serviceUrl + "/SearchStreet/service/modifyservice",
    deleteUrl: app.globalData.serviceUrl + "/SearchStreet/service/deleteservice?serviceId=",
    errorMsgs: {
      name_error: '',
      price_error: '',
      priority_error: ''
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    //页面初始化，options为页面跳转所带来的参数
    that.setData({
      serviceId: options.serviceId,
      isadd:options.isadd,
      shopId:options.shopId
    });
    if(parseInt(options.isadd)==0)
    {
      that.setData(
        {
          disabletodelete:false
        }
      )
    }
    if (options.serviceId == 0) {
      that.setData({
        addormodify:'添加'
        })
      return;
    }
    wx.request({
      url: app.globalData.serviceUrl +'/SearchStreet/service/searchservicebyid?serviceId='+that.data.serviceId,
      data:{},
      method: 'GET',
      success:function (res) {
        var service = res.data.rows[0];
        console.log(service);
        if (service == undefined) {
          var toastText = "获取数据失败" ;
          wx.showModal({
            title: '警告',
            content: toastText,
            showCancel: false,
            success(res) {
              if (res.confirm) {
                //  console.log('用户点击确定')
                wx.navigateBack({      
                  url: '../service-list/service-list?shopId='+that.data.shopId
                })
              }
            }
          });
        }
        else { 
          var flag=false;
          if(service.serviceImgAddr==null)
          {
            flag=true;
            that.data.serviceImg=null;
           }
           else
           {
            that.data.serviceImg[0] = service.serviceImgAddr;
            console.log(that.data.serviceImg[0])
           }
          that.setData({
            shopId:service.shopId,
            serviceName: service.serviceName,
            servicePrice: service.servicePrice,
            servicePriority: service.servicePriority,
            serviceDesc: service.serviceDesc,
            serviceContent: service.serviceContent,
            serviceImg:that.data.serviceImg,
            flag:flag,
            service:service
          });
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

  },
  uploadimg: function () {
    var that = this;
    var flag = this.data.flag;
    wx.chooseImage({
      sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有  
      sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
      success: function (res) {
        that.setData({
          serviceImg: res.tempFilePaths,
          flag: !flag
        })
      }
    })
  },
changeimg: function () {
    var that = this;
    wx.chooseImage({
      sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有  
      sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
      success: function (res) {
        that.setData({
          serviceImg: res.tempFilePaths,
        })
      }
    })
  },
  formSubmit: function (e) {
    var that = this;
    var abletosubmit=true;
    var error=1;
    if (e.detail.value.serviceName.length == 0) {
      this.setData({
        errorMsgs: {
          name_error: '请输入服务名称'
        }
      })
      abletosubmit=false;
    } 
    else if (e.detail.value.servicePrice.length == 0) {
      this.setData({
        errorMsgs: {
          price_error: '请输入服务价格'
        }
      })
      abletosubmit=false;
      error=2;
    } 
    else if (e.detail.value.servicePriority.length == 0) {
      this.setData({
        errorMsgs: {
          priority_error: '请输入优先级'
        }
      })
      abletosubmit=false;
      error=3;
    }
    if(abletosubmit==false)
    {
      var toastText =that.data.errorMsgs.name_error; 
      if(error==2)
      toastText=that.data.errorMsgs.price_error;
      else if(error==3)
      toastText=that.data.errorMsgs.priority_error;
      wx.showModal({
        title: '提示',
        content: toastText,
        showCancel:false,
        success(res) {
          if (res.confirm) {
          //  console.log('用户点击确定')
          } 
        }
      });
    }
    else {
      
      /*
      var token = null;
      try {
        const value = wx.getStorageSync('token')
        if (value) {
          token = value;
        }
      } catch (e) {
        console.log("error");
      }*/
      
      that.data.serviceName=e.detail.value.serviceName;
      that.data.servicePrice=e.detail.value.servicePrice;
      that.data.servicePriority= e.detail.value.servicePriority;
      that.data.serviceDesc=e.detail.value.serviceDesc;
      that.data.serviceContent=e.detail.value.serviceContent;
      that.data.service.shopId=that.data.shopId;
      that.data.service.serviceName=that.data.serviceName;
      that.data.service.servicePrice=that.data.servicePrice;
      that.data.service.servicePriority=that.data.servicePriority;
      that.data.service.serviceDesc=that.data.serviceDesc;
      that.data.service.serviceContent=that.data.serviceContent;
      if(that.data.serviceImg!=null)
      that.data.service.serviceImgAddr=that.data.serviceImg[0];
      

      that.setData({
      
      });
      var url='';
      if(parseInt(that.data.isadd)==1)
      {
        url=that.data.addUrl;
      }
      else
      {
        url=that.data.modifyUrl;
        that.data.service.serviceId=that.data.serviceId;
      }
      wx.request({
        url: url,
        data: JSON.stringify(that.data.service),
        method: 'POST',
        header: {
          "Content-Type": 'application/json'
        },
        success: res => {
          console.log(res);
          if (res.data.success) {
            wx.setStorage({
              key: 'serviceId',
              data: res.data.serviceId
            })
            if (that.data.serviceImg != null && that.data.serviceImg[0] != undefined  )
           {
              var date = new Date();
            var url = app.globalData.serviceUrl + "/SearchStreet/service/uploadimg?serviceId=" + res.data.serviceId + "&createTime=" + app.timeStamp2String(date);
            app.uploadAImg({
              url: url,
              filePath: that.data.serviceImg[0],
              fileName: "serviceImg"
            })
            }
            wx.navigateBack({
              url: '../service-list/service-list?shopId='+that.data.shopId
            })
          } 
        }
      })
    }
  },
   formReset: function (e) {
    var that = this;
      var url=that.data.deleteUrl;
      wx.request({
        url: url+that.data.serviceId,
        method: 'POST',
        success: res => {
          console.log(res);
          if (res.data.success) { 
            wx.redirectTo({
              url: '../personal-center/personal-center'
            })
          } 
        }
      })
  }
})