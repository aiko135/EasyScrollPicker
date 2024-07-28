package ktepin.android.easyscrollpicker.exception

import android.content.Context
import ktepin.android.easyscrollpicker.R

class WrongLayoutManagerException (context:Context) : AbstractException(
    context,
    R.string.easy_scroll_wrong_lm
)