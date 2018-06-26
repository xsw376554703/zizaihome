package com.zizaihome.api.resources.commodity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.restlet.Request;

import com.zizaihome.api.db.model.BuddnistCeremonyCommodityModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityOrderModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityPicsModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommoditySubdivideModel;
import com.zizaihome.api.db.model.OrderModel;
import com.zizaihome.api.db.model.TempleModel;
import com.zizaihome.api.db.model.UserModel;
import com.zizaihome.api.resources.base.BaseResource;
import com.zizaihome.api.service.BuddnistCeremonyCommodityOrderService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityPicsService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityService;
import com.zizaihome.api.service.BuddnistCeremonyCommoditySubdivideService;
import com.zizaihome.api.service.OrderService;
import com.zizaihome.api.service.TempleService;
import com.zizaihome.api.service.UserService;
import com.zizaihome.api.utils.Encryptstr;

public class GetCommodityOrderMessageResource extends BaseResource {

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected JSONObject getMethod(JSONObject json) {
		BuddnistCeremonyCommodityOrderService commodityOrderService = new BuddnistCeremonyCommodityOrderService();
		BuddnistCeremonyCommodityService commodityService = new BuddnistCeremonyCommodityService();
		BuddnistCeremonyCommodityPicsService commodityPicsService = new BuddnistCeremonyCommodityPicsService();
		BuddnistCeremonyCommoditySubdivideService subdivideService = new BuddnistCeremonyCommoditySubdivideService();
		OrderService orderService = new OrderService();
		UserService userService = new UserService();
		TempleService templeService = new TempleService();
		String oid = getParameter("orderId","");
		int orderId = 0;
		if(!oid.equals("")){
			String oidDecodeStr = Encryptstr.decode(oid);
		    Pattern pattern = Pattern.compile("[0-9]*"); 
		    Matcher isNum = pattern.matcher(oidDecodeStr);
		    if( !isNum.matches() ){
		    	orderId = Integer.parseInt(oid);
		    }
		    else{
		    	orderId = Integer.parseInt(oidDecodeStr);
		    }
		}
		int pageType = getParameter("pageType",0);
		BuddnistCeremonyCommodityOrderModel commodityOrder = commodityOrderService.getModel(orderId);
		TempleModel temple = templeService.getModel(commodityOrder.getTemple_id());
		if(commodityOrder.getConversion_type() == 2 && pageType == 2 && commodityOrder.getConversion_father_order_id() == 0){
			commodityOrder = commodityOrderService.findConversionOrderId(orderId);
		}
		BuddnistCeremonyCommodityModel commodity = commodityService.getModel(commodityOrder.getCommodity_id());
		JSONObject dataMap = new JSONObject();
		dataMap.put("commodityName", commodity.getName());
		if(commodityOrder.getSubdiride_id() == 0){
			dataMap.put("subdirideName", "");
			List<BuddnistCeremonyCommodityPicsModel> pics = commodityPicsService.findByCommodityId(commodity.getId());
			dataMap.put("commodityPic", pics.get(0).getPic_url());
		}
		else{
			BuddnistCeremonyCommoditySubdivideModel subdivide = subdivideService.getModel(commodityOrder.getSubdiride_id());
			dataMap.put("subdirideName", subdivide.getName());
			dataMap.put("commodityPic", subdivide.getPic_url());
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		OrderModel order = orderService.getModel(commodityOrder.getOrder_id());
		dataMap.put("orderNo", order.getOrderNo());
		dataMap.put("payTime", sdf.format(commodityOrder.getPay_time()));
		dataMap.put("payPrice", commodityOrder.getPrice());
		dataMap.put("buyNum", commodityOrder.getBuy_num());
		UserModel user = userService.getModel(commodityOrder.getUser_id());
		dataMap.put("nickName", user.getNick_name());
		dataMap.put("posiscript", commodityOrder.getPosiscript());
		dataMap.put("pic", commodityOrder.getDispose_pic_url());
		dataMap.put("remark", commodityOrder.getRemark());
		dataMap.put("isFinish", commodityOrder.getIs_finish());
		dataMap.put("disposeName", temple.getName());
		dataMap.put("mobile", temple.getMobile());
		dataMap.put("disposeTime", sdf.format(commodityOrder.getUpdate_time()));
		dataMap.put("video", commodityOrder.getDispose_video_url());
		json.put("data", dataMap);
		if(!json.getJSONObject("data").get("posiscript").toString().equals("")){
			json.getJSONObject("data").put("posiscript", "\""+json.getJSONObject("data").get("posiscript").toString()+"\"");
		}
		return succRequest("成功",json);
	}

	@Override
	protected JSONObject postMethod(JSONObject json) {
		return this.getMethod(json);
	}

}
