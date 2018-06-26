package com.zizaihome.api.resources.article;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.restlet.Request;

import com.zizaihome.api.db.model.TempleModel;
import com.zizaihome.api.db.model.UserModel;
import com.zizaihome.api.db.model.ZizaijiaArticleModel;
import com.zizaihome.api.db.model.ZizaijiaTempleWebsiteIntrodactionModel;
import com.zizaihome.api.db.model.ZizaijiaTempleWebsiteIntrodactionPicsModel;
import com.zizaihome.api.service.TempleService;
import com.zizaihome.api.service.UserService;
import com.zizaihome.api.service.ZizaijiaArticleService;
import com.zizaihome.api.service.ZizaijiaTempleWebsiteIntrodactionPicsService;
import com.zizaihome.api.service.ZizaijiaTempleWebsiteIntrodactionService;
import com.zizaihome.web.resource.base.BaseResultModel;
import com.zizaihome.web.resource.base.BaseWebResource;

public class ArticleIndexResource extends BaseWebResource {

	@Override
	protected void initParams(Request request) {
		isPCBrowser = true;
	}
	
	
	

	@Override
	protected void getMethod(BaseResultModel br) {
		TempleService templeService = new TempleService();
		ZizaijiaArticleService articleService = new ZizaijiaArticleService();
		ZizaijiaTempleWebsiteIntrodactionService templeWebsiteIntrodactionService = new ZizaijiaTempleWebsiteIntrodactionService();
		ZizaijiaTempleWebsiteIntrodactionPicsService templeWebsiteIntrodactionPicsService = new ZizaijiaTempleWebsiteIntrodactionPicsService();
		UserService userService = new UserService();
		int articleId = getParameter("articleId",0);
		
		ZizaijiaArticleModel article = articleService.getModel(articleId);
		TempleModel temple = templeService.getModel(article.getTemple_id());
		
		//记录用户访问记录
		saveVisitRecord(articleId,2,temple.getId());
		
		ZizaijiaTempleWebsiteIntrodactionModel templeWebsiteIntrodactionModel = templeWebsiteIntrodactionService.findByTempleId(temple.getId());
		String pic = "";
		if(templeWebsiteIntrodactionModel != null){
			List<ZizaijiaTempleWebsiteIntrodactionPicsModel> templeWebsiteIntrodactionPics = templeWebsiteIntrodactionPicsService.findByIntrodactionId(templeWebsiteIntrodactionModel.getId());
			pic = templeWebsiteIntrodactionPics.get(0).getImg_url();
		}
		int isWeChat = 1;
		if(userId == 0){
			isWeChat = 0;	
		}
		if(article.getStatus() == 4){
			br.setHtmlPath("error/deleted.html");
		}
	    String nickName = "";
	    String headImg = "";
	    UserModel user = userService.getModel(userId);
	    if(user != null){
	    	nickName = user.getNick_name();
	    	headImg = user.getHead_img();
	    }
		Map<String, Object> dataModel = new HashMap<String, Object>();
		dataModel.put("articleId", articleId+"");
		dataModel.put("templeName", temple.getName());
		dataModel.put("articleTitle", article.getTitle());
		dataModel.put("nickName", nickName);
		dataModel.put("headImg", headImg);
		dataModel.put("templePic", pic);
//		dataModel.put("isChanzai", isChanzai);
		dataModel.put("isWeChat", isWeChat);
		dataModel.put("chanzaiIsVirtual", chanzaiIsVirtual);
		br.setDataModel(dataModel);
		br.setHtmlPath("article/index.html");
		
		
		
		
		
	}

	@Override
	protected void postMethod(BaseResultModel br) {
		
	}

}
