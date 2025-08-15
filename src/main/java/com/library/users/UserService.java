package com.library.users;

import com.library.shared.events.UserRegisteredEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService, UserModuleAPI {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Transactional
    public User registerUser(UserRegistrationDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());
        user.setRole(dto.getRole() != null ? dto.getRole() : User.Role.USER);

        User savedUser = userRepository.save(user);
        
        // Publish event for other modules
        eventPublisher.publishEvent(new UserRegisteredEvent(
            savedUser.getId(), 
            savedUser.getUsername(), 
            savedUser.getEmail(),
            savedUser.getRole().name()
        ));
        
        return savedUser;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public User updateUserRole(Long userId, User.Role newRole) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setRole(newRole);
        return userRepository.save(user);
    }

    // Module API Implementation
    @Override
    public UserAnalytics getUserAnalytics() {
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.countActiveUsers();
        
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        long newUsersThisMonth = userRepository.countUsersCreatedAfter(startOfMonth);
        
        long adminCount = userRepository.countByRole(User.Role.ADMIN);
        long librarianCount = userRepository.countByRole(User.Role.LIBRARIAN);
        long userCount = userRepository.countByRole(User.Role.USER);
        
        return new UserAnalytics(
            totalUsers,
            activeUsers,
            newUsersThisMonth,
            adminCount,
            librarianCount,
            userCount
        );
    }
    
    @Override
    public UserInfo getUserInfo(Long userId) {
        return userRepository.findById(userId)
            .map(user -> new UserInfo(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getRole().name()
            ))
            .orElse(null);
    }
    
    @Override
    public UserInfo getUserInfoByUsername(String username) {
        return userRepository.findByUsername(username)
            .map(user -> new UserInfo(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getRole().name()
            ))
            .orElse(null);
    }
    
    @Override
    public boolean userExists(Long userId) {
        return userRepository.existsById(userId);
    }
    
    @Override
    public long getTotalUsers() {
        return userRepository.count();
    }
    
    @Override
    public long getActiveUsers() {
        return userRepository.countActiveUsers();
    }
    
    @Override
    public long getNewUsersThisMonth() {
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        return userRepository.countUsersCreatedAfter(startOfMonth);
    }
    
    @Override
    public long getTotalUsersCount() {
        return userRepository.count();
    }
    
    @Override
    public long getUserCountByRole(String role) {
        try {
            User.Role userRole = User.Role.valueOf(role);
            return userRepository.countByRole(userRole);
        } catch (IllegalArgumentException e) {
            return 0;
        }
    }
    
    @Override
    public long getUsersCreatedInMonth(int year, int month) {
        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0, 0);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1);
        return userRepository.countUsersCreatedBetween(startOfMonth, endOfMonth);
    }
    
    public User createUser(User user) {
        // Encode password if it's not already encoded
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }
}
