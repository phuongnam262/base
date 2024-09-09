package com.lock.smartlocker.util

class ConstantUtils {

    companion object {

        const val API_TOKEN = "API_TOKEN"
        const val USER_TOKEN = "USER_TOKEN"
        const val ADMIN_NAME = "ADMIN_NAME"
        const val TERMINAL_LOGIN = "TERMINAL_LOGIN"
        const val TERMINAL_NAME   = "TERMINAL_NAME"
        const val GET_SETTING = "GET_SETTING"
        const val LIST_CATEGORY = "LIST_CATEGORY"
        const val OWNER_TEXT = "OWNER_TEXT"
        const val BACKGROUND = "BACKGROUND"
        const val CHECK_DOOR_STATUS_ON_CONFIRM = "CHECK_DOOR_STATUS_ON_CONFIRM"
        const val MANDATORY_DOOR_CLOSURE_VERIFICATION = "MANDATORY_DOOR_CLOSURE_VERIFICATION"
        const val RETURN_AVAILABLE_LOCKER_LIST = "RETURN_AVAILABLE_LOCKER_LIST"
        const val ERROR_CODE_SUCCESS = "SUCCESS"

        //Type Open
        const val TYPE_OPEN = "TYPE_OPEN"
        const val TYPE_OPEN_MANAGER = "TYPE_OPEN_MANAGER"
        const val TYPE_REGISTER_FACE = "TYPE_REGISTER_FACE"
        const val TYPE_ADMIN_CONSOLE = "TYPE_ADMIN_CONSOLE"
        const val TYPE_MANAGER_FACE = "TYPE_MANAGER_FACE"

        //Type Consumer
        const val TYPE_LOAN = "TYPE_LOAN"
        const val TYPE_COLLECT = "TYPE_COLLECT"
        const val TYPE_CONSUMABLE_COLLECT = "TYPE_CONSUMABLE_COLLECT"

        //Type input serial
        const val TYPE_RETURN = "TYPE_RETURN"
        const val TYPE_TOPUP_ITEM = "TYPE_TOPUP_ITEM"

        //Collect
        const val TRANSACTION_ID = "TRANSACTION_ID"
        const val LOCKER_INFOS = "LOCKER_INFOS"

        //Dialog Confirm
        const val DISABLE_LOCKER = "DISABLE_LOCKER"
        const val ENABLE_LOCKER = "ENABLE_LOCKER"

        //End user
        const val NAME_END_USER = "NAME_END_USER"
        const val WORK_CARD_NUMBER = "WORK_CARD_NUMBER"

        //Error code
        const val LOGIN_EMAIL_NOT_EXISTED = "401-001"
        const val LOGIN_WRONG_PASSWORD = "401-002"
        const val SERIAL_NUMBER_INVALID_1 = "421"
        const val EMAIL_NOT_CORRECT_FORMAT = "422"
        const val DOOR_HAS_NOT_BEEN_CLOSE = "423-001"

        const val INVALID_OTP = "601"
        const val SERIAL_NUMBER_INVALID = "60101"
        const val ADMIN_WRONG_USERNAME_PASS = "60104"
        const val ADMIN_ACCOUNT_LOCKED = "60201"
        const val CREATE_TRANSACTION_FAILED = "604"
        const val PASSWORD_POLICY_UPDATE = "60402"
        const val ERROR_CARD_NUMBER = "60404"
        const val ERROR_NO_AVAILABLE_ITEM = "605"
        const val PASSWORD_EXPIRED = "60503"
        const val ERROR_NO_RETRIEVE_ITEM = "60505"
        const val ERROR_NO_RETRIEVE_FAULTY = "60506"
        const val ERROR_RETRIEVE_ITEM_FAIL = "60509"
        const val ERROR_UNABLE_LOCKER = "606"
        const val ADMIN_NO_PERMISSION = "60601"
        const val ERROR_LOGIC = "623"

    }

    object Language {
        const val ENGLISH = "en"
        const val VIETNAMESE = "vi"
    }
}