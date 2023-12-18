package com.mugloar.dragons;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mugloar.dragons.processor.MessageProcessor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mugloar.dragons.model.Message;

public class MessageProcessorTest {

	private MessageProcessor messageProcessor;

	@BeforeEach
	public void setUp() {
		messageProcessor = new MessageProcessor();
	}

	@Test
	public void testFindAdIds() {
		Message[] messages = new Message[]{
				new Message("Ad1", "Sure thing", 20, "Defending"),
				new Message("Ad2", "Piece of cake", 40, "Escort"),
				new Message("Ad3", "Quite likely", 60, ""),
				new Message("Ad4", "Walk in the park", 80, "Help"),
				new Message("Ad5", "Gamble", 100, "Help"),
				new Message("Ad6", "Risky", 120, "Defending")
		};

		Set<String> seenIds = new HashSet<>(Arrays.asList("Ad2", "Ad4"));

		List<String> result1 = messageProcessor.findAdIds(messages, seenIds, 40, "");
		List<String> result2 = messageProcessor.findAdIds(messages, seenIds, 60, "");
		List<String> result3 = messageProcessor.findAdIds(messages, seenIds, 200, "Defending");

		assertEquals(Arrays.asList("Ad1"), result1);
		assertEquals(Arrays.asList("Ad1", "Ad3", "Ad6"), result2);
		assertEquals(Arrays.asList("Ad6"), result3);
	}
}
