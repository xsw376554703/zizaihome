package com.zizaihome.api.resources.commodity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import org.restlet.Request;

import com.zizaihome.api.db.model.BuddnistCeremonyCommodityModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityPicsModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityPostscriptModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityPostscriptSelectInputModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityShoppingCartModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommoditySubdivideModel;
import com.zizaihome.api.db.model.TempleModel;
import com.zizaihome.api.db.model.UserModel;
import com.zizaihome.api.resources.base.BaseResource;
import com.zizaihome.api.service.BuddnistCeremonyCommodityPicsService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityPostscriptSelectInputService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityPostscriptService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityShoppingCartService;
import com.zizaihome.api.service.BuddnistCeremonyCommoditySubdivideService;
import com.zizaihome.api.service.TempleService;
import com.zizaihome.api.service.UserService;
import com.zizaihome.api.utils.JSONFromBean;
import com.zizaihome.api.utils.ZizaihomeJSONUtils;

public class GetShoppingCartResource extends BaseResource {

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected JSONObject getMethod(JSONObject json) {
		UserService userService = new UserService();
		BuddnistCeremonyCommodityService buddnistCeremonyCommodityService = new BuddnistCeremonyCommodityService();
		BuddnistCeremonyCommodityShoppingCartService buddnistCeremonyCommodityShoppingCartService = new BuddnistCeremonyCommodityShoppingCartService();
		BuddnistCeremonyCommoditySubdivideService buddnistCeremonyCommoditySubdivideService = new BuddnistCeremonyCommoditySubdivideService();
		BuddnistCeremonyCommodityPicsService buddnistCeremonyCommodityPicsService = new BuddnistCeremonyCommodityPicsService();
		BuddnistCeremonyCommodityPostscriptService buddnistCeremonyCommodityPostscriptService = new BuddnistCeremonyCommodityPostscriptService();
		BuddnistCeremonyCommodityPostscriptSelectInputService buddnistCeremonyCommodityPostscriptSelectInputService = new BuddnistCeremonyCommodityPostscriptSelectInputService();
		UserModel user = userService.getModel(userId);
		TempleService templeService = new TempleService();
		List<BuddnistCeremonyCommodityShoppingCartModel> buddnistCeremonyCommodityShoppingCartModelList = buddnistCeremonyCommodityShoppingCartService.findByUserId(user.getId());
		List<HashMap<String,Object>> dataList = new ArrayList<HashMap<String,Object>>();
		HashMap<Integer,Integer> templeNumMap = new HashMap<Integer,Integer>();
		for(BuddnistCeremonyCommodityShoppingCartModel buddnistCeremonyCommodityShoppingCartModel:buddnistCeremonyCommodityShoppingCartModelList){
			BuddnistCeremonyCommodityModel buddnistCeremonyCommodityModel = buddnistCeremonyCommodityService.getModel(buddnistCeremonyCommodityShoppingCartModel.getCommodity_id());
			int commodityIsEnd = 0;
			//组装佛事还有多少天结束
			long differTime = (buddnistCeremonyCommodityModel.getEnd_time().getTime()-new Date().getTime());
			if(differTime/60 < 0){
				commodityIsEnd = 1;
			}
			JSONObject commodityMap = new JSONObject();
			if(buddnistCeremonyCommodityShoppingCartModel.getSubdivide_id() == 0){
				if(buddnistCeremonyCommodityModel.getPrice() <= 0){
					buddnistCeremonyCommodityShoppingCartModel.setOp_status(-1);
					buddnistCeremonyCommodityShoppingCartService.updateAny(buddnistCeremonyCommodityShoppingCartModel);
					continue;
				}
				BuddnistCeremonyCommodityPicsModel buddnistCeremonyCommodityPicsModel = buddnistCeremonyCommodityPicsService.findByCommodityId(buddnistCeremonyCommodityModel.getId()).get(0);
				commodityMap.put("pic_url", buddnistCeremonyCommodityPicsModel.getPic_url());
				commodityMap.put("price", buddnistCeremonyCommodityModel.getPrice());
				commodityMap.put("stock", buddnistCeremonyCommodityModel.getStock());
				commodityMap.put("name", buddnistCeremonyCommodityModel.getName());
				commodityMap.put("subdividename", "");
				commodityMap.put("id", buddnistCeremonyCommodityShoppingCartModel.getId());
				commodityMap.put("explain", buddnistCeremonyCommodityModel.getExplain());
				commodityMap.put("commodityId", buddnistCeremonyCommodityModel.getId());
				commodityMap.put("isEnd", commodityIsEnd);
				List<BuddnistCeremonyCommodityPostscriptModel> buddnistCeremonyCommodityPostscriptList = buddnistCeremonyCommodityPostscriptService.findByCommodityId(buddnistCeremonyCommodityModel.getId());
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
				commodityMap.put("postscript", buddnistCeremonyCommodityPostscriptJSONList.toString());
			}
			else{
				BuddnistCeremonyCommoditySubdivideModel buddnistCeremonyCommoditySubdivideModel = buddnistCeremonyCommoditySubdivideService.getModel(buddnistCeremonyCommodityShoppingCartModel.getSubdivide_id());
				if(buddnistCeremonyCommoditySubdivideModel.getPrice() <= 0){
					buddnistCeremonyCommodityShoppingCartModel.setOp_status(-1);
					buddnistCeremonyCommodityShoppingCartService.updateAny(buddnistCeremonyCommodityShoppingCartModel);
					continue;
				}
				commodityMap.put("pic_url", buddnistCeremonyCommoditySubdivideModel.getPic_url());
				commodityMap.put("price", buddnistCeremonyCommoditySubdivideModel.getPrice());
				commodityMap.put("stock", buddnistCeremonyCommoditySubdivideModel.getStock());
				commodityMap.put("name", buddnistCeremonyCommodityModel.getName());
				commodityMap.put("subdividename", buddnistCeremonyCommoditySubdivideModel.getName());
				commodityMap.put("id", buddnistCeremonyCommodityShoppingCartModel.getId());
				commodityMap.put("explain", buddnistCeremonyCommoditySubdivideModel.getExplain());
				commodityMap.put("commodityId", buddnistCeremonyCommodityModel.getId());
				commodityMap.put("isEnd", commodityIsEnd);
				List<BuddnistCeremonyCommodityPostscriptModel> buddnistCeremonyCommoditySubdividePostscriptList = buddnistCeremonyCommodityPostscriptService.findBySubdivideId(buddnistCeremonyCommoditySubdivideModel.getId());
				List<String> buddnistCeremonyCommodityPostscriptJSONList = new ArrayList<String>();
				for(BuddnistCeremonyCommodityPostscriptModel buddnistCeremonyCommoditySubdividePostscript:buddnistCeremonyCommoditySubdividePostscriptList){
					JSONFromBean buddnistCeremonyCommodityPostscriptJSON = new JSONFromBean(buddnistCeremonyCommoditySubdividePostscript, ZizaihomeJSONUtils.emptyNullFilter());
					if(buddnistCeremonyCommoditySubdividePostscript.getInput_type() == 3){
						List<BuddnistCeremonyCommodityPostscriptSelectInputModel> buddnistCeremonyCommodityPostscriptSelectInputList = buddnistCeremonyCommodityPostscriptSelectInputService.findByPostscriptId(buddnistCeremonyCommoditySubdividePostscript.getId());
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
				List<BuddnistCeremonyCommodityPostscriptModel> buddnistCeremonyCommodityPostscriptList = buddnistCeremonyCommodityPostscriptService.findByCommodityId(buddnistCeremonyCommodityModel.getId());
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
				commodityMap.put("postscript", buddnistCeremonyCommodityPostscriptJSONList.toString());
			}
			commodityMap.put("num", buddnistCeremonyCommodityShoppingCartModel.getBuy_num());
			TempleModel templeModel = templeService.getModel(buddnistCeremonyCommodityModel.getTemple_id());
			if(templeNumMap.containsKey(templeModel.getId())){
				List<JSONObject> commodityMapList = (List<JSONObject>) dataList.get(templeNumMap.get(templeModel.getId())).get("commodityItem");
				commodityMapList.add(commodityMap);
			}
			else{
				HashMap<String,Object> dataMap = new HashMap<String,Object>();
				dataMap.put("templeName", templeModel.getName());
				dataMap.put("templeId", templeModel.getId());
				List<JSONObject> commodityMapList = new ArrayList<JSONObject>();
				commodityMapList.add(commodityMap);
				dataMap.put("commodityItem", commodityMapList);
				dataList.add(dataMap);
				templeNumMap.put(templeModel.getId(), dataList.size()-1);
			}
		}
		json.put("data", dataList);
		return succRequest("成功",json);
	}

	@Override
	protected JSONObject postMethod(JSONObject json) {
		return this.getMethod(json);
	}

}
