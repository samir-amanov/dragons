package com.mugloar.dragons.controller;

import com.mugloar.dragons.model.PurchaseItemResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mugloar.dragons.model.Message;
import com.mugloar.dragons.model.SolvingResult;
import com.mugloar.dragons.service.DragonsOfMugloarService;

@RestController
@RequestMapping("/api/v1")
public class DragonsOfMugloarController {

	private final DragonsOfMugloarService dragonsOfMugloarService;

	public DragonsOfMugloarController(DragonsOfMugloarService dragonsOfMugloarService) {
		this.dragonsOfMugloarService = dragonsOfMugloarService;
	}

	@RequestMapping("/start")
	public ResponseEntity<String> startGame() {
		String gameId = dragonsOfMugloarService.startGame();
		return ResponseEntity.ok(gameId);
	}

	@RequestMapping("/messages")
	public ResponseEntity<Message[]> getMessages(String gameId) {
		Message[] messages = dragonsOfMugloarService.getMessages(gameId);
		if (messages != null) {
			return ResponseEntity.ok(messages);
		} else {
			return null;
		}
	}

	@RequestMapping("/solve")
	public ResponseEntity<SolvingResult> solveMessage(String gameId, String adId) {
		SolvingResult solvingResult = dragonsOfMugloarService.solveMessageWithRetry(gameId, adId);
		if (solvingResult != null) {
			return ResponseEntity.ok(solvingResult);
		} else {
			return null;
		}
	}

	@RequestMapping("/purchase")
	public ResponseEntity<PurchaseItemResult> purchaseItem(String gameId, String itemId) {
		PurchaseItemResult purchaseItemResult = dragonsOfMugloarService.purchaseItem(gameId, itemId);
		if (purchaseItemResult != null) {
			return ResponseEntity.ok(purchaseItemResult);
		} else {
			return null;
		}
	}
}
