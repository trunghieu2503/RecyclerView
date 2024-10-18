package com.example.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Adapter cho RecyclerView để hiển thị danh sách sinh viên
class StudentAdapter(
    private var studentList: MutableList<Student>, // Danh sách sinh viên, sử dụng MutableList để có thể thay đổi dữ liệu
    private var onClick: (Student, Int) -> Unit // Hàm callback để xử lý sự kiện khi người dùng nhấp vào một sinh viên
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() { // Kế thừa từ lớp RecyclerView.Adapter

    // Lớp StudentViewHolder giữ các thành phần giao diện cho từng mục trong danh sách
    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // TextView để hiển thị tên sinh viên
        val nameTextView = itemView.findViewById<TextView>(R.id.nameTextView)
        // TextView để hiển thị tên lớp của sinh viên
        val classTextView = itemView.findViewById<TextView>(R.id.classTextView)
    }

    // Phương thức này được gọi khi cần tạo ViewHolder mới
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        // Inflate layout cho từng mục từ file XML (item.xml)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return StudentViewHolder(view) // Trả về ViewHolder với giao diện vừa tạo
    }

    // Trả về số lượng sinh viên trong danh sách
    override fun getItemCount(): Int {
        return studentList.size // Kích thước của danh sách sinh viên
    }

    // Gán dữ liệu cho từng mục hiển thị trên RecyclerView
    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList[position] // Lấy đối tượng sinh viên tại vị trí hiện tại
        // Gán tên sinh viên và lớp học vào các TextView tương ứng
        holder.nameTextView.text = student.name
        holder.classTextView.text = student.className

        // Thiết lập sự kiện nhấp chuột vào mục trong danh sách
        holder.itemView.setOnClickListener {
            onClick(student, position) // Gọi hàm callback khi người dùng nhấp vào mục
        }
    }

    // Phương thức để thêm sinh viên mới vào danh sách
    fun addStudent(student: Student) {
        studentList.add(student) // Thêm sinh viên vào danh sách
        notifyItemInserted(studentList.size - 1) // Thông báo RecyclerView có một mục mới được thêm
    }

    // Phương thức để cập nhật thông tin của sinh viên tại một vị trí nhất định
    fun updateStudent(position: Int, updateStudent: Student) {
        studentList[position] = updateStudent // Cập nhật sinh viên tại vị trí được chỉ định
        notifyItemChanged(position) // Thông báo rằng mục tại vị trí này đã thay đổi
    }

    // Phương thức để xóa một sinh viên khỏi danh sách
    fun deleteStudent(position: Int) {
        studentList.removeAt(position) // Xóa sinh viên khỏi danh sách
        notifyItemRemoved(position) // Thông báo rằng mục tại vị trí này đã bị xóa
        notifyItemRangeChanged(position, studentList.size) // Cập nhật lại các mục còn lại trong danh sách
    }
}
