package com.zizaihome.api.resources.commodity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.restlet.Request;

import com.zizaihome.api.db.model.BuddnistCeremonyCommodityModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityPicsModel;
import com.zizaihome.api.db.model.TempleModel;
import com.zizaihome.api.db.model.ZizaijiaTagContentModel;
import com.zizaihome.api.db.model.ZizaijiaTagModel;
import com.zizaihome.api.resources.base.BaseResource;
import com.zizaihome.api.service.BuddnistCeremonyCommodityPicsService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityService;
import com.zizaihome.api.service.TempleService;
import com.zizaihome.api.service.ZizaijiaTagContentService;
import com.zizaihome.api.service.ZizaijiaTagService;

public class GetCeremonyCommodityListResource extends BaseResource {

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected JSONObject getMethod(JSONObject json) {
		ZizaijiaTagContentService zizaijiaTagContentService = new ZizaijiaTagContentService();
		ZizaijiaTagService zizaijiaTagService = new ZizaijiaTagService();
		BuddnistCeremonyCommodityService buddnistCeremonyCommodityService = new BuddnistCeremonyCommodityService();
		BuddnistCeremonyCommodityPicsService buddnistCeremonyCommodityPicsService = new BuddnistCeremonyCommodityPicsService();
		TempleService templeService = new TempleService();
		int pageNumber = getParameter("pageNumber",0);
		int pageSize = getParameter("pageSize",0);
		int type = getParameter("type",0);
		int tagId = getParameter("tagId",0);
		List<ZizaijiaTagContentModel> zizaijiaTagContentList = zizaijiaTagContentService.findByTagId(tagId);
		List<Integer> tagIds = new ArrayList<Integer>();
		for(ZizaijiaTagContentModel zizaijiaTagContent:zizaijiaTagContentList){
			tagIds.add(zizaijiaTagContent.getContentId());
		}
		HashMap<Integer,ZizaijiaTagModel> tagMap = new HashMap<Integer,ZizaijiaTagModel>();
		List<ZizaijiaTagModel> zizaijiaTagModelList = zizaijiaTagService.findAllTag();
		for(ZizaijiaTagModel zizaijiaTagModel:zizaijiaTagModelList){
			tagMap.put(zizaijiaTagModel.getId(), zizaijiaTagModel);
		}
		List<BuddnistCeremonyCommodityModel> buddnistCeremonyCommodityModelList = buddnistCeremonyCommodityService.getCommodityList(pageNumber, pageSize, type, tagIds);
		JSONArray dataList = new JSONArray();
		for(BuddnistCeremonyCommodityModel buddnistCeremonyCommodityModel:buddnistCeremonyCommodityModelList){
			JSONObject dataMap = new JSONObject();
			List<ZizaijiaTagContentModel> zizaijiaTagContentList2 = zizaijiaTagContentService.findByCommodityId(buddnistCeremonyCommodityModel.getId());
			JSONArray tagList = new JSONArray();
			for(ZizaijiaTagContentModel zizaijiaTagContent:zizaijiaTagContentList2){
				if(tagMap.containsKey(zizaijiaTagContent.getTagId())){
					JSONObject tMap = new JSONObject();
					ZizaijiaTagModel tag = tagMap.get(zizaijiaTagContent.getTagId());
					tMap.put("tagId", tag.getId());
					tMap.put("name", tag.getName());
					tagList.add(tMap);
				}
			}
			dataMap.put("tagList", tagList);
			dataMap.put("name", buddnistCeremonyCommodityModel.getName());
			List<BuddnistCeremonyCommodityPicsModel> picList = buddnistCeremonyCommodityPicsService.findByCommodityId(buddnistCeremonyCommodityModel.getId());
			dataMap.put("pic", picList.get(0).getPic_url());
			TempleModel temple = templeService.getModel(buddnistCeremonyCommodityModel.getTemple_id());
			dataMap.put("templeName", temple.getName());
			if(buddnistCeremonyCommodityModel.getStart_time() != null && buddnistCeremonyCommodityModel.getEnd_time() != null){
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
				String str=sdf.format(buddnistCeremonyCommodityModel.getStart_time())+"-"+sdf.format(buddnistCeremonyCommodityModel.getEnd_time()); 
				dataMap.put("timeStr", str);
			}
			else{
				dataMap.put("timeStr", "");
			}
			if(buddnistCeremonyCommodityModel.getStart_time() != null){
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
				String str=sdf.format(buddnistCeremonyCommodityModel.getStart_time()); 
				dataMap.put("startTime", str);
			}
			else{
				dataMap.put("startTime", "");
			}
			if(buddnistCeremonyCommodityModel.getEnd_time() != null){
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
				java.util.Date date=new java.util.Date();  
				String str=sdf.format(buddnistCeremonyCommodityModel.getEnd_time()); 
				dataMap.put("endTime", str);
			}
			else{
				dataMap.put("endTime", "");
			}
			dataMap.put("isEnd", buddnistCeremonyCommodityModel.getIs_end());
			dataMap.put("url", "https://wx.zizaihome.com/commodity/commodityAuth?commodityId="+buddnistCeremonyCommodityModel.getId());
			dataList.add(dataMap);
		}
		if(dataList.size() >= pageNumber){
			json.put("pageNumber", pageNumber+1);
		}
		else{
			json.put("pageNumber", -1);
		}
		json.put("data", dataList);
		return succRequest("获取成功",json);
	}

	@Override
	protected JSONObject postMethod(JSONObject json) {
		return this.getMethod(json);
	}

}
