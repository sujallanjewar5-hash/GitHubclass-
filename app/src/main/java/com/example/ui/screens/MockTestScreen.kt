package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Rule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.utils.BackPressHandler
import com.example.ui.theme.EduBlueDark
import com.example.ui.theme.EduBluePrimary
import com.example.ui.theme.EduGoldAccent
import com.example.ui.theme.EduTealAccent
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MockTestScreen(
    viewModel: MainViewModel,
    onTestSubmitted: () -> Unit,
    onQuit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activeTest by viewModel.activeTest.collectAsState()
    val activeIndex by viewModel.currentQuestionIndex.collectAsState()
    val selectedAnswers by viewModel.selectedAnswers.collectAsState()
    val timeLeftSeconds by viewModel.timeLeftSeconds.collectAsState()

    // Handle quit dialog request
    var showQuitDialog by remember { mutableStateOf(false) }

    // Intercept system back button to prevent accidental data loss!
    BackPressHandler {
        showQuitDialog = true
    }

    val test = activeTest ?: return

    val currentQuestion = test.questions.getOrNull(activeIndex) ?: test.questions.first()
    val totalQuestions = test.questions.size

    val minutes = timeLeftSeconds / 60
    val seconds = timeLeftSeconds % 60
    val formattedTime = String.format("%02d:%02d", minutes, seconds)

    val progress = (activeIndex + 1).toFloat() / totalQuestions.toFloat()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(test.chapterName, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { showQuitDialog = true }, modifier = Modifier.testTag("test_exit_button")) {
                        Icon(Icons.Default.Cancel, contentDescription = "Exit Test", tint = Color.White)
                    }
                },
                actions = {
                    // Timer box
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (timeLeftSeconds < 120) MaterialTheme.colorScheme.errorContainer else EduGoldAccent.copy(alpha = 0.2f)
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Alarm,
                                contentDescription = "Time",
                                modifier = Modifier.size(16.dp),
                                tint = if (timeLeftSeconds < 120) MaterialTheme.colorScheme.error else Color.White
                            )
                            Text(
                                text = formattedTime,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (timeLeftSeconds < 120) MaterialTheme.colorScheme.error else Color.White,
                                modifier = Modifier.testTag("countdown_timer_text")
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = EduBluePrimary,
                    titleContentColor = Color.White
                )
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Progress Bar and Question Tracker
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Question ${activeIndex + 1} of $totalQuestions",
                        fontWeight = FontWeight.Bold,
                        color = EduBlueDark,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "${test.questions[activeIndex].marks} Marks",
                        color = EduTealAccent,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Progress Indicator
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(CircleShape),
                    color = EduTealAccent,
                    trackColor = Color.LightGray.copy(alpha = 0.4f)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Main Question Body Card
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        color = Color.White,
                        tonalElevation = 4.dp
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            // Question header
                            Text(
                                text = currentQuestion.text,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = EduBlueDark,
                                lineHeight = 24.sp,
                                modifier = Modifier.testTag("question_text")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "CHOOSE CORRECT ANSWER",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 4.dp, bottom = 10.dp)
                    )

                    // Choices list
                    currentQuestion.options.forEachIndexed { optIdx, optText ->
                        val isSelected = selectedAnswers[currentQuestion.id] == optIdx

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clickable { viewModel.selectAnswer(currentQuestion.id, optIdx) },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) EduTealAccent.copy(alpha = 0.08f) else Color.White
                            ),
                            border = if (isSelected) {
                                BorderStroke(2.dp, EduTealAccent)
                            } else {
                                BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f))
                            }
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) EduTealAccent else Color.LightGray.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = when(optIdx) {
                                            0 -> "A"
                                            1 -> "B"
                                            2 -> "C"
                                            else -> "D"
                                        },
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = if (isSelected) Color.White else EduBlueDark
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Text(
                                    text = optText,
                                    fontSize = 14.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                    color = if (isSelected) EduTealAccent else EduBlueDark,
                                    modifier = Modifier.weight(1f)
                                )

                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Selected",
                                        tint = EduTealAccent
                                    )
                                }
                            }
                        }
                    }
                }

                // Bottom control actions
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = { viewModel.prevQuestion() },
                        enabled = activeIndex > 0,
                        modifier = Modifier.height(48.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Previous")
                    }

                    if (activeIndex == totalQuestions - 1) {
                        Button(
                            onClick = {
                                viewModel.submitTest {
                                    onTestSubmitted()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = EduGoldAccent,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .height(48.dp)
                                .testTag("submit_test_button")
                        ) {
                            Text("Submit Test", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
                        }
                    } else {
                        Button(
                            onClick = { viewModel.nextQuestion() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = EduBluePrimary,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.height(48.dp)
                        ) {
                            Text("Next")
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }

            // Quit confirm dialog
            if (showQuitDialog) {
                AlertDialog(
                    onDismissRequest = { showQuitDialog = false },
                    confirmButton = {
                        Button(
                            onClick = {
                                showQuitDialog = false
                                onQuit()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Quit & Cancel")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showQuitDialog = false }) {
                            Text("Continue Exam")
                        }
                    },
                    icon = { Icon(Icons.Default.Rule, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(32.dp)) },
                    title = { Text("Quit Mock Test?", fontWeight = FontWeight.Bold) },
                    text = { Text("Are you sure you want to exit? Your progression for this 50-mark test will be lost completely.") },
                    shape = RoundedCornerShape(20.dp),
                    containerColor = Color.White
                )
            }
        }
    }
}
