// page/search-shop/search-shop.js
var app=getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    latitude: 23.139317,
    longitude: 113.352354,
    scale: 18,
    maxlng: null,
    maxlat:null,
    minlng:null,
    minlat:null,
    markers: []
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function () {
    var that = this;
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
        that.getShopLocation(res);
      },
    });

    //设置地图控件位置及大小
    wx.getSystemInfo({
      success: (res) => {
        this.setData({
          controls: [
            //使当前定位位置处于屏幕中间的图标
            {
              id: 1,
              iconPath: '/images/heavy-black-heart_2764.png',
              position: {
                left: 10,
                top: res.windowHeight - 75,
                width: 50,
                height: 50
              },
              clickable: true,
            },
            //搜索功能按钮图
            {
              id: 2,
              iconPath: '/images/search_buck.png',
              position: {
                left: res.windowWidth / 2 - 60,
                top: res.windowHeight - 130,
                width: 120,
                height: 120
              },
              clickable: true,
            },
            //中心位置图标（用中心位置去计算距离弥补定位不准确）
            {
              id: 3,
              iconPath: '/images/girl_1f467.png',
              position: {
                left: res.windowWidth / 2 - 10,
                top: res.windowHeight / 2 - 10,
                width: 20,
                height: 20
              },
              clickable: false
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

  getShopLocation: function (res) {
    wx.request({
      url: app.globalData.serviceUrl+'/SearchStreet/shopadmin/searchnearbyshops',    //获取商铺位置
      data: {
        longitude: res.longitude,
        latitude: res.latitude
      },
      method: 'GET',
      success: (res) => {
        if (res.data.success) {
          this.setMarkers(res.data.shopList);
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

  setMarkers: function (shopList) {
    var _markers=[];
    for (var i = 0; i < shopList.length; i++){
      _markers[i] = {
        id: shopList[i].shopId,
        latitude: shopList[i].latitude,
        longitude: shopList[i].longitude,
        name: shopList[i].shopName,

        callout: {
          content: shopList[i].shopName,
          boderRadius: 1000,
          color: "#ff7700",
          display: 'BYCLICK'
        }
      }
    }
    this.setData({
      markers:_markers
    })
    console.log(this.data.markers);
  },

  //移动地图到当前所在位置
  moveToPosition: function () {
    this.mapCtx.moveToLocation();
  },

  //地图control点击事件
  bindcontroltap: function (e) {
    switch (e.controlId) {
      case 1: this.moveToPosition();
        break;
      default: break;
    }
  },

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
      scale: 18
    })
  },

  //地图拖动事件
  bindregionchange: function (e) {
    //console.log(e.type);
    if (e.type == "end") {
      this.mapCtx.getRegion({
        success: (res) => {
          if(res.northeast.latitude > this.data.maxlat ||
            res.northeast.longitude > this.data.maxlng ||
            res.southwest.latitude < this.data.minlat ||
            res.southwest.longitude < this.data.minlng){
            this.mapCtx.getCenterLocation({
              success: (res) => {
                this.getShopLocation(res);
              }
            })
          }
        }
      })
    }
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