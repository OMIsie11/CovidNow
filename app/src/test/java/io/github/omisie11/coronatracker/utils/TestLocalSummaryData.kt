package io.github.omisie11.coronatracker.utils

import io.github.omisie11.coronatracker.data.local.model.LocalSummary
import io.github.omisie11.coronatracker.data.remote.model.LocalSummaryRemote
import io.github.omisie11.coronatracker.data.remote.model.SummaryRemoteItem

val testLocalSummary = LocalSummary(
    confirmed = 1085,
    recovered = 7,
    deaths = 15,
    lastUpdate = "2020-03-26T09:09:25.000Z"
)

val testLocalSummaryRemote = LocalSummaryRemote(
    SummaryRemoteItem(value = 1085, detail = ""),
    SummaryRemoteItem(value = 7, detail = ""),
    SummaryRemoteItem(value = 15, detail = ""),
    lastUpdate = "2020-03-26T09:14:15.000Z"
)
