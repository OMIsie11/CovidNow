package io.github.omisie11.coronatracker.vo

/**
 * Possible labels in Global Summary Pie chart
 * enum used to enable localization in UI layer -> Fragment
 */
enum class GlobalPieChartLabels(englishLabel: String) {
    CONFIRMED("confirmed"),
    RECOVERED("recovered"),
    DEATHS("deaths")
}
