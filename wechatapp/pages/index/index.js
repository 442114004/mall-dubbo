
var api = require('../../api.js');
var app = getApp();
var share_count = 0;
Page({
    data: {
       imgServer :  api.default.img_server,
      x:wx.getSystemInfoSync().windowWidth,
      y:wx.getSystemInfoSync().windowHeight
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
      var access_token = wx.getStorageSync("access_token");
      console.log('token:' + access_token);
      if (!access_token)
      app.login();
      console.log('login-token:' + access_token);
        this.loadData(options);
        var page = this;
        var parent_id = 0;
        var user_id = options.user_id;
        var scene = decodeURIComponent(options.scene);
        if (user_id != undefined) {
            parent_id = user_id;
        }
        else if (scene != undefined) {
            parent_id = scene;
        }
        app.loginBindParent({parent_id: parent_id});
    },

    /**
     * 加载页面数据
     */
    loadData: function (options) {
        var page = this;
        app.request({
            url: api.default.index,
            success: function (res) {
                if (res.code == 200) {
                    page.setData(res.data);
                }
            },
            complete: function () {
                wx.stopPullDownRefresh();
            }
        });

    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {
        share_count = 0;
        var store = wx.getStorageSync("store");
        if (store && store.name) {
            wx.setNavigationBarTitle({
                title: store.name,
            });
        }
    },

    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh: function () {
        this.loadData();
    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage: function (options) {
        var page = this;
        var user_info = wx.getStorageSync("user_info");
        return {
            path: "/pages/index/index?user_id=" + user_info.id,
            success: function (e) {
                share_count++;
                if (share_count == 1)
                    app.shareSendCoupon(page);
            }
        };
    },
    receive: function (e) {
        var page = this;
        var id = e.target.dataset.index;
        wx.showLoading({
            mask: true,
        });
        if (!page.hideGetCoupon) {
            page.hideGetCoupon = function (e) {
                var url = e.currentTarget.dataset.url || false;
                page.setData({
                    get_coupon_list: null,
                });
                if (url) {
                    wx.navigateTo({
                        url: url,
                    });
                }
            };
        }
        app.request({
            url: api.coupon.receive,
            data: {id: id},
            success: function (res) {
                wx.hideLoading();
                if (res.code == 200) {
                    page.setData({
                        get_coupon_list: res.data.list,
                        coupon_list: res.data.coupon_list
                    });
                } else {
                    wx.showToast({
                        title: '已领取',
                        duration: 2000
                    })
                    page.setData({
                        coupon_list: res.data.coupon_list
                    });
                }
            },
            // complete: function () {
            //   wx.hideLoading();
            // }
        });
    },

    navigatorClick: function (e) {
        var page = this;
        var open_type = e.currentTarget.dataset.open_type;
        var url = e.currentTarget.dataset.url;
        if (open_type != 'wxapp')
            return true;
        //console.log(url);
        url = parseQueryString(url);
        url.path = url.path ? decodeURIComponent(url.path) : "";
        console.log("Open New App");
        wx.navigateToMiniProgram({
            appId: url.appId,
            path: url.path,
            complete: function (e) {
                console.log(e);
            }
        });
        return false;

        function parseQueryString(url) {
            var reg_url = /^[^\?]+\?([\w\W]+)$/,
                reg_para = /([^&=]+)=([\w\W]*?)(&|$|#)/g,
                arr_url = reg_url.exec(url),
                ret = {};
            if (arr_url && arr_url[1]) {
                var str_para = arr_url[1], result;
                while ((result = reg_para.exec(str_para)) != null) {
                    ret[result[1]] = result[2];
                }
            }
            return ret;
        }
    },
    closeCouponBox: function (e) {
      this.setData({
        get_coupon_list: ""
      });
    }

});
