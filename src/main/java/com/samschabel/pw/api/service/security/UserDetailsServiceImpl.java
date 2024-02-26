package com.samschabel.pw.api.service.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.samschabel.pw.api.entity.LkRoleEntity;
import com.samschabel.pw.api.entity.UserEntity;
import com.samschabel.pw.api.model.security.RoleEnum;
import com.samschabel.pw.api.model.security.UserDetailsImpl;
import com.samschabel.pw.api.repository.LkRoleRepository;
import com.samschabel.pw.api.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final LkRoleRepository lkRoleRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserEntity> userEntity = userRepository.findByUsername(username);

        // Converting userDetail to UserDetails
        return userEntity.map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }

    @Transactional
    public void addUser(UserDetailsImpl userDetails, List<RoleEnum> roles) {
        userDetails.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(userDetails.getPassword());
        userEntity.setUsername(userDetails.getUsername());
        userEntity.setEnabled(true);
        userEntity.setLocked(false);
        userEntity.setLoginAttempts(0);
        List<LkRoleEntity> roleEntities = new ArrayList<>();
        roles.forEach(role -> {
            lkRoleRepository
                    .findByName(role.toString())
                    .ifPresent(roleEntity -> roleEntities.add(roleEntity));
        });
        userEntity.setRoles(roleEntities);
        userRepository.save(userEntity);
    }

    public void incrementLoginAttempts(String username) {
        log.info("Bad credentials for user " + username + ". Incrementing login attempt count.");
        
        userRepository
                .findByUsername(username)
                .ifPresentOrElse(user -> checkLoginAttempts(user),
                        () -> { // on else, throw exception
                            String message = "User not found, " + username;
                            throw new UsernameNotFoundException(message);
                        });
    }

    private void checkLoginAttempts(UserEntity user) {
        int loginAttempts = user.getLoginAttempts() < 0 ? 1 : user.getLoginAttempts() + 1; // increment user login attempts

        if (loginAttempts >= 3 && !user.isLocked()) {
            log.info("Locking user " + user.getUsername() + "with " + loginAttempts + " login attempts.");
            user.setLocked(true);
        }
        
        user.setLoginAttempts(loginAttempts);
        userRepository.save(user);
    }

}
