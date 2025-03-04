package foly.anhld.duan1.Modol;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class SanPham implements Parcelable {
    private String maSanPham;      // Mã sản phẩm
    private String tenSanPham;    // Tên sản phẩm
    private double gia;           // Giá sản phẩm
    private int soLuong;          // Tổng số lượng sản phẩm
    private String moTa;          // Mô tả sản phẩm
    private String hinhAnh;       // Đường dẫn hình ảnh
    private List<SizeWithQuantity> sizes; // Danh sách size cùng số lượng
    private Category category;    // Thêm thuộc tính Category

    // Default constructor
    public SanPham() {}

    // Constructor với tất cả các trường, bao gồm Category
    public SanPham(String maSanPham, String tenSanPham, double gia, int soLuong, String moTa, String hinhAnh, List<SizeWithQuantity> sizes, Category category) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.gia = gia;
        this.soLuong = soLuong;
        this.moTa = moTa;
        this.hinhAnh = hinhAnh;
        this.sizes = sizes;
        this.category = category;  // Gán Category
    }

    // Getters và Setters
    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public List<SizeWithQuantity> getSizes() {
        return sizes;
    }

    public void setSizes(List<SizeWithQuantity> sizes) {
        this.sizes = sizes;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    // Parcelable implementation
    protected SanPham(Parcel in) {
        maSanPham = in.readString();
        tenSanPham = in.readString();
        gia = in.readDouble();
        soLuong = in.readInt();
        moTa = in.readString();
        hinhAnh = in.readString();
        sizes = in.createTypedArrayList(SizeWithQuantity.CREATOR);
        category = in.readParcelable(Category.class.getClassLoader()); // Đọc Category
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(maSanPham);
        dest.writeString(tenSanPham);
        dest.writeDouble(gia);
        dest.writeInt(soLuong);
        dest.writeString(moTa);
        dest.writeString(hinhAnh);
        dest.writeTypedList(sizes);
        dest.writeParcelable(category, flags); // Ghi Category vào Parcel
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SanPham> CREATOR = new Creator<SanPham>() {
        @Override
        public SanPham createFromParcel(Parcel in) {
            return new SanPham(in);
        }

        @Override
        public SanPham[] newArray(int size) {
            return new SanPham[size];
        }
    };
}
