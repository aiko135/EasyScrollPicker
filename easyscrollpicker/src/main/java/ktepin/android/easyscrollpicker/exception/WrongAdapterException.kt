package ktepin.android.easyscrollpicker.exception

import android.content.Context
import ktepin.android.easyscrollpicker.R

class WrongAdapterException(context: Context)  : AbstractException(
    context,
    R.string.easy_scroll_wrong_adapter
)