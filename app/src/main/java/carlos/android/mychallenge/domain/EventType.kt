package carlos.android.mychallenge.domain

import carlos.android.mychallenge.R

enum class EventType(val stringResId: Int) {
    ADD(R.string.add_message),
    ERROR_EMPTY(R.string.error_empty),
    ERROR_DATABASE(R.string.error_database),
}