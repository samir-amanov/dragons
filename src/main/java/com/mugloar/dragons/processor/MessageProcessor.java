package com.mugloar.dragons.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

import com.mugloar.dragons.model.Message;

@Service
public class MessageProcessor {

	private static final String ESC = "Escort";
	private static final String DEF = "Defending";
	private static final String TOP = "NoMessages";
	private static final String LEVEL_SURE = "Sure thing";
	private static final String LEVEL_CAKE = "Piece of cake";
	private static final String LEVEL_PARK = "Walk in the park";
	private static final String LEVEL_QUITE = "Quite likely";
	private static final String LEVEL_RISKY = "Risky";
	private static final String LEVEL_FIRE = "Playing with fire";
	private static final String LEVEL_DET = "Rather detrimental";
	private static final String LEVEL_GAMBLE = "Gamble";

	public List<String> findAdIds(Message[] messages, Set<String> seenIds, int gold, String specialMission) {
		if (specialMission.equals(DEF)) {
			List<Message> messageList = defendingLevelId(messages);
			return getAdIds(messageList, seenIds);
		} else if (specialMission.equals(ESC)) {
			List<Message> messageList = escortLevelId(messages);
			return getAdIds(messageList, seenIds);
		} else if (specialMission.equals(TOP)) {
			List<Message> messageList = noMessageLevelIds(messages);
			return getAdIds(messageList, seenIds);
		} else if (gold <= 50) {
			List<Message> messageList = firstLevelIds(messages);
			return getAdIds(messageList, seenIds);
		} else if (gold > 50) {
			List<Message> messageList = secondLevelIds(messages);
			return getAdIds(messageList, seenIds);
		} else if (gold > 150) {
			return getAdIds(Arrays.asList(messages), seenIds);
		}
		return new ArrayList<>();
	}

	public List<Message> firstLevelIds(Message[] messages) {
		return Arrays.asList(messages).stream()
				.filter(message -> message.getProbability().equals(LEVEL_SURE)
						|| message.getProbability().equals(LEVEL_CAKE)
						|| message.getProbability().equals(LEVEL_PARK))
				.toList();
	}

	public List<Message> secondLevelIds(Message[] messages) {
		return Arrays.asList(messages).stream()
				.filter(message -> message.getProbability().equals(LEVEL_SURE)
						|| message.getProbability().equals(LEVEL_CAKE)
						|| message.getProbability().equals(LEVEL_PARK)
						|| message.getProbability().equals(LEVEL_QUITE)
						|| message.getProbability().equals(LEVEL_RISKY))
				.toList();
	}

	public List<Message> noMessageLevelIds(Message[] messages) {
		return Arrays.asList(messages).stream()
				.filter(message -> message.getProbability().equals(LEVEL_FIRE)
						|| message.getProbability().equals(LEVEL_DET)
						|| message.getProbability().equals(LEVEL_GAMBLE))
				.max(Comparator.comparingInt(Message::getReward)).stream()
				.toList();
	}

	public List<Message> defendingLevelId(Message[] messages) {
		return Arrays.asList(messages).stream().filter(message -> message.getMessage().contains(DEF))
				.max(Comparator.comparingInt(Message::getReward)).stream().toList();
	}

	public List<Message> escortLevelId(Message[] messages) {
		return Arrays.asList(messages).stream().filter(message -> message.getMessage().contains(ESC))
				.max(Comparator.comparingInt(Message::getReward)).stream().toList();
	}

	public List<String> getAdIds(List<Message> messageList, Set<String> seenIds) {
		List<String> adIds = new ArrayList<>();
		Collections.sort(new ArrayList<>(messageList), Comparator.comparing(Message::getReward).reversed());
		for (Message message : messageList) {
			String adId = message.getAdId();
			if (!seenIds.contains(adId)) {
				adIds.add(adId);
			}
		}
		return adIds;
	}
}