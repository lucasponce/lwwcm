package org.gatein.lwwcm.portlet.util;

import org.gatein.lwwcm.Wcm;

public class ViewMetadata {
    public enum ViewType {
        POSTS, UPLOADS, TEMPLATES
    }
    private ViewType viewType;
    private boolean filterCategory = false;
    private Long categoryId;
    private int fromIndex;
    private int toIndex;
    private int totalIndex;

    public ViewType getViewType() {
        return viewType;
    }

    public void setViewType(ViewType viewType) {
        this.viewType = viewType;
    }

    public boolean isFilterCategory() {
        return filterCategory;
    }

    public void setFilterCategory(boolean filterCategory) {
        this.filterCategory = filterCategory;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public int getFromIndex() {
        return fromIndex;
    }

    public void setFromIndex(int fromIndex) {
        this.fromIndex = fromIndex;
    }

    public int getToIndex() {
        return toIndex;
    }

    public void setToIndex(int toIndex) {
        this.toIndex = toIndex;
    }

    public int getTotalIndex() {
        return totalIndex;
    }

    public void setTotalIndex(int totalIndex) {
        this.totalIndex = totalIndex;
    }

    public void resetPagination() {
        fromIndex = 0;
        toIndex = (Wcm.VIEWS.MAX_PER_PAGE >= totalIndex ? totalIndex - 1: Wcm.VIEWS.MAX_PER_PAGE -1);
        if (toIndex == -1) toIndex = 0;
    }

    public void rightPage() {
        fromIndex = toIndex + 1;
        toIndex = fromIndex + Wcm.VIEWS.MAX_PER_PAGE - 1;
        if (toIndex >= totalIndex) {
            toIndex = totalIndex - 1;
        }
        if (toIndex < 0) toIndex = 0;
    }

    public void leftPage() {
        toIndex = fromIndex - 1;
        if (toIndex < 0) toIndex = 0;
        fromIndex = toIndex - Wcm.VIEWS.MAX_PER_PAGE + 1;
        if (fromIndex < 0) fromIndex = 0;
    }

    public void checkPagination() {
        if (fromIndex < 0) fromIndex = 0;
        if (toIndex < 0) toIndex = (Wcm.VIEWS.MAX_PER_PAGE > totalIndex? totalIndex - 1: Wcm.VIEWS.MAX_PER_PAGE - 1);
        if ((toIndex - fromIndex) +1 < Wcm.VIEWS.MAX_PER_PAGE) {
            toIndex = fromIndex + Wcm.VIEWS.MAX_PER_PAGE;
        }
        if (fromIndex >= totalIndex) fromIndex = totalIndex - 1;
        if (toIndex >= totalIndex) toIndex = totalIndex - 1;
    }
}
