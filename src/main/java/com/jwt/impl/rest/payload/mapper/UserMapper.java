package com.jwt.impl.rest.payload.mapper;

import com.jwt.impl.core.persistance.entity.User;
import com.jwt.impl.rest.payload.request.SignUpRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User mapEntityOf(SignUpRequest singUpRequest);
}
