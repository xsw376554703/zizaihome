package com.zizaihome.api.resources.website;

import java.util.HashMap;
import java.util.Map;

import org.restlet.Request;

import com.zizaihome.api.db.model.TempleModel;
import com.zizaihome.api.service.TempleService;
import com.zizaihome.web.resource.base.BaseResultModel;
import com.zizaihome.web.resource.base.BaseWebResource;

public class CalendarResource extends BaseWebResource {

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected void getMethod(BaseResultModel br) {
		int templeId = getParameter("templeId",0);
		TempleService service = new TempleService();
		TempleModel temple = service.getModel(templeId);
		Map<String, Object> dataModel = new HashMap<String,Object>();
		dataModel.put("access_token", accessToken);
		if(temple != null){
			dataModel.put("templeName", temple.getName());
		}
		else{
			dataModel.put("templeName","");
		}
		br.setDataModel(dataModel);
		br.setHtmlPath("temple/calendar.html");
	}

	@Override
	protected void postMethod(BaseResultModel br) {
		
	}

}
