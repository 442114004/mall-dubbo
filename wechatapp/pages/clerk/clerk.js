// pages/order-detail/order-detail.js
var api = require('../../api.js');
var app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
      imgServer :  api.default.img_server,

      order: null,
    getGoodsTotalPrice: function () {
      return this.data.order.total_price;
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var page = this;
    page.setData({
      store: wx.getStorageSync('store'),
      user_info: wx.getStorageSync("user_info")
    });
    if (page.data.user_info.is_clerk != 1) {
      wx.showModal({
        title:'警告！',
        showCancel:false,
        content:'您不是核销员无法核销订单！！',
        confirmText:'确认',
        success:function(res){
          if (res.confirm) {
            wx.switchTab({
              url: '/pages/index/index',
            })
          }
        }
      });
    } else {
      wx.showLoading({
        title: "正在加载",
      });
      app.request({
        url: api.order.clerk_detail,
        data: {
          order_no: options.scene,
        },
        success: function (res) {
          if (res.code == 200) {
            page.setData({
              order: res.data,
            });
          } else {
            wx.showModal({
              title: '警告！',
              showCancel: false,
              content: '订单不存在',
              confirmText: '确认',
              success: function (res) {
                if (res.confirm) {
                  wx.switchTab({
                    url: '/pages/index/index',
                  })
                }
              }
            });
          }
        },
        complete: function () {
          wx.hideLoading();
        }
      });
    }
  },
  clerk:function(e){
    var page = this; 
    console.log(page.data.order)
    wx.showModal({
      title: '提示',
      content: '是否确认核销？',
      success: function (res) {
        if (res.confirm) {
          wx.showLoading({
            title: "正在加载",
          });
          app.request({
            url: api.order.clerk,
            data: {
              order_id: page.data.order.order_id
            },
            success: function (res) {
              if (res.code == 200) {
                wx.switchTab({
                  url: '/pages/user/user',
                  success: function (e) {
                    console.log(e)
                  }, fail: function (e) {
                    console.log(e)
                  }
                })
              } else {
                wx.showModal({
                  title: '警告！',
                  showCancel: false,
                  content: res.msg,
                  confirmText: '确认',
                  success: function (res) {
                    if (res.confirm) {
                      wx.switchTab({
                        url: '/pages/index/index',
                      })
                    }
                  }
                });
              }
            },
            complete: function () {
              wx.hideLoading();
            }
          });
        } else if (res.cancel) {
        }
      }
    })
  }

});