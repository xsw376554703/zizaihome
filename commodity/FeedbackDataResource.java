package com.zizaihome.api.resources.commodity;

import java.text.SimpleDateFormat;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.restlet.Request;

import com.zizaihome.api.db.model.BuddnistCeremonyBuyerInfoModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityOrderModel;
import com.zizaihome.api.db.model.TempleModel;
import com.zizaihome.api.db.model.UserModel;
import com.zizaihome.api.resources.base.BaseResource;
import com.zizaihome.api.service.BuddnistCeremonyBuyerInfoService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityOrderService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityService;
import com.zizaihome.api.service.TempleService;
import com.zizaihome.api.service.UserService;
import com.zizaihome.api.utils.ConfigReadUtils;

public class FeedbackDataResource extends BaseResource {

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected JSONObject getMethod(JSONObject json) {
		BuddnistCeremonyCommodityOrderService buddnistCeremonyCommodityOrderService = new BuddnistCeremonyCommodityOrderService();
		BuddnistCeremonyBuyerInfoService buddnistCeremonyBuyerInfoService = new BuddnistCeremonyBuyerInfoService();
		UserService userService = new UserService();
		TempleService templeService = new TempleService();
		BuddnistCeremonyCommodityService buddnistCeremonyCommodityService = new BuddnistCeremonyCommodityService();
		int orderId = getParameter("orderId",0);
		BuddnistCeremonyCommodityOrderModel commodityOrder = buddnistCeremonyCommodityOrderService.getModel(orderId);
		if(commodityOrder != null){
			JSONObject data = new JSONObject();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			data.put("commodityName", commodityOrder.getName());
			data.put("addTime", sdf.format(commodityOrder.getAdd_time()));
			BuddnistCeremonyBuyerInfoModel buyerInfo = buddnistCeremonyBuyerInfoService.getModel(commodityOrder.getBuyer_info_id());
			String name = "";
			if(buyerInfo != null) {
				name = buyerInfo.getName();
			}
			JSONArray posiscriptJSONList = JSONArray.fromObject(commodityOrder.getPosiscript());
			//如果上传的附言有类型为提示文本框类型进行处理不存入数据库
			for(int i=0;i<posiscriptJSONList.size();i++){
				JSONObject posiscriptJSON = posiscriptJSONList.getJSONObject(i);
				if(posiscriptJSON.getInt("type") == 12){
					name = posiscriptJSON.getString("value");
				}
			}
			if(name.equals("") || name == null){
				UserModel user = userService.getModel(userId);
				name = user.getNick_name();
			}
			data.put("name", name);
			TempleModel temple = templeService.getModel(commodityOrder.getTemple_id());
			data.put("templeName", temple.getName());
			data.put("stamp", temple.getStamp());
			BuddnistCeremonyCommodityModel commodity = buddnistCeremonyCommodityService.getModel(commodityOrder.getCommodity_id());
			data.put("feedbackType", commodity.getFeedbackType());
			String zizaihomeHost = ConfigReadUtils.loadConfigByConfDir("server.properties").getProperty("zizaihome_host");
			data.put("commodityUrl", zizaihomeHost+"/commodity/commodityAuth?commodityId="+commodity.getId());
			json.put("data", data);
			return succRequest("成功",json);
		}
		return failRequest("获取失败",json);
	}

	@Override
	protected JSONObject postMethod(JSONObject json) {
		return this.getMethod(json);
	}

}
