package com.gxj;

import com.alibaba.fastjson.JSONObject;
import com.gxj.util.RsaSecretUtils;
import com.winxuan.open.sdk.DefaultWinxuanClient;
import org.springframework.security.rsa.crypto.RsaRawEncryptor;

/**
 * @version 1.0
 * @description:
 * @author: guanxingjiang
 * @time: 2019-10-25 17:07
 */
public class RsaEncrypt {
    public static void main(String[] args) {
        RsaRawEncryptor rsaSecretEncryptor1 =  new RsaRawEncryptor("-----BEGIN PUBLIC KEY-----" +
                "MIGeMA0GCSqGSIb3DQEBAQUAA4GMADCBiAKBgHT1pHAVD/yyDAT8XZv3sN/6uAx5" +
                "fZRvqn1ATW6oHzClYbFn1A3H2aHfXZvtWJ0QeJfFo7KykReylJdYHrFSioxm5YaP" +
                "87aBe7G7SxtFHVPDZcZBJEnJcrvNjqcWZcRewM+0aPTlgj9ENQQCZDposz+P+Kyk" +
                "oyIMiAXgj2bGErsPAgMBAAE=" +
                "-----END PUBLIC KEY-----");
        String encrypt = rsaSecretEncryptor1.encrypt("{\"apiPath\":\"winxuan.shop.trade.create\",\"deliveryType\":\"EXPRESS\",\"invoiceType\":\"NOT_NEED\",\"items\":[{\"itemId\":1201468168,\"listPrice\":68.00,\"outItemId\":\"605612721615\",\"purchaseQuantity\":3,\"salePrice\":29.20}],\"listPrice\":204.00,\"outerTrade\":\"564157671676422993\",\"purchaseTime\":1572074053000,\"reqMethod\":\"POST\",\"reqParams\":{\"items[0].purchaseQuantity\":\"3\",\"tradeConsignee.zipCode\":\"000000\",\"tradeConsignee.country\":\"23\",\"items[0].outItemId\":\"605612721615\",\"tradeConsignee.email\":\"yifei@163.com\",\"tradeConsignee.province\":\"165\",\"sellType\":\"NORMAL_SELL\",\"tradeConsignee.mobile\":\"18752448555\",\"tradeConsignee.city\":\"343\",\"tradeConsignee.town\":\"58644\",\"stockOutOption\":\"UTMOST_DELIVERY\",\"invoiceType\":\"NOT_NEED\",\"items[0].itemId\":\"1201468168\",\"tradeConsignee.address\":\"南湖街道中国国际图书城二楼书香味\",\"salePrice\":\"87.60\",\"items[0].salePrice\":\"29.20\",\"deliveryType\":\"EXPRESS\",\"tradeConsignee.remark\":\"-雷雷817\",\"items[0].listPrice\":\"68.00\",\"outerTrade\":\"564157671676422993\",\"purchaseTime\":\"2019-10-26 15:14:13\",\"tradeConsignee.district\":\"3784\",\"listPrice\":\"204.00\",\"tradeConsignee.consignee\":\"陈龙\"},\"responseClass\":\"com.winxuan.open.sdk.response.shoptrade.ShopTradeCreateResponse\",\"salePrice\":87.60,\"sellType\":\"NORMAL_SELL\",\"stockOutOption\":\"UTMOST_DELIVERY\",\"tradeConsignee\":{\"address\":\"南湖街道中国国际图书城二楼书香味\",\"city\":343,\"consignee\":\"陈龙\",\"country\":23,\"district\":3784,\"email\":\"yifei@163.com\",\"mobile\":\"18752448555\",\"province\":165,\"remark\":\"-雷雷817\",\"town\":58644,\"zipCode\":\"000000\"}}");
        DefaultWinxuanClient client = new DefaultWinxuanClient();
        client.setAppKey("100095");
        client.setAppSecret("5af12d0a8c42b3b17459f8a469378864");
        client.setAccessToken("8ba07ef6b368d3e9b2343d64bedf33ba");
        client.setServerUrl("http://gw.api.winxuan.com/router/rest");
        String encrypt1 = rsaSecretEncryptor1.encrypt(JSONObject.toJSONString(client));
//        System.out.println(encrypt);
//        System.out.println(encrypt1);
        System.out.println(RsaSecretUtils.decrypt("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"));
    }
}
