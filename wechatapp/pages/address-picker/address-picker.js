var api = require('../../api.js');
var app = getApp();
Page({

    /**
     * 页面的初始数据
     */
    data: {
        address_list: null,
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
		 var access_token = wx.getStorageSync("access_token");
        var page = this;
        wx.showNavigationBarLoading();
        app.request({
            url: api.user.address_list,
				 data:{
				  userid:access_token,
				  pageSize:15
				},
            success: function (res) {
                wx.hideNavigationBarLoading();
                if (res.code == 200) {
                    page.setData({
                        address_list: res.data,
                    });
                }
            }
        });
    },

    pickAddress: function (e) {
        var page = this;
        var index = e.currentTarget.dataset.index;
        var address = page.data.address_list[index];
        wx.setStorageSync("picker_address", address);
        wx.navigateBack();
    },

    getWechatAddress: function (e) {
        var page = this;
        wx.chooseAddress({
            success: function (e) {
                if (e.errMsg != 'chooseAddress:ok')
                    return;
                wx.showLoading();
				console.log('e.nationalCode'+e.nationalCode);
				 var access_token = wx.getStorageSync("access_token");
                app.request({
                    url: api.user.address_save,

                    data: {
						userid:access_token,
                        postCode: e.nationalCode,
                        name: e.userName,
                        phoneNumber: e.telNumber,
                        detailAddress: e.detailInfo,
                    },
                    success: function (res) {
                        console.log(res);
                        if (res.code == 1) {
                            wx.showModal({
                                title: '提示',
                                content: res.msg,
                                showCancel: false,
                            });
                            return;
                        }
                        if (res.code == 200) {
                            wx.setStorageSync("picker_address", res.data);
                            wx.navigateBack();
                        }
                    },
                    complete: function () {
                        wx.hideLoading();
                    }
                });
            }
        });
    },
});