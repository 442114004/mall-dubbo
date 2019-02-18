package com.macro.mall.portal.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@ToString
@Component
@ConfigurationProperties(prefix = "wx")
public class WxAppletProperties {

    private String secret;
    private String appId;
    private String mchId;
    private String paySignKey;
    private String certName;
    private String getCode;
    private String notifyUrl;
    private String orderquery;
    private String refundUrl;
    private String refundqueryUrl;
    private String tradeType;
    private String uniformorder;
    private String userMessage;
    private String webAccessTokenhttps;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getPaySignKey() {
        return paySignKey;
    }

    public void setPaySignKey(String paySignKey) {
        this.paySignKey = paySignKey;
    }

    public String getCertName() {
        return certName;
    }

    public void setCertName(String certName) {
        this.certName = certName;
    }

    public String getGetCode() {
        return getCode;
    }

    public void setGetCode(String getCode) {
        this.getCode = getCode;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getOrderquery() {
        return orderquery;
    }

    public void setOrderquery(String orderquery) {
        this.orderquery = orderquery;
    }

    public String getRefundUrl() {
        return refundUrl;
    }

    public void setRefundUrl(String refundUrl) {
        this.refundUrl = refundUrl;
    }

    public String getRefundqueryUrl() {
        return refundqueryUrl;
    }

    public void setRefundqueryUrl(String refundqueryUrl) {
        this.refundqueryUrl = refundqueryUrl;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getUniformorder() {
        return uniformorder;
    }

    public void setUniformorder(String uniformorder) {
        this.uniformorder = uniformorder;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public String getWebAccessTokenhttps() {
        return webAccessTokenhttps;
    }

    public void setWebAccessTokenhttps(String webAccessTokenhttps) {
        this.webAccessTokenhttps = webAccessTokenhttps;
    }
}
