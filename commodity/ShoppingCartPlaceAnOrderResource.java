package com.zizaihome.api.resources.commodity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.restlet.Request;

import com.zizaihome.api.db.model.BuddnistCeremonyBuyerInfoModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityOrderModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityShoppingCartModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommoditySubdivideModel;
import com.zizaihome.api.db.model.UserModel;
import com.zizaihome.api.db.model.ZizaijiaUserCouponModel;
import com.zizaihome.api.resources.base.BaseResource;
import com.zizaihome.api.service.BuddnistCeremonyBuyerInfoService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityOrderService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityShoppingCartService;
import com.zizaihome.api.service.BuddnistCeremonyCommoditySubdivideService;
import com.zizaihome.api.service.UserService;
import com.zizaihome.api.service.ZizaijiaUserCouponService;
import com.zizaihome.api.utils.PayUtils;

public class ShoppingCartPlaceAnOrderResource extends BaseResource {

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected JSONObject getMethod(JSONObject json) {
		UserService userService = new UserService();
		BuddnistCeremonyCommodityShoppingCartService buddnistCeremonyCommodityShoppingCartService = new BuddnistCeremonyCommodityShoppingCartService();
		BuddnistCeremonyCommodityService buddnistCeremonyCommodityService = new BuddnistCeremonyCommodityService();
		BuddnistCeremonyCommoditySubdivideService buddnistCeremonyCommoditySubdivideService = new BuddnistCeremonyCommoditySubdivideService();
		BuddnistCeremonyBuyerInfoService buddnistCeremonyBuyerInfoService = new BuddnistCeremonyBuyerInfoService();
		ZizaijiaUserCouponService zizaijiaUserCouponService = new ZizaijiaUserCouponService();
		BuddnistCeremonyCommodityOrderService buddnistCeremonyCommodityOrderService = new BuddnistCeremonyCommodityOrderService();
		String orderDateList = getParameter("orderDateList","[]");
		UserModel user = userService.getModel(userId);
		JSONArray orderDateJSONList = JSONArray.fromObject(orderDateList);
		double price = 0.0;
		List<BuddnistCeremonyCommodityOrderModel> buddnistCeremonyCommodityOrderList = new ArrayList<BuddnistCeremonyCommodityOrderModel>();
		for(int i=0;i<orderDateJSONList.size();i++){
			JSONObject orderDateJSON = orderDateJSONList.getJSONObject(i);
			BuddnistCeremonyCommodityShoppingCartModel buddnistCeremonyCommodityShoppingCart = buddnistCeremonyCommodityShoppingCartService.getModel(orderDateJSON.getInt("shoppingCartId"));
			//查询用户是否拥有代金券如果拥有代金券进行代金券记录并减免代金券相对金额
			int couponNum = 0;
			int couponPrice = 0;
			JSONArray jsonList = new JSONArray();
			if(orderDateJSON.has("couponIds")){
				jsonList = orderDateJSON.getJSONArray("couponIds");
				for(int b=0;b<jsonList.size();b++){
					ZizaijiaUserCouponModel zizaijiaUserCouponModel = zizaijiaUserCouponService.getModel(Integer.parseInt(jsonList.get(i).toString()));
					if(zizaijiaUserCouponModel != null){
						couponNum = couponNum+1;
						couponPrice = couponPrice+zizaijiaUserCouponModel.getPrice();
					}
				}
			}
			double orderPrice = 0.0;
			String subdivideName = "";
			BuddnistCeremonyCommodityModel buddnistCeremonyCommodity = buddnistCeremonyCommodityService.getModel(buddnistCeremonyCommodityShoppingCart.getCommodity_id());
			
			if (buddnistCeremonyCommodityShoppingCart.getSubdivide_id() > 0) {
				int isHaveSub = 0;
				List<BuddnistCeremonyCommoditySubdivideModel> commoditySubdivideList = buddnistCeremonyCommoditySubdivideService
						.findByCommodityId(buddnistCeremonyCommodity.getId());
				for (BuddnistCeremonyCommoditySubdivideModel commoditySubdivide : commoditySubdivideList) {
					if (commoditySubdivide.getId().intValue() == buddnistCeremonyCommodityShoppingCart.getSubdivide_id()) {
						isHaveSub = 1;
					}
				}
				if (isHaveSub == 0) {
					return failRequest("参数错误，无法支付请重试", json);
				}
			}
			
			if(buddnistCeremonyCommodityShoppingCart.getSubdivide_id() == 0){
				if(buddnistCeremonyCommodity.getStock() < orderDateJSON.getInt("num") && buddnistCeremonyCommodity.getStock() != -1){
					return failRequest("库存不足",json);
				}
				orderPrice = buddnistCeremonyCommodity.getPrice()*orderDateJSON.getInt("num")-couponPrice/100;
			}
			else{
				BuddnistCeremonyCommoditySubdivideModel buddnistCeremonyCommoditySubdivide = buddnistCeremonyCommoditySubdivideService.getModel(buddnistCeremonyCommodityShoppingCart.getSubdivide_id());
				if(buddnistCeremonyCommoditySubdivide.getStock() < orderDateJSON.getInt("num") && buddnistCeremonyCommoditySubdivide.getStock() != -1){
					return failRequest("库存不足",json);
				}
				orderPrice = buddnistCeremonyCommoditySubdivide.getPrice()*orderDateJSON.getInt("num")-couponPrice/100;
				subdivideName = buddnistCeremonyCommoditySubdivide.getName();
			}
			String name = "";
			String mobile = "";
			String address = "";
			String postscriptStr = orderDateJSON.getString("postscriptStr");
			postscriptStr = postscriptStr.replace("\\", "\\\\");
			postscriptStr = postscriptStr.replace("\n", "\\n");
			if(postscriptStr.subSequence(0, 1).equals("\"")){
				postscriptStr = postscriptStr.substring(1, postscriptStr.length());
			}
			if(postscriptStr.subSequence(postscriptStr.length()-1, postscriptStr.length()).equals("\"")){
				postscriptStr = postscriptStr.substring(0, postscriptStr.length()-1);
			}
			JSONArray posiscriptJSONList = JSONArray.fromObject(postscriptStr);
			JSONArray posiscriptJSONList2 = new JSONArray();
			String wish = "";
			for(int a=0;a<posiscriptJSONList.size();a++){
				JSONObject posiscriptJSON = posiscriptJSONList.getJSONObject(a);
				if(posiscriptJSON.getInt("type") == 4){
					name = posiscriptJSON.getString("value");
				}
				else if(posiscriptJSON.getInt("type") == 5){
					mobile = posiscriptJSON.getString("value");
				}
				else if(posiscriptJSON.getInt("type") == 6){
					address = posiscriptJSON.getString("value");
				}
				if(posiscriptJSON.getInt("type") != 13){
					posiscriptJSONList2.add(posiscriptJSON);
				}
				if(posiscriptJSON.getInt("type") == 15){
					wish = posiscriptJSON.getString("value");
				}
			}
			BuddnistCeremonyBuyerInfoModel userInfo = new BuddnistCeremonyBuyerInfoModel();
			userInfo.setAdd_time(new Date());
			userInfo.setAddress(address);
			userInfo.setMobile(mobile);
			userInfo.setName(name);
			userInfo.setUpdate_time(new Date());
			int userInfoId = buddnistCeremonyBuyerInfoService.insert(userInfo);
			BuddnistCeremonyCommodityOrderModel buddnistCeremonyCommodityOrder = new BuddnistCeremonyCommodityOrderModel();
			buddnistCeremonyCommodityOrder.setAbbot_id(0);
			buddnistCeremonyCommodityOrder.setAdd_time(new Date());
			buddnistCeremonyCommodityOrder.setBuy_num(orderDateJSON.getInt("num"));
			buddnistCeremonyCommodityOrder.setBuyer_info_id(userInfoId);
			buddnistCeremonyCommodityOrder.setCommodity_id(buddnistCeremonyCommodityShoppingCart.getCommodity_id());
			buddnistCeremonyCommodityOrder.setCoupon_num(couponNum);
			buddnistCeremonyCommodityOrder.setCoupon_price(couponPrice);
			buddnistCeremonyCommodityOrder.setDispose_pic_url("");
			buddnistCeremonyCommodityOrder.setIs_cryptonym_buy(0);
			buddnistCeremonyCommodityOrder.setIs_finish(0);
			buddnistCeremonyCommodityOrder.setIs_print(0);
			buddnistCeremonyCommodityOrder.setIs_sort_order(0);
			buddnistCeremonyCommodityOrder.setName(buddnistCeremonyCommodity.getName());
			buddnistCeremonyCommodityOrder.setOld_user_id(0);
			buddnistCeremonyCommodityOrder.setOrder_id(0);
			buddnistCeremonyCommodityOrder.setPay_type(0);
			buddnistCeremonyCommodityOrder.setPosiscript(posiscriptJSONList2.toString());
			buddnistCeremonyCommodityOrder.setPrice(orderPrice);
			buddnistCeremonyCommodityOrder.setRemark("");
			buddnistCeremonyCommodityOrder.setSort_time(new Date());
			buddnistCeremonyCommodityOrder.setSubdiride_id(buddnistCeremonyCommodityShoppingCart.getSubdivide_id());
			buddnistCeremonyCommodityOrder.setSubdivideName(subdivideName);
			buddnistCeremonyCommodityOrder.setTemple_id(buddnistCeremonyCommodity.getTemple_id());
			buddnistCeremonyCommodityOrder.setUpdate_time(new Date());
			buddnistCeremonyCommodityOrder.setUser_coupon_ids(jsonList.toString());
			buddnistCeremonyCommodityOrder.setUser_id(userId);
			buddnistCeremonyCommodityOrder.setWish(wish);
			int buddnistCeremonyCommodityOrderId = buddnistCeremonyCommodityOrderService.insert(buddnistCeremonyCommodityOrder);
			buddnistCeremonyCommodityOrder.setId(buddnistCeremonyCommodityOrderId);
			buddnistCeremonyCommodityOrderList.add(buddnistCeremonyCommodityOrder);
			price = price+orderPrice;
		}
		JSONObject payMessage = PayUtils.getPayMessage(user, price, realip, "购物车下单", 2, 0,1,0);
		for(BuddnistCeremonyCommodityOrderModel buddnistCeremonyCommodityOrder:buddnistCeremonyCommodityOrderList){
			buddnistCeremonyCommodityOrder.setOrder_id(payMessage.getInt("orderId"));
			buddnistCeremonyCommodityOrderService.updateAny(buddnistCeremonyCommodityOrder);
		}
		if(!sid.equals("")){
			payMessage.put("isChanzai", 1);
			payMessage.put("giveChanzaiOrderId", Integer.parseInt(payMessage.get("orderId").toString()));
			payMessage.put("chanzaiId", sid);
		}
		else{
			payMessage.put("isChanzai", 0);
			payMessage.put("giveChanzaiOrderId", 0);
			payMessage.put("chanzaiId", "");
		}
		payMessage.put("chanzaiUserId", user.getId());
		return succRequest("成功",payMessage);
	}

	@Override
	protected JSONObject postMethod(JSONObject json) {
		return this.getMethod(json);
	}

}
