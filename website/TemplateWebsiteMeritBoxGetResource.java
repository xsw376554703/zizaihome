package com.zizaihome.api.resources.website;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import org.restlet.Request;

import com.zizaihome.api.db.model.BuddnistCeremonyBuyerInfoModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityOrderModel;
import com.zizaihome.api.db.model.UserModel;
import com.zizaihome.api.db.model.ZizaijiaTempleWebsiteMeritBoxModel;
import com.zizaihome.api.db.model.ZizaijiaTempleWebsiteSortModel;
import com.zizaihome.api.resources.base.BaseResource;
import com.zizaihome.api.service.BuddnistCeremonyBuyerInfoService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityOrderService;
import com.zizaihome.api.service.UserService;
import com.zizaihome.api.service.ZizaijiaTempleWebsiteMeritBoxService;
import com.zizaihome.api.service.ZizaijiaTempleWebsiteSortService;
import com.zizaihome.api.utils.JSONFromBean;
import com.zizaihome.api.utils.ZizaihomeJSONUtils;

public class TemplateWebsiteMeritBoxGetResource extends BaseResource {

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected JSONObject getMethod(JSONObject json) {
		ZizaijiaTempleWebsiteSortService zizaijiaTempleWebsiteSortService = new ZizaijiaTempleWebsiteSortService();
		ZizaijiaTempleWebsiteMeritBoxService meritBoxService = new ZizaijiaTempleWebsiteMeritBoxService();
		UserService userService = new UserService();
		BuddnistCeremonyCommodityOrderService commodityOrderService = new BuddnistCeremonyCommodityOrderService();
		BuddnistCeremonyBuyerInfoService buddnistCeremonyBuyerInfoService = new BuddnistCeremonyBuyerInfoService();
		int templeId = getParameter("templeId",0);
		int pageNumber = getParameter("pageNumber",0);
		int pageSize = getParameter("pageSize",0);
		int meritBoxId = getParameter("meritBoxId",0);
		int type = getParameter("type",0);
		ZizaijiaTempleWebsiteSortModel sort = zizaijiaTempleWebsiteSortService.findTempleIdAndType(templeId, 4);
		int showListNum = 10;
		if(meritBoxId != 0){
			ZizaijiaTempleWebsiteMeritBoxModel meritBox = meritBoxService.getModel(meritBoxId);
			showListNum = meritBox.getShow_list_num();
		}
		else if(sort != null){
			ZizaijiaTempleWebsiteMeritBoxModel meritBox = meritBoxService.getModel(sort.getMessage_id());
			showListNum = meritBox.getShow_list_num();
		}
		if(pageSize != 0){
			showListNum = pageSize;
		}
		List<BuddnistCeremonyCommodityOrderModel> buddnistCeremonyCommodityOrderList = new ArrayList<BuddnistCeremonyCommodityOrderModel>();
		if(type == 1){
			buddnistCeremonyCommodityOrderList = commodityOrderService.findByTempleIdAndAbbotIdForRealTime(templeId, pageNumber, showListNum);
		}
		else if(type == 2){
			buddnistCeremonyCommodityOrderList = commodityOrderService.findByTempleIdAndAbbotId(templeId, pageNumber, showListNum,1);
		}
		else if(type == 3){
			buddnistCeremonyCommodityOrderList = commodityOrderService.findByTempleIdAndAbbotId(templeId, pageNumber, showListNum,0);
		}
		List<String> buddnistCeremonyCommodityOrderList2 = new ArrayList<String>();
		for(BuddnistCeremonyCommodityOrderModel buddnistCeremonyCommodityOrder:buddnistCeremonyCommodityOrderList){
			UserModel buyUser = userService.getModel(buddnistCeremonyCommodityOrder.getUser_id());
			HashMap<String,Object> buddnistCeremonyUser = new HashMap<String,Object>();
			if(buddnistCeremonyCommodityOrder.getBuyer_info_id() != 0){
				BuddnistCeremonyBuyerInfoModel buddnistCeremonyBuyerInfo = buddnistCeremonyBuyerInfoService.getModel(buddnistCeremonyCommodityOrder.getBuyer_info_id());
				if(buddnistCeremonyBuyerInfo != null){
					buddnistCeremonyUser.put("name", buddnistCeremonyBuyerInfo.getName());
				}
			}
			if(buyUser != null){
				buddnistCeremonyUser.put("head_img", buyUser.getHead_img());
				buddnistCeremonyUser.put("province", buyUser.getProvince());
				buddnistCeremonyUser.put("city", buyUser.getCity());
				if(buddnistCeremonyUser.get("name") == null || buddnistCeremonyUser.get("name").equals("")){
					buddnistCeremonyUser.put("name", buyUser.getNick_name());
				}
				if(buyUser.getChanzaiMobile().equals("") || buyUser.getChanzaiMobile() == null){
					buddnistCeremonyUser.put("userType", 1);
				}
				else{
					buddnistCeremonyUser.put("userType", 2);				
				}
			}
			JSONFromBean buddnistCeremonyCommodityOrderJSON = new JSONFromBean(buddnistCeremonyCommodityOrder, ZizaihomeJSONUtils.emptyNullFilter());
			buddnistCeremonyCommodityOrderJSON.addPropertyObj("user", buddnistCeremonyUser,ZizaihomeJSONUtils.emptyNullFilter());
			try {
				buddnistCeremonyCommodityOrderList2.add(buddnistCeremonyCommodityOrderJSON.buildString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		json.put("data", buddnistCeremonyCommodityOrderList2);
		if(buddnistCeremonyCommodityOrderList2.size() >= pageNumber){
			json.put("pageNumber", pageNumber+1);
		}
		else{
			json.put("pageNumber", -1);
		}
		return succRequest("成功",json);
	}

	@Override
	protected JSONObject postMethod(JSONObject json) {
		return this.getMethod(json);
	}

}
