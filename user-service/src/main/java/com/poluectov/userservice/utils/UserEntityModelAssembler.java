package com.poluectov.userservice.utils;


import com.poluectov.userservice.controller.UserController;
import com.poluectov.userservice.model.user.UserResponseDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserEntityModelAssembler implements RepresentationModelAssembler<UserResponseDto, EntityModel<UserResponseDto>> {
    @Override
    public EntityModel<UserResponseDto> toModel(UserResponseDto entity) {
        return EntityModel.of(entity, //
                linkTo(methodOn(UserController.class).one(entity.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).all()).withRel("users"));
    }
}
