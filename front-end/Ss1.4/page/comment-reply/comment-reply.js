// page/comment-reply/comment-reply.js
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    bf: true,
    shopComment: {
      "shopId": 0,
      "orderId": 0,
      "userId": 0,
      "commentContent": "",
      "serviceRating": 0,
      "starRating": 0,
      "commentReply": ""
    },
    noteMaxLen: 300, // 最多放多少字
    texts: "至少输入5个字",
    noteMinLen: 5,
    info: "",
    noteNowLen: 0,//备注当前字数
    order: null

  },
  // 监听字数
  bindTextAreaChange: function (e) {
    var that = this;
    var bf=true;
    var value = e.detail.value;
    var text = '至少输入5个字';
    var len = parseInt(value.length);
    var llen = 5 - len;
    if (len > that.data.noteMaxLen)
      return;
    if (len < that.data.noteMinLen && len != 0)
      text = '还需输入' + String(llen) + '个字';
    if (len >= that.data.noteMinLen) {
      text = String(len) + '/' + String(that.data.noteMaxLen);
      bf=false;
    }
    that.setData({ info: value, bf:bf, texts: text })

  },
  formReset: function (e) {
    var that=this;
    wx.navigateBack({
      url: '../shop-order-list/shop-order-list?shopId=' + that.data.shopComment.shopId
    })
  },
  formSubmit: function (e) {
    var that = this;
    that.data.shopComment.commentReply = that.data.info;
    that.data.order.orderStatus=5;

    that.setData({
      shopComment: that.data.shopComment,
      order: that.data.order
    })
    var orderId=that.data.shopComment.orderId;
    var commentReply = that.data.info;
    var url= app.globalData.serviceUrl + '/SearchStreet/shopComment/modifyshopCommentReply?orderId=' + orderId + '&commentReply=' + commentReply;
    wx.request({
      url: url,
      method: 'POST',
      success: function (res) {
        var result = res.data.success;
        var toastText = "添加回复成功";
        if (result != true) {
          toastText = "添加回复失败：" + res.data.errMsg;
        }
        wx.showToast({
          title: toastText,
          icon: '',
          duration: 1000
        });
        if (result == true) {
          wx.navigateBack({
            url: '../shop-order-list/shop-order-list?shopId='+that.data.shopComment.shopId
          })
        }
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    var order = JSON.parse(options.order);
    var shopComment = that.data.shopComment;
    shopComment.shopId = options.shopId;
    shopComment.userId = order.userId;
    shopComment.orderId = order.orderId;
    that.setData({
      order: order,
      shopComment: shopComment
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

})