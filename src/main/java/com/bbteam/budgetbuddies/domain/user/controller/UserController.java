package com.bbteam.budgetbuddies.domain.user.controller;

import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.UserConsumptionGoalResponse;
import com.bbteam.budgetbuddies.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/{userId}/add/default-categories/consumption-goal")
    public ResponseEntity<List<UserConsumptionGoalResponse>> createConsumptionGoals(@PathVariable Long userId) {
        List<UserConsumptionGoalResponse> consumptionGoals = userService.createConsumptionGoalWithDefaultGoals(userId);
        return ResponseEntity.ok(consumptionGoals);
    }
}
