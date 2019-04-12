// pages/comment/comment.js
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    bf:true,
    imgUrl:app.globalData.imgUrl,
    shopComment: {
      "shopId": 0,
      "orderId": 0,
      "userId": 0,
    "commentContent": null,
    "serviceRating": 0,
    "starRating": 0,
      "commentReply": null
    },
    shopname: '店名',
    userInfo : {},
    evaluate_content: ['服务评分','星级评分'],
    startext: [' ', ' '],
    flag: [0, 0],
    stardata: [1, 2, 3, 4, 5],
    noteMaxLen: 300, // 最多放多少字
    texts: "至少输入5个字",
    noteMinLen: 5,
    info: "",
    noteNowLen: 0,//备注当前字数
    service:null,
    order:null

  },
  // 监听字数
  bindTextAreaChange: function (e) {
    var that = this;
    var value = e.detail.value;
    var bf = true;
    var text= '至少输入5个字';
    var len = parseInt(value.length);
    var llen=5-len;
    if (len > that.data.noteMaxLen)
        return;
    if (len < that.data.noteMinLen&&len!=0)
      text = '还需输入' + String(llen) +'个字';
    if (len >= that.data.noteMinLen)
    {
        text = String(len) + '/' + String(that.data.noteMaxLen);
          bf=false;
    }
    that.setData({ info: value, bf: bf,texts:text })

  },
  formReset: function (e) 
  {
    wx.navigateBack({
      url: '../user-order-list/user-order-list'
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
          wx.navigateBack({
            url: '../user-order-list/user-order-list'
          })
        }
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
   var that=this;
    var service = JSON.parse(options.service);
    console.log(options.service)
    var order =JSON.parse(options.order);
    var shopComment=that.data.shopComment;
    if(service!=null)
    shopComment.shopId=service.shopId;
    shopComment.userId=order.userId;
    shopComment.orderId=order.orderId;
   that.setData({
         service:service,
         order:order,
         shopComment:shopComment
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