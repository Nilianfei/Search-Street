// page/index/index.js
var app = getApp();
Page({
  
  /**
   * 页面的初始数据
   */
  data: {
    open:false,
    token:null,
    background:"/images/index-background.jpg",
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    let base64 = wx.getFileSystemManager().readFileSync(this.data.background, 'base64');
    that.setData({
      'background': 'data:image/jpg;base64,' + base64
    });
    wx.checkSession({
      success() {         // session_key 未过期，并且在本生命周期一直有效
        // 查看是否授权
        wx.getSetting({
          success: function (res) {
            console.log(res)
            if (res.authSetting['scope.userInfo']) {
              wx.getUserInfo({
                success: function (res) {
                  var token = null;
                  try {
                    const value = wx.getStorageSync('token')
                    if (value) {
                      token = value;
                    }
                  } catch (e) {
                    console.log("error");
                  }
                  that.setData({
                    token: token
                  });
                  wx.request({
                    url: app.globalData.serviceUrl + '/SearchStreet/wechat/getUserInfo',
                    header: {
                      token: token
                    },
                    success: function (res) {
                      // 拿到自己后台传过来的数据，自己作处理
                      console.log(res.data);
                      if (null != res.data.success && res.data.success) {
                        //用户登录成功
                        wx.setStorage({
                          key: 'userId',
                          data: res.data.personInfo.userId
                        });
                      }else {
                        wx.redirectTo({
                          url: '../../page/login/login'
                        })
                      }
                    }
                    , fail: function (err) {
                      console.log(err)
                    }
                  })         
                  /*console.log(res)
                  var token="dfef"
                  try {
                    const value = wx.getStorageSync('shopId')
                    if (token) {
                      wx.showModal({
                        title: '提示',
                        content: '检测到您已经进行过商铺注册，是否直接进入后台中心？',
                        showCancel: true,
                        cancelText: '否',
                        confirmText: '是',
                        success: function(res) {
                          if(res.confirm)
                          {
                            wx.navigateTo({
                              url: '../shop-beinfo/shop-beinfo',
                            })
                          }
                        }
                      })
                    }
                  } catch (e) {
                    console.log("error");
                  }*/
                }
              });
            }
            else {
              wx.redirectTo({
                url: '../../page/login/login'
              })
            }

          }
        })
      },
      fail: function(res){
        wx.redirectTo({
          url: '../../page/login/login'
        })
      }
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
  kindToggle1: function (e) {
    var open1 = this.data.open1;
    open1=!open1;
    this.setData({
      open1: open1
    });
  },
  kindToggle2: function (e) {
    var open2 = this.data.open2;
    open2 = !open2;
    this.setData({
      open2: open2
    });
  },
  searchstore:function(){
    wx.navigateTo({
        url: '../search-shop/search-shop',
      })
  }
})