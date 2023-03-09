package it.polimi.db2.projectdb.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Order", schema = "db_telco")
@NamedQuery(name = "Order.rejectedOrdersFromUserID", query = "SELECT o FROM Order o JOIN o.user_ID u WHERE locate(:user_ID, u.username)>0 AND o.Status = \"REJECTED\"")
public class Order {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int ID;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date DataOrd;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date StartOrd;
	
	private BigDecimal TotalValue;
	
	private String Status;

	@ManyToOne
	@JoinColumn(name="user_ID")
	private User user_ID;
	
	@ManyToOne
	@JoinColumn(name="ServPack_ID")
	private ServicePackage ServPack_ID;
	
	@ManyToOne
	@JoinColumn(name="ValPeriod_ID")
	private ValidityPeriod ValPeriod_ID;
	
	
	@OneToOne(mappedBy="Order_ID", fetch = FetchType.LAZY)
	private ActivationSchedule schedule;
		
		
	@OneToMany(mappedBy="Order_ID", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Payment> payment;
	
	@ManyToMany(mappedBy="orderList", fetch = FetchType.EAGER)
	private List<OptionalProduct> optProdList;
	
	public Order() {};
	
	//Constructor For CreateOrder (Logged)
	public Order(User user, ServicePackage SP, List<OptionalProduct> OPTlist, ValidityPeriod validityPer, Date ActivationDate) {
		this.user_ID=user;
		
		this.ServPack_ID=SP;
		this.ValPeriod_ID=validityPer;
		this.StartOrd=ActivationDate;
		this.optProdList=new ArrayList<>();
		this.payment= new ArrayList<>();
		
		for(OptionalProduct OPT: OPTlist) {
			optProdList.add(OPT);
			OPT.addOrder(this);
		}
				
		user.addOrder(this);
		validityPer.addOrder(this);
		
		setTotalValue();
	}
	
	//Constructor For CreateOrder (NON Logged)
	public Order(ServicePackage SP, List<OptionalProduct> OPTlist, ValidityPeriod validityPer, Date ActivationDate) {
		this.ServPack_ID=SP;
		this.ValPeriod_ID=validityPer;
		this.StartOrd=ActivationDate;
		this.optProdList=new ArrayList<>();
		this.payment= new ArrayList<>();
		
		for(OptionalProduct OPT: OPTlist) {
			optProdList.add(OPT);
			OPT.addOrder(this);
		}
		
		validityPer.addOrder(this);
		setTotalValue();
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public Date getDataOrd() {
		return DataOrd;
	}

	public void setDataOrd() {
		if(DataOrd==null)
		this.DataOrd = new Date(); //already initialized with current date
	}

	public Date getStartOrd() {
		return StartOrd;
	}

	public void setStartOrd(Date startOrd) {
		StartOrd = startOrd;
	}

	public BigDecimal getTotalValue() {
		return TotalValue;
	}

	//Set the correct Value of Order
	public void setTotalValue() {
		float sum=0;
		
		sum= ValPeriod_ID.getMonthlyFee().floatValue() * ValPeriod_ID.getNumMonths();
		
		try {
			for(OptionalProduct OPT: optProdList) {
				sum= sum + OPT.getMonthlyFee().floatValue() *  ValPeriod_ID.getNumMonths();
			}
		}catch(NullPointerException e) {
			e.printStackTrace();
		}
		TotalValue= BigDecimal.valueOf(sum);
	}
	
	public String getStatus() {
		return Status;
	}

	public void setStatus(Boolean state) {
		if(state)
			Status = "ACCEPTED";
		else
			Status = "REJECTED";
	}

	public User getUser_ID() {
		return user_ID;
	}

	public void setUser_ID(User user) {
		this.user_ID = user;
		user.addOrder(this);
	}

	public ServicePackage getServPack_ID() {
		return ServPack_ID;
	}

	public void setServPack_ID(ServicePackage servPack_ID) {
		ServPack_ID = servPack_ID;
	}
		
	public ValidityPeriod getValPeriod_ID() {
		return ValPeriod_ID;
	}

	public void setValPeriod_ID(ValidityPeriod valPeriod_ID) {
		ValPeriod_ID = valPeriod_ID;
	}

	public ActivationSchedule getSchedule() {
		return schedule;
	}

	public void setSchedule(ActivationSchedule schedule) {
		this.schedule = schedule;
	}

	public List<Payment> getPayment() {
		return payment;
	}
	
	public void addPayment(Payment pay) {
		payment.add(pay);
		
	}
	
	public Payment payOrder(Order order) {
		return new Payment(order); 		
	}

	public List<OptionalProduct> getOptProdList() {
		return optProdList;
	}
	
	
	//to check if is a new order 
	public Boolean isRepeated() {
		try {
		return Status.equals("REJECTED");
		}
		catch (Exception e) {
			//if order is new the status isn't settled yet
			return false;
		}
	}
}
