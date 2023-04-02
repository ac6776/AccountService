package account.service;

public enum Event {
    CREATE_USER, //user service
    CHANGE_PASSWORD, //user service
    ACCESS_DENIED, //Access denied Handler
    LOGIN_FAILED, //entrypoint
    GRANT_ROLE, //user service
    REMOVE_ROLE, //user service
    LOCK_USER, //user service
    UNLOCK_USER, //user service
    DELETE_USER, //user service
    BRUTE_FORCE //entrypoint
}
