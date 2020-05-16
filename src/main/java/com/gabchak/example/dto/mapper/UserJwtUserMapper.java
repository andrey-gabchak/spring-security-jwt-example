package com.gabchak.example.dto.mapper;

import com.gabchak.example.models.Role;
import com.gabchak.example.models.User;
import com.gabchak.example.dto.jwt.JwtUser;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class UserJwtUserMapper extends ConfigurableMapper {
  @Override
  protected void configure(MapperFactory factory) {
    factory
            .classMap(User.class, JwtUser.class)
            .byDefault()
            .customize(new UserJwtUserMapperImpl())
            .register();
  }

  private static class UserJwtUserMapperImpl extends CustomMapper<User, JwtUser> {

    public static final String GRANTED_AUTHORITY_PREFIX = "ROLE_";

    @Override
    public void mapAtoB(User user, JwtUser jwtUser, MappingContext context) {
      jwtUser.setId(user.getId());
      jwtUser.setFirstName(user.getFirstName());
      jwtUser.setLastName(user.getLastName());
      jwtUser.setPassword(user.getPassword());
      jwtUser.setAuthorities(mapRolesToAuthorities(user.getRoles()));
      jwtUser.setUsername(user.getEmail());
    }

    private List<GrantedAuthority> mapRolesToAuthorities(Collection<Role> userRoles) {
      return userRoles.stream()
              .map(role ->
                      new SimpleGrantedAuthority(
                              GRANTED_AUTHORITY_PREFIX + role.getName())
              ).collect(Collectors.toList());
    }
  }
}
