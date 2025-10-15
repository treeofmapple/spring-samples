package com.tom.auth.monolithic.user.mapper;

import java.time.ZonedDateTime;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import com.tom.auth.monolithic.user.model.User;
import com.tom.auth.monolithic.user.repository.LoginHistoryRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MappingUtils {

    private final LoginHistoryRepository loginHistoryRepository;

    @Named("getLastLoginTime")
    public ZonedDateTime getLastLoginTime(User user) {
        if (user == null || user.getId() == null) {
            return null;
        }
        return loginHistoryRepository.findFirstByUserIdOrderByLoginTimeDesc(user.getId())
                .map(loginHistory -> loginHistory.getLoginTime())
                .orElse(null);
    }
	
}
