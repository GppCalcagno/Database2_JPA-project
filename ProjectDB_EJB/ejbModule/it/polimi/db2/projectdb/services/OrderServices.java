package it.polimi.db2.projectdb.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import it.polimi.db2.projectdb.entities.OptionalProduct;
import it.polimi.db2.projectdb.entities.Order;
import it.polimi.db2.projectdb.entities.Payment;


@Stateless
public class OrderServices {
	@PersistenceContext(unitName = "ProjectDB_EJB")
	private EntityManager em;
	
	public OrderServices() {}
	
	public List<Order> rejectedOrdersFromUserID(String user_ID){
		if(user_ID == null | user_ID.isEmpty())
			return null;
		return em.createNamedQuery("Order.rejectedOrdersFromUserID", Order.class).setParameter("user_ID", user_ID).getResultList();
	}

	public Order getFromID(int ID) {
		try {
			return em.find(Order.class, ID);
		}catch(Exception e) {	/*nothing to do*/	}
		return null;	
	}
		
	public String loadAndPayOrder (Order order) {
		Order check= em.find(Order.class, order.getID());
		Payment payment = new Payment(order);
		
		//if order already present update the status, otherwise load the entire entry
		if(check==null) {
			em.persist(order);
			for(OptionalProduct opt: order.getOptProdList())	
				em.merge(opt);									
		}	
		else {
			em.merge(order);
		}
		
		em.flush();
		
		System.out.println("INFO: NEW ORDER PERSIST: " + em.contains(order));
		System.out.println("INFO: NEW PAYMENT PERSIST: " + em.contains(payment));
		
		return payment.getStatus();
	}
}
