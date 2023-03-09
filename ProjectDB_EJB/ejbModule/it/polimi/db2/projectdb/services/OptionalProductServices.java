package it.polimi.db2.projectdb.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import it.polimi.db2.projectdb.entities.OptionalProduct;
import it.polimi.db2.projectdb.entities.ServicePackage;
import it.polimi.db2.projectdb.exceptions.ServicePackageException;

@Stateless
public class OptionalProductServices {
	@PersistenceContext(unitName = "ProjectDB_EJB")
	private EntityManager em;
	
	public OptionalProductServices() {};
	
	public OptionalProduct getFromID(int ID) throws PersistenceException {
		try {
			return em.find(OptionalProduct.class,ID);
		}catch (PersistenceException e){
			throw new PersistenceException("Could not Access OPT in OptionalProductServices");
			}
	}
	
	public List<OptionalProduct> findAllOptProd() throws ServicePackageException{
		List<OptionalProduct> optProdList = null;
		
		try {
			optProdList = em.createNamedQuery("OptionalProduct.findAll", OptionalProduct.class).getResultList();
		}
		catch(PersistenceException e) {
			throw new ServicePackageException("Service Packages not found.");
		}
		if (optProdList.isEmpty())
			return null;
		else 
			return optProdList; 
	}
	
	public void createOptionalProduct(String name_prod, BigDecimal monFee, List<Integer> servPackList_int) {
		OptionalProduct optProd = new OptionalProduct(name_prod, monFee);
	
		if(servPackList_int!=null) {
			List<ServicePackage> servPackList = new ArrayList<>();
			for(Integer s:servPackList_int) {
				servPackList.add(em.find(ServicePackage.class, s));
			}
 			optProd.addManyServPack(servPackList);
		}
		
		// for debugging: let's check if mission is managed
		System.out.println("Method createServPack before em.persist(optProd)");
		System.out.println("Is servPack object managed?  " + em.contains(optProd));
		
		em.persist(optProd);
		
		System.out.println("Method createServPack AFTER em.persist(optProd)");
		System.out.println("Is servPack object managed?  " + em.contains(optProd));
		
		em.flush();
	}
}
