package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MockTestReportDao {
    @Query("SELECT * FROM mock_test_reports ORDER BY timestamp DESC")
    fun getAllReports(): Flow<List<MockTestReport>>

    @Query("SELECT * FROM mock_test_reports WHERE studentMobile = :mobile ORDER BY timestamp DESC")
    fun getReportsByStudent(mobile: String): Flow<List<MockTestReport>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: MockTestReport): Long

    @Query("UPDATE mock_test_reports SET reportedViaSms = :reported WHERE id = :id")
    suspend fun updateSmsStatus(id: Int, reported: Boolean)

    @Query("UPDATE mock_test_reports SET reportedViaWhatsapp = :reported WHERE id = :id")
    suspend fun updateWhatsappStatus(id: Int, reported: Boolean)
}
