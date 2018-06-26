package com.zizaihome.api.resources.website;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import org.restlet.Request;

import com.zizaihome.api.db.model.BuddnistCeremonyCommodityModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityPicsModel;
import com.zizaihome.api.db.model.ZizaijiaTempleWebsiteCalendarListModel;
import com.zizaihome.api.resources.base.BaseResource;
import com.zizaihome.api.service.BuddnistCeremonyCommodityPicsService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityService;
import com.zizaihome.api.service.ZizaijiaTempleWebsiteCalendarDayService;
import com.zizaihome.api.service.ZizaijiaTempleWebsiteCalendarListService;
import com.zizaihome.api.service.ZizaijiaTempleWebsiteCalendarService;

public class TemplateWebsiteCalendarDayResource extends BaseResource {

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected JSONObject getMethod(JSONObject json) {
		ZizaijiaTempleWebsiteCalendarListService templeWebsiteCalendarListService = new ZizaijiaTempleWebsiteCalendarListService();
		BuddnistCeremonyCommodityService commodityService = new BuddnistCeremonyCommodityService();
		BuddnistCeremonyCommodityPicsService commodityPicsService = new BuddnistCeremonyCommodityPicsService();
		int templeId = getParameter("templeId",0);
		String checkdate = getParameter("date","");
		int calendarId = getParameter("calendarId",0);
		List<ZizaijiaTempleWebsiteCalendarListModel> calendarEventList = templeWebsiteCalendarListService.findByCalendarDateAndCalendarIdAndTempleId(checkdate, calendarId, templeId);
        List<HashMap<String,Object>> eventList = new ArrayList<HashMap<String,Object>>();
        if(calendarEventList.size() > 0){
            for(ZizaijiaTempleWebsiteCalendarListModel calendarEvent:calendarEventList){
            	HashMap<String,Object> eventMap = new HashMap<String,Object>();
            	eventMap.put("id", calendarEvent.getId());
            	eventMap.put("title", calendarEvent.getTitle());
            	eventMap.put("cover_pic", calendarEvent.getCover_pic());
            	eventMap.put("commodityId", calendarEvent.getCommodityId());
            	if(calendarEvent.getCommodityId() > 0){
            		BuddnistCeremonyCommodityModel commodity = commodityService.getModel(calendarEvent.getCommodityId());
            		List<BuddnistCeremonyCommodityPicsModel> pics = commodityPicsService.findByCommodityId(commodity.getId());
            		eventMap.put("commodityName", commodity.getName());
            		eventMap.put("commodityPic", pics.get(0).getPic_url());
            		eventMap.put("link","https://wx.zizaihome.com/commodity/commodityAuth?commodityId=" + commodity.getId());
            		
            	}
            	else{
            		eventMap.put("commodityName", "");
            		eventMap.put("commodityPic", "");
            		eventMap.put("link","");
            	}
            	eventList.add(eventMap);
            }
        }
        json.put("data", eventList);
		return succRequest("获取成功",json);
	}

	@Override
	protected JSONObject postMethod(JSONObject json) {
		return this.getMethod(json);
	}

}
