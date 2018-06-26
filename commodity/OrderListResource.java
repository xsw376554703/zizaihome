package com.zizaihome.api.resources.commodity;

import java.text.SimpleDateFormat;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.restlet.Request;

import com.coola.jutil.data.DataPage;
import com.zizaihome.api.db.model.BuddnistCeremonyBuyerInfoModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityOrderModel;
import com.zizaihome.api.db.model.ZizaijiaBuddhaWallOrderModel;
import com.zizaihome.api.db.model.ZizaijiaTempleContentManagerNotifyModel;
import com.zizaihome.api.resources.base.BaseResource;
import com.zizaihome.api.service.BuddnistCeremonyBuyerInfoService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityOrderService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityService;
import com.zizaihome.api.service.BuddnistCeremonyCommoditySubdivideService;
import com.zizaihome.api.service.TempleService;
import com.zizaihome.api.service.ZizaijiaBuddhaWallBuddhaService;
import com.zizaihome.api.service.ZizaijiaBuddhaWallOrderService;
import com.zizaihome.api.service.ZizaijiaTempleContentManagerNotifyService;
import com.zizaihome.api.utils.ConfigReadUtils;
import com.zizaihome.api.utils.Encryptstr;

public class OrderListResource extends BaseResource {

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected JSONObject getMethod(JSONObject json) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		int contentId = getParameter("contentId", 0);
		int type = getParameter("type", 0);
		int pageNo = getParameter("pageNo", 1);
		int pageSize = getParameter("pageSize", 20);
		int orderType = getParameter("orderType", 0);//0已处理订单,1未处理订单
		ZizaijiaTempleContentManagerNotifyService notifyService = new ZizaijiaTempleContentManagerNotifyService();
		BuddnistCeremonyCommodityOrderService buddnistCeremonyCommodityOrderService = new BuddnistCeremonyCommodityOrderService();
//		BuddnistCeremonyCommodityService buddnistCeremonyCommodityService = new BuddnistCeremonyCommodityService();
//		BuddnistCeremonyCommoditySubdivideService buddnistCeremonyCommoditySubdivideService = new BuddnistCeremonyCommoditySubdivideService();
		BuddnistCeremonyBuyerInfoService buyerInfoService = new BuddnistCeremonyBuyerInfoService();
		ZizaijiaBuddhaWallOrderService zizaijiaBuddhaWallOrderService = new ZizaijiaBuddhaWallOrderService();
//		ZizaijiaBuddhaWallBuddhaService zizaijiaBuddhaWallBuddhaService = new ZizaijiaBuddhaWallBuddhaService();
		
		DataPage<ZizaijiaTempleContentManagerNotifyModel> orderList = notifyService.findByUser(userId,type,contentId,pageNo,pageSize,orderType);
		
//		BuddnistCeremonyCommodityModel buddnistCeremonyCommodityModel = buddnistCeremonyCommodityService.getModel(buddnistCeremonyCommodityOrderModel.getCommodity_id());
//		BuddnistCeremonyCommoditySubdivideModel buddnistCeremonyCommoditySubdivideModel = buddnistCeremonyCommoditySubdivideService.getModel(buddnistCeremonyCommodityOrderModel.getSubdiride_id());
		JSONArray list = new JSONArray();
		if(orderList != null){
			for(ZizaijiaTempleContentManagerNotifyModel model:orderList.getRecord()){
				JSONObject item = new JSONObject();
				if(model.getType() == 1){
					BuddnistCeremonyCommodityOrderModel buddnistCeremonyCommodityOrderModel = buddnistCeremonyCommodityOrderService.getModel(model.getOrderId());
					item.put("orderId", buddnistCeremonyCommodityOrderModel.getId());
					item.put("date", sdf.format(buddnistCeremonyCommodityOrderModel.getPay_time()));
					item.put("title", buddnistCeremonyCommodityOrderModel.getName());
					item.put("subName", buddnistCeremonyCommodityOrderModel.getSubdivideName());
					item.put("price", buddnistCeremonyCommodityOrderModel.getPrice());
					item.put("buyNum", buddnistCeremonyCommodityOrderModel.getBuy_num());
					item.put("type", model.getType());
					BuddnistCeremonyBuyerInfoModel buyerInfo = buyerInfoService.getModel(buddnistCeremonyCommodityOrderModel.getBuyer_info_id());
					item.put("nickName", buyerInfo.getName());
					String url = "https://wx.zizaihome.com/commodity/commodityOrder?orderId="+Encryptstr.encrypt(buddnistCeremonyCommodityOrderModel.getId().toString())+"&pageType=1";
					if(ConfigReadUtils.loadConfig("conf/server.properties").get("isTest").equals("1")){
						url = "http://test.zizaihome.com/commodity/commodityOrder?orderId="+Encryptstr.encrypt(buddnistCeremonyCommodityOrderModel.getId().toString())+"&pageType=1&isTest=1";
					}
					else if(ConfigReadUtils.loadConfig("conf/server.properties").get("isTest").equals("2")){
						url = "http://test2.zizaihome.com/commodity/commodityOrder?orderId="+Encryptstr.encrypt(buddnistCeremonyCommodityOrderModel.getId().toString())+"&pageType=1&isTest=2";
					}
					item.put("url", url);
					list.add(item);
				}
				else if(model.getType() == 2){
					ZizaijiaBuddhaWallOrderModel wallOrderModel = zizaijiaBuddhaWallOrderService.getModel(model.getOrderId());
					item.put("orderId", wallOrderModel.getId());
					item.put("date", sdf.format(wallOrderModel.getAddTime()));
					item.put("title", wallOrderModel.getWallName());
					item.put("subName", wallOrderModel.getBuddhaName());
					item.put("price", wallOrderModel.getPrice());
					item.put("buyNum", wallOrderModel.getBuyNum());
					item.put("type", model.getType());
					item.put("nickName", wallOrderModel.getWriteName());
					String url = "https://wx.zizaihome.com/buddhaWall/wallOrder?orderId="+Encryptstr.encrypt(wallOrderModel.getId()+"")+"&pageType=1";
					if(ConfigReadUtils.loadConfig("conf/server.properties").get("isTest").equals("1")){
						url = "http://test.zizaihome.com/buddhaWall/wallOrder?orderId="+Encryptstr.encrypt(wallOrderModel.getId()+"")+"&pageType=1&isTest=1";
					}
					else if(ConfigReadUtils.loadConfig("conf/server.properties").get("isTest").equals("2")){
						url = "http://test2.zizaihome.com/buddhaWall/wallOrder?orderId="+Encryptstr.encrypt(wallOrderModel.getId()+"")+"&pageType=1&isTest=2";
					}
					item.put("url", url);
					list.add(item);
				}
				else if(model.getType() == 3){
					BuddnistCeremonyCommodityOrderModel buddnistCeremonyCommodityOrderModel = buddnistCeremonyCommodityOrderService.getModel(model.getOrderId());
					item.put("orderId", buddnistCeremonyCommodityOrderModel.getId());
					item.put("date", sdf.format(buddnistCeremonyCommodityOrderModel.getPay_time()));
					item.put("title", buddnistCeremonyCommodityOrderModel.getName());
					item.put("subName", buddnistCeremonyCommodityOrderModel.getSubdivideName());
					item.put("price", buddnistCeremonyCommodityOrderModel.getPrice());
					item.put("buyNum", buddnistCeremonyCommodityOrderModel.getBuy_num());
					item.put("type", model.getType());
					BuddnistCeremonyBuyerInfoModel buyerInfo = buyerInfoService.getModel(buddnistCeremonyCommodityOrderModel.getBuyer_info_id());
					item.put("nickName", buyerInfo.getName());
					String url = "https://wx.zizaihome.com/commodity/commodityOrder?orderId="+Encryptstr.encrypt(buddnistCeremonyCommodityOrderModel.getId().toString())+"&pageType=1";
					if(ConfigReadUtils.loadConfig("conf/server.properties").get("isTest").equals("1")){
						url = "http://test.zizaihome.com/commodity/commodityOrder?orderId="+Encryptstr.encrypt(buddnistCeremonyCommodityOrderModel.getId().toString())+"&pageType=1&isTest=1";
					}
					else if(ConfigReadUtils.loadConfig("conf/server.properties").get("isTest").equals("2")){
						url = "http://test2.zizaihome.com/commodity/commodityOrder?orderId="+Encryptstr.encrypt(buddnistCeremonyCommodityOrderModel.getId().toString())+"&pageType=1&isTest=2";
					}
					item.put("url", url);
					list.add(item);
				}
				
			}
			json.put("total", orderList.getTotalRecordCount());
			json.put("hasNextPage", orderList.hasNextPage());
			json.put("list", list);
		}
		else{
			json.put("total", 0);
			json.put("hasNextPage", false);
			json.put("list", list);
		}
		
		return succRequest("处理成功",json);
	}

	@Override
	protected JSONObject postMethod(JSONObject json) {
		return this.getMethod(json);
	}

}
