var app = getApp();

var initData = '添加店铺环境或菜品图片审核通过率会高哦'
var showCheck = "此为必填选项哦"
// 引入SDK核心类
var QQMapWX = require('../../util/qqmap-wx-jssdk.min.js');
//引入md5工具类
var md5 = require('../../util/md5.min.js');
// 实例化API核心类
var qqmapsdk = new QQMapWX({

  key: 'DQYBZ-AQFK6-6JDSI-EHRZV-EFRCJ-TDFZU' // 必填

});
Page({
  data: {
    text: initData,
    errorMsg: showCheck,
    region: ['广东省', '广州市', '海珠区'],
    customItem: '全部',
    imgsrc: null,
    business_img: [],
    business_logo: [],
    show_chlogo: true,
    flag: true,
    shop_imgs: [],
    latitude: null,
    longitude: null,
    markers: [{
      id: 0,
      title: 'T.I.T 创意园',
      latitude: 23.099994,
      longitude: 113.324520,
      iconPath: '../../images/locat.png',//图标路径
      width: 40,
      height: 40,
    }],
    poi: {
      latitude: 23.099994,
      longitude: 113.324520
    },
    errorMsgs: {
      name_error: null,
      scope_error: null,
      percost_error: null,
      code_error: null,
      phone_error: null
    }
  },
  chooseImage: function () {
    var that = this;
    var shop_imgs = this.data.shop_imgs;

    if (this.data.shop_imgs.length < 9) {
      wx.chooseImage({
        count: 9,  //最多可以选择的图片总数  
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
    } else {
      wx.showToast({
        title: '最多上传9张图片',
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

  //数据回填方法
  backfill: function (e) {
    console.log(e);
    var id = e.currentTarget.id;
    for (var i = 0; i < this.data.suggestion.length; i++) {
      if (i == id) {
        this.setData({
          backfill: this.data.suggestion[i].title
        });
        console.log(this.data.backfill);
      }
    }
    this.setData({
      showkeyword: false,
    })
  },

  //触发关键词输入提示事件
  getsuggest: function (e) {
    this.setData({
      showkeyword: true,
    })
    console.log(e);
    var _this = this;
    var _keyword = e.detail.value;
    // var sig = md5("/ws/place/v1/suggestion?keyword=" + _keyword + "&key=X2DBZ-4TCHU-T43VA-BRN5Y-T7LY7-MVBXT&output=jsonSvqdT7KldVKgkNjH86dXvaViwQzTy2X" + "&policy=0&region=" + _this.data.region[1] +"&region_fix=0");
    //console.log(sig);
    //调用关键词提示接口
    qqmapsdk.getSuggestion({
      // sig: sig,
      //获取输入框值并设置keyword参数
      keyword: _keyword, //用户输入的关键词，可设置固定值,如keyword:'KFC'
      region: _this.data.region[1], //设置城市名，限制关键词所示的地域范围，非必填参数
      success: function (res) {//搜索成功后的回调
        console.log(res);
        var sug = [];
        for (var i = 0; i < res.data.length; i++) {
          sug.push({ // 获取返回结果，放到sug数组中
            title: res.data[i].title,
            id: res.data[i].id,
            addr: res.data[i].address,
            city: res.data[i].city,
            district: res.data[i].district,
            latitude: res.data[i].location.lat,
            longitude: res.data[i].location.lng
          });
        }
        _this.setData({ //设置suggestion属性，将关键词搜索结果以列表形式展示
          suggestion: sug
        });
      },
      fail: function (error) {
        console.error(error);
      },
      complete: function (res) {
        console.log(res);
      }
    });
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
  sformSubmit(e) {
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
            iconPath: '../../images/locat.png',//图标路径
            width: 40,
            height: 40,
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
    //console.log(this.data.markers)
  },
  formSubmit: function (e) {
    console.log(e);
    var that = this;
    var errorMsg = this.data.errorMsg;
    if (that.data.business_logo.length == 0) {
      wx.showModal({
        title: '提示',
        content: '请上传您的商店头像',
      })
    } else if (that.data.business_img.length == 0) {
      wx.showModal({
        title: '提示',
        content: '固定商家需要上传营业执照，请您上传',
      })
    } else if (e.detail.value.shopName.length == 0) {
      this.setData({
        errorMsgs: {
          name_error: errorMsg
        }
      })
    } else if (e.detail.value.businessScope.length == 0) {
      this.setData({
        errorMsgs: {
          scope_error: errorMsg
        }
      })
    } else if (e.detail.value.perCost.length == 0) {
      this.setData({
        errorMsgs: {
          percost_error: errorMsg
        }
      })
    } else if (e.detail.value.businessLicenseCode.length == 0) {
      this.setData({
        errorMsgs: {
          code_error: errorMsg
        }
      })
    } else if (e.detail.value.phone.length == 0) {
      this.setData({
        errorMsgs: {
          phone_error: errorMsg
        }
      })
    } else if (!(/^1(3|4|5|7|8)\d{9}$/.test(e.detail.value.phone)) && !(/^(\(\d{3,4}\)|\d{3,4}-|\s)?\d{7,8}$/.test(e.detail.value.phone))) //验证11位手机号码，固定电话号码
    {
      wx.showModal({
        title: '提示',
        content: '您输入的手机号码或固定号码有误，请重新输入',
      })
    } 
    else if (e.detail.value.fullAddress.length == 0) {
      wx.showModal({
        title: '提示',
        content: '请您输入商店的完整地址',
      })
    }
    else if (that.data.latitude == null || that.data.longitude == null) {
      wx.showModal({
        title: '提示',
        content: '您还没有开启定位哦',
      })
    }
    else {
      console.log('form发生了submit事件，携带数据为：', e.detail.value);
      //var that = this;
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
        url: app.globalData.serviceUrl + "/SearchStreet/shopadmin/registershop",

        data: {
          shopName: e.detail.value.shopName,
          businessScope: e.detail.value.businessScope,
          perCost: e.detail.value.perCost,
          businessLicenseCode: e.detail.value.businessLicenseCode,
          phone: e.detail.value.phone,
          province: that.data.region[0],
          city: that.data.region[1],
          district: that.data.region[2],
          fullAddress: e.detail.value.fullAddress,
          longitude: that.data.longitude,
          latitude: that.data.latitude,
          shopMoreInfo: e.detail.value.shopMoreInfo,
          isMobile: 0
        },
        method: "POST",
        header: {
          'content-type': 'application/json',
          'token': token
        },
        success: res => {
          console.log(res);
          if (res.data.success) {
            wx.setStorage({
              key: 'shopId',
              data: res.data.shopId
            })
            var date = new Date();
            var url = app.globalData.serviceUrl + "/SearchStreet/shopadmin/uploadimg?shopId=" + res.data.shopId + "&createTime=" + app.timeStamp2String(date);
            app.uploadAImg({
              url: url,
              filePath: that.data.business_img[0],
              fileName: "businessLicenseImg",
              token: token,
            })
            app.uploadAImg({
              url: url,
              filePath: that.data.business_logo[0],
              fileName: "profileImg",
              token: token,
            })
            for (var i = 0; i < that.data.shop_imgs.length; i++) {
              app.uploadAImg({
                url: url,
                filePath: that.data.shop_imgs[i],
                fileName: "shopImg",
                token: token,
              })
            }
            wx.navigateBack({
              delta: 1
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
  }
})