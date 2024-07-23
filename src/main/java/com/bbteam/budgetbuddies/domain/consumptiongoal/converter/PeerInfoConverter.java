package com.bbteam.budgetbuddies.domain.consumptiongoal.converter;

import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.PeerInfoResponseDTO;
import com.bbteam.budgetbuddies.enums.Gender;

public class PeerInfoConverter {

	public static PeerInfoResponseDTO fromEntity(int peerAgeStart, int peerAgeEnd, Gender peerGender) {

		return PeerInfoResponseDTO.builder()
			.peerAgeStart(peerAgeStart)
			.peerAgeEnd(peerAgeEnd)
			.peerGender(peerGender.name())
			.build();
	}
}
