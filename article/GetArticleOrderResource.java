package com.zizaihome.api.resources.article;

import net.sf.json.JSONObject;

import org.restlet.Request;

import com.zizaihome.api.db.model.ZizaijiaArticlePayModel;
import com.zizaihome.api.resources.base.BaseResource;
import com.zizaihome.api.service.ZizaijiaArticlePayService;

public class GetArticleOrderResource extends BaseResource {

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected JSONObject getMethod(JSONObject json) {
		ZizaijiaArticlePayService articlePayService = new ZizaijiaArticlePayService();
		int orderId = getParameter("orderId",0);
		ZizaijiaArticlePayModel articlePay = articlePayService.findByOrderId(orderId);
		if(articlePay.getPay_type() == 1){
			return succRequest("支付成功",json);
		}
		else{
			return failRequest("等待返回中",json);
		}
	}

	@Override
	protected JSONObject postMethod(JSONObject json) {
		return this.getMethod(json);
	}

}
