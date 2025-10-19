package com.github.georgeh1998.simplesplit.data.dto

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionDto(
    val id: String,
    @SerialName("group_id") val groupId: String,
    @SerialName("user_id") val userId: String?,
    val amount: Double,
    val memo: String?,
    val date: LocalDate,
    @SerialName("created_at") val createdAt: LocalDateTime,
)
