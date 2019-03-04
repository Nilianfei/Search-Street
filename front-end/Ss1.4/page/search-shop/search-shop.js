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
    markers: [],

    openSearch: false,
    shopName: '',
    shopAddress:'',
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

  //搜索栏打开/隐藏
  searchShop: function (e) {
    
    var that=this;
    if(!that.data.openSearch)
    {
      that.setData({
        openSearch: true
      })
    }
    else
    {
      console.log('already open it.');
    }
    
  },

  //地图control点击事件
  bindcontroltap: function (e) {
    
  },

  //查看订单
  checkOrder: function(e){
    /* 订单url填入这里！！！
    wx.navigateTo({
      url: ' '
    })
    */
    console.log('跳转订单page');
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
  //搜索框写入商铺名
  inputShopName: function(e){
    this.setData({
      shopName: e.detail.detail.value
    });
  },

  //搜索框写入商铺地址
  inputShopAddress: function (e) {
    this.setData({
       shopAddress: e.detail.detail.value
    })
  },

  //按商铺名、商铺地址搜索
  buttonClickSearch: function(e){
    console.log(this.data.shopName);
    console.log(this.data.shopAddress);
  },

  buttonClickBack: function(e){
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