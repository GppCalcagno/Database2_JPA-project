package it.polimi.db2.projectdb.entities;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="ValidityPeriod", schema = "db_telco")
@NamedQuery(name="ValidityPeriod.findAll", query="SELECT v FROM ValidityPeriod v")
public class ValidityPeriod {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int ID;
	
	private int NumMonths;
	
	private BigDecimal MonthlyFee;
	
	@ManyToMany(mappedBy="valPeriodList", fetch = FetchType.EAGER)
	private List<ServicePackage> servPackList;
	
	@OneToMany(mappedBy="ValPeriod_ID", fetch = FetchType.EAGER)
	private List<Order> OrderList;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getNumMonths() {
		return NumMonths;
	}

	public void setNumMonths(int numMonths) {
		NumMonths = numMonths;
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
	
	public void addServPack(ServicePackage servPack) {
		getServPackList().add(servPack);
	}
	
	public void removeServPack(ServicePackage servPack) {
		getServPackList().remove(servPack);
	}
	
	public void addOrder(Order order){
		OrderList.add(order);
	}
}
