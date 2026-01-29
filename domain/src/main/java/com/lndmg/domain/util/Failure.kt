package com.lndmg.domain.util

sealed class Failure {
    object NetworkError : Failure()
    object ServerError : Failure()
    object LocationNotFound : Failure()
    object UnknownError : Failure()
    object NoData : Failure()
}