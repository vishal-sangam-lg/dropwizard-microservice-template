package com.sellular.sampledropwizard.constants;

import com.sellular.commons.core.exception.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCodes implements ErrorCodeInterface {

    ERROR_IN_GETTING_USER_1("8001", "Error in getting user by id"),
    USER_NOT_FOUND_FOR_THIS_EXTERNAL_ID("8002", "User not found for this external id"),
    ERROR_IN_GETTING_USER_2("8003", "Error in getting user by external id"),
    UNABLE_TO_CREATE_USER("8004", "Unable to create user"),
    USER_CONTACT_ALREADY_EXISTS("8005", "This contact is already mapped to another user"),
    USER_EMAIL_ALREADY_EXISTS("8006", "This email is already mapped to another user"),
    ERROR_IN_GETTING_USER_3("8007", "Error in getting paginated user");

    private String errorCode;

    private String errorMessage;

    private String errorHeader;

    ErrorCodes(final String code, final String message) {
        this.errorCode = code;
        this.errorMessage = message;
    }

}
