// pages/share-qrcode/share-qrcode.js
var api = require('../../api.js');
var app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
      imgServer :  api.default.img_server,

      qrcode:""
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var page = this;
    var setting = wx.getStorageSync("share_setting")
    page.setData({
      qrcode: setting.qrcode_bg
    });
    var qrcode = wx.getStorageSync("qrcode");
    if (qrcode) {
      console.log(qrcode)
      console.log(111)
      page.setData({
        qrcode: qrcode
      });
    } else {
      wx.showLoading({
        title: "正在加载",
        mask: true,
      });
      app.request({
        url: api.share.get_qrcode,
        success: function (res) {
          wx.setStorageSync("qrcode", res.data);
          page.setData({
            qrcode: res.data
          });
        },
        complete: function () {
          wx.hideLoading();
        }
      });
    }
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
    var page = this;
    var user_info = wx.getStorageSync("user_info");
    this.setData({
      user_info: user_info,
    });
  },

  click:function(){
    var page = this;
    wx.previewImage({
      current: page.data.qrcode,
      urls: [page.data.qrcode]
    })
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
})