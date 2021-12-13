package com.sky.library.view

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.CompoundButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.IntDef
import com.sky.library.R
import com.sky.library.utils.PXTool
import com.google.android.material.switchmaterial.SwitchMaterial

/**
 * description ：
 * author : Derik.Wu
 * email : Derik.Wu@waclighting.com.cn
 * date : 2021/1/8
 */

private const val TEXT_MAX_LINES = 1

class SettingItemView(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : RelativeLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var tvName: TextView? = null
    private var tvDesc: TextView? = null
    private var sw: SwitchMaterial? = null
    private var divider: View? = null

    /**
     * Default value is 0
     * 0 Text and indicator.
     * 1 Switch Only.
     * 2 Text Only.
     */
    var settingType: Int = SettingType.TEXT_INDICATOR
        private set
    var name: String? = null
        set(value) {
            field = value
            tvName?.text = value
        }
    var desc: String? = null
        set(value) {
            field = value
            tvDesc?.text = value
        }
    var nameSize: Int = 20
        set(value) {
            field = value
            tvName?.textSize = value.toFloat()
        }
    var descSize: Int = 16
        set(value) {
            field = value
            tvDesc?.textSize = value.toFloat()
        }
    var nameColor: Int = Color.WHITE
        set(value) {
            field = value
            tvName?.setTextColor(value)
        }
    var descColor: Int = Color.DKGRAY
        set(value) {
            field = value
            tvDesc?.setTextColor(value)
        }
    var isDividerEnable: Boolean = true
        set(value) {
            field = value
            divider?.visibility = if (value) View.VISIBLE else View.GONE
        }
    var dividerColor: Int = Color.GRAY
        set(value) {
            field = value
            divider?.setBackgroundColor(value)
        }
    var dividerWidth: Int = 1
        set(value) {
            field = value
            divider?.layoutParams?.height = value
        }

    /**
     * Default value is 0
     * 0 Right of parent
     * 1 After name
     */
    var descPosition: Int = DescPosition.RIGHT_OF_PARENT
        private set
    var descMarginLeft: Int = 0
    var indicatorDrawable: Drawable? = null
    var indicatorDrawablePadding: Int = 0

    var nameMarginLeft: Int = 0
    var indicatorMarginRight: Int = 0
    var dividerMarginRight: Int = 0
    var dividerMarginLeft: Int = 0
    var switchMarginRight: Int = 0
    var switchChecked: Boolean = false
        set(value) {
            field = value
            sw?.isChecked = value
        }
        get() {
            return sw?.isChecked ?: false
        }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingItemView, defStyleAttr, defStyleRes)
            settingType = typedArray.getInt(R.styleable.SettingItemView_settingType, settingType)
            name = typedArray.getString(R.styleable.SettingItemView_settingName)
            desc = typedArray.getString(R.styleable.SettingItemView_settingDesc)
            nameSize = typedArray.getDimensionPixelSize(R.styleable.SettingItemView_settingNameSize, PXTool.sp2px(context, nameSize))
            descSize = typedArray.getDimensionPixelSize(R.styleable.SettingItemView_settingDescSize, PXTool.sp2px(context, descSize))
            nameColor = typedArray.getColor(R.styleable.SettingItemView_settingNameColor, nameColor)
            descColor = typedArray.getColor(R.styleable.SettingItemView_settingDescColor, descColor)
            isDividerEnable = typedArray.getBoolean(R.styleable.SettingItemView_settingDividerEnable, isDividerEnable)
            dividerColor = typedArray.getColor(R.styleable.SettingItemView_settingDividerColor, dividerColor)
            dividerWidth = typedArray.getDimensionPixelSize(R.styleable.SettingItemView_settingDividerWidth, dividerWidth)
            descPosition = typedArray.getInt(R.styleable.SettingItemView_settingDescPosition, descPosition)
            descMarginLeft = typedArray.getDimensionPixelSize(R.styleable.SettingItemView_settingDescMarginLeft, descMarginLeft)
            indicatorDrawable = typedArray.getDrawable(R.styleable.SettingItemView_settingIndicatorDrawable)

            val indicatorDrawableTint = typedArray.getColor(R.styleable.SettingItemView_settingIndicatorDrawableTint, Color.WHITE)
            val indicatorDrawableModeValue = typedArray.getInt(R.styleable.SettingItemView_settingIndicatorDrawableTintMode, PorterDuff.Mode.SRC_IN.ordinal)
            indicatorDrawable?.setTint(indicatorDrawableTint)
            indicatorDrawable?.setTintMode(parseTintMode(indicatorDrawableModeValue, PorterDuff.Mode.SRC_IN))

            indicatorDrawablePadding = typedArray.getDimensionPixelSize(R.styleable.SettingItemView_settingIndicatorDrawablePadding, indicatorDrawablePadding)
            nameMarginLeft = typedArray.getDimensionPixelSize(R.styleable.SettingItemView_settingNameMarginLeft, nameMarginLeft)
            indicatorMarginRight = typedArray.getDimensionPixelSize(R.styleable.SettingItemView_settingIndicatorMarginRight, indicatorMarginRight)
            dividerMarginRight = typedArray.getDimensionPixelSize(R.styleable.SettingItemView_settingDividerMarginRight, dividerMarginRight)
            dividerMarginLeft = typedArray.getDimensionPixelSize(R.styleable.SettingItemView_settingDividerMarginLeft, dividerMarginLeft)
            switchMarginRight = typedArray.getDimensionPixelSize(R.styleable.SettingItemView_settingSwitchMarginRight, switchMarginRight)
            switchChecked = typedArray.getBoolean(R.styleable.SettingItemView_settingSwitchMarginRight, switchChecked)
            typedArray.recycle()
        }
        isClickable = true
        isFocusable = true
        initLayout()
    }

    private fun initLayout() {
        //default
        initName()
        initDivider()
        when (settingType) {
            SettingType.TEXT_INDICATOR -> {
                initDesc(LayoutParams.MATCH_PARENT)
                initRightIndicator()
            }
            SettingType.SWITCH_ONLY -> {
                initSwitch()
            }
            SettingType.TEXT_ONLY -> {
                initDesc(LayoutParams.WRAP_CONTENT)
            }
        }
    }

    fun relayout(@SettingType type: Int, @DescPosition descPos: Int?) {
        this.removeAllViewsInLayout()
        settingType = type
        descPos?.let {
            descPosition = it
        }
        initLayout()
    }

    private fun initName() {
        tvName = TextView(context).apply {
            id = View.generateViewId()
            text = name
            maxLines = TEXT_MAX_LINES
            ellipsize = TextUtils.TruncateAt.END
            setTextColor(nameColor)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, nameSize.toFloat())
            LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT).also {
                it.addRule(ALIGN_PARENT_LEFT)
                it.addRule(CENTER_VERTICAL)
                it.leftMargin = nameMarginLeft
                layoutParams = it
            }
            gravity = Gravity.CENTER_VERTICAL
        }
        this.addView(tvName)
    }

    private fun initDesc(width: Int) {
        tvDesc = TextView(context).apply {
            text = desc
            maxLines = TEXT_MAX_LINES
            ellipsize = TextUtils.TruncateAt.END
            setTextColor(descColor)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, descSize.toFloat())

            LayoutParams(width, LayoutParams.WRAP_CONTENT).also {
                it.addRule(CENTER_VERTICAL)
                it.leftMargin = descMarginLeft
                it.rightMargin = indicatorMarginRight

                if (descPosition == DescPosition.AFTER_NAME) {
                    it.addRule(RIGHT_OF, tvName?.id ?: TRUE)
                    gravity = Gravity.START
                } else {
                    gravity = Gravity.END
                    it.addRule(ALIGN_PARENT_RIGHT)
                    if (width == LayoutParams.MATCH_PARENT) {
                        it.addRule(RIGHT_OF, tvName?.id ?: TRUE)
                    }
                }
                layoutParams = it
            }
        }
        this.addView(tvDesc)
    }

    private fun initRightIndicator() {
        tvDesc?.apply {
            compoundDrawablePadding = indicatorDrawablePadding
            setCompoundDrawablesWithIntrinsicBounds(null, null, indicatorDrawable, null)
        }
    }

    private fun initSwitch() {
        sw = SwitchMaterial(context).apply {
            LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT).also {
                it.addRule(ALIGN_PARENT_RIGHT)
                it.addRule(CENTER_VERTICAL)
                it.rightMargin = switchMarginRight
                layoutParams = it
            }
//            thumbDrawable = context.resources.getDrawable(R.drawable.common_selector_switch_thumb)
//            trackDrawable = context.resources.getDrawable(R.drawable.common_selector_switch_track)
            gravity = Gravity.CENTER_VERTICAL
            isChecked = switchChecked
        }
        this.addView(sw)
    }

    private fun initDivider() {
        divider = View(context).apply {
            setBackgroundColor(dividerColor)
            LayoutParams(LayoutParams.MATCH_PARENT, dividerWidth).also {
                it.addRule(ALIGN_PARENT_BOTTOM)
                it.addRule(CENTER_HORIZONTAL)
                it.leftMargin = dividerMarginLeft
                it.rightMargin = dividerMarginRight
                layoutParams = it
            }
        }
        this.addView(divider)
        divider?.visibility = if (isDividerEnable) View.VISIBLE else View.GONE
    }

    fun setOnDescClickListener(listener: OnClickListener?) {
        tvDesc?.setOnClickListener(listener)
    }

    fun setOnCheckedChangeListener(listener: CompoundButton.OnCheckedChangeListener?) {
        sw?.setOnCheckedChangeListener(listener)
    }

    private fun parseTintMode(value: Int, defaultMode: PorterDuff.Mode): PorterDuff.Mode {
        return when (value) {
            3 -> PorterDuff.Mode.SRC_OVER
            5 -> PorterDuff.Mode.SRC_IN
            9 -> PorterDuff.Mode.SRC_ATOP
            14 -> PorterDuff.Mode.MULTIPLY
            15 -> PorterDuff.Mode.SCREEN
            16 -> PorterDuff.Mode.ADD
            else -> defaultMode
        }
    }
}

@IntDef(SettingType.TEXT_INDICATOR, SettingType.SWITCH_ONLY, SettingType.TEXT_ONLY)
annotation class SettingType {
    companion object {
        const val TEXT_INDICATOR = 0
        const val SWITCH_ONLY = 1
        const val TEXT_ONLY = 2
    }
}

@IntDef(DescPosition.RIGHT_OF_PARENT, DescPosition.AFTER_NAME)
annotation class DescPosition {
    companion object {
        const val RIGHT_OF_PARENT = 0
        const val AFTER_NAME = 1
    }
}

data class SettingItemBean(
    @SettingType var settingType: Int,
    var name: String
) {
    var desc: String? = null
    var nameColor: Int? = null
    var descColor: Int? = null
    var nameSize: Int? = null
    var descSize: Int? = null
    var isDividerEnable: Boolean? = null
    var dividerColor: Int? = null
    var dividerWidth: Int? = null
    var descMarginLeft: Int? = null
    var indicatorDrawable: Drawable? = null
    var indicatorDrawablePadding: Int? = null
    var nameMarginLeft: Int? = null
    var indicatorMarginRight: Int? = null
    var switchMarginRight: Int? = null
    var dividerMarginRight: Int? = null
    var dividerMarginLeft: Int? = null
    var isSwitchChecked: Boolean? = null

    @DescPosition
    var descPosition: Int? = null
}