package com.gabchak.example.dto.mapper;

import com.gabchak.example.dto.enums.Roles;
import com.gabchak.example.dto.jwt.RegisterRequest;
import com.gabchak.example.models.Role;
import com.gabchak.example.models.User;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.ConfigurableMapper;

import java.util.Collections;

public class RegisterRequestUserMapper extends ConfigurableMapper {

    @Override
    protected void configure(MapperFactory factory) {
        factory
                .classMap(RegisterRequest.class, User.class)
                .byDefault()
                .customize(new RegisterDtoUserMapperImpl())
                .register();
    }

    private class RegisterDtoUserMapperImpl extends CustomMapper<RegisterRequest, User> {
        @Override
        public void mapAtoB(RegisterRequest registerRequest, User user, MappingContext context) {
            user.setEmail(registerRequest.getEmail());
            user.setFirstName(registerRequest.getFirstName());
            user.setLastName(registerRequest.getLastName());
            user.setRoles(Collections.singletonList(
                    Role.fromEnum(Roles.FREE_USER)));
            super.mapAtoB(registerRequest, user, context);
        }
    }

}
