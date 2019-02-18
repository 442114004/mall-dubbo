// user.js
var api = require('../../api.js');
var app = getApp();
Page({

    /**
     * 页面的初始数据
     */
    data: {
        contact_tel: "",
        show_customer_service: 0,
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
    },

    loadData: function (options) {
        var page = this;
        page.setData({
            store: wx.getStorageSync('store'),
        });
        var access_token = wx.getStorageSync("access_token");
        app.request({
            url: api.user.index,
            data:{id:access_token},
            success: function (res) {
                if (res.code == 200) {
                  page.setData({user_info:res.data});
                }
            }
        });
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
        page.loadData();
    },

    callTel: function (e) {
        var tel = e.currentTarget.dataset.tel;
        wx.makePhoneCall({
            phoneNumber: tel, //仅为示例，并非真实的电话号码
        });
    },
    apply: function (e) {
        var page = this;
        var share_setting = wx.getStorageSync("share_setting");
        var user_info = wx.getStorageSync("user_info");
        if (share_setting.share_condition == 1) {
            wx.navigateTo({
                url: '/pages/add-share/index',
            })
        } else if (share_setting.share_condition == 0) {
            if (user_info.is_distributor == 0) {
                wx.showModal({
                    title: "申请成为分销商",
                    content: "是否申请？",
                    success: function (r) {
                        if (r.confirm) {
                            wx.showLoading({
                                title: "正在加载",
                                mask: true,
                            });
                            app.request({
                                url: api.share.join,
                                methods: "POST",
                                success: function (res) {
                                    if (res.code == 200) {
                                        user_info.is_distributor = 2;
                                        wx.setStorageSync("user_info", user_info);
                                    }
                                },
                                complete: function () {
                                    wx.hideLoading();
                                    wx.navigateTo({
                                        url: '/pages/add-share/index',
                                    })
                                }
                            });
                        }
                    },
                })
            } else {
                wx.navigateTo({
                    url: '/pages/add-share/index',
                })
            }
        }
    },
    verify:function(e){
      wx.scanCode({
        onlyFromCamera:false,
        success: function (res) {
          console.log(res)
          wx.navigateTo({
            url: '/'+res.path,
          })
        },fail:function(e){
          wx.showToast({
            title:'失败'
          });
        }
      });
    }
});