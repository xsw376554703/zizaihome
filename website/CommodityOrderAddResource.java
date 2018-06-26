package com.zizaihome.api.resources.website;

import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.restlet.Request;

import com.zizaihome.api.db.model.BuddnistCeremonyBuyerInfoModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityOrderModel;
import com.zizaihome.api.db.model.UserModel;
import com.zizaihome.api.resources.base.BaseResource;
import com.zizaihome.api.service.BuddnistCeremonyBuyerInfoService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityOrderService;
import com.zizaihome.api.service.UserService;
import com.zizaihome.api.utils.PayUtils;

public class CommodityOrderAddResource extends BaseResource {

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected JSONObject getMethod(JSONObject json) {
		UserService userService = new UserService();
		BuddnistCeremonyBuyerInfoService buddnistCeremonyBuyerInfoService = new BuddnistCeremonyBuyerInfoService();
		BuddnistCeremonyCommodityOrderService buddnistCeremonyCommodityOrderService = new BuddnistCeremonyCommodityOrderService();
		double price = getParameter("price",0.0);
		int isCryptonymBuy = getParameter("isCryptonymBuy",0);
		String name = getParameter("name","");
		String mobile = getParameter("mobile","");
		String address = getParameter("address","");
		int abbotId = getParameter("abbotId",0);
		int templeId = getParameter("templeId",0);
		
		UserModel user = userService.getModel(userId);
		
		if(name.equals("")){
			name = user.getNick_name();
		}
		
		int isNeedPay = 1;
		int isFinish = 0;
		String subdivideName = "";
		String commodityName = "";
		//如果该佛事需要支付
		if(isNeedPay == 1 && price > 0){
			//查询用户是否拥有代金券如果拥有代金券进行代金券记录并减免代金券相对金额
			price = price*100;
			//获取拉起微信支付的所需信息
			JSONObject payMessage = PayUtils.getPayMessage(user, price/100, realip, "功德箱捐赠/供养", 2, templeId,1,0);
			//插入购买者信息
			BuddnistCeremonyBuyerInfoModel buddnistCeremonyBuyerInfoModel = new BuddnistCeremonyBuyerInfoModel();
			buddnistCeremonyBuyerInfoModel.setAdd_time(new Date());
			buddnistCeremonyBuyerInfoModel.setName(name);
			buddnistCeremonyBuyerInfoModel.setAddress(address);
			buddnistCeremonyBuyerInfoModel.setMobile(mobile);
			buddnistCeremonyBuyerInfoModel.setUpdate_time(new Date());
			int buyerInfoId = buddnistCeremonyBuyerInfoService.insert(buddnistCeremonyBuyerInfoModel);
			//插入佛事订单信息
			BuddnistCeremonyCommodityOrderModel buddnistCeremonyCommodityOrderModel = new BuddnistCeremonyCommodityOrderModel();
			buddnistCeremonyCommodityOrderModel.setAbbot_id(abbotId);
			buddnistCeremonyCommodityOrderModel.setAdd_time(new Date());
			buddnistCeremonyCommodityOrderModel.setBuy_num(1);
			buddnistCeremonyCommodityOrderModel.setBuyer_info_id(buyerInfoId);
			buddnistCeremonyCommodityOrderModel.setCommodity_id(0);
			buddnistCeremonyCommodityOrderModel.setDispose_pic_url("");
			buddnistCeremonyCommodityOrderModel.setIs_cryptonym_buy(isCryptonymBuy);
			buddnistCeremonyCommodityOrderModel.setIs_finish(isFinish);
			buddnistCeremonyCommodityOrderModel.setIs_print(0);
			buddnistCeremonyCommodityOrderModel.setIs_sort_order(0);
			buddnistCeremonyCommodityOrderModel.setOrder_id(payMessage.getInt("orderId"));
			buddnistCeremonyCommodityOrderModel.setPay_type(0);
			buddnistCeremonyCommodityOrderModel.setPosiscript("[]");
			buddnistCeremonyCommodityOrderModel.setPrice(price/100);
			buddnistCeremonyCommodityOrderModel.setRemark("");
			buddnistCeremonyCommodityOrderModel.setSort_time(null);
			buddnistCeremonyCommodityOrderModel.setSubdiride_id(0);
			buddnistCeremonyCommodityOrderModel.setTemple_id(templeId);
			buddnistCeremonyCommodityOrderModel.setUpdate_time(new Date());
			buddnistCeremonyCommodityOrderModel.setUser_id(user.getId());
			buddnistCeremonyCommodityOrderModel.setName(commodityName);
			buddnistCeremonyCommodityOrderModel.setSubdivideName(subdivideName);
			buddnistCeremonyCommodityOrderModel.setUser_coupon_ids("[]");
			buddnistCeremonyCommodityOrderModel.setCoupon_num(0);
			buddnistCeremonyCommodityOrderModel.setCoupon_price(0);
			int id = buddnistCeremonyCommodityOrderService.insert(buddnistCeremonyCommodityOrderModel);
			buddnistCeremonyCommodityOrderModel.setId(id);
			payMessage.put("isNeedPay", isNeedPay);
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
			payMessage.put("orderId", Integer.parseInt(payMessage.get("orderId").toString()));
			payMessage.put("chanzaiUserId", user.getId());
			payMessage.put("commodityId", buddnistCeremonyCommodityOrderModel.getId());
			return succRequest("成功",payMessage);
		}
		return failRequest("失败",json);
	}

	@Override
	protected JSONObject postMethod(JSONObject json) {
		return this.getMethod(json);
	}

}
