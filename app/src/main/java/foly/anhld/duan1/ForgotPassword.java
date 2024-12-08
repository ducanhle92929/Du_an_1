package foly.anhld.duan1;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText emailEditText;
    private TextView txtContinue;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Khởi tạo FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Ánh xạ các view từ layout
        emailEditText = findViewById(R.id.emailEditText);
        txtContinue = findViewById(R.id.txtContinue);

        // Xử lý sự kiện nhấn nút "Tiếp tục"
        txtContinue.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();

            // Kiểm tra tính hợp lệ của email
            if (email.isEmpty()) {
                emailEditText.setError("Vui lòng nhập email");
                emailEditText.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEditText.setError("Email không hợp lệ");
                emailEditText.requestFocus();
                return;
            }

            // Gửi yêu cầu đặt lại mật khẩu qua email
            sendPasswordResetEmail(email);
        });
    }

    // Phương thức gửi email reset mật khẩu
    private void sendPasswordResetEmail(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgotPassword.this,
                                "Email khôi phục mật khẩu đã được gửi, vui lòng kiểm tra email của bạn.",
                                Toast.LENGTH_LONG).show();
                        // Quay lại màn hình EnterNewPassWord và truyền email
//                        Intent intent = new Intent(ForgotPassword.this, EnterNewPassWord.class);
//                        intent.putExtra("email", email); // Truyền email qua Intent
//                        startActivity(intent);
//                        finish();
                    } else {
                        Toast.makeText(ForgotPassword.this,
                                "Đã xảy ra lỗi: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
