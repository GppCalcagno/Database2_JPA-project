package it.polimi.db2.projectdb.controllers;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import org.thymeleaf.context.WebContext;

import it.polimi.db2.projectdb.entities.Employee;
import it.polimi.db2.projectdb.entities.FixedInternet;
import it.polimi.db2.projectdb.entities.FixedPhone;
import it.polimi.db2.projectdb.entities.MobileInternet;
import it.polimi.db2.projectdb.entities.MobilePhone;
import it.polimi.db2.projectdb.entities.OptionalProduct;
import it.polimi.db2.projectdb.entities.ServicePackage;
import it.polimi.db2.projectdb.entities.User;
import it.polimi.db2.projectdb.entities.ValidityPeriod;
import it.polimi.db2.projectdb.services.OptionalProductServices;
import it.polimi.db2.projectdb.services.ServicePakcageServices;
import it.polimi.db2.projectdb.services.ServicesServices;
import it.polimi.db2.projectdb.services.ValidityPeriodServices;

@WebServlet("/HomeEmployee")
public class GoToHomeEmployee extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.projectdb.services/ServicesServices")
	private ServicesServices sService;
	@EJB(name = "it.polimi.db2.projectdb.services/OptionalProductServices")
	private OptionalProductServices oService;
	@EJB(name = "it.polimi.db2.projectdb.services/ValidityPeriodServices")
	private ValidityPeriodServices vService;
	@EJB(name = "it.polimi.db2.projectdb.services/ServicePakcageServices")
	private ServicePakcageServices serService;
	
	public GoToHomeEmployee() {
		super();
	}
	
	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Employee employee;
		Boolean existEmp = false;
		try {
			employee = (Employee) request.getSession().getAttribute("employee");
			if(employee != null)
				existEmp = true;
		}
		catch(Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Where is my gold?1");
			return;
		}
		
		List<FixedInternet> fixIntList = null;
		try {
			fixIntList = sService.findAllFixInt();
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "fi");
			return;
		}
		
		List<FixedPhone> fixPhoList = null;
		try {
			fixPhoList = sService.findAllFixPho();
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "fp");
			return;
		}
		
		List<MobilePhone> mobPhoList = null;
		try {
			mobPhoList = sService.findAllMobPho();
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "mp");
			return;
		}
		
		List<MobileInternet> mobIntList = null;
		try {
			mobIntList = sService.findAllMobInt();
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "mi");
			return;
		}
		
		List<OptionalProduct> optProdList = null;
		try {
			optProdList = oService.findAllOptProd();
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "opt");
			return;
		}
		
		List<ValidityPeriod> valPerList = null;
		try {
			valPerList = vService.findAllValPeriod();
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "vp");
			return;
		}
		
		List<ServicePackage> servPackList = null;
		try {
			servPackList = serService.findAllServPackages();
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "vp");
			return;
		}
		
		String path = "/WEB-INF/HomeEmployee.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		
		ctx.setVariable("employee", employee);
		ctx.setVariable("existEmp", existEmp);
		
		ctx.setVariable("fixIntList", fixIntList);
		ctx.setVariable("fixPhoList", fixPhoList);
		ctx.setVariable("mobPhoList", mobPhoList);
		ctx.setVariable("mobIntList", mobIntList);
		
		ctx.setVariable("optProdList", optProdList);
		
		ctx.setVariable("valPerList", valPerList);
		
		ctx.setVariable("servPackList", servPackList);
		
		templateEngine.process(path, ctx, response.getWriter());
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {}
}
