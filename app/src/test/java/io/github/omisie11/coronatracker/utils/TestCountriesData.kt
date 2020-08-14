package io.github.omisie11.coronatracker.utils

import io.github.omisie11.coronatracker.data.local.model.Country
import io.github.omisie11.coronatracker.data.remote.model.CountriesRemote
import io.github.omisie11.coronatracker.data.remote.model.CountryRemote

val testCountry1 = Country(name = "Afghanistan", iso2 = "AF", iso3 = "AFG")
val testCountry2 = Country(name = "Albania", iso2 = "AL", iso3 = "ALB")
val testCountry3 = Country(name = "Algeria", iso2 = "DZ", iso3 = "DZA")
val testCountry4 = Country(name = "Belize", iso2 = "BZ", iso3 = "BLZ")
val testCountry5 = Country(name = "Brazil", iso2 = "BR", iso3 = "BRA")
val testCountry6 = Country(name = "Canada", iso2 = "CA", iso3 = "CAN")

val testCountryRemote1 = CountryRemote(name = "Afghanistan", iso2 = "AF", iso3 = "AFG")
val testCountryRemote2 = CountryRemote(name = "Albania", iso2 = "AL", iso3 = "ALB")
val testCountryRemote3 = CountryRemote(name = "Algeria", iso2 = "DZ", iso3 = "DZA")
val testCountryRemote4 = CountryRemote(name = "Belize", iso2 = "BZ", iso3 = "BLZ")
val testCountryRemote5 = CountryRemote(name = "Brazil", iso2 = "BR", iso3 = "BRA")
val testCountryRemote6 = CountryRemote(name = "Canada", iso2 = "CA", iso3 = "CAN")

val testCountriesRemote = CountriesRemote(
    countries = listOf(
        testCountryRemote1,
        testCountryRemote2,
        testCountryRemote3,
        testCountryRemote4,
        testCountryRemote5,
        testCountryRemote6
    )
)

val testCountriesLocalList = listOf(
    testCountry1,
    testCountry2,
    testCountry3,
    testCountry4,
    testCountry5,
    testCountry6
)

val testCountriesLocalNames = listOf(
    testCountry1.name,
    testCountry2.name,
    testCountry3.name,
    testCountry4.name,
    testCountry5.name,
    testCountry6.name
)
