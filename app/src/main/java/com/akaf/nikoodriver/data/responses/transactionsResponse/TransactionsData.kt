package com.akaf.nikoodriver.data.responses.transactionsResponse

data class TransactionsData(
    val admin_id: Any,
    val afterAmount: Any?,
    val amount: String,
    val beforeAmount: Any?,
    val createdAt: String?,
    val driver_id: Int?,
    val id: Int,
    val invoiceNumber: Any?,
    val ip: String,
    val photo_id: Any?,
    val reason: String?,
    val transactionReferenceID: Any?,
    val trip_id: Any?,
    val type: String?,
    val updatedAt: String?,
    val user_id: Any?
)