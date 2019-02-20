var app = getApp();

var initData='添加店铺环境或菜品图片审核通过率会高哦'
var showCheck="此为必填选项哦"
// 引入SDK核心类
var QQMapWX = require('../../util/qqmap-wx-jssdk.min.js');
//引入md5工具类
var md5 = require('../../util/md5.min.js');
// 实例化API核心类
var qqmapsdk = new QQMapWX({
  key: 'GTPBZ-3HY35-YSDIY-Q4O5T-5SSTQ-YOBGM' // 必填
});
Page({
  data:{
    text:initData,
    errorMsg: showCheck,
    region: ['广东省', '广州市', '海珠区'],
    customItem: '全部',
    imgsrc:null,
    business_img:[],
    business_logo:[],
    show_chlogo:true,
    flag:true,
    shop_imgs:[],
    latitude:null,
    longitude:null,
    markers: [{
      id: 0,
      title: 'T.I.T 创意园',
      latitude: 23.099994,
      longitude: 113.324520,
      iconPath: '../../images/定位.png',//图标路径
      width: 40,
      height: 40,
    }],
    poi: {
      latitude: 23.099994,
      longitude: 113.324520
    },
    errorMsgs:{
      name_error:null,
      scope_error:null,
      percost_error:null,
      code_error:null,
      phone_error:null
    }
  },
  chooseImage: function () {
    var that = this;
    var shop_imgs = this.data.shop_imgs;
    
    if(this.data.shop_imgs.length<3){
    wx.chooseImage({
      count: 3,  //最多可以选择的图片总数  
      sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有  
      sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
      success: function (res) {
        //console.log(res)
        //var imgsrc = res.tempFilePaths
        shop_imgs = shop_imgs.concat(res.tempFilePaths);
        that.setData({
          shop_imgs: shop_imgs,
          //imgsrc:imgsrc
        })
        console.log(shop_imgs);
      }
    })
    }else{
      wx.showToast({
        title: '最多上传3张图片',
        icon: 'loading',
        duration: 2000
      })
    }
  },
  previewImage: function (e) {
    var current = e.target.dataset.src

    wx.previewImage({
      current: current,
      urls: this.data.shop_imgs
    })
  },
  bindRegionChange: function (e) {
    console.log('picker发送选择改变，携带值为', e.detail.value)
    this.setData({
      region: e.detail.value
    })
  },
  upimg: function () {
    var that = this;
    var flag=this.data.flag;
    wx.chooseImage({
      sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有  
      sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
      success: function (res) {
        that.setData({
          business_img: res.tempFilePaths,
          flag:!flag
        })
      }
    })
  },
  chooseLogo: function () {
    var that = this;
    var flag = this.data.show_chlogo;
    wx.chooseImage({
      sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有  
      sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
      success: function (res) {
        that.setData({
          business_logo: res.tempFilePaths,
          show_chlogo: !flag
        })
      }
    })
  },
  sformSubmit(e){
    var _this = this;
    var fulladdress = _this.data.region[0] + _this.data.region[1] + _this.data.region[2] + e.detail.value.fullAddress;
    var sig = md5("/ws/geocoder/v1/?address=" + fulladdress + "&key=GTPBZ-3HY35-YSDIY-Q4O5T-5SSTQ-YOBGM&output=jsonPPxq4x9BswKT7fyXshwNjUOfacWkNbxJ");
    //调用地址解析接口
    qqmapsdk.geocoder({
      sig: sig,
      //获取表单传入地址
      address: fulladdress, //地址参数，例：固定地址，address: '北京市海淀区彩和坊路海淀西大街74号'
      success: function (res) {//成功后的回调
        console.log(res);
        var res = res.result;
        var latitude = res.location.lat;
        var longitude = res.location.lng;
        //根据地址解析在地图上标记解析地址位置
        _this.setData({ // 获取返回结果，放到markers及poi中，并在地图展示
          markers: [{
            id: 0,
            title: res.title,
            latitude: latitude,
            longitude: longitude,
            iconPath: '../../images/定位.png',//图标路径
            width: 20,
            height: 20,
          }],
          poi: { //根据自己data数据设置相应的地图中心坐标变量名称
            latitude: latitude,
            longitude: longitude
          }
        });
        _this.data.latitude = latitude;
        _this.data.longitude = longitude;
        console.log(longitude);
      },
      fail: function (error) {
        console.error(error);
      },
      complete: function (res) {
        console.log(res);
      }
    })
  },
  formSubmit:function(e){
    var that = this;
    var errorMsg=this.data.errorMsg;
    if (that.data.business_logo.length==0)
    {
      wx.showModal({
        title:'提示',  
        content: '请上传您的商店头像',
      })
    }else if(that.data.business_img.length==0){
      wx.showModal({
        title:'提示',
        content: '固定商家需要上传营业执照，请您上传',
      })
    }else if(e.detail.value.shopName.length==0){
      this.setData({
        errorMsgs: {
          name_error: errorMsg
        }
      })
    }else if(e.detail.value.businessScope.length==0){
      this.setData({
        errorMsgs: {
          scope_error: errorMsg
        }
      })
    }else if(e.detail.value.perCost.length==0){
      this.setData({
        errorMsgs:{
         percost_error:errorMsg
        } 
      })
    }else if(e.detail.value.businessLicenseCode.length==0){
      this.setData({
        errorMsgs: {
          code_error: errorMsg
        }
      })
    }else if(e.detail.value.phone.length==0){
      this.setData({
        errorMsgs: {
          phone_error: errorMsg
        }
      })
    }else if(e.detail.value.fullAddress.length==0){
      wx.showModal({
        title: '提示',
        content: '请您输入商店的完整地址',
      })
    }else{
    //console.log('form发生了submit事件，携带数据为：', e.detail.value);
    //var that = this;
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
      url: app.globalData.serviceUrl+"/SearchStreet/shopadmin/registershop?token=" + token,
      data: {
        shopName: e.detail.value.shopName,
        businessScope: e.detail.value.businessScope,
        perCost: e.detail.value.perCost,
        businessLicenseCode: e.detail.value.businessLicenseCode,
        phone: e.detail.value.phone,
        province: that.data.region[0],
        city: that.data.region[1],
        district: that.data.region[2],
        fullAddress : e.detail.value.fullAddress,
        longitude: that.data.longitude,
        latitude: that.data.latitude,
        shopMoreInfo: e.detail.value.shopMoreInfo,
        isMobile : 0
      },
      method: "POST",
      success: res => {
        console.log(res);
        if(res.data.success){
          wx.setStorage({
            key: 'shopId',
            data: res.data.shopId
          })
          var url = app.globalData.serviceUrl+"/SearchStreet/shopadmin/uploadimg?shopId=" + res.data.shopId + "&token=" + token;
          app.uploadAImg({
            url : url,
            filePath: that.data.business_img[0],
            fileName: "businessLicenseImg"
          })
          app.uploadAImg({
            url: url,
            filePath: that.data.business_logo[0],
            fileName: "profileImg"
          })
          for(var i=0; i<that.data.shop_imgs.length; i++){
            app.uploadAImg({
              url: url,
              filePath: that.data.shop_imgs[i],
              fileName: "shopImg"
            })
          }
        } else{
          if(res.data.errMsg=="token为空"||res.data.errMsg=="token无效"){
            wx.redirectTo({
              url: '../../page/login/login'
            })
          }
        }
      }
    })
    }
  }
})