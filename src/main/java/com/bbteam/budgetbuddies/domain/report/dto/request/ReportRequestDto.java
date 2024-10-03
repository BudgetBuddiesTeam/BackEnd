package com.bbteam.budgetbuddies.domain.report.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ReportRequestDto {
	@NotBlank
	private String reason;
}
