package ktepin.android.easyscrollpicker.sample

import android.content.Context
import android.util.TypedValue

//dp to px
fun dpToPx(ctx: Context, dp: Int): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    dp.toFloat(),
    ctx.resources.displayMetrics
)