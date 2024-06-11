package com.poluectov.subscriptionservice.repository;

import com.poluectov.subscriptionservice.model.SubscribedUser;
import com.poluectov.subscriptionservice.model.UserListResponseDto;

import java.util.Collection;
import java.util.List;

public interface UserRepository {

    List<SubscribedUser> subscribedUsers(Collection<Long> userIds);
}
