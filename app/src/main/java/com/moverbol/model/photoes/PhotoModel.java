package com.moverbol.model.photoes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.moverbol.BR;
import com.moverbol.constants.Constants;

import java.util.ArrayList;

/**
 * Created by AkashM on 16/2/18.
 */

public class PhotoModel extends BaseObservable implements Parcelable {
    public static final Parcelable.Creator<PhotoModel> CREATOR = new Parcelable.Creator<PhotoModel>() {
        @Override
        public PhotoModel createFromParcel(Parcel source) {
            return new PhotoModel(source);
        }

        @Override
        public PhotoModel[] newArray(int size) {
            return new PhotoModel[size];
        }
    };
    private String name;
    private boolean selectedForDelete;
    private boolean showSelection;
    private ArrayList<ImagesModel> images;

    public PhotoModel() {
    }

    protected PhotoModel(Parcel in) {
        this.name = in.readString();
        this.images = new ArrayList<ImagesModel>();
        in.readList(this.images, ImagesModel.class.getClassLoader());
        this.selectedForDelete = in.readByte() != 0;
        this.showSelection = in.readByte() != 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Bindable
    public boolean isSelectedForDelete() {
        return selectedForDelete;
    }

    public void setSelectedForDelete(boolean selectedForDelete) {
        this.selectedForDelete = selectedForDelete;
        notifyPropertyChanged(BR.selectedForDelete);
    }

    @Bindable
    public boolean isShowSelection() {
        return showSelection;
    }

    public void setShowSelection(boolean showSelection) {
        this.showSelection = showSelection;
        notifyPropertyChanged(BR.showSelection);
    }

    public ArrayList<ImagesModel> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImagesModel> images) {
        this.images = images;
    }

    public String getImageLink() {
        if (images != null) {
            if (name.equalsIgnoreCase(Constants.INVENTORY_PHOTOS)) {
                for (ImagesModel imageModel : images) {
                    for (ImageItemModel imageItemModel : imageModel.getItems()) {
                        for (ImagesModel innerModel : imageItemModel.getImages()) {
                            if (!innerModel.getLink().isEmpty()) {
                                return innerModel.getLink();
                            }
                        }
                    }
                }


            } else if (name.equalsIgnoreCase(Constants.CRATING_PHOTOS)) {

                for (ImagesModel imagesModel : images) {
                    for (ImagesModel innerImageModel : imagesModel.getImages()) {
                        if (!innerImageModel.getLink().isEmpty()) {
                            return innerImageModel.getLink();
                        }
                    }
                }
            } else if (!images.isEmpty()) {
                return images.get(0).getLink();
            }
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeList(this.images);
        dest.writeByte(this.selectedForDelete ? (byte) 1 : (byte) 0);
        dest.writeByte(this.showSelection ? (byte) 1 : (byte) 0);
    }
}
