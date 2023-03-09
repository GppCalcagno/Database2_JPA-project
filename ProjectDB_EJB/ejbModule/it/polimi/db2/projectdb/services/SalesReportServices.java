package it.polimi.db2.projectdb.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import it.polimi.db2.projectdb.entities.Alert;
import it.polimi.db2.projectdb.entities.InsolventUsers;
import it.polimi.db2.projectdb.entities.SalesReport1;
import it.polimi.db2.projectdb.entities.SalesReport2;
import it.polimi.db2.projectdb.entities.SalesReport3;
import it.polimi.db2.projectdb.entities.SuspendedOrders;
import it.polimi.db2.projectdb.exceptions.SalesReportException;

@Stateless
public class SalesReportServices {
	@PersistenceContext(unitName = "ProjectDB_EJB")
	private EntityManager em;
	
	public SalesReportServices() {};
	
	public List<SalesReport1> getSalesReport1() throws SalesReportException{
		List<SalesReport1> SalesReport1List = null;
		
		try {
			SalesReport1List = em.createNamedQuery("SalesReport1.findAll", SalesReport1.class).getResultList();
			for(SalesReport1 i :SalesReport1List)
				em.refresh(i);
			return SalesReport1List;
		}
		catch(PersistenceException e) {
			throw new SalesReportException("CANT RETRIVE SALEREPORT1 IN DB.");
		}
		
	}
	
	public List<SalesReport2> getSalesReport2() throws SalesReportException{
		List<SalesReport2> SalesReport2List = null;
		
		try {
			SalesReport2List = em.createNamedQuery("SalesReport2.findAll", SalesReport2.class).getResultList();
			for(SalesReport2 i :SalesReport2List)
				em.refresh(i);
			return SalesReport2List;
		}
		catch(PersistenceException e) {
			throw new SalesReportException("CANT RETRIVE SALEREPORT2 IN DB.");
		}
		
	}
	
	public List<SalesReport3> getSalesReport3() throws SalesReportException{
		List<SalesReport3> SalesReport3List = null;
		
		try {
			//pick only the first result
			SalesReport3List = em.createNamedQuery("SalesReport3.findAll", SalesReport3.class).setMaxResults(1).getResultList();
			for(SalesReport3 i :SalesReport3List)
				em.refresh(i);
			return SalesReport3List;
		}
		catch(PersistenceException e) {
			throw new SalesReportException("CANT RETRIVE SALEREPORT3 IN DB.");
		}
	}
	
	public List<InsolventUsers> getInsolventUsers() throws SalesReportException{
		List<InsolventUsers> InsolventUsersList = null;
		
		try {
			InsolventUsersList = em.createNamedQuery("InsolventUsers.findAll", InsolventUsers.class).getResultList();
			for(InsolventUsers i : InsolventUsersList)
				em.refresh(i);
			return InsolventUsersList;
		}
		catch(PersistenceException e) {
			throw new SalesReportException("CANT RETRIVE InsolventUsers IN DB.");
		}
	}
	
	public List<SuspendedOrders> getSuspendedOrders() throws SalesReportException{
		List<SuspendedOrders> SuspendedOrdersList = null;
		
		try {
			SuspendedOrdersList = em.createNamedQuery("SuspendedOrders.findAll", SuspendedOrders.class).getResultList();
			for (SuspendedOrders i : SuspendedOrdersList)
				em.refresh(i);
			return SuspendedOrdersList;
		}
		catch(PersistenceException e) {
			throw new SalesReportException("CANT RETRIVE SuspendedOrders IN DB.");
		}
	}
	
	public List<Alert> getAlerts() throws SalesReportException{
		List<Alert> AlertsList = null;
		
		try {
			AlertsList = em.createNamedQuery("Alert.findAll", Alert.class).getResultList();
			
			for(Alert i : AlertsList)
				em.refresh(i);
			return AlertsList;
		}
		catch(PersistenceException e) {
			throw new SalesReportException("CANT RETRIVE Alert IN DB.");
		}
	}
}
