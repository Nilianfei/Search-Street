var initData='添加店铺环境或菜品图片审核通过率会高哦'
Page({
  data:{
    text:initData,
    imageList: [],
    region: ['广东省', '广州市', '海珠区'],
    customItem: '全部',
    imgsrc:null
  },
  chooseImage: function () {
    var that = this,
    imageList = this.data.imageList

    wx.chooseImage({
      count: 4,  //最多可以选择的图片总数  
      sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有  
      sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
      success: function (res) {
        console.log(res)
        var imgsrc = res.tempFilePaths
        imageList=imageList.concat(imgsrc)

        that.setData({
          imageList: imageList,
          imgsrc:imgsrc
        })
      }
    })
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
  }
})