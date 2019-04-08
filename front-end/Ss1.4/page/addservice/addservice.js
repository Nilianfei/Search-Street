// page/addservice/addservice.js
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    token:"",
    isadd: undefined,
    flag: false,
    serviceId: undefined,
    addOrModify: "添加服务",
    cancelOrDelete: "取消",
    isDelete: "",
    img:null,
    shopId: 0,
    service: {
      "serviceContent": "",
      "serviceDesc": "",
      "serviceId": null,
      "serviceImgAddr": "",
      "serviceName": "",
      "servicePrice": '',
      "servicePriority": '',
      "shopId": null,
    },
    imgUrl: app.globalData.imgUrl,
    serviceImg: null,
    addormodify: '更新',
    disabletodelete: true,
    //basePath :"D:\\projectdev\\image",
    //basePath :"\\home\\SearchStreet\\image",
    addUrl: app.globalData.serviceUrl + "/SearchStreet/service/addservice",
    modifyUrl: app.globalData.serviceUrl + "/SearchStreet/service/modifyservice",
    deleteUrl: app.globalData.serviceUrl + "/SearchStreet/service/deleteservice?serviceId=",
    errorMsgs: {
      name_error: '',
      price_error: '',
      priority_error: ''
    },
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    var that = this;

    try {
      const value = wx.getStorageSync('token')
      if (value) {
        that.setData({
          token: value,
        })
      }
    } catch (e) {
      console.log("error");
    }

    var point="service.shopId"
    //页面初始化，options为页面跳转所带来的参数
    that.setData({
      serviceId: options.serviceId,
      isadd: options.isadd,
      [point]: options.shopId
    });
    console.log(that.data.shopId + " " + that.data.serviceId);
    if (parseInt(options.isadd) == 0) {
      that.setData({
        addOrModify: "保存更改",
        cancelOrDelete: "删除",
        isDelete: "error",
      })
    }

    if (options.serviceId == 0) {
      //var p ="serviceImg"
      that.setData({
        serviceImg: '../../images/add-photo.png',
      })
      return; //serviceId==0即是添加服务的情况，原表单内指为空，不许要get
    }
    else{
      wx.request({
        url: app.globalData.serviceUrl + '/SearchStreet/service/searchservicebyid?serviceId=' + that.data.serviceId,
        
        data: {},
        method: 'GET',
        success: function(res) {
          console.log(res);
          var service = res.data.rows[0];
          console.log(service);
          if (service == undefined) {
            var toastText = "获取数据失败";
            wx.showModal({
              title: '警告',
              content: toastText,
              showCancel: false,
              success(res) {
                if (res.confirm) {
                  //  console.log('用户点击确定')
                  wx.navigateBack({
                    url: '../service-list/service-list?shopId=' + that.data.shopId
                  })
                }
              }
            });
          } else {
            //var flag = false; //确定该服务有无图片信息的flag，flag=true表示没有，反之则有。
            if (service.serviceImgAddr == null) {
             // flag = true;
            } else {
              //var p="serviceImg";
              var serviceImg=[];
              serviceImg[0]=that.data.imgUrl+service.serviceImgAddr;
              that.setData({
                serviceImg: serviceImg,
                img:service.serviceImgAddr
              })
              console.log(that.data.serviceImg)
            }
            that.setData({
              //flag: flag,
              service: service
            });
          }


        }
      })
    }
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function() {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function() {

  },
  uploadimg: function() {
    var that = this;
   // var flag = this.data.flag;
    //var p="serviceImg";
    wx.chooseImage({
      sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有  
      sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
      success: function(res) {
        that.setData({
          serviceImg: res.tempFilePaths,
           flag: true
        });
        console.log(that.data.serviceImg);
      }
    })
  },
  changeimg: function() {
    var that = this;
    //var p = "serviceImg";
    wx.chooseImage({
      sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有  
      sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
      success: function(res) {
        that.setData({
          serviceImg: res.tempFilePaths,
          flag:true     //图片存在的情况下，有修改过图片
        })
      }
    })
  },

  inputServiceName: function(e) {
    var p = "service.serviceName";
    this.setData({
      [p]: e.detail.detail.value,
    })
    console.log(this.data.service.serviceName);
  },

  inputServicePrice: function(e) {
    var p = "service.servicePrice";
    this.setData({
      [p]: e.detail.detail.value,
    })
  },

  inputServicePriority: function(e) {
    var p = "service.servicePriority";
    this.setData({
      [p]: e.detail.detail.value,
    })
  },

  inputServiceDesc: function(e) {
    var p = "service.serviceDesc";
    this.setData({
      [p]: e.detail.detail.value,
    })
  },

  inputServiceContent: function(e) {
    var p = "service.serviceContent";
    this.setData({
      [p]: e.detail.detail.value,
    })
  },

  addOrModifyService: function(e) {
    var that = this;
    var ableToSubmit = true;
    var error = 1;
    if (that.data.service.serviceName.length == 0) {
      this.setData({
        errorMsgs: {
          name_error: '请输入服务名称'
        }
      })
      ableToSubmit = false;
    } else if (that.data.service.servicePrice.length == 0) {
      that.setData({
        errorMsgs: {
          price_error: '请输入服务价格'
        }
      })
      ableToSubmit = false;
      error = 2;
    } else if (that.data.service.servicePriority.length == 0) {
      that.setData({
        errorMsgs: {
          priority_error: '请输入优先级'
        }
      })
      ableToSubmit = false;
      error = 3;
    }
    if (ableToSubmit == false) {
      var toastText = that.data.errorMsgs.name_error;
      if (error == 2)
        toastText = that.data.errorMsgs.price_error;
      else if (error == 3)
        toastText = that.data.errorMsgs.priority_error;
      wx.showModal({
        title: '提示',
        content: toastText,
        showCancel: false,
        success(res) {
          if (res.confirm) {
            //  console.log('用户点击确定')
          }
        }
      });
    } else {

      
      if (that.data.serviceImg != '../../images/add-photo.png') {
        var imgAddr = "service.serviceImgAddr";
        var addr="";
        if(that.data.flag)
        {
          //修改过图片或者上传过图片
          addr=that.data.serviceImg[0];
          that.setData({
            [imgAddr]: addr,
          })
        }
        else
          addr=that.data.img;
        console.log(that.data.serviceImg[0]); //
        console.log(that.data.service.serviceImgAddr);
      }
      else{
        var imgAddr="service.serviceImgAddr" ;
        that.setData({
          [imgAddr]: null,
        })
        console.log(that.data.serviceImg);
        console.log(that.data.service.serviceImgAddr);
      }
      

      var url = '';
      if (parseInt(that.data.isadd) == 1) {
        url = that.data.addUrl;
      } else {
        url = that.data.modifyUrl;
        that.data.service.serviceId = that.data.serviceId;
      }
      console.log(that.data.service);
      wx.request({
        url: url,
        data: JSON.stringify(that.data.service),
        header: {
          token: that.data.token,
        },
        /*
        data: {

          "serviceContent": that.data.service.serviceContent,
          "serviceDesc": that.data.service.serviceDesc,
          "serviceId": that.data.serviceId,
          "serviceImgAddr": that.data.service.serviceImgAddr,
          "serviceName": that.data.service.serviceName,
          "servicePrice": that.data.service.servicePrice,
          "servicePriority": that.data.service.servicePriority,
          "shopId": that.data.shopId,
        },
        */
        method: 'POST',

        success: res => {
          console.log(res);
          if (res.data.success) {
            wx.setStorage({
              key: 'serviceId',
              data: res.data.serviceId
            })
            if (that.data.service.serviceImgAddr != null&&that.data.flag) {
              var date = new Date();
              var url = app.globalData.serviceUrl + "/SearchStreet/service/uploadimg?serviceId=" + res.data.serviceId + "&createTime=" + app.timeStamp2String(date);
              console.log(url);
              console.log("uploadAImg:" +that.data.service.serviceImgAddr);
              app.uploadAImg({
                token: that.data.token,
                url: url,
                filePath: that.data.service.serviceImgAddr,
                fileName: "serviceImg"
              })
              console.log(that.data.token);
            }
            wx.navigateBack({
              url: '../service-list/service-list?shopId=' + that.data.shopId
            })
          }
        }
      })
    }
  },
  /*
  formSubmit: function(e) {
    var that = this;
    var ableToSubmit = true;
    var error = 1;
    if (e.detail.value.serviceName.length == 0) {
      this.setData({
        errorMsgs: {
          name_error: '请输入服务名称'
        }
      })
      ableToSubmit = false;
    } else if (e.detail.value.servicePrice.length == 0) {
      this.setData({
        errorMsgs: {
          price_error: '请输入服务价格'
        }
      })
      ableToSubmit = false;
      error = 2;
    } else if (e.detail.value.servicePriority.length == 0) {
      this.setData({
        errorMsgs: {
          priority_error: '请输入优先级'
        }
      })
      ableToSubmit = false;
      error = 3;
    }
    if (ableToSubmit == false) {
      var toastText = that.data.errorMsgs.name_error;
      if (error == 2)
        toastText = that.data.errorMsgs.price_error;
      else if (error == 3)
        toastText = that.data.errorMsgs.priority_error;
      wx.showModal({
        title: '提示',
        content: toastText,
        showCancel: false,
        success(res) {
          if (res.confirm) {
            //  console.log('用户点击确定')
          }
        }
      });
    } else {

      var token = null;
      try {
        const value = wx.getStorageSync('token')
        if (value) {
          token = value;
        }
      } catch (e) {
        console.log("error");
      }
      
      that.data.serviceName = e.detail.value.serviceName;
      that.data.servicePrice = e.detail.value.servicePrice;
      that.data.servicePriority = e.detail.value.servicePriority;
      that.data.serviceDesc = e.detail.value.serviceDesc;
      that.data.serviceContent = e.detail.value.serviceContent;
      that.data.service.shopId = that.data.shopId;
      that.data.service.serviceName = that.data.detail.serviceName;
      that.data.service.servicePrice = that.data.detail.servicePrice;
      that.data.service.servicePriority = that.data.detail.servicePriority;
      that.data.service.serviceDesc = that.data.detail.serviceDesc;
      that.data.service.serviceContent = that.data.detail.serviceContent;
      
      if (that.data.serviceImg != null) {
        that.data.service.serviceImgAddr = that.data.serviceImg;
      }

      var url = '';
      if (parseInt(that.data.isadd) == 1) {
        url = that.data.addUrl;
      } else {
        url = that.data.modifyUrl;
        that.data.service.serviceId = that.data.serviceId;
      }
      console.log(that.data.service);
      wx.request({
        url: url,
        data: {
          "serviceContent": that.data.service.serviceContent,
          "serviceDesc": that.data.service.serviceDesc,
          "serviceId": that.data.serviceId,
          "serviceImgAddr": that.data.service.serviceImgAddr,
          "serviceName": that.data.service.serviceName,
          "servicePrice": that.data.service.servicePrice,
          "servicePriority": that.data.service.servicePriority,
          "shopId": that.data.shopId,
        },
        method: 'POST',
        
        success: res => {
          console.log(res);
          if (res.data.success) {
            wx.setStorage({
              key: 'serviceId',
              data: res.data.serviceId
            })
            if (that.data.serviceImg != null && that.data.serviceImg != undefined) {
              var date = new Date();
              var url = app.globalData.serviceUrl + "/SearchStreet/service/uploadimg?serviceId=" + res.data.serviceId + "&createTime=" + app.timeStamp2String(date);
              app.uploadAImg({
                url: url,
                filePath: that.data.serviceImg,
                fileName: "serviceImg"
              })
            }
            wx.navigateBack({
              url: '../service-list/service-list?shopId=' + that.data.shopId
            })
          }
        }
      })
    }
  },
  */

  cancelOrDeleteService: function(e) {
    var that = this;
    if (that.data.isadd == 1) {
      wx.navigateBack({
        url: '../../page/service-list/service-list?shopId=' + that.data.shopId,
      })
    } else if (that.data.isadd == 0) {
      wx.showModal({
        title: '提示',
        content: "确认删除该服务？",
        showCancel: true,
        success(res) {
          if (res.confirm) {
            var url = that.data.deleteUrl;
            wx.request({
              url: url + that.data.serviceId,
              method: 'POST',
              success: res => {
                console.log(res);
                if (res.data.success) {
                  console.log("删除服务成功")
                  wx.navigateBack({
                    url: '../../page/service-list/service-list' + that.data.shopId,
                  })
                }
              }
            })
          }
        }
      })
    }

  },
  /*
  formReset: function(e) {
    var that = this;
    var url = that.data.deleteUrl;
    wx.request({
      url: url + that.data.serviceId,
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
  */
})