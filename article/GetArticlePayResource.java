package com.zizaihome.api.resources.article;

import java.util.Date;

import net.sf.json.JSONObject;

import org.restlet.Request;

import com.zizaihome.api.db.model.UserModel;
import com.zizaihome.api.db.model.ZizaijiaArticleModel;
import com.zizaihome.api.db.model.ZizaijiaArticlePayModel;
import com.zizaihome.api.resources.base.BaseResource;
import com.zizaihome.api.service.UserService;
import com.zizaihome.api.service.ZizaijiaArticlePayService;
import com.zizaihome.api.service.ZizaijiaArticleService;
import com.zizaihome.api.utils.PayUtils;

public class GetArticlePayResource extends BaseResource {

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected JSONObject getMethod(JSONObject json) {
		ZizaijiaArticleService zizaijiaArticleService = new ZizaijiaArticleService();
		ZizaijiaArticlePayService articlePayService = new ZizaijiaArticlePayService();
		UserService userService = new UserService();
		int articleId = getParameter("articleId",0);
		double price = getParameter("price",0.0);
		int isAnonymous = getParameter("isAnonymous",0);
		String nickName = getParameter("nickName","");
		ZizaijiaArticleModel article = zizaijiaArticleService.getModel(articleId);
		UserModel user = userService.getModel(userId);
		JSONObject payMessage = PayUtils.getPayMessage(user, price, realip, article.getTitle().length()>42?article.getTitle().substring(0,42):article.getTitle(), 5, article.getTemple_id(),1,0);
		ZizaijiaArticlePayModel articlePay = new ZizaijiaArticlePayModel();
		articlePay.setAdd_time(new Date());
		articlePay.setArticle_id(article.getId());
		articlePay.setIs_anonymous(isAnonymous);
		articlePay.setNick_name(nickName);
		articlePay.setOrder_id(Integer.parseInt(payMessage.getString("orderId")));
		articlePay.setPay_type(0);
		articlePay.setPrice(price);
		articlePay.setTemple_id(article.getTemple_id());
		articlePay.setUpdate_time(new Date());
		articlePay.setUser_id(user.getId());
		articlePayService.insert(articlePay);
		
		if(!sid.equals("")){
			payMessage.put("isChanzai", 1);
			payMessage.put("giveChanzaiOrderId", Integer.parseInt(payMessage.getString("orderId")));
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