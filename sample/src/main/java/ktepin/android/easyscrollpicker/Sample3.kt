package ktepin.android.easyscrollpicker

import android.animation.ValueAnimator
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue.COMPLEX_UNIT_PX
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.TextView
import ktepin.android.easyscrollpicker.databinding.ActivitySample3Binding

class Sample3 : Activity() {
    private val binding: ActivitySample3Binding by lazy {
        ActivitySample3Binding.inflate(layoutInflater)
    }

    //1st and 2th ViewHolder - Animate size
    class ItemViewHolder1and2(view: View) : EasyScrollViewHolder<Int>(view) {
        private val bigTextSize = dpToPx(view.context, 24)
        private val smallTextSize = dpToPx(view.context, 16)

        val itemText: TextView

        init {
            itemText = view.findViewById(R.id.payloadText)
            itemText.setTextSize(COMPLEX_UNIT_PX, smallTextSize)

            val size = ValueAnimator.ofFloat(smallTextSize, bigTextSize).apply {
                addUpdateListener {
                    itemText.setTextSize(COMPLEX_UNIT_PX, animatedValue as Float)
                }
                interpolator = AccelerateInterpolator(1.5f) //Configure some interpolator
            }
            applyAnimations(mapOf(0 to size))
        }
    }

    //3th ViewHolder - Animate size & Rotation
    class ItemViewHolder3(view: View) : EasyScrollViewHolder<Int>(view) {
        private val bigTextSize = dpToPx(view.context, 24)
        private val smallTextSize = dpToPx(view.context, 16)

        val itemText: TextView

        init {
            itemText = view.findViewById(R.id.payloadText)
            itemText.setTextSize(COMPLEX_UNIT_PX, smallTextSize)

            val animatorSize = ValueAnimator.ofFloat(smallTextSize, bigTextSize).apply {
                addUpdateListener {
                    itemText.setTextSize(COMPLEX_UNIT_PX, animatedValue as Float)
                }
                interpolator = AccelerateInterpolator(1.5f) //Configure some interpolator
            }
            val animatorAppear = ValueAnimator.ofFloat(0f, 360f).apply {
                addUpdateListener {
                    itemText.rotation = animatedValue as Float
                }
            }
            applyAnimations(mapOf(
                0 to animatorSize,
                1 to animatorAppear
            ))
        }
    }

    //3th ViewHolder - Animate size & Alpha
    class ItemViewHolder4(view: View) : EasyScrollViewHolder<Int>(view) {
        private val bigTextSize = dpToPx(view.context, 24)
        private val smallTextSize = dpToPx(view.context, 16)

        val itemText: TextView

        init {
            itemText = view.findViewById(R.id.payloadText)
            itemText.setTextSize(COMPLEX_UNIT_PX, smallTextSize)

            val animatorSize = ValueAnimator.ofFloat(smallTextSize, bigTextSize).apply {
                addUpdateListener {
                    itemText.setTextSize(COMPLEX_UNIT_PX, animatedValue as Float)
                }
                interpolator = AccelerateInterpolator(1.5f) //Configure some interpolator
            }
            val animatorAppear = ValueAnimator.ofFloat(0.4f, 1f).apply {
                addUpdateListener {
                    itemText.alpha = animatedValue as Float
                }
            }
            applyAnimations(mapOf(
                0 to animatorSize,
                1 to animatorAppear
            ))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initEasyPicker1()
        initEasyPicker2()
        initEasyPicker3()
        initEasyPicker4()

        setContentView(binding.root)
    }

    private fun initEasyPicker1(){
        // in generic pass <YOUR_CUSTOM_VIEW_HOLDER, PAYLOAD_TYPE>
        val scrollPickerManager = EasyScrollManager<ItemViewHolder1and2, Int>(
            easyScrollPicker = binding.easyScrollPicker1,
            onCreateViewHolder = { parent ->
                val view = LayoutInflater.from(parent.context).inflate(R.layout.sample_item3, parent, false)
                ItemViewHolder1and2(view)
            },
            onBindViewHolder = { holder, item ->
                holder.itemText.text = item.toString()
            },
            onItemSelect = {
                //your logic on item select
            }
        )

        val dataset = (1..100).toList()
        scrollPickerManager.setItems(dataset)
        scrollPickerManager.setInitialPosition(1)
    }

    private fun initEasyPicker2(){
        val scrollPickerManager = EasyScrollManager<ItemViewHolder1and2, Int>(
            easyScrollPicker = binding.easyScrollPicker2,
            onCreateViewHolder = { parent ->
                val view = LayoutInflater.from(parent.context).inflate(R.layout.sample_item3, parent, false)
                ItemViewHolder1and2(view)
            },
            onBindViewHolder = { holder, item ->
                holder.itemText.text = item.toString()
            },
            onItemSelect = {
                //your logic on item select
            }
        )

        val dataset = (1..100).toList()
        scrollPickerManager.setItems(dataset)
        scrollPickerManager.setInitialPosition(2)
    }

    private fun initEasyPicker3(){
        val scrollPickerManager = EasyScrollManager<ItemViewHolder3, Int>(
            easyScrollPicker = binding.easyScrollPicker3,
            onCreateViewHolder = { parent ->
                val view = LayoutInflater.from(parent.context).inflate(R.layout.sample_item3, parent, false)
                ItemViewHolder3(view)
            },
            onBindViewHolder = { holder, item ->
                holder.itemText.text = item.toString()
            },
            onItemSelect = {
                //your logic on item select
            }
        )

        val dataset = (1..100).toList()
        scrollPickerManager.setItems(dataset)
        scrollPickerManager.setInitialPosition(2)
    }

    private fun initEasyPicker4(){
        val scrollPickerManager = EasyScrollManager<ItemViewHolder4, Int>(
            easyScrollPicker = binding.easyScrollPicker4,
            onCreateViewHolder = { parent ->
                val view = LayoutInflater.from(parent.context).inflate(R.layout.sample_item3, parent, false)
                ItemViewHolder4(view)
            },
            onBindViewHolder = { holder, item ->
                holder.itemText.text = item.toString()
            },
            onItemSelect = {
                //your logic on item select
            }
        )

        val dataset = (1..100).toList()
        scrollPickerManager.setItems(dataset)
        scrollPickerManager.setInitialPosition(3)
    }
}