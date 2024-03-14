package org.kata.controller;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    // URI Endpoints
    static final String URI_ADDRESS = "v1/address";
    static final String URI_AVATAR = "v1/avatar";
    static final String URI_CONTACT_MEDIUM = "v1/contactMedium";
    static final String URI_DOCUMENT = "v1/document";
    static final String URI_INDIVIDUAL = "v1/individual";
    static final String URI_WALLET = "v1/wallet";
    static final String ALL_ENDPOINT = "/all";
    static final String CREATE_ENDPOINT = "/create";
    static final String UPDATE_ACTUAL_STATE_ENDPOINT = "/updateActualState";
    static final String BY_PHONE_ENDPOINT = "/byPhone";
    static final String DELETE_ENDPOINT = "/delete";
    static final String BY_MOBILE_AND_CURRENCY_ENDPOINT = "/byMobileAndCurrency";

    // Operation Summaries
    static final String GET_ADDRESS_BY_ICP = "Получить Address по icp";
    static final String CREATE_NEW_ADDRESS = "Создать новый Address";
    static final String GET_AVATAR_BY_ICP = "Получить Avatar по icp";
    static final String CREATE_NEW_AVATAR = "Создать новый Avatar";
    static final String GET_CONTACT_MEDIUM_SUMMARY = "Получить ContactMedium по icp";
    static final String CREATE_CONTACT_MEDIUM_SUMMARY = "Создать новый ContactMedium";
    static final String GET_DOCUMENT_SUMMARY = "Получить Document по icp";
    static final String CREATE_NEW_DOCUMENT = "Создать новый Document";
    static final String GET_INDIVIDUAL_SUMMARY = "Получить Individual по ICP";
    static final String CREATE_INDIVIDUAL_SUMMARY = "Создать нового Individual";
    static final String DELETE_INDIVIDUAL_SUMMARY = "Delete an individual by icp";
    static final String GET_WALLET_SUMMARY = "Получить Wallet по icp";
    static final String CREATE_WALLET_SUMMARY = "Создать новый Wallet";
    static final String GET_WALLET_BY_MOBILE_AND_CURRENCY_SUMMARY = "Получить Wallet по номеру телефона и валюте";
    static final String UPDATE_WALLET_SUMMARY = "Обновляет баланс Wallet по номеру кошелька";

    // Operation Descriptions
    static final String ICP_WALLET_DESCRIPTION = "ICP Wallet";
    static final String ICP_DOCUMENT = "ICP Document";
    static final String ICP_INDIVIDUAL = "ICP Individual";
    static final String ICP_ADDRESS = "ICP Address";
    static final String ICP_AVATAR = "ICP Avatar";
    static final String PHONE_INDIVIDUAL = "Phone Individual";
    static final String RETURNS_DTO_ADDRESS_BY_ICP = "Возвращает DTO Address по ICP";
    static final String SAVES_AND_RETURN_NEW_ADDRESS_DTO = "Сохраняет и возвращает DTO нового адреса";
    static final String RETURNS_DTO_AVATAR_BY_ICP = "Возвращает DTO Avatar по ICP";
    static final String SAVES_AND_RETURNS_NEW_AVATAR_DTO = "Сохраняет и возвращает DTO нового аватара";
    static final String GET_CONTACT_MEDIUM_DESCRIPTION = "Возвращает DTO ContactMedium по ICP";
    static final String CREATE_CONTACT_MEDIUM_DESCRIPTION = "Сохраняет и возвращает DTO нового контакта";
    static final String GET_DOCUMENT_DESCRIPTION = "Возвращает DTO Document по ICP";
    static final String CREATE_NEW_DOCUMENT_DESC = "Сохраняет и возвращает DTO нового документа";
    static final String GET_INDIVIDUAL_DESCRIPTION = "Возвращает DTO Individual по ICP";
    static final String CREATE_INDIVIDUAL_DESCRIPTION = "Сохраняет и возвращает DTO нового индивида";
    static final String GET_WALLET_DESCRIPTION = "Возвращает DTO Wallet по ICP";
    static final String CREATE_WALLET_DESCRIPTION = "Сохраняет и возвращает DTO нового кошелька";
    static final String WALLET_CREATED_DESCRIPTION = "Wallet успешно создан";
    static final String DTO_WALLET_FOR_CREATION_DESCRIPTION = "DTO Wallet для создания";
    static final String GET_WALLET_BY_MOBILE_AND_CURRENCY_DESCRIPTION = "Возвращает WalletDto";
    static final String WALLET_SUCCESSFULLY_UPDATED_DESCRIPTION = "Wallet успешно обновлён";
    static final String ICP_CONTACT_MEDIUM_DESCRIPTION = "ICP ContactMedium";
    static final String DTO_CONTACT_MEDIUM_DESCRIPTION = "DTO ContactMedium для создания";
    static final String INTERNAL_SERVER_ERROR_DESCRIPTION = "Internal server error";
    static final String DOCUMENT_DTO_FOR_CREATION_DESCRIPTION = "DTO Document для создания";
    static final String UPDATE_ACTUAL_STATE_DESCRIPTION = "Деактивирует актуальный документ если более новый есть в топике Kafka";
    static final String DTO_DOCUMENT_FOR_DEACTIVATION_DESCRIPTION = "DTO Document для деактивации";
    static final String INDIVIDUAL_CREATED_DESCRIPTION = "Individual успешно создан";
    static final String INDIVIDUAL_TO_CREATE_DESCRIPTION = "DTO Individual для создания";
    static final String CREATE_TEST_INDIVIDUAL_DESCRIPTION = "Сохраняет и возвращает DTO тестового индивида";
    static final String GET_INDIVIDUAL_BY_PHONE_DESCRIPTION = "Возвращает DTO Individual по номеру";
    static final String UPDATE_WALLET_DESCRIPTION = "Возвращает DTO обновлённого Wallet";

    // Response Codes and Messages
    static final String CODE_200 = "200";
    static final String CODE_201 = "201";
    static final String CODE_202 = "202";
    static final String CODE_204 = "204";
    static final String CODE_400 = "400";
    static final String CODE_500 = "500";
    static final String ADDRESS_CREATED = "Address успешно создан";
    static final String INCORRECT_REQUEST = "Неверный запрос";
    static final String ADDRESS_WITH_ICP_SAVED = "Address with icp: %s, successfully saved to the database!";
    static final String AVATAR_WITH_NAME_SAVED = "Avatar with name: %s, successfully saved to the database!";
    static final String CONTACT_MEDIUM_SAVED_TO_DB = "%s successfully saved to the database!";
    static final String AVATAR_SUCCESSFULLY_CREATED = "Avatar успешно создан";
    static final String AVATAR_UPDATED_SUCCESS = "Avatar успешно обновлён";
    static final String CONTACT_MEDIUM_CREATED = "ContactMedium успешно создан";
    static final String SUCCESSFUL_RETRIEVAL_ALL_DOCUMENTS = "Successful retrieval all documents";
    static final String DOCUMENT_SUCCESSFULLY_SAVED_TO_DB = "%s successfully saved to the database!";
    static final String DOCUMENT_SUCCESSFULLY_DEACTIVATED = "Document успешно деактивирован";
    static final String INDIVIDUAL_SUCCESSFULLY_SAVED = "Individual with icp: %s successfully saved to the database!";
    static final String TEST_INDIVIDUAL_CREATED = "Individual успешно создан";
    static final String SUCCESSFUL_DELETION_DESCRIPTION = "Successful deleted of Individual";
    static final String CONTROLLER_LOADER_DELETE_ICP = "controller loader delete icp %s";
    static final String WALLET_SUCCESSFULLY_SAVED = "Wallet with ID: %s, successfully saved to the database!";
    static final String DOCUMENT_CREATED = "Document успешно создан";

    // Debug and Error Handling
    static final String X_DEBUG_INFO = "X-Debug-Info";
    static final String BAD_REQUEST = "Неверный запрос";

    // DTO Operations
    static final String DTO_ADDRESS_FOR_CREATE = "DTO Address для создания";
    static final String DTO_AVATAR_FOR_CREATION = "DTO Avatar для создания";
    static final String GET_AVATAR_LIST_BY_ICP = "Получить список Avatar по icp";
    static final String RETURNS_LIST_DTO_AVATAR_BY_ICP = "Возвращает список DTO Avatar по ICP";
    static final String ICP_TO_GET_LIST_AVATAR = "ICP для получения List<Avatar>";
    static final String DELETE_AVATARS_BY_ICP_AND_FLAGS = "Запрос на удаление аватаров по icp и списку флагов";
    static final String DELETE_AVATARS_DESC = "Запрос на удаление одного или нескольких Avatar по icp и списку boolean (галочки) в соответствии со списком getAllAvatars(String icp)";
    static final String ICP_FOR_DELETION = "ICP для удаления";
    static final String FLAGS_FOR_DELETION = "Флаги для удаления";
    static final String UPDATE_ACTIVE_AVATAR = "Обновляет активный аватар";
    static final String UPDATE_AVATAR_DESC = "Обновляет аватар по AvatarDto и хешу, рассчитанному в ProfileAvatar";
    static final String DTO_FOR_UPDATE = "DTO для обновления";
    static final String HASH_OF_UPDATING_IMAGE = "Хеш обновляемого изображения";
    static final String UPDATE_ACTUAL_STATE_SUMMARY = "Деактивация актуального документа";
    static final String CREATE_TEST_INDIVIDUAL_SUMMARY = "Создать тестового Individual";
    static final String GET_INDIVIDUAL_BY_PHONE_SUMMARY = "Получить Individual по номеру";
}
