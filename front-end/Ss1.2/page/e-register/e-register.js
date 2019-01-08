var initData='添加店铺环境或菜品图片审核通过率会高哦'
// 引入SDK核心类
var QQMapWX = require('../../util/qqmap-wx-jssdk.min.js');

// 实例化API核心类
var qqmapsdk = new QQMapWX({
  key: 'X2DBZ-4TCHU-T43VA-BRN5Y-T7LY7-MVBXT' // 必填
});
Page({
  data:{
    text:initData,
    region: ['广东省', '广州市', '海珠区'],
    customItem: '全部',
    imgsrc:null,
    img_arr:[],
    flag:true,
    imageList:[],
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
    }
  },
  chooseImage: function () {
    var that = this,
    imageList = this.data.imageList
    
    if(this.data.imageList.length<3){
    wx.chooseImage({
      count: 3,  //最多可以选择的图片总数  
      sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有  
      sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
      success: function (res) {
        console.log(res)
        var imgsrc = res.tempFilePaths
        imageList = imageList.concat(res.tempFilePaths)
        that.setData({
          imageList: imageList,
          imgsrc:imgsrc
        })
        wx.uploadFile({
          url: '',    //服务器地址
          filePath: imgsrc[0],
          name: 'file',
          formData:null,
          success(resp) {
            if (resp.statusCode == 200){
              console.log(resp.data);
              wx.showModal({
                title: '提示',
                content: '提交成功!'
              })
            }
          }
        })
      }
    })
    }else{
      wx.showToast({
        title: '最多上传3张图片',
        icon: 'loading',
        duration: 3000
      })
    }
   console.log(imageList)
  },
  previewImage: function (e) {
    var current = e.target.dataset.src

    wx.previewImage({
      current: current,
      urls: this.data.imageList
    })
  },
  bindRegionChange: function (e) {
    console.log('picker发送选择改变，携带值为', e.detail.value)
    this.setData({
      region: e.detail.value
    })
  },
  upimg: function () {
    var that = this;
    var flag=this.data.flag;
      wx.chooseImage({
        sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有  
        sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
        success: function (res) {
          console.log(res.tempFilePaths)
          that.setData({
            img_arr: that.data.img_arr.concat(res.tempFilePaths),
            flag:!flag
          })
        }
      })
  },
  sformSubmit(e){
    var _this = this;
    //调用地址解析接口
    qqmapsdk.geocoder({
      //获取表单传入地址
      address: e.detail.value.geocoder, //地址参数，例：固定地址，address: '北京市海淀区彩和坊路海淀西大街74号'
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
      },
      fail: function (error) {
        console.error(error);
      },
      complete: function (res) {
        console.log(res);
      }
    })
  },
  formSubmit:function(e){
    console.log('form发生了submit事件，携带数据为：', e.detail.value);
    var that = this;
    var form_data = e.detail.value;
    wx.uploadFile({
      url: util.apiUrl + 'Index/formsubmit?program_id=' + app.jtappid,  //服务器地址
      filePath:img_arr[0],
      name:logoimg,
      formData:form_data,
      success: function (res) {
        if (resp.statusCode == 200) {
          console.log(res.data)
          wx.showToast({
            icon: "success",
            duration: 1000
          });
          setTimeout(function () {
            wx.navigateBack({});
          }, 1000);
        }
      }, fail: function (error) {
        console.error(error);
      }
    })
  }
})