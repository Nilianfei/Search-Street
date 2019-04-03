// page/user-service-list/user-service-list.js
var app = getApp();
Page({
  data: {
    TabCur: 0,
    starRating: 0,
    serviceRating: 0,
    successRate: 0,
    service: [],
    shop: null,
    imgUrl: "http://139.196.101.84:8080/image",
    shopId: 0,
    profileImgUrl: '',
    //全局变量
    list: [],
    token: null,
    //加载样式是否显示
    loading: true,
    current: 1,
    pageNum: 0,
    pageSize: 6,
    cardCur: 0,
    shopImgList: null,
    businessLicenseImg: null,
    tower: [{
      id: 0,
      url: ''
    }, {
      id: 1,
      url: ''
    }, {
      id: 2,
      url: ''
    }, {
      id: 3,
      url: ''
    }, {
      id: 4,
      url: ''
    }, {
      id: 5,
      url: ''
    }, {
      id: 6,
      url: ''
    }],
    tower2: null,

    //from star new css
    cur: 'tab1',
  },

  handleChange_tabs({
    detail
  }) {
    this.setData({
      cur: detail.key,
    })
  },

  handleChange_page({
    detail
  }) {
    var that = this;

    if (that.data.current <= that.data.pageNum) {
      that.setData({
        loading: true
      })
      const type = detail.type;
      if (type === 'next') {
        that.setData({
          current: that.data.current + 1
        });
      } else if (type === 'prev') {
        that.setData({
          current: that.data.current - 1
        });
      }
      that.tabchange(that.data.current)


    }
  },
  onLoad: function(options) {
    var that = this;
    that.setData({
      shopId: options.shopId //options.id
    })
    //获取商铺信息
    wx.request({
      url: app.globalData.serviceUrl + "/SearchStreet/shopComment/getAvgScorebyshopid?shopId=" + that.data.shopId, //获取评分信息
      data: {},
      method: "GET",
      success: res => {
        console.log(res);
        var shop = res.data.shop;
        var tower = that.data.tower;
        var businessLicenseImg = app.globalData.imgUrl + shop.businessLicenseImg;
        var url = app.globalData.imgUrl + shop.profileImg;
        that.setData({
          shop: shop, //设置页面中的数据
          profileImgUrl: url,
          tower: tower,
          tower2: tower,
          businessLicenseImg: businessLicenseImg,
          serviceRating: res.data.serviceAvg,
          starRating: res.data.starAvg,
          successRate: res.data.successRate
        })
        
        
        if (shop.shopImgList) {
          var length = shop.shopImgList.length;
          for (var i = 0; i < shop.shopImgList.length; i++) {
            shop.shopImgList[i].imgAddr = app.globalData.imgUrl + shop.shopImgList[i].imgAddr;
            tower[i].url = shop.shopImgList[i].imgAddr;
          }
        }
        

        // 初始化towerSwiper 传已有的数组名即可
        that.towerSwiper('tower2', length);
        
      }
    })
    that.tabchange(0)

  },
  tabchange: function(options) {
    var that = this;
    var pageIndex = options;
    if (that.data.TabCur == 0) {
      wx.request({
        //点击服务时 获得服务列表 
        url: app.globalData.serviceUrl + '/SearchStreet/service/getservicelistbyshopid?shopId=' + that.data.shopId + '&pageIndex=' + pageIndex + '&pageSize=' + that.data.pageSize,
        method: 'GET',
        success: function(res) {
          var serviceList = res.data.serviceList; //res.data就是从后台接收到的值
          that.setData({
            list: serviceList,
            loading: false,
            pageNum: res.data.pageNum
          })
        },
        fail: function(res) {
          console.log('submit fail');
        },
        complete: function(res) {
          console.log('submit complete');
        }
      })
    } else if (that.data.TabCur == 1) {
      //点击评论时，获得评论
      wx.request({
        url: app.globalData.serviceUrl + '/SearchStreet/shopComment/getshopCommentlistbyshopid?shopId=' + that.data.shopId + '&pageIndex=' + pageIndex + '&pageSize=' + that.data.pageSize,
        method: 'GET',
        success: function(res) {
          var shopCommentList = res.data.shopCommentList; //res.data就是从后台接收到的值
          that.setData({
            list: shopCommentList,
            service: res.data.serviceList,
            loading: false,
            pageNum: res.data.pageNum
          })
        },
        fail: function(res) {
          console.log('submit fail');
        },
        complete: function(res) {
          console.log('submit complete');
        }
      })
    } else {

    }
  },
  tabSelect(e) {
    this.setData({
      TabCur: e.currentTarget.dataset.id,
      scrollLeft: (e.currentTarget.dataset.id - 1) * 60
    })
    this.tabchange(this.data.current)
  },
  DotStyle(e) {
    this.setData({
      DotStyle: e.detail.value
    })
  },
  // cardSwiper
  cardSwiper(e) {
    this.setData({
      cardCur: e.detail.current
    })
  },
  // towerSwiper
  // 初始化towerSwiper
  towerSwiper(name, length) {
    let list = this.data[name];
    for (let i = 0; i < length; i++) {
      list[i].zIndex = parseInt(length / 2) + 1 - Math.abs(i - parseInt(length / 2))
      list[i].mLeft = i - parseInt(length / 2)
    }
    this.setData({
      towerList: list
    })
  },

  // towerSwiper触摸开始
  towerStart(e) {
    this.setData({
      towerStart: e.touches[0].pageX
    })
  },

  // towerSwiper计算方向
  towerMove(e) {
    this.setData({
      direction: e.touches[0].pageX - this.data.towerStart > 0 ? 'right' : 'left'
    })
  },

  // towerSwiper计算滚动
  towerEnd(e) {
    let direction = this.data.direction;
    let list = this.data.towerList;
    if (direction == 'right') {
      let mLeft = list[0].mLeft;
      let zIndex = list[0].zIndex;
      for (let i = 1; i < list.length; i++) {
        list[i - 1].mLeft = list[i].mLeft
        list[i - 1].zIndex = list[i].zIndex
      }
      list[list.length - 1].mLeft = mLeft;
      list[list.length - 1].zIndex = zIndex;
      this.setData({
        towerList: list
      })
    } else {
      let mLeft = list[list.length - 1].mLeft;
      let zIndex = list[list.length - 1].zIndex;
      for (let i = list.length - 1; i > 0; i--) {
        list[i].mLeft = list[i - 1].mLeft
        list[i].zIndex = list[i - 1].zIndex
      }
      list[0].mLeft = mLeft;
      list[0].zIndex = zIndex;
      this.setData({
        towerList: list
      })
    }
  },
  order: function(e) {
    var service = e.currentTarget.dataset.content;
    wx.navigateTo({
      url: '../service/service?service=' + JSON.stringify(service),
    })
  }
})