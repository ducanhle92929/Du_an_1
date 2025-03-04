package foly.anhld.duan1.Modol;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable {
    private String name;

    // Constructor mặc định cho Firestore
    public Category() {}

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Parcelable implementation
    protected Category(Parcel in) {
        name = in.readString(); // Đọc thuộc tính name từ Parcel
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name); // Ghi thuộc tính name vào Parcel
    }

    @Override
    public int describeContents() {
        return 0; // Không có đặc tính đặc biệt nào
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in); // Khởi tạo đối tượng từ Parcel
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size]; // Tạo mảng đối tượng Category
        }
    };
}
