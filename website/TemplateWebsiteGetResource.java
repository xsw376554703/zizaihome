package com.zizaihome.api.resources.website;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import org.restlet.Request;

import com.zizaihome.api.db.model.BuddnistCeremonyBuyerInfoModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityOrderModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityPicsModel;
import com.zizaihome.api.db.model.TempleModel;
import com.zizaihome.api.db.model.UserModel;
import com.zizaihome.api.db.model.ZizaijiaArticleModel;
import com.zizaihome.api.db.model.ZizaijiaTempleWebsiteAbbotModel;
import com.zizaihome.api.db.model.ZizaijiaTempleWebsiteCalendarDayModel;
import com.zizaihome.api.db.model.ZizaijiaTempleWebsiteCalendarListModel;
import com.zizaihome.api.db.model.ZizaijiaTempleWebsiteCalendarModel;
import com.zizaihome.api.db.model.ZizaijiaTempleWebsiteImageTextListModel;
import com.zizaihome.api.db.model.ZizaijiaTempleWebsiteImageTextModel;
import com.zizaihome.api.db.model.ZizaijiaTempleWebsiteIntrodactionModel;
import com.zizaihome.api.db.model.ZizaijiaTempleWebsiteIntrodactionPicsModel;
import com.zizaihome.api.db.model.ZizaijiaTempleWebsiteMeritBoxModel;
import com.zizaihome.api.db.model.ZizaijiaTempleWebsiteQuickEntryLinkModel;
import com.zizaihome.api.db.model.ZizaijiaTempleWebsiteQuickEntryModel;
import com.zizaihome.api.db.model.ZizaijiaTempleWebsiteSortModel;
import com.zizaihome.api.resources.base.BaseResource;
import com.zizaihome.api.service.BuddnistCeremonyBuyerInfoService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityOrderService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityPicsService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityService;
import com.zizaihome.api.service.TempleService;
import com.zizaihome.api.service.UserService;
import com.zizaihome.api.service.ZizaijiaArticleService;
import com.zizaihome.api.service.ZizaijiaTempleWebsiteAbbotService;
import com.zizaihome.api.service.ZizaijiaTempleWebsiteCalendarDayService;
import com.zizaihome.api.service.ZizaijiaTempleWebsiteCalendarListService;
import com.zizaihome.api.service.ZizaijiaTempleWebsiteCalendarService;
import com.zizaihome.api.service.ZizaijiaTempleWebsiteImageTextListService;
import com.zizaihome.api.service.ZizaijiaTempleWebsiteImageTextService;
import com.zizaihome.api.service.ZizaijiaTempleWebsiteIntrodactionPicsService;
import com.zizaihome.api.service.ZizaijiaTempleWebsiteIntrodactionService;
import com.zizaihome.api.service.ZizaijiaTempleWebsiteMeritBoxService;
import com.zizaihome.api.service.ZizaijiaTempleWebsiteQuickEntryLinkService;
import com.zizaihome.api.service.ZizaijiaTempleWebsiteQuickEntryService;
import com.zizaihome.api.service.ZizaijiaTempleWebsiteSortService;
import com.zizaihome.api.utils.ConfigReadUtils;
import com.zizaihome.api.utils.JSONFromBean;
import com.zizaihome.api.utils.LogUtils;
import com.zizaihome.api.utils.ZizaihomeJSONUtils;

public class TemplateWebsiteGetResource extends BaseResource {

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected JSONObject getMethod(JSONObject json) {
		ZizaijiaTempleWebsiteSortService zizaijiaTempleWebsiteSortService = new ZizaijiaTempleWebsiteSortService();
		ZizaijiaTempleWebsiteIntrodactionService zizaijiaTempleWebsiteIntrodactionService = new ZizaijiaTempleWebsiteIntrodactionService();
		ZizaijiaTempleWebsiteIntrodactionPicsService zizaijiaTempleWebsiteIntrodactionPicsService = new ZizaijiaTempleWebsiteIntrodactionPicsService();
		ZizaijiaTempleWebsiteAbbotService abbotService = new ZizaijiaTempleWebsiteAbbotService();
		TempleService templeService = new TempleService();
		UserService userService = new UserService();
		ZizaijiaTempleWebsiteImageTextService imageTextService = new ZizaijiaTempleWebsiteImageTextService();
		ZizaijiaTempleWebsiteImageTextListService textListService = new ZizaijiaTempleWebsiteImageTextListService();
		BuddnistCeremonyCommodityService commodityService = new BuddnistCeremonyCommodityService();
		BuddnistCeremonyCommodityPicsService commodityPicsService = new BuddnistCeremonyCommodityPicsService();
		ZizaijiaArticleService articleService = new ZizaijiaArticleService();
		ZizaijiaTempleWebsiteMeritBoxService meritBoxService = new ZizaijiaTempleWebsiteMeritBoxService();
		BuddnistCeremonyCommodityOrderService commodityOrderService = new BuddnistCeremonyCommodityOrderService();
		BuddnistCeremonyBuyerInfoService buddnistCeremonyBuyerInfoService = new BuddnistCeremonyBuyerInfoService();
		ZizaijiaTempleWebsiteCalendarService templeWebsiteCalendarService = new ZizaijiaTempleWebsiteCalendarService();
		ZizaijiaTempleWebsiteCalendarDayService templeWebsiteCalendarDayService = new ZizaijiaTempleWebsiteCalendarDayService();
		ZizaijiaTempleWebsiteCalendarListService templeWebsiteCalendarListService = new ZizaijiaTempleWebsiteCalendarListService();
		ZizaijiaTempleWebsiteQuickEntryService templeWebsiteQuickEntryService = new ZizaijiaTempleWebsiteQuickEntryService();
		ZizaijiaTempleWebsiteQuickEntryLinkService templeWebsiteQuickEntryLinkService = new ZizaijiaTempleWebsiteQuickEntryLinkService();
		int templeId = getParameter("templeId",0);
		List<Object> dataList = new ArrayList<Object>();
		List<ZizaijiaTempleWebsiteSortModel> sortList = zizaijiaTempleWebsiteSortService.findSortList(templeId);
		TempleModel temple = templeService.getModel(templeId);
		temple.setWebsite_visit_num(temple.getWebsite_visit_num()+1);
		templeService.updateAny(temple);
		UserModel user = userService.getModel(userId);
		if(user != null){
			json.put("nickName", user.getNick_name());
			json.put("headImg", user.getHead_img());
		}
		for(ZizaijiaTempleWebsiteSortModel sort:sortList){
			if(sort.getType() == 1){
				ZizaijiaTempleWebsiteIntrodactionModel introdaction = zizaijiaTempleWebsiteIntrodactionService.getModel(sort.getMessage_id());
				JSONFromBean introdactionJSON = new JSONFromBean(introdaction, ZizaihomeJSONUtils.emptyNullFilter());
				List<ZizaijiaTempleWebsiteIntrodactionPicsModel> zizaijiaTempleWebsiteIntrodactionPicsList = zizaijiaTempleWebsiteIntrodactionPicsService.findByIntrodactionId(introdaction.getId());
				List<String> pics = new ArrayList<String>();
				for(ZizaijiaTempleWebsiteIntrodactionPicsModel zizaijiaTempleWebsiteIntrodactionPics:zizaijiaTempleWebsiteIntrodactionPicsList){
					JSONFromBean zizaijiaTempleWebsiteIntrodactionPicsJSON = new JSONFromBean(zizaijiaTempleWebsiteIntrodactionPics, ZizaihomeJSONUtils.emptyNullFilter());
					try {
						pics.add(zizaijiaTempleWebsiteIntrodactionPicsJSON.buildString());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				introdactionJSON.addPropertyObj("pics", pics, ZizaihomeJSONUtils.emptyNullFilter());
				introdactionJSON.addPropertyObj("type", 1, null);
				try {
					dataList.add(introdactionJSON.buildString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(sort.getType() == 2){
				ZizaijiaTempleWebsiteAbbotModel abbot = abbotService.getModel(sort.getMessage_id());
				if(abbot != null){
					JSONFromBean abbotJSON = new JSONFromBean(abbot, ZizaihomeJSONUtils.emptyNullFilter());
					abbotJSON.addPropertyObj("type", 2, null);
					try {
						dataList.add(abbotJSON.buildString());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			else if(sort.getType() == 3){
				ZizaijiaTempleWebsiteImageTextModel imageText = imageTextService.getModel(sort.getMessage_id());
				if(imageText != null){
					JSONFromBean imageTextJSON = new JSONFromBean(imageText, ZizaihomeJSONUtils.emptyNullFilter());
					List<String> templeWebsiteImageTextList2 = new ArrayList<String>();
					if(imageText.getShow_type() == 1){
						if(imageText.getContent_type() == 1){
							List<BuddnistCeremonyCommodityModel> commodityModelList = null;
							if(imageText.getBuddnist_ceremony_type_id() == 0){
								commodityModelList = commodityService.findByTempleId(0,  imageText.getList_num(), templeId);
							}
							else{
								commodityModelList = commodityService.findByCommodityTypeId(0, imageText.getList_num(), imageText.getBuddnist_ceremony_type_id());
							}
							for(BuddnistCeremonyCommodityModel commodityModel:commodityModelList){
								String pic = commodityPicsService.findByCommodityId(commodityModel.getId()).get(0).getPic_url();
								ZizaijiaTempleWebsiteImageTextListModel imageTextList = new ZizaijiaTempleWebsiteImageTextListModel();
								imageTextList.setBuddnist_ceremony_commodity_id(commodityModel.getId());
								imageTextList.setImage_text_id(imageText.getId());
								imageTextList.setLink_url("https://wx.zizaihome.com/commodity/commodityAuth?commodityId="+commodityModel.getId());
								if(ConfigReadUtils.loadConfigByConfDir("server.properties").getProperty("isTest").equals("1")){
									imageTextList.setLink_url("http://test.zizaihome.com/commodity/commodityAuth?commodityId="+commodityModel.getId());
								}else if(ConfigReadUtils.loadConfigByConfDir("server.properties").getProperty("isTest").equals("2")){
									imageTextList.setLink_url("http://test2.zizaihome.com/commodity/commodityAuth?commodityId="+commodityModel.getId());
								}
								imageTextList.setOp_status(0);
								imageTextList.setPic(pic);
								imageTextList.setTitle(commodityModel.getName());
								int progressType = 2;
								if(commodityModel.getStart_time() != null){
									if((commodityModel.getStart_time().getTime()-new Date().getTime()) > 0){
										progressType = 1;
									}
									else if(commodityModel.getIs_end() == 1){
										progressType = 3;
									}
								}
								else if(commodityModel.getIs_end() == 1){
									progressType = 3;
								}
								JSONFromBean imageTextListJSON = new JSONFromBean(imageTextList, ZizaihomeJSONUtils.emptyNullFilter());
								imageTextListJSON.addPropertyObj("progressType", progressType, null);
								try {
									templeWebsiteImageTextList2.add(imageTextListJSON.buildString());
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						else{
							List<ZizaijiaTempleWebsiteImageTextListModel> imageTextListModelList = textListService.findByImageTextId(imageText.getId());
							List<ZizaijiaTempleWebsiteImageTextListModel> isEndCommodityList = new ArrayList<ZizaijiaTempleWebsiteImageTextListModel>();
							List<ZizaijiaTempleWebsiteImageTextListModel> notEndCommodityList = new ArrayList<ZizaijiaTempleWebsiteImageTextListModel>();
							for(ZizaijiaTempleWebsiteImageTextListModel imageTextListModel:imageTextListModelList){
								SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
								if(sdf.format(imageTextListModel.getUpdate_time()) == sdf.format(new Date())){
									break;
								}
								BuddnistCeremonyCommodityModel commodity = commodityService.getModel(imageTextListModel.getBuddnist_ceremony_commodity_id());
								if(commodity != null){
									if(commodity.getIs_end() == 1){
										isEndCommodityList.add(imageTextListModel);
									}
									else{
										notEndCommodityList.add(imageTextListModel);
									}
								}
							}
//							isEndCommodityList.addAll(notEndCommodityList);
							notEndCommodityList.addAll(isEndCommodityList);
							List<ZizaijiaTempleWebsiteImageTextListModel> imageTextListModelList2 = notEndCommodityList;
							int updateNum = 1;
							for(ZizaijiaTempleWebsiteImageTextListModel imageTextListModel:imageTextListModelList2){
								imageTextListModel.setSort(updateNum);
								imageTextListModel.setUpdate_time(new Date());
								textListService.updateAny(imageTextListModel);
								updateNum = updateNum+1;
							}
							List<ZizaijiaTempleWebsiteImageTextListModel> imageTextListModelList3 = textListService.findByImageTextIdForPage(imageText.getId(), 0, imageText.getList_num());
							for(ZizaijiaTempleWebsiteImageTextListModel imageTextListModel:imageTextListModelList3){
								JSONFromBean imageTextListJSON = new JSONFromBean(imageTextListModel, ZizaihomeJSONUtils.emptyNullFilter());
								int progressType = 2;
								BuddnistCeremonyCommodityModel commodity = commodityService.getModel(imageTextListModel.getBuddnist_ceremony_commodity_id());
								if(commodity.getStart_time() != null){
									if((commodity.getStart_time().getTime()-new Date().getTime()) > 0){
										progressType = 1;
									}
									else if(commodity.getIs_end() == 1){
										progressType = 3;
									}
								}
								else if(commodity.getIs_end() == 1){
									progressType = 3;
								}
								imageTextListJSON.addPropertyObj("progressType", progressType, null);
								try {
									templeWebsiteImageTextList2.add(imageTextListJSON.buildString());
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
					else{
						if(imageText.getContent_type() == 1){
							List<ZizaijiaArticleModel> articleList = null;
							if(imageText.getArticle_type_id() == 0){
								articleList = articleService.findArticleList(templeId, 0, imageText.getList_num());
							}
							else{
								articleList = articleService.findArticleAndTypeList(templeId, 0, imageText.getList_num(),imageText.getArticle_type_id());
							}
							for(ZizaijiaArticleModel article:articleList){
								ZizaijiaTempleWebsiteImageTextListModel imageTextList = new ZizaijiaTempleWebsiteImageTextListModel();
								imageTextList.setBuddnist_ceremony_commodity_id(0);
								imageTextList.setImage_text_id(imageText.getId());
								imageTextList.setLink_url("https://wx.zizaihome.com/article/articleIndex?articleId="+article.getId());
								if(ConfigReadUtils.loadConfigByConfDir("server.properties").getProperty("isTest").equals("1")){
									imageTextList.setLink_url("http://test.zizaihome.com/article/articleIndex?articleId="+article.getId());
								}else if(ConfigReadUtils.loadConfigByConfDir("server.properties").getProperty("isTest").equals("2")){
									imageTextList.setLink_url("http://test2.zizaihome.com/article/articleIndex?articleId="+article.getId());
								}
								
								imageTextList.setOp_status(0);
								imageTextList.setPic(article.getPic());
								imageTextList.setTitle(article.getTitle());
								imageTextList.setArticle_id(article.getId());
								JSONFromBean imageTextListJSON = new JSONFromBean(imageTextList, ZizaihomeJSONUtils.emptyNullFilter());
								imageTextListJSON.addPropertyObj("progressType", 0, null);
								try {
									templeWebsiteImageTextList2.add(imageTextListJSON.buildString());
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						else{
							List<ZizaijiaTempleWebsiteImageTextListModel> imageTextListModelList = textListService.findByImageTextIdForPage(imageText.getId(), 0, imageText.getList_num());
							for(ZizaijiaTempleWebsiteImageTextListModel imageTextListModel:imageTextListModelList){
								JSONFromBean imageTextListJSON = new JSONFromBean(imageTextListModel, ZizaihomeJSONUtils.emptyNullFilter());
								imageTextListJSON.addPropertyObj("progressType", 0, null);
								try {
									templeWebsiteImageTextList2.add(imageTextListJSON.buildString());
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
					imageTextJSON.addPropertyObj("templeWebsiteImageTextList", templeWebsiteImageTextList2, ZizaihomeJSONUtils.emptyNullFilter());
					imageTextJSON.addPropertyObj("type", 3, null);
					try {
						dataList.add(imageTextJSON.buildString());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			else if(sort.getType() == 4){
				ZizaijiaTempleWebsiteMeritBoxModel meritBox = meritBoxService.getModel(sort.getMessage_id());
				List<BuddnistCeremonyCommodityOrderModel> buddnistCeremonyCommodityOrderList = new ArrayList<BuddnistCeremonyCommodityOrderModel>();
				if(meritBox.getIs_show_real_time_list() == 1){
					buddnistCeremonyCommodityOrderList = commodityOrderService.findByTempleIdAndAbbotIdForRealTime(templeId, 0, meritBox.getShow_list_num());
				}
				else if(meritBox.getIs_show_month_list() == 1){
					buddnistCeremonyCommodityOrderList = commodityOrderService.findByTempleIdAndAbbotId(templeId, 0, meritBox.getShow_list_num(),1);
				}
				else if(meritBox.getIs_show_total_list() == 1){
					buddnistCeremonyCommodityOrderList = commodityOrderService.findByTempleIdAndAbbotId(templeId, 0, meritBox.getShow_list_num(),0);
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
				JSONFromBean meritBoxJSON = new JSONFromBean(meritBox, ZizaihomeJSONUtils.emptyNullFilter());
				meritBoxJSON.addPropertyObj("meritBox", buddnistCeremonyCommodityOrderList2, ZizaihomeJSONUtils.emptyNullFilter());
				meritBoxJSON.addPropertyObj("type", 4, null);
				try {
					dataList.add(meritBoxJSON.buildString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(sort.getType() == 5){
	            HashMap<String,Object> cMap = new HashMap<String,Object>();
	            ZizaijiaTempleWebsiteCalendarModel calendar = templeWebsiteCalendarService.getModel(sort.getMessage_id());
	            HashMap<String,Object> calendarMap = new HashMap<String,Object>();
	            calendarMap.put("calendarId", calendar.getId());
	            SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
	            String date = sdf.format(new Date());
	            Calendar cal = Calendar.getInstance();  
	            cal.setTime(new Date());  
	            cal.add(Calendar.MONTH, -90); 
	            Date day = cal.getTime();
	            List<ZizaijiaTempleWebsiteCalendarDayModel> calendarDayList =  templeWebsiteCalendarDayService.findByCalendarIdAndDay(calendar.getId(), day);
	            List<String> dateList = new ArrayList<String>();
	            for(ZizaijiaTempleWebsiteCalendarDayModel calendarDay:calendarDayList){
	            	dateList.add(sdf.format(calendarDay.getCalendarDate()));
	            }
	            calendarMap.put("list", dateList);
	            List<ZizaijiaTempleWebsiteCalendarListModel> calendarEventList = templeWebsiteCalendarListService.findByCalendarDateAndCalendarIdAndTempleId(date, calendar.getId(), templeId);
	            List<HashMap<String,Object>> eventList = new ArrayList<HashMap<String,Object>>();
	            List<HashMap<String,Object>> eventList2 = new ArrayList<HashMap<String,Object>>();
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
		            		if(ConfigReadUtils.loadConfigByConfDir("server.properties").getProperty("isTest").equals("1")){
		            			eventMap.put("link","http://test.zizaihome.com/commodity/commodityAuth?commodityId=" + commodity.getId());
		            			
		            		}else if(ConfigReadUtils.loadConfigByConfDir("server.properties").getProperty("isTest").equals("2")){
		            			eventMap.put("link","http://test.zizaihome.com/commodity/commodityAuth?commodityId=" + commodity.getId());
		            			
		            		}
		            		
		            	}
		            	else{
		            		eventMap.put("commodityName", "");
		            		eventMap.put("commodityPic", "");
		            		eventMap.put("link","");
		            	}
		            	eventList.add(eventMap);
		            }
	            }
	            else{
	            	ZizaijiaTempleWebsiteCalendarListModel event = templeWebsiteCalendarListService.findByCalendarDateAndCommodityIdAndCalendarIdAndTempleId(date, calendar.getId(), templeId);
	            	if(event != null){
		            	HashMap<String,Object> eventMap = new HashMap<String,Object>();
		            	eventMap.put("id", event.getId());
		            	eventMap.put("title", event.getTitle());
		            	eventMap.put("cover_pic", event.getCover_pic());
		            	eventMap.put("commodityId", event.getCommodityId());
		            	if(event.getCommodityId() > 0){
		            		BuddnistCeremonyCommodityModel commodity = commodityService.getModel(event.getCommodityId());
		            		List<BuddnistCeremonyCommodityPicsModel> pics = commodityPicsService.findByCommodityId(commodity.getId());
		            		eventMap.put("commodityName", commodity.getName());
		            		eventMap.put("commodityPic", pics.get(0).getPic_url());
		            		eventMap.put("link","https://wx.zizaihome.com/commodity/commodityAuth?commodityId=" + commodity.getId());
		            		if(ConfigReadUtils.loadConfigByConfDir("server.properties").getProperty("isTest").equals("1")){
		            			eventMap.put("link","http://test.zizaihome.com/commodity/commodityAuth?commodityId=" + commodity.getId());
		            		}else if(ConfigReadUtils.loadConfigByConfDir("server.properties").getProperty("isTest").equals("2")){
		            			eventMap.put("link","http://test2.zizaihome.com/commodity/commodityAuth?commodityId=" + commodity.getId());
		            		}
		            		
		            	}
		            	else{
		            		eventMap.put("commodityName", "");
		            		eventMap.put("commodityPic", "");
		            		eventMap.put("link","");
		            	}
		            	eventMap.put("calendarDate", sdf.format(event.getCalendarDate()));
		            	eventList2.add(eventMap);
	            	}
	            }
	            calendarMap.put("events", eventList);
	            calendarMap.put("trailers", eventList2);
	            cMap.put("calendar", calendarMap);
	            cMap.put("type", 5);
	            dataList.add(JSONObject.fromObject(cMap));
			}
			int isShowWebsiteQuickEntry = 1;
//			if(isIOS == 1){
//				if(!sid.equals("")){
//					isShowWebsiteQuickEntry = 0;
//				}
//			}
			if(isShowWebsiteQuickEntry == 1){
				if(sort.getType() == 6){
					HashMap<String,Object> dataMap = new HashMap<String,Object>();
					ZizaijiaTempleWebsiteQuickEntryModel quickEntry = templeWebsiteQuickEntryService.getModel(sort.getMessage_id());
					dataMap.put("title", quickEntry.getTitle());
					List<ZizaijiaTempleWebsiteQuickEntryLinkModel> quickEntryLinkList = templeWebsiteQuickEntryLinkService.findByQuickEntryId(quickEntry.getId());
		            List<HashMap<String,Object>> linkMapList = new ArrayList<HashMap<String,Object>>();
		            for(ZizaijiaTempleWebsiteQuickEntryLinkModel quickEntryLink:quickEntryLinkList){
		            	HashMap<String,Object> linkMap = new HashMap<String,Object>();
		            	linkMap.put("pic", quickEntryLink.getPic());
		            	linkMap.put("name", quickEntryLink.getName());
		            	linkMap.put("link", quickEntryLink.getLink());
		            	linkMap.put("type", quickEntryLink.getType());
		            	linkMapList.add(linkMap);
		            }
		            dataMap.put("linkList", linkMapList);
		            dataMap.put("type", 6);
		            dataList.add(JSONObject.fromObject(dataMap));
				}
			}
		}
		json.put("templeName", temple.getName());
		json.put("websiteVisitNum", temple.getWebsite_visit_num());
		json.put("data", dataList);
		return succRequest("获取成功",json);
	}

	@Override
	protected JSONObject postMethod(JSONObject json) {
		return this.getMethod(json);
	}

}
