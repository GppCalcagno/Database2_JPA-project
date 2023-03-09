package it.polimi.db2.projectdb.controllers;

import java.io.IOException;
import java.util.List;

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

import it.polimi.db2.projectdb.entities.*;
import it.polimi.db2.projectdb.services.ServicePakcageServices;
import it.polimi.db2.projectdb.services.ServicesServices;

@WebServlet("/GetServicePackageDetails")
public class GetServicePackageDetails extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.projectdb.services/ServicePakcageServices")
	private ServicePakcageServices sPService;
	@EJB(name = "it.polimi.db2.projectdb.services/ServicesServices")
	private ServicesServices servService;
	
	public GetServicePackageDetails() {}
	
	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user;
		Boolean existUser = false;
		try {
			user = (User) request.getSession().getAttribute("user");
			if(user != null)
				existUser = true;
		}
		catch(Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Where is my gold?");
			return;
		}
		
		Integer servPackID = null;
		try {
			servPackID = Integer.parseInt(request.getParameter("servPackID"));
		} catch (NumberFormatException | NullPointerException e) {
			// for debugging e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect param values");
			return;
		}
		
		ServicePackage servPack = null;
		try {
			servPack = sPService.findServPackByID(servPackID);
			if (servPack == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Resource not found");
				return;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
		List<ValidityPeriod> valPeriodList = null;
		try {
			valPeriodList = servPack.getValPeriodList();
			if (valPeriodList == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Resource not found");
				return;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
		Boolean hasOptProd = false;
		List<OptionalProduct> optProdList = null;
		
		if(!servPack.getOptProdList().isEmpty()) {
			hasOptProd = true;
			optProdList = servPack.getOptProdList();
		}
		
		Boolean hasFP = false;
		Boolean hasFI = false;
		Boolean hasMP = false;
		Boolean hasMI = false;
		
		FixedInternet fI = null;
		MobileInternet mI = null;
		MobilePhone mP = null;
		FixedPhone fP = null;
		
		String path = "/WEB-INF/ServicePackageDetails.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		
		if(servPack.getFP_ID() != null) {
			hasFP = true;
			fP = servPack.getFP_ID();
			ctx.setVariable("fP", fP);
		}
		if(servPack.getFI_ID() != null) {
			hasFI = true;
			fI = servPack.getFI_ID();
			ctx.setVariable("fI", fI);
		}
		if(servPack.getMI_ID() != null) {
			hasMI = true;
			mI = servPack.getMI_ID();
			ctx.setVariable("mI", mI);
		}
		if(servPack.getMP_ID() != null) {
			hasMP = true;
			mP = servPack.getMP_ID();
			ctx.setVariable("mP", mP);
		}
		
		ctx.setVariable("hasFP", hasFP);
		ctx.setVariable("hasFI", hasFI);
		ctx.setVariable("hasMP", hasMP);
		ctx.setVariable("hasMI", hasMI);
		
		ctx.setVariable("servPack", servPack);
		
		ctx.setVariable("hasOptProd", hasOptProd);
		ctx.setVariable("optProdList", optProdList);
		
		ctx.setVariable("valPeriodList", valPeriodList);
		
		ctx.setVariable("user", user);
		ctx.setVariable("existUser", existUser);
		
		templateEngine.process(path, ctx, response.getWriter());
	}
	
	public void destroy() {}

}
