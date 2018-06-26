package com.zizaihome.api.resources.commodity;

import java.text.SimpleDateFormat;
import java.util.List;

import org.restlet.Request;

import com.zizaihome.api.db.model.BuddnistCeremonyCommodityModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityPicsModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityPostscriptModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityPostscriptSelectInputModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommoditySubdivideModel;
import com.zizaihome.api.db.model.TempleModel;
import com.zizaihome.api.resources.base.BaseResource;
import com.zizaihome.api.service.BuddnistCeremonyCommodityPicsService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityPostscriptSelectInputService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityPostscriptService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityService;
import com.zizaihome.api.service.BuddnistCeremonyCommoditySubdivideService;
import com.zizaihome.api.service.TempleService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class GetSubdividePostscriptResource extends BaseResource {

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected JSONObject getMethod(JSONObject json) {
		BuddnistCeremonyCommodityService buddnistCeremonyCommodityService = new BuddnistCeremonyCommodityService();
		BuddnistCeremonyCommodityPicsService buddnistCeremonyCommodityPicsService = new BuddnistCeremonyCommodityPicsService();
		BuddnistCeremonyCommoditySubdivideService buddnistCeremonyCommoditySubdivideService = new BuddnistCeremonyCommoditySubdivideService();
		BuddnistCeremonyCommodityPostscriptService buddnistCeremonyCommodityPostscriptService = new BuddnistCeremonyCommodityPostscriptService();
		BuddnistCeremonyCommodityPostscriptSelectInputService buddnistCeremonyCommodityPostscriptSelectInputService = new BuddnistCeremonyCommodityPostscriptSelectInputService();
		TempleService templeService = new TempleService();
		int subdivideId = getParameter("subdivideId",0);
		int commodityId = getParameter("commodityId",0);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		BuddnistCeremonyCommoditySubdivideModel subdivide = buddnistCeremonyCommoditySubdivideService.getModel(subdivideId);
		if(subdivide != null) {
			commodityId = subdivide.getCommodity_id();
		}
		BuddnistCeremonyCommodityModel commodity = buddnistCeremonyCommodityService.getModel(commodityId);
		JSONObject dataJSON = new JSONObject();
		List<BuddnistCeremonyCommodityPicsModel> pics = buddnistCeremonyCommodityPicsService.findByCommodityId(commodity.getId());
		dataJSON.put("commodityId", commodityId);
		dataJSON.put("subdivideId", subdivideId);
		dataJSON.put("commodityName", commodity.getName());
		dataJSON.put("subdivideName", "");
		dataJSON.put("is_conversion", 0);
		dataJSON.put("pic", pics.get(0).getPic_url());
		JSONArray postscriptJSONList = new JSONArray();
		List<BuddnistCeremonyCommodityPostscriptModel> postscriptList = buddnistCeremonyCommodityPostscriptService.findByCommodityId(commodityId);
		for(BuddnistCeremonyCommodityPostscriptModel postscript:postscriptList) {
			JSONObject postscriptJSON = new JSONObject();
			postscriptJSON.put("id", postscript.getId());
			postscriptJSON.put("name", postscript.getName());
			postscriptJSON.put("is_must", postscript.getIs_must());
			postscriptJSON.put("prompt_text", postscript.getPrompt_text());
			postscriptJSON.put("data_change_type", postscript.getData_change_type());
			postscriptJSON.put("font_length", postscript.getFont_length());
			postscriptJSON.put("pic_num", postscript.getPic_num());
			postscriptJSON.put("describe", postscript.getDescribe());
			postscriptJSON.put("commodity_id", postscript.getCommodity_id());
			postscriptJSON.put("subdivide_id", postscript.getSubdivide_id());
			postscriptJSON.put("update_time", sdf.format(postscript.getUpdate_time()));
			postscriptJSON.put("add_time", sdf.format(postscript.getAdd_time()));
			postscriptJSON.put("op_status", postscript.getOp_status());
			postscriptJSON.put("input_type", postscript.getInput_type());
			JSONArray postscriptSelectInputJSONList = new JSONArray();
			if(postscript.getInput_type() == 3){
				List<BuddnistCeremonyCommodityPostscriptSelectInputModel> buddnistCeremonyCommodityPostscriptSelectInputList = buddnistCeremonyCommodityPostscriptSelectInputService.findByPostscriptId(postscript.getId());
				for(BuddnistCeremonyCommodityPostscriptSelectInputModel buddnistCeremonyCommodityPostscriptSelectInput:buddnistCeremonyCommodityPostscriptSelectInputList) {
					JSONObject postscriptSelectInputJSON = new JSONObject();
					postscriptSelectInputJSON.put("id", buddnistCeremonyCommodityPostscriptSelectInput.getId());
					postscriptSelectInputJSON.put("pposiscript_id", buddnistCeremonyCommodityPostscriptSelectInput.getPposiscript_id());
					postscriptSelectInputJSON.put("name", buddnistCeremonyCommodityPostscriptSelectInput.getName());
					postscriptSelectInputJSONList.add(postscriptSelectInputJSON);
				}
			}
			postscriptJSON.put("selectInput", postscriptSelectInputJSONList);
			postscriptJSONList.add(postscriptJSON);
		}
		JSONArray conversionSubdivideList = new JSONArray();
		dataJSON.put("conversionSubdivideList", conversionSubdivideList);
		if(subdivide != null) {
			dataJSON.put("pic", subdivide.getPic_url());
			dataJSON.put("subdivideName", subdivide.getName());
			dataJSON.put("is_conversion", subdivide.getIs_conversion());
			if(subdivide.getIs_conversion() == 1) {
				JSONArray conversionSubdivideJsonArray = JSONArray.fromObject(subdivide.getConversion_subdivide_json());
				for(int i=0;i<conversionSubdivideJsonArray.size();i++) {
					JSONObject conversionSubdivideJson = conversionSubdivideJsonArray.getJSONObject(i);
					List<BuddnistCeremonyCommodityPostscriptModel> conversionCommodityPostscriptList = buddnistCeremonyCommodityPostscriptService.findByCommodityId(conversionSubdivideJson.getInt("commodityId"));
					JSONArray conversionPostscriptJSONList = new JSONArray();
					//对转单佛事的附言进行数据组装
					for(BuddnistCeremonyCommodityPostscriptModel conversionCommodityPostscript:conversionCommodityPostscriptList) {
						JSONObject postscriptJSON = new JSONObject();
						postscriptJSON.put("id", conversionCommodityPostscript.getId());
						postscriptJSON.put("name", conversionCommodityPostscript.getName());
						postscriptJSON.put("is_must", conversionCommodityPostscript.getIs_must());
						postscriptJSON.put("prompt_text", conversionCommodityPostscript.getPrompt_text());
						postscriptJSON.put("data_change_type", conversionCommodityPostscript.getData_change_type());
						postscriptJSON.put("font_length", conversionCommodityPostscript.getFont_length());
						postscriptJSON.put("pic_num", conversionCommodityPostscript.getPic_num());
						postscriptJSON.put("describe", conversionCommodityPostscript.getDescribe());
						postscriptJSON.put("commodity_id", conversionCommodityPostscript.getCommodity_id());
						postscriptJSON.put("subdivide_id", conversionCommodityPostscript.getSubdivide_id());
						postscriptJSON.put("update_time", sdf.format(conversionCommodityPostscript.getUpdate_time()));
						postscriptJSON.put("add_time", sdf.format(conversionCommodityPostscript.getAdd_time()));
						postscriptJSON.put("op_status", conversionCommodityPostscript.getOp_status());
						postscriptJSON.put("input_type", conversionCommodityPostscript.getInput_type());
						JSONArray postscriptSelectInputJSONList = new JSONArray();
						if(conversionCommodityPostscript.getInput_type() == 3){
							List<BuddnistCeremonyCommodityPostscriptSelectInputModel> buddnistCeremonyCommodityPostscriptSelectInputList = buddnistCeremonyCommodityPostscriptSelectInputService.findByPostscriptId(conversionCommodityPostscript.getId());
							for(BuddnistCeremonyCommodityPostscriptSelectInputModel buddnistCeremonyCommodityPostscriptSelectInput:buddnistCeremonyCommodityPostscriptSelectInputList) {
								JSONObject postscriptSelectInputJSON = new JSONObject();
								postscriptSelectInputJSON.put("id", buddnistCeremonyCommodityPostscriptSelectInput.getId());
								postscriptSelectInputJSON.put("pposiscript_id", buddnistCeremonyCommodityPostscriptSelectInput.getPposiscript_id());
								postscriptSelectInputJSON.put("name", buddnistCeremonyCommodityPostscriptSelectInput.getName());
								postscriptSelectInputJSONList.add(postscriptSelectInputJSON);
							}
						}
						postscriptJSON.put("selectInput", postscriptSelectInputJSONList);
						conversionPostscriptJSONList.add(postscriptJSON);
					}
					List<BuddnistCeremonyCommodityPostscriptModel> conversionSubdividePostscriptList = buddnistCeremonyCommodityPostscriptService.findBySubdivideId(conversionSubdivideJson.getInt("subdivideId"));
					//对转单规格的附言进行数据组装
					for(BuddnistCeremonyCommodityPostscriptModel conversionSubdividePostscript:conversionSubdividePostscriptList) {
						JSONObject postscriptJSON = new JSONObject();
						postscriptJSON.put("id", conversionSubdividePostscript.getId());
						postscriptJSON.put("name", conversionSubdividePostscript.getName());
						postscriptJSON.put("is_must", conversionSubdividePostscript.getIs_must());
						postscriptJSON.put("prompt_text", conversionSubdividePostscript.getPrompt_text());
						postscriptJSON.put("data_change_type", conversionSubdividePostscript.getData_change_type());
						postscriptJSON.put("font_length", conversionSubdividePostscript.getFont_length());
						postscriptJSON.put("pic_num", conversionSubdividePostscript.getPic_num());
						postscriptJSON.put("describe", conversionSubdividePostscript.getDescribe());
						postscriptJSON.put("commodity_id", conversionSubdividePostscript.getCommodity_id());
						postscriptJSON.put("subdivide_id", conversionSubdividePostscript.getSubdivide_id());
						postscriptJSON.put("update_time", sdf.format(conversionSubdividePostscript.getUpdate_time()));
						postscriptJSON.put("add_time", sdf.format(conversionSubdividePostscript.getAdd_time()));
						postscriptJSON.put("op_status", conversionSubdividePostscript.getOp_status());
						postscriptJSON.put("input_type", conversionSubdividePostscript.getInput_type());
						JSONArray postscriptSelectInputJSONList = new JSONArray();
						if(conversionSubdividePostscript.getInput_type() == 3){
							List<BuddnistCeremonyCommodityPostscriptSelectInputModel> buddnistCeremonyCommodityPostscriptSelectInputList = buddnistCeremonyCommodityPostscriptSelectInputService.findByPostscriptId(conversionSubdividePostscript.getId());
							for(BuddnistCeremonyCommodityPostscriptSelectInputModel buddnistCeremonyCommodityPostscriptSelectInput:buddnistCeremonyCommodityPostscriptSelectInputList) {
								JSONObject postscriptSelectInputJSON = new JSONObject();
								postscriptSelectInputJSON.put("id", buddnistCeremonyCommodityPostscriptSelectInput.getId());
								postscriptSelectInputJSON.put("pposiscript_id", buddnistCeremonyCommodityPostscriptSelectInput.getPposiscript_id());
								postscriptSelectInputJSON.put("name", buddnistCeremonyCommodityPostscriptSelectInput.getName());
								postscriptSelectInputJSONList.add(postscriptSelectInputJSON);
							}
						}
						postscriptJSON.put("selectInput", postscriptSelectInputJSONList);
						conversionPostscriptJSONList.add(postscriptJSON);
					}
					
					BuddnistCeremonyCommoditySubdivideModel conversionSubdivideModel = buddnistCeremonyCommoditySubdivideService.getModel(conversionSubdivideJson.getInt("subdivideId"));
//					JSONFromBean conversionSubdivideJSON = new JSONFromBean(conversionSubdivideModel, ZizaihomeJSONUtils.emptyNullFilter());
					JSONObject conversionSubdivideJSON = new JSONObject();
					conversionSubdivideJSON.put("pic", conversionSubdivideModel.getPic_url());
					conversionSubdivideJSON.put("name", conversionSubdivideModel.getName());
					conversionSubdivideJSON.put("id", conversionSubdivideModel.getId());
					conversionSubdivideJSON.put("conversionSubdivideModel", conversionPostscriptJSONList);
					TempleModel temple = templeService.getModel(conversionSubdivideJson.getInt("templeId"));
					int isSetConversionSubdivideList = 0;
					for(int b=0;b<conversionSubdivideList.size();b++) {
						JSONObject conversionSubdivide = conversionSubdivideList.getJSONObject(b);
						if(conversionSubdivide.getString("templeName").equals(temple.getName())) {
							conversionSubdivideList.getJSONObject(b).getJSONArray("subdivideList").add(conversionSubdivideJSON);
							isSetConversionSubdivideList = 1;
						}
					}
					if(isSetConversionSubdivideList == 0) {
						JSONObject conversionSubdivide = new JSONObject();
						JSONArray newSubdivideList = new JSONArray();
						newSubdivideList.add(conversionSubdivideJSON);
						conversionSubdivide.put("templeName", temple.getName());
						conversionSubdivide.put("subdivideList", newSubdivideList);
						conversionSubdivideList.add(conversionSubdivide);
					}
				}
				dataJSON.put("conversionSubdivideList", conversionSubdivideList);
			}
			else {
				List<BuddnistCeremonyCommodityPostscriptModel> subPostscriptList = buddnistCeremonyCommodityPostscriptService.findBySubdivideId(subdivide.getId());
				for(BuddnistCeremonyCommodityPostscriptModel subPostscript:subPostscriptList) {
					JSONObject postscriptJSON = new JSONObject();
					postscriptJSON.put("id", subPostscript.getId());
					postscriptJSON.put("name", subPostscript.getName());
					postscriptJSON.put("is_must", subPostscript.getIs_must());
					postscriptJSON.put("prompt_text", subPostscript.getPrompt_text());
					postscriptJSON.put("data_change_type", subPostscript.getData_change_type());
					postscriptJSON.put("font_length", subPostscript.getFont_length());
					postscriptJSON.put("pic_num", subPostscript.getPic_num());
					postscriptJSON.put("describe", subPostscript.getDescribe());
					postscriptJSON.put("commodity_id", subPostscript.getCommodity_id());
					postscriptJSON.put("subdivide_id", subPostscript.getSubdivide_id());
					postscriptJSON.put("update_time", sdf.format(subPostscript.getUpdate_time()));
					postscriptJSON.put("add_time", sdf.format(subPostscript.getAdd_time()));
					postscriptJSON.put("op_status", subPostscript.getOp_status());
					postscriptJSON.put("input_type", subPostscript.getInput_type());
					JSONArray postscriptSelectInputJSONList = new JSONArray();
					if(subPostscript.getInput_type() == 3){
						List<BuddnistCeremonyCommodityPostscriptSelectInputModel> buddnistCeremonyCommodityPostscriptSelectInputList = buddnistCeremonyCommodityPostscriptSelectInputService.findByPostscriptId(subPostscript.getId());
						for(BuddnistCeremonyCommodityPostscriptSelectInputModel buddnistCeremonyCommodityPostscriptSelectInput:buddnistCeremonyCommodityPostscriptSelectInputList) {
							JSONObject postscriptSelectInputJSON = new JSONObject();
							postscriptSelectInputJSON.put("id", buddnistCeremonyCommodityPostscriptSelectInput.getId());
							postscriptSelectInputJSON.put("pposiscript_id", buddnistCeremonyCommodityPostscriptSelectInput.getPposiscript_id());
							postscriptSelectInputJSON.put("name", buddnistCeremonyCommodityPostscriptSelectInput.getName());
							postscriptSelectInputJSONList.add(postscriptSelectInputJSON);
						}
					}
					postscriptJSON.put("selectInput", postscriptSelectInputJSONList);
					postscriptJSONList.add(postscriptJSON);
				}
			}
		}
		dataJSON.put("postscript", postscriptJSONList);
		json.put("data", dataJSON);
		return succRequest("获取成功",json);
	}

	@Override
	protected JSONObject postMethod(JSONObject json) {
		return this.getMethod(json);
	}

}
