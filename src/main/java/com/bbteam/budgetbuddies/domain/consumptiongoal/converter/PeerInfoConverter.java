package com.bbteam.budgetbuddies.domain.consumptiongoal.converter;

import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.PeerInfoResponseDto;
import com.bbteam.budgetbuddies.enums.Gender;

public class PeerInfoConverter {

	public static PeerInfoResponseDto fromEntity(int peerAgeStart, int peerAgeEnd, Gender peerGender) {

		return PeerInfoResponseDto.builder()
			.peerAgeStart(peerAgeStart)
			.peerAgeEnd(peerAgeEnd)
			.peerGender(peerGender.name())
			.build();
	}
}
