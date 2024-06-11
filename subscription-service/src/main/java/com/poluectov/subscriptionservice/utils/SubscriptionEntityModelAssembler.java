package com.poluectov.subscriptionservice.utils;

import com.poluectov.subscriptionservice.controller.SubscriptionController;
import com.poluectov.subscriptionservice.model.SubscriptionResponseDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SubscriptionEntityModelAssembler implements RepresentationModelAssembler<SubscriptionResponseDto, EntityModel<SubscriptionResponseDto>> {


    @Override
    public EntityModel<SubscriptionResponseDto> toModel(SubscriptionResponseDto entity) {
        return EntityModel.of(entity, //
                linkTo(methodOn(SubscriptionController.class).one(entity.getId())).withSelfRel(),
                linkTo(methodOn(SubscriptionController.class).all()).withRel("users"));

    }
}

