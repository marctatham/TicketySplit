package com.marctatham.ticketysplit.common.csv

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.io.File

fun parseCsvFile(filePath: String): List<Map<String, String>> =
    csvReader().readAllWithHeader(File(filePath))
