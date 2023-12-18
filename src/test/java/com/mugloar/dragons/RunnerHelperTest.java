package com.mugloar.dragons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.mugloar.dragons.runner.RunnerHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.mugloar.dragons.controller.DragonsOfMugloarController;
import com.mugloar.dragons.model.Message;
import com.mugloar.dragons.model.PurchaseItemResult;

public class RunnerHelperTest {

	private RunnerHelper runnerHelper;
	private DragonsOfMugloarController mockController;

	@BeforeEach
	public void setUp() {
		runnerHelper = new RunnerHelper();
		mockController = mock(DragonsOfMugloarController.class);
	}

	@Test
	public void testDecimalFormat() {
		int num1 = 13;
		int num2 = 15;
		String expectedFormattedResult = "86.67";
		String actualFormattedResult = runnerHelper.decimalFormatter(num1, num2);
		assertEquals(expectedFormattedResult, actualFormattedResult);
	}
	@Test
	public void testSpecialMissionDefending() {
		Message[] messages = new Message[]{
				new Message("Ad1", "Sure thing", 20, "Defending"),
				new Message("Ad2", "Piece of cake", 40, "Escort"),
				new Message("Ad3", "Quite likely", 60, ""),
				new Message("Ad4", "Walk in the park", 80, "Help"),
				new Message("Ad5", "Gamble", 100, "Help"),
				new Message("Ad6", "Risky", 120, "Defending")
		};
		ResponseEntity<PurchaseItemResult> purchaseItemResult = mockPurchaseItemResult(true);

		when(mockController.purchaseItem("gameId", "wingpot")).thenReturn(purchaseItemResult);
		String result = runnerHelper.specialMission(mockController, messages, 2, 200, "gameId");
		assertEquals("Defending", result);
	}

	@Test
	public void testSpecialMissionEscort() {
		Message[] messages = new Message[]{
				new Message("Ad2", "Piece of cake", 40, "Escort"),
				new Message("Ad3", "Quite likely", 60, ""),
				new Message("Ad4", "Walk in the park", 80, "Help"),
				new Message("Ad5", "Gamble", 100, "Help"),
				new Message("Ad6", "Risky", 120, "Escort")
		};
		ResponseEntity<PurchaseItemResult> purchaseItemResult = mockPurchaseItemResult(true);

		when(mockController.purchaseItem("gameId", "rf")).thenReturn(purchaseItemResult);
		String result = runnerHelper.specialMission(mockController, messages, 2, 400, "gameId");
		assertEquals("Escort", result);
	}

	@Test
	public void testSpecialMissionNoMission() {
		Message[] messages = new Message[]{
				new Message("Ad1", "Sure thing", 100, "Steal"),
				new Message("Ad3", "Quite likely", 60, ""),
				new Message("Ad4", "Walk in the park", 80, "Help"),
				new Message("Ad5", "Gamble", 100, "Help")
		};

		String result = runnerHelper.specialMission(mockController, messages, 2, 200, "gameId");
		assertEquals("", result);
	}

	@Test
	public void testPurchaseLives() {
		ResponseEntity<PurchaseItemResult> purchaseItemResult = mockPurchaseItemResult(true);
		when(mockController.purchaseItem("gameId", "hpot")).thenReturn(purchaseItemResult);
		int result = runnerHelper.purchaseLives(mockController, 2, 60, "gameId");
		assertEquals(4, result); // assuming that it will purchase 2 lives
	}

	private ResponseEntity<PurchaseItemResult> mockPurchaseItemResult(boolean shoppingSuccess) {
		PurchaseItemResult result = new PurchaseItemResult();
		result.setShoppingSuccess(String.valueOf(shoppingSuccess));
		result.setLives(4);
		return ResponseEntity.ok(result);
	}
}
