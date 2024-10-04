package com.bbteam.budgetbuddies.domain.report.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ReportRequestDto {
	@NotBlank
	private String reason;
}
