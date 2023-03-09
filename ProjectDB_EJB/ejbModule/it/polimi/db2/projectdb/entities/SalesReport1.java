package it.polimi.db2.projectdb.entities;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "SalesReport1", schema = "db_telco")
@NamedQuery(name="SalesReport1.findAll", query="SELECT o FROM SalesReport1 o")
public class SalesReport1 {

	@Id
	private int ServPack_ID;

	private int NumSales_SP;
	
	private int NumSales_W;
	
	private int NumSales_WO;
	
	private BigDecimal AvgSales_OPT;
	
	private int NumSales_OPT;

	public int getServPack_ID() {
		return ServPack_ID;
	}

	public int getNumSales_SP() {
		return NumSales_SP;
	}

	public int getNumSales_W() {
		return NumSales_W;
	}

	public int getNumSales_WO() {
		return NumSales_WO;
	}

	public BigDecimal getAvgSales_OPT() {
		return AvgSales_OPT;
	}

	public int getNumSales_OPT() {
		return NumSales_OPT;
	} 
	
	
	
}
