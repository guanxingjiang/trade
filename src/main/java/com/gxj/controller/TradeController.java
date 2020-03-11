package com.gxj.controller;


import com.alibaba.fastjson.JSONObject;
import com.gxj.util.RsaSecretUtils;
import com.gxj.util.StringUtils;
import com.winxuan.open.sdk.DefaultWinxuanClient;
import com.winxuan.open.sdk.WinxuanApiException;
import com.winxuan.open.sdk.WinxuanClient;
import com.winxuan.open.sdk.request.shoptrade.ShopTradeCreateRequest;
import com.winxuan.open.sdk.request.trade.TradeCreateRequest;
import com.winxuan.open.sdk.response.shoptrade.ShopTradeCreateResponse;
import com.winxuan.open.sdk.response.trade.TradeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


/**
 * @version 1.0
 * @description:
 * @author: guanxingjiang
 * @time: 2018-12-28 10:54
 */
@RestController
@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})
public class TradeController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/create",method=RequestMethod.GET)
    public String createTrade(String client,String request){
        if(StringUtils.isBlank(client) || StringUtils.isBlank(request)){
            return "client or request is null";
        }
//        logger.info("client:{},request:{}",client,request);
        String clientDecrypt = RsaSecretUtils.decrypt(client);
        String requestDecrypt = RsaSecretUtils.decrypt(request);
        if(StringUtils.isBlank(clientDecrypt) || StringUtils.isBlank(requestDecrypt)){
           return "decrypt fail";
        }
        logger.info("clientDecrypt:{},requestDecrypt:{}",clientDecrypt,requestDecrypt);
        WinxuanClient winxuanClient = JSONObject.parseObject(clientDecrypt, DefaultWinxuanClient.class);
        ShopTradeCreateRequest tradeCreateRequest = JSONObject.parseObject(requestDecrypt, ShopTradeCreateRequest.class);
        try {
            ShopTradeCreateResponse response = winxuanClient.execute(tradeCreateRequest);
            String result = JSONObject.toJSONString(response);
            logger.info("response:{}",result);
            return result;
        } catch (WinxuanApiException e) {
           logger.error(e.getMessage(),e);
           return e.getMessage();
        }
    }
}
