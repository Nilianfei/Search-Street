// page/index/index.js
Page({
  
  /**
   * 页面的初始数据
   */
  data: {
    open:false
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    wx.checkSession({
      success() {         // session_key 未过期，并且在本生命周期一直有效
        // 查看是否授权
        wx.getSetting({
          success: function (res) {
            console.log(res)
            if (res.authSetting['scope.userInfo']) {
              wx.getUserInfo({
                success: function (res) {
                  console.log(res)
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
                            wx.redirectTo({
                              url: '../shop-beinfo/shop-beinfo',
                            })
                          }
                        }
                      })
                    }
                  } catch (e) {
                    console.log("error");
                  }
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
  kindToggle: function (e) {
    var open = this.data.open;
    open=!open;
    this.setData({
      open: open
    });
  }
})