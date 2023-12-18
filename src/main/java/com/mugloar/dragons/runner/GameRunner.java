package com.mugloar.dragons.runner;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import java.util.Set;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.mugloar.dragons.controller.DragonsOfMugloarController;
import com.mugloar.dragons.model.Message;
import com.mugloar.dragons.model.SolvingResult;
import com.mugloar.dragons.processor.MessageProcessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class GameRunner {

	private final ConfigurableApplicationContext context;
	private final DragonsOfMugloarController dragonsOfMugloarController;
	private final MessageProcessor messageProcessor;
	private static final Logger fileLogger = LoggerFactory.getLogger("general");
	private static final Logger consoleLogger = LoggerFactory.getLogger("stats");

	RunnerHelper helper = new RunnerHelper();

	public GameRunner(DragonsOfMugloarController dragonsOfMugloarController, MessageProcessor messageProcessor,
			ConfigurableApplicationContext context) {
		this.dragonsOfMugloarController = dragonsOfMugloarController;
		this.messageProcessor = messageProcessor;
		this.context = context;
	}

	public void runGame() {

		System.out.print("Enter number of tries to start the game... ");
		int repeats = new Scanner(System.in).nextInt();
		int counter = 0;

		for (int i = 1; i <= repeats; i++) {
			ResponseEntity<String> startResponse = dragonsOfMugloarController.startGame();
			String gameId = startResponse.getBody();
			consoleLogger.info("### Game no: " + i);
			consoleLogger.info("### started with id: " + gameId);
			int lives = 3;
			int gold = 0;
			int score;
			boolean win = false;
			String specialMission;
			Set<String> seenIds = new HashSet<>();

			while (lives > 0 && !win) {
				ResponseEntity<Message[]> messagesResponse = dragonsOfMugloarController.getMessages(gameId);
				fileLogger.info("Messages API call for new messages ...");
				if (messagesResponse == null || messagesResponse.getBody() == null) {
					continue;
				}
				specialMission =
						helper.specialMission(dragonsOfMugloarController, messagesResponse.getBody(), lives, gold,
								gameId);
				fileLogger.info("Special Mission: " + specialMission);
				List<String> adIds =
						messageProcessor.findAdIds(messagesResponse.getBody(), seenIds, gold, specialMission);
				fileLogger.info("Number of adds found with given criteria: " + adIds.size());
				for (String adId : adIds) {
					ResponseEntity<SolvingResult> solvingResult = dragonsOfMugloarController.solveMessage(gameId, adId);
					if (solvingResult == null || solvingResult.getBody() == null) {
						continue;
					}
					seenIds.add(adId);
					lives = solvingResult.getBody().getLives();
					gold = solvingResult.getBody().getGold();
					score = solvingResult.getBody().getScore();

					int purchasedLives = helper.purchaseLives(dragonsOfMugloarController, lives, gold, gameId);
					if (purchasedLives == 0) {
						continue;
					} else {
						lives = purchasedLives;
					}
					if (score > 1000) {
						win = true;
						counter++;
						consoleLogger.info("Score: " + score);
						consoleLogger.info("Gold: " + gold);
						consoleLogger.info("Lives: " + lives);
						consoleLogger.info("Turn: " + solvingResult.getBody().getTurn());
						consoleLogger.info("VICTORY");
						break;
					}
				}
			}
			if (!win) {
				consoleLogger.info("DEFEAT");
			}
			consoleLogger.info("###");
			consoleLogger.info("###");
		}
		consoleLogger.info("Win percentage is: " + helper.decimalFormatter(counter, repeats) + "%");
		stopApplication();
	}

	private void stopApplication() {
		int exitCode = SpringApplication.exit(context, () -> 0);
		System.exit(exitCode);
	}
}
