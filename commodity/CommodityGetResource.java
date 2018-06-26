 package com.zizaihome.api.resources.commodity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.restlet.Request;

import com.zizaihome.api.db.model.BuddnistCeremonyCommodityModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityOrderModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityPicsModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityPostscriptModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityPostscriptSelectInputModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityShoppingCartModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommoditySubdivideModel;
import com.zizaihome.api.db.model.BuddnistCeremonyShopModel;
import com.zizaihome.api.db.model.TempleMeritMachineAdvertisementModel;
import com.zizaihome.api.db.model.TempleModel;
import com.zizaihome.api.db.model.UserModel;
import com.zizaihome.api.db.model.ZizaijiaTempleCouponModel;
import com.zizaihome.api.db.model.ZizaijiaUserInfoModel;
import com.zizaihome.api.resources.base.BaseResource;
import com.zizaihome.api.service.BuddnistCeremonyCommodityOrderService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityPicsService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityPostscriptSelectInputService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityPostscriptService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityShoppingCartService;
import com.zizaihome.api.service.BuddnistCeremonyCommoditySubdivideService;
import com.zizaihome.api.service.BuddnistCeremonyShopService;
import com.zizaihome.api.service.TempleMeritMachineAdvertisementService;
import com.zizaihome.api.service.TempleService;
import com.zizaihome.api.service.UserService;
import com.zizaihome.api.service.ZizaijiaTempleCouponService;
import com.zizaihome.api.service.ZizaijiaUserInfoService;
import com.zizaihome.api.utils.JSONFromBean;
import com.zizaihome.api.utils.ZizaihomeJSONUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class CommodityGetResource extends BaseResource {

	@Override
	protected void initParams(Request request) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected JSONObject getMethod(JSONObject json) {
		BuddnistCeremonyCommodityService buddnistCeremonyCommodityService = new BuddnistCeremonyCommodityService();
		BuddnistCeremonyCommodityPicsService buddnistCeremonyCommodityPicsService = new BuddnistCeremonyCommodityPicsService();
		BuddnistCeremonyCommodityPostscriptService buddnistCeremonyCommodityPostscriptService = new BuddnistCeremonyCommodityPostscriptService();
		BuddnistCeremonyCommodityPostscriptSelectInputService buddnistCeremonyCommodityPostscriptSelectInputService = new BuddnistCeremonyCommodityPostscriptSelectInputService();
		BuddnistCeremonyCommoditySubdivideService buddnistCeremonyCommoditySubdivideService = new BuddnistCeremonyCommoditySubdivideService();
		ZizaijiaUserInfoService userInfoService = new ZizaijiaUserInfoService();
		UserService userService = new UserService();
		BuddnistCeremonyCommodityOrderService buddnistCeremonyCommodityOrderService = new BuddnistCeremonyCommodityOrderService();
		BuddnistCeremonyCommodityShoppingCartService buddnistCeremonyCommodityShoppingCartService = new BuddnistCeremonyCommodityShoppingCartService();
		BuddnistCeremonyShopService buddnistCeremonyShopService = new BuddnistCeremonyShopService();
		TempleService templeService = new TempleService();
		TempleMeritMachineAdvertisementService meritMachineAdvertisementService = new TempleMeritMachineAdvertisementService();
		ZizaijiaTempleCouponService templeCouponService = new ZizaijiaTempleCouponService();
		int commodityId = getParameter("commodityId",0);
		int orderId = getParameter("orderId",0);
		BuddnistCeremonyCommodityModel buddnistCeremonyCommodity = buddnistCeremonyCommodityService.getModel(commodityId);
		if(buddnistCeremonyCommodity == null){
			return failRequest("参数错误",json);
		}
		UserModel user = userService.getModel(userId);
		JSONFromBean buddnistCeremonyCommodityJSON = new JSONFromBean(buddnistCeremonyCommodity, ZizaihomeJSONUtils.emptyBuddnistCeremonyCommodityFilter());
		String endDayNum = "";
		String startDayNum = "";
		int commodityIsEnd = buddnistCeremonyCommodity.getIs_end();
		//组装佛事还有多少天结束
		long differTime = (buddnistCeremonyCommodity.getEnd_time().getTime()-new Date().getTime());
		if(differTime/(24*60*60*1000)>1 && commodityIsEnd !=1){
			commodityIsEnd = 0;
			endDayNum = (int)(differTime/(24*60*60*1000))+"<span style='color:#000000'>天</span>";
		}
		else if(differTime/(60*60*1000)>1 && commodityIsEnd !=1){
			commodityIsEnd = 0;
			endDayNum = (int)(differTime/(60*60*1000))+"<span style='color:#000000'>小时</span>";
		}
		else if(differTime/(60*1000) > 0 && commodityIsEnd !=1){
			commodityIsEnd = 0;
			endDayNum = (int)(differTime/(60*1000))+"<span style='color:#000000'>分钟</span>";
		}
		//如果佛事已经结束将佛事的结束标志位设置为已结束
		else{
			commodityIsEnd = 1;
			buddnistCeremonyCommodity.setIs_end(1);
			buddnistCeremonyCommodityService.updateAny(buddnistCeremonyCommodity);
			//去除过期广告
			TempleMeritMachineAdvertisementModel meritMachineAdvertisement = meritMachineAdvertisementService.findByTypeAndCommodityId(2, commodityId);
			if(meritMachineAdvertisement != null){
				meritMachineAdvertisement.setStatus(-1);
				meritMachineAdvertisementService.updateAny(meritMachineAdvertisement);
			}
		}
		buddnistCeremonyCommodityJSON.addPropertyObj("endDayNum", endDayNum, null);
		//如果佛事已经结束但是经过用户在后台修改时间的话将佛事设置为未结束
		if(commodityIsEnd == 0){
			if(buddnistCeremonyCommodity.getIs_end() == 1 || buddnistCeremonyCommodity.getIs_end() == -1){
				buddnistCeremonyCommodity.setIs_end(0);
				buddnistCeremonyCommodityService.updateAny(buddnistCeremonyCommodity);
			}
		}
		//拼装自定义分享标题
		String title=buddnistCeremonyCommodity.getName();
		if (buddnistCeremonyCommodity.getAllow_showVistNum()==1) {
			title=title+"|已有"+buddnistCeremonyCommodity.getJoin_num()+"人参与,随手转发功德无量";
		}	
		buddnistCeremonyCommodityJSON.addPropertyObj("title", title, null);
		
		//拼装开始时间展示字符串
		long differTime2 = (new Date().getTime()-buddnistCeremonyCommodity.getStart_time().getTime());
		if(buddnistCeremonyCommodity.getIs_show_cnt() == 1){
			if(differTime2/(24*60*60*1000)>1){
				startDayNum = (int)(differTime2/(24*60*60*1000))+"天";
			}
			else if(differTime2/(60*60*1000)>1){
				startDayNum = (int)(differTime2/(60*60*1000))+"小时";
			}
			else if(differTime2/(60*1000) > 0){
				startDayNum = (int)(differTime2/(60*1000))+"分钟";
			}
			else if(differTime2/(60*1000) < 0){
				startDayNum = "尚未开始";
			}			
		}
		//如果佛事还未开始设置标志位为还未开始
		if(differTime2/(60*1000) < 0 && commodityIsEnd != 1){
			buddnistCeremonyCommodity.setIs_end(-1);
			buddnistCeremonyCommodityService.updateAny(buddnistCeremonyCommodity);
		}
		buddnistCeremonyCommodityJSON.addPropertyObj("startDayNum", startDayNum, null);
		//返回用户头像
		if(user != null){
			buddnistCeremonyCommodityJSON.addPropertyObj("wechat_nickname", user.getNick_name(), null);
			buddnistCeremonyCommodityJSON.addPropertyObj("head_img", user.getHead_img(), null);
		}
		else{
			buddnistCeremonyCommodityJSON.addPropertyObj("wechat_nickname", "", null);
			buddnistCeremonyCommodityJSON.addPropertyObj("head_img", "", null);
		}
		//查询该佛事的图片
		List<BuddnistCeremonyCommodityPicsModel> buddnistCeremonyCommodityPicsList = buddnistCeremonyCommodityPicsService.findByCommodityId(commodityId);
		JSONFromBean buddnistCeremonyCommodityPicsJSONList = new JSONFromBean(buddnistCeremonyCommodityPicsList, ZizaihomeJSONUtils.emptyNullFilter());
		try {
			buddnistCeremonyCommodityJSON.addPropertyObj("productionImg", buddnistCeremonyCommodityPicsJSONList.buildString(), null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		//查询该佛事的附言
		List<BuddnistCeremonyCommodityPostscriptModel> buddnistCeremonyCommodityPostscriptList = buddnistCeremonyCommodityPostscriptService.findByCommodityId(commodityId);
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
		buddnistCeremonyCommodityJSON.addPropertyObj("postscript", buddnistCeremonyCommodityPostscriptJSONList.toString(), ZizaihomeJSONUtils.emptyNullFilter());
		
		if(buddnistCeremonyCommodityPostscriptList.size()>0){
			buddnistCeremonyCommodityJSON.addPropertyObj("is_to_addition", 1, null);
		}
		else{
			buddnistCeremonyCommodityJSON.addPropertyObj("is_to_addition", 0, null);
		}
		//查询佛事规格列表
		List<BuddnistCeremonyCommoditySubdivideModel> buddnistCeremonyCommoditySubdivideModelList = buddnistCeremonyCommoditySubdivideService.findByCommodityId(commodityId);
		List<String> buddnistCeremonyCommoditySubdivideModelJSONList = new ArrayList<String>();
		for(BuddnistCeremonyCommoditySubdivideModel buddnistCeremonyCommoditySubdivideModel:buddnistCeremonyCommoditySubdivideModelList){
			if(buddnistCeremonyCommoditySubdivideModel.getEndTime()!=null 
					&& buddnistCeremonyCommoditySubdivideModel.getEndTime().getTime()<System.currentTimeMillis()){
				buddnistCeremonyCommoditySubdivideModel.setStock(0);
			}
			//查询佛事规格的附言
			List<BuddnistCeremonyCommodityPostscriptModel> commoditySubdividePostscriptList = buddnistCeremonyCommodityPostscriptService.findBySubdivideId(buddnistCeremonyCommoditySubdivideModel.getId());
			List<String> commoditySubdividePostscriptJSONList = new ArrayList<String>();
			//对佛事规格的附言进行数据组装
			for(BuddnistCeremonyCommodityPostscriptModel commoditySubdividePostscript:commoditySubdividePostscriptList){
				JSONFromBean buddnistCeremonyCommodityPostscriptJSON = new JSONFromBean(commoditySubdividePostscript, ZizaihomeJSONUtils.emptyNullFilter());
				if(commoditySubdividePostscript.getInput_type() == 3){
					List<BuddnistCeremonyCommodityPostscriptSelectInputModel> buddnistCeremonyCommodityPostscriptSelectInputList = buddnistCeremonyCommodityPostscriptSelectInputService.findByPostscriptId(commoditySubdividePostscript.getId());
					JSONFromBean buddnistCeremonyCommodityPostscriptSelectInputJSONList = new JSONFromBean(buddnistCeremonyCommodityPostscriptSelectInputList, ZizaihomeJSONUtils.emptyNullFilter());
					try {
						buddnistCeremonyCommodityPostscriptJSON.addPropertyObj("selectInput", buddnistCeremonyCommodityPostscriptSelectInputJSONList.buildString(), null);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				try {
					commoditySubdividePostscriptJSONList.add(buddnistCeremonyCommodityPostscriptJSON.buildString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			JSONFromBean buddnistCeremonyCommoditySubdivideModelJSON = new JSONFromBean(buddnistCeremonyCommoditySubdivideModel, ZizaihomeJSONUtils.emptyNullFilter());
			//合并佛事与规格的附言
			for(String buddnistCeremonyCommodityPostscriptJSON:buddnistCeremonyCommodityPostscriptJSONList){
				commoditySubdividePostscriptJSONList.add(buddnistCeremonyCommodityPostscriptJSON);
			}
			buddnistCeremonyCommoditySubdivideModelJSON.addPropertyObj("postscript", commoditySubdividePostscriptJSONList, ZizaihomeJSONUtils.emptyNullFilter());
			if(commoditySubdividePostscriptJSONList.size()>0){
				buddnistCeremonyCommoditySubdivideModelJSON.addPropertyObj("is_to_addition", 1, null);
			}
			else{
				buddnistCeremonyCommoditySubdivideModelJSON.addPropertyObj("is_to_addition", 0, null);
			}
			
			JSONArray conversionSubdivideList = new JSONArray();
			//如果该佛事规格为拆单规格查询出关联的佛事规格
			if(buddnistCeremonyCommoditySubdivideModel.getIs_conversion() == 1) {
				JSONArray conversionSubdivideJsonArray = JSONArray.fromObject(buddnistCeremonyCommoditySubdivideModel.getConversion_subdivide_json());
				for(int i=0;i<conversionSubdivideJsonArray.size();i++) {
					JSONObject conversionSubdivideJson = conversionSubdivideJsonArray.getJSONObject(i);
					List<BuddnistCeremonyCommodityPostscriptModel> conversionCommodityPostscriptList = buddnistCeremonyCommodityPostscriptService.findByCommodityId(conversionSubdivideJson.getInt("commodityId"));
					List<String> conversionCommodityPostscriptJSONList = new ArrayList<String>();
					//对转单佛事的附言进行数据组装
					for(BuddnistCeremonyCommodityPostscriptModel conversionCommodityPostscript:conversionCommodityPostscriptList){
						JSONFromBean buddnistCeremonyCommodityPostscriptJSON = new JSONFromBean(conversionCommodityPostscript, ZizaihomeJSONUtils.emptyNullFilter());
						if(conversionCommodityPostscript.getInput_type() == 3){
							List<BuddnistCeremonyCommodityPostscriptSelectInputModel> buddnistCeremonyCommodityPostscriptSelectInputList = buddnistCeremonyCommodityPostscriptSelectInputService.findByPostscriptId(conversionCommodityPostscript.getId());
							JSONFromBean buddnistCeremonyCommodityPostscriptSelectInputJSONList = new JSONFromBean(buddnistCeremonyCommodityPostscriptSelectInputList, ZizaihomeJSONUtils.emptyNullFilter());
							try {
								buddnistCeremonyCommodityPostscriptJSON.addPropertyObj("selectInput", buddnistCeremonyCommodityPostscriptSelectInputJSONList.buildString(), null);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						try {
							conversionCommodityPostscriptJSONList.add(buddnistCeremonyCommodityPostscriptJSON.buildString());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					List<BuddnistCeremonyCommodityPostscriptModel> conversionSubdividePostscriptList = buddnistCeremonyCommodityPostscriptService.findBySubdivideId(conversionSubdivideJson.getInt("subdivideId"));
					List<String> conversionSubdividePostscriptJSONList = new ArrayList<String>();
					//对转单规格的附言进行数据组装
					for(BuddnistCeremonyCommodityPostscriptModel conversionSubdividePostscript:conversionSubdividePostscriptList){
						JSONFromBean buddnistCeremonyCommodityPostscriptJSON = new JSONFromBean(conversionSubdividePostscript, ZizaihomeJSONUtils.emptyNullFilter());
						if(conversionSubdividePostscript.getInput_type() == 3){
							List<BuddnistCeremonyCommodityPostscriptSelectInputModel> buddnistCeremonyCommodityPostscriptSelectInputList = buddnistCeremonyCommodityPostscriptSelectInputService.findByPostscriptId(conversionSubdividePostscript.getId());
							JSONFromBean buddnistCeremonyCommodityPostscriptSelectInputJSONList = new JSONFromBean(buddnistCeremonyCommodityPostscriptSelectInputList, ZizaihomeJSONUtils.emptyNullFilter());
							try {
								buddnistCeremonyCommodityPostscriptJSON.addPropertyObj("selectInput", buddnistCeremonyCommodityPostscriptSelectInputJSONList.buildString(), null);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						try {
							conversionSubdividePostscriptJSONList.add(buddnistCeremonyCommodityPostscriptJSON.buildString());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					BuddnistCeremonyCommoditySubdivideModel conversionSubdivideModel = buddnistCeremonyCommoditySubdivideService.getModel(conversionSubdivideJson.getInt("subdivideId"));
					JSONFromBean conversionSubdivideJSON = new JSONFromBean(conversionSubdivideModel, ZizaihomeJSONUtils.emptyNullFilter());
					//合并佛事与规格的附言
					for(String conversionCommodityPostscriptJSON:conversionCommodityPostscriptJSONList){
						conversionSubdividePostscriptJSONList.add(conversionCommodityPostscriptJSON);
					}
					conversionSubdivideJSON.addPropertyObj("postscript", conversionSubdividePostscriptJSONList, ZizaihomeJSONUtils.emptyNullFilter());
					TempleModel temple = templeService.getModel(conversionSubdivideJson.getInt("templeId"));
					int isSetConversionSubdivideList = 0;
					for(int b=0;b<conversionSubdivideList.size();b++) {
						JSONObject conversionSubdivide = conversionSubdivideList.getJSONObject(b);
						if(conversionSubdivide.getString("templeName").equals(temple.getName())) {
							try {
								conversionSubdivideList.getJSONObject(b).getJSONArray("subdivideList").add(conversionSubdivideJSON.buildString());
							} catch (Exception e) {
								e.printStackTrace();
							}
							isSetConversionSubdivideList = 1;
						}
					}
					if(isSetConversionSubdivideList == 0) {
						JSONObject conversionSubdivide = new JSONObject();
						JSONArray newSubdivideList = new JSONArray();
						try {
							newSubdivideList.add(conversionSubdivideJSON.buildString());
						} catch (Exception e) {
							e.printStackTrace();
						}
						conversionSubdivide.put("templeName", temple.getName());
						conversionSubdivide.put("subdivideList", newSubdivideList);
						conversionSubdivideList.add(conversionSubdivide);
					}
				}
			}
			buddnistCeremonyCommoditySubdivideModelJSON.addPropertyObj("conversionSubdivideList", conversionSubdivideList.toString(), ZizaihomeJSONUtils.emptyArticleFilter());
			//如果该规格有限购次数
			if(buddnistCeremonyCommoditySubdivideModel.getEnroll_num() > 0) {
				//因为佛事管理后台编辑佛事之后规格id会改变所以只能依靠规格名称来判断用户是否有购买过该规格
				List<BuddnistCeremonyCommodityOrderModel> commodityOrderList = buddnistCeremonyCommodityOrderService.findCommodityIdAndUserIdAndSubdirideName(commodityId, userId, buddnistCeremonyCommoditySubdivideModel.getName());
				//判断用户购买该规格的次数有没有超过限定购买的次数
				if(commodityOrderList.size() >= buddnistCeremonyCommoditySubdivideModel.getEnroll_num()) {
					buddnistCeremonyCommoditySubdivideModelJSON.addPropertyObj("unableBuy", 1, ZizaihomeJSONUtils.emptyArticleFilter());
				}
				else {
					buddnistCeremonyCommoditySubdivideModelJSON.addPropertyObj("unableBuy", 0, ZizaihomeJSONUtils.emptyArticleFilter());
				}
			}
			else {
				buddnistCeremonyCommoditySubdivideModelJSON.addPropertyObj("unableBuy", 0, ZizaihomeJSONUtils.emptyArticleFilter());
			}
			try {
				buddnistCeremonyCommoditySubdivideModelJSONList.add(buddnistCeremonyCommoditySubdivideModelJSON.buildString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		buddnistCeremonyCommodityJSON.addPropertyObj("subdivide", buddnistCeremonyCommoditySubdivideModelJSONList.toString(), ZizaihomeJSONUtils.emptyNullFilter());
//		int cnt = buddnistCeremonyCommodityOrderService.findCommodityIdCnt(commodityId);
//		if(cnt > 0){
//			buddnistCeremonyCommodityJSON.addPropertyObj("hasParticipant", 1, null);
//		}
//		else{
//			buddnistCeremonyCommodityJSON.addPropertyObj("hasParticipant", 0, null);
//		}
		
		//如果用户有设置默认功德主信息返回功德组数据
		JSONObject userInfoJSON = new JSONObject();
		if(user != null){
			if(user.getFrequently_used_info_id() != 0){
				ZizaijiaUserInfoModel userInfo = userInfoService.getModel(user.getFrequently_used_info_id());
				userInfoJSON.put("name", userInfo.getName());
				userInfoJSON.put("sex", userInfo.getSex());
				userInfoJSON.put("mobile", userInfo.getMobile());
				if(userInfo.getBirthday() != null){
					 SimpleDateFormat sdf = new SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " );
					userInfoJSON.put("birthday", sdf.format(userInfo.getBirthday()));
				}
				else{
					userInfoJSON.put("birthday", "1985-01-01");
				}
				userInfoJSON.put("birthdayType", userInfo.getBirthdayType());
				userInfoJSON.put("city", userInfo.getCity());
				userInfoJSON.put("province", userInfo.getProvince());
				userInfoJSON.put("address", userInfo.getAddress());
				userInfoJSON.put("area", userInfo.getArea());
			}
		}
		buddnistCeremonyCommodityJSON.addPropertyObj("userInfo", userInfoJSON.toString(), null);
		
		if(user != null){
			List<BuddnistCeremonyCommodityShoppingCartModel> buddnistCeremonyCommodityShoppingCartList = buddnistCeremonyCommodityShoppingCartService.findByUserId(user.getId());
			buddnistCeremonyCommodityJSON.addPropertyObj("shoppingCartNum", buddnistCeremonyCommodityShoppingCartList.size(), null);
		}
		else{
			buddnistCeremonyCommodityJSON.addPropertyObj("shoppingCartNum", 0, null);
		}
		
		BuddnistCeremonyShopModel shop =  buddnistCeremonyShopService.getModel(buddnistCeremonyCommodity.getShop_id());
		TempleModel temple = templeService.getModel(shop.getTemple_id());
		buddnistCeremonyCommodityJSON.addPropertyObj("templeVerify", temple.getVerify(), null);
		
		buddnistCeremonyCommodityJSON.addPropertyObj("showWebsiteSuixi", temple.getShow_website_suixi(), null);
		
        buddnistCeremonyCommodityJSON.addPropertyObj("websiteVisitNum", temple.getWebsite_visit_num(), null);

        buddnistCeremonyCommodityJSON.addPropertyObj("templeName", temple.getName(), null);

        buddnistCeremonyCommodityJSON.addPropertyObj("templeImg", temple.getYg_index_head_img()==null?"":temple.getYg_index_head_img(), null);

		ZizaijiaTempleCouponModel templeCoupon = templeCouponService.findByCommodityId(commodityId);
		if(templeCoupon != null){
			buddnistCeremonyCommodityJSON.addPropertyObj("isCanUseCoupon", 1, null);
		}
		else{
			buddnistCeremonyCommodityJSON.addPropertyObj("isCanUseCoupon", 0, null);
		}
		
		if(user != null){
			buddnistCeremonyCommodityJSON.addPropertyObj("isWeChat", 1, null);
		}
		else{
			buddnistCeremonyCommodityJSON.addPropertyObj("isWeChat", 0, null);
		}
		
		//更新佛事浏览次数
		buddnistCeremonyCommodityService.updateCommodityViewCount(commodityId);
		
		buddnistCeremonyCommodityJSON.addPropertyObj("chanzaiIsVirtual", chanzaiIsVirtual, null);
		
		if(sid.equals("")){
			buddnistCeremonyCommodityJSON.addPropertyObj("isChanzai", 0, null);
		}
		else{
			buddnistCeremonyCommodityJSON.addPropertyObj("isChanzai", 1, null);
		}
		
		if(orderId != 0){
			List<BuddnistCeremonyCommodityOrderModel> commodityOrderList = buddnistCeremonyCommodityOrderService.findByOrderId(orderId);
			if(commodityOrderList.size() > 0){
				buddnistCeremonyCommodityJSON.addPropertyObj("userPosiscriptStr", commodityOrderList.get(0).getPosiscript(), null);
			}
			else{
				buddnistCeremonyCommodityJSON.addPropertyObj("userPosiscriptStr", "", null);
			}
		}
		else{
			buddnistCeremonyCommodityJSON.addPropertyObj("userPosiscriptStr", "", null);
		}
		
		try {
			json.put("data", buddnistCeremonyCommodityJSON.buildString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//因为随喜金额转json的时候会自动转成数组所以要toString将它转成string类型
		if(!json.getJSONObject("data").get("random_money_list").toString().equals("")){
			json.getJSONObject("data").put("random_money_list", "\""+json.getJSONObject("data").get("random_money_list").toString()+"\"");
		}
		for(int i=0;i<json.getJSONObject("data").getJSONArray("subdivide").size();i++){
			if(!json.getJSONObject("data").getJSONArray("subdivide").getJSONObject(i).get("random_money_list").toString().equals("")){
				json.getJSONObject("data").getJSONArray("subdivide").getJSONObject(i).put("random_money_list", "\""+json.getJSONObject("data").getJSONArray("subdivide").getJSONObject(i).get("random_money_list").toString()+"\"");
			}
		}
		return succRequest("成功",json);
	}

	@Override
	protected JSONObject postMethod(JSONObject json) {
		return this.getMethod(json);
	}

}