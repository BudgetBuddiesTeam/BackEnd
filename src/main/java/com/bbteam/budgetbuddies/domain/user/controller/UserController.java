package com.bbteam.budgetbuddies.domain.user.controller;

import com.bbteam.budgetbuddies.apiPayload.ApiResponse;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.UserConsumptionGoalResponse;
import com.bbteam.budgetbuddies.domain.user.dto.UserDto;
import com.bbteam.budgetbuddies.domain.user.service.UserService;
import com.bbteam.budgetbuddies.domain.user.validation.ExistUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping("/{userId}/add/default-categories/consumption-goal")
    public ResponseEntity<List<UserConsumptionGoalResponse>> createConsumptionGoals(@PathVariable Long userId) {
        List<UserConsumptionGoalResponse> consumptionGoals = userService.createConsumptionGoalWithDefaultGoals(userId);
        return ResponseEntity.ok(consumptionGoals);
    }

    @PostMapping("/register")
    public ApiResponse<UserDto.ResponseDto> registerUser(@RequestBody UserDto.RegisterDto dto) {
        return ApiResponse.onSuccess(userService.saveUser(dto));
    }

    @GetMapping("/{userId}/find")
    public ApiResponse<UserDto.ResponseDto> findOne(@PathVariable("userId") @ExistUser Long userId) {
        return ApiResponse.onSuccess(userService.findUser(userId));
    }

    @PutMapping("/{userId}/change")
    public ApiResponse<UserDto.ResponseDto> changeOne(@PathVariable("userId") @ExistUser Long userId,
                                                      @RequestParam("email") String email, @RequestParam("name") String name) {
        return ApiResponse.onSuccess(userService.changeUser(userId, email, name));
    }
}
