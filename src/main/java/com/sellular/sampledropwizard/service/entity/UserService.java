package com.sellular.sampledropwizard.service.entity;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import com.sellular.commons.core.exception.SellularException;
import com.sellular.commons.core.service.BaseService;
import com.sellular.sampledropwizard.dao.UserDao;
import com.sellular.sampledropwizard.domain.dtos.request.CreateUserRequest;
import com.sellular.sampledropwizard.domain.dtos.request.UpdateUserRequest;
import com.sellular.sampledropwizard.domain.dtos.response.UserResponse;
import com.sellular.sampledropwizard.domain.entity.User;
import com.sellular.sampledropwizard.service.kafka.publisher.UserEventPublisherService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpStatus;
import org.modelmapper.ModelMapper;

import java.util.Objects;

import static com.sellular.sampledropwizard.constants.ErrorCodes.*;

@Singleton
@Slf4j
public class UserService extends BaseService<User> {

    private final UserDao userDao;

    private final ModelMapper modelMapper;

    private final UserEventPublisherService userEventPublisherService;

    @Inject
    public UserService(final UserDao userDao, final ModelMapper modelMapper, final UserEventPublisherService userEventPublisherService) {
        super(userDao);
        this.userDao = userDao;
        this.modelMapper = modelMapper;
        this.userEventPublisherService = userEventPublisherService;
    }

    public User getUserById(final Long id) {
        log.info("Fetching user by ID: {}", id);
        try {
            return userDao.findById(id);
        } catch (Exception e) {
            log.error("Error in getting userById: {}. Exception: {}", id, e.toString());
            throw new SellularException(ERROR_IN_GETTING_USER_1, HttpStatus.INTERNAL_SERVER_ERROR_500);
        }
    }

    public User getUserByExternalId(final String externalId) {
        try {
            log.info("Fetching user by externalId: {}", externalId);
            return userDao.findByExternalId(externalId)
                    .orElseThrow(() -> new SellularException(USER_NOT_FOUND_FOR_THIS_EXTERNAL_ID, HttpStatus.NOT_FOUND_404));
        } catch (SellularException se) {
            throw se;
        } catch (Exception e) {
            log.error("Error in getUserByExternalId: {}. Exception: {}", externalId, e.toString());
            throw new SellularException(ERROR_IN_GETTING_USER_2, HttpStatus.INTERNAL_SERVER_ERROR_500);
        }
    }

    @Transactional
    public UserResponse createUser(final CreateUserRequest request) {
        try {
            log.info("Creating user with request: {}", request);
            createUserValidation(request);
            final User user = User.from(request);
            userDao.create(user);
            log.info("User created successfully with externalId: {}", user.getExternalId());
            userEventPublisherService.publishUserCreatedEvent(user.getUsername(), user.getContact(), user.getEmail(), user.getExternalId());
            return modelMapper.map(user, UserResponse.class);
        } catch (SellularException se) {
            throw se;
        } catch (Exception e) {
            log.error("Failed to create user", e);
            throw new SellularException(UNABLE_TO_CREATE_USER, HttpStatus.INTERNAL_SERVER_ERROR_500);
        }
    }

    private void createUserValidation(CreateUserRequest request) {
        if (!Strings.isNullOrEmpty(request.getContact())
                && userDao.findUserByContact(request.getContact()).isPresent()) {
            log.warn("Contact already exists: {}", request.getContact());
            throw new SellularException(USER_CONTACT_ALREADY_EXISTS, HttpStatus.CONFLICT_409);
        }
        if (!Strings.isNullOrEmpty(request.getEmail())
                && userDao.findUserByEmail(request.getEmail()).isPresent()) {
            log.warn("Email already exists: {}", request.getEmail());
            throw new SellularException(USER_EMAIL_ALREADY_EXISTS, HttpStatus.CONFLICT_409);
        }
    }

    @Transactional
    public UserResponse updateUser(final UpdateUserRequest request, final String externalId) {
        log.info("Updating user for externalId: {} with request: {}", externalId, request);
        final User fetchedUser = userDao.findByExternalId(externalId)
                .orElseThrow(() -> new SellularException(USER_NOT_FOUND_FOR_THIS_EXTERNAL_ID, HttpStatus.NOT_FOUND_404));
        updateUserValidation(request, fetchedUser);
        fetchedUser.merge(request);
        log.info("User updated for externalId: {}", externalId);
        return modelMapper.map(fetchedUser, UserResponse.class);
    }

    private void updateUserValidation(final UpdateUserRequest request, final User fetchedUser) {
        if (Objects.nonNull(request.getContact()) && !request.getContact().equals(fetchedUser.getContact())) {
            userDao.findUserByContact(request.getContact()).ifPresent(existingUser -> {
                log.warn("Contact already exists during update: {}", request.getContact());
                throw new SellularException(USER_CONTACT_ALREADY_EXISTS, HttpStatus.CONFLICT_409);
            });
        }
        if (Objects.nonNull(request.getEmail()) && !request.getEmail().equals(fetchedUser.getEmail())) {
            userDao.findUserByEmail(request.getEmail()).ifPresent(existingUser -> {
                log.warn("Email already exists during update: {}", request.getEmail());
                throw new SellularException(USER_EMAIL_ALREADY_EXISTS, HttpStatus.CONFLICT_409);
            });
        }
    }

    @Transactional
    public UserResponse resetUserEmail(final String externalId) {
        log.info("Resetting user email for externalId: {}", externalId);
        final User user = userDao.findByExternalId(externalId)
                .orElseThrow(() -> new SellularException(USER_NOT_FOUND_FOR_THIS_EXTERNAL_ID, HttpStatus.NOT_FOUND_404));

        user.setEmail(null);
        log.info("Email reset for user with externalId: {}", externalId);
        return modelMapper.map(user, UserResponse.class);
    }

    @Transactional
    public void deleteUserByExternalId(final String externalId) {
        log.info("Deleting user for externalId: {}", externalId);
        final User user = userDao.findByExternalId(externalId)
                .orElseThrow(() -> new SellularException(USER_NOT_FOUND_FOR_THIS_EXTERNAL_ID, HttpStatus.NOT_FOUND_404));

        user.setDeleted(true);
        log.info("User marked as deleted: {}", externalId);
    }

}
