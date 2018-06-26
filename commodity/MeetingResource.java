package com.zizaihome.api.resources.commodity;

import org.restlet.Request;

import com.zizaihome.web.resource.base.BaseResultModel;
import com.zizaihome.web.resource.base.BaseWebResource;

public class MeetingResource extends BaseWebResource {

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected void getMethod(BaseResultModel br) {
		br.setHtmlPath("activity/meeting.html");
	}

	@Override
	protected void postMethod(BaseResultModel br) {
		
	}

}
