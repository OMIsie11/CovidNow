package io.github.omisie11.coronatracker.util

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import io.github.omisie11.coronatracker.R
import kotlinx.android.synthetic.main.compound_single_stat.view.*

/**
 * Compound view that shows single statistic with icon and title
 * Used in Global and Local Summary screens
 */
class SingleStatView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.compound_single_stat, this, true)
        orientation = VERTICAL

        context.theme.obtainStyledAttributes(
            attrs, R.styleable.SingleStatView, 0, 0
        ).apply {
            try {
                stat_title.text =
                    getText(R.styleable.SingleStatView_title) ?: context.getString(R.string.title)
                stat_value.text =
                    getText(R.styleable.SingleStatView_statValue) ?: context.getString(
                        R.string.value
                    )
                stat_icon.setImageDrawable(getDrawable(R.styleable.SingleStatView_icon))
                val iconColor = getColor(
                    R.styleable.SingleStatView_iconColor,
                    ContextCompat.getColor(context, R.color.pie_chart_yellow)
                )
                stat_icon.setColorFilter(iconColor, PorterDuff.Mode.SRC_IN)
            } finally {
                recycle()
            }
        }
    }
}
