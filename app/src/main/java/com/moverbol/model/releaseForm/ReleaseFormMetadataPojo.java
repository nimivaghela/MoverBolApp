package com.moverbol.model.releaseForm;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.moverbol.BR;

import java.util.List;

/**
 * Created by AkashM on 7/2/18.
 */

public class ReleaseFormMetadataPojo extends BaseObservable implements Parcelable {

    @SerializedName("r_id")
    private String releaseFormId;

    private String selectionId;

    @SerializedName("r_label")
    private String label;

    @SerializedName("r_description")
    private String description;

    @SerializedName("large_description")
    private String largeDescription;

    private boolean selected;

    @SerializedName("address_null")
    private String address;

    @SerializedName("address")
    private List<String> addressList;

    public String getReleaseFormId() {
        return releaseFormId;
    }

    public void setReleaseFormId(String releaseFormId) {
        this.releaseFormId = releaseFormId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    @Bindable
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        notifyPropertyChanged(BR.selected);
        /*if(!selected){
            setAddress("");
        }*/
    }

    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);
    }

    public String getLargeDescription() {
        return largeDescription;
    }

    public void setLargeDescription(String largeDescription) {
        this.largeDescription = largeDescription;
    }

    public List<String> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<String> addressList) {
        this.addressList = addressList;
    }


    public String getSelectionId() {
        return selectionId;
    }

    public void setSelectionId(String selectionId) {
        this.selectionId = selectionId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.releaseFormId);
        dest.writeString(this.selectionId);
        dest.writeString(this.label);
        dest.writeString(this.description);
        dest.writeString(this.largeDescription);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
        dest.writeString(this.address);
        dest.writeStringList(this.addressList);
    }

    public ReleaseFormMetadataPojo() {
    }

    protected ReleaseFormMetadataPojo(Parcel in) {
        this.releaseFormId = in.readString();
        this.selectionId = in.readString();
        this.label = in.readString();
        this.description = in.readString();
        this.largeDescription = in.readString();
        this.selected = in.readByte() != 0;
        this.address = in.readString();
        this.addressList = in.createStringArrayList();
    }

    public static final Parcelable.Creator<ReleaseFormMetadataPojo> CREATOR = new Parcelable.Creator<ReleaseFormMetadataPojo>() {
        @Override
        public ReleaseFormMetadataPojo createFromParcel(Parcel source) {
            return new ReleaseFormMetadataPojo(source);
        }

        @Override
        public ReleaseFormMetadataPojo[] newArray(int size) {
            return new ReleaseFormMetadataPojo[size];
        }
    };
}
