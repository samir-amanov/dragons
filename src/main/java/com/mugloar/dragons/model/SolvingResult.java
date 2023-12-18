package com.mugloar.dragons.model;

import lombok.Getter;

@Getter
public class SolvingResult {
	private boolean success;
	private int lives;
	private int gold;
	private int score;
	private int highScore;
	private int turn;
	private String message;
}
