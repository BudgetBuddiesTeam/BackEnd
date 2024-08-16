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
public class UserController implements UserApi{

    private final UserService userService;

    @PostMapping("/{userId}/add/default-categories/consumption-goal")
    public ResponseEntity<List<UserConsumptionGoalResponse>> createConsumptionGoals(@PathVariable Long userId) {
        List<UserConsumptionGoalResponse> consumptionGoals = userService.createConsumptionGoalWithDefaultGoals(userId);
        return ResponseEntity.ok(consumptionGoals);
    }

    @PostMapping("/register")
    public ApiResponse<UserDto.ResponseUserDto> registerUser(@RequestBody UserDto.RegisterUserDto dto) {
        return ApiResponse.onSuccess(userService.saveUser(dto));
    }

    @GetMapping("/find/{userid}")
    public ApiResponse<UserDto.ResponseUserDto> findOne(@PathVariable("userId") @ExistUser Long userId) {
        return ApiResponse.onSuccess(userService.findUser(userId));
    }

    @Override
    @GetMapping("/findAll")
    public ApiResponse<List<UserDto.ResponseUserDto>> findAll() {
        return ApiResponse.onSuccess(userService.findAll());
    }

    @PutMapping("/modify/{userId}")
    public ApiResponse<UserDto.ResponseUserDto> changeOne(@PathVariable("userId") @ExistUser Long userId,
                                                          @RequestBody UserDto.ModifyUserDto dto) {
        return ApiResponse.onSuccess(userService.modifyUser(userId, dto));
    }
}
