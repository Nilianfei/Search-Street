// page/shop-settings/shop-settings.js
var app = getApp();
var QQMapWX = require('../../util/qqmap-wx-jssdk.min.js');
var qqmapsdk;

Page({

  /**
   * 页面的初始数据
   */
  data: {
    shop: {},
    latitude: 23.139317,
    longitude: 113.352354,
    tempProvince:'',
    tempCity:'',
    tempDistrict: '',
    markers: [],
    isMobile: [{
      id: 0,
      name: '固定商铺',
    }, {
      id: 1,
      name: '移动商铺'
    }],
    current: "固定商铺",
  },

  handleIsMobileChange({
    detail = {}
  }) {
    var p = "shop.isMobile";
    this.setData({
      current: detail.value,
    });

    if (this.data.current == "固定商铺") {
      this.setData({
        [p]: 0
      })
    } else if (this.data.current == "移动商铺") {
      this.setData({
        [p]: 1
      })
    } else {
      console.log("error!");
    }
    console.log(this.data.shop.isMobile);
  },
  /**
   * bind:change事件集合
   */
  inputShopName: function(e) {
    var p = "shop.shopName";
    this.setData({
      [p]: e.detail.detail.value
    });
    console.log(this.data.shop.shopName);
  },

  inputPhone: function(e) {
    var p = "shop.phone";
    this.setData({
      [p]: e.detail.detail.value
    });
    console.log(this.data.shop.phone);
  },

  inputBusinessScope: function(e) {
    var p = "shop.businessScope";
    this.setData({
      [p]: e.detail.detail.value
    });
    console.log(this.data.shop.businessScope);
  },

  inputPerCost: function(e) {
    var p = "shop.perCost";
    this.setData({
      [p]: e.detail.detail.value
    });
    console.log(this.data.shop.perCost);
  },

  inputBusinessLicenceCode: function(e) {
    var p = "shop.businessLicenseCode";
    this.setData({
      [p]: e.detail.detail.value
    });
    console.log(this.data.shop.businessLicenseCode);
  },

  inputprovince: function(e) {
    this.setData({
      tempProvince: e.detail.detail.value
    });
    console.log(this.data.tempProvince);
  },

  inputcity: function(e) {
    this.setData({
      tempCity: e.detail.detail.value
    });
    console.log(this.data.tempCity);
  },

  inputdistrict: function(e) {
    this.setData({
      tempDistrict: e.detail.detail.value
    });
    console.log(this.data.tempDistrict);
  },

  inputFullAddress: function(e) {
    var p = "shop.fullAddress";
    this.setData({
      [p]: e.detail.detail.value
    });
    console.log(this.data.shop.fullAddress);
  },

  inputShopMoreInfo: function(e) {
    var p = "shop.shopMoreInfo";
    this.setData({
      [p]: e.detail.detail.value
    });
    console.log(this.data.shop.shopMoreInfo);
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    var that = this;
    console.log("get" + options.shopId);
    that.setData({
      shopId: options.shopId
    })
    this.mapCtx = wx.createMapContext('myMap');

    //获取商铺信息
    wx.request({
      url: app.globalData.serviceUrl + "/SearchStreet/shopadmin/getshopbyid?shopId=" + that.data.shopId,
      data: {},
      method: "GET",
      success: res => {
        console.log(res);
        var shop = res.data.shop;
        shop.profileImg = that.data.imgUrl + shop.profileImg;
        that.setData({
          shop: res.data.shop, //设置页面中的数据
          longitude: res.data.shop.longitude,
          latitude: res.data.shop.latitude,
          tempProvince: res.data.shop.province,
          tempCity: res.data.shop.city,
          tempDistrict: res.data.shop.district,
          markers: [{
            id: 0,
            latitude: res.data.shop.latitude,
            longitude: res.data.shop.longitude,
            iconPath: '/images/round-pushpin_1f4cd.png', //图标路径
            width: 30,
            height: 30,
          }, ],
        })
        if(shop.isMobile==0)
        {
          that.setData({
            current: "固定商铺"
          })
        }else if(shop.isMobile==1){
          that.setData({
            current: "移动商铺"
          })
        }
        this.moveToLocation;
        console.log(this.data.markers);
      }
    })

    //实体化qqmap(API)核心类
    qqmapsdk = new QQMapWX({
      key: 'DQYBZ-AQFK6-6JDSI-EHRZV-EFRCJ-TDFZU'
    });


  },

  moveToPosition: function() {
    var that = this;
    wx.getLocation({
      type: "gcj02",
      success: function(res) {
        that.setData({
          latitude: res.latitude,
          longitude: res.longitude
        });
      },
    })
    this.mapCtx.moveToLocation();
  },

  buttonClickMoveMap: function(e) {
    var that = this;
    var _address = this.data.tempProvince + this.data.tempCity + this.data.tempDistrict + this.data.shop.fullAddress;
    var _province = "shop.province";
    var _city = "shop.city";
    var _district = "shop.district";
    console.log(_address);
    qqmapsdk.geocoder({
      address: _address,
      success: function(res) {
        //成功后的回调
        console.log(res);
        var res = res.result;
        var _latitude = res.location.lat;
        var _longitude = res.location.lng;
        that.setData({
          longitude: _longitude,
          latitude: _latitude,
          [_province]: res.address_components.province,
          [_city]: res.address_components.city,
          [_district]: res.address_components.district,
          markers: [{
            id: 0,
            latitude: _latitude,
            longitude: _longitude,
            iconPath: '/images/round-pushpin_1f4cd.png', //图标路径
            width: 30,
            height: 30,
          }],
        })
        this.moveToPosition;
      },
      fail: function(error) {
        console.error(error);
      },
      complete: function(res) {
        console.log(res);
      }
    })
  },

  //确认修改上传数据
  buttonClickCommit: function(e) {
    var that = this;

    if (that.data.shop.shopName.length == 0) {
      wx.showModal({
        title: '提示',
        content: '请填写您的商铺名称',
      })
    } else if (that.data.shop.phone.length == 0) {
      wx.showModal({
        title: '提示',
        content: '请填写商铺的联系电话',
      })
    } else if (that.data.shop.businessScope.length == 0) {
      wx.showModal({
        title: '提示',
        content: '请填写商铺的经营范围',
      })
    } else if (that.data.shop.perCost.length == 0) {
      wx.showModal({
        title: '提示',
        content: '请填写商铺的人均消费',
      })
    } else if (that.data.shop.businessLicenseCode.length == 0) {
      wx.showModal({
        title: '提示',
        content: '请填写商铺的执照编码',
      })
    } else if (that.data.shop.fullAddress.length == 0) {
      wx.showModal({
        title: '提示',
        content: '请您输入商店的完整地址',
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
      console.log(that.data.shop);
      wx.request({
        url: app.globalData.serviceUrl + "/SearchStreet/shopadmin/modifyshop?token=" + token,
        data: {
          shopId: this.data.shop.shopId,
          shopName: this.data.shop.shopName,
          businessScope: this.data.shop.businessScope,
          perCost: this.data.shop.perCost,
          businessLicenseCode: this.data.shop.businessLicenseCode,
          phone: this.data.shop.phone,
          province: this.data.shop.province,
          city: this.data.shop.city,
          district: this.data.shop.district,
          fullAddress: this.data.shop.fullAddress,
          longitude: this.data.longitude,
          latitude: this.data.latitude,
          shopMoreInfo: this.data.shop.shopMoreInfo,
          isMobile: this.data.shop.isMobile
        },
        method: "POST",
        success: res => {
          console.log(res);
          wx.navigateBack({
            
          });
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

}
})