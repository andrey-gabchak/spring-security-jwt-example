package com.gabchak.weather.dto.mappers;

import com.gabchak.weather.dto.jwt.RegisterRequest;
import com.gabchak.weather.models.User;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

@Component
public class RegisterUserMapper extends ConfigurableMapper {

  @Override
  protected void configure(final MapperFactory factory) {
    factory.classMap(RegisterRequest.class, User.class)
        .exclude("role")
        .exclude("password")
        .exclude("id")
        .customize(new CustomMapper<RegisterRequest, User>() {
          @Override
          public void mapAtoB(RegisterRequest registerRequest,
                              User user,
                              MappingContext context) {
            super.mapAtoB(registerRequest, user, context);
          }

          @Override
          public void mapBtoA(User averageData,
                              RegisterRequest dataDto,
                              MappingContext context) {
            super.mapBtoA(averageData, dataDto, context);
          }
        })
        .byDefault()
        .register();
  }
}
