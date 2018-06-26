package com.zizaihome.api.resources.commodity;

import java.util.Date;

import net.sf.json.JSONObject;

import org.restlet.Request;

import com.zizaihome.api.db.model.BuddnistCeremonyCommodityShoppingCartModel;
import com.zizaihome.api.db.model.UserModel;
import com.zizaihome.api.resources.base.BaseResource;
import com.zizaihome.api.service.BuddnistCeremonyCommodityShoppingCartService;
import com.zizaihome.api.service.UserService;
import com.zizaihome.api.utils.ConfigReadUtils;
import com.zizaihome.api.utils.HttpRequestUtils;

public class AddCommodity2ShoppingCartResource extends BaseResource {

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected JSONObject getMethod(JSONObject json) {
		UserService userService = new UserService();
		BuddnistCeremonyCommodityShoppingCartService buddnistCeremonyCommodityShoppingCartService = new BuddnistCeremonyCommodityShoppingCartService(); 
		int commodityId = getParameter("commodityId",0);
		int subdivideId = getParameter("subdivide_id",0);
		int buy_num = getParameter("buy_num",0);
		if(sid == null || sid.equals("") || sid.equals("null")){
			sid = getParameter("sid","");
		}
		
		//如果禅在APP调用订单接口去禅在接口获取用户id
		if(!sid.equals("")){
			String chanzaiHost = ConfigReadUtils.loadConfigByConfDir("server.properties").getProperty("chanzai_host");
			String chanzaiUrl = chanzaiHost+"/app_h5/zizaihome/temple_check";
			JSONObject jsonParam = new JSONObject();
			jsonParam.put("sid", sid);
			JSONObject chanzaiResult = HttpRequestUtils.httpPostFormData(chanzaiUrl, jsonParam);
			if(chanzaiResult.getInt("code") == 1){
				JSONObject chanzaiResultData = chanzaiResult.getJSONObject("data");
				userId = Integer.parseInt(chanzaiResultData.getString("user_id"));
			}
		}
		
		BuddnistCeremonyCommodityShoppingCartModel buddnistCeremonyCommodityShoppingCartModel = buddnistCeremonyCommodityShoppingCartService.findByUserIdAndcommodityIdAndSubdivideId(userId, commodityId, subdivideId);
		if(buddnistCeremonyCommodityShoppingCartModel == null){
			BuddnistCeremonyCommodityShoppingCartModel newBuddnistCeremonyCommodityShoppingCartModel = new BuddnistCeremonyCommodityShoppingCartModel();
			newBuddnistCeremonyCommodityShoppingCartModel.setAdd_time(new Date());
			newBuddnistCeremonyCommodityShoppingCartModel.setBuy_num(buy_num);
			newBuddnistCeremonyCommodityShoppingCartModel.setCommodity_id(commodityId);
			newBuddnistCeremonyCommodityShoppingCartModel.setOp_status(0);
			newBuddnistCeremonyCommodityShoppingCartModel.setSubdivide_id(subdivideId);
			newBuddnistCeremonyCommodityShoppingCartModel.setUpdate_time(new Date());
			newBuddnistCeremonyCommodityShoppingCartModel.setUser_id(userId);
			buddnistCeremonyCommodityShoppingCartService.insert(newBuddnistCeremonyCommodityShoppingCartModel);
		}
		else{
			buddnistCeremonyCommodityShoppingCartModel.setBuy_num(buddnistCeremonyCommodityShoppingCartModel.getBuy_num()+1);
			buddnistCeremonyCommodityShoppingCartService.updateAny(buddnistCeremonyCommodityShoppingCartModel);
		}
		return succRequest("添加成功",json);
	}

	@Override
	protected JSONObject postMethod(JSONObject json) {
		return this.getMethod(json);
	}

}
