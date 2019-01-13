// 引入SDK核心类
var QQMapWX = require('../../util/qqmap-wx-jssdk.min.js');

// 实例化API核心类
var qqmapsdk = new QQMapWX({
  key: 'X2DBZ-4TCHU-T43VA-BRN5Y-T7LY7-MVBXT' // 必填
});
Page({

  /**
   * 页面的初始数据
   */
  data: {
    region: ['广东省', '广州市', '海珠区'],
    customItem: '全部',
    latitude: null,
    longitude: null,
    flag:true,
    business_img:[],
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
    }
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
  upimg: function () {
    var that = this;
    var flag = this.data.flag;
    wx.chooseImage({
      sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有  
      sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
      success: function (res) {
        that.setData({
          business_img: res.tempFilePaths,
          flag: !flag
        })
      }
    })
  },
  sformSubmit(e) {
    var _this = this;
    var fulladdress = _this.data.region[0] + _this.data.region[1] + _this.data.region[2] + e.detail.value.fullAddress;
    //调用地址解析接口
    qqmapsdk.geocoder({
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
  formSubmit: function (e) {
    //console.log('form发生了submit事件，携带数据为：', e.detail.value);
    var that = this;
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
      url: "http://localhost:8080/ss/shopadmin/registershop?token=" + token,
      data: {
        shopName: e.detail.value.shopName,
        businessScope: e.detail.value.businessScope,
        perCost: e.detail.value.perCost,
        phone: e.detail.value.phone,
        province: that.data.region[0],
        city: that.data.region[1],
        district: that.data.region[2],
        fullAddress: e.detail.value.fullAddress,
        coordinateY: that.data.longitude,
        coordinateX: that.data.latitude,
        shopMoreInfo: e.detail.value.shopMoreInfo,
        isMobile: 1
      },
      method: "POST",
      success: res => {
        console.log(res);
        if (res.data.success) {
          wx.setStorage({
            key: 'shopId',
            data: res.data.shopId
          })
          var url = "http://localhost:8080/ss/shopadmin/modifyshop?shopId=" + res.data.shopId + "&token=" + token;
          app.uploadAImg({
            url: url,
            filePath: that.data.business_img[0],
            fileName: "businessLogo"
          })
        } else {
          console.log("registershop error")
        }
      }
    })

  }
})