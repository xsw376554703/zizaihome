package com.zizaihome.api.resources.commodity;

import org.restlet.Request;

import com.zizaihome.web.resource.base.BaseResultModel;
import com.zizaihome.web.resource.base.BaseWebResource;

public class CommodityOrderResource extends BaseWebResource {

	@Override
	protected void initParams(Request request) {
		isAuthWeiXin = false;
	}

	@Override
	protected void getMethod(BaseResultModel br) {
		br.setHtmlPath("buddhist/handle_order.html");
	}

	@Override
	protected void postMethod(BaseResultModel br) {
		
	}

}
