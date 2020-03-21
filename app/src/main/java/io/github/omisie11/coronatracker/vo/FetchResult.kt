package io.github.omisie11.coronatracker.vo

/**
 * Class representing result of data fetching
 * Used to show snackbar with error
 */
enum class FetchResult {
    SERVER_ERROR,
    NETWORK_ERROR,
    UNEXPECTED_ERROR,
    OK
}
