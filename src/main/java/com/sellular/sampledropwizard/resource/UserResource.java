package com.sellular.sampledropwizard.resource;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sellular.commons.core.annotations.GlobalSwaggerHeaders;
import com.sellular.commons.core.filter.AccessTokenRequired;
import com.sellular.sampledropwizard.domain.dtos.request.CreateUserRequest;
import com.sellular.sampledropwizard.domain.dtos.request.UpdateUserRequest;
import com.sellular.sampledropwizard.service.entity.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Path("/user")
@Singleton
@Slf4j
@AccessTokenRequired
@Tag(name = "User Entity API", description = "Operations related to User management")
public class UserResource {

    private final UserService userService;

    @Inject
    public UserResource(final UserService userService) {
        this.userService = userService;
    }

    @GET
    @Operation(summary = "Check if the server is up", description = "Returns a simple server status message")
    public String check() {
        return "Server is Up";
    }

    @GET
    @Path("/id/{id}")
    @Timed
    @Operation(summary = "Find user by ID", description = "Retrieve a user by their ID")
    public Response findUser(@PathParam("id") final Long id) {
        return Response.status(Response.Status.OK).entity(userService.getUserById(id)).build();
    }

    @GET
    @Path("{externalId}")
    @Timed
    @Operation(summary = "Find user by external ID", description = "Retrieve a user by their external ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GlobalSwaggerHeaders
    public Response findUserByExternalId(@PathParam("externalId") final String externalId) {
        return Response.status(Response.Status.OK).entity(userService.getUserByExternalId(externalId)).build();
    }

    @POST
    @Timed
    @Operation(summary = "Create a new user", description = "Create a new user by providing user details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "409", description = "Duplicate contact or email"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GlobalSwaggerHeaders
    public Response createUser(@Valid @NotNull final CreateUserRequest request) {
        return Response.status(Response.Status.CREATED).entity(userService.createUser(request)).build();
    }

    @PUT
    @Path("{externalId}")
    @Timed
    @Operation(summary = "Update user details", description = "Update user details by providing the external ID and new information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "409", description = "Duplicate contact or email"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GlobalSwaggerHeaders
    public Response updateUser(@Valid @NotNull final UpdateUserRequest request, @PathParam("externalId") final String externalId) {
        return Response.status(Response.Status.OK).entity(userService.updateUser(request, externalId)).build();
    }

    @PATCH
    @Path("/reset-email/{externalId}")
    @Timed
    @Operation(summary = "Reset user email", description = "Reset the email address of a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User email reset"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GlobalSwaggerHeaders
    public Response resetUserEmail(@PathParam("externalId") final String externalId) {
        return Response.status(Response.Status.OK).entity(userService.resetUserEmail(externalId)).build();
    }

    @DELETE
    @Path("/{externalId}")
    @Timed
    @Operation(summary = "Delete user", description = "Delete a user by their external ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GlobalSwaggerHeaders
    public Response deleteUser(@PathParam("externalId") final String externalId) {
        userService.deleteUserByExternalId(externalId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
