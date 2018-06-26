package com.zizaihome.api.resources.commodity;

import java.util.HashMap;
import java.util.Map;

import org.restlet.Request;

import com.zizaihome.web.resource.base.BaseResultModel;
import com.zizaihome.web.resource.base.BaseWebResource;

import net.sf.json.JSONObject;
/**
 * 
 * @author ChenLY
 * @time 上午11:39:51
 */
public class GetvCardHtmlResource extends BaseWebResource{

	@Override
	protected void initParams(Request request) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void getMethod(BaseResultModel br) {
		/*dataModel.put("chanzaiIsVirtual", chanzaiIsVirtual);
	    if(!sid.equals("")){
	    	dataModel.put("isChanzai", 1);
	    }
	    else{
	    	dataModel.put("isChanzai", 0);
	    }*/
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("access_token", accessToken);
		br.setDataModel(jsonObject);
		br.setHtmlPath("buddhistCeremony/vCard.html");
	}

	@Override
	protected void postMethod(BaseResultModel br) {
		getMethod(br);
	}

}
