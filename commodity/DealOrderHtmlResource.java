package com.zizaihome.api.resources.commodity;


import java.util.HashMap;
import java.util.Map;

import org.restlet.Request;

import com.zizaihome.web.resource.base.BaseResultModel;
import com.zizaihome.web.resource.base.BaseWebResource;


public class DealOrderHtmlResource extends BaseWebResource {
	

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected void getMethod(BaseResultModel br) {
		Map<String, Object> dataModel = new HashMap<String, Object>();
		dataModel.put("access_token", accessToken);
		br.setDataModel(dataModel);
		br.setHtmlPath("buddhist/orders.html");
	}

	@Override
	protected void postMethod(BaseResultModel br) {
		// TODO Auto-generated method stub
		
	}

}
