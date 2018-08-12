package com.ymmihw.spring.data.jpa.with.spring.security.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.ymmihw.spring.data.jpa.with.spring.security.data.repositories.UserRepository;
import com.ymmihw.spring.data.jpa.with.spring.security.models.AppUser;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  public CustomUserDetailsService() {
    super();
  }

  @Override
  public UserDetails loadUserByUsername(final String username) {
    final AppUser appUser = userRepository.findByUsername(username);
    if (appUser == null) {
      throw new UsernameNotFoundException(username);
    }
    return new AppUserPrincipal(appUser);
  }

}
