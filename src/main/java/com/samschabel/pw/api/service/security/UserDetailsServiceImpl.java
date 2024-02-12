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
        List<LkRoleEntity> roleEntities = new ArrayList<>();
        roles.forEach(role -> {
            lkRoleRepository
                    .findByName(role.toString())
                    .ifPresent(roleEntity -> roleEntities.add(roleEntity));
        });
        userEntity.setRoles(roleEntities);
        userRepository.save(userEntity);
    }

}
