package com.moverbol.custom.binders;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.bumptech.glide.Glide;
import com.moverbol.R;
import com.moverbol.adapters.AddressSelectionListAdapter;
import com.moverbol.adapters.ArticleAdapter;
import com.moverbol.adapters.ArticleEditAdapter;
import com.moverbol.adapters.CalendarAdapter;
import com.moverbol.adapters.ClockHistoryDetailsAdapter;
import com.moverbol.adapters.ColorListAdapter;
import com.moverbol.adapters.CommentAdapter;
import com.moverbol.adapters.CrewAdapter;
import com.moverbol.adapters.HomeListAdapter;
import com.moverbol.adapters.InventoryRiderAdapter;
import com.moverbol.adapters.MaterialAdapter;
import com.moverbol.adapters.NotesAdapter;
import com.moverbol.adapters.PackingListAdapter;
import com.moverbol.adapters.PackingListEditAdapter;
import com.moverbol.adapters.PhotoAdapter;
import com.moverbol.adapters.PickupStopAdapter;
import com.moverbol.adapters.ReleaseFormListAdapter;
import com.moverbol.adapters.RiderListEditAdapter;
import com.moverbol.adapters.TruckAdapter;
import com.moverbol.adapters.ValuationAdapter;
import com.moverbol.adapters.bolAdapters.ClockHistoryTotalAdapter;
import com.moverbol.constants.Constants;
import com.moverbol.model.ColorPojo;
import com.moverbol.model.ExtraStopsPojo;
import com.moverbol.model.InventoryRiderPojo;
import com.moverbol.model.PackingListEditPojo;
import com.moverbol.model.PackingListPojo;
import com.moverbol.model.photoes.PhotoModel;
import com.moverbol.util.DividerItemDecoration;
import com.moverbol.util.Util;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sumeet on 28/8/17.
 */

public class BindingUtils {

    @BindingAdapter("homeitems")
    public static void bindTaskList(RecyclerView view, /*ObservableArrayList<JobPojo> list*/ HomeListAdapter homeListAdapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        view.setLayoutManager(layoutManager);
        view.setHasFixedSize(true);
//        view.setAdapter(new HomeListAdapter(view, list));
        view.setAdapter(homeListAdapter);
    }

    @BindingAdapter("crewitems")
    public static void bindCrewList(RecyclerView view, CrewAdapter crewAdapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        view.setLayoutManager(layoutManager);
        view.setHasFixedSize(true);
        view.setAdapter(crewAdapter);
    }

    @BindingAdapter("truckitems")
    public static void bindTruckList(RecyclerView view, TruckAdapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        view.setLayoutManager(layoutManager);
        view.setHasFixedSize(true);
        view.setAdapter(adapter);
    }

    @BindingAdapter("materiallistitems")
    public static void bindMaterialList(RecyclerView view, MaterialAdapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        view.setLayoutManager(layoutManager);
        view.setHasFixedSize(true);
        view.setAdapter(adapter);
    }

    @BindingAdapter("articleListAdapter")
    public static void bindArticleListAdapter(RecyclerView view, ArticleAdapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        view.setLayoutManager(layoutManager);
        view.setHasFixedSize(true);
        view.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
        view.setAdapter(adapter);
    }

    @BindingAdapter("releaseFormListAdapter")
    public static void bindReleaseFormListAdapter(RecyclerView view, ReleaseFormListAdapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        view.setLayoutManager(layoutManager);
        view.setHasFixedSize(true);
        view.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
        view.setAdapter(adapter);
    }

    @BindingAdapter("articleListEditAdapter")
    public static void bindArticleEditAdapter(RecyclerView view, ArticleEditAdapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        view.setLayoutManager(layoutManager);
        view.setHasFixedSize(true);
        view.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
        view.setAdapter(adapter);
    }


    @BindingAdapter("addressSelectionListAdapter")
    public static void bindAddressSelectionListAdapter(RecyclerView view, AddressSelectionListAdapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        view.setLayoutManager(layoutManager);
//        view.setHasFixedSize(true);
        view.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
        view.setAdapter(adapter);
    }

    @BindingAdapter("storageAdapter")
    public static void bindStorageAdapter(RecyclerView view, RecyclerView.Adapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        view.setLayoutManager(layoutManager);
        view.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
        view.setHasFixedSize(true);
        view.setAdapter(adapter);
    }

    @BindingAdapter("packinglistitems")
    public static void bindPackingList(RecyclerView view, ObservableArrayList<PackingListPojo> list) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        view.setLayoutManager(layoutManager);
        view.setHasFixedSize(true);
        view.setAdapter(new PackingListAdapter(view, list));
    }

    @BindingAdapter("inventoryrideritems")
    public static void bindInventoryRiderList(RecyclerView view, ObservableArrayList<InventoryRiderPojo> list) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        view.setLayoutManager(layoutManager);
        view.setHasFixedSize(true);
        view.setAdapter(new InventoryRiderAdapter(view, list));
    }

    @BindingAdapter("packinglistedititems")
    public static void bindPackingListEdit(RecyclerView view, ObservableArrayList<PackingListEditPojo> list) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        view.setLayoutManager(layoutManager);
        view.setHasFixedSize(true);
        view.setAdapter(new PackingListEditAdapter(view, list));
    }

    @BindingAdapter("riderlistedititems")
    public static void bindRiderListEdit(RecyclerView view, ObservableArrayList<PackingListEditPojo> list) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        view.setLayoutManager(layoutManager);
        view.setAdapter(new RiderListEditAdapter(view, list));
    }

    @BindingAdapter("pickupStopList")
    public static void bindPickUpStopList(RecyclerView view, List<ExtraStopsPojo> list) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        view.setLayoutManager(layoutManager);
        view.setHasFixedSize(true);
        view.setAdapter(new PickupStopAdapter(view, list));
    }


    @BindingAdapter("commentsList")
    public static void bindCommentsList(RecyclerView view, CommentAdapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        view.setLayoutManager(layoutManager);
        view.setHasFixedSize(true);
        view.setAdapter(adapter);
    }

    @BindingAdapter("calendarList")
    public static void bindCalendarList(RecyclerView view, CalendarAdapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        view.setLayoutManager(layoutManager);
        view.setHasFixedSize(true);
        view.setAdapter(adapter);
    }

    @BindingAdapter("notesitems")
    public static void bindNotesList(RecyclerView view, NotesAdapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        view.setLayoutManager(layoutManager);
        view.setHasFixedSize(true);
        view.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
        view.setAdapter(adapter);
    }

    @BindingAdapter("photoAdapter")
    public static void bindPhotoAdapter(RecyclerView view, PhotoAdapter adapter) {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(view.getContext(), 3);
        view.setLayoutManager(layoutManager);
        view.setHasFixedSize(true);
        view.setAdapter(adapter);
    }
    /*@BindingAdapter("bind:crewList")
    public static void bindCrewList(RecyclerView view, ObservableArrayList<PickupStopPojo> list) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        view.setLayoutManager(layoutManager);
        view.setAdapter(new PickupStopAdapter(view, list));
    }*/

    /**
     * Unlike the support library app:srcCompat, this will ONLY work with vectors.
     *
     * @param imageView
     * @param resourceId
     */
    @BindingAdapter("android:src")
    public static void setImage(ImageView imageView, int resourceId) {
        Drawable drawable = VectorDrawableCompat.create(imageView.getResources(), resourceId, imageView.getContext().getTheme());
        imageView.setImageDrawable(drawable);
    }

    @BindingAdapter({"coloritems", "viewColorLayout", "selectedColor"})
    public static void bindColorList(RecyclerView view, ObservableArrayList<ColorPojo> colorList, int layoutId, String selectedColor) {
        GridLayoutManager layoutManager = new GridLayoutManager(view.getContext(), 6);
        view.setLayoutManager(layoutManager);
        /* LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        view.setLayoutManager(layoutManager);*/
        view.setAdapter(new ColorListAdapter(view, colorList, layoutId, selectedColor));
    }

    @BindingAdapter("additionalChargesAdapter")
    public static void bindAdditionalChargesAdapter(RecyclerView view, RecyclerView.Adapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        view.setLayoutManager(layoutManager);
        view.setHasFixedSize(true);
        view.setAdapter(adapter);
    }

    @BindingAdapter("valuationChargesAdapter")
    public static void bindValuationChargesAdapter(RecyclerView view, RecyclerView.Adapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        view.setLayoutManager(layoutManager);
        view.setHasFixedSize(true);
        view.setAdapter(adapter);
    }

    @BindingAdapter("packingchargesAdapter")
    public static void bindPackingChargesAdapter(RecyclerView view, RecyclerView.Adapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        view.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
        view.setLayoutManager(layoutManager);
        view.setHasFixedSize(true);
        view.setAdapter(adapter);
    }

    @BindingAdapter("moveChargesAdapter")
    public static void bindmoveChargesAdapter(RecyclerView view, RecyclerView.Adapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        view.setLayoutManager(layoutManager);
//        view.setHasFixedSize(true);
        view.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
        view.setAdapter(adapter);
    }

    @BindingAdapter("valuationAdapter")
    public static void bindValuationItemAdapter(RecyclerView view, ValuationAdapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        view.setLayoutManager(layoutManager);
        view.setHasFixedSize(true);
        view.setAdapter(adapter);
    }


    @BindingAdapter("clock_history_details_adapter")
    public static void bindClockHistoryDetailsAdapter(RecyclerView view, ClockHistoryDetailsAdapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        view.setLayoutManager(layoutManager);
        view.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
        view.setHasFixedSize(true);
        view.setAdapter(adapter);
    }

    @BindingAdapter("clock_history_total_adapter")
    public static void bindClockHistoryTotalAdapter(RecyclerView view, ClockHistoryTotalAdapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        view.setLayoutManager(layoutManager);
        view.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
        view.setHasFixedSize(true);
        view.setAdapter(adapter);
    }

    @BindingAdapter("visibleGone")
    public static void visibleGone(View view, boolean isVisible) {
        if (isVisible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    @BindingAdapter("visibleInvisible")
    public static void visibleInvisible(View view, boolean isVisible) {
        if (isVisible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.INVISIBLE);
        }
    }

    @BindingAdapter(value = {"imageUrl", "placeHolder"}, requireAll = true)
    public static void loadImage(AppCompatImageView view, String url, Drawable placeHolder) {
        Picasso.get().load(url).placeholder(placeHolder).into(view);
    }

    @BindingAdapter("photosTotal")
    public static void photosTotal(AppCompatTextView textView, PhotoModel photoModel) {
        switch (photoModel.getName()) {
            case Constants.INVENTORY_PHOTOS: {
                textView.setText(String.format(textView.getContext().getString(R.string.total_room_format), photoModel.getImages().size()));
                break;
            }

            case Constants.CRATING_PHOTOS: {
                textView.setText(String.format(textView.getContext().getString(R.string.total_items_format), photoModel.getImages().size()));
                break;
            }

            default: {
                textView.setText(String.format(textView.getContext().getString(R.string.total_images_format), photoModel.getImages().size()));
            }
        }
    }

    @BindingAdapter("htmlText")
    public static void htmlText(AppCompatTextView textView, String text) {
        if (text != null) {
            textView.setText(Util.fromHtml(text));
        }
    }

    @BindingAdapter("loadGif")
    public static void loadGif(AppCompatImageView image, Drawable drawable) {
        Glide.with(image.getContext()).asGif()
                .load(drawable)
                .into(image);
    }


}
