package com.zizaihome.api.resources.commodity;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.restlet.Request;

import com.zizaihome.api.db.model.BuddnistCeremonyBuyerInfoModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityOrderModel;
import com.zizaihome.api.db.model.UserModel;
import com.zizaihome.api.resources.base.BaseResource;
import com.zizaihome.api.service.BuddnistCeremonyBuyerInfoService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityOrderService;
import com.zizaihome.api.service.UserService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class GetCommodityWishList extends BaseResource {

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected JSONObject getMethod(JSONObject json) {
		BuddnistCeremonyBuyerInfoService buddnistCeremonyBuyerInfoService = new BuddnistCeremonyBuyerInfoService();
		BuddnistCeremonyCommodityOrderService commodityOrderService = new BuddnistCeremonyCommodityOrderService();
		UserService userService = new UserService();
		String commodityIds = getParameter("commodityIds","");
		int pageNumber = getParameter("pageNumber",0);
		int pageSize = getParameter("pageSize",20);
		List<BuddnistCeremonyCommodityOrderModel> commodityOrderModelList = commodityOrderService.getWishForCommodityIds(commodityIds, pageNumber, pageSize);
		JSONArray dataList = new JSONArray();
		for(BuddnistCeremonyCommodityOrderModel commodityOrderModel:commodityOrderModelList) {
			JSONObject data = new JSONObject();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			data.put("payTime", dateFormat.format(commodityOrderModel.getPay_time()));
			if(commodityOrderModel.getIs_cryptonym_wish() == 0){
				data.put("wish", commodityOrderModel.getWish());
			}
			else{
				data.put("wish", "");
			}
			if(commodityOrderModel.getIs_cryptonym_buy() == 1) {
				data.put("nickName", "匿名");
				data.put("headImg", "https://pic.zizaihome.com/chanzai_avatar_170619.png");
			}else {
				UserModel user = userService.getModel(commodityOrderModel.getUser_id());
				if(user != null){
					data.put("headImg", user.getHead_img());
				}
				
				if(commodityOrderModel.getBuyer_info_id().intValue() > 0){
					BuddnistCeremonyBuyerInfoModel buyerInfo = buddnistCeremonyBuyerInfoService.getModel(commodityOrderModel.getBuyer_info_id().intValue());
					if(buyerInfo != null && StringUtils.isNotEmpty(buyerInfo.getName())){
						data.put("nickName", buyerInfo.getName());
					}
				}
				else if(user != null) {
					data.put("nickName", user.getNick_name());
				}
				else {
					data.put("nickName", "匿名");
					data.put("headImg", "https://pic.zizaihome.com/chanzai_avatar_170619.png");
				}
			}
			dataList.add(data);
		}
		if(dataList.size() >= pageSize) {
			json.put("pageNumber", pageNumber+1);
		}
		else {
			json.put("pageNumber", -1);
		}
		json.put("data", dataList);
		if(dataList.size()>=pageSize) {
			json.put("pageNumber", pageNumber+1);
		}
		else {
			json.put("pageNumber", -1);
		}
		return succRequest("获取成功",json);
	}

	@Override
	protected JSONObject postMethod(JSONObject json) {
		return this.getMethod(json);
	}
	
}
