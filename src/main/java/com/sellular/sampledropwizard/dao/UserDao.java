package com.sellular.sampledropwizard.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sellular.commons.jpa.dao.AbstractDao;
import com.sellular.commons.jpa.querybuilder.JPACountQueryBuilder;
import com.sellular.commons.jpa.querybuilder.JPAQueryBuilder;
import com.sellular.sampledropwizard.domain.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@Singleton
public class UserDao extends AbstractDao<User> {

    @Inject
    public UserDao(final Provider<EntityManager> entityManager) {
        super(entityManager, User.class);
    }

    private JPAQueryBuilder<User> queryBuilder() {
        return JPAQueryBuilder.init(getEntityManager(), User.class).equal("deleted", Boolean.FALSE);
    }

    private JPACountQueryBuilder<User> countQueryBuilder() {
        return JPACountQueryBuilder.init(getEntityManager(), User.class).equal("deleted", Boolean.FALSE);
    }

    public Optional<User> findByExternalId(final String externalId) {
        try {
            final JPAQueryBuilder<User> queryBuilder = queryBuilder();
            queryBuilder.equal("externalId", externalId);
            return queryBuilder.first();
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<User> findUserByContact(final String contact) {
        try {
            final JPAQueryBuilder<User> queryBuilder = queryBuilder();
            queryBuilder.equal("contact", contact);
            return queryBuilder.first();
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<User> findUserByEmail(final String email) {
        try {
            final JPAQueryBuilder<User> queryBuilder = queryBuilder();
            queryBuilder.equal("email", email);
            return queryBuilder.first();
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}