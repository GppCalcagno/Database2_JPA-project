package it.polimi.db2.projectdb.entities;

import java.io.Serializable;

import javax.persistence.Id;

public class SalesReport2_Key implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int ServPack_ID;
	
	@Id
	private int ValPeriod_ID;
	
    public int hashCode() {
    	Integer hash = ValPeriod_ID;
    	return hash.hashCode();
    	
    }
    
    public boolean equals(Object o) {
        return ((o instanceof SalesReport2_Key) && 
        		ServPack_ID ==((SalesReport2_Key) o).getServPack_ID() && 
        		ValPeriod_ID == ((SalesReport2_Key) o).getValPeriod_ID());

    }

	public int getServPack_ID() {
		return ServPack_ID;
	}

	public void setServPack_ID(int servPack_ID) {
		ServPack_ID = servPack_ID;
	}

	public int getValPeriod_ID() {
		return ValPeriod_ID;
	}

	public void setValPeriod_ID(int valPeriod_ID) {
		ValPeriod_ID = valPeriod_ID;
	}
    
    

}
