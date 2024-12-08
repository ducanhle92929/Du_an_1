package foly.anhld.duan1.dailog;




import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import foly.anhld.duan1.R;

public class CongratulationsDialogFragment extends DialogFragment {

    private OnDialogButtonClickListener listener;

    public interface OnDialogButtonClickListener {
        void onBackToHomeClick();
    }

    public void setOnDialogButtonClickListener(OnDialogButtonClickListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Sử dụng AlertDialog để hiển thị layout tùy chỉnh
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireActivity());

        // Inflate layout tùy chỉnh
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_congratulations, null);
        builder.setView(dialogView).setCancelable(false);

        // Cài đặt sự kiện cho nút "Back to Home"
        dialogView.findViewById(R.id.back_to_home_button).setOnClickListener(v -> {
            if (listener != null) {
                listener.onBackToHomeClick();
            }
            dismiss();
        });

        return builder.create();
    }
}
