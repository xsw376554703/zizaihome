package com.zizaihome.api.resources.commodity;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.restlet.Request;

import com.zizaihome.api.db.model.BuddnistCeremonyCommodityModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityShoppingCartModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommoditySubdivideModel;
import com.zizaihome.api.resources.base.BaseResource;
import com.zizaihome.api.service.BuddnistCeremonyCommodityService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityShoppingCartService;
import com.zizaihome.api.service.BuddnistCeremonyCommoditySubdivideService;

public class UpdateShoppingCartResource extends BaseResource {

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected JSONObject getMethod(JSONObject json) {
		BuddnistCeremonyCommodityShoppingCartService buddnistCeremonyCommodityShoppingCartService = new BuddnistCeremonyCommodityShoppingCartService();
		BuddnistCeremonyCommodityService buddnistCeremonyCommodityService = new BuddnistCeremonyCommodityService();
		BuddnistCeremonyCommoditySubdivideService buddnistCeremonyCommoditySubdivideService = new BuddnistCeremonyCommoditySubdivideService();
		String 	updateDataList = getParameter("updateData","[]");
		JSONArray updateDataJSONList = JSONArray.fromObject(updateDataList);
		for(int i=0;i<updateDataJSONList.size();i++){
			JSONObject updateDataJSON = updateDataJSONList.getJSONObject(i);
			BuddnistCeremonyCommodityShoppingCartModel shoppingCart = buddnistCeremonyCommodityShoppingCartService.getModel(updateDataJSON.getInt("id"));
			if(updateDataJSON.has("buyNum")){
				if(shoppingCart.getSubdivide_id() == 0){
					BuddnistCeremonyCommodityModel buddnistCeremonyCommodity = buddnistCeremonyCommodityService.getModel(shoppingCart.getCommodity_id());
					if(buddnistCeremonyCommodity.getStock() < updateDataJSON.getInt("buyNum") && buddnistCeremonyCommodity.getStock() != -1){
						return failRequest(buddnistCeremonyCommodity.getName()+"库存只剩"+buddnistCeremonyCommodity.getStock()+",请重新调整数量",json);
					}
				}
				else{
					BuddnistCeremonyCommoditySubdivideModel buddnistCeremonyCommoditySubdivide = buddnistCeremonyCommoditySubdivideService.getModel(shoppingCart.getSubdivide_id());
					if(buddnistCeremonyCommoditySubdivide.getStock() < updateDataJSON.getInt("buyNum") && buddnistCeremonyCommoditySubdivide.getStock() != -1){
						return failRequest(buddnistCeremonyCommoditySubdivide.getName()+"库存只剩"+buddnistCeremonyCommoditySubdivide.getStock()+",请重新调整数量",json);
					}
				}
				shoppingCart.setBuy_num(updateDataJSON.getInt("buyNum"));
			}
			if(updateDataJSON.has("isDel")){
				if(updateDataJSON.getInt("isDel") == 1){
					shoppingCart.setOp_status(-1);
				}
			}
			buddnistCeremonyCommodityShoppingCartService.updateAny(shoppingCart);
		}
		return succRequest("成功",json);
	}

	@Override
	protected JSONObject postMethod(JSONObject json) {
		return this.getMethod(json);
	}

}
