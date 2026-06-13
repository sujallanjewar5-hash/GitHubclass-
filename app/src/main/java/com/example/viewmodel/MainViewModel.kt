package com.example.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(private val repository: Repository) : ViewModel() {

    // --- Authentication State ---
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // --- Report History ---
    val allReports: StateFlow<List<MockTestReport>> = _currentUser
        .flatMapLatest { user ->
            if (user != null) {
                repository.getHistory(user.mobile)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Active Test State ---
    private val _activeTest = MutableStateFlow<MockChapterTest?>(null)
    val activeTest: StateFlow<MockChapterTest?> = _activeTest.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private val _selectedAnswers = MutableStateFlow<Map<Int, Int>>(emptyMap()) // Question ID -> Selected option index
    val selectedAnswers: StateFlow<Map<Int, Int>> = _selectedAnswers.asStateFlow()

    private val _timeLeftSeconds = MutableStateFlow(0)
    val timeLeftSeconds: StateFlow<Int> = _timeLeftSeconds.asStateFlow()

    private var timerJob: Job? = null

    // --- Latest Submited Test Result ---
    private val _latestSavedReport = MutableStateFlow<MockTestReport?>(null)
    val latestSavedReport: StateFlow<MockTestReport?> = _latestSavedReport.asStateFlow()

    fun clearAuthError() {
        _authError.value = null
    }

    fun registerUser(name: String, mobile: String, password: String, grade: String, board: String, onSuccess: () -> Unit) {
        if (name.isBlank() || mobile.isBlank() || password.isBlank()) {
            _authError.value = "All fields are required"
            return
        }
        if (mobile.length < 10) {
            _authError.value = "Enter a valid 10-digit mobile number"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            val success = repository.signupUser(
                User(
                    mobile = mobile,
                    name = name,
                    passwordHash = password, // Simplified for mock use
                    currentClass = grade,
                    board = board
                )
            )
            _isLoading.value = false
            if (success) {
                _authError.value = null
                loginUser(mobile, password, onSuccess)
            } else {
                _authError.value = "A user with this mobile number already exists"
            }
        }
    }

    fun loginUser(mobile: String, password: String, onSuccess: () -> Unit) {
        if (mobile.isBlank() || password.isBlank()) {
            _authError.value = "All fields are required"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            val user = repository.loginUser(mobile, password)
            _isLoading.value = false
            if (user != null) {
                _currentUser.value = user
                _authError.value = null
                onSuccess()
            } else {
                _authError.value = "Invalid mobile number or password"
            }
        }
    }

    fun logout() {
        _currentUser.value = null
        _activeTest.value = null
        _latestSavedReport.value = null
        stopTimer()
    }

    // --- Mock Test Functionality ---
    fun startTest(test: MockChapterTest) {
        _activeTest.value = test
        _currentQuestionIndex.value = 0
        _selectedAnswers.value = emptyMap()
        _timeLeftSeconds.value = test.durationMinutes * 60
        _latestSavedReport.value = null

        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_timeLeftSeconds.value > 0) {
                delay(1000)
                _timeLeftSeconds.value -= 1
            }
            // Timer expired, auto submit
            submitTest {
                // Done
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    fun selectAnswer(questionId: Int, optionIndex: Int) {
        val current = _selectedAnswers.value.toMutableMap()
        current[questionId] = optionIndex
        _selectedAnswers.value = current
    }

    fun nextQuestion() {
        val test = _activeTest.value ?: return
        if (_currentQuestionIndex.value < test.questions.lastIndex) {
            _currentQuestionIndex.value += 1
        }
    }

    fun prevQuestion() {
        if (_currentQuestionIndex.value > 0) {
            _currentQuestionIndex.value -= 1
        }
    }

    fun submitTest(onComplete: () -> Unit) {
        stopTimer()
        val test = _activeTest.value ?: return
        val user = _currentUser.value ?: return

        // Calculate score
        var score = 0
        test.questions.forEach { q ->
            val selected = _selectedAnswers.value[q.id]
            if (selected == q.correctOptionIndex) {
                score += q.marks
            }
        }

        viewModelScope.launch {
            val report = MockTestReport(
                studentName = user.name,
                studentMobile = user.mobile,
                classSelected = user.currentClass,
                boardSelected = user.board,
                chapterName = test.chapterName,
                score = score,
                totalMarks = test.totalMarks,
                timestamp = System.currentTimeMillis()
            )

            val id = repository.saveReport(report)
            val savedReport = report.copy(id = id.toInt())
            _latestSavedReport.value = savedReport
            _activeTest.value = null
            onComplete()
        }
    }

    // --- Share/Report Orchestration ---
    fun getReportMessage(report: MockTestReport): String {
        val sdf = SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.getDefault())
        val formattedDate = sdf.format(Date(report.timestamp))
        return """
            *Gitai Coaching Test Report*
            *Student Name:* ${report.studentName}
            *Class & Board:* ${report.classSelected} - ${report.boardSelected}
            *Test Category:* Chapter-wise Mock Test
            *Topic:* ${report.chapterName}
            *Score Obtained:* ${report.score}/${report.totalMarks}
            *Date/Time:* $formattedDate
        """.trimIndent()
    }

    fun shareViaSms(context: Context, report: MockTestReport) {
        val destinationNumber = "+919545651407"
        val message = getReportMessage(report)
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:$destinationNumber")
                putExtra("sms_body", message)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            viewModelScope.launch {
                repository.updateSmsStatus(report.id, true)
                // Refresh local state if it's the latest
                if (_latestSavedReport.value?.id == report.id) {
                    _latestSavedReport.value = _latestSavedReport.value?.copy(reportedViaSms = true)
                }
            }
        } catch (e: Exception) {
            // Fallback general SMS compose
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(Uri.parse("sms:$destinationNumber"), "vnd.android-dir/mms-sms")
                putExtra("sms_body", message)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    fun shareViaWhatsapp(context: Context, report: MockTestReport) {
        val destinationNumber = "919545651407" // Without '+' for Whatsapp URL
        val message = Uri.encode(getReportMessage(report))
        val url = "https://api.whatsapp.com/send?phone=$destinationNumber&text=$message"
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            viewModelScope.launch {
                repository.updateWhatsappStatus(report.id, true)
                // Refresh local state if it's the latest
                if (_latestSavedReport.value?.id == report.id) {
                    _latestSavedReport.value = _latestSavedReport.value?.copy(reportedViaWhatsapp = true)
                }
            }
        } catch (e: Exception) {
            // Fallback via general share/send intent
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getReportMessage(report))
                type = "text/plain"
                setPackage("com.whatsapp")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            try {
                context.startActivity(sendIntent)
            } catch (ex: Exception) {
                // If Whatsapp isn't installed, trigger normal share sheet
                val chooserIntent = Intent.createChooser(Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, getReportMessage(report))
                    type = "text/plain"
                }, "Send Score Report")
                chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(chooserIntent)
            }
        }
    }
}

class MainViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
