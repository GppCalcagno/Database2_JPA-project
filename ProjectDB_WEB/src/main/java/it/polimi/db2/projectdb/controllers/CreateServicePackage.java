package it.polimi.db2.projectdb.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.db2.projectdb.entities.Employee;
import it.polimi.db2.projectdb.entities.FixedInternet;
import it.polimi.db2.projectdb.entities.FixedPhone;
import it.polimi.db2.projectdb.entities.MobileInternet;
import it.polimi.db2.projectdb.entities.MobilePhone;
import it.polimi.db2.projectdb.entities.OptionalProduct;
import it.polimi.db2.projectdb.entities.ServicePackage;
import it.polimi.db2.projectdb.entities.ValidityPeriod;
import it.polimi.db2.projectdb.services.OptionalProductServices;
import it.polimi.db2.projectdb.services.ServicePakcageServices;
import it.polimi.db2.projectdb.services.ServicesServices;
import it.polimi.db2.projectdb.services.ValidityPeriodServices;

@WebServlet("/CreateServicePackage")
public class CreateServicePackage extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.projectdb.services/ServicePackageServices")
	private ServicePakcageServices sService;
	@EJB(name = "it.polimi.db2.projectdb.services/OptionalProductServices")
	private OptionalProductServices oService;
	@EJB(name = "it.polimi.db2.projectdb.services/ServicesServices")
	private ServicesServices esseService;
	@EJB(name = "it.polimi.db2.projectdb.services/ValidityPeriodServices")
	private ValidityPeriodServices vService;
	
	public CreateServicePackage() {
		super();
	}
	
	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		//templateResolver.setSuffix(".html");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// If the user is not logged in (not present in session) redirect to the login
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("employee") == null) {
			String loginpath = getServletContext().getContextPath() + "/index.html";
			response.sendRedirect(loginpath);
			return;
		}
		
		List<FixedInternet> fixIntList = null;
		try {
			fixIntList = esseService.findAllFixInt();
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "fi");
			return;
		}
		
		List<FixedPhone> fixPhoList = null;
		try {
			fixPhoList = esseService.findAllFixPho();
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "fp");
			return;
		}
		
		List<MobilePhone> mobPhoList = null;
		try {
			mobPhoList = esseService.findAllMobPho();
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "mp");
			return;
		}
		
		List<MobileInternet> mobIntList = null;
		try {
			mobIntList = esseService.findAllMobInt();
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
		
		String name_pack = null;
		Integer fI = null;
		Integer fP = null;
		Integer mI = null;
		Integer mP = null;
		List<Integer> OPTList = new ArrayList<>();
		String[] OPTs_str = null;
		List<Integer> VPList = new ArrayList<>();
		String[] VPs_str = null;
		
		try {
			name_pack = StringEscapeUtils.escapeJava(request.getParameter("name_pack"));
			if(fixIntList != null)
				fI = Integer.parseInt(request.getParameter("fI"));
			if(fixPhoList != null)
				fP = Integer.parseInt(request.getParameter("fP"));
			if(mobIntList != null)
				mI = Integer.parseInt(request.getParameter("mI"));
			if(mobPhoList != null)
				mP = Integer.parseInt(request.getParameter("mP"));
			if(optProdList != null)
				OPTs_str = request.getParameterValues("OPTs");
			if(valPerList != null)
				VPs_str = request.getParameterValues("VPs");
		}
		catch(Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "1CANT TAKE DATA from HTLM in CreateServicePackage");
			return;
		}
		
		if(OPTs_str!=null) {
			for(String s : OPTs_str) {
				try{
					OPTList.add(Integer.parseInt(s));
				}catch(Exception e) {
					e.printStackTrace();
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, "2CANT TAKE DATA from HTLM in CreateServicePackage");
					return;
				}
			}
		}
		
		if(VPs_str!=null) {
			for(String s : VPs_str) {
				try{
					VPList.add(Integer.parseInt(s));
				}catch(Exception e) {
					e.printStackTrace();
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, "3CANT TAKE DATA from HTLM in CreateServicePackage");
					return;
				}
			}
		}
		
		//se non dovessero esserci servizi
		if(fP==null)
			fP = -1;
		if(mP==null)
			mP = -1;
		if(fI==null)
			fI = -1;
		if(mI==null)
			mI = -1;
		
		String path;
		
		if((fP!=-1 || mP!=-1 || fI!=-1 || mI!=-1) && OPTs_str!=null && VPs_str!=null) {
			try {
				sService.createServPack(name_pack, fP, mP, fI, mI, OPTList, VPList);
			} catch (Exception e) {
				e.printStackTrace();
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to create servpack");
				return;
			}
			
			String ctxpath = getServletContext().getContextPath();
			path = ctxpath + "/HomeEmployee";
			response.sendRedirect(path);
		}
		else {
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			
			Employee employee;
			Boolean existEmp = false;
			try {
				employee = (Employee) request.getSession().getAttribute("employee");
				if(employee != null)
					existEmp = true;
			}
			catch(Exception e) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Create Service Package Error");
				return;
			}
			
			List<ServicePackage> servPackList = null;
			try {
				servPackList = sService.findAllServPackages();
			} catch (Exception e) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "vp");
				return;
			}
			
			ctx.setVariable("employee", employee);
			ctx.setVariable("existEmp", existEmp);
			
			ctx.setVariable("fixIntList", fixIntList);
			ctx.setVariable("fixPhoList", fixPhoList);
			ctx.setVariable("mobPhoList", mobPhoList);
			ctx.setVariable("mobIntList", mobIntList);
			
			ctx.setVariable("optProdList", optProdList);
			
			ctx.setVariable("valPerList", valPerList);
			
			ctx.setVariable("servPackList", servPackList);
			
			if(fP==-1 && mP==-1 && fI==-1 && mI==-1)
				ctx.setVariable("errorMsg_serv", "You have to insert at least a service");
			if(OPTs_str==null)
				ctx.setVariable("errorMsg_opt", "You have to insert at least one optional product");
			if(VPs_str==null)
				ctx.setVariable("errorMsg_valp", "You have to insert at least one validity period");
			path = "/WEB-INF/HomeEmployee.html";
			templateEngine.process(path, ctx, response.getWriter());
		}
	}
	
	public void destroy() {}
}
