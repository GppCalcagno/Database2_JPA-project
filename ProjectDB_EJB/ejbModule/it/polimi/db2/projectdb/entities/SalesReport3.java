package it.polimi.db2.projectdb.entities;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "SalesReport3", schema = "db_telco")
@NamedQuery(name="SalesReport3.findAll", query="SELECT o FROM SalesReport3 o  ORDER BY o.NumSales DESC")
public class SalesReport3 {

	@Id
	private int OptProd_ID;
	
	private int NumSales;

	public int getOptProd_ID() {
		return OptProd_ID;
	}

	public int getNumSales() {
		return NumSales;
	}
}