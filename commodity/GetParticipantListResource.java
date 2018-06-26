package com.zizaihome.api.resources.commodity;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.restlet.Request;

import com.zizaihome.api.db.model.BuddnistCeremonyBuyerInfoModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityOrderModel;
import com.zizaihome.api.db.model.UserModel;
import com.zizaihome.api.resources.base.BaseResource;
import com.zizaihome.api.service.BuddnistCeremonyBuyerInfoService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityOrderService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityService;
import com.zizaihome.api.service.UserService;
import com.zizaihome.api.utils.JSONFromBean;
import com.zizaihome.api.utils.ZizaihomeJSONUtils;

public class GetParticipantListResource extends BaseResource {

	@Override
	protected void initParams(Request request) {
		isAuth = false;
	}

	@Override
	protected JSONObject getMethod(JSONObject json) {
		BuddnistCeremonyCommodityService buddnistCeremonyCommodityService = new BuddnistCeremonyCommodityService();
		BuddnistCeremonyCommodityOrderService buddnistCeremonyCommodityOrderService = new BuddnistCeremonyCommodityOrderService();
		UserService userService = new UserService();
		BuddnistCeremonyBuyerInfoService buddnistCeremonyBuyerInfoService = new BuddnistCeremonyBuyerInfoService();
		int commodityId = getParameter("commodityId",0);
		int size = getParameter("size",20);
		int page = getParameter("page",0);
		if(page == -1){
			return failRequest("没有更多了",json);
		}
		BuddnistCeremonyCommodityModel buddnistCeremonyCommodityModel = buddnistCeremonyCommodityService.getModel(commodityId);
		if(buddnistCeremonyCommodityModel == null){
			return failRequest("参数错误",json);
		}
		if(buddnistCeremonyCommodityModel.getIs_show_participant() == 1){
			List<BuddnistCeremonyCommodityOrderModel> buddnistCeremonyCommodityOrderList = buddnistCeremonyCommodityOrderService.findParticipantList(commodityId, size, page);
			List<String> buddnistCeremonyCommodityOrderJSONList = new ArrayList<String>();
			for(BuddnistCeremonyCommodityOrderModel buddnistCeremonyCommodityOrder:buddnistCeremonyCommodityOrderList){
				UserModel user = userService.getModel(buddnistCeremonyCommodityOrder.getUser_id());
				JSONObject userInfoJSON = new JSONObject();
				userInfoJSON.put("address", "");
				userInfoJSON.put("mobile", "");
				userInfoJSON.put("name", "匿名");
				
				if(user != null){
					userInfoJSON.put("name", user.getNick_name());
					userInfoJSON.put("head_img", user.getHead_img());
					if(user.getChanzaiMobile().equals("") || user.getChanzaiMobile() == null){
						userInfoJSON.put("userType", 1);
					}
					else{
						userInfoJSON.put("userType", 2);					
					}
				}
				else{
					userInfoJSON.put("name", "");
					userInfoJSON.put("head_img", "");
					userInfoJSON.put("userType", 1);
				}
				JSONArray posiscript = JSONArray.fromObject(buddnistCeremonyCommodityOrder.getPosiscript());
				String wish = "";

//				BuddnistCeremonyBuyerInfoModel buddnistCeremonyBuyerInfoModel = buddnistCeremonyBuyerInfoService.getModel(buddnistCeremonyCommodityOrder.getBuyer_info_id());
//				if(buddnistCeremonyBuyerInfoModel != null){
//					if(!buddnistCeremonyBuyerInfoModel.getName().equals("")){
//						userInfoJSON.put("name", buddnistCeremonyBuyerInfoModel.getName());
//					}
//				}
				JSONFromBean buddnistCeremonyCommodityOrderJSON = new JSONFromBean(buddnistCeremonyCommodityOrder, ZizaihomeJSONUtils.emptyBuddnistCeremonyCommodityOrderFilter());
				buddnistCeremonyCommodityOrderJSON.addPropertyObj("user", userInfoJSON.toString(), null);
				for(int i=0;i<posiscript.size();i++){
					if(posiscript.getJSONObject(i).has("type")) {
						if(posiscript.getJSONObject(i).getInt("type") == 15){
							wish = posiscript.getJSONObject(i).getString("value");
						}
					}
				}
				if(buddnistCeremonyCommodityOrder.getIs_cryptonym_wish() == 0){
					buddnistCeremonyCommodityOrderJSON.addPropertyObj("wish", wish, null);
				}
				else{
					buddnistCeremonyCommodityOrderJSON.addPropertyObj("wish", "", null);
				}
				try {
					buddnistCeremonyCommodityOrderJSONList.add(buddnistCeremonyCommodityOrderJSON.buildString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			json.put("data", buddnistCeremonyCommodityOrderJSONList);
			if(buddnistCeremonyCommodityOrderList.size() >= size){
				json.put("page", page+1);
			}
			else{
				json.put("page", -1);
			}
			return succRequest("获取成功",json);
		}
		return failRequest("该佛事不展示参与者列表",json);
	}

	@Override
	protected JSONObject postMethod(JSONObject json) {
		return this.getMethod(json);
	}

}
