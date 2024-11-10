package ktepin.android.easyscrollpicker.lib

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class EasyScrollCallbacks<VH : RecyclerView.ViewHolder, I>(
    /* Required arguments */
    val onCreateViewHolder: (parent: ViewGroup) -> VH,
    val onBindViewHolder: (holder: VH, item: I) -> Unit,

    /* Optional arguments */
    val onItemSelect: ((item: I) -> Unit)? = null,
)