// goods.js

var api = require('../../api.js');
var utils = require('../../utils.js');
var app = getApp();
var WxParse = require('../../wxParse/wxParse.js');
var p = 1;
var is_loading_comment = false;
var is_more_comment = true;
var share_count = 0;
Page({

    /**
     * 页面的初始数据
     */
    data: {
        imgServer :  api.default.img_server,
        id: null,
        goods: {},
        show_attr_picker: false,
        subImages:[],
        form: {
            number: 1,
        },
        tab_detail: "active",
        tab_comment: "",
        comment_list: [],
        comment_count: {
            score_all: 0,
            score_3: 0,
            score_2: 0,
            score_1: 0,
        },
        autoplay: false,
        hide: "hide",
        show: false,
        x: wx.getSystemInfoSync().windowWidth,
        y: wx.getSystemInfoSync().windowHeight - 20
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {

        share_count = 0;
        p = 1;
        is_loading_comment = false;
        is_more_comment = true;

        var parent_id = 0;
        var user_id = options.user_id;
        console.log("options=>" + JSON.stringify(options));
        var scene = decodeURIComponent(options.scene);
        if (user_id != undefined) {
            parent_id = user_id;
        } else if (scene != undefined) {
            console.log("scene string=>" + scene);
            var scene_obj = utils.scene_decode(scene);
            console.log("scene obj=>" + JSON.stringify(scene_obj));
            if (scene_obj.uid && scene_obj.gid) {
                parent_id = scene_obj.uid;
                options.id = scene_obj.gid;
            } else {
                parent_id = scene;
            }
        }
        app.loginBindParent({parent_id: parent_id});
        var page = this;
        page.setData({
            id: options.id,
        });
        page.getGoods();
        page.getCommentList();
    },
    getGoods: function () {
        var page = this;
        var access_token = wx.getStorageSync("access_token");
        app.request({
            url: api.default.goods,
            data: {
              userid: access_token,
                id: page.data.id
            },
            success: function (res) {
                if (res.code == 200) {
                    var detail = res.data.detailHtml;
                    WxParse.wxParse("detail", "html", detail, page);
                    var subImages = res.data.albumPics;
                    var array = subImages.split(',');
                    console.log(res.data.skuStockList);
                    page.setData({
                        goods: res.data,
                        subImages: array,
                        attr_group_list: res.data.skuStockList
                    });


                }
                if (res.code == 1) {
                    wx.showModal({
                        title: "提示",
                        content: res.msg,
                        showCancel: false,
                        success: function (res) {
                            if (res.confirm) {
                                wx.switchTab({
                                    url: "/pages/index/index"
                                });
                            }
                        }
                    });
                }
            }
        });


    },
    getCommentList: function (more) {
        var page = this;
        if (more && page.data.tab_comment != "active")
            return;
        if (is_loading_comment)
            return;
        if (!is_more_comment)
            return;
        is_loading_comment = true;
        app.request({
            url: api.default.comment_list,
            data: {
                goodsId: page.data.id,
                limit: p,
            },
            success: function (res) {
                if (res.code != 0)
                    return;
                is_loading_comment = false;
                p++;
                page.setData({
                  comment_count: res.ret.evaluteEntity,
                    comment_list: more ? page.data.comment_list.concat(res.ret.list) : res.ret.list,
                });
                if (res.data.rows.length == 0)
                    is_more_comment = false;
            }
        });
    },

    onGoodsImageClick: function (e) {
        var page = this;
        var urls = [];
        var index = e.currentTarget.dataset.index;
        //console.log(page.data.goods.pic_list);
        for (var i in page.data.goods.pic_list) {
            urls.push(page.data.goods.pic_list[i].pic_url);
        }
        wx.previewImage({
            urls: urls, // 需要预览的图片http链接列表
            current: urls[index],
        });
    },

    numberSub: function () {
        var page = this;
        var num = page.data.form.number;
        if (num <= 1)
            return true;
        num--;
        page.setData({
            form: {
                number: num,
            }
        });
    },

    numberAdd: function () {
        var page = this;
        var num = page.data.form.number;
        num++;
        page.setData({
            form: {
                number: num,
            }
        });
    },

    numberBlur: function (e) {
        var page = this;
        var num = e.detail.value;
        num = parseInt(num);
        if (isNaN(num))
            num = 1;
        if (num <= 0)
            num = 1;
        page.setData({
            form: {
                number: num,
            }
        });
    },

    addCart: function () {
        this.submit('ADD_CART');
    },

    buyNow: function () {
        this.submit('BUY_NOW');
    },

    submit: function (type) {
		 var access_token = wx.getStorageSync("access_token");
        var page = this;
        if (!page.data.show_attr_picker) {
            page.setData({
                show_attr_picker: true,
            });
            return true;
        }
        if (page.data.form.number > page.data.goods.num) {
            wx.showToast({
                title: "商品库存不足，请选择其它规格或数量",
                image: "/images/icon-warning.png",
            });
            return true;
        }
        var attr_group = page.data.attr_group;

        var checked_attr_list = [];
        if (!attr_group) {
            wx.showToast({
                title: "请选择规格",
                image: "/images/icon-warning.png",
            });
            return true;
        } else {
            checked_attr_list.push({
                id: attr_group.id
            });
        }
        if (type == 'ADD_CART') {//加入购物车
            wx.showLoading({
                title: "正在提交",
                mask: true,
            });
            app.request({
                url: api.cart.add_cart,
                method: "POST",
                data: {
					userid:access_token,
                    id: attr_group.id,
                },
                success: function (res) {
                    wx.showToast({
                        title: res.msg,
                        duration: 1500
                    });
                    wx.hideLoading();
                    page.setData({
                        show_attr_picker: false,
                    });

                }
            });
        }
        if (type == 'BUY_NOW') {//立即购买
            page.setData({
                show_attr_picker: false,
            });
            wx.showLoading({
                title: "正在提交",
                mask: true,
            });
            app.request({
                url: api.cart.add_cart,
                method: "POST",
                data: {
                    userid:access_token,
                    id: attr_group.id,
                },
                success: function (res) {
                    wx.showToast({
                        title: res.msg,
                        duration: 1500
                    });
                    wx.hideLoading();
                    page.setData({
                        show_attr_picker: false,
                    });
                    console.log('cartId='+res.data);
                    wx.redirectTo({
                        url: "/pages/order-submit/order-submit?goods_info=" + JSON.stringify({
                            cartId:res.data,
                            count: page.data.form.number,
                        }),
                    });

                }
            });

        }

    },

    hideAttrPicker: function () {
        var page = this;
        page.setData({
            show_attr_picker: false,
        });
    },
    showAttrPicker: function () {
        var page = this;
        page.setData({
            show_attr_picker: true,
        });
    },

    attrClick: function (e) {

        var page = this;
        var attr_group_id = e.target.dataset.groupId; // skuCode
        var attr_id = e.target.dataset.id; // goodsid
        var attr_group_list = page.data.attr_group_list;
        var attr_group ;

        for (var i in attr_group_list) {
            if (attr_group_list[i].id == attr_id){
                attr_group_list[i].checked = true;
                attr_group=attr_group_list[i];
            }
        }
        page.setData({
            attr_group_list: attr_group_list,
            attr_group : attr_group
        });


    },


    favoriteAdd: function () {
        var page = this;
        var access_token = wx.getStorageSync("access_token");
        app.request({
            url: api.user.favorite_add,
            method: "post",
            data: {
                type:1,
                userid:access_token,
                goodsid: page.data.goods.id,
            },
            success: function (res) {
                if (res.code == 200) {
                    var goods = page.data.goods;
                    goods.is_favorite = 1;
                    page.setData({
                        goods: goods,
                    });
                }
            }
        });
    },

    favoriteRemove: function () {
        var page = this;
		var access_token = wx.getStorageSync("access_token");
        app.request({
            url: api.user.favorite_add,
            method: "post",
            data: {
                type:1,
                userid:access_token,
                goodsid: page.data.goods.id,
            },
            success: function (res) {
                if (res.code == 200) {
                    var goods = page.data.goods;
                    goods.is_favorite = 0;
                    page.setData({
                        goods: goods,
                    });
                }
            }
        });
    },

    tabSwitch: function (e) {
        var page = this;
        var tab = e.currentTarget.dataset.tab;
        if (tab == "detail") {
            page.setData({
                tab_detail: "active",
                tab_comment: "",
            });
        } else {
            page.setData({
                tab_detail: "",
                tab_comment: "active",
            });
        }
    },
    commentPicView: function (e) {
        console.log(e);
        var page = this;
        var index = e.currentTarget.dataset.index;
        var pic_index = e.currentTarget.dataset.picIndex;
        wx.previewImage({
            current: page.data.comment_list[index].pic_list[pic_index],
            urls: page.data.comment_list[index].pic_list,
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
        var page = this;
        page.getCommentList(true);
    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage: function () {
        var page = this;
        var user_info = wx.getStorageSync("user_info");
        var res = {
            path: "/pages/goods/goods?id=" + this.data.id + "&user_id=" + user_info.id,
            success: function (e) {
                console.log(e);
                share_count++;
                if (share_count == 1)
                    app.shareSendCoupon(page);
            },
            title: page.data.goods.name,
            imageUrl: page.data.goods.pic_list[0].pic_url,
        };
        return res;
    },
    play: function (e) {
        var url = e.target.dataset.url;//获取视频链接
        this.setData({
            url: url,
            hide: '',
            show: true,
        });
        var videoContext = wx.createVideoContext('video');
        videoContext.play();
    },

    close: function (e) {
        if (e.target.id == 'video') {
            return true;
        }
        this.setData({
            hide: "hide",
            show: false
        });
        var videoContext = wx.createVideoContext('video');
        videoContext.pause();
    },
    hide: function (e) {
        if (e.detail.current == 0) {
            this.setData({
                img_hide: ""
            });
        } else {
            this.setData({
                img_hide: "hide"
            });
        }
    },

    showShareModal: function () {
        var page = this;
        page.setData({
            share_modal_active: "active",
            no_scroll: true,
        });
    },

    shareModalClose: function () {
        var page = this;
        page.setData({
            share_modal_active: "",
            no_scroll: false,
        });
    },

    getGoodsQrcode: function () {
        var page = this;
        page.setData({
            goods_qrcode_active: "active",
            share_modal_active: "",
        });
        if (page.data.goods_qrcode)
            return true;
        app.request({
            url: api.default.goods_qrcode,
            data: {
                goods_id: page.data.id,
            },
            success: function (res) {
                if (res.code == 200) {
                    page.setData({
                        goods_qrcode: res.data.pic_url,
                    });
                }
                if (res.code == 1) {
                    page.goodsQrcodeClose();
                    wx.showModal({
                        title: "提示",
                        content: res.msg,
                        showCancel: false,
                        success: function (res) {
                            if (res.confirm) {

                            }
                        }
                    });
                }
            },
        });
    },

    goodsQrcodeClose: function () {
        var page = this;
        page.setData({
            goods_qrcode_active: "",
            no_scroll: false,
        });
    },

    saveGoodsQrcode: function () {
        var page = this;
        if (!wx.saveImageToPhotosAlbum) {
            // 如果希望用户在最新版本的客户端上体验您的小程序，可以这样子提示
            wx.showModal({
                title: '提示',
                content: '当前微信版本过低，无法使用该功能，请升级到最新微信版本后重试。',
                showCancel: false,
            });
            return;
        }

        wx.showLoading({
            title: "正在保存图片",
            mask: false,
        });

        wx.downloadFile({
            url: page.data.goods_qrcode,
            success: function (e) {
                wx.showLoading({
                    title: "正在保存图片",
                    mask: false,
                });
                wx.saveImageToPhotosAlbum({
                    filePath: e.tempFilePath,
                    success: function () {
                        wx.showModal({
                            title: '提示',
                            content: '商品海报保存成功',
                            showCancel: false,
                        });
                    },
                    fail: function (e) {
                        wx.showModal({
                            title: '图片保存失败',
                            content: e.errMsg,
                            showCancel: false,
                        });
                    },
                    complete: function (e) {
                        console.log(e);
                        wx.hideLoading();
                    }
                });
            },
            fail: function (e) {
                wx.showModal({
                    title: '图片下载失败',
                    content: e.errMsg + ";" + page.data.goods_qrcode,
                    showCancel: false,
                });
            },
            complete: function (e) {
                console.log(e);
                wx.hideLoading();
            }
        });

    },

    goodsQrcodeClick: function (e) {
        var src = e.currentTarget.dataset.src;
        wx.previewImage({
            urls: [src],
        });
    },
    closeCouponBox: function (e) {
        this.setData({
            get_coupon_list: ""
        });
    }

});