package it.polimi.db2.projectdb.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "OptionalProduct", schema = "db_telco")
@NamedQuery(name="OptionalProduct.findAll", query="SELECT o FROM OptionalProduct o")
public class OptionalProduct {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int ID;
	
	private String Name;
	
	private BigDecimal MonthlyFee;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinTable(name="Include",
					joinColumns= @JoinColumn(name="OptProd_ID"),
					inverseJoinColumns= @JoinColumn(name="ServPack_ID"))
	private List<ServicePackage> servPackList;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="SelectOpt",
					joinColumns= @JoinColumn(name="OptProd_ID"),
					inverseJoinColumns= @JoinColumn(name="Order_ID"))
	private List<Order> orderList;
	
	public OptionalProduct() {}
	
	public OptionalProduct(String name, BigDecimal monthlyFee) {
		Name = name;
		MonthlyFee = monthlyFee;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public BigDecimal getMonthlyFee() {
		return MonthlyFee;
	}

	public void setMonthlyFee(BigDecimal monthlyFee) {
		MonthlyFee = monthlyFee;
	}

	public List<ServicePackage> getServPackList() {
		return servPackList;
	}

	public void setServPackList(List<ServicePackage> servPackList) {
		this.servPackList = servPackList;
	}
	
	public void addManyServPack(List<ServicePackage> servPackList) {
		 for(ServicePackage s:servPackList) {
			 s.addOptProd(this);
		 }
	}
	
	public void addServPack(ServicePackage servPack) {
		if(getServPackList()==null)
			servPackList=new ArrayList<>();
		getServPackList().add(servPack);
	}
	
	public void removeServPack(ServicePackage servPack) {
		getServPackList().remove(servPack);
	}
	
	public void addOrder(Order order ) {
		if(orderList==null)
			orderList=new ArrayList<>();
		orderList.add(order);
	}

	public List<Order> getOrderList() {
		return orderList;
	}
	
	
}
