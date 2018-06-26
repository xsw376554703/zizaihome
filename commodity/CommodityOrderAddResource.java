package com.zizaihome.api.resources.commodity;

import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.restlet.Request;

import com.zizaihome.api.db.model.BuddnistCeremonyBuyerInfoModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityOrderModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommoditySubdivideModel;
import com.zizaihome.api.db.model.OrderModel;
import com.zizaihome.api.db.model.TempleModel;
import com.zizaihome.api.db.model.UserModel;
import com.zizaihome.api.db.model.ZizaijiaUserCouponModel;
import com.zizaihome.api.resources.base.BaseResource;
import com.zizaihome.api.service.BuddnistCeremonyBuyerInfoService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityOrderService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityService;
import com.zizaihome.api.service.BuddnistCeremonyCommoditySubdivideService;
import com.zizaihome.api.service.OrderService;
import com.zizaihome.api.service.TempleService;
import com.zizaihome.api.service.UserService;
import com.zizaihome.api.service.VerificationCodeCheckService;
import com.zizaihome.api.service.ZizaijiaUserCouponService;
import com.zizaihome.api.utils.ConfigReadUtils;
import com.zizaihome.api.utils.HttpRequestUtils;
import com.zizaihome.api.utils.LogUtils;
import com.zizaihome.api.utils.PayUtils;

public class CommodityOrderAddResource extends BaseResource {

	@Override
	protected void initParams(Request request) {

	}

	@Override
	protected JSONObject getMethod(JSONObject json) {
		UserService userService = new UserService();
		BuddnistCeremonyCommodityService buddnistCeremonyCommodityService = new BuddnistCeremonyCommodityService();
		BuddnistCeremonyCommoditySubdivideService buddnistCeremonyCommoditySubdivideService = new BuddnistCeremonyCommoditySubdivideService();
		BuddnistCeremonyBuyerInfoService buddnistCeremonyBuyerInfoService = new BuddnistCeremonyBuyerInfoService();
		TempleService templeService = new TempleService();
		BuddnistCeremonyCommodityOrderService buddnistCeremonyCommodityOrderService = new BuddnistCeremonyCommodityOrderService();
		ZizaijiaUserCouponService zizaijiaUserCouponService = new ZizaijiaUserCouponService();
		OrderService orderService = new OrderService();
		int orderId = getParameter("orderId", 0);
		int commodityId = getParameter("commodityId", 0);
		double price = getParameter("price", 0.0);
		int buyNum = getParameter("buyNum", 0);
		String posiscript = getParameter("posiscript", "[]");
		int subdirideId = getParameter("subdirideId", 0);
		int isCryptonymBuy = getParameter("isCryptonymBuy", 0);
		String name = getParameter("name", "");
		String mobile = getParameter("mobile", "");
		String address = getParameter("address", "");
		int abbotId = getParameter("abbotId", 0);
		int templeId = getParameter("templeId", 0);
		String couponIds = getParameter("couponIds", "[]");
		if (sid.equals("")) {
			sid = getParameter("sid", "");
		}
		LogUtils.log.info("posiscript============================"+posiscript);
		// 如果禅在APP调用订单接口去禅在接口获取用户id
		if (!sid.equals("")) {
			String chanzaiHost = ConfigReadUtils.loadConfigByConfDir(
					"server.properties").getProperty("chanzai_host");
			String chanzaiUrl = chanzaiHost + "/app_h5/zizaihome/temple_check";
			JSONObject jsonParam = new JSONObject();
			jsonParam.put("sid", sid);
			JSONObject chanzaiResult = HttpRequestUtils.httpPostFormData(
					chanzaiUrl, jsonParam);
			if (chanzaiResult.getInt("code") == 1) {
				JSONObject chanzaiResultData = chanzaiResult
						.getJSONObject("data");
				userId = Integer.parseInt(chanzaiResultData
						.getString("user_id"));
			}
		}

		// 如果有orderId表示用户经过禅在未支付订单进行支付，由之前用户的订单获取新订单的订单信息
		if (orderId != 0) {
			OrderModel order = orderService.getModel(orderId);
			if (order != null) {
				List<BuddnistCeremonyCommodityOrderModel> commodityOrderList = buddnistCeremonyCommodityOrderService
						.findByOrderId(orderId);
				if (commodityOrderList.size() > 0) {
					commodityId = commodityOrderList.get(0).getCommodity_id();
					price = commodityOrderList.get(0).getPrice();
					buyNum = commodityOrderList.get(0).getBuy_num();
					posiscript = commodityOrderList.get(0).getPosiscript();
					subdirideId = commodityOrderList.get(0).getSubdiride_id();
					isCryptonymBuy = commodityOrderList.get(0)
							.getIs_cryptonym_buy();
					BuddnistCeremonyBuyerInfoModel buyerInfo = buddnistCeremonyBuyerInfoService
							.getModel(commodityOrderList.get(0)
									.getBuyer_info_id());
					if (buyerInfo != null) {
						name = buyerInfo.getName();
						mobile = buyerInfo.getMobile();
						address = buyerInfo.getAddress();
					}
					abbotId = commodityOrderList.get(0).getAbbot_id();
					templeId = commodityOrderList.get(0).getTemple_id();
				}
			}
		}

		// 处理附言信息

		posiscript = posiscript.replace("\\", "\\\\");
		posiscript = posiscript.replace("\n", "\\n");
		if (!posiscript.equals("") && posiscript.subSequence(0, 1).equals("\"")) {
			posiscript = posiscript.substring(1, posiscript.length());
		}
		if (!posiscript.equals("")
				&& posiscript.subSequence(posiscript.length() - 1,
						posiscript.length()).equals("\"")) {
			posiscript = posiscript.substring(0, posiscript.length() - 1);
		}
		JSONArray posiscriptJSONList = JSONArray.fromObject(posiscript);
		JSONArray posiscriptJSONList2 = new JSONArray();
		String wish = "";
		int code = 0;
		int is_cryptonym_wish = 0;
		VerificationCodeCheckService verifyService = new VerificationCodeCheckService();
		// 如果上传的附言有类型为提示文本框类型进行处理不存入数据库
		for (int i = 0; i < posiscriptJSONList.size(); i++) {
			JSONObject posiscriptJSON = posiscriptJSONList.getJSONObject(i);
			if(posiscriptJSON.has("type")) {
				if (posiscriptJSON.getInt("type") == 4) {
					name = posiscriptJSON.getString("value");
				}
				if (posiscriptJSON.getInt("type") == 5) {
					mobile = posiscriptJSON.getString("value");
					code = posiscriptJSON.optInt("code",0);
					if(code!=0 && verifyService.getMobileAndCode(mobile, code) == null){
						return failRequest("验证码不正确", json);
					}
				}
				if (posiscriptJSON.getInt("type") == 6) {
					address = posiscriptJSON.getString("value");
				}
				if (posiscriptJSON.getInt("type") != 13) {
					posiscriptJSONList2.add(posiscriptJSON);
				}
				if(posiscriptJSON.getInt("type") == 15){
					wish = posiscriptJSON.getString("value");
					is_cryptonym_wish = posiscriptJSON.optInt("is_cryptonym_wish",0);
				}
			}
			else {
				posiscriptJSONList2.add(posiscriptJSON);
			}
		}

		// 将附言的值更改成处理后的附言
		posiscript = posiscriptJSONList2.toString();
		UserModel user = userService.getModel(userId);
		BuddnistCeremonyCommodityModel buddnistCeremonyCommodity = buddnistCeremonyCommodityService
				.getModel(commodityId);

		if (subdirideId > 0) {
			int isHaveSub = 0;
			List<BuddnistCeremonyCommoditySubdivideModel> commoditySubdivideList = buddnistCeremonyCommoditySubdivideService
					.findByCommodityId(buddnistCeremonyCommodity.getId());
			for (BuddnistCeremonyCommoditySubdivideModel commoditySubdivide : commoditySubdivideList) {
				if (commoditySubdivide.getId().intValue() == subdirideId) {
					isHaveSub = 1;
				}
			}
			if (isHaveSub == 0) {
				return failRequest("参数错误，无法支付请重试", json);
			}
		}

		if (name.equals("")) {
			name = user.getNick_name();
		}

		int isNeedPay = 1;
		int isFinish = 0;
		String subdivideName = "";
		String commodityName = "";
		if (buddnistCeremonyCommodity != null) {
			// 获取佛事名称
			commodityName = buddnistCeremonyCommodity.getName();
			List<BuddnistCeremonyCommoditySubdivideModel> buddnistCeremonyCommoditySubdivideModelList = buddnistCeremonyCommoditySubdivideService
					.findByCommodityId(buddnistCeremonyCommodity.getId());
			// 如果该佛事有规格但是不传规格id返回错误信息不让用户支付
			if (buddnistCeremonyCommoditySubdivideModelList.size() > 0) {
				if (subdirideId == 0) {
					return failRequest("参数错误", json);
				}
			}
			// 如果有规格id查询规格是否库存足够用户购买如果库存不足返回错误信息并不让用户支付
			if (subdirideId > 0) {
				BuddnistCeremonyCommoditySubdivideModel buddnistCeremonyCommoditySubdivideModel = buddnistCeremonyCommoditySubdivideService
						.getModel(subdirideId);
				subdivideName = buddnistCeremonyCommoditySubdivideModel
						.getName();
				if (buddnistCeremonyCommoditySubdivideModel.getStock() != -1) {
					if (buddnistCeremonyCommoditySubdivideModel.getStock() < buyNum) {
						return failRequest("库存不足", json);
					}
				}
				isNeedPay = buddnistCeremonyCommoditySubdivideModel
						.getIs_need_pay();
			}
			// 如果没有传入规格id查询佛事库存是否足够用户购买如果库存不足返回错误信息并不让用户支付
			else {
				isNeedPay = buddnistCeremonyCommodity.getIs_need_pay();
				if (buddnistCeremonyCommodity.getStock() != -1) {
					if (buddnistCeremonyCommodity.getStock() < buyNum) {
						return failRequest("库存不足", json);
					}
				}
			}
			// 判断该佛事自动设置自动将订单设置为完成功能
			if (buddnistCeremonyCommodity.getIs_auto_finish() == 1) {
				isFinish = 1;
			}
		}
		// 获取佛事名称为微信订单拉起支付标题
		String orderMessageName = "功德箱捐赠/供养";
		if (buddnistCeremonyCommodity != null) {
			orderMessageName = buddnistCeremonyCommodity.getName();
		}
		if (templeId == 0) {
			TempleModel temple = templeService
					.getModel(buddnistCeremonyCommodity.getTemple_id());
			templeId = temple.getId();
		}
		// 如果该佛事需要支付
		if (isNeedPay == 1 && price > 0) {
			// 查询用户是否拥有代金券如果拥有代金券进行代金券记录并减免代金券相对金额
			int couponNum = 0;
			int couponPrice = 0;
			price = price * 100;
			JSONArray jsonList = JSONArray.fromObject(couponIds);
			for (int i = 0; i < jsonList.size(); i++) {
				ZizaijiaUserCouponModel zizaijiaUserCouponModel = zizaijiaUserCouponService
						.getModel(Integer.parseInt(jsonList.get(i).toString()));
				if (zizaijiaUserCouponModel != null) {
					couponNum = couponNum + 1;
					couponPrice = couponPrice
							+ zizaijiaUserCouponModel.getPrice();
					price = price - zizaijiaUserCouponModel.getPrice();
				}
			}
			// 获取拉起微信支付的所需信息
			JSONObject payMessage = PayUtils.getPayMessage(user, price / 100,
					realip, orderMessageName, 2, templeId,1,0);
			// 插入购买者信息
			BuddnistCeremonyBuyerInfoModel buddnistCeremonyBuyerInfoModel = new BuddnistCeremonyBuyerInfoModel();
			buddnistCeremonyBuyerInfoModel.setAdd_time(new Date());
			buddnistCeremonyBuyerInfoModel.setName(name);
			buddnistCeremonyBuyerInfoModel.setAddress(address);
			buddnistCeremonyBuyerInfoModel.setMobile(mobile);
			buddnistCeremonyBuyerInfoModel.setUpdate_time(new Date());
			int buyerInfoId = buddnistCeremonyBuyerInfoService
					.insert(buddnistCeremonyBuyerInfoModel);
			// 插入佛事订单信息
			BuddnistCeremonyCommodityOrderModel buddnistCeremonyCommodityOrderModel = new BuddnistCeremonyCommodityOrderModel();
			buddnistCeremonyCommodityOrderModel.setAbbot_id(abbotId);
			buddnistCeremonyCommodityOrderModel.setAdd_time(new Date());
			buddnistCeremonyCommodityOrderModel.setBuy_num(buyNum);
			buddnistCeremonyCommodityOrderModel.setBuyer_info_id(buyerInfoId);
			buddnistCeremonyCommodityOrderModel.setCommodity_id(commodityId);
			buddnistCeremonyCommodityOrderModel.setDispose_pic_url("");
			buddnistCeremonyCommodityOrderModel
					.setIs_cryptonym_buy(isCryptonymBuy);
			buddnistCeremonyCommodityOrderModel.setIs_finish(isFinish);
			buddnistCeremonyCommodityOrderModel.setIs_print(0);
			buddnistCeremonyCommodityOrderModel.setIs_sort_order(0);
			buddnistCeremonyCommodityOrderModel.setOrder_id(payMessage
					.getInt("orderId"));
			buddnistCeremonyCommodityOrderModel.setPay_type(0);
			buddnistCeremonyCommodityOrderModel.setPosiscript(posiscript);
			buddnistCeremonyCommodityOrderModel.setPrice(price / 100);
			buddnistCeremonyCommodityOrderModel.setRemark("");
			buddnistCeremonyCommodityOrderModel.setSort_time(null);
			buddnistCeremonyCommodityOrderModel.setSubdiride_id(subdirideId);
			buddnistCeremonyCommodityOrderModel.setTemple_id(templeId);
			buddnistCeremonyCommodityOrderModel.setUpdate_time(new Date());
			buddnistCeremonyCommodityOrderModel.setUser_id(user.getId());
			buddnistCeremonyCommodityOrderModel.setName(commodityName);
			buddnistCeremonyCommodityOrderModel.setSubdivideName(subdivideName);
			buddnistCeremonyCommodityOrderModel.setUser_coupon_ids(couponIds);
			buddnistCeremonyCommodityOrderModel.setCoupon_num(couponNum);
			buddnistCeremonyCommodityOrderModel.setCoupon_price(couponPrice);
			buddnistCeremonyCommodityOrderModel.setWish(wish);
			buddnistCeremonyCommodityOrderModel.setIs_cryptonym_wish(is_cryptonym_wish);
			int id = buddnistCeremonyCommodityOrderService
					.insert(buddnistCeremonyCommodityOrderModel);
			buddnistCeremonyCommodityOrderModel.setId(id);
			payMessage.put("isNeedPay", isNeedPay);
			if (!sid.equals("")) {
				payMessage.put("isChanzai", 1);
				payMessage.put("giveChanzaiOrderId",
						Integer.parseInt(payMessage.get("orderId").toString()));
				payMessage.put("chanzaiId", sid);
			} else {
				payMessage.put("isChanzai", 0);
				payMessage.put("giveChanzaiOrderId", 0);
				payMessage.put("chanzaiId", "");
			}
			payMessage.put("orderId",
					Integer.parseInt(payMessage.get("orderId").toString()));
			payMessage.put("chanzaiUserId", user.getId());
			payMessage.put("commodityId",
					buddnistCeremonyCommodityOrderModel.getId());
			return succRequest("成功", payMessage);
		}
		// 如果佛事不需要支付
		else {
			OrderModel order = new OrderModel();
			order.setUserId(user.getId());
			order.setStatus(1);
			order.setWxPrepayId("");
			order.setOrderXml("");
			order.setOrderNo(System.nanoTime() + "");
			order.setMoney(0.0);
			order.setWxTransactionId("");
			order.setResultXML("");
			order.setDetail(commodityName);
			order.setWxOpenId(user.getWx_openid());
			order.setBcUserId(user.getId());
			order.setType(2);
			order.setTemple_id(templeId);
			order.setPayType(0);
			order.setPayTime(new Date());
			int newOrderId = orderService.insert(order);
			// 插入购买者信息
			BuddnistCeremonyBuyerInfoModel buddnistCeremonyBuyerInfoModel = new BuddnistCeremonyBuyerInfoModel();
			buddnistCeremonyBuyerInfoModel.setAdd_time(new Date());
			buddnistCeremonyBuyerInfoModel.setName(name);
			buddnistCeremonyBuyerInfoModel.setAddress(address);
			buddnistCeremonyBuyerInfoModel.setMobile(mobile);
			buddnistCeremonyBuyerInfoModel.setUpdate_time(new Date());
			int buyerInfoId = buddnistCeremonyBuyerInfoService
					.insert(buddnistCeremonyBuyerInfoModel);
			// 插入佛事订单信息
			BuddnistCeremonyCommodityOrderModel buddnistCeremonyCommodityOrderModel = new BuddnistCeremonyCommodityOrderModel();
			buddnistCeremonyCommodityOrderModel.setAbbot_id(abbotId);
			buddnistCeremonyCommodityOrderModel.setAdd_time(new Date());
			buddnistCeremonyCommodityOrderModel.setBuy_num(buyNum);
			buddnistCeremonyCommodityOrderModel.setBuyer_info_id(buyerInfoId);
			buddnistCeremonyCommodityOrderModel.setCommodity_id(commodityId);
			buddnistCeremonyCommodityOrderModel.setDispose_pic_url("");
			buddnistCeremonyCommodityOrderModel
					.setIs_cryptonym_buy(isCryptonymBuy);
			buddnistCeremonyCommodityOrderModel.setIs_finish(isFinish);
			buddnistCeremonyCommodityOrderModel.setIs_print(0);
			buddnistCeremonyCommodityOrderModel.setIs_sort_order(0);
			buddnistCeremonyCommodityOrderModel.setOrder_id(newOrderId);
			buddnistCeremonyCommodityOrderModel.setPay_type(1);
			buddnistCeremonyCommodityOrderModel.setPosiscript(posiscript);
			buddnistCeremonyCommodityOrderModel.setPrice(price);
			buddnistCeremonyCommodityOrderModel.setRemark("");
			buddnistCeremonyCommodityOrderModel.setSort_time(null);
			buddnistCeremonyCommodityOrderModel.setSubdiride_id(subdirideId);
			buddnistCeremonyCommodityOrderModel.setTemple_id(templeId);
			buddnistCeremonyCommodityOrderModel.setUpdate_time(new Date());
			buddnistCeremonyCommodityOrderModel.setUser_id(user.getId());
			buddnistCeremonyCommodityOrderModel.setName(commodityName);
			buddnistCeremonyCommodityOrderModel.setSubdivideName(subdivideName);
			buddnistCeremonyCommodityOrderModel.setUser_coupon_ids("[]");
			buddnistCeremonyCommodityOrderModel.setCoupon_num(0);
			buddnistCeremonyCommodityOrderModel.setCoupon_price(0);	
			buddnistCeremonyCommodityOrderModel.setWish(wish);
			buddnistCeremonyCommodityOrderModel.setIs_cryptonym_wish(is_cryptonym_wish);
			int id = buddnistCeremonyCommodityOrderService.insert(buddnistCeremonyCommodityOrderModel);
			buddnistCeremonyCommodityOrderModel.setId(id);
			// 如果用户有传入规格id将规格的库存扣除用户购买的数量
			if (subdirideId != 0) {
				buddnistCeremonyCommodityService
						.updateCommodityJoinNumAndCollectMoney(
								buddnistCeremonyCommodityOrderModel.getPrice(),
								0, buddnistCeremonyCommodity.getId());
				BuddnistCeremonyCommoditySubdivideModel buddnistCeremonyCommoditySubdivideModel = buddnistCeremonyCommoditySubdivideService
						.getModel(subdirideId);
				if (buddnistCeremonyCommoditySubdivideModel.getStock() != -1) {
					buddnistCeremonyCommoditySubdivideService
							.updateStock(buddnistCeremonyCommoditySubdivideModel
									.getId());
				}
			}
			// 如果用户没有传入规格id将佛事的库存扣除用户购买的数量
			else if (buddnistCeremonyCommodity.getStock() != -1) {
				buddnistCeremonyCommodityService
						.updateCommodityJoinNumAndCollectMoney(
								buddnistCeremonyCommodityOrderModel.getPrice(),
								buyNum, buddnistCeremonyCommodity.getId());
			} else {
				buddnistCeremonyCommodityService
						.updateCommodityJoinNumAndCollectMoney(
								buddnistCeremonyCommodityOrderModel.getPrice(),
								0, buddnistCeremonyCommodity.getId());
			}
			json.put("isNeedPay", isNeedPay);
			if (!sid.equals("")) {
				json.put("isChanzai", 1);
				json.put("chanzaiId", sid);
			} else {
				json.put("isChanzai", 0);
				json.put("chanzaiId", "");
			}
			json.put("giveChanzaiOrderId", newOrderId);
			json.put("chanzaiUserId", user.getId());
			json.put("commodityId", buddnistCeremonyCommodityOrderModel.getId());
			return succRequest("成功", json);
		}
	}

	@Override
	protected JSONObject postMethod(JSONObject json) {
		return this.getMethod(json);
	}

}
