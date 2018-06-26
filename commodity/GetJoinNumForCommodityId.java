package com.zizaihome.api.resources.commodity;

import java.util.List;

import org.restlet.Request;

import com.zizaihome.api.db.model.BuddnistCeremonyCommodityModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommoditySubdivideModel;
import com.zizaihome.api.resources.base.BaseResource;
import com.zizaihome.api.service.BuddnistCeremonyCommodityService;
import com.zizaihome.api.service.BuddnistCeremonyCommoditySubdivideService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class GetJoinNumForCommodityId extends BaseResource {

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected JSONObject getMethod(JSONObject json) {
		BuddnistCeremonyCommodityService commodityService = new BuddnistCeremonyCommodityService();
		BuddnistCeremonyCommoditySubdivideService subdivideService = new BuddnistCeremonyCommoditySubdivideService();
		String commodityIdsStr = getParameter("commodityIds","");
		String[] commodityIds = commodityIdsStr.split(",");
		JSONArray dataList = new JSONArray();
		int count = 0;
		for(int i=0;i<commodityIds.length;i++) {
			JSONObject data = new JSONObject();
			BuddnistCeremonyCommodityModel commodity = commodityService.getModel(Integer.parseInt(commodityIds[i]));
			data.put("commodityId", commodity.getId());
			data.put("joinNum", commodity.getJoin_num());
			List<BuddnistCeremonyCommoditySubdivideModel> subdivideList = subdivideService.findByCommodityId(commodity.getId());
			double money = 1;
			for(BuddnistCeremonyCommoditySubdivideModel subdivide:subdivideList) {
				if(subdivide.getPrice() > 0) {
					if(money == 1 && subdivide.getPrice() > money) {
						money = subdivide.getPrice();
					}
					else if(subdivide.getPrice() < money) {
						money = subdivide.getPrice();
					}
				}
			}
			data.put("price", money);
			dataList.add(data);
			count += commodity.getJoin_num();
		}
		json.put("count", count);
		json.put("data", dataList);
		return succRequest("获取成功",json);
	}

	@Override
	protected JSONObject postMethod(JSONObject json) {
		return this.getMethod(json);
	}

}
