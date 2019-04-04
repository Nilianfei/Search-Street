var app=getApp();
Page({
  data: {
    //判断小程序的API，回调，参数，组件等是否在当前版本可用。
    canIUse: wx.canIUse('button.open-type.getUserInfo')
  },
  onLoad: function () {
    var that = this;
    
  },
  bindGetUserInfo: function (e) {
    //用户按了允许授权按钮
    var that = this;
    if (e.detail.userInfo) {
      wx.login({
        success: res => {
          console.log(res)
          var code = res.code;
          wx.request({
            url: app.globalData.serviceUrl+"/SearchStreet/wechat/login",
            data: {
              code: code,
              userInfo: e.detail.userInfo
            },
            method: "POST",
            success: res => {
              console.log(res.data.token);
              wx.setStorage({
                key: 'token',
                data: res.data.token
              })
              //授权成功后，跳转进入小程序首页
              wx.redirectTo({
                url: '../index/index'
              })
            },
            fail: function (err) {
              console.log(err)
            }
          })
        }
      });
    } else {
      //用户按了拒绝按钮
      wx.showModal({
        title: '警告',
        content: '您点击了拒绝授权，将无法进入小程序，请授权之后再进入!',
        showCancel: false,
        confirmText: '返回授权',
        success: function (res) {
          if (res.confirm) {
            console.log('用户点击了“返回授权”')
          }
        }
      })
    }
  },
  //获取用户信息接口
  queryUserInfo: function () {
    var token = null;
    try {
      const value = wx.getStorageSync('token')
      if (value) {
        token = value;
      }
    } catch (e) {
      console.log("error");
    }
    wx.request({
      url: app.globalData.serviceUrl+'/SearchStreet/wechat/getUserInfo',
      data: {
        //token: token
      },
      header:{
       'content-type':'application/json',
        'token':token
      },
      success: function (res) {
        // 拿到自己后台传过来的数据，自己作处理
        console.log(res.data);
        if(null != res.data.success && res.data.success){
          //用户登录成功
          wx.redirectTo({
            url: '../index/index'
          })
        }
      }
      ,fail:function(err){
        console.log(err)
      }
    }) 
  },
  /*login:function(){
    var that = this;
    wx.login({
      success: function (res) {
        
        wx.request({
          //获取openid接口  
          url: 'https://api.weixin.qq.com/sns/jscode2session',
          data: {
            appid: APP_ID,
            secret: APP_SECRET,
            js_code: res.code,
            grant_type: 'authorization_code'
          },
          method: 'GET',
          success: function (res) {
            console.log(res.data)
            OPEN_ID = res.data.openid;//获取到的openid  
            SESSION_KEY = res.data.session_key;//获取到session_key  
            wx.showModal({
              title: '提示注册',         
              content: '您的商铺当前还未注册，请先注册',
              success: function (res) {
                if (res.confirm) {//这里是点击了确定以后
                 // console.log('用户点击确定')
                 wx.redirectTo({
                   url: '../register/register',
                 })
                } else {//这里是点击了取消以后
                  console.log('用户点击取消')
                }
              }
            })
          }
        })
      },
      fail: function (err) {
        console.log('wx.login 接口调用失败，将无法正常使用开放接口等服务', err)
        callback(err)
      }
    })
  }  */
})  
