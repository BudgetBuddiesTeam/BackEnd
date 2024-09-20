package com.bbteam.budgetbuddies.domain.notice.controller;

import com.bbteam.budgetbuddies.apiPayload.ApiResponse;
import com.bbteam.budgetbuddies.domain.notice.dto.NoticeRequestDto;
import com.bbteam.budgetbuddies.domain.notice.validation.ExistNotice;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;

public interface NoticeApi {

    @Operation(summary = "[Admin] 공지 저장 API", description = "공지를 저장하는 API입니다.",
    requestBody = @RequestBody(
            content = @Content(
                    schema = @Schema(
                            allOf = { NoticeRequestDto.class},
                            requiredProperties = {"title", "body"}
                    ),
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = {
                            @ExampleObject(name = "someExample1", value = """ 
                                        { 
                                            "title" : "공지 제목 써주시면 됩니다", 
                                            "body" : "공지 내용 써주시면 됩니다."
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
            @Parameter(name = "userId", description = "현재 데이터를 게시하는 어드민입니다. parameter"),
    }
    )
    ApiResponse<?> saveNotice(@ExistUser Long userId, NoticeRequestDto dto);

    @Operation(summary = "[Admin] 공지 전체 확인 API", description = "공지 전체를 확인하는 API입니다. 페이징 포함합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "size", description = "페이징 size입니다. parameter"),
            @Parameter(name = "page", description = "페이징 page입니다. parameter")
    }
    )
    ApiResponse<?> findAllWithPaging(Pageable pageable);

    @Operation(summary = "[Admin] 공지 확인 API", description = "공지를 확인하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "noticeId", description = "확인할 공지 id입니다. pathVariable", in = ParameterIn.PATH),
    }
    )
    ApiResponse<?> findOne(@ExistNotice Long noticeId);

    @Operation(summary = "[Admin] 공지 수정 API", description = "공지를 수정하는 API입니다.",
            requestBody = @RequestBody(
                    content = @Content(
                            schema = @Schema(
                                    allOf = { NoticeRequestDto.class},
                                    requiredProperties = {"title", "body"}
                            ),
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(name = "someExample1", value = """ 
                                        { 
                                            "title" : "공지 제목 써주시면 됩니다", 
                                            "body" : "공지 내용 써주시면 됩니다."
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
            @Parameter(name = "noticeId", description = "수정할 공지 id입니다. pathVariable", in = ParameterIn.PATH),
    }
    )
    ApiResponse<?> modifyNotice(@ExistNotice Long noticeId, NoticeRequestDto noticeRequestDto);

    @Operation(summary = "[Admin] 공지 삭제 API", description = "공지를 삭제하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "noticeId", description = "삭제할 공지 id입니다. pathVariable", in = ParameterIn.PATH),
    }
    )
    ApiResponse<?> deleteNotice(@ExistNotice Long noticeId);


}
