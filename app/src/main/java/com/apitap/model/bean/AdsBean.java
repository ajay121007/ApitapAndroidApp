package com.apitap.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by sourcefuse on 12/12/16.
 */

public class AdsBean implements Parcelable {
    private String imageUrl;
    private String business_type;
    private String videoUrl;
    private String seen;
    private String MerchantName;
    private String MerchantLogo;
    private ArrayList<AdsDetailBean> arrayList;

    public AdsBean() {

    }
    public AdsBean(Parcel in) {
        imageUrl = in.readString();
        videoUrl = in.readString();
        MerchantName = in.readString();
        MerchantLogo = in.readString();
        business_type = in.readString();
        arrayList = in.createTypedArrayList(AdsDetailBean.CREATOR);
    }

    public static final Creator<AdsBean> CREATOR = new Creator<AdsBean>() {
        @Override
        public AdsBean createFromParcel(Parcel in) {
            return new AdsBean(in);
        }

        @Override
        public AdsBean[] newArray(int size) {
            return new AdsBean[size];
        }
    };

    public ArrayList<AdsDetailBean> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<AdsDetailBean> arrayList) {
        this.arrayList = arrayList;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMerchantLogo() {
        return MerchantLogo;
    }

    public void setMerchantLogo(String merchantLogo) {
        this.MerchantLogo = merchantLogo;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getSeen() {
        return seen;
    }

    public String getBusiness_type() {
        return business_type;
    }

    public void setBusiness_type(String business_type) {
        this.business_type = business_type;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public String getMerchantName() {
        return MerchantName;
    }

    public void setMerchantName(String MerchantName) {
        this.MerchantName = MerchantName;
    }


    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imageUrl);
        parcel.writeString(videoUrl);
        parcel.writeString(MerchantLogo);
        parcel.writeString(MerchantName);
        parcel.writeString(business_type);
        parcel.writeTypedList(arrayList);

    }

}
