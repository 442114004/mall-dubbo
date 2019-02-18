var Promise = require('../utils/lib/es6-promise.min.js');

module.exports={
  toast(params) {
    if (!wx.showToast) return;
    if (Object.prototype.toString.call(params) != '[object Object]') {
      params = {
        title: params + ''
      };
    }
    var defaultParams = {
      duration: 1500,
      mask: false
    }
    wx.showToast(Object.assign(defaultParams, params || {}));
  },
  loading(params) {
    if (!wx.hideLoading || !wx.showLoading) return;
    if (!params) {
      wx.hideLoading();
      return;
    } else if (Object.prototype.toString.call(params) != '[object Object]') {
      params = { title: params + '' };
    }
    var defaultParams = {
      mask: true
    }
    wx.showLoading(Object.assign(defaultParams, params || {}));
  },
  confirm(params) {
    if (!wx.showModal) return;
    return new Promise((resolve, reject)=>{
      wx.showModal({
        title: params.title || '提示',
        content: params.content || '',
        showCancel: true,
        cancelText: params.cancelText || '取消',
        confirmText: params.confirmText || '确定',
        success(res) {
          resolve(res);
        }
      });
    });
    
  },
  alert(params) {
    if (!wx.showModal) return;
    if (Object.prototype.toString.call(params) != '[object Object]') {
      params = { content: params + '' };
    }
    var defaultParams = {
      textAlign: 'center',
      title: params.title || '提示',
      content: params.content || '',
      showCancel: false,
      success: params.success||function(){}
    }
    wx.showModal(defaultParams);
  },
  location(url) {
    console.log('location');
    if (!wx.navigateTo) return;
    wx.navigateTo({
      url: url
    });
  },
  locationReplace(url) {
    if (wx.reLaunch) {
      wx.reLaunch({
        url: url
      })
    } else {
      this.location(url);
    }
  },
  redirectTo(url){
    if ( wx.redirectTo ){
      wx.redirectTo({
        url: url
      })
    } else {
      this.location(url);
    }
  },
  /*
    obj : 配置信息
    sync: 是否同步
  */
  setStorage(obj, sync) {
    if (!wx.setStorageSync || !wx.setStorage) return;
    if (sync) {
      wx.setStorageSync(obj.key, obj.data);
    } else {
      wx.setStorage(obj);
    }
  },
  /*
    key : key/配置信息
    sync: 是否同步
  */
  getStorage(key, sync) {
    if (!wx.getStorageSync || !wx.getStorage) return;
    if (sync) {
      return wx.getStorageSync(key);
    } else {
      return wx.getStorage(key);
    }
  },
  /*
    key : key/配置信息
    sync: 是否同步
  */
  removeStorage(obj, sync) {
    if (!wx.removeStorageSync || !wx.removeStorage) return;
    if (sync) {
      return wx.removeStorageSync(obj.key);
    } else {
      return wx.removeStorage(obj);
    }
  },
  wxLogin() {
    return new Promise((resolve, reject) => {
      wx.login({
        success(dt) {
          resolve(dt);
        },
        fail(dt) {
          resolve(dt);
        },
        complete( res ){
        }
      });
    });
  },
  getUserInfo(cb) {
    var that = this;
    if (!wx.getUserInfo) return;
    wx.getUserInfo({
      success(res) {
        //that.globalData.userInfo = res.userInfo;
        //that.globalData.iv = res.iv;
        //that.globalData.encryptedData = res.encryptedData;
        typeof cb == "function" && cb(Object.assign({ iv: res.iv, encryptedData: res.encryptedData }, that.globalData.config))
      },
      fail(dt) {
        typeof cb == "function" && cb({ statusCode: -404 })
      }
    })
  },
  getSystemInfo() {
    if (!wx.getSystemInfo) return;
    return new Promise((resolve, reject) => {
      if (!wx.getSystemInfo) {
        reject();
        return;
      }
      wx.getSystemInfo({
        success(res) {
          resolve(res);
        },
        fail(res) {
          reject(res);
        }
      })
    })
  },
  getWeRunData(){
    return new Promise(( resolve, reject)=>{
      if (!wx.getWeRunData){
        resolve({})
        return;
      }
      wx.getWeRunData({
        success( res ){
          resolve( res );
        },
        fail( res ){
          resolve( res );
        }
      });
    })
  },
  chooseImage(count = 1) {//选择图片
    return new Promise((resolve, reject) => {
      wx.chooseImage({
        count,
        sizeType: ['original', 'compressed'],
        sourceType: ['camera','album'],
        success(res) {
          resolve(res);
        },
        fail(res) {
          reject(res);
        }
      })
    })
  },
  getFileInfo(filePath){
    var ctx = this;
    return new Promise((resolve, reject) => {
      wx.getFileInfo({
        filePath,
        complete( res ){
          res.errMsg == 'getFileInfo:ok' ? resolve(res) : reject(res);
        }
      })
    })
  },
  getNetworkType(){
    return new Promise((resolve, reject) => {
      wx.getNetworkType({
        complete(res) {
          resolve(res);
        }
      })
    })
  },
  downloadFile( url ){
    return new Promise((resolve, reject) => {
      wx.downloadFile({
        url,
        success(res) {
          resolve(res);
        },
        fail(res) {
          reject(res);
        }
      })
    })
  },
  navigateBack(delta=1){
    if (wx.navigateBack){
      wx.navigateBack({ delta });
    }
  },
  setKeepScreenOn(keepScreenOn=true){
    if (wx.setKeepScreenOn) {
      wx.setKeepScreenOn({
        keepScreenOn
      })
    }
  },
  getSetting(){
    return new Promise((resolve, reject) => {
      if(!wx.getSetting){
        setTimeout(function(){
          resolve({})
        },20);
        return;
      }
      wx.getSetting({
        success(res) {
          resolve(res.authSetting);
        },
        fail(res) {
          resolve({});
        }
      })
    })
  },
  openSetting(){
    return new Promise((resolve,reject)=>{
      if (!wx.openSetting){
        setTimeout(function () {
          resolve({ 'errorMsg':'wx.openSetting不支持'})
        }, 20);
        return;
      }
      wx.openSetting({
        complete( res ){
          resolve( res )
        }
      })
    });
  }
  
  
}