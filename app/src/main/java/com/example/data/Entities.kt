package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val mobile: String, // mobile is unique
    val name: String,
    val passwordHash: String,
    val currentClass: String, // Dropdown: "5th", "6th", "7th", "8th", "9th", "10th"
    val board: String // Dropdown: "Maharashtra State Board", "CBSE"
) : Serializable

@Entity(tableName = "mock_test_reports")
data class MockTestReport(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val studentName: String,
    val studentMobile: String,
    val classSelected: String,
    val boardSelected: String,
    val chapterName: String,
    val score: Int,
    val totalMarks: Int = 50,
    val timestamp: Long = System.currentTimeMillis(),
    val reportedViaSms: Boolean = false,
    val reportedViaWhatsapp: Boolean = false
) : Serializable
