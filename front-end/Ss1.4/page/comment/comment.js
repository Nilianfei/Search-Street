// pages/comment/comment.js
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    shopComment: {
      "shopId": 2,
      "orderId": 13,
      "userId": 1,
    "commentContent": "",
    "serviceRating": 0,
    "starRating": 0,
      "commentReply": ""
    },
    shopname: '店名',
    shopid:2,//获取shopid
    serviceid:1, //获取serviceid
    orderid: 1,//获取orderid
    userInfo : {},
    evaluate_content: ['服务评分','星级评分'],
    startext: [' ', ' '],
    flag: [0, 0],
    stardata: [1, 2, 3, 4, 5],
    noteMaxLen: 300, // 最多放多少字
    texts: "至少输入8个字",
    noteMinLen: 8,
    info: "",
    noteNowLen: 0,//备注当前字数

  },
  // 监听字数
  bindTextAreaChange: function (e) {
    var that = this;
    var value = e.detail.value;
    var text= '至少输入8个字';
    var len = parseInt(value.length);
    var llen=8-len;
    if (len > that.data.noteMaxLen)
        return;
    if (len < that.data.noteMinLen&&len!=0)
      text = '还需输入' + String(llen) +'个字';
    if (len >= that.data.noteMinLen)
      text = String(len) + '/' + String(that.data.noteMaxLen);
    that.setData({ info: value, texts:text })

  },
  formReset: function (e) 
  {
    wx.redirectTo({
      url: '../order/order'
    })
  },
  formSubmit: function (e) {
    var that = this;
    that.data.shopComment.commentContent = that.data.info;
    var score = parseInt(that.data.flag[0]);
    score = score * 20;
    that.data.shopComment.serviceRating = score;
    that.data.shopComment.starRating = that.data.flag[1];
    that.setData({
      shopComment: that.data.shopComment
    })
    wx.request({
      url: app.globalData.serviceUrl + '/SearchStreet/shopComment/addshopComment',
      data: JSON.stringify(that.data.shopComment),
      method: 'POST',
      header: {
        "Content-Type": 'application/json'
      },
      success: function (res) {
        var result = res.data.success;
        console.log('errMsg:' + res.data.errMsg)
        console.log('shopComment:' + res.data.shopComment.serviceRating)
        console.log('shopCommentId:' + res.data.shopCommentId)
        var toastText = "添加评论成功";
        if (result != true) {
          toastText = "添加评论失败：" + res.data.errMsg;
        }
        wx.showToast({
          title: toastText,
          icon: '',
          duration: 2000
        });
        if (result == true) {
          wx.redirectTo({
            url: '../personal-center/personal-center'
          })
        }
      }
    })
  },
  // 提交事件
  submit_evaluate: function (e) {
    var that = this;
    that.data.shopComment.commentContent = that.data.info;
    that.data.shopComment.serviceRating = that.data.flag[0];
    that.data.shopComment.starRating = that.data.flag[1];
    that.setData({
      shopComment: that.data.shopComment
    })
    wx.request({
      url: app.globalData.serviceUrl + '/SearchStreet/shopComment/addshopComment',
      header: {
        "Content-Type": 'application/json'
      },
      data: { "shopComment": e.target.dataset.shopComment },
      method: 'POST',
      success: function (res) {
        var result = res.data.success;
        console.log('errMsg:' + res.data.errMsg)
        console.log('shopComment:' + res.data.shopComment.serviceRating)
        console.log('shopCommentId:' + res.data.shopCommentId)
        if (result == true) {
         
        }
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    wx.getUserInfo({
      success: res => {
        app.globalData.userInfo = res.userInfo
        this.setData({
          userInfo: res.userInfo
        })
      }
    })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /*改变评分星星*/
   onChange: function (e) {

     var index = e.currentTarget.dataset.no;

     var num = e.detail.index;

    var a = 'flag[' + index + ']';

    var b = 'startext[' + index + ']';

    var that = this;

    if (num == 1) {

      that.setData({

        [a]: 1,

        [b]: '非常不满意',

      });

    } else if (num == 2) {

      that.setData({

        [a]: 2,

        [b]: '不满意'

      });

    } else if (num == 3) {

      that.setData({

        [a]: 3,

        [b]: '一般'

      });

    } else if (num == 4) {

      that.setData({

        [a]: 4,

        [b]: '满意'

      });

    } else if (num == 5) {

      that.setData({

        [a]: 5,

        [b]: '非常满意'

      });

    }

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