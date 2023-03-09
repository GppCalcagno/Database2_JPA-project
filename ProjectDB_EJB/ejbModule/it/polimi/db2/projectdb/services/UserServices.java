package it.polimi.db2.projectdb.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import it.polimi.db2.projectdb.entities.Order;
import it.polimi.db2.projectdb.entities.User;
import it.polimi.db2.projectdb.exceptions.CredentialsException;

@Stateless
public class UserServices {
	@PersistenceContext(unitName = "ProjectDB_EJB")
	private EntityManager em;
	
	public UserServices(){}
	
	public User checkCredentials(String usrn, String pwd) throws CredentialsException, NonUniqueResultException {
		List<User> uList = null;
		try {
			uList = em.createNamedQuery("User.checkCredentials", User.class).setParameter(1, usrn).setParameter(2, pwd).getResultList();
		} catch (PersistenceException e) {
			throw new CredentialsException("Could not verify credentals");
		}
		if (uList.isEmpty()) return null;
		else if (uList.size() == 1)	return uList.get(0);
		throw new NonUniqueResultException("More than one user registered with same credentials");
	}
	
	public void createUser(String name, String email, String password) throws CredentialsException{
		
		User user= em.find(User.class, name);
		
		if(user!= null)
			throw new CredentialsException("More than one user registered with same credentials");
		user= new User(name,email,password);
		
		em.persist(user);
		System.out.println("INFO: NEW CLIENT PERSIST: " + em.contains(user));
		em.flush();	
	}
	
	public void updateUserStatus(User user) throws Exception {
		List<Order> orderList;
		try {
			
			//here we could use a for with the list of order, checking the status of the order. We used named query to test db access
			orderList = em.createNamedQuery("Order.rejectedOrdersFromUserID", Order.class).setParameter("user_ID", user.getUsername()).getResultList();
		} catch (PersistenceException e) {
			throw new Exception("Could not update profile");
		}
		user.setStatus(orderList.size()==0);
		try {
			em.merge(user);
			em.flush();
			System.out.println("INFO: User Status UPDATED");
		} catch (PersistenceException e) {
			throw new Exception("Could not update profile");
		}
	}
}

