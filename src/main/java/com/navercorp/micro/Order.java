package com.navercorp.micro;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class Order implements Serializable {
	
	 private static final long serialVersionUID = 1L;
	
	private String posNo;

	public String getPosNo() {
		return posNo;
	}

	public void setPosNo(String posNo) {
		this.posNo = posNo;
	}

}
