package com.tom.first.username.processes.events;

import com.tom.first.username.model.User;

public record UserCreatedEvent(User username) {

}
