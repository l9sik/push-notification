package com.poluectov.subscriptionservice.controller;

import com.poluectov.subscriptionservice.model.SubscribedUserResponseDto;
import com.poluectov.subscriptionservice.model.SubscriptionRequestDto;
import com.poluectov.subscriptionservice.model.SubscriptionResponseDto;
import com.poluectov.subscriptionservice.service.SubscriptionService;
import com.poluectov.subscriptionservice.utils.SubscriptionEntityModelAssembler;
import jakarta.persistence.EntityNotFoundException;
import jdk.jfr.ContentType;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final SubscriptionEntityModelAssembler assembler;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody SubscriptionRequestDto requestDto) {

        SubscriptionResponseDto responseDto = subscriptionService.create(requestDto);

        EntityModel<SubscriptionResponseDto> entityModel = assembler.toModel(responseDto);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    /**
     * Retrieves a SubscriptionResponseDto object for the given ID.
     *
     * @param  id  the ID of the SubscriptionResponseDto object
     * @return     a ResponseEntity containing the Subscription object and a location header
     *             pointing to the object's URI
     * @throws EntityNotFoundException if the Subscription object with the given ID is not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> one(@PathVariable Long id){
        SubscriptionResponseDto user = subscriptionService.one(id).orElseThrow(() -> new EntityNotFoundException("User not found"));

        EntityModel<SubscriptionResponseDto> entityModel = assembler.toModel(user);


        return ResponseEntity
                .ok()
                .location(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    /**
     * Retrieves a collection of SubscriptionResponseDto objects for a given user ID.
     *
     * @param  id  the ID of the user
     * @return     a CollectionModel containing EntityModels of SubscriptionResponseDto objects
     */
    @GetMapping("/users/{id}")
    public List<SubscriptionResponseDto> findByUserId(@PathVariable Long id) {

        return subscriptionService.findByUserId(id);
    }

    /**
     * Retrieves a list of all subscribed users.
     * @return a list of subscribed users
     */
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> delete() {
        List<SubscribedUserResponseDto> users = subscriptionService.subscribedUsers();
        return ResponseEntity.ok().body(users);
    }

    @GetMapping
    public CollectionModel<EntityModel<SubscriptionResponseDto>> all(){
        List<SubscriptionResponseDto> subscriptions = subscriptionService.all();

        return toCollectionModel(subscriptions);
    }


    /** Transforms the SubscriptionResponseDto into an EntityModel for metadata in response
     *
     * @param subscriptions the list of SubscriptionResponseDto
     * @return the list of EntityModel
     */
    private CollectionModel<EntityModel<SubscriptionResponseDto>> toCollectionModel(List<SubscriptionResponseDto> subscriptions) {
        List<EntityModel<SubscriptionResponseDto>> subscriptionEntities = subscriptions.stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(subscriptionEntities, linkTo(methodOn(SubscriptionController.class).all()).withSelfRel());
    }

}
