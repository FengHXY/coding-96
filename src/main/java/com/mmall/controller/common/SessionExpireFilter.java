package com.mmall.controller.common;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.client.HttpServerErrorException;

import com.alipay.api.internal.util.StringUtils;
import com.mmall.common.Const;
import com.mmall.pojo.User;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisPoolUtil;

public class SessionExpireFilter implements Filter{

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		
		String loginToken = CookieUtil.readLoginToken(httpServletRequest);
		if(StringUtils.isEmpty(loginToken)){
			//判断loginToken是否为空或者“”
			//如果不为空，符合条件，继续拿user信息
			String userJsonStr = RedisPoolUtil.get(loginToken);
			User user = JsonUtil.string2Obj(userJsonStr, User.class);
			if(user != null){
				//如果user不为空，则重置session的时间，即调用expire
				RedisPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
			}
		}
		filterChain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
