// page/user-order-list/user-order-list.js
var app = getApp();
var util = require("../../util/util.js");

Page({
  data: {
    current: 0,
    service: [],
    shopComment: [],
    sserviceImg: [],
    orderlist: [],
    worderlist: [],
    corderlist: [],
    serviceImg: [],
    imgId: [],
    wserviceImg: [],
    wimgId: [],
    cserviceImg: [],
    cimgId: [],
    state: [{
        "id": 0,
        "color": "green",
        "state": "已下单"
      },
      {
        "id": 1,
        "color": "orange",
        "state": "待评价"
      },
      {
        "id": 2,
        "color": "gray",
        "state": "已完成"
      },
      {
        "id": 3,
        "color": "grey",
        "state": "已取消"
      },
      {
        "id": 4,
        "color": "black",
        "state": "已删除"
      }

    ],
    token: null,
    userId: null,
    currtab: 0,
    swipertab: [{
      name: '全部',
      index: 0
    }, {
      name: '待评价',
      index: 1
    }, {
      name: '已取消',
      index: 2
    }, {
      name: '我的评价',
      index: 3
    }],
  },
  onLoad: function() {
    var that = this
    try { //同步获取与用户信息有关的缓存token
      const value = wx.getStorageSync('token');
      const userId = wx.getStorageSync('userId');
      if (value) {
        that.setData({
          token: value
        })
      }
      if (userId) {
        that.setData({
          userId: userId
        })
      } else {
        wx.request({
          url: app.globalData.serviceUrl + '/SearchStreet/wechat/getUserInfo',
          header: {
            token: token,
          },
          data: {

          },
          success: function(res) {
            // 拿到自己后台传过来的数据，自己作处理
            console.log(res.data);
            if (null != res.data.success && res.data.success) {
              //用户登录成功
              wx.setStorage({
                key: 'userId',
                data: res.data.personInfo.userId
              });
              that.setData({
                userId: res.data.personInfo.userId
              })
            }
          },
          fail: function(err) {
            console.log(err)
          }
        })
      }
    } catch (e) {
      console.log("error");
    }
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function() {
    // 页面渲染完成
    this.getDeviceInfo()
    this.orderShow()
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function() {
    this.orderShow()
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

  getDeviceInfo: function() {
    let that = this
    wx.getSystemInfo({
      success: function(res) {
        that.setData({
          deviceW: res.windowWidth,
          deviceH: res.windowHeight
        })
      }
    })
  },

  /**
   * 选项卡点击切换
   */
  tabSwitch: function(e) {
    var that = this
    if (this.data.currtab === e.target.dataset.current) {
      return false
    } else {
      that.setData({
        currtab: e.target.dataset.current
      })
    }
  },

  tabChange: function(e) {
    this.setData({
      currtab: e.detail.current
    })
    this.orderShow()
  },

  orderShow: function() {
    let that = this
    switch (this.data.currtab) {
      case 0:
        that.allShow()
        break
      case 1:
        that.waitShow()
        break
      case 2:
        that.cancelShow()
        break
      case 3:
        that.commentShow()
        break
    }
  },
  allShow: function() {
    var that = this;
    //查询用户的订单
    wx.request({
      url: app.globalData.serviceUrl + '/SearchStreet/order/getOrderlistAndServicebyuo?userId=' + that.data.userId,
      data: {},
      method: "GET",
      success: function(res) {
        console.log(res);
        if (res.data.success) {
          var order = res.data.OrderList;
          var img = [];
          var id = [];
          var service = res.data.serviceList;
          console.log(service);
          //利用一次遍历将每一单的照片和订单号记录下来(orderList与serviceList其实是一一对应的orderList[i]对应的服务是serviceList[i])
          for (var i = 0; i < order.length; i++) {
            id[i] = order[i].orderId;
            var time = JSON.stringify(order[i].createTime);
            order[i].createTime = util.formatDate(time);
            if (order[i].overTime != null) {
              time = JSON.stringify(order[i].overTime);
              order[i].overTime = util.formatDate(time);
            } else
              order[i].overTime = '';
            /*
             *跳过null 需要与后端沟通更好的解决办法  显示服务不存在的图片
             */
            if (service[i] != null)
            {  
              if (service[i].serviceImgAddr != null)
                img[i] = app.globalData.imgUrl + service[i].serviceImgAddr; //经过修改后可以正常显示订单图片信息,还需等addservice调整后加上服务器的ip  
            }
            if(img[i]==null)
            {
              img[i] = "/images/nophoto.png";
            }
          }
          that.setData({
            orderlist: order,
            serviceImg: img,
            imgId: id,  //记录订单id
            service: service //此4全为数组变量，且index相同时的是同一个订单记录里不同的信息。
          })
        }
      }
    })
  },

  waitShow: function() {
    var that = this;

    //查询用户待评价订单
    wx.request({
      url: app.globalData.serviceUrl + '/SearchStreet/order/getOrderlistAndServicebyuo?userId=' + that.data.userId + '&orderStatus=1',
      data: {},
      method: "GET",
      success: res => {
        console.log(res);
        if (res.data.success) {
          var order = res.data.OrderList; //根据条件筛选出待评价的订单信息
          var img = that.data.serviceImg; //该商铺提供的所有服务的img都存储在此，第一次载入时由先执行的allShow()存到data
          var simg = [];
          var simgid = [];
          var id = that.data.imgId;
          var service = res.data.serviceList;
          for (var i = 0; i < order.length; i++) {
            var time = JSON.stringify(order[i].createTime);
            order[i].createTime = util.formatDate(time);
            if (order[i].overTime != null) {
              time = JSON.stringify(order[i].overTime);
              order[i].overTime = util.formatDate(time);
            } else
              order[i].overTime = '';
              for (var j = 0; j < img.length; j++) {
                if (id[j] == order[i].orderId) {
                  simgid[i] = j;//在全部订单列表的第j个
                  if (img[j] != null) {
                    simg[i] = img[j];
                  }
                }
              }
          }
          that.setData({
            worderlist: res.data.OrderList,
            wserviceImg: simg,
            wimgId: simgid
          })
        }
      }
    })
  },

  cancelShow: function() {
    var that = this;
    //查询用户已取消的订单
    wx.request({
      url: app.globalData.serviceUrl + '/SearchStreet/order/getOrderlistAndServicebyuo?userId=' + that.data.userId + '&orderStatus=3',
      data: {},
      method: "GET",
      success: res => {
        console.log(res);
        if (res.data.success) {
            var order = res.data.OrderList;
            var img = that.data.serviceImg;
            var cimg = [];
            var id = that.data.imgId;
            var cimgid = [];
            var service = res.data.serviceList;
            for (var i = 0; i < order.length; i++) {
              var time = JSON.stringify(order[i].createTime);
              order[i].createTime = util.formatDate(time);
              if (order[i].overTime != null) {
                time = JSON.stringify(order[i].overTime);
                order[i].overTime = util.formatDate(time);
              } else
                order[i].overTime = '';
                for (var j = 0; j < img.length; j++) {
                  if (id[j] == order[i].orderId) {
                    cimgid[i] = j;
                    if (img[j] != null) {
                      cimg[i] = img[j];
                    }
                  }
                }
           }
            that.setData({
              corderlist: res.data.OrderList,
              cserviceImg: cimg,
              cimgId: cimgid
            })
        }
      }
    })
  },
  /*
   * 无法正确的到正确的评价(当一项服务被商家删除后评价依然存在)
   */
  commentShow: function() {
    var that = this;
    //查询用户的评论
    wx.request({
      url: app.globalData.serviceUrl + '/SearchStreet/shopComment/getshopCommentlistbyuid?userId=' + that.data.userId,
      data: {},
      method: "GET",
      success: res => {
        console.log(res);
        if (res.data.success) {
          var shopComment = res.data.shopCommentList;
          var service = res.data.serviceList;
          var img = [];
          for (var i = 0; i < shopComment.length; i++) {
            if (shopComment[i].commentReply == null)
              shopComment[i].commentReply = '无';
            /*服务不存在时候的措施*/   //图片为服务不存在
            if(service[i]!=null)
            {
              if (service[i].serviceImgAddr != null) {
                img[i] = app.globalData.imgUrl + service[i].serviceImgAddr;
             }
          }
            if (img[i] == null) {
              img[i] = "/images/nophoto.png";
            }
            
       }

          that.setData({
            shopComment: shopComment,
            sserviceImg: img,
            service: service
          })
        }
      }
    })
  },
  cancelOrder: function(e) {
    wx.showToast({
      title: '正在取消订单',
      icon: 'loading',
      duration: 1000
    })
    var that = this;
    var order = e.target.dataset.item;
    console.log(order)
    order.orderStatus = 3;
    order.createTime = new Date(order.createTime);
    if (order.overTime != '')
      order.overTime = new Date(order.overTime);
    order = JSON.stringify(order);
    wx.request({
      url: app.globalData.serviceUrl + "/SearchStreet/order/changeorderstatus",
      data: order,
      method: 'POST',
      header: {
        "Content-Type": 'application/json'
      },
      success: res => {
        console.log(res);
        if (res.data.success) {
          that.allShow();
        }
      }
    })
  },
  finishOrder: function(e) {
    var that = this;
    var order = e.target.dataset.item;
    console.log(order)
    order.orderStatus = 1;
    order.createTime = new Date(order.createTime);
    if (order.overTime != '')
      order.overTime = new Date(order.overTime);
    order = JSON.stringify(order);
    wx.request({
      url: app.globalData.serviceUrl + '/SearchStreet/order/changeorderstatus',
      data: order,
      method: 'POST',
      header: {
        "Content-Type": 'application/json'
      },
      success: res => {
        console.log(res);
        if (res.data.success) {
          wx.showToast({
            title: "订单已确认",
            icon: '',
            duration: 1500
          });
          that.allShow();
        }
      }
    })
  },
  addComment: function(e) {
    var that = this;
    var service = that.data.service[e.target.dataset.id];
    var order = e.target.dataset.item;
      wx.navigateTo({
        url: '../comment/comment?service=' + JSON.stringify(service) + '&order=' + JSON.stringify(order),
      })
   // that.allShow()
  },
  serviceDetail: function(e) {
    var that = this;
    var service = that.data.service[e.currentTarget.dataset.id];
    console.log(e.currentTarget.dataset.id);
    if (service == null) {
      wx.showModal({
        title: '提示',
        content: "该服务已被商家删除",
        showCancel: false,
        success(res) {
          if (res.confirm) {
            console.log('用户点击确定')
          }
        }
      });
    } else {
      wx.navigateTo({
        url: '../service/service?service=' + JSON.stringify(service),
      })
    }
  }
})