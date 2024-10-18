package com.example.recyclerview

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    // Khai báo biến cho DatabaseHelper và các thành phần giao diện
    private lateinit var databaseHelper: DatabaseHelper  // Đối tượng để thao tác với cơ sở dữ liệu
    private lateinit var recyclerView: RecyclerView  // RecyclerView hiển thị danh sách sinh viên
    private lateinit var studentAdapter: StudentAdapter  // Adapter để quản lý dữ liệu trong RecyclerView
    private lateinit var nameEditText: EditText  // Ô nhập tên sinh viên
    private lateinit var classNameEditText: EditText  // Ô nhập lớp của sinh viên
    private lateinit var radioGroup: RadioGroup  // Nhóm radio buttons
    private lateinit var radioYes: RadioButton  // Radio button "Có" để xác nhận thao tác
    private lateinit var radioNo: RadioButton  // Radio button "Không" để từ chối thao tác
    private lateinit var addButton: Button  // Nút thêm sinh viên
    private lateinit var editButton: Button  // Nút chỉnh sửa sinh viên
    private lateinit var deleteButton: Button  // Nút xóa sinh viên
    private var selectedPosition: Int = -1  // Vị trí của sinh viên được chọn trong danh sách

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)  // Kết nối giao diện với layout XML

        // Khởi tạo DatabaseHelper để thao tác với cơ sở dữ liệu
        databaseHelper = DatabaseHelper(this)

        // Liên kết các thành phần giao diện với các biến đã khai báo
        recyclerView = findViewById(R.id.recyclerView)
        nameEditText = findViewById(R.id.nameEditText)
        classNameEditText = findViewById(R.id.classEditText)
        radioGroup = findViewById(R.id.radioGroup)
        radioYes = findViewById(R.id.radioYes)
        radioNo = findViewById(R.id.radioNo)
        addButton = findViewById(R.id.addButton)
        editButton = findViewById(R.id.editButton)
        deleteButton = findViewById(R.id.deleteButton)

        // Lấy danh sách sinh viên từ cơ sở dữ liệu và hiển thị trên RecyclerView
        val students = databaseHelper.getAllStudents()

        // Khởi tạo adapter và gắn vào RecyclerView
        studentAdapter = StudentAdapter(students) { student, position ->
            // Khi người dùng chọn một sinh viên, hiển thị thông tin sinh viên đó lên các ô nhập
            nameEditText.setText(student.name)
            classNameEditText.setText(student.className)
            selectedPosition = position  // Lưu lại vị trí của sinh viên được chọn
        }

        recyclerView.layoutManager = LinearLayoutManager(this)  // Sử dụng LinearLayout cho RecyclerView
        recyclerView.adapter = studentAdapter  // Gắn adapter vào RecyclerView

        // Thiết lập sự kiện khi nút "Thêm" được nhấn
        addButton.setOnClickListener {
            val name = nameEditText.text.toString()  // Lấy tên từ ô nhập
            val className = classNameEditText.text.toString()  // Lấy tên lớp từ ô nhập

            // Kiểm tra điều kiện: các trường không rỗng và radioYes được chọn
            if (name.isNotEmpty() && className.isNotEmpty() && radioYes.isChecked) {
                val newStudent = Student(0, name, className)  // Tạo đối tượng sinh viên mới (id = 0)
                databaseHelper.addStudent(newStudent)  // Thêm sinh viên vào cơ sở dữ liệu
                studentAdapter.addStudent(newStudent)  // Thêm sinh viên vào danh sách hiển thị
                clear()  // Xóa trắng các ô nhập sau khi thêm
                Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show()  // Thông báo thành công
            }
        }

        // Thiết lập sự kiện khi nút "Sửa" được nhấn
        editButton.setOnClickListener {
            if (selectedPosition != -1) {  // Chỉ thực hiện khi đã chọn một sinh viên
                val name = nameEditText.text.toString()  // Lấy tên từ ô nhập
                val className = classNameEditText.text.toString()  // Lấy tên lớp từ ô nhập

                // Kiểm tra điều kiện: các trường không rỗng và radioYes được chọn
                if (name.isNotEmpty() && className.isNotEmpty() && radioYes.isChecked) {
                    val updatedStudent = Student(0, name, className)  // Tạo đối tượng sinh viên mới (id = 0)
                    val studentId = students[selectedPosition].id  // Lấy ID của sinh viên từ danh sách
                    databaseHelper.updateStudent(studentId, updatedStudent)  // Cập nhật sinh viên trong cơ sở dữ liệu
                    studentAdapter.updateStudent(selectedPosition, updatedStudent)  // Cập nhật sinh viên trong danh sách hiển thị
                    clear()  // Xóa trắng các ô nhập sau khi sửa
                    Toast.makeText(this, "Sửa thành công", Toast.LENGTH_SHORT).show()  // Thông báo thành công
                }
            }
        }

        // Thiết lập sự kiện khi nút "Xóa" được nhấn
        deleteButton.setOnClickListener {
            if (selectedPosition != -1) {  // Chỉ thực hiện khi đã chọn một sinh viên
                val studentId = students[selectedPosition].id  // Lấy ID của sinh viên từ danh sách
                databaseHelper.deleteStudent(studentId)  // Xóa sinh viên khỏi cơ sở dữ liệu
                studentAdapter.deleteStudent(selectedPosition)  // Xóa sinh viên khỏi danh sách hiển thị
                clear()  // Xóa trắng các ô nhập sau khi xóa
                Toast.makeText(this, "Xoá thành công", Toast.LENGTH_SHORT).show()  // Thông báo thành công
            }
        }
    }

    // Hàm xóa trắng các ô nhập và đặt lại trạng thái mặc định
    fun clear() {
        nameEditText.text.clear()  // Xóa nội dung ô nhập tên
        classNameEditText.text.clear()  // Xóa nội dung ô nhập lớp
        radioGroup.clearCheck()  // Bỏ chọn radio button
        selectedPosition = -1  // Đặt lại vị trí được chọn thành -1 (không có sinh viên nào được chọn)
    }
}
