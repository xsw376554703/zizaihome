package com.zizaihome.api.resources.commodity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.restlet.Request;
import org.restlet.data.CookieSetting;

import com.zizaihome.api.common.Constants;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityModel;
import com.zizaihome.api.db.model.EnterStatisticsModel;
import com.zizaihome.api.db.model.TempleModel;
import com.zizaihome.api.db.model.ZizaijiaTempleWebsiteSortModel;
import com.zizaihome.api.service.BuddnistCeremonyCommodityService;
import com.zizaihome.api.service.EnterStatisticsService;
import com.zizaihome.api.service.TempleService;
import com.zizaihome.api.service.ZizaijiaTempleWebsiteSortService;
import com.zizaihome.api.utils.ConfigReadUtils;
import com.zizaihome.web.resource.base.BaseResultModel;
import com.zizaihome.web.resource.base.BaseWebResource;

public class CommodityAuthResource extends BaseWebResource {

	@Override
	protected void initParams(Request request) {
		isPCBrowser = true;
	}

	@Override
	protected void getMethod(BaseResultModel br) {
		BuddnistCeremonyCommodityService commodityService = new BuddnistCeremonyCommodityService();
		ZizaijiaTempleWebsiteSortService templeWebsiteSortService = new ZizaijiaTempleWebsiteSortService();
		EnterStatisticsService enterStatisticsService = new EnterStatisticsService();
		TempleService templeService = new TempleService();
		int commodityId = getParameter("commodityId",0);
		int isRedirectOrderResult = getParameter("isRedirectOrderResult",0);
		int orderId = getParameter("orderId",0);
		int foshiOrderId = getParameter("foshiOrderId",0);
		int sType = getParameter("sType",0);
		int sId = getParameter("sId",0);
		int isMeeting = getParameter("isMeeting",0);
		BuddnistCeremonyCommodityModel commodity = commodityService.getModel(commodityId);
		if(commodity == null){
			br.setHtmlPath("error/deleted.html");
		}
		if(commodity.getOp_status() == -1){
			br.setHtmlPath("error/deleted.html");
		}
		if(isRedirectOrderResult == 2){
			String zizaihomeHost = ConfigReadUtils.loadConfigByConfDir("server.properties").getProperty("zizaihome_host");
			getResponse().redirectSeeOther(zizaihomeHost+"/commodity/commodityShoppingCart?orderId="+orderId+"&foshiOrderId="+foshiOrderId+"#/accomplish");
		}
		int isLinkWebsiteSort = 0;
		TempleModel temple = templeService.getModel(commodity.getTemple_id());
		
		//记录访问记录
		saveVisitRecord(commodityId,1,temple.getId());
		
		//获取该佛事的寺院是否有开通寺庙微站
		List<ZizaijiaTempleWebsiteSortModel> websiteSortList = templeWebsiteSortService.findSortList(temple.getId());
		if(websiteSortList.size() > 0){
			isLinkWebsiteSort = 1;
		}
		//统计进入途径
		EnterStatisticsModel enterStatistics = new EnterStatisticsModel();
		enterStatistics.setBusinessId(sId);
		enterStatistics.setTempleId(temple.getId());
		enterStatistics.setType(Constants.VISIT_TYPE_COMMODITY);
		enterStatistics.setUserId(userId);
		enterStatisticsService.insert(enterStatistics);
		//如果是由法会活动列表跳入到佛事页面再cookie中记录法会活动标识
        CookieSetting isMeetingCS = new CookieSetting(0, "isMeeting", isMeeting+"","/",null);
        this.getResponse().getCookieSettings().add(isMeetingCS);
		Map<String, Object> dataModel = new HashMap<String, Object>();
		dataModel.put("access_token", accessToken);
		//由于取出来的id int型的时候会有一个逗号在百位分隔将返回数据String型
		dataModel.put("commodityId", commodityId+"");
		//2017-10-18产品说要再次在标题里面加入寺庙名称
		dataModel.put("commodityName", temple.getName()+"-"+commodity.getName());
		dataModel.put("templeName", temple.getName());
		//由于取出来的id int型的时候会有一个逗号在百位分隔将返回数据String型
		dataModel.put("templeId", temple.getId().toString());
		dataModel.put("isLinkWebsiteSort", isLinkWebsiteSort);
		dataModel.put("orderId", orderId+"");
		dataModel.put("foshiOrderId", foshiOrderId+"");
		dataModel.put("chanzaiIsVirtual", chanzaiIsVirtual);
	    if(!sid.equals("")){
	    	dataModel.put("isChanzai", 1);
	    }
	    else{
	    	dataModel.put("isChanzai", 0);
	    }
		br.setDataModel(dataModel);
		br.setHtmlPath("buddhistCeremony/index.html");
	}

	@Override
	protected void postMethod(BaseResultModel br) {
		
	}

}
