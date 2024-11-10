package ktepin.android.easyscrollpicker.lib.exception

import android.content.Context

abstract class AbstractException(
    context: Context,
    messageStringRes: Int
) : Exception(context.getString(messageStringRes))