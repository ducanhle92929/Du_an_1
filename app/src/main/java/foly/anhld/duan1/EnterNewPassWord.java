package foly.anhld.duan1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class EnterNewPassWord extends AppCompatActivity {

    private TextInputEditText edtPassword, edtConfirmPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_new_pass_word);

        // Khởi tạo FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Ánh xạ các view từ layout
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        TextView txtContinue = findViewById(R.id.txtContinue);

        // Xử lý khi nhấn nút "Tiếp tục"
        txtContinue.setOnClickListener(v -> handlePasswordValidation());
    }

    /**
     * Hàm xử lý kiểm tra mật khẩu.
     */
    private void handlePasswordValidation() {
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        if (password.isEmpty() || confirmPassword.isEmpty()) {
            showErrorDialog("Các trường không thể để trống!");
        } else if (!password.equals(confirmPassword)) {
            showErrorDialog("Mật khẩu không khớp!");
        } else {
            // Cập nhật mật khẩu
            updatePassword(password);
        }
    }

    /**
     * Cập nhật mật khẩu mới cho người dùng.
     */
    private void updatePassword(String newPassword) {
        if (mAuth.getCurrentUser() != null) {
            // Thay đổi mật khẩu trên Firebase
            mAuth.getCurrentUser().updatePassword(newPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(EnterNewPassWord.this, "Mật khẩu đã được thay đổi.", Toast.LENGTH_SHORT).show();
                            // Chuyển đến màn hình chính sau khi thay đổi mật khẩu thành công
                            navigateToHomeScreen();
                        } else {
                            // Hiển thị lỗi nếu không thành công
                            showErrorDialog("Lỗi: " + task.getException().getMessage());
                        }
                    });
        } else {
            // Nếu không có người dùng đăng nhập
            showErrorDialog("Bạn cần phải đăng nhập trước");
        }
    }

    /**
     * Hiển thị dialog thông báo lỗi.
     *
     * @param message Thông báo lỗi.
     */
    private void showErrorDialog(String message) {
        Toast.makeText(EnterNewPassWord.this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Chuyển đến màn hình chính sau khi thay đổi mật khẩu thành công.
     */
    private void navigateToHomeScreen() {
        Intent intent = new Intent(EnterNewPassWord.this, Home.class);
        startActivity(intent);
        finish();
    }
}
