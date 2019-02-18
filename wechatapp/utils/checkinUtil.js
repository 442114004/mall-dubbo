const app = getApp();
const Crypto = require('./lib/crypto1/crypto');
const Base64 = require('./lib/crypto1/base64');

const IMG_SIZE_TYPE = {
  TYPE_WIDTH: (750 / 570) * 100 | 0,
  TYPE_HEIGHT: (750 / 900) * 100 | 0,
  TYPE_EQUAL: (750 / 750) * 100 | 0
};

module.exports = {
  // 是否是本地默认打卡图片
  isDefaultRecordImage(imgPath) {
    return imgPath && imgPath.startsWith('/img');
  },
  // 添加图片裁剪参数
  addImageCutParam (owidth, oheight, imgPath) {
    let ratio = owidth / oheight, width = owidth, height = oheight;
    let result = {};

    if ((ratio * 100 | 0) >= IMG_SIZE_TYPE.TYPE_WIDTH) {
      width = parseInt(oheight * 750 / 570);
      imgPath += "?imageView2/1/w/" + width + "/h/" + (height);

      result.width = width;
      result.height = height;
    } else if ((ratio * 100 | 0) <= IMG_SIZE_TYPE.TYPE_HEIGHT) {
      height = parseInt(width * 900 / 750);
      imgPath += "?imageView2/1/w/" + (width) + "/h/" + height;

      result.width = width;
      result.height = height;
    } else {
      imgPath += "?imageView2/1/w/" + (owidth) + "/h/" + (owidth);

      result.width = result.height = width;
    }
    result.imgPath = imgPath;

    return result;
  },
  // 添加图片缩放、裁剪参数
  addImageCutParamAli(owidth, oheight, imgPath) {
    const MAX_LEN = 4096;
    let ratio = owidth / oheight, width = owidth, height = oheight;

    imgPath += '?x-oss-process=image';

    // 图片宽高大于4096时，需要先进行缩放
    if (owidth > MAX_LEN || oheight > MAX_LEN) {
      imgPath += `/resize,h_${MAX_LEN},w_${MAX_LEN}`;

      // 这里需要重新计算缩放后的图片大小
      if (owidth > oheight) {
        width = MAX_LEN;
        let zoomRatio = width / owidth;
        height = parseInt(zoomRatio * oheight);
      } else if (owidth < oheight) {
        height = MAX_LEN;
        let zoomRatio = height / oheight;
        width = parseInt(zoomRatio * owidth);
      } else {
        width = height = MAX_LEN;
      }
    }

    // 裁剪
    ratio = width / height;

    let result = {};
    if ((ratio * 100 | 0) >= IMG_SIZE_TYPE.TYPE_WIDTH) {
      width = parseInt(oheight * 750 / 570);
      imgPath += `/crop,w_${width},h_${height},g_center`;

      result.width = width;
      result.height = height;
    } else if ((ratio * 100 | 0) <= IMG_SIZE_TYPE.TYPE_HEIGHT) {
      height = parseInt(width * 900 / 750);
      imgPath += `/crop,w_${width},h_${height},g_center`;

      result.width = width;
      result.height = height;
    } else {
      imgPath += `/crop,w_${width},h_${width},g_center`;

      result.width = result.height = width;
    }
    result.imgPath = imgPath;

    return result;
  },
  getMultipartParams() {
    let policyText = {
      "expiration": "2020-01-01T12:00:00.000Z", // 设置该Policy的失效时间，超过这个失效时间之后，就没有办法通过这个policy上传文件了
      "conditions": [
        ["content-length-range", 0, 1048576000] // 设置上传文件的大小限制
      ]
    };

    let policyBase64 = Base64.encode(JSON.stringify(policyText));
    let bytes = Crypto.HMAC(Crypto.SHA1, policyBase64, app.globalData.config.aliAccessKey, { asBytes: true }) ;
    let signature = Crypto.util.bytesToBase64(bytes);

    return {
      'Filename': 'wx-applet-fittime/' + '${filename}',
      'key': '',
      'policy': policyBase64,
      'OSSAccessKeyId': app.globalData.config.aliAccessId,
      'success_action_status': '200', // 让服务端返回200,不然，默认会返回204
      'signature': signature
    };
  }
};