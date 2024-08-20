package com.lock.smartlocker.util

class ConstantUtils {

    companion object {
        const val TIME_OUT = 2000
        const val TIME_DELAY = 5000

        const val API_TOKEN = "API_TOKEN"
        const val USER_TOKEN = "USER_TOKEN"
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
        const val TYPE_REGISTER_FACE = "TYPE_REGISTER_FACE"
        const val TYPE_ADMIN_CONSOLE = "TYPE_ADMIN_CONSOLE"
        const val TYPE_MANAGER_FACE = "TYPE_MANAGER_FACE"

        //Error code
        const val LOGIN_EMAIL_NOT_EXISTED = "401-001"
        const val LOGIN_WRONG_PASSWORD = "401-002"
        const val EMAIL_NOT_CORRECT_FORMAT = "422"
        const val ADMIN_WRONG_USERNAME_PASS = "601"


        const val DOOR_HAS_NOT_BEEN_CLOSE = "423-001"
        const val SERIAL_NUMBER_IS_NO_LONGER_AVAILABLE = "601"
    }
}