package vamsi.kalanidhi;

import android.os.Parcel;
import android.os.Parcelable;

public class Images implements Parcelable {
    String image;
    String product;

    protected Images(Parcel in) {
        image = in.readString();
        product = in.readString();
    }

    public static final Creator<Images> CREATOR = new Creator<Images>() {
        @Override
        public Images createFromParcel(Parcel in) {
            return new Images(in);
        }

        @Override
        public Images[] newArray(int size) {
            return new Images[size];
        }
    };

    public String getImage() {
        return image;
    }

    public String getProduct() {
        return product;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeString(product);
    }
}
