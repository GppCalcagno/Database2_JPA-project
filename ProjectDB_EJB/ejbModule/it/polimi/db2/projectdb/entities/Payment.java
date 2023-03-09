package it.polimi.db2.projectdb.entities;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "Payment", schema = "db_telco")
public class Payment {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int ID;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date DatePayment;
	
	private String Status;
	
	@ManyToOne
	@JoinColumn(name="user_ID")
	private User user_ID;
	
	@ManyToOne
	@JoinColumn(name="Order_ID")
	private Order Order_ID;
	
	public Payment() {};
	
	public Payment(Order order) {
		this.Order_ID=order;
		this.user_ID=order.getUser_ID();
		
		this.DatePayment= new Date();//initialized with the current date	
		Random rand = new Random();
		int result= rand.nextInt(2);
		//if(true) {
		if(result==1) {
			Status="ACCEPTED";
			order.setStatus(true);
		}
		else
		{
			Status="REJECTED";
			order.setStatus(false);
		}
		order.addPayment(this);
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public Date getDatePayment() {
		return DatePayment;
	}

	public void setDatePayment(Date datePayment) {
		DatePayment = datePayment;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public User getUser_ID() {
		return user_ID;
	}

	public void setUser_ID(User user_ID) {
		this.user_ID = user_ID;
	}

	public Order getOrder_ID() {
		return Order_ID;
	}

	public void setOrder_ID(Order order_ID) {
		Order_ID = order_ID;
	}
}
