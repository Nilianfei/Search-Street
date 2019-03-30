var app=getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    starIndex1: 0,
    starIndex2: 0,
    starIndex3: 0,
    texts: "至少15个字",
    min: 15,
    max: 500, 
    currentWordNumber:0,
    helpcomment_imgs:[],
    comment_content:null,
    id:0,
    helpId:0,
  },
  /*改变评分星星*/
  onChange1(e) {
    const index = e.detail.index;
    this.setData({
      'starIndex1': index
    })
  },
  onChange2(e) {
    const index = e.detail.index;
    this.setData({
      'starIndex2': index
    })
  }, 
  onChange3(e) {
    const index = e.detail.index;
    this.setData({
      'starIndex3': index
    })
  },
 
  //字数限制  
  inputs: function (e) {
    // 获取输入框的内容
    var value = e.detail.value;
    // 获取输入框内容的长度
    var len = parseInt(value.length);

    //最少字数限制
    if (len <= this.data.min)
      this.setData({
        texts: "加油，写够15个字才能发布哦"
      })
    else if (len > this.data.min)
      this.setData({
        texts: " "
      })

    //最多字数限制
    if (len > this.data.max) return;
    // 当输入框内容的长度大于最大长度限制（max)时，终止setData()的执行
    this.setData({
      currentWordNumber: len, //当前字数
      comment_content:value,
    });
  },

/* 选择图片函数和预览图片函数 */
  chooseImage: function () {
    var that = this;
    var helpcomment_imgs = this.data.helpcomment_imgs;

    if (this.data.helpcomment_imgs.length < 9) {
      wx.chooseImage({
        count: 9,  //最多可以选择的图片总数  
        sizeType: ['compressed','original'], // 可以指定是原图还是压缩图，默认二者都有  
        sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
        success: function (res) {
          //console.log(res)
          //var imgsrc = res.tempFilePaths
          helpcomment_imgs = helpcomment_imgs.concat(res.tempFilePaths);
          that.setData({
            helpcomment_imgs: helpcomment_imgs,
            //imgsrc:imgsrc
          })
          console.log(helpcomment_imgs);
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
  previewImage: function (e) {
    var current = e.target.dataset.src

    wx.previewImage({
      current: current,
      urls: this.data.helpcomment_imgs
    })
  },
/* 提交表单 */
  formSubmit:function(e){
  var that = this;

  if(that.data.starIndex1== 0) {
  wx.showModal({
    title: '提示',
    content: '请为此次帮助的完成度打分',
  })
} else if (that.data.starIndex2== 0) {
  wx.showModal({
    title: '提示',
    content: '请为此次帮助的效率打分',
  })
} else if (that.data.starIndex3== 0) {
    wx.showModal({
      title: '提示',
      content: '请为此次帮助的态度打分',
    })
 } else {
  //console.log('form发生了submit事件，携带数据为：', e.detail.value);
  //var that = this;
  var token = null;
  try {
    const value = wx.getStorageSync('token')
    if (value) {
      token = value;
      console.log(token);
    }
  } catch (e) {
    console.log("error");
  }
  wx.request({
    url: app.globalData.serviceUrl + "/SearchStreet/help/commenthelp?token=" + token+'&appealId'+id+'&helpId',
    data: {
      completion: that.data.starIndex1,
      efficiency: that.data.starIndex2,
      attitude:that.data.starIndex3,
    },
    method: "POST",
    success: res => {
      console.log(res);
      if (res.data.success) {
      
      } else {
        if (res.data.errMsg == "token为空" || res.data.errMsg == "token无效") {
          wx.redirectTo({
            url: '../../page/login/login'
          })
        }
      }
    },
  })
 }},
  
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log(options);
    this.setData({
      id:options.id,
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