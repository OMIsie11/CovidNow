package io.github.omisie11.coronatracker

import io.github.omisie11.coronatracker.data.local.model.Country
import io.github.omisie11.coronatracker.data.local.model.GlobalSummary
import io.github.omisie11.coronatracker.data.local.model.LocalSummary

val testCountry1 = Country(name = "Afghanistan", iso2 = "AF", iso3 = "AFG")
val testCountry2 = Country(name = "Albania", iso2 = "AL", iso3 = "ALB")
val testCountry3 = Country(name = "Algeria", iso2 = "DZ", iso3 = "DZA")
val testCountry4 = Country(name = "Belize", iso2 = "BZ", iso3 = "BLZ")
val testCountry5 = Country(name = "Brazil", iso2 = "BR", iso3 = "BRA")
val testCountry6 = Country(name = "Canada", iso2 = "CA", iso3 = "CAN")

val testGlobalSummary = GlobalSummary(
    confirmed = 474204,
    recovered = 115003,
    deaths = 21353,
    imageUrl = "https://covid19.mathdro.id/api/og",
    lastUpdate = "2020-03-26T09:14:15.000Z"
)

val testLocalSummary = LocalSummary(
    confirmed = 1085,
    recovered = 7,
    deaths = 15,
    lastUpdate = "2020-03-26T09:09:25.000Z"
)
