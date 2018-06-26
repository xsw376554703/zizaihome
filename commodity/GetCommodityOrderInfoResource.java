package com.zizaihome.api.resources.commodity;

import net.sf.json.JSONObject;

import org.restlet.Request;

import com.zizaihome.api.db.model.BuddnistCeremonyCommodityOrderModel;
import com.zizaihome.api.resources.base.BaseResource;
import com.zizaihome.api.service.BuddnistCeremonyCommodityOrderService;

public class GetCommodityOrderInfoResource extends BaseResource {

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected JSONObject getMethod(JSONObject json) {
		BuddnistCeremonyCommodityOrderService buddnistCeremonyCommodityOrderService = new BuddnistCeremonyCommodityOrderService();
		int commodityOrderId = getParameter("commodityOrderId",0);
		BuddnistCeremonyCommodityOrderModel buddnistCeremonyCommodityOrderModel = buddnistCeremonyCommodityOrderService.getModel(commodityOrderId);
		if(buddnistCeremonyCommodityOrderModel == null){
			return failRequest("缺少参数",json);
		}
		if(buddnistCeremonyCommodityOrderModel.getPay_type() == 1){
			return succRequest("已经付款",json);
		}
		else{
			return failRequest("未付款",json);
		}
	}

	@Override
	protected JSONObject postMethod(JSONObject json) {
		return this.getMethod(json);
	}

}
