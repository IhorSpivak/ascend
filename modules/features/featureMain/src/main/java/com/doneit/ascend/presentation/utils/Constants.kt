package com.doneit.ascend.presentation.utils

object Constants {

    const val REMOTE_DATE_FORMAT_FULL = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val AWS_BUCKET: String = "bucket-ascend"
    const val AWS_BUCKET_MSG: String = "ascend2u"
    const val AWS_REGION: String = "us-east-1"
    const val AWS_REGION_2: String = "us-east-2"
    const val COGNITO_POOL_REGION: String = "us-east-1"
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

    const val SUPPORT_EMAIL = "support@ascend2u.com"

    const val PRIVACY_POLICY = "https://support.ascend2u.com/en/article/privacy-policy"
    const val TERMS_OF_USAGE = "https://support.ascend2u.com/en/article/terms-conditions "

    const val AM = "AM"
    const val PM = "PM"

    const val LIST_INDEX_ABSENT = -1
    const val DEFAULT_MODEL_ID = -1L

    const val KEY_GROUP_ID = "group_id"
    const val KEY_PROFILE_ID = "profile_id"

    const val DEEP_LINK_PROFILE_URL = "https://ascend.com/profile/"
    const val DEEP_LINK_GROUP_URL = "https://ascend.com/group/"

    const val MAX_MEMBERS_COUNT = 50
}