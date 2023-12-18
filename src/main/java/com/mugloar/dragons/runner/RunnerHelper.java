package com.mugloar.dragons.runner;

import com.mugloar.dragons.controller.DragonsOfMugloarController;
import com.mugloar.dragons.model.Message;
import com.mugloar.dragons.model.PurchaseItemResult;
import java.text.DecimalFormat;
import java.util.Arrays;
import org.springframework.http.ResponseEntity;

public class RunnerHelper {

	private static final String CLASSIC = "Classic";
	private static final String ESC = "Escort";
	private static final String DEF = "Defending";
	private static final String LIVES = "hpot";
	private static final String WINGS = "wingpot";
	private static final String FUEL = "rf";
	private static final String FALSE_RESULT = "false";

	public String specialMission(DragonsOfMugloarController dragonsOfMugloarController, Message[] messages, int lives,
			int gold, String gameId) {
		if (lives >= 2 && gold >= 200 && !Arrays.asList(messages).stream()
				.filter(message -> message.getMessage().contains(DEF)).toList().isEmpty()) {
			ResponseEntity<PurchaseItemResult> purchaseItemResult =
					dragonsOfMugloarController.purchaseItem(gameId, WINGS);
			if (purchaseItemResult == null || purchaseItemResult.getBody() == null) {
				return CLASSIC;
			} else if (purchaseItemResult.getBody().getShoppingSuccess().equals(FALSE_RESULT)) {
				return CLASSIC;
			}
			return DEF;
		} else if (lives >= 2 && gold >= 400 && !Arrays.asList(messages).stream()
				.filter(message -> message.getMessage().contains(ESC)).toList().isEmpty()) {
			ResponseEntity<PurchaseItemResult> purchaseItemResult =
					dragonsOfMugloarController.purchaseItem(gameId, FUEL);
			if (purchaseItemResult == null || purchaseItemResult.getBody() == null) {
				return CLASSIC;
			} else if (purchaseItemResult.getBody().getShoppingSuccess().equals(FALSE_RESULT)) {
				return CLASSIC;
			}
			return ESC;
		}
		return CLASSIC;
	}

	public int purchaseLives(DragonsOfMugloarController dragonsOfMugloarController, int lives, int gold,
			String gameId) {
		if (lives <= 2 && gold >= 50) {
			ResponseEntity<PurchaseItemResult> purchaseItemResult = null;
			for (int i = 0; i < 2; i++) {
				purchaseItemResult = dragonsOfMugloarController.purchaseItem(gameId, LIVES);
			}
			if (purchaseItemResult == null || purchaseItemResult.getBody() == null) {
				return 0;
			}
			if (purchaseItemResult.getBody().getShoppingSuccess().equals(FALSE_RESULT)) {
				dragonsOfMugloarController.purchaseItem(gameId, LIVES);
			} else {
				lives = purchaseItemResult.getBody().getLives();
			}
		}
		return lives;
	}

	public String decimalFormatter(int num1, int num2) {
		double result = ((double) num1 / num2) * 100;
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(result);
	}
}
