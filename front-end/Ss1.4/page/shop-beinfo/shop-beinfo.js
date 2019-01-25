
var test_name='华南师范大学小卖部石牌校区旗舰店'
var test_pic='../../images/search_buck.png'
var score=80;
var score_rank=5;
score_rank=score_rank.toFixed(1);
var score_rate=0.8;

Page({

  /**
   * 页面的初始数据
   */
  data: {

    shopname:test_name,
    shoppic:test_pic,
    score_number0: score,
    score_number1: score_rank,
    score_number2: score_rate,
    /*
    shopname:null,
    shoppic:null,
    */
    time:0,
    number:0,

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    /*
    var token = null;
    try {
      const value = wx.getStorageSync('shopId')
      if (value) {
        token = value;
      }
    } catch (e) {
      console.log("error");
    }
    wx.request({
      url: '',
      data:{
       shopId:token
      },
      method:"POST",
      success: res => {
        console.log(res);   //设置页面中的数据
        this.setData(
          {
            shopname:res.data.shopName,
            shoppic:res.data.pofileImg,
          }
        )
      }
    })
    */    
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