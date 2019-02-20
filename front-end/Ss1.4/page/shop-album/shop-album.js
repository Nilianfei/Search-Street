// page/shop-album/shop-album.js



Page({

  /**
   * 页面的初始数据
   */
  data: {

    current: 'tab1',

    // 相册列表数据
    albumList: [],

    // 图片布局列表（二维数组，由`albumList`计算而得）
    layoutList: [],

    // 布局列数
    layoutColumnSize: 3,

    // 是否显示loading
    showLoading: false,

    // loading提示语
    loadingMessage: '',

    // 是否显示toast
    showToast: false,

    // 提示消息
    toastMessage: '',

    // 是否显示动作命令
    showActionsSheet: false,

    // 当前操作的图片
    imageInAction: '',

    // 图片预览模式
    previewMode: false,

    // 当前预览索引
    previewIndex: 0,
  },

  handleChange({ detail }) {
    this.setData({
      current: detail.key
    });
  },

  //隐藏loading提示
  hideLoading(){
    this.setData({ showLoading: false,loadingMessage:''});
  },

  //上传图片
  upimg: function () {
    var that = this;
    var flag = this.data.flag;
    wx.chooseImage({
      sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有  
      sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
      success: function (res) {
        that.setData({
          business_img: res.tempFilePaths,
          flag: !flag
        })
      }
    })
  },
  
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },

  //获取albumList
  getAlblumList(){
    this.showLoading('加载列表中');
    setTimeout(() =>this.hideLoading(), 1000);
    return 
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