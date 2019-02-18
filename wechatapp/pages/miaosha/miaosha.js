// pages/miaosha/miaosha.js
var api = require('../../api.js');
var app = getApp();
Page({

    /**
     * 页面的初始数据
     */
    data: {
        imgServer :  api.default.img_server,

        start_time_list: [
            {
                time: "00:00",
                status: 0,
                status_text: "已结束",
            },
            {
                time: "01:00",
                status: 0,
                status_text: "已结束",
            },
            {
                time: "02:00",
                status: 0,
                status_text: "已结束",
            },
            {
                time: "03:00",
                status: 0,
                status_text: "已结束",
            },
            {
                time: "04:00",
                status: 1,
                status_text: "进行中",
            },
            {
                time: "05:00",
                status: 2,
                status_text: "即将开始",
            },
            {
                time: "06:00",
                status: 2,
                status_text: "即将开始",
            },
            {
                time: "07:00",
                status: 2,
                status_text: "即将开始",
            },
            {
                time: "08:00",
                status: 2,
                status_text: "即将开始",
            },
        ],
        over_time: "00:18:24",
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