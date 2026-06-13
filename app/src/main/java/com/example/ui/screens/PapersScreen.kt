package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
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
import com.example.data.AppData
import com.example.data.PYQPaper
import com.example.ui.theme.EduBlueDark
import com.example.ui.theme.EduBluePrimary
import com.example.ui.theme.EduGoldAccent
import com.example.ui.theme.EduTealAccent
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PapersScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val studentClass = currentUser?.currentClass ?: "10th"

    // Filter papers based on class
    val classPapers = remember(studentClass) {
        AppData.PYQPapers.filter { it.className == studentClass }
    }

    // Boards & Subjects tabs for 10th Class
    var selectedBoard10th by remember { mutableStateOf("Maharashtra State Board") }
    var selectedSubject10th by remember { mutableStateOf("Maths") }

    // State for viewing a specific paper (exam simulator overlay)
    var activeViewingPaper by remember { mutableStateOf<PYQPaper?>(null) }

    // Final filtered paper list
    val filteredPapers = remember(studentClass, selectedBoard10th, selectedSubject10th, classPapers) {
        if (studentClass == "10th") {
            classPapers.filter {
                it.board == selectedBoard10th && it.subject == selectedSubject10th
            }
        } else {
            classPapers // For 5th-9th, show all available for their class (State Board)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PYQs Library - Class $studentClass", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("pyq_back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = EduBluePrimary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
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
            Column(modifier = Modifier.fillMaxSize()) {
                // Header Area with stats or tips
                Surface(
                    color = EduBluePrimary,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Solve real board standard exam question papers year-wise designed to benchmark student readiness.",
                            color = Color.White.copy(alpha = 0.82f),
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                }

                // 10th class specific navigation split tabs
                if (studentClass == "10th") {
                    // Board selector chips
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { selectedBoard10th = "Maharashtra State Board" },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedBoard10th == "Maharashtra State Board") EduBlueDark else Color.White,
                                contentColor = if (selectedBoard10th == "Maharashtra State Board") Color.White else EduBlueDark
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp)
                        ) {
                            Text("State Board", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { selectedBoard10th = "CBSE" },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedBoard10th == "CBSE") EduBlueDark else Color.White,
                                contentColor = if (selectedBoard10th == "CBSE") Color.White else EduBlueDark
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp)
                        ) {
                            Text("CBSE Board", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Subject selector row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 2.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilterChip(
                            selected = selectedSubject10th == "Maths",
                            onClick = { selectedSubject10th = "Maths" },
                            label = { Text("Mathematics") },
                            leadingIcon = {
                                if (selectedSubject10th == "Maths") {
                                    Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                }
                            }
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        FilterChip(
                            selected = selectedSubject10th == "Science",
                            onClick = { selectedSubject10th = "Science" },
                            label = { Text("Science & Tech") },
                            leadingIcon = {
                                if (selectedSubject10th == "Science") {
                                    Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                }
                            }
                        )
                    }
                } else {
                    // For Class 5th-9th, show a subtle info banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(EduTealAccent.copy(alpha = 0.08f))
                            .padding(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = EduTealAccent)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Showing Maharashtra State Board Yearly Exam Papers & Scholarship syllabus archives.",
                                fontSize = 12.sp,
                                color = EduBlueDark,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // Papers Listing
                if (filteredPapers.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.CloudQueue,
                                contentDescription = null,
                                tint = Color.LightGray,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "No Papers Found",
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )
                            Text(
                                "Check back later for newly added archives.",
                                fontSize = 12.sp,
                                color = Color.LightGray
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredPapers) { paper ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { activeViewingPaper = paper },
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Year emblem sphere
                                    Box(
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(CircleShape)
                                            .background(EduGoldAccent.copy(alpha = 0.1f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(
                                                text = "${paper.year}",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = EduGoldAccent
                                            )
                                            Text(
                                                text = "PYQ",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Light,
                                                color = EduGoldAccent
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = paper.title,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = EduBlueDark
                                        )
                                        Row(
                                            modifier = Modifier.padding(top = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "${paper.board} • ${paper.subject}",
                                                fontSize = 12.sp,
                                                color = Color.Gray
                                            )
                                        }
                                        Row(
                                            modifier = Modifier.padding(top = 6.dp),
                                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Default.Timer, contentDescription = null, size = 12.dp, tint = Color.Gray)
                                                Spacer(modifier = Modifier.width(2.dp))
                                                Text("${paper.durationMinutes} mins", fontSize = 11.sp, color = Color.Gray)
                                            }
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Default.WorkspacePremium, contentDescription = null, size = 12.dp, tint = EduTealAccent)
                                                Spacer(modifier = Modifier.width(2.dp))
                                                Text("${paper.totalMarks} Marks", fontSize = 11.sp, color = EduTealAccent, fontWeight = FontWeight.SemiBold)
                                            }
                                        }
                                    }

                                    Icon(
                                        imageVector = Icons.Default.Launch,
                                        contentDescription = "Open Simulator Exam",
                                        tint = EduBluePrimary,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Interactive self-test overlay/dialog
            activeViewingPaper?.let { paper ->
                AlertDialog(
                    onDismissRequest = { activeViewingPaper = null },
                    confirmButton = {
                        TextButton(
                            onClick = { activeViewingPaper = null },
                            colors = ButtonDefaults.textButtonColors(contentColor = EduTealAccent)
                        ) {
                            Text("Close Exam Sheet", fontWeight = FontWeight.Bold)
                        }
                    },
                    icon = { Icon(Icons.Default.MenuBook, contentDescription = null, tint = EduGoldAccent, modifier = Modifier.size(32.dp)) },
                    title = {
                        Text(
                            text = "${paper.year} Board Exam Sheet",
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp
                        )
                    },
                    text = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(320.dp)
                                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "Subject: ${paper.subject} | Marks: ${paper.totalMarks}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = EduBlueDark,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Divider()
                            Spacer(modifier = Modifier.height(8.dp))

                            // Display mock standard paper layout
                            LazyColumn(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                item {
                                    Text(
                                        text = "SECTION A (Objective - 1 Mark each)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Gray
                                    )
                                }
                                item {
                                    Text(
                                        text = "Q1. Choose the correct alternative:\ni. Acceleration due to gravity is maximum at __________.\n   (a) Equator (b) Poles (c) Center of Earth (d) Altitude",
                                        fontSize = 13.sp,
                                        color = EduBlueDark,
                                        lineHeight = 18.sp
                                    )
                                }
                                item {
                                    Text(
                                        text = "ii. If simultaneous equations have no solutions, lines must be:\n   (a) Intersecting (b) Overlapping (c) Parallel (d) Coincident",
                                        fontSize = 13.sp,
                                        color = EduBlueDark,
                                        lineHeight = 18.sp
                                    )
                                }
                                item {
                                    Text(
                                        text = "SECTION B (Analytical - 2 Marks each)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Gray
                                    )
                                }
                                item {
                                    Text(
                                        text = "Q2. Solve the following:\ni. Distinguish between Mass and Weight.\nii. Find the value of determinant D for simultaneous equations:\n    5x + 3y = -11 ; 2x + 4y = -10.",
                                        fontSize = 13.sp,
                                        color = EduBlueDark,
                                        lineHeight = 18.sp
                                    )
                                }
                                item {
                                    Text(
                                        text = "SECTION C (Descriptive - 5 Marks each)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Gray
                                    )
                                }
                                item {
                                    Text(
                                        text = "Q3. Attempt in detail:\nExplain Keplar's three laws of Planetary Motion with the help of a neat labeled diagram, highlighting orbit geometry and areal velocity metrics.",
                                        fontSize = 13.sp,
                                        color = EduBlueDark,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    },
                    shape = RoundedCornerShape(24.dp),
                    containerColor = Color.White
                )
            }
        }
    }
}

// Inline helper for chip sizing
@Composable
fun Icon(imageVector: androidx.compose.ui.graphics.vector.ImageVector, contentDescription: String?, size: androidx.compose.ui.unit.Dp, tint: Color) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = Modifier.size(size),
        tint = tint
    )
}
