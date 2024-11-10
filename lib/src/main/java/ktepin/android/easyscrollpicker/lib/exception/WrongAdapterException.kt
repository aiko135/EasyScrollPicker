package ktepin.android.easyscrollpicker.lib.exception

import android.content.Context
import ktepin.android.easyscrollpicker.lib.R

class WrongAdapterException(context: Context)  : AbstractException(
    context,
    R.string.easy_scroll_wrong_adapter
)