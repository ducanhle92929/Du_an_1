package foly.anhld.duan1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Register extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private TextInputEditText usernameEditText, emailEditText, passwordEditText;

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Client ID lấy từ Firebase
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Xử lý sự kiện click Google Sign-In
        LinearLayout btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);
        btnGoogleSignIn.setOnClickListener(v -> googleSignIn());
        // Back button
        ImageView imgicon = findViewById(R.id.imgicon);
         TextView txtIn = findViewById(R.id.txtIn);
         txtIn.setOnClickListener(v -> {
             startActivity(new Intent( Register.this, Login.class));
         });
        imgicon.setOnClickListener(v -> finish());

        // Initialize views
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        TextView btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> handleRegister());

        // Google Sign-in
//        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);
//        btnGoogleSignIn.setOnClickListener(v -> googleSignIn());

        // Facebook Sign-in
        LinearLayout btnFacebookSignIn = findViewById(R.id.btnFacebookSignIn);
        btnFacebookSignIn.setOnClickListener(v -> facebookSignIn());
    }

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private void handleRegister() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (username.isEmpty()) {
            usernameEditText.setError("Vui lòng nhập tên người dùng");
            usernameEditText.requestFocus(); // Đặt con trỏ vào ô nhập liệu
            return;
        } else if (username.length() < 6) {
            usernameEditText.setError("Tên người dùng phải có ít nhất 6 ký tự");
            usernameEditText.requestFocus();
            return;
        }



        // Kiểm tra nếu email rỗng hoặc không hợp lệ
        if (email.isEmpty()) {
            emailEditText.setError("Vui lòng nhập email");
            emailEditText.requestFocus();
            return;

        }

        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Email không hợp lệ");
            emailEditText.requestFocus();
            return;
        }

        // Kiểm tra nếu mật khẩu rỗng hoặc không đạt yêu cầu
        if (password.isEmpty()) {
            passwordEditText.setError("Vui lòng nhập mật khẩu");
            passwordEditText.requestFocus();
            return;
        } else if (password.length() < 6 ) {
            passwordEditText.setError("Mật khẩu phải có ít nhất 6 ký tự");
            passwordEditText.requestFocus();
            return;
        }

        // Đăng ký người dùng với Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Đăng ký thành công
                        FirebaseUser user = mAuth.getCurrentUser();
                        // Lưu username vào Firestore hoặc Realtime Database nếu cần
                        // Lưu trữ thông tin người dùng ở đây

                        // Hiển thị thông báo đăng ký thành công
                        Toast.makeText(Register.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

                        // Chuyển sang màn hình login
                        Intent intent = new Intent(Register.this, Login.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Đăng ký thất bại
                        Toast.makeText(Register.this, "Đăng ký thất bại. Thử lại!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void googleSignIn() {
        // Implement Google Sign-in logic with Firebase here
        Toast.makeText(this, "Google Sign-in clicked", Toast.LENGTH_SHORT).show();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Kiểm tra yêu cầu là Google Sign-In
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(this, "Google sign-in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Xử lý đăng nhập Firebase sau khi Google Sign-In thành công
//    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
//        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
//        firebaseAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, task -> {
//                    if (task.isSuccessful()) {
//                        // Đăng nhập Google thành công
//                        Toast.makeText(this, "Đăng nhập Google thành công", Toast.LENGTH_SHORT).show();
//
//                        // Chuyển đến MainActivity sau khi đăng nhập thành công
//                        Intent intent = new Intent(Register.this, MainActivity.class);
//                        startActivity(intent);
//                        finish(); // Đảm bảo không quay lại màn hình đăng ký khi bấm nút back
//                    }
//                    else {
//                        // Thông báo lỗi khi đăng nhập thất bại
//                        Toast.makeText(this, "Đăng nhập Google thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
    // Xử lý đăng nhập Firebase sau khi Google Sign-In thành công
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    // Đăng nhập hoàn tất (Dù thành công hay thất bại, vẫn chuyển màn)
                    Intent intent = new Intent(Register.this, Home.class);
                    startActivity(intent);
                    finish(); // Đảm bảo không quay lại màn hình đăng ký khi bấm nút back
                });
    }



    private void facebookSignIn() {
        // Implement Facebook Sign-in logic with Firebase here
        Toast.makeText(this, "Facebook Sign-in clicked", Toast.LENGTH_SHORT).show();
    }
}
