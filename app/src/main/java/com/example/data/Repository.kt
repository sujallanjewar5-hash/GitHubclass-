package com.example.data

import kotlinx.coroutines.flow.Flow

class Repository(private val db: AppDatabase) {
    private val userDao = db.userDao()
    private val reportDao = db.mockTestReportDao()

    suspend fun signupUser(user: User): Boolean {
        val existing = userDao.getUserByMobile(user.mobile)
        return if (existing == null) {
            userDao.insertUser(user)
            true
        } else {
            false
        }
    }

    suspend fun loginUser(mobile: String, passwordHash: String): User? {
        val user = userDao.getUserByMobile(mobile)
        return if (user != null && user.passwordHash == passwordHash) {
            user
        } else {
            null
        }
    }

    suspend fun saveReport(report: MockTestReport): Long {
        return reportDao.insertReport(report)
    }

    fun getHistory(mobile: String): Flow<List<MockTestReport>> {
        return reportDao.getReportsByStudent(mobile)
    }

    suspend fun updateSmsStatus(reportId: Int, reported: Boolean) {
        reportDao.updateSmsStatus(reportId, reported)
    }

    suspend fun updateWhatsappStatus(reportId: Int, reported: Boolean) {
        reportDao.updateWhatsappStatus(reportId, reported)
    }
}
