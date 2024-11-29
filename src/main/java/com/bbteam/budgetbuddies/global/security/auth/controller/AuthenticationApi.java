package com.bbteam.budgetbuddies.global.security.auth.controller;

import com.bbteam.budgetbuddies.apiPayload.ApiResponse;
import com.bbteam.budgetbuddies.global.security.auth.dto.AuthenticationRequest;
import com.bbteam.budgetbuddies.global.security.auth.dto.AuthenticationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthenticationApi {

    @Operation(summary = "OTP 요청 API", description = "전화번호를 입력받아 해당 번호로 OTP를 발송하고, 발송된 OTP 정보를 반환합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH4001", description = "전화번호 형식이 유효하지 않습니다. (예: 01012341234)", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<AuthenticationResponse.SendOtpNumber> getOtpNumber(
        @RequestBody AuthenticationRequest.ToReceiveNumber request
    );

    @Operation(summary = "로그인 API", description = "전화번호와 OTP를 받아 로그인 처리 후, 인증 토큰을 반환합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "OTP4001", description = "인증번호가 유효하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH4001", description = "전화번호 형식이 유효하지 않습니다. (예: 01012341234)", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<AuthenticationResponse.SendTokens> login(
        @RequestBody AuthenticationRequest.ToLogin request
    );

    @Operation(summary = "액세스 토큰 재발급 API", description = "현재 인증된 사용자로부터 새로운 액세스 토큰을 발급받습니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH4001", description = "전화번호 형식이 유효하지 않습니다. (예: 01012341234)", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TOKEN4001", description = "토큰이 유효하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TOKEN4002", description = "토큰이 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TOKEN4003", description = "토큰이 만료되었습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TOKEN4004", description = "토큰의 페이로드 혹은 시그니처가 유효하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TOKEN4005", description = "토큰 헤더가 유효하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<AuthenticationResponse.SendAccessToken> reIssueAccessToken();
}
