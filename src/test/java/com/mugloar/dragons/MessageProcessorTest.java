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
				new Message("Ad1", "Defending", 20, "Sure thing"),
				new Message("Ad2", "Escort", 40, "Piece of cake"),
				new Message("Ad3", "", 60, "Quite likely"),
				new Message("Ad4", "Help", 80, "Walk in the park"),
				new Message("Ad5", "Help", 100, "Gamble"),
				new Message("Ad6", "Defending", 120, "Risky")
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
