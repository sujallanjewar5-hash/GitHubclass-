package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MockTestReport
import com.example.ui.theme.EduBlueDark
import com.example.ui.theme.EduBluePrimary
import com.example.ui.theme.EduGoldAccent
import com.example.ui.theme.EduTealAccent
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    viewModel: MainViewModel,
    customReport: MockTestReport? = null, // If null, display newest saved score from ViewModel
    onNavigateHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val newestReport by viewModel.latestSavedReport.collectAsState()
    val context = LocalContext.current

    val report = customReport ?: newestReport

    if (report == null) {
        // Fallback safety empty state
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Result Dashboard", fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = EduBluePrimary, titleContentColor = Color.White)
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = EduTealAccent)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Fetching mock test results...", color = Color.Gray)
                }
            }
        }
        return
    }

    val percentage = (report.score.toFloat() / report.totalMarks.toFloat()) * 100f
    val performanceText = when {
        percentage >= 90 -> "EXCELLENT PERFORMANCE!"
        percentage >= 75 -> "GREAT WORK!"
        percentage >= 50 -> "GOOD EFFORT!"
        else -> "KEEP PRACTICING!"
    }
    val themeColor = when {
        percentage >= 80 -> EduTealAccent
        percentage >= 50 -> EduGoldAccent
        else -> MaterialTheme.colorScheme.error
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Test Report Card", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = EduBluePrimary,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 0.dp)
            ) {
                Button(
                    onClick = onNavigateHome,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EduBluePrimary,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(52.dp)
                        .testTag("result_home_button"),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(imageVector = Icons.Default.Home, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Back to Dashboard", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Score Circle Gauge representation in M3 styling
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(themeColor.copy(alpha = 0.12f), themeColor.copy(alpha = 0.02f))
                        )
                    )
                    .border(2.dp, themeColor.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${report.score}",
                        fontSize = 54.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = themeColor,
                        modifier = Modifier.testTag("score_display_value")
                    )
                    Divider(
                        modifier = Modifier
                            .width(80.dp)
                            .padding(vertical = 4.dp),
                        color = themeColor.copy(alpha = 0.3f),
                        thickness = 2.dp
                    )
                    Text(
                        text = "OUT OF ${report.totalMarks}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = performanceText,
                color = themeColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )

            Text(
                text = "Score percentage: ${percentage.toInt()}%",
                color = Color.Gray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // CRITICAL SECTION: SCORE REPORT ACTIONS FOR PARENTS/DIRECTOR
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                shadowElevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "MANDATORY DIRECTOR SCORE ALERT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = EduGoldAccent,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Text(
                        text = "Upon clicking submit, report this scorecard to director Makul Shende Sir (+91 95456 51407). Choose your automated delivery channel below:",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Deliver channels row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Deliver via Whatsapp (deep link routing)
                        Button(
                            onClick = { viewModel.shareViaWhatsapp(context, report) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = EduTealAccent,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1.5f)
                                .height(48.dp)
                                .testTag("send_report_whatsapp_button")
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null, size = 16.dp, tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("WhatsApp", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }

                        // Deliver via SMS (intent layout)
                        Button(
                            onClick = { viewModel.shareViaSms(context, report) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = EduBluePrimary,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1.5f)
                                .height(48.dp)
                                .testTag("send_report_sms_button")
                        ) {
                            Icon(Icons.Outlined.Message, contentDescription = null, size = 16.dp, tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Sms Text", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Real-time update feedback badges depending on whether they shared
                    if (report.reportedViaSms || report.reportedViaWhatsapp) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            color = EduTealAccent.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = EduTealAccent, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Automated score recorded as reported successfully.",
                                    fontSize = 11.sp,
                                    color = EduTealAccent,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Text Payload presentation envelope
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = EduBlueDark.copy(alpha = 0.03f),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "PREVIEW OF SHARING PAYLOAD",
                            fontSize = 10.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(Icons.Default.Receipt, contentDescription = null, size = 14.dp, tint = Color.Gray)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, RoundedCornerShape(8.dp))
                            .border(1.dp, Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = viewModel.getReportMessage(report),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = EduBlueDark,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
