const utils = require('./util');

const STYLE_DEFAULT = {
  TEXT_ALIGN: 'center',
  COLOR: '#000000',
  LIGHT_COLOR: '#fff',
  FONT_SIZE: 24,
  AXLE_COLOR: '#e5e5e5'
};

function CurvePainter(ctx, config) {
  this.ctx = ctx;
  this.windowWidth = config.windowWidth;
  this.canvasWidth = config.canvasWidth;
  this.canvasHeight = config.canvasHeight;
  this.marginTop = config.marginTop; // 画布上边留白
  this.marginBottom = config.marginBottom; // 画布底边留白
  this.marginLeft = config.marginLeft; // 画布左边留白
  this.marginRight = config.marginRight; // 画布右边留白
  this.drawWidth = this.canvasWidth - this.marginLeft - this.marginRight; // 绘制宽度
  this.drawHeight = this.canvasHeight - this.marginTop - this.marginBottom; // 绘制高度
}

CurvePainter.prototype = {
  setData(data) {
    this.hAxleData = data.hAxleData || []; // x轴刻度
    this.hAxleTitle = data.hAxleTitle || ''; // x轴标题
    this.vAxleData = data.vAxleData || []; // y轴刻度
    this.vAxleTitle = data.vAxleTitle || ''; // y轴标题
    this.originDataList = data.dataList || []; // 原始数据集
    this.selectedIndex = data.selectedIndex; // 被选中的序号
    this.reference = data.reference || 0; // 参考值
    this.unit = data.unit || ''; // 数值单位
    this.drawHAxle = data.drawHAxle || false; // 是否绘制横轴
    this.drawVAxle = data.drawVAxle || false; // 是否绘制纵轴
    this.drawCurveDecorate = data.drawCurveDecorate || false; // 是否绘制背景
    this.drawCurveDataPoint = data.drawCurveDataPoint || false; // 是否绘制数据点
    this.drawCurveDataText = data.drawCurveDataText || false; // 是否绘制数据值
  },
  setOffset(data) {
    this.topOffset = data.topOffset; // 最大数据点距画布顶部距离
    this.bottomOffset = data.bottomOffset; // 最小数据点距画布底部距离
  },
  setStyle(config) {
    this.curveWidth = config.curveWidth || this.toPx(6); // 曲线宽度
    this.curveColor = config.curveColor || STYLE_DEFAULT.COLOR; // 曲线颜色
    this.curveDecorateColors = config.curveDecorateColors || [STYLE_DEFAULT.LIGHT_COLOR, STYLE_DEFAULT.LIGHT_COLOR]; // 背景颜色
    this.scaleColor = config.scaleColor || STYLE_DEFAULT.COLOR; // 刻度字体颜色
    this.selectedScaleColor = config.selectedScaleColor || STYLE_DEFAULT.COLOR; // 选中刻度的字体颜色
    this.scaleFontSize = config.scaleFontSize || this.toPx(STYLE_DEFAULT.FONT_SIZE); // 刻度字体大小
    this.selectedScaleFontSize = config.selectedScaleFontSize || this.toPx(STYLE_DEFAULT.FONT_SIZE); // 选中的刻度字体大小
    this.dataPointFontColor = config.dataPointFontColor || STYLE_DEFAULT.COLOR; // 数据值颜色
    this.selectedDataPointFontColor = config.selectedDataPointFontColor || STYLE_DEFAULT.COLOR; // 选中的数据值颜色
    this.dataPointFontSize = config.dataPointFontSize || this.toPx(STYLE_DEFAULT.FONT_SIZE); // 数据值字体大小
    this.selectedDataPointFontSize = config.selectedDataPointFontSize || this.toPx(STYLE_DEFAULT.FONT_SIZE); // 选中的数据值字体大小
    this.textAlign = config.textAlign || 'center'; // 文字对其方式
    this.hAxleTitleColor = config.hAxleTitleColor || STYLE_DEFAULT.COLOR; // x轴标题颜色
    this.vAxleTitleColor = config.vAxleTitleColor || STYLE_DEFAULT.COLOR; // y轴标题颜色
    this.hAxleColor = config.hAxleColor || STYLE_DEFAULT.AXLE_COLOR; // x轴线颜色
    this.hAxleWidth = config.hAxleWidth || 1; // x轴宽度
    this.vAxleColor = config.vAxleColor || STYLE_DEFAULT.AXLE_COLOR; // y轴颜色
    this.vAxleWidth = config.vAxleWidth || 1; // y轴宽度
    this.pointMarginColor = config.pointMarginColor || STYLE_DEFAULT.COLOR; // 数据点外圆颜色
    this.pointMarginRadius = config.pointMarginRadius || this.toPx(24 / 2); // 数据点外缘半径
    this.pointColor = config.pointColor || STYLE_DEFAULT.COLOR;// 数据点颜色
    this.pointRadius = config.pointRadius || this.toPx(12 / 2); // 数据点半径
  },
  addAction(fn) {
    if (!utils.isFunction(fn)) return;

    if (!this.actions) {
      this.actions = [];
    }

    this.actions.push(fn);
  },
  prepare() {
    let self = this;
    let data = this.originDataList;

    dataToPos();

    function dataToPos() {
      if (!self.isValid(data)) {
        return;
      }

      let {max, min} = self.getMaxAndMin(data, self.reference);
      let range = max !== min ? max - min : 1; // 需要考虑最大值等于最小值，也就是所有值都一样的情况
      let drawWidth = self.drawWidth;
      let drawHeight = self.drawHeight;
      let dataPointMaxHeight = drawHeight - self.topOffset - self.bottomOffset; // 数据点最大高度
      let xOffset = drawWidth / (data.length - 1);

      let calcData = [];
      for (let i = 0; i < data.length; i++) {
        if (data[i]) {
          calcData.push({
            x: i * xOffset,
            y: self.topOffset + dataPointMaxHeight - self.calcRate(data, max, min, range, i) * dataPointMaxHeight,
            value: data[i],
          });
        } else {
          calcData.push({
            value: data[i]
          });
        }
      }

      self.calcData = calcData;
    }

    return this;
  },
  // 数据合法化处理
  calcRate(list, max, min, range, idx) {
    if(max === min) {
      return 1;
    }

    let diff = list[idx] - min;
    if (diff === 0) {
      return 0;
    } else {
      if (range === 0) {
        return list[idx] / max;
      } else {
        return diff / range;
      }
    }

  },
  getMaxAndMin(list, target) {
    let max = 0, min = 0, self = this;

    let listWithoutZero = this.filterZero(list);
    if (listWithoutZero.length > 1) {
      return {
        max: self.max(listWithoutZero),
        min: self.minWithoutZero(listWithoutZero)
      };
    } else {
      let only = listWithoutZero[0];
      max = only > target ? only : target;
      min = only > target ? target: only;

      return { max, min };
    }
  },
  max(obj) {
    if (!this.isValid(obj)) {
      return void 0;
    }

    let max = obj[0];
    obj.forEach(item => {
      if (item > max) {
        max = item;
      }
    });

    return max;
  },
  minWithoutZero(obj) {
    if (!this.isValid(obj)) {
      return void 0;
    }

    let min = obj[0];
    obj.forEach(item => {
      if (item > 0 && item <= min) {
        min = item;
      }
    });

    return min;
  },
  filterZero(list) {
    return list.filter(item => item);
  },
  draw(reDraw) {
    reDraw && this.clear();
    this.performDraw();
  },
  performDraw() {
    // 坐标轴变换方便计算
    this.resetCurvesCoordinate();
    // 绘制轴线
    this.drawAxle();
    // 绘制曲线
    this.drawCurve();

    this.ctx.draw();
  },
  clear() {
    this.ctx && this.ctx.clearRect(0, 0, this.drawHeight, this.drawHeight);
  },
  resetCurvesCoordinate() {
    let xCut = this.marginLeft;
    let yCut = this.marginTop;

    this.ctx.translate(xCut, yCut);
  },
  drawAxle() {
    let ctx = this.ctx;
    let drawWidth = this.drawWidth;
    let drawHeight = this.drawHeight;
    let defaultColor = STYLE_DEFAULT.COLOR;
    let drawHAxle = this.drawHAxle;

    if (drawHAxle) {
      let hAxleData = this.hAxleData;
      let dataLen = hAxleData.length;
      let selectedIndex = this.selectedIndex;
      let selectedScaleColor = this.selectedScaleColor;
      let scaleFontSize = this.scaleFontSize;
      let selectedScaleFontSize = this.selectedScaleFontSize;
      let hAxleTitle = this.hAxleTitle;
      let hAxleTitleColor = this.hAxleTitleColor;
      let hAxleWidth = this.hAxleWidth;
      let hAxleColor = this.hAxleColor;

      // 绘制刻度
      ctx.setFontSize(scaleFontSize);
      ctx.setTextAlign(this.textAlign);

      let item;
      let xOffset = dataLen - 1 ? drawWidth / (dataLen - 1) : 0;
      for (let i = 0, len = dataLen; i < len; i++) {
        item = hAxleData[i];
        ctx.setFillStyle(i === selectedIndex ? selectedScaleColor : defaultColor);
        ctx.setFontSize(i === selectedIndex ? selectedScaleFontSize : scaleFontSize);
        ctx.fillText(item, xOffset * i, drawHeight);
      }

      // 绘制刻度标题
      ctx.setFillStyle(hAxleTitleColor);
      ctx.setFontSize(scaleFontSize);
      ctx.fillText(hAxleTitle, drawWidth + this.toPx(45), drawHeight); // 45 = 偏离y轴的距离

      // 画轴线
      ctx.setLineWidth(hAxleWidth);
      ctx.setStrokeStyle(hAxleColor);
      ctx.beginPath();
      ctx.moveTo(0, drawHeight - this.toPx(36)); // 36 = 刻度文字与x轴的距离 + 文字大小
      ctx.lineTo(drawWidth + this.toPx(55), drawHeight - this.toPx(36));
      ctx.stroke();
    }
  },
  drawCurve() {
    let self = this;
    let ctx = this.ctx;
    let drawWidth = this.drawWidth;
    let drawHeight = this.drawHeight;
    let calcData = this.calcData;
    let selectedIndex = self.selectedIndex;

    if (calcData && calcData.length) {
      let curveWidth = this.curveWidth;
      let curveColor = this.curveColor;

      ctx.setLineWidth(curveWidth);

      let pos, lastPos = calcData[0];
      ctx.beginPath();
      ctx.moveTo(lastPos.x, lastPos.y);
      for (let i = 1, len = calcData.length; i < len; i++) {
        pos = calcData[i];
        if (!pos.value) continue;

        // 这里每次都得创建一个新的渐变
        // FIXME 这里是为了解决，android，在同一个canvas内，
        // FIXME 先用gradient绘制了线条后，无法setStrokeStyle设置为普通颜色的Bug
        let gd = ctx.createLinearGradient(0, 0, pos.x, pos.y);
        gd.addColorStop(0, curveColor);
        gd.addColorStop(1, curveColor);
        ctx.setStrokeStyle(gd);

        if (i === 1) {
          ctx.lineTo(pos.x, pos.y);
        } else {
          ctx.beginPath();
          ctx.moveTo(lastPos.x, lastPos.y);
          ctx.lineTo(pos.x, pos.y);
        }
        ctx.stroke();
        lastPos = pos;
      }

      drawCurveDecorate(); // 绘制曲线修饰
      drawCurveDataPoint(); // 绘制数据点
      drawCurveDataText(); // 绘制数据值
      drawCustom(); // 绘制自定义内容
    }

    function drawCurveDecorate() {
      let drawCurveDecorate = self.drawCurveDecorate;
      if (!drawCurveDecorate || calcData.length <= 1) return;

      let curveDecorateColors = self.curveDecorateColors;
      let lg = ctx.createLinearGradient(0, 0, 0, drawHeight);
      lg.addColorStop(0, curveDecorateColors[0]);
      lg.addColorStop(1, curveDecorateColors[1]);

      ctx.setFillStyle(lg);
      ctx.beginPath();
      ctx.moveTo(calcData[0].x || 0, calcData[0].y || 0);

      for(let i = 0, len = calcData.length; i < len; i++) {
        let pos = calcData[i];
        if (!pos.value) continue;

        ctx.lineTo(pos.x, pos.y);
      }

      ctx.lineTo(getNonZeroPos(calcData).x, drawHeight);
      ctx.lineTo(calcData[0].x || 0, drawHeight);
      ctx.lineTo(calcData[0].x || 0, calcData[0].y || 0);
      ctx.closePath();
      ctx.fill();
    }

    function getNonZeroPos(list) {
      let len = list.length;
      for (let i = len - 1, end = 0; i >= end; i--) {
        let temp = list[i];
        if (!temp.value) continue;

        return temp;
      }
    }

    function drawCurveDataPoint() {
      let drawCurveDataPoint = self.drawCurveDataPoint;
      if (!drawCurveDataPoint) return;

      let pointMarginColor = self.pointMarginColor;
      let pointColor = self.pointColor;

      for (let i = 0, len = calcData.length; i < len; i++) {
        let pos = calcData[i];
        if (!pos.value) continue;

        // 被选中的数据点有外边框
        if (i === selectedIndex) {
          ctx.setFillStyle(pointMarginColor);
          ctx.beginPath();
          ctx.arc(pos.x, pos.y, self.toPx(24 / 2), 0, 2 * Math.PI);
          ctx.fill();
        }

        // 再画颜色小圆
        ctx.setFillStyle(pointColor);
        ctx.beginPath();
        ctx.arc(pos.x, pos.y, self.toPx(12 / 2), 0, 2 * Math.PI);
        ctx.fill();
      }
    }

    function drawCurveDataText() {
      let drawCurveDataText = self.drawCurveDataText;
      if (!drawCurveDataText) return;

      let textAlign = self.textAlign;
      let dataPointFontSize = self.dataPointFontSize;
      let selectedDataPointFontSize = self.selectedDataPointFontSize;
      let dataPointFontColor = self.dataPointFontColor;
      let selectedDataPointFontColor = self.selectedDataPointFontColor;
      ctx.setTextAlign(textAlign);

      for (let i = 0, len = calcData.length; i < len; i++) {
        let pos = calcData[i];
        if (!pos.value) continue;

        ctx.setFontSize( i === selectedIndex ? selectedDataPointFontSize : dataPointFontSize);
        ctx.setFillStyle( i === selectedIndex ? selectedDataPointFontColor : dataPointFontColor);

        ctx.fillText(
          `${pos.value.toFixed(1)}`,
          pos.x,
          pos.y - self.toPx(25)); // 25 = 数据点和数据文字的距离
      }
    }

    function drawCustom() {
      let actions = self.actions;
      actions && actions.forEach(action => {
        action.apply(self);
      });
    }
  },
  toPx(rpx) {
    let windowWidth = this.windowWidth;
    return utils.rpx_to_px(rpx, windowWidth);
  },
  isValid(obj) {
    let isArray = utils.isArray(obj);
    if (isArray) {
      return obj.length > 0;
    } else {
      return isArray;
    }
  }
};

module.exports = CurvePainter;