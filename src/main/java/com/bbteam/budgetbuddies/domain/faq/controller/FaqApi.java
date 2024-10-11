package com.bbteam.budgetbuddies.domain.faq.controller;


import com.bbteam.budgetbuddies.apiPayload.ApiResponse;
import com.bbteam.budgetbuddies.domain.faq.dto.FaqRequestDto;
import com.bbteam.budgetbuddies.domain.faq.validation.ExistFaq;
import com.bbteam.budgetbuddies.domain.user.validation.ExistUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface FaqApi {
    @Operation(summary = "[User] FAQ 게시 API", description = "FAQ를 게시하는 API입니다.",
            requestBody = @RequestBody(
                    content = @Content(
                            schema = @Schema(
                                    allOf = { FaqRequestDto.FaqPostRequest.class},
                                    requiredProperties = {"title", "body"}
                            ),
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(name = "someExample1", value = """ 
                                        { 
                                            "title" : "FAQ 제목 써주시면 됩니다", 
                                            "body" : "FAQ 내용 써주시면 됩니다."
                                        } 
                                    """)
                            }
                    )
            )
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "userId", description = "FAQ를 사용하는 userId입니다.. pathVariable", in = ParameterIn.QUERY),
    }
    )
    ApiResponse<?> postFaq(@ExistUser @RequestParam Long userId,
                           @Valid @org.springframework.web.bind.annotation.RequestBody FaqRequestDto.FaqPostRequest dto);
    @Operation(summary = "[Admin] FAQ 조회 API", description = "FAQ를 조회하는 API입니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "faqId", description = "조회할 공지 id입니다. pathVariable", in = ParameterIn.PATH),
    }
    )
    ApiResponse<?> findFaq(@ExistFaq Long FaqId);
    @Operation(summary = "[Admin] FAQ 다건 조회 API", description = "FAQ를 페이징으로 조회하는 API입니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "page", description = "조회할 page입니다. 0부터 시작합니다. pathVariable", in = ParameterIn.QUERY),
            @Parameter(name = "size", description = "조회할 페이지의 크기입니다. pathVariable", in = ParameterIn.QUERY)

    }
    )
    ApiResponse<?> findByPaging(Pageable pageable, String SearchCondition);

    @Operation(summary = "[User] FAQ 수정 API", description = "FAQ를 수정하는 API입니다.",
            requestBody = @RequestBody(
                    content = @Content(
                            schema = @Schema(
                                    allOf = { FaqRequestDto.FaqPostRequest.class},
                                    requiredProperties = {"title", "body"}
                            ),
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(name = "someExample1", value = """ 
                                        { 
                                            "title" : "FAQ 제목 써주시면 됩니다", 
                                            "body" : "FAQ 내용 써주시면 됩니다."
                                        } 
                                    """)
                            }
                    )
            )
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "faqId", description = "수정할 FAQ id입니다. pathVariable", in = ParameterIn.PATH),
    }
    )
    ApiResponse<?> modifyFaq(@PathVariable @ExistFaq Long faqId,
                             @Valid @org.springframework.web.bind.annotation.RequestBody FaqRequestDto.FaqModifyRequest dto);

    @Operation(summary = "[Admin] FAQ 삭제 API", description = "FAQ를 삭제하는 API입니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "faqId", description = "삭제할 faqId입니다.. pathVariable", in = ParameterIn.PATH),

    }
    )
    ApiResponse<?> deleteFaq(@ExistFaq Long faqId);
}
