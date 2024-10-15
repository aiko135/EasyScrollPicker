package ktepin.android.easyscrollpicker

import android.view.ViewGroup

/**
 * there are 2 generics used in the lib. This generics passed by user form the app level
 *    VH - view holder of the inner RecyclerView
 *    I - item, payload data type which is associated for each item of the inner RecyclerView
 *
 *    Note: this facade required only to perform right generics to lib user, under the hood they will be unbounded
 */
class EasyScrollManager<VH : EasyScrollViewHolder<I>, I>(
    private val easyScrollPicker: EasyScrollPicker,
    /* Required arguments */
    onCreateViewHolder: (parent: ViewGroup) -> VH,
    onBindViewHolder: (holder: VH, item: I) -> Unit,

    /* Optional arguments */
    onItemSelect: ((item: I) -> Unit)? = null,
) {
    init {
        easyScrollPicker.configure(
            EasyScrollCallbacks(
                onCreateViewHolder = onCreateViewHolder,
                onBindViewHolder = onBindViewHolder,
                onItemSelect = onItemSelect,
            )
        )
    }

    fun setItems(items: List<I>)  = easyScrollPicker.setItems(items)

    fun setInitialPosition(position: Int) {
        if (position > 0)
            easyScrollPicker.initialPos = position
    }

}