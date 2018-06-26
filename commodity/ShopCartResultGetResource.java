package com.zizaihome.api.resources.commodity;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.restlet.Request;

import com.zizaihome.api.db.model.BuddnistCeremonyCommodityModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityOrderModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityPicsModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityPostscriptModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityPostscriptSelectInputModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityShoppingCartModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommoditySubdivideModel;
import com.zizaihome.api.db.model.TempleModel;
import com.zizaihome.api.resources.base.BaseResource;
import com.zizaihome.api.service.BuddnistCeremonyCommodityOrderService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityPicsService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityPostscriptSelectInputService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityPostscriptService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityShoppingCartService;
import com.zizaihome.api.service.BuddnistCeremonyCommoditySubdivideService;
import com.zizaihome.api.service.TempleService;
import com.zizaihome.api.utils.JSONFromBean;
import com.zizaihome.api.utils.ZizaihomeJSONUtils;

public class ShopCartResultGetResource extends BaseResource {

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected JSONObject getMethod(JSONObject json) {
		BuddnistCeremonyCommodityOrderService buddnistCeremonyCommodityOrderService = new BuddnistCeremonyCommodityOrderService();
		BuddnistCeremonyCommodityService buddnistCeremonyCommodityService = new BuddnistCeremonyCommodityService();
		BuddnistCeremonyCommoditySubdivideService buddnistCeremonyCommoditySubdivideService = new BuddnistCeremonyCommoditySubdivideService();
		TempleService templeService = new TempleService();
		BuddnistCeremonyCommodityPostscriptService buddnistCeremonyCommodityPostscriptService = new BuddnistCeremonyCommodityPostscriptService();
		BuddnistCeremonyCommodityPostscriptSelectInputService buddnistCeremonyCommodityPostscriptSelectInputService = new BuddnistCeremonyCommodityPostscriptSelectInputService();
		BuddnistCeremonyCommodityPicsService buddnistCeremonyCommodityPicsService = new BuddnistCeremonyCommodityPicsService();
		BuddnistCeremonyCommodityShoppingCartService buddnistCeremonyCommodityShoppingCartService = new BuddnistCeremonyCommodityShoppingCartService();
		int orderId = getParameter("orderId",0);
		List<BuddnistCeremonyCommodityOrderModel> buddnistCeremonyCommodityOrderModelList = buddnistCeremonyCommodityOrderService.findByOrderId(orderId);
		JSONArray dataList = new JSONArray();
		for(BuddnistCeremonyCommodityOrderModel buddnistCeremonyCommodityOrderModel:buddnistCeremonyCommodityOrderModelList){
			BuddnistCeremonyCommodityModel buddnistCeremonyCommodity = buddnistCeremonyCommodityService.getModel(buddnistCeremonyCommodityOrderModel.getCommodity_id());
			TempleModel temple = templeService.getModel(buddnistCeremonyCommodity.getTemple_id());
			List<BuddnistCeremonyCommodityPostscriptModel> buddnistCeremonyCommodityPostscriptList = buddnistCeremonyCommodityPostscriptService.findByCommodityId(buddnistCeremonyCommodity.getId());
			List<String> buddnistCeremonyCommodityPostscriptJSONList = new ArrayList<String>();
			for(BuddnistCeremonyCommodityPostscriptModel buddnistCeremonyCommodityPostscript:buddnistCeremonyCommodityPostscriptList){
				JSONFromBean buddnistCeremonyCommodityPostscriptJSON = new JSONFromBean(buddnistCeremonyCommodityPostscript, ZizaihomeJSONUtils.emptyNullFilter());
				if(buddnistCeremonyCommodityPostscript.getInput_type() == 3){
					List<BuddnistCeremonyCommodityPostscriptSelectInputModel> buddnistCeremonyCommodityPostscriptSelectInputList = buddnistCeremonyCommodityPostscriptSelectInputService.findByPostscriptId(buddnistCeremonyCommodityPostscript.getId());
					JSONFromBean buddnistCeremonyCommodityPostscriptSelectInputJSONList = new JSONFromBean(buddnistCeremonyCommodityPostscriptSelectInputList, ZizaihomeJSONUtils.emptyNullFilter());
					try {
						buddnistCeremonyCommodityPostscriptJSON.addPropertyObj("selectInput", buddnistCeremonyCommodityPostscriptSelectInputJSONList.buildString(), null);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				try {
					buddnistCeremonyCommodityPostscriptJSONList.add(buddnistCeremonyCommodityPostscriptJSON.buildString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			String subdividename = "";
			int stock = buddnistCeremonyCommodity.getStock();
			String picUrl = "";
			if(buddnistCeremonyCommodityOrderModel.getSubdiride_id() != 0){
				BuddnistCeremonyCommoditySubdivideModel buddnistCeremonyCommoditySubdivide = buddnistCeremonyCommoditySubdivideService.getModel(buddnistCeremonyCommodityOrderModel.getSubdiride_id());
				stock = buddnistCeremonyCommoditySubdivide.getStock();
				picUrl = buddnistCeremonyCommoditySubdivide.getPic_url();
				subdividename = buddnistCeremonyCommoditySubdivide.getName();
				List<BuddnistCeremonyCommodityPostscriptModel> buddnistCeremonyCommodityPostscriptList2 = buddnistCeremonyCommodityPostscriptService.findBySubdivideId(buddnistCeremonyCommodityOrderModel.getSubdiride_id());
				for(BuddnistCeremonyCommodityPostscriptModel buddnistCeremonyCommodityPostscript:buddnistCeremonyCommodityPostscriptList2){
					JSONFromBean buddnistCeremonyCommodityPostscriptJSON = new JSONFromBean(buddnistCeremonyCommodityPostscript, ZizaihomeJSONUtils.emptyNullFilter());
					if(buddnistCeremonyCommodityPostscript.getInput_type() == 3){
						List<BuddnistCeremonyCommodityPostscriptSelectInputModel> buddnistCeremonyCommodityPostscriptSelectInputList = buddnistCeremonyCommodityPostscriptSelectInputService.findByPostscriptId(buddnistCeremonyCommodityPostscript.getId());
						JSONFromBean buddnistCeremonyCommodityPostscriptSelectInputJSONList = new JSONFromBean(buddnistCeremonyCommodityPostscriptSelectInputList, ZizaihomeJSONUtils.emptyNullFilter());
						try {
							buddnistCeremonyCommodityPostscriptJSON.addPropertyObj("selectInput", buddnistCeremonyCommodityPostscriptSelectInputJSONList.buildString(), null);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					try {
						buddnistCeremonyCommodityPostscriptJSONList.add(buddnistCeremonyCommodityPostscriptJSON.buildString());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			else{
				List<BuddnistCeremonyCommodityPicsModel> buddnistCeremonyCommodityPicsModelList = buddnistCeremonyCommodityPicsService.findByCommodityId(buddnistCeremonyCommodity.getId());
				picUrl = buddnistCeremonyCommodityPicsModelList.get(0).getPic_url();
			}
			int isHaveTempleName = 0;
			for(int i=0;i<dataList.size();i++){
				JSONObject data = dataList.getJSONObject(i);
				if(data.getString("templeName") == temple.getName()){
					isHaveTempleName = 1;
					JSONObject dataMap = new JSONObject();
					dataMap.put("name", buddnistCeremonyCommodity.getName());
					dataMap.put("feedbackType", buddnistCeremonyCommodity.getFeedbackType());
					dataMap.put("orderId", buddnistCeremonyCommodityOrderModel.getId());
					dataMap.put("postscript", buddnistCeremonyCommodityPostscriptJSONList);
					dataMap.put("subdividename", subdividename);
					dataMap.put("price", buddnistCeremonyCommodityOrderModel.getPrice());
					dataMap.put("explain", buddnistCeremonyCommodity.getExplain());
					dataMap.put("num", buddnistCeremonyCommodityOrderModel.getBuy_num());
					dataMap.put("pic_url", picUrl);
					dataMap.put("stock", stock);
					dataMap.put("temple_index", 0);
					dataMap.put("postscriptStr", buddnistCeremonyCommodityOrderModel.getPosiscript());
					data.getJSONArray("commodityItem").add(dataMap);
				}
			}
			if(isHaveTempleName == 0){
				JSONObject dataMap1 = new JSONObject();
				dataMap1.put("templeName", temple.getName());
				JSONArray commodityItem = new JSONArray();
				JSONObject dataMap = new JSONObject();
				dataMap.put("name", buddnistCeremonyCommodity.getName());
				dataMap.put("feedbackType", buddnistCeremonyCommodity.getFeedbackType());
				dataMap.put("orderId", buddnistCeremonyCommodityOrderModel.getId());
				dataMap.put("postscript", buddnistCeremonyCommodityPostscriptJSONList);
				dataMap.put("subdividename", subdividename);
				dataMap.put("price", buddnistCeremonyCommodityOrderModel.getPrice());
				dataMap.put("explain", buddnistCeremonyCommodity.getExplain());
				dataMap.put("num", buddnistCeremonyCommodityOrderModel.getBuy_num());
				dataMap.put("pic_url", picUrl);
				dataMap.put("stock", stock);
				dataMap.put("temple_index", 0);
				dataMap.put("postscriptStr", buddnistCeremonyCommodityOrderModel.getPosiscript());
				commodityItem.add(dataMap);
				dataMap1.put("commodityItem", commodityItem);
				dataMap1.put("index", 0);
				dataList.add(dataMap1);
			}
			BuddnistCeremonyCommodityShoppingCartModel buddnistCeremonyCommodityShoppingCartModel = buddnistCeremonyCommodityShoppingCartService.findByUserIdAndcommodityIdAndSubdivideId(userId, buddnistCeremonyCommodityOrderModel.getCommodity_id(), buddnistCeremonyCommodityOrderModel.getSubdiride_id());
			if(buddnistCeremonyCommodityShoppingCartModel != null){
				buddnistCeremonyCommodityShoppingCartModel.setOp_status(-1);
				buddnistCeremonyCommodityShoppingCartService.updateAny(buddnistCeremonyCommodityShoppingCartModel);
			}
		}
		json.put("data", dataList);
		return succRequest("获取成功",json);
	}

	@Override
	protected JSONObject postMethod(JSONObject json) {
		return this.getMethod(json);
	}

}
