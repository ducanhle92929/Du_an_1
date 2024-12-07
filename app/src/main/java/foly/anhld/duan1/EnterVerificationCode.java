package foly.anhld.duan1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EnterVerificationCode extends AppCompatActivity {

    private EditText otpEditText;
    private TextView submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_verification_code);

        // Lấy email từ intent
        String email = getIntent().getStringExtra("email");

//        otpEditText = findViewById(R.id.otpEditText);
//        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(v -> {
            String otp = otpEditText.getText().toString().trim();

            if (otp.isEmpty()) {
                otpEditText.setError("Vui lòng nhập mã OTP");
                otpEditText.requestFocus();
                return;
            }

            // Kiểm tra mã OTP và thực hiện hành động xác thực
            validateOTP(otp, email);
        });
    }

    // Phương thức xác thực mã OTP
    private void validateOTP(String otp, String email) {
        // Lọc mã OTP trong Firestore theo email người dùng
        // Kiểm tra mã OTP có đúng không và tiếp tục quá trình đổi mật khẩu

        // Đây là chỗ bạn sẽ truy vấn Firestore và so sánh mã OTP người dùng nhập
        // với mã OTP đã lưu trong cơ sở dữ liệu.

        // Nếu đúng mã OTP, bạn có thể chuyển sang màn hình đổi mật khẩu.
        Toast.makeText(this, "Xác thực thành công", Toast.LENGTH_SHORT).show();
    }
}
