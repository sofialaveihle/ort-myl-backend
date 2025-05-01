package ar.com.test.loaddata.models;

public class Collection {
    private String id;
    private String order;
    private String slug;
    private String title;
    private String image;
    private String date_release;
    private String date_empire_valid;
    private String custom_bg;
    private String flags;
    private String is_empire;
    private String is_legacy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate_release() {
        return date_release;
    }

    public void setDate_release(String date_release) {
        this.date_release = date_release;
    }

    public String getDate_empire_valid() {
        return date_empire_valid;
    }

    public void setDate_empire_valid(String date_empire_valid) {
        this.date_empire_valid = date_empire_valid;
    }

    public String getCustom_bg() {
        return custom_bg;
    }

    public void setCustom_bg(String custom_bg) {
        this.custom_bg = custom_bg;
    }

    public String getFlags() {
        return flags;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }

    public String getIs_empire() {
        return is_empire;
    }

    public void setIs_empire(String is_empire) {
        this.is_empire = is_empire;
    }

    public String getIs_legacy() {
        return is_legacy;
    }

    public void setIs_legacy(String is_legacy) {
        this.is_legacy = is_legacy;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Collection collection) {
            return this.id.equals(collection.id);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
