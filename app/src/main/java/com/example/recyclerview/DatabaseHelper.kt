package com.example.recyclerview

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// Lớp hỗ trợ kết nối và thao tác với cơ sở dữ liệu SQLite
class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {
    companion object {
        private const val DATABASE_NAME = "students.db" // Tên của cơ sở dữ liệu
        private const val DATABASE_VERSION = 1 // Phiên bản của cơ sở dữ liệu

        // Tên bảng và các cột trong bảng sinh viên
        private const val TABLE_NAME = "students"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_CLASS = "class_name"
    }

    // Phương thức này được gọi khi cơ sở dữ liệu được tạo lần đầu
    override fun onCreate(db: SQLiteDatabase?) {
        // Tạo bảng sinh viên với các cột: id, name, class_name
        val createTable = ("CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_CLASS TEXT)")
        db?.execSQL(createTable) // Thực thi lệnh SQL để tạo bảng
    }

    // Phương thức này được gọi khi cơ sở dữ liệu cần nâng cấp (thay đổi phiên bản)
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Tạm thời bỏ trống, nhưng khi nâng cấp có thể xóa hoặc thêm bảng mới
    }

    // Phương thức để thêm sinh viên vào cơ sở dữ liệu
    fun addStudent(student: Student) {
        val db = this.writableDatabase // Mở cơ sở dữ liệu để ghi dữ liệu
        val values = ContentValues().apply {
            put(COLUMN_NAME, student.name) // Thêm tên sinh viên
            put(COLUMN_CLASS, student.className) // Thêm lớp học của sinh viên
        }
        db.insert(TABLE_NAME, null, values) // Chèn dữ liệu vào bảng
        db.close() // Đóng kết nối cơ sở dữ liệu
    }

    // Phương thức để cập nhật thông tin sinh viên theo ID
    fun updateStudent(id: Int, student: Student) {
        val db = this.writableDatabase // Mở cơ sở dữ liệu để ghi dữ liệu
        val values = ContentValues().apply {
            put(COLUMN_NAME, student.name) // Cập nhật tên sinh viên
            put(COLUMN_CLASS, student.className) // Cập nhật lớp học
        }
        db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(id.toString())) // Cập nhật dữ liệu trong bảng
        db.close() // Đóng kết nối cơ sở dữ liệu
    }

    // Phương thức để xóa sinh viên theo ID
    fun deleteStudent(id: Int) {
        val db = this.writableDatabase // Mở cơ sở dữ liệu để ghi dữ liệu
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString())) // Xóa dữ liệu theo ID
        db.close() // Đóng kết nối cơ sở dữ liệu
    }

    // Phương thức để lấy tất cả sinh viên từ cơ sở dữ liệu
    fun getAllStudents(): MutableList<Student> {
        val students = mutableListOf<Student>() // Danh sách sinh viên trống để lưu kết quả
        val db = this.readableDatabase // Mở cơ sở dữ liệu để đọc dữ liệu
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null) // Truy vấn lấy tất cả sinh viên

        // Lặp qua kết quả của truy vấn
        if (cursor.moveToFirst()) {
            do {
                // Lấy dữ liệu từ cột id, name và class_name
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val className = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLASS))

                // Thêm sinh viên vào danh sách
                students.add(Student(id, name, className))
            } while (cursor.moveToNext()) // Tiếp tục lặp nếu còn dữ liệu
        }
        cursor.close() // Đóng con trỏ (cursor)
        return students // Trả về danh sách sinh viên
    }
}
