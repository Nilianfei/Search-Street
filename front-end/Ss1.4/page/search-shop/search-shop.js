 // page/search-shop/search-shop.js
var app=getApp();
var QQMapWX = require('../../util/qqmap-wx-jssdk.min.js');
var qqmapsdk;

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
    openSearch:false,
    openShopInfo: 0,
    searchShopName: '',
    searchShopAddress:'',
    imgUrl: app.globalData.imgUrl,
    shopInfoPic:'/images/search_buck.png',
    shopInfoName:'',
    shopInfoAddress:'',
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function () {
    var that = this;
    //实体化qqmap(API)核心类
    qqmapsdk = new QQMapWX({
      key: 'GTPBZ-3HY35-YSDIY-Q4O5T-5SSTQ-YOBGM'
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
          console.log(res.data.shopList);
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
    var that=this;
    var _markers=[];
    var shopProfileImg='';
    for (var i = 0; i < shopList.length; i++){
      //markers的计数从1开始，方便控制shopInfo的打开，openShopInfo默认为0(关闭)
      //marker的id不能使用shopId，shopId与markerId不是一一对应的关系
      if(shopList[i].profileImg!=null){
        shopProfileImg=that.data.imgUrl + shopList[i].profileImg;
      }
      else{
        shopProfileImg='/images/search_buck.png';
      }

      _markers[i+1] = {
        id: i+1,
        shopId: shopList[i].shopId,
        latitude: shopList[i].latitude,
        longitude: shopList[i].longitude,
        name: shopList[i].shopName,
        profileImg: shopProfileImg,
        address: shopList[i].city + shopList[i].district + shopList[i].fullAddress,
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
      that.setData({
        openSearch: false
      })
    }
    
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

    console.log(markerId);
    console.log(currentMarker);

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
      shopInfoName: currentMarker.name,
      openShopInfo: markerId,
      shopInfoPic: currentMarker.profileImg,
      shopInfoAddress: currentMarker.address,
    })

    console.log(currentMarker.address);
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
      searchShopName: e.detail.detail.value
    });
  },

  inputShopAddress: function(e){
    this.setData({
      searchShopAddress: e.detail.detail.value
    })
  },

/*
  //getSuggest数据回填
  backfill: function (e) {
    var id = e.currentTarget.id;
    for (var i = 0; i < this.data.suggestion.length; i++) {
      if (i == id) {
        this.setData({
          backfill: this.data.suggestion[i].title
        });
      }
    }
  },

  //搜索框写入商铺地址
  inputShopAddress: function (e) {
    var that=this;
    //调用关键词提示接口
    qqmapsdk.getSuggestion({
      keyword: e.detail.detail.value,
      success: function(res) {
        console.log(res);
        var sug= [];
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
          suggestion: sug
        })
      },
      fail: function(error) {
        console.log("error");
      },
      complete: function(res) {
        console.log(res);
      }
    })
  },
*/
  //按商铺名、商铺地址搜索
  buttonClickSearch: function(e){
    console.log(this.data.searchShopName);
    console.log(this.data.searchShopAddress);
    var _shopName=this.data.searchShopName;
    var _address=this.data.searchShopAddress;
    var that=this;
    if(_address)
    {
      qqmapsdk.geocoder({
        address: _address,
        success: function (res) {
          //成功后的回调
          console.log(res);
          var res = res.result;
          var _latitude = res.location.lat;
          var _longitude = res.location.lng;
          this.setData({
            longitude: _longitude,
            latitude: _latitude
          })
        },
        fail: function (error) {
          console.error(error);
        },
        complete: function (res) {
          console.log(res);
        }
      })

    }
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
  
  /*
  //openInfo(测试功能)
  openInfo: function(e){
    var that = this;
    if(that.data.openShopInfo){
      that.setData({
        openShopInfo:0
      })
    }
    else{
      that.setData({
        openShopInfo: 1
      })
    }
  },
  */

  //关闭shopInfo
  shopInfoBack: function(e){
    var that=this;
    that.setData({
      openShopInfo: 0
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