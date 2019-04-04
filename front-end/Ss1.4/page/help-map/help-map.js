// page/help-map/help-map.js

var app = getApp();
const { $Message } = require('../../dist/base/index');
var QQMapWX = require('../../util/qqmap-wx-jssdk.min.js');
var qqmapsdk;


Page({

  /**
   * 页面的初始数据
   */
  data: {
    latitude: 23.139317,
    longitude: 113.352354,
    city: '',
    scale: 18,
    maxlng: null,
    maxlat: null,
    minlng: null,
    minlat: null,
    markers: [],
    openSearch: false,
    openAppealInfo: 0,
    searchAppealTitle: '',
    searchAppealAddress: '',
    imgUrl: app.globalData.imgUrl,
    shopInfoPic: '/images/search_buck.png',
    appealInfoTitle: '',
    appealInfoAddress: '',
    appealInfoSouCoin: '',
    appealInfoEndTime: '',
    appealInfoId: 12
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function () {
    var that = this;
    //实体化qqmap(API)核心类
    qqmapsdk = new QQMapWX({
      key: 'DQYBZ-AQFK6-6JDSI-EHRZV-EFRCJ-TDFZU'
    });

    console.log("初始化地图");
    wx.getSetting({
      success(res) {
        if (!res.authSetting['scope.userLocation']) {
          wx.authorize({
            scope: 'scope.userLocation',
            success() {

            }
          })
        }
      }
    })

    qqmapsdk.reverseGeocoder({
      success(res) {
        console.log(res);
        that.setData({
          city: res.result.address_component.city
        })
      },
      fail: function (error) {
        console.error(error);
      },
      complete: function (res) {
        console.log(res);
      }
    })


    //获取当前位置
    wx.getLocation({
      type: "gcj02",
      success: function (res) {
        var latitude = res.latitude;
        var longitude = res.longitude;
        that.setData({
          latitude: res.latitude,
          longitude: res.longitude
        });
        //初始化markers
        that.getAppealLocation(res);
      },
    });

    //设置地图控件位置及大小
    wx.getSystemInfo({
      success: (res) => {
        this.setData({
          controls: [
            //中心位置图标
            {
              id: 3,
              iconPath: '/images/round-pushpin_1f4cd.png',
              position: {
                left: res.windowWidth / 2 - 20,
                top: res.windowHeight / 2 - 40,
                width: 40,
                height: 40
              },
              clickable: false,
            }

          ]
        })
      },
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
    this.mapCtx = wx.createMapContext('myMap');
    this.moveToPosition();
  },

  getAppealLocation: function (res) {
    wx.request({
      url: app.globalData.serviceUrl + '/SearchStreet/appeal/searchnearbyappeals',    //获取附近求助位置
      data: {
        longitude: res.longitude,
        latitude: res.latitude,
        appealTitle: this.data.searchAppealTitle
      },
      method: 'GET',
      success: (res) => {
        if (res.data.success) {
          this.setMarkers(res.data.appealList);
          console.log(res.data.appealList);
          this.setData({
            maxlat: res.data.maxlat,
            maxlng: res.data.maxlng,
            minlat: res.data.minlat,
            minlng: res.data.minlng
          })
        } else {
          console.log(res.data.errMsg);
        }
      },
      fail: function (res) {

      },
      complete: function (res) {

      }
    })
  },

  setMarkers: function (appealList) {
    var that = this;
    var _markers = [];
    for (var i = 0; i < appealList.length; i++) {
      //markers的计数从1开始，方便控制shopInfo的打开，openShopInfo默认为0(关闭)
      //marker的id不能使用appealId，appealId与markerId不是一一对应的关系

      _markers[i + 1] = {
        id: i + 1,
        appealId: appealList[i].appealId,
        latitude: appealList[i].latitude,
        longitude: appealList[i].longitude,
        appealTitle: appealList[i].appealTitle,
        address: appealList[i].city + appealList[i].district + appealList[i].fullAddress,
        appealContent: appealList[i].appealContent,
        startTime: appealList[i].startTime,
        endTime: appealList[i].endTime,
        souCoin: appealList[i].souCoin,
        iconPath: "/images/help_markers.png",
      }

    }
    this.setData({
      markers: _markers
    })
    console.log(this.data.markers);
  },


  //移动地图到当前所在位置
  moveToPosition: function () {
    var that = this;
    wx.getLocation({
      type: "gcj02",
      success: function (res) {
        that.setData({
          latitude: res.latitude,
          longitude: res.longitude
        });
      },
    })
    this.mapCtx.moveToLocation();
  },

  //搜索栏打开/隐藏
  searchAppeal: function (e) {

    var that = this;

    that.setData({
      searchAppealTitle: '',
      searchAppealAddress: ''
    })
    if (!that.data.openSearch) {
      that.setData({
        openSearch: true
      })
    }
    else {
      that.setData({
        openSearch: false
      })
    }

  },

  /*
  //查看服务
  checkService: function (e) {
    var that = this;
    wx.navigateTo({
      url: '../user-service-list/user-service-list?appealId=' + that.data.appealId,
    })
  },
  */

  //地图marker点击事件
  bindmarkertap: function (e) {
    let _markers = this.data.markers;
    let markerId = e.markerId;
    let currentMarker = _markers[markerId];
    
    this.setData({
      polyline: [{
        points: [{
          longitude: this.data.longitude,
          latitude: this.data.latitude
        },
        {
          longitude: currentMarker.longitude,
          latitude: currentMarker.latitude
        }],
        color: "#ff7700",
        width: 1,
        dottedLine: true
      }],
      scale: 18,
      appealInfoTitle: currentMarker.appealTitle,
      openAppealInfo: markerId,
      appealInfoAddress: currentMarker.address,
      appealInfoSouCoin: currentMarker.souCoin,
      appealInfoId: currentMarker.appealId,
      appealInfoEndTime: app.timeStamp2String(currentMarker.endTime),
    })

    console.log(this.data.appealInfoId);
  },

  gotoAppeal: function (e) {
    wx.navigateTo({
      url: '../appeal/appeal?appealId=' + this.data.appealInfoId
    })
  },

  //地图拖动事件
  bindregionchange: function (e) {
    //console.log(e.type);
    if (e.type == "end") {
      this.mapCtx.getRegion({
        success: (res) => {
          if (res.northeast.latitude > this.data.maxlat ||
            res.northeast.longitude > this.data.maxlng ||
            res.southwest.latitude < this.data.minlat ||
            res.southwest.longitude < this.data.minlng) {

            this.setData({
              openAppealInfo: 0
            })

            this.mapCtx.getCenterLocation({
              success: (res) => {
                this.getAppealLocation(res);
              }
            })
          }
        }
      })
    }
  },
  //搜索框写入商铺名
  inputAppealTitle: function (e) {
    this.setData({
      searchAppealTitle: e.detail.detail.value
    });
  },


  // inputAppealAddress: function(e){
  //   this.setData({
  //     searchAppealAddress: e.detail.detail.value
  //   })
  //   console.log(searchAppealAddress);
  // },


  //getSuggest数据回填
  backfill: function (e) {
    var that = this;
    var id = e.currentTarget.id;
    for (var i = 0; i < this.data.suggestion.length; i++) {
      if (this.data.suggestion[i].title == id) {
        that.setData({
          searchAppealAddress: this.data.suggestion[i].title,
          latitude: this.data.suggestion[i].latitude,
          longitude: this.data.suggestion[i].longitude,
          openSearch: false
        });
      }
    }
  },

  //搜索框写入商铺地址
  inputAppealAddress: function (e) {
    var that = this;
    var _keyword = e.detail.detail.value;

    that.setData({
      searchAppealAddress: e.detail.detail.value
    })
    //调用关键词提示接口
    qqmapsdk.getSuggestion({
      keyword: _keyword,
      region: that.data.city,
      success: function (res) {
        console.log(res);
        var sug = [];
        for (var i = 0; i < res.data.length; ++i) {
          sug.push({
            title: res.data[i].title,
            id: res.data[i].id,
            addr: res.data[i].address,
            city: res.data[i].city,
            district: res.data[i].district,
            latitude: res.data[i].location.lat,
            longitude: res.data[i].location.lng
          });
        }
        that.setData({
          suggestion: sug,
        })
      },
      fail: function (error) {
        console.log("error");
      },
      complete: function (res) {
        console.log(res);
      }
    })
  },

  addAddress: function (e) {
    console.log(e.currentTarget.id);
  },

  // handleNoAddressResult() {
  //   $Message({
  //     content: '3秒后消失',
  //     duration: 3
  //   });
  // },

  //按商铺名、商铺地址搜索
  buttonClickSearch: function (e) {

    console.log(this.data.searchAppealTitle);
    console.log(this.data.searchAppealAddress);

    var that = this;

    var _shopName = this.data.searchAppealTitle;
    var _address = this.data.searchAppealAddress;

    if (_shopName) {
      this.mapCtx.getCenterLocation({
        success: (res) => {
          this.getAppealLocation(res);
        }
      })
    }

    if (_address) {

      qqmapsdk.geocoder({
        address: this.data.city + _address,
        success: function (res) {
          //成功后的回调
          console.log(res);
          var res = res.result;
          var _latitude = res.location.lat;
          var _longitude = res.location.lng;
          that.setData({
            longitude: _longitude,
            latitude: _latitude
          })
          this.moveToPosition;
        },
        fail: function (error) {
          console.error(error);
        },
        complete: function (res) {
          console.log(res);
        }
      })

    }

    this.setData({
      openSearch: false
    })

  },

  buttonClickBack: function (e) {
    var that = this;
    if (that.data.openSearch) {
      that.setData({
        openSearch: false
      })
    }
    else {
      console.log('impossible click this hh.');
    }
  },
  /* 点击导航按钮转移到外部应用进行导航 */
  navigation: function (e) {
    var point = this.data.polyline[0].points[1];
    var shopname = this.data.shopInfoName;
    console.log(point.latitude);
    wx.openLocation({
      latitude: parseInt(point.latitude),
      longitude: parseInt(point.longitude),
      scale: 18,
      name: shopname
    })
  },
  /*
  //openInfo(测试功能)
  openInfo: function(e){
    var that = this;
    if(that.data.openAppealInfo){
      that.setData({
        openAppealInfo:0
      })
    }
    else{
      that.setData({
        openAppealInfo: 1
      })
    }
  },
  */

  //关闭shopInfo
  appealInfoBack: function (e) {
    var that = this;
    that.setData({
      openAppealInfo: 0
    })
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