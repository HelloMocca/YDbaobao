package com.ydbaobao.model;

public class AdminConfig {
	private int adminConfigId;
	private int adminDisplayProducts;
	private String adminPassword;
	private int adminCostPerWeight;
	
	public AdminConfig(){};
	
	public AdminConfig(int adminConfigId, int adminDisplayProducts, String adminPassword, int adminCostPerWeight) {
		this.adminConfigId = adminConfigId;
		this.adminDisplayProducts = adminDisplayProducts;
		this.adminPassword = adminPassword;
		this.adminCostPerWeight = adminCostPerWeight;
	}

	public int getAdminConfigId() {
		return adminConfigId;
	}

	public void setAdminConfigId(int adminConfigId) {
		this.adminConfigId = adminConfigId;
	}

	public int getAdminDisplayProducts() {
		return adminDisplayProducts;
	}

	public void setAdminDisplayProducts(int adminDisplayProducts) {
		this.adminDisplayProducts = adminDisplayProducts;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}
	
	public int getAdminCostPerWeight() {
		return adminCostPerWeight;
	}

	public void setAdminCostPerWeight(int adminCostPerWeight) {
		this.adminCostPerWeight = adminCostPerWeight;
	}

	@Override
	public String toString() {
		return "AdminConfig [adminConfigId=" + adminConfigId + ", adminDisplayProducts=" + adminDisplayProducts
				+ ", adminPassword=" + adminPassword + ", adminCostPerWeight=" + adminCostPerWeight + "]";
	}

}
