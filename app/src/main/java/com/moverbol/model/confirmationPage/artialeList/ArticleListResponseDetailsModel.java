package com.moverbol.model.confirmationPage.artialeList;

import com.google.gson.annotations.SerializedName;
import com.moverbol.constants.Constants;

import java.util.List;

/**
 * Created by AkashM on 29/1/18.
 */

public class ArticleListResponseDetailsModel {

    @SerializedName("item")
    private List<ArticleListItemPojo> normalItems;

    @SerializedName("custom")
    private List<ArticleListItemPojo> customItems;

    @SerializedName("inventory_id")
    private String inventoryId;

    @SerializedName("group_id")
    private String groupId;

    @SerializedName("article_signature")
    private String articleSignature;

    @SerializedName("total_volume")
    private String totalVolume;

    @SerializedName("total_weight")
    private String totalWeight;


    public List<ArticleListItemPojo> getNormalItems() {
        if(normalItems!=null) {
            for (int i = 0; i < normalItems.size(); i++) {
                normalItems.get(i).setItemType(Constants.ArticleListItemTypes.NORMAL_TYPE);
            }
        }
        return normalItems;
    }

    public void setNormalItems(List<ArticleListItemPojo> normalItems) {
        this.normalItems = normalItems;
    }

    public List<ArticleListItemPojo> getCustomItems() {
        if(customItems!=null) {
            for (int i = 0; i < customItems.size(); i++) {
                customItems.get(i).setItemType(Constants.ArticleListItemTypes.CUSTOM_TYPE);
            }
        }
        return customItems;
    }

    public void setCustomItems(List<ArticleListItemPojo> customItems) {

        this.customItems = customItems;
    }

    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArticleSignature() {
        return articleSignature;
    }

    public void setArticleSignature(String articleSignature) {
        this.articleSignature = articleSignature;
    }

    public String getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(String totalVolume) {
        this.totalVolume = totalVolume;
    }

    public String getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(String totalWeight) {
        this.totalWeight = totalWeight;
    }
}
