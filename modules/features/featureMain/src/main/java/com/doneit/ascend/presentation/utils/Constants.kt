package com.doneit.ascend.presentation.utils

object Constants {

    //TODO: amazon credentials:
    const val AWS_REGION: String = "us-east-2"
    const val COGNITO_POOL_REGION: String = "us-east-2"
    const val COGNITO_POOL_ID: String = "us-east-1:16288c3c-c8eb-4c4f-8219-a38737f3438e"

    const val TIME_AUTO_CLOSE_DIALOG: Long = 3_000
    const val TIME_CHECK_NETWORK: Long = 5_000
    const val RESEND_CODE_INTERVAL = 60 * 1_000L
    const val RC_SIGN_IN = 1488

    const val END_OF_LINE = "\n"

    const val COMPRESSION_QUALITY = 80
    const val PER_PAGE_COMMON = 60

    const val STRIPE_KEY_LIVE = "pk_live_nNCN6FgjDBRl6Go0aWieSuIs00JA9WmEq0"
    const val STRIPE_KEY_TEST = "pk_test_bWIeTCRqJewkDArnkei72wHr00lqB0NIEq"

    const val SUPPORT_EMAIL = "support@doneit.com"


    const val AM = "AM"
    const val PM = "PM"

    const val LIST_INDEX_ABSENT = -1
    const val DEFAULT_MODEL_ID = -1L

    const val KEY_GROUP_ID = "group_id"
}