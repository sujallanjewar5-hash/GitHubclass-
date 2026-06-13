package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.History
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppData
import com.example.data.MockChapterTest
import com.example.data.MockTestReport
import com.example.ui.theme.*
import com.example.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: MainViewModel,
    onNavigateToMockTest: (MockChapterTest) -> Unit,
    onNavigateToPYQs: () -> Unit,
    onNavigateToStudyMaterials: () -> Unit,
    onNavigateToReportResult: (MockTestReport) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val reports by viewModel.allReports.collectAsState()
    val context = LocalContext.current

    val studentClass = currentUser?.currentClass ?: "10th"
    val studentBoard = currentUser?.board ?: "Maharashtra State Board"

    val matchedTests = remember(studentClass, studentBoard) {
        AppData.MockTests.filter { it.className == studentClass && it.board == studentBoard }
    }
    val primaryMockTest = matchedTests.firstOrNull() ?: AppData.MockTests.first()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(ProfessionalBackground)
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Header Section (Professional Polish Theme)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Gitai Coaching".uppercase(),
                            color = ProfessionalPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 1.2.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Hello, ${currentUser?.name?.split(" ")?.firstOrNull() ?: "Rahul"}!",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 24.sp,
                            color = ProfessionalOnBackground
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Class $studentClass • $studentBoard",
                            fontSize = 13.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Quick logout
                        IconButton(
                            onClick = onLogout,
                            modifier = Modifier.testTag("logout_icon_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Logout,
                                contentDescription = "Log out",
                                tint = Color.Gray.copy(alpha = 0.8f)
                            )
                        }

                        // Profile Circle
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(ProfessionalPrimaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .background(ProfessionalPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = (currentUser?.name ?: "R").take(1).uppercase(),
                                    color = Color.White,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                }
            }

            // Director Guidance / Credentials Banner
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 6.dp),
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, ProfessionalOutline)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(ProfessionalTertiaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stars,
                                contentDescription = null,
                                tint = EduGoldAccent,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Director: Makul Shende Sir",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = ProfessionalOnBackground
                            )
                            Text(
                                text = "Under B.Sc., B.Ed. professional academic guidance",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }

            // Primary Highlight: Daily 50-Mark Mock Test
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 10.dp)
                        .clickable { onNavigateToMockTest(primaryMockTest) }
                        .testTag("hub_mock_test_card"),
                    shape = RoundedCornerShape(28.dp),
                    color = ProfessionalPrimaryContainer,
                    border = BorderStroke(1.dp, Color(0xFFADC9F0))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        // Ambient radial pattern on the bottom right
                        Box(
                            modifier = Modifier
                                .size(140.dp)
                                .align(Alignment.BottomEnd)
                                .offset(x = 42.dp, y = 42.dp)
                                .background(Color(0xFFADC9F0).copy(alpha = 0.35f), CircleShape)
                        )

                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(bottom = 8.dp)
                            ) {
                                Surface(
                                    color = ProfessionalPrimary,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "LIVE NOW",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                                Text(
                                    text = "Daily Performance Engine",
                                    color = ProfessionalPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Text(
                                text = "50-Mark Mock Test",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 22.sp,
                                color = ProfessionalOnPrimaryContainer
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Topic: ${primaryMockTest.subject} • ${primaryMockTest.chapterName}",
                                fontSize = 14.sp,
                                color = ProfessionalSecondaryText,
                                lineHeight = 18.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = { onNavigateToMockTest(primaryMockTest) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ProfessionalPrimary,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                            ) {
                                Text(
                                    text = "Begin Assessment",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }
            }

            // Secondary Hubs Grid (Design layout equivalent)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Hub 2: PYQs
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigateToPYQs() }
                            .testTag("hub_pyq_card"),
                        shape = RoundedCornerShape(24.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, ProfessionalOutline)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(ProfessionalTertiaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Description,
                                    contentDescription = null,
                                    tint = EduGoldAccent,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "PYQ Library",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = ProfessionalOnBackground
                                )
                                Text(
                                    text = "2018-2025 Papers",
                                    fontSize = 10.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(top = 1.dp)
                                )
                            }
                        }
                    }

                    // Hub 3: Study Material / E-Books
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigateToStudyMaterials() }
                            .testTag("hub_study_materials_card"),
                        shape = RoundedCornerShape(24.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, ProfessionalOutline)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(ProfessionalSecondaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Book,
                                    contentDescription = null,
                                    tint = ProfessionalPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "E-Books",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = ProfessionalOnBackground
                                )
                                Text(
                                    text = "NCERT & Board Notes",
                                    fontSize = 10.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(top = 1.dp)
                                )
                            }
                        }
                    }
                }
            }

            // More Quizzes Title
            if (matchedTests.size > 1) {
                item {
                    Text(
                        text = "More Chapter-wise Quizzes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = ProfessionalOnBackground,
                        modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 8.dp)
                    )
                }
                items(matchedTests.drop(1)) { extraTest ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .clickable { onNavigateToMockTest(extraTest) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, ProfessionalOutline.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(ProfessionalSecondaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Quiz,
                                    contentDescription = null,
                                    tint = ProfessionalPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = extraTest.chapterName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = ProfessionalOnBackground
                                )
                                Text(
                                    text = "${extraTest.subject} • ${extraTest.totalMarks} Marks",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "Start",
                                tint = Color.Gray
                            )
                        }
                    }
                }
            }

            // Recent Progress / Stats (Latest Report of the Student)
            if (reports.isNotEmpty()) {
                val latestReport = reports.first()

                item {
                    val percentage = (latestReport.score.toFloat() / latestReport.totalMarks.toFloat()) * 100f
                    val performanceText = when {
                        percentage >= 90 -> "Excellent"
                        percentage >= 75 -> "Great Work"
                        percentage >= 50 -> "Good Effort"
                        else -> "Practice"
                    }
                    val errorColor = MaterialTheme.colorScheme.error
                    val errorContainerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.08f)
                    val badgeColor = when {
                        percentage >= 80 -> Color(0xFF16A34A) // Green 600
                        percentage >= 50 -> EduGoldAccent
                        else -> errorColor
                    }
                    val badgeBg = when {
                        percentage >= 80 -> Color(0xFFF0FDF4) // Green 50
                        percentage >= 50 -> EduGoldAccent.copy(alpha = 0.08f)
                        else -> errorContainerColor
                    }

                    val dateFormatted = remember(latestReport.timestamp) {
                        SimpleDateFormat("MMM d, h:mm a", Locale.getDefault()).format(Date(latestReport.timestamp))
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Text(
                            text = "Latest Performance Report",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = ProfessionalOnBackground,
                            modifier = Modifier.padding(start = 20.dp, bottom = 8.dp)
                        )

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 6.dp)
                                .clickable { onNavigateToReportResult(latestReport) },
                            shape = RoundedCornerShape(24.dp),
                            color = ProfessionalSurfaceVariant,
                            border = BorderStroke(1.dp, ProfessionalOutline)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "LATEST REPORT",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Gray,
                                        letterSpacing = 1.sp
                                    )

                                    Surface(
                                        color = Color.White,
                                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                        shape = RoundedCornerShape(100.dp)
                                    ) {
                                        Text(
                                            text = if (latestReport.reportedViaSms || latestReport.reportedViaWhatsapp) "Sent to Shende Sir" else "Ready to Report",
                                            color = if (latestReport.reportedViaSms || latestReport.reportedViaWhatsapp) ProfessionalPrimary else Color.Gray,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    verticalAlignment = Alignment.Bottom,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "${latestReport.score}",
                                        fontSize = 42.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = ProfessionalOnBackground,
                                        lineHeight = 42.sp
                                    )
                                    Text(
                                        text = " / ${latestReport.totalMarks}",
                                        fontSize = 18.sp,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(bottom = 6.dp, start = 2.dp)
                                    )

                                    Spacer(modifier = Modifier.weight(1f))

                                    Surface(
                                        color = badgeBg,
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.padding(bottom = 6.dp)
                                    ) {
                                        Text(
                                            text = performanceText,
                                            color = badgeColor,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "${latestReport.classSelected} • ${latestReport.chapterName} • $dateFormatted",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )

                                Spacer(modifier = Modifier.height(16.dp))
                                Divider(color = Color.LightGray.copy(alpha = 0.4f), thickness = 1.dp)
                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(horizontalArrangement = Arrangement.spacedBy((-6).dp)) {
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFE2E8F0))
                                        )
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFCBD5E1))
                                        )
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(ProfessionalPrimary),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "+14",
                                                color = Color.White,
                                                fontSize = 8.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                    Text(
                                        text = "14 Students taking test now",
                                        fontSize = 11.sp,
                                        color = Color.Gray,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }

                // If they have multiple reports, show history list
                if (reports.size > 1) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Past Test History",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = ProfessionalOnBackground
                            )
                        }
                    }

                    items(reports.drop(1)) { item ->
                        val datePFormatted = remember(item.timestamp) {
                            SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(item.timestamp))
                        }
                        val pct = (item.score.toFloat() / item.totalMarks.toFloat()) * 100f
                        val performanceColor = when {
                            pct >= 80 -> EduTealAccent
                            pct >= 40 -> EduGoldAccent
                            else -> MaterialTheme.colorScheme.error
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 5.dp)
                                .clickable { onNavigateToReportResult(item) },
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, ProfessionalOutline.copy(alpha = 0.4f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(performanceColor.copy(alpha = 0.1f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = "${item.score}",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = performanceColor
                                        )
                                        Divider(modifier = Modifier.width(16.dp), color = performanceColor.copy(alpha = 0.3f))
                                        Text(
                                            text = "${item.totalMarks}",
                                            fontSize = 9.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.chapterName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = ProfessionalOnBackground,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = datePFormatted,
                                        fontSize = 11.sp,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }

                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = "Read",
                                    tint = Color.Gray
                                )
                            }
                        }
                    }
                }
            } else {
                // Empty state card
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        shape = RoundedCornerShape(24.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, ProfessionalOutline)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.FactCheck,
                                contentDescription = null,
                                tint = Color.Gray.copy(alpha = 0.4f),
                                modifier = Modifier.size(44.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "No Tests Submitted Yet",
                                fontWeight = FontWeight.Bold,
                                color = ProfessionalOnBackground,
                                fontSize = 14.sp
                            )
                            Text(
                                "Solve your first 50-Mark Chapter-wise test to view and report your scores instantly to Director Sir.",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
