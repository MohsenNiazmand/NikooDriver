package com.akaf.nikoodriver.data.responses.updateResponse

data class UpdateData(
    val downloadURI: String,
    val forceUpdate: Boolean,
    val latestVersion: String,
    val outDated: Boolean
)