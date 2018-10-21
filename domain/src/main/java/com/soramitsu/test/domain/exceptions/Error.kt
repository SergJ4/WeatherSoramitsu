package com.soramitsu.test.domain.exceptions

sealed class Error

object RefreshDataError : Error()

object NetworkConnectionError : Error()

object ResourceNotFoundError : Error()