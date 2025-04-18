package com.sellular.sampledropwizard.utils;

import com.sellular.sampledropwizard.constants.KafkaEvents;
import com.sellular.user.v1.UserEventModel;
import lombok.SneakyThrows;
import java.net.InetAddress;

public class ProtoModelMapper {

    private static final String serviceName = "user_service";

    @SneakyThrows
    private static String getHostName() {
        return InetAddress.getLocalHost().getHostName();
    }

    public static UserEventModel createUserEventModel(final String username, final String contact, final String email, final String userExternalId) {
        final UserEventModel.Data data = UserEventModel.Data.newBuilder()
                .setUsername(username)
                .setContact(contact)
                .setEmail(email)
                .build();

        return UserEventModel.newBuilder()
                .setData(data)
                .setSource(serviceName)
                .setEventTime(System.currentTimeMillis())
                .setHostname(getHostName())
                .setEntityId(userExternalId)
                .setEventType(KafkaEvents.user_created.name())
                .setEntityType("user")
                .build();
    }


}
