package it.polimi.db2.projectdb.controllers;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.db2.projectdb.entities.Order;
import it.polimi.db2.projectdb.services.OrderServices;
import it.polimi.db2.projectdb.services.UserServices;

@WebServlet("/CheckOUT")
public class OrderCheckOUT extends HttpServlet{
	@EJB(name = "it.polimi.db2.projectdb.services/OrderServices")
	private OrderServices orderServices;
	@EJB(name = "it.polimi.db2.projectdb.services/UserServices")
	private UserServices userServices;
	
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	
	
	public OrderCheckOUT() {};
	
	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException { doGet(request, response);}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = "/WEB-INF/OrderCheckOut.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		
		Order order;
		try {
			order = (Order)request.getSession().getAttribute("order");
		}
		catch(Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "CANT TAKE DATA from HTLM in CreateOrder");
			return;
		}
		
		
		try {	
			order.setDataOrd();
			String result= orderServices.loadAndPayOrder(order);
			userServices.updateUserStatus(order.getUser_ID());

			//set htlm variables
			ctx.setVariable("status", result);
			request.getSession().setAttribute("order", null);
		}
		catch(Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "CANT ACCESS DB in CreateOrder");
			return;
		}
		templateEngine.process(path, ctx, response.getWriter());
	}
}
