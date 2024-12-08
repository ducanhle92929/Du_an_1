package foly.anhld.duan1.Modol;

import android.os.Parcel;
import android.os.Parcelable;

public class SizeWithQuantity implements Parcelable {
    private String size;   // Kích thước
    private int quantity;  // Số lượng

    // Constructor mặc định
    public SizeWithQuantity() {}

    // Constructor với tham số
    public SizeWithQuantity(String size, int quantity) {
        this.size = size;
        this.quantity = quantity;
    }

    // Getter và Setter cho size
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    // Getter và Setter cho quantity
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Phương thức Parcelable để truyền tải đối tượng giữa các Activity
    protected SizeWithQuantity(Parcel in) {
        size = in.readString();
        quantity = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(size);   // Ghi kích thước vào Parcel
        dest.writeInt(quantity);  // Ghi số lượng vào Parcel
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Creator để tái tạo đối tượng từ Parcel
    public static final Creator<SizeWithQuantity> CREATOR = new Creator<SizeWithQuantity>() {
        @Override
        public SizeWithQuantity createFromParcel(Parcel in) {
            return new SizeWithQuantity(in);  // Khôi phục đối tượng từ Parcel
        }

        @Override
        public SizeWithQuantity[] newArray(int size) {
            return new SizeWithQuantity[size];  // Tạo mảng mới
        }
    };
}
