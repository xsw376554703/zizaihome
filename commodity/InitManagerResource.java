package com.zizaihome.api.resources.commodity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.restlet.Request;

import com.zizaihome.api.db.model.TempleModel;
import com.zizaihome.api.db.model.ZizaijiaTempleManagerModel;
import com.zizaihome.api.service.TempleService;
import com.zizaihome.api.service.ZizaijiaTempleManagerService;
import com.zizaihome.web.resource.base.BaseResultModel;
import com.zizaihome.web.resource.base.BaseWebResource;

public class InitManagerResource extends BaseWebResource {

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected void getMethod(BaseResultModel br) {
		ZizaijiaTempleManagerService templeManagerService = new ZizaijiaTempleManagerService();
		TempleService templeService = new TempleService();
		int templeId = getParameter("p_templeId",0);
		ZizaijiaTempleManagerModel templeManager = templeManagerService.findByUser(userId, templeId);
		if(templeManager == null){
			ZizaijiaTempleManagerModel newTempleManager = new ZizaijiaTempleManagerModel();
			newTempleManager.setAddTime(new Date());
			newTempleManager.setOldUserId(0);
			newTempleManager.setStatus(0);
			newTempleManager.setTempleId(templeId);
			newTempleManager.setUserId(userId);
			templeManagerService.insert(newTempleManager);
		}
		TempleModel temple = templeService.getModel(templeId);
		Map<String, Object> dataModel = new HashMap<String, Object>();
		dataModel.put("templeName", temple.getName());
		br.setDataModel(dataModel);
		br.setHtmlPath("buddhist/be_manager.html");
	}

	@Override
	protected void postMethod(BaseResultModel br) {
		
	}


}
