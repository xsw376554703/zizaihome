package com.zizaihome.api.resources.website;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.restlet.Request;

import com.zizaihome.api.db.model.TempleModel;
import com.zizaihome.api.db.model.VrDevoteSceneModel;
import com.zizaihome.api.service.TempleService;
import com.zizaihome.api.service.VrDevoteSceneService;
import com.zizaihome.web.resource.base.BaseResultModel;
import com.zizaihome.web.resource.base.BaseWebResource;

import freemarker.ext.beans.DateModel;

public class TemplateWebsiteInfoResource extends BaseWebResource {

	@Override
	protected void initParams(Request request) {
		isPCBrowser = true;
	}

	@Override
	protected void getMethod(BaseResultModel br) {
		TempleService templeService = new TempleService();
		int templeId = getParameter("templeId",0);
		
		//记录用户访问记录
		saveVisitRecord(0,4,templeId);
		
		TempleModel temple = templeService.getModel(templeId);
		VrDevoteSceneService sceneService = new VrDevoteSceneService();
		List<VrDevoteSceneModel> list = sceneService.getList(templeId);
		int showVRDevote = 0;
		if(list.size() > 0){
			showVRDevote = 1;
		}
		int isWeChat = 1;
		if(userId == 0){
			isWeChat = 0;
		}
		int isChanzai = 0;
		if(!sid.equals("")){
			isChanzai = 1;
		}
		Map<String, Object> dataModel = new HashMap<String,Object>();
		dataModel.put("access_token", accessToken);
		dataModel.put("templeName", temple.getName());
		dataModel.put("isChanzai", isChanzai);
		dataModel.put("chanzaiIsVirtual", chanzaiIsVirtual);
		dataModel.put("showWebsiteSuixi", temple.getShow_website_suixi());
		dataModel.put("showVRDevote", showVRDevote);
		dataModel.put("verify", temple.getVerify());
		dataModel.put("isWeChat", isWeChat);
		dataModel.put("isOffline", temple.getIs_offline());
		dataModel.put("visit_num", temple.getWebsite_visit_num());//放入寺院微站访问次数
		br.setDataModel(dataModel);
		br.setHtmlPath("temple/index.html");
	}

	@Override
	protected void postMethod(BaseResultModel br) {
		
	}

}
