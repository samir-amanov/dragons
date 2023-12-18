package com.mugloar.dragons.service;

import com.mugloar.dragons.model.PurchaseItemResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.mugloar.dragons.model.Message;
import com.mugloar.dragons.model.SolvingResult;
import com.mugloar.dragons.model.StartResponse;

@Service
public class DragonsOfMugloarService {

	private static final String BASE_URL = "https://dragonsofmugloar.com/api/v2/";
	private final RestTemplate restTemplate;
	private static final Logger fileLogger = LoggerFactory.getLogger("general");

	public DragonsOfMugloarService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public String startGame() {
		String startUrl = BASE_URL + "game/start";
		return restTemplate.postForObject(startUrl, null, StartResponse.class).getGameId();
	}

	public Message[] getMessages(String gameId) {
		String messagesUrl = BASE_URL + gameId + "/messages";
		try {
			return restTemplate.getForObject(messagesUrl, Message[].class);
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			fileLogger.error("Massages not found: ", e);
			return null;
		}
	}

	public SolvingResult solveMessageWithRetry(String gameId, String adId) {
		String solveUrl = BASE_URL + gameId + "/solve/" + adId;
		int maxRetries = 1;

		for (int retry = 0; retry <= maxRetries; retry++) {
			try {
				ResponseEntity<SolvingResult> responseEntity =
						restTemplate.postForEntity(solveUrl, null, SolvingResult.class);
				return responseEntity.getBody();
			} catch (HttpClientErrorException | HttpServerErrorException e) {
				if (retry < maxRetries) {
					continue;
				} else {
					fileLogger.error("It is not possible to solve an ad: ", e);
					return null;
				}
			}
		}
		return null;
	}

	public PurchaseItemResult purchaseItem(String gameId, String itemId) {
		String solveUrl = BASE_URL + gameId + "/shop/buy/" + itemId;
		try {
			return restTemplate.postForObject(solveUrl, null, PurchaseItemResult.class);
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			fileLogger.error("It is not possible to buy an item: ", e);
			return null;
		}
	}
}
