var util=require('../../util/util.js');

var app=getApp();
var shelp_text='添加关于求助的图片会有更多人愿意帮助你哦'
// 引入SDK核心类
var QQMapWX = require('../../util/qqmap-wx-jssdk.min.js');
//引入md5工具类
var md5 = require('../../util/md5.min.js');
// 实例化API核心类
var qqmapsdk = new QQMapWX({
  key: 'DQYBZ-AQFK6-6JDSI-EHRZV-EFRCJ-TDFZU' // 必填
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

      iconPath: '../../images/locat.png',//图标路径

      width: 40,
      height: 40,
    }],
    poi: {
      latitude: 23.099994,
      longitude: 113.324520
    },
    suggestion:[],
    backfill:null,
    errorMsg:'此为必填选项哦',
    errorMsgs: {
      title_error: null,
      content_error: null,
      phone_error: null
    },
    persoucoin:0,
    minHour: 10,
    maxHour: 20,
    minDate: new Date().getTime(),
    maxDate: new Date(2019, 10, 1).getTime(),
    currentDate: new Date().getTime(),
    show: false,
    currenttime: '选择时间',
    showkeyword:false,
  },
  showtime(){
    this.setData({ show: true });
  },
  /* 弹出组件取消 */
  onClose() {
    this.setData({ show: false });
  },
/*时间选择器 */
  onInput(e) {
    console.log(e.detail);
     this.setData({
      currentDate: e.detail
    });
  },
  onConfirm(e){
    console.log(e);
  this.setData({
    show:false,
    currenttime: app.timeStamp2String(e.detail),
  })
  },
  /* 上传求助图片 */
  chooseImage: function () {
    var that = this;
    var shelp_imgs = this.data.shelp_imgs;
  

    if (this.data.shelp_imgs.length < 9) {
      wx.chooseImage({
        count: 9,  //最多可以选择的图片总数  
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
        title: '最多上传9张图片',
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

/* 选择地址改变值函数*/
  bindRegionChange: function (e) {
    console.log('picker发送选择改变，携带值为', e.detail.value)
    this.setData({
      region: e.detail.value
    })
  },

  //数据回填方法
  backfill: function (e) {
    console.log(e);
    var id = e.currentTarget.id;
    for (var i = 0; i < this.data.suggestion.length; i++) {
      if (i == id) {
        this.setData({
          backfill: this.data.suggestion[i].title
        });
        console.log(this.data.backfill);
      }
    }
    this.setData({
      showkeyword:false,
    })
  },

  //触发关键词输入提示事件
  getsuggest: function (e) {
    this.setData({
      showkeyword:true,
    })
    console.log(e);
    var _this = this;
    var _keyword=e.detail.value;
   // var sig = md5("/ws/place/v1/suggestion?keyword=" + _keyword + "&key=X2DBZ-4TCHU-T43VA-BRN5Y-T7LY7-MVBXT&output=jsonSvqdT7KldVKgkNjH86dXvaViwQzTy2X" + "&policy=0&region=" + _this.data.region[1] +"&region_fix=0");
    //console.log(sig);
    //调用关键词提示接口
    qqmapsdk.getSuggestion({
     // sig: sig,
      //获取输入框值并设置keyword参数
      keyword: _keyword, //用户输入的关键词，可设置固定值,如keyword:'KFC'
      region:_this.data.region[1], //设置城市名，限制关键词所示的地域范围，非必填参数
      success: function (res) {//搜索成功后的回调
        console.log(res);
        var sug = [];
        for (var i = 0; i < res.data.length; i++) {
          sug.push({ // 获取返回结果，放到sug数组中
            title: res.data[i].title,
            id: res.data[i].id,
            addr: res.data[i].address,
            city: res.data[i].city,
            district: res.data[i].district,
            latitude: res.data[i].location.lat,
            longitude: res.data[i].location.lng
          });
        }
        _this.setData({ //设置suggestion属性，将关键词搜索结果以列表形式展示
          suggestion: sug
        });
      },
      fail: function (error) {
        console.error(error);
      },
      complete: function (res) {
        console.log(res);
      }
    });
  },

  sformSubmit(e) {
    var _this = this;
    var fulladdress = _this.data.region[0] + _this.data.region[1] + _this.data.region[2] + e.detail.value.fullAddress;
    var sig = md5("/ws/geocoder/v1/?address=" + fulladdress + "&key=X2DBZ-4TCHU-T43VA-BRN5Y-T7LY7-MVBXT&output=jsonSvqdT7KldVKgkNjH86dXvaViwQzTy2X");
    console.log(sig);
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
   // console.log(that.data.shelp_imgs);
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
    } else if (e.detail.value.shelpCost==null) {                    
       wx.showModal({
         title: '提示',
         content: '请您填写搜币值',
       })
    } else if (that.data.persoucoin-e.detail.value.shelpCost<0) {   /*获取用户当前钱包中搜币的数目，与之比较*/
      wx.showModal({
        title: '提示',
        content: '您的搜币不足，请前往充值',
        confirmText:'确定充值',
        success(res) {
          if (res.confirm) {
            wx.navigateTo({
              url: '../personal-account/personal-account',
            })
          } else if (res.cancel) {
            //console.log('用户点击取消')
          }
        }
      })
     } else if(that.data.currentDate-(new Date().getTime)<=0){
      wx.showModal({
        title: '提示',
        content: '请您选择结束时间',
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
      //var end_timetamp = timestamp + parseInt(this.data.shelpTimelimit) * 60;
      //var n_to = end_timetamp * 1000;
      var end_date = new Date(that.data.currentDate);
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
          souCoin:e.detail.value.shelpCost,
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
            var url = app.globalData.serviceUrl + "/SearchStreet/appeal/uploadimg?appealId=" + res.data.appealId +"&token=" + token;          
            //后台保存用户发布帮助的图片的url
            for (var i = 0; i < that.data.shelp_imgs.length; i++) {
              app.uploadAImg({
                url: url,
                filePath: that.data.shelp_imgs[i],
                fileName: "appealImg"
              })
            }
            wx.showToast({
              title: '发布成功',
              icon: 'success',
              duration: 4000
            })
            wx.redirectTo({
              url: '../../page/index/index'
            })
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
    var token = null;
    var that=this;
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
      data: {
        token: token
      },
      success: function (res) {
        // 拿到自己后台传过来的数据，自己作处理
        console.log(res.data);
        if(res.data.success){
          that.setData({
            persoucoin:res.data.personInfo.souCoin,
          })
        }
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
    
  }
})