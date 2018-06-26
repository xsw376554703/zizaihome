package com.zizaihome.api.resources.website;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import org.restlet.Request;

import com.zizaihome.api.db.model.BuddnistCeremonyCommodityModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityPicsModel;
import com.zizaihome.api.db.model.ZizaijiaTempleWebsiteCalendarDayModel;
import com.zizaihome.api.db.model.ZizaijiaTempleWebsiteCalendarListModel;
import com.zizaihome.api.resources.base.BaseResource;
import com.zizaihome.api.service.BuddnistCeremonyCommodityPicsService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityService;
import com.zizaihome.api.service.ZizaijiaTempleWebsiteCalendarDayService;
import com.zizaihome.api.service.ZizaijiaTempleWebsiteCalendarListService;

public class TemplateWebsiteCalendarDayListResource extends BaseResource {

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected JSONObject getMethod(JSONObject json) {
		ZizaijiaTempleWebsiteCalendarListService templeWebsiteCalendarListService = new ZizaijiaTempleWebsiteCalendarListService();
		BuddnistCeremonyCommodityService commodityService = new BuddnistCeremonyCommodityService();
		BuddnistCeremonyCommodityPicsService commodityPicsService = new BuddnistCeremonyCommodityPicsService();
		ZizaijiaTempleWebsiteCalendarDayService templeWebsiteCalendarDayService = new ZizaijiaTempleWebsiteCalendarDayService();
		int templeId = getParameter("templeId",0);
		String startDate = getParameter("startDate","");
		String endDate = getParameter("endDate","");
		int calendarId = getParameter("calendarId",0);
		
		if(startDate.equals("") || endDate.equals("")){
			return failRequest("请传日期参数",json);
		}
		
		List<ZizaijiaTempleWebsiteCalendarDayModel> templeWebsiteCalendarDayModelList = templeWebsiteCalendarDayService.findByStarDateAndEndDateAndCommodityIdAndCalendarIdAndTempleId(startDate, endDate, calendarId, templeId);
		List<HashMap<String,Object>> dateList = new ArrayList<HashMap<String,Object>>();
		for(ZizaijiaTempleWebsiteCalendarDayModel templeWebsiteCalendarDayModel:templeWebsiteCalendarDayModelList){
			HashMap<String,Object> calendarMap = new HashMap<String,Object>();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			calendarMap.put("date", sdf.format(templeWebsiteCalendarDayModel.getCalendarDate()));
			List<ZizaijiaTempleWebsiteCalendarListModel> calendarEventList = templeWebsiteCalendarListService.findByCalendarDateAndCalendarIdAndTempleId(sdf.format(templeWebsiteCalendarDayModel.getCalendarDate()), calendarId, templeId);
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
	        calendarMap.put("events", eventList);
	        dateList.add(calendarMap);
		}
		json.put("data", dateList);
		return succRequest("获取成功",json);
	}

	@Override
	protected JSONObject postMethod(JSONObject json) {
		return this.getMethod(json);
	}

}
