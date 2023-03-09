package it.polimi.db2.projectdb.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "ServicePackage", schema = "db_telco")
@NamedQuery(name="ServicePackage.findAll", query="SELECT s FROM ServicePackage s")
public class ServicePackage {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int ID;
	
	private String Name;
	
	@OneToMany(mappedBy="ServPack_ID", fetch= FetchType.EAGER) 
	private List<Order> orderList;
	
	@ManyToOne() 
	@JoinColumn(name="FP_ID")
	private FixedPhone FP_ID;
	
	@ManyToOne() 
	@JoinColumn(name="MP_ID")
	private MobilePhone MP_ID;
	
	@ManyToOne() 
	@JoinColumn(name="FI_ID")
	private FixedInternet FI_ID;
	
	@ManyToOne() 
	@JoinColumn(name="MI_ID")
	private MobileInternet MI_ID;
	
	@ManyToMany(mappedBy="servPackList", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST) //qui giusto
	private List<OptionalProduct> optProdList;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
			name="LastingPackage", 
			joinColumns = @JoinColumn(name = "ServPack_ID"), 
			inverseJoinColumns = @JoinColumn(name="ValPeriod_ID"))
	private List<ValidityPeriod> valPeriodList;
	
	public ServicePackage() {}

	public ServicePackage(String name) {
		Name = name;
	}
	
	public ServicePackage(String name, FixedPhone fP_ID, MobilePhone mP_ID, FixedInternet fI_ID, MobileInternet mI_ID) {
		Name = name;
		FP_ID = fP_ID;
		MP_ID = mP_ID;
		FI_ID = fI_ID;
		MI_ID = mI_ID;
	}
	
	public ServicePackage(String name, FixedPhone fP_ID, MobilePhone mP_ID, FixedInternet fI_ID, MobileInternet mI_ID,
			List<OptionalProduct> optProdList, List<ValidityPeriod> valPeriodList) {
		Name = name;
		FP_ID = fP_ID;
		MP_ID = mP_ID;
		FI_ID = fI_ID;
		MI_ID = mI_ID;
		this.optProdList = optProdList;
		this.valPeriodList = valPeriodList;
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

	public List<Order> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<Order> orderList) {
		this.orderList = orderList;
	}

	public FixedPhone getFP_ID() {
		return FP_ID;
	}

	public void setFP_ID(FixedPhone fP_ID) {
		FP_ID = fP_ID;
		fP_ID.addServPack(this);
		
	}

	public MobilePhone getMP_ID() {
		return MP_ID;
	}

	public void setMP_ID(MobilePhone mP_ID) {
		MP_ID = mP_ID;
		mP_ID.addServPack(this);
	}

	public FixedInternet getFI_ID() {
		return FI_ID;
	}

	public void setFI_ID(FixedInternet fI_ID) {
		FI_ID = fI_ID;
		fI_ID.addServPack(this);
	}

	public MobileInternet getMI_ID() {
		return MI_ID;
	}

	public void setMI_ID(MobileInternet mI_ID) {
		MI_ID = mI_ID;
		mI_ID.addServPack(this);
	}

	public List<OptionalProduct> getOptProdList() {
		return optProdList;
	}

	public void setOptProdList(List<OptionalProduct> optProdList) {
		this.optProdList = optProdList;
	}

	public List<ValidityPeriod> getValPeriodList() {
		return valPeriodList;
	}

	public void setValPeriodList(List<ValidityPeriod> valPeriodList) {
		this.valPeriodList = valPeriodList;
	}
	
	public void addValPeriod(ValidityPeriod valPeriod) {
		if(getValPeriodList()==null)
			valPeriodList = new ArrayList<>();
		getValPeriodList().add(valPeriod);
		valPeriod.addServPack(this);
	}
	
	public void removeValPeriod(ValidityPeriod valPeriod) {
		getValPeriodList().remove(valPeriod);
		valPeriod.removeServPack(this);
	}
	
	public void addOptProd(OptionalProduct optProd) {
		if(getOptProdList()==null)
			optProdList = new ArrayList<>();
		getOptProdList().add(optProd);
		optProd.addServPack(this);
	}
	
	public void removeOptProd(OptionalProduct optProd) {
		getOptProdList().remove(optProd);
		optProd.removeServPack(this);
	}
}
