package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppData
import com.example.data.ChapterContent
import com.example.data.StudyMaterial
import com.example.ui.theme.EduBlueDark
import com.example.ui.theme.EduBluePrimary
import com.example.ui.theme.EduGoldAccent
import com.example.ui.theme.EduTealAccent
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyMaterialScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val studentClass = currentUser?.currentClass ?: "10th"
    val studentBoard = currentUser?.board ?: "Maharashtra State Board"

    // Priority filter
    val materials = remember(studentClass, studentBoard) {
        val gradeFiltered = AppData.StudyMaterials.filter { it.className == studentClass }
        if (gradeFiltered.isNotEmpty()) gradeFiltered else AppData.StudyMaterials
    }

    // Active Reader State
    var activeMaterial by remember { mutableStateOf<StudyMaterial?>(null) }
    var activeChapterIndex by remember { mutableStateOf(0) }
    var activePageIndex by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(activeMaterial?.let { "Reading: ${it.title}" } ?: "Study & E-Books", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (activeMaterial != null) {
                                activeMaterial = null
                            } else {
                                onBack()
                            }
                        },
                        modifier = Modifier.testTag("study_back_button")
                    ) {
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
            if (activeMaterial == null) {
                // List of available materials
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            color = EduBlueDark
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(
                                    text = "Class textbooks and revision guides curated for Maharashtra State Board and CBSE standard curriculums.",
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.MenuBook, contentDescription = null, size = 16.dp, tint = EduGoldAccent)
                                    Text(
                                        text = "Grade $studentClass Material Selected",
                                        color = EduGoldAccent,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(start = 6.dp)
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Recommended E-Books & Reference Notes",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = EduBlueDark,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    items(materials) { mat ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    activeMaterial = mat
                                    activeChapterIndex = 0
                                    activePageIndex = 0
                                },
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            Brush.verticalGradient(
                                                colors = listOf(EduTealAccent, EduBluePrimary)
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MenuBook,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Surface(
                                        color = EduGoldAccent.copy(alpha = 0.15f),
                                        contentColor = EduGoldAccent,
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = mat.type.uppercase(),
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = mat.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = EduBlueDark,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "${mat.board} • ${mat.contentPreview.size} Chapters",
                                        fontSize = 12.sp,
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
                // PDF / Book Viewer Simulator UI
                val currentBook = activeMaterial!!
                val currentChapterObj: ChapterContent = currentBook.contentPreview.getOrNull(activeChapterIndex)
                    ?: currentBook.contentPreview.first()
                val activePageText = currentChapterObj.pagesText.getOrNull(activePageIndex)
                    ?: currentChapterObj.pagesText.first()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFFCFBF7)) // Real off-white vintage book paper style
                ) {
                    // Header Chapter Selector
                    ScrollableTabRow(
                        selectedTabIndex = activeChapterIndex,
                        containerColor = EduBluePrimary,
                        contentColor = Color.White,
                        edgePadding = 16.dp
                    ) {
                        currentBook.contentPreview.forEachIndexed { idx, ch ->
                            Tab(
                                selected = activeChapterIndex == idx,
                                onClick = {
                                    activeChapterIndex = idx
                                    activePageIndex = 0
                                },
                                text = {
                                    Text(
                                        text = "Ch ${ch.chapterNum}: ${ch.name}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }
                            )
                        }
                    }

                    // Content Canvas (The Book Page style)
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(24.dp)
                    ) {
                        // Title
                        Text(
                            text = "CHAPTER ${currentChapterObj.chapterNum}: ${currentChapterObj.name}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = EduBlueDark,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        )
                        Divider(color = EduBlueDark.copy(alpha = 0.1f), thickness = 2.dp)
                        Spacer(modifier = Modifier.height(16.dp))

                        // Curated e-reader body text
                        Text(
                            text = activePageText,
                            fontSize = 16.sp,
                            lineHeight = 26.sp,
                            color = Color(0xFF2C2C2C),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 32.dp)
                        )
                    }

                    // Bottom Navigation inside Book
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White,
                        shadowElevation = 8.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Page ${activePageIndex + 1} of ${currentChapterObj.pagesText.size}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Button(
                                    onClick = {
                                        if (activePageIndex > 0) {
                                            activePageIndex -= 1
                                        } else if (activeChapterIndex > 0) {
                                            activeChapterIndex -= 1
                                            activePageIndex = currentBook.contentPreview[activeChapterIndex].pagesText.lastIndex
                                        }
                                    },
                                    enabled = activePageIndex > 0 || activeChapterIndex > 0,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = EduBluePrimary,
                                        contentColor = Color.White,
                                        disabledContainerColor = Color.LightGray
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Prev Page", modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Back", fontSize = 12.sp)
                                }

                                Button(
                                    onClick = {
                                        if (activePageIndex < currentChapterObj.pagesText.lastIndex) {
                                            activePageIndex += 1
                                        } else if (activeChapterIndex < currentBook.contentPreview.lastIndex) {
                                            activeChapterIndex += 1
                                            activePageIndex = 0
                                        }
                                    },
                                    enabled = activePageIndex < currentChapterObj.pagesText.lastIndex || activeChapterIndex < currentBook.contentPreview.lastIndex,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = EduBluePrimary,
                                        contentColor = Color.White,
                                        disabledContainerColor = Color.LightGray
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Next", fontSize = 12.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Page", modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
