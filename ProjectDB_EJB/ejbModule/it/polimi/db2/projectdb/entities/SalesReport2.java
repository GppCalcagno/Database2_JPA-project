package it.polimi.db2.projectdb.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@IdClass(SalesReport2_Key.class)
@Table(name = "SalesReport2", schema = "db_telco")
@NamedQuery(name="SalesReport2.findAll", query="SELECT o FROM SalesReport2 o")
public class SalesReport2 {
	
	@Id
	private int ServPack_ID;
	
	@Id
	private int ValPeriod_ID;
	
	private int NumSales;

	public int getServPack_ID() {
		return ServPack_ID;
	}

	public int getValPeriod_ID() {
		return ValPeriod_ID;
	}

	public int getNumSales() {
		return NumSales;
	}
	
	

}
