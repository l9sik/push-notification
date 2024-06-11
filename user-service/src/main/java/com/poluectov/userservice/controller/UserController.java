package com.poluectov.userservice.controller;

import com.poluectov.userservice.model.user.UserRequestDto;
import com.poluectov.userservice.model.user.UserResponseDto;
import com.poluectov.userservice.service.UserService;
import com.poluectov.userservice.utils.UserEntityModelAssembler;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserEntityModelAssembler assembler;

    @GetMapping("/{id}")
    public ResponseEntity<?> one(@PathVariable Long id){
        UserResponseDto user = userService.one(id).orElseThrow(() -> new EntityNotFoundException("User not found"));

        EntityModel<UserResponseDto> entityModel = assembler.toModel(user);


        return ResponseEntity
                .ok()
                .location(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/self")
    public ResponseEntity<?> self(HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        UserResponseDto user = userService.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));


        return ResponseEntity
                .ok()
                .body(user);
    }

    @GetMapping
    public CollectionModel<EntityModel<UserResponseDto>> all(){
        List<UserResponseDto> users = userService.all();

        List<EntityModel<UserResponseDto>> userEntities = users.stream()
                        .map( assembler::toModel)
                .toList();

        return CollectionModel.of(userEntities, linkTo(methodOn(UserController.class).all()).withSelfRel());
    }

    @RequestMapping(value = "/some/{userIds}", method = RequestMethod.GET)
    public List<UserResponseDto> findByUserIds(@PathVariable Long[] userIds) {
        return userService.allByUserIds(Arrays.stream(userIds).toList());
    }

}
