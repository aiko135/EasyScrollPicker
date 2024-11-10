package ktepin.android.easyscrollpicker.lib

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ktepin.android.easyscrollpicker.lib.exception.ItemsOnScreenEvenException

/*
*  This abstraction represents logic for drawing preview in Android Studio editor.
* */
abstract class AEasyScrollPicker : RecyclerView {
    protected abstract val orientation: Int
    protected abstract val itemsOnScreen: Int //only odd numbers allowed
    private val elementPaint = Paint(PREVIEW_ELEM_COLOR)
    private val elementsToDraw = mutableListOf<Rect>()

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    )

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        if (isInEditMode){
            val modeWidth = MeasureSpec.getMode(widthSpec)
            val modeHeight = MeasureSpec.getMode(heightSpec)

            val overrideWidth = orientation == LinearLayoutManager.VERTICAL && modeWidth != MeasureSpec.EXACTLY
            val overrideHeight = orientation == LinearLayoutManager.HORIZONTAL && modeHeight != MeasureSpec.EXACTLY

            super.onMeasure(
                if (overrideWidth) MeasureSpec.makeMeasureSpec(PREVIEW_MIN_SIZE_PX, MeasureSpec.EXACTLY) else widthSpec,
                if (overrideHeight) MeasureSpec.makeMeasureSpec(PREVIEW_MIN_SIZE_PX, MeasureSpec.EXACTLY) else heightSpec
            )
        }else{
            super.onMeasure(widthSpec, heightSpec)
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        //only odd numbers allowed
        if (itemsOnScreen % 2 == 0)
            throw ItemsOnScreenEvenException(context)

        if (isInEditMode){
//            elementsToDraw.clear()
//
//            val spacingsOnScreen = itemsOnScreen - 1
//            val elemSize = if (orientation == LinearLayoutManager.HORIZONTAL){
//                (width - (spacingsOnScreen * PREVIEW_ELEMENTS_SPACING_PX)) / itemsOnScreen
//            }else{
//                (height - (spacingsOnScreen * PREVIEW_ELEMENTS_SPACING_PX)) / itemsOnScreen
//            }
//
//            var offset = if (orientation == LinearLayoutManager.HORIZONTAL) left else top
//            for (i in 1..itemsOnScreen){
//                if (i > 1){
//                    offset += PREVIEW_ELEMENTS_SPACING_PX
//                }
//
//                val rect = if (orientation == LinearLayoutManager.HORIZONTAL){
//                    Rect(offset, top, offset + elemSize, bottom)
//                }else{
//                    Rect(left, offset, right, offset + elemSize)
//                }
//                elementsToDraw.add(rect)
//                offset += elemSize
//            }
            elementsToDraw.add(Rect(0, 0, width, height))
            elementsToDraw.add(Rect(left, top, right, bottom))
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (isInEditMode){
            canvas.drawColor(PREVIEW_BACKGROUND_COLOR)
            elementsToDraw.forEach {
                canvas.drawRect(it, elementPaint)
            }
        }
        super.onDraw(canvas)
    }

    companion object{
        private const val PREVIEW_MIN_SIZE_PX = 100
        private const val PREVIEW_ELEMENTS_SPACING_PX = 12 //spacing between schematic elements
        private const val PREVIEW_BACKGROUND_COLOR = Color.GRAY
        private const val PREVIEW_ELEM_COLOR = Color.BLACK
    }
}