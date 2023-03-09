package it.polimi.db2.projectdb.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "Alert", schema = "db_telco")
@NamedQuery(name="Alert.findAll", query="SELECT a FROM Alert a")
public class Alert {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int ID;
	
	private String User_Username;

	private String User_Email;
	
	private BigDecimal Order_TotalValue;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date Payment_Date;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getUser_Username() {
		return User_Username;
	}

	public void setUser_Username(String user_Username) {
		User_Username = user_Username;
	}

	public String getUser_Email() {
		return User_Email;
	}

	public void setUser_Email(String user_Email) {
		User_Email = user_Email;
	}

	public BigDecimal getOrder_TotalValue() {
		return Order_TotalValue;
	}

	public void setOrder_TotalValue(BigDecimal order_TotalValue) {
		Order_TotalValue = order_TotalValue;
	}

	public Date getPayment_Date() {
		return Payment_Date;
	}

	public void setPayment_Date(Date payment_Date) {
		Payment_Date = payment_Date;
	}
}
