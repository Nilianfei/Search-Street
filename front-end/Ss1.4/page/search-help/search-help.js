var util=require('../../util/util.js');

var app=getApp();
var shelp_text='添加关于求助的图片会有更多人愿意帮助你哦'
// 引入SDK核心类
var QQMapWX = require('../../util/qqmap-wx-jssdk.min.js');
//引入md5工具类
var md5 = require('../../util/md5.min.js');
// 实例化API核心类
var qqmapsdk = new QQMapWX({
  key: 'GTPBZ-3HY35-YSDIY-Q4O5T-5SSTQ-YOBGM' // 必填
});
Page({

  /**
   * 页面的初始数据
   */
  data: {
    shelp_text:shelp_text,
    shelp_imgs:[],
    shelpCost:null,
    shelpTimelimit:null,
    cost:['10','20','40','80'],
    timelimit:['30分钟','60分钟','90分钟','120分钟'],
    region: ['广东省', '广州市', '海珠区'],
    customItem: '全部',
    latitude: null,
    longitude: null,
    markers: [{
      id: 0,
      title: 'T.I.T 创意园',
      latitude: 23.099994,
      longitude: 113.324520,
      iconPath: '../../images/定位.png',//图标路径
      width: 40,
      height: 40,
    }],
    poi: {
      latitude: 23.099994,
      longitude: 113.324520
    },
    errorMsg:'此为必填选项哦',
    errorMsgs: {
      title_error: null,
      content_error: null,
      phone_error: null
    }
  },

  /* 上传求助图片 */
  chooseImage: function () {
    var that = this;
    var shelp_imgs = this.data.shelp_imgs;
  

    if (this.data.shelp_imgs.length < 3) {
      wx.chooseImage({
        count: 3,  //最多可以选择的图片总数  
        sizeType: ['compressed','original'], // 可以指定是原图还是压缩图，默认二者都有  
        sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
        success: function (res) {
          shelp_imgs = shelp_imgs.concat(res.tempFilePaths);
          
          that.setData({
            shelp_imgs: shelp_imgs,
            
          })
          console.log(shelp_imgs);
          
        }
      })
    } else {
      wx.showToast({
        title: '最多上传3张图片',
        icon: 'loading',
        duration: 2000
      })
    }
  },

  /*预览上传的求助图片*/
previewImage:function (e){
  var current=e.target.dataset.src

  wx.previewImage({
    current: current,
    urls: this.data.shelp_imgs,
   
  })
},
/*  接受triggerEvent 方法触发的自定义组件事件来更新同步数据 */
  getCost: function (e) {
    console.log(e.detail)
    this.setData({
    shelpCost:e.detail.text
    })
  },
  getTime: function (e) {
    console.log(e.detail)
    this.setData({
      shelpTimelimit: e.detail.text
    })
    console.log(parseInt(this.data.shelpTimelimit));
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
            iconPath: '../../images/定位.png',//图标路径
            width: 20,
            height: 20,
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
  },
  
  /*整张表单上传*/
  formSubmit: function (e) {
    var that = this;
    var errorMsg = this.data.errorMsg;
    //console.log(e.detail.text);
    if (e.detail.value.shelpTitle.length == 0) {
      this.setData({
        errorMsgs: {
          title_error: errorMsg
        }
      })
    } else if (e.detail.value.shelpContent.length == 0) {
      this.setData({
        errorMsgs: {
          content_error: errorMsg
        }
      })
    } else if (that.data.shelpCost==null) {                    
       wx.showModal({
         title: '提示',
         content: '请您填写搜币值',
       })
    } else if (that.data.shelpCost==null) {   /*获取用户当前钱包中搜币的数目，与之比较*/
      wx.showModal({
        title: '提示',
        content: '您的搜币不足，请前往充值',
      })
     } else if(that.data.shelpTimelimit==null){
      wx.showModal({
        title: '提示',
        content: '请您填写有效时间',
      })
     }else if (e.detail.value.shelpPhone.length == 0) {
      this.setData({
        errorMsgs: {
          phone_error: errorMsg
        }
      })
    } else if (e.detail.value.fullAddress.length == 0) {
      wx.showModal({
        title: '提示',
        content: '请输入您的详细地址',
      })
    } else {
      console.log('form发生了submit事件，携带数据为：', e.detail.value);
      var date = new Date();
      //获取当前时间戳
      var timestamp = Date.parse(new Date());
      timestamp = timestamp / 1000;
      console.log("当前时间戳为：" + timestamp);
      //获取结束时间戳
      var end_timetamp = timestamp + parseInt(this.data.shelpTimelimit) * 60;
      var n_to = end_timetamp * 1000;
      var end_date = new Date(n_to);
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
        url: app.globalData.serviceUrl + "/SearchStreet/appeal/registerappeal?token=" + token,      //完整的发布帮助的url
        data: {
          appealTitle: e.detail.value.shelpTitle,
          appealContent:e.detail.value.shelpContent,
          phone: e.detail.value.shelpPhone,
          souCoin:that.data.shelpCost,
          startTime:date,
          endTime:end_date,
          province: that.data.region[0],
          city: that.data.region[1],
          district: that.data.region[2],
          fullAddress: e.detail.value.fullAddress,
          longitude: that.data.longitude,
          latitude: that.data.latitude,
          appealMoreInfo: e.detail.value.shelpMoreInfo,
        },
        method: "POST",
        success: res => {
          console.log(res);
          if (res.data.success) {
            wx.setStorage({                 /*根据后台保存该表单的key值来存*/
              key: 'appealId',
              data: res.data.appealId
            })
            var date = new Date();
            var url = app.globalData.serviceUrl + "/SearchStreet/appeal/uploadimg?appealId=" + res.data.appealId + "&createTime=" + app.timeStamp2String(date) +"&token=" + token;          
            //后台保存用户发布帮助的图片的url
            for (var i = 0; i < that.data.shelp_imgs.length; i++) {
              app.uploadAImg({
                url: url,
                filePath: that.data.shelp_imgs[i],
                fileName: "appealImgList"
              })
            }
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
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    
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
    
  }
})