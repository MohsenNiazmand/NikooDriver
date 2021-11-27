package com.akaf.nikoodriver.data.responses.transactionsResponse

data class TransactionsResponse(
    val `data`: List<TransactionsData>,
    val message: String
)