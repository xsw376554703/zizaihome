package com.zizaihome.api.resources.commodity;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.restlet.Request;

import com.zizaihome.api.db.model.BuddnistCeremonyCommodityModel;
import com.zizaihome.api.db.model.ZizaijiaBuddhaWallModel;
import com.zizaihome.api.db.model.ZizaijiaTempleContentManagerNotifyModel;
import com.zizaihome.api.resources.base.BaseResource;
import com.zizaihome.api.service.BuddnistCeremonyCommodityService;
import com.zizaihome.api.service.ZizaijiaBuddhaWallService;
import com.zizaihome.api.service.ZizaijiaTempleContentManagerNotifyService;

public class CommodityListWithOrderResource extends BaseResource {

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected JSONObject getMethod(JSONObject json) {
		ZizaijiaBuddhaWallService zizaijiaBuddhaWallService = new ZizaijiaBuddhaWallService();
		BuddnistCeremonyCommodityService buddnistCeremonyCommodityService = new BuddnistCeremonyCommodityService();
		ZizaijiaTempleContentManagerNotifyService notifyService = new ZizaijiaTempleContentManagerNotifyService();
		JSONArray list = new JSONArray();
		List<ZizaijiaTempleContentManagerNotifyModel> modelList = notifyService.findByUser(userId);
		for(ZizaijiaTempleContentManagerNotifyModel model:modelList){
			JSONObject j = new JSONObject();
			if(model.getType() == 1){
				BuddnistCeremonyCommodityModel commodityModel = buddnistCeremonyCommodityService.getModel(model.getContentId());
				j.put("name", commodityModel.getName());
				j.put("type", model.getType());
				j.put("contentId", model.getContentId());
				list.add(j);
			}
			else if(model.getType() == 2){
				ZizaijiaBuddhaWallModel wallModel = zizaijiaBuddhaWallService.getModel(model.getContentId());
				j.put("name", wallModel.getName());
				j.put("type", model.getType());
				j.put("contentId", model.getContentId());
				list.add(j);
			}
			else if(model.getType() == 3){
				BuddnistCeremonyCommodityModel commodityModel = buddnistCeremonyCommodityService.getModel(model.getContentId());
				j.put("name", "禅在订单");
				j.put("type", model.getType());
				j.put("contentId", model.getContentId());
				list.add(j);
			}
		}
		json.put("list", list);
		return succRequest("处理成功",json);
	}

	@Override
	protected JSONObject postMethod(JSONObject json) {
		return this.getMethod(json);
	}

}
