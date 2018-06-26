package com.zizaihome.api.resources.commodity;

import java.util.HashMap;
import java.util.Map;

import org.restlet.Request;

import com.zizaihome.web.resource.base.BaseResultModel;
import com.zizaihome.web.resource.base.BaseWebResource;

public class CommodityShoppingCartResource extends BaseWebResource {

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected void getMethod(BaseResultModel br) {
		int orderId = getParameter("orderId",0);
		int foshiOrderId = getParameter("foshiOrderId",0);
		Map<String, Object> dataModel = new HashMap<String, Object>();
		dataModel.put("orderId", orderId+"");
		dataModel.put("foshiOrderId", foshiOrderId+"");
		dataModel.put("access_token", accessToken);
		br.setDataModel(dataModel);
		br.setHtmlPath("buddhistCeremony/cart.html");
	}

	@Override
	protected void postMethod(BaseResultModel br) {
		
	}

}
