package com.zizaihome.api.resources.commodity;

import net.sf.json.JSONObject;

import org.restlet.Request;

import com.zizaihome.api.resources.base.BaseResource;
import com.zizaihome.api.utils.HttpRequestUtils;
import com.zizaihome.api.utils.LogUtils;
import com.zizaihome.api.utils.QiniuUtils;

public class UploadPicResource extends BaseResource {

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected JSONObject getMethod(JSONObject json) {
		String file = getParameter("file","");
		log.info("file====="+file);
		String mediaId = getParameter("mediaId", "");
		if(mediaId.equals("")){
			String qiniuPicUrl = QiniuUtils.uploadPic2QiNiu(file,"");
			json.put("data", qiniuPicUrl);
		}
		else{
			JSONObject requestResult = HttpRequestUtils.httpGet("http://wxtoken.zizaihome.com/getToken/");
			String url = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=" + requestResult.getString("access_token") + "&media_id=" + mediaId;
			LogUtils.log.info("UploadPicResource==="+url);
			String qiniuPicUrl = QiniuUtils.uploadPic2QiNiu("",url);
			json.put("data", qiniuPicUrl);
		}
		return succRequest("上传成功",json);
	}

	@Override
	protected JSONObject postMethod(JSONObject json) {
		return this.getMethod(json);
	}

}
