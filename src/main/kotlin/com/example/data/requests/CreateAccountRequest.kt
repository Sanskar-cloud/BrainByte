package com.example.data.requests

import com.example.data.models.UserType
import kotlinx.serialization.Serializable


@Serializable
data class CreateAccountRequest(
    val email: String,
    val username: String,
    val password: String,
    val accountType: String,
    val accountDetails: AccountDetails? = null
)

@Serializable
data class AccountDetails(
    val accountNumber: String,
    val ifscCode: String,
    val accountHolderName: String
)

