package org.kata.service.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    static final String UUID_STRING_VALUE = "uuid";

    // Errors
    static final String ERROR_ADDRESS_NOT_FOUND = "Address with icp: %s not found";
    static final String ERROR_NO_ADDRESS_FOUND_FOR_INDIVIDUAL = "No address found for individual with icp: %s";
    static final String ERROR_INDIVIDUAL_WITH_ICP_NOT_FOUND = "Individual with icp: %s not found";
    static final String ERROR_INDIVIDUAL_UUID_NOT_FOUND = "Individual with uuid: %s not found";
    static final String ERROR_INDIVIDUAL_WITH_PHONE_NOT_FOUND = "Individual with phone: %s not found";
    static final String ERROR_INVALID_ID_OR_TYPE = "Invalid id or type";
    static final String ERROR_INVALID_TYPE = "Invalid type";
    static final String ERROR_AVATAR_WITH_ICP_NOT_FOUND = "Avatar with icp: %s not found";
    static final String ERROR_NO_AVATAR_FOR_INDIVIDUAL = "No avatar found for individual with icp: %s";
    static final String ERROR_FAILED_TO_SAVE_AVATAR_TO_THE_DATABASE = "Failed to save avatar to the database.";
    static final String ERROR_NO_CONTACT_MEDIUM_FOUND_FOR_INDIVIDUAL = "No ContactMedium found for individual with icp: %s";
    static final String ERROR_NO_CONTACT_MEDIUM_FOUND_WITH_TYPE_AND_VALUE = "No contact medium found with type %s and value %s";
    static final String ERROR_NO_DOCUMENT_FOUND_FOR_INDIVIDUAL = "No Document found for individual with icp: %s";
    static final String ERROR_NO_WALLET_FOUND_FOR_ICP = "No wallets for icp %s found";
    static final String ERROR_WALLET_WITH_ID_NOT_FOUND = "Wallet with id %s not found";
    static final String ERROR_WALLET_FOR_MOBILE_WITH_CURRENCY_NOT_FOUND = "Wallet for mobile %s with currency$s not found";

    //Log messages
    static final String LOG_FOR_ICP_CREATED_NEW_ADDRESS = "For icp {} created new Address: {}";
    static final String LOG_SAVED_ADDRESS_TO_DATABASE = "Saved address to the database: {}";
    static final String LOG_FAILED_TO_SAVE_ADDRESS = "Failed to save address to the database.";
    static final String LOG_FOR_ICP_SET_NEW_AVATAR = "For icp {} set new Avatar: {}";
    static final String LOG_FOR_ICP_CREATED_NEW_AVATAR = "For icp {} created new Avatar: {}";
    static final String LOG_SAVED_AVATAR_TO_DATABASE = "Saved avatar to the database: {}";
    static final String LOG_FOR_ICP_CREATED_NEW_CONTACT_MEDIUM = "For icp {} created new ContactMedium: {}";
    static final String LOG_SAVED_CONTACT_MEDIUM_TO_DATABASE = "Saved contactMedium to the database: {}";
    static final String LOG_FAILED_TO_SAVE_CONTACT_MEDIUM_TO_DATABASE = "Failed to save contactMedium to the database.";
    static final String LOG_FOR_ICP_CREATED_NEW_DOCUMENT = "For icp {} created new Document: {}";
    static final String LOG_SAVED_DOCUMENT_MEDIUM_TO_DATABASE = "Saved document to the database: {}";
    static final String LOG_FAILED_TO_SAVE_DOCUMENT_TO_DATABASE = "Failed to save document to the database.";
    static final String LOG_CREATE_NEW_INDIVIDUAL = "Create new Individual: {}";
    static final String LOG_SAVED_INDIVIDUAL_TO_THE_DATABASE = "Saved individual to the database: {}";
    static final String LOG_FAILED_TO_SAVE_INDIVIDUAL_TO_THE_DATABASE = "Saved individual to the database: {}";
    static final String LOG_FOR_ICP_CREATED_NEW_WALLET = "For icp {}, wallet {} was created";
    static final String LOG_SAVED_WALLET_TO_THE_DATABASE = "Saved wallet toi the database :{}";
    static final String LOG_FAILED_TO_SAVE_WALLET_TO_THE_DATABASE = "Failed to save wallet {} to the database";

    // Kafka
    public static final String KAFKA_TOPIC_CREATE = "${kafka.topic.create}";
    public static final String KAFKA_TOPIC_CREATE_2 = "${kafka.topic.create2}";
    public static final String PROFILE_LOADER_GROUP_ID = "profile-loader";
    public static final String KAFKA_CONSUMER_GROUP_ID = "${spring.kafka.consumer.group-id}";
    public static final String FILTER_KAFKA_LISTENER_CONTAINER_FACTORY = "filterKafkaListenerContainerFactory";
    public static final String KAFKA_TOPIC_LISTEN = "${kafka.topic.listen}";
    public static final String MESSAGE_ID = "messageId";
    public static final String LOG_MESSAGE_SEND_ERROR = "Unable to send message to topic:{}, \n{}";
    public static final String LOG_MESSAGE_SEND_SUCCESS = "Message send to topic:{}";
}
