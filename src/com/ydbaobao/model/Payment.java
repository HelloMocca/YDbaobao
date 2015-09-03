package com.ydbaobao.model;

public class Payment {
	private int paymentId;
	private Customer customer;
	private String paymentType; // 'I':납입금액 'C':반품금액 'P':구매금액
	private int amount;
	private String paymentDate;
	
	public Payment(int paymentId, Customer customer, String paymentType, int amount, String paymentDate) {
		this.paymentId = paymentId;
		this.customer = customer;
		this.paymentType = paymentType;
		this.amount = amount;
		this.paymentDate = paymentDate;
	}

	public Payment(Customer customer, String paymentType, int amount, String paymentDate) {
		this(0, customer, paymentType, amount, paymentDate);
	}

	public Payment(Customer customer, String paymentType, int amount) {
		this(customer, paymentType, amount, null);
	}
	
	public Payment(int paymentId, String paymentType, int amount) {
		this(paymentId, null, paymentType, amount, null);
	}

	public int getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + amount;
		result = prime * result
				+ ((customer == null) ? 0 : customer.hashCode());
		result = prime * result
				+ ((paymentDate == null) ? 0 : paymentDate.hashCode());
		result = prime * result + paymentId;
		result = prime * result
				+ ((paymentType == null) ? 0 : paymentType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Payment other = (Payment) obj;
		if (amount != other.amount)
			return false;
		if (customer == null) {
			if (other.customer != null)
				return false;
		} else if (!customer.equals(other.customer))
			return false;
		if (paymentDate == null) {
			if (other.paymentDate != null)
				return false;
		} else if (!paymentDate.equals(other.paymentDate))
			return false;
		if (paymentId != other.paymentId)
			return false;
		if (paymentType == null) {
			if (other.paymentType != null)
				return false;
		} else if (!paymentType.equals(other.paymentType))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Payment [paymentId=" + paymentId + ", customer=" + customer
				+ ", paymentType=" + paymentType + ", amount=" + amount
				+ ", paymentDate=" + paymentDate + "]";
	}
	
}
