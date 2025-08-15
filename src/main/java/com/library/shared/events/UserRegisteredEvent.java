package com.library.shared.events;

import org.springframework.modulith.events.Externalized;

/**
 * Event published when a new user registers in the system
 */
@Externalized("library.user.registered::#{#this.userId}")
public record UserRegisteredEvent(
    Long userId,
    String username,
    String email,
    String role,
    long timestamp
) {
    public UserRegisteredEvent(Long userId, String username, String email, String role) {
        this(userId, username, email, role, System.currentTimeMillis());
    }
}
