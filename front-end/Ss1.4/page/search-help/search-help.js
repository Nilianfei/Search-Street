var shelp_text='添加关于求助的图片会有更多人愿意帮助你哦'
Page({

  /**
   * 页面的初始数据
   */
  data: {
    shelp_text:shelp_text,
    shelp_imgs:[],
    show_photo:true,
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
  },

  /* 上传求助图片 */
  chooseImage: function () {
    var that = this;
    var shelp_imgs = this.data.shelp_imgs;
    var flag=this.data.show_photo;

    if (this.data.shelp_imgs.length < 3) {
      wx.chooseImage({
        count: 3,  //最多可以选择的图片总数  
        sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有  
        sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
        success: function (res) {
          shelp_imgs = shelp_imgs.concat(res.tempFilePaths);
          that.setData({
            shelp_imgs: shelp_imgs,
            show_photo:!flag,
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