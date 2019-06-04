package com.quark.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.core.Controller;
import com.jfinal.handler.Handler;
import com.jfinal.render.Render;
import com.jfinal.render.RenderFactory;

public class H5Handler extends Handler {

	@Override
	public void handle(String target, HttpServletRequest request,
			HttpServletResponse response, boolean[] isHandled) {
		//处理rp请求
		System.out.println("===="+target);
		if (target.startsWith("/latest")) {
			target = "/latest/";
			System.out.println("=="+target);
		}
		nextHandler.handle(target, request, response, isHandled);
	}
}
