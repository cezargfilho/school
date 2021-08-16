package br.com.alura.school.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserReportResponse {
	
	@JsonProperty
	private final String email;
	
	@JsonProperty(value = "quantidade_matriculas")
	private final int amount;
	
	public UserReportResponse(String email, long amount) {
		this.email = email;
		this.amount = (int) amount;
	}

}
