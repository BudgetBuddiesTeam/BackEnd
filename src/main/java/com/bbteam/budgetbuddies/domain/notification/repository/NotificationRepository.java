package com.bbteam.budgetbuddies.domain.notification.repository;

import com.bbteam.budgetbuddies.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {


}
