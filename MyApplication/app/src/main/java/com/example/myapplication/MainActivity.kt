package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LibraryManagementScreen()
        }
    }
}

// Data class để lưu thông tin sách
data class Book(val title: String, val available: Boolean = true)

// Data class để lưu thông tin mượn sách
data class BorrowRecord(val borrowerName: String, val bookTitle: String)

@Composable
fun LibraryManagementScreen() {
    // State cho các trường nhập liệu
    var hoTen by remember { mutableStateOf("") }
    var sach by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var borrowRecords by remember { mutableStateOf(listOf<BorrowRecord>()) }

    // Danh sách sách nguồn (giả lập)
    val sourceBooks = remember {
        mutableStateListOf(
            Book("Sách 01"),
            Book("Sách 02")
        )
    }
    var selectedBook by remember { mutableStateOf<Book?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Tiêu đề
        Text(
            text = "Hệ thống Quản lý Thư viện",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        // Lưu ý
        Text(
            text = "Tắt các thông tin điều phải đây lên github",
            color = Color.Red,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Phần nhập liệu
        OutlinedTextField(
            value = hoTen,
            onValueChange = { hoTen = it },
            label = { Text("Họ tên") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = sach,
            onValueChange = { sach = it },
            label = { Text("Danh sách sách") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Nút "Thêm"
        Button(
            onClick = {
                if (hoTen.isEmpty() || sach.isEmpty()) {
                    errorMessage = "Vui lòng nhập họ tên và danh sách sách"
                } else {
                    borrowRecords = borrowRecords + BorrowRecord(hoTen, sach)
                    hoTen = ""
                    sach = ""
                    errorMessage = ""
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
        ) {
            Text("Thêm")
        }

        // Hiển thị lỗi nếu có
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Danh sách sách nguồn
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            sourceBooks.forEach { book ->
                Button(
                    onClick = {
                        if (book.available) {
                            selectedBook = book
                            // Đánh dấu sách đã được mượn (giả lập)
                            sourceBooks[sourceBooks.indexOf(book)] = book.copy(available = false)
                        } else {
                            errorMessage = "${book.title} đã được mượn"
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (book.available) Color.LightGray else Color.Gray
                    ),
                    enabled = book.available
                ) {
                    Text(book.title)
                }
            }
        }

        // Hiển thị thông tin sách đã mượn
        if (selectedBook != null) {
            Text(
                text = "Sách đã mượn: ${selectedBook?.title} bởi $hoTen",
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Danh sách sách đã thêm (lịch sử mượn)
        LazyColumn {
            items(borrowRecords) { record ->
                Text(
                    text = "${record.borrowerName} - ${record.bookTitle}",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Các nút dưới cùng
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { /* Xử lý Quản lý */ },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Quản lý")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { /* Xử lý DS Sách */ },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("DS Sách")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { /* Xử lý Người dùng */ },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Người dùng")
            }
        }
    }
}