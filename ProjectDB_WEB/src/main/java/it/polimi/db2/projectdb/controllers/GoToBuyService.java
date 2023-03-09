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

import it.polimi.db2.projectdb.entities.OptionalProduct;
import it.polimi.db2.projectdb.entities.ServicePackage;
import it.polimi.db2.projectdb.entities.User;
import it.polimi.db2.projectdb.entities.ValidityPeriod;
import it.polimi.db2.projectdb.services.ServicePakcageServices;

@WebServlet("/BuyPage")
public class GoToBuyService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.projectdb.services/ServicePackageServices")
	private ServicePakcageServices SPS;
	
	public GoToBuyService() {super();}

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
		String path = "/WEB-INF/BuyPage.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		
		//To show user 
		User user;
		Boolean existUser = false;
		try {
			user = (User) request.getSession().getAttribute("user");
			if(user != null)
				existUser = true;
		}
		catch(Exception e) {response.sendError(HttpServletResponse.SC_BAD_REQUEST, "CAN'T LOAD USER IN BUYPAGE");	return;}
		
		ctx.setVariable("user", user);
		ctx.setVariable("existUser", existUser);
		ctx.setVariable("errorMsg", request.getSession().getAttribute("errorMsg")); //to show error message between 2 servlet app
		request.getSession().setAttribute("errorMsg",null);
		
		//TO Show Service Packs
		List<ServicePackage> servPackList = null;
		Boolean listEmpty = false;
		try {
			servPackList = SPS.findAllServPackages();
			if (servPackList == null) {
				listEmpty = true;
			}
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to get ServicePackage is GoToBuyService");
			return;
		}

		//TO Show Optional Product
		ctx.setVariable("servPackList", servPackList);
		ctx.setVariable("listEmpty", listEmpty);		
		Integer servicePackID = null;
		String servicePackName = null;
		Boolean isSelectedSP;
		
	    try {
	    	servicePackID =Integer.parseInt(request.getParameter("servPackID"));
    	
	    	for(ServicePackage x : servPackList) {
	    		if(x.getID() == servicePackID)
	    		servicePackName= x.getName();		
	    	}
	    	
	    	
			isSelectedSP=true;
	    } catch (NumberFormatException | NullPointerException e) {
	    	isSelectedSP=false;
	    }
	    
	    ctx.setVariable("isSelectedSP", isSelectedSP);
	    ctx.setVariable("servicePackID", servicePackID);
	    ctx.setVariable("servicePackName", servicePackName);
	    
	    if(isSelectedSP) {
	    	ServicePackage SP;
	    	Boolean arePresentOPT;
		    try {
		    	SP=SPS.findServPackByID(servicePackID);
		    	if(SP.getOptProdList().isEmpty()) arePresentOPT=false;
		    	else arePresentOPT=true;
		    } catch (Exception e) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to get OptionalProduct is GoToBuyService");
				return;
				}
		    request.getSession().setAttribute("choosenSP", SP);
		    List<OptionalProduct> OTPList=SP.getOptProdList();
		    
		    ctx.setVariable("arePresentOPT", arePresentOPT);
		    ctx.setVariable("OPTList", OTPList); 
		    ctx.setVariable("OPTnum",(int)OTPList.size());
		    
		    
		    List<ValidityPeriod> validityPerList= SP.getValPeriodList();
		    ctx.setVariable("VPList", validityPerList); 
		    ctx.setVariable("VPTnum",(int)validityPerList.size());
		    
		    
		
	    }//end SelectedSP

		templateEngine.process(path, ctx, response.getWriter());
	}
	
	
	
	
}
