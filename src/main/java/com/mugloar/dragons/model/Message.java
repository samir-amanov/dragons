package com.mugloar.dragons.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Message {
	private String adId;
	private String message;
	private int reward;
	private String probability;
}
