package ar.com.test.loaddata.models;

public class Card implements Serializable {
    private String id;
    private String edid;
    private String slug;
    private String name;
    private String rarity;
    private String race;
    private String type;
    private String keywords;
    private String cost;
    private String damage;
    private String ability;
    private String flavour;
    private String ed_edid;
    private String ed_slug;
    private Format formats;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEdid() {
        return edid;
    }

    public void setEdid(String edid) {
        this.edid = edid;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDamage() {
        return damage;
    }

    public void setDamage(String damage) {
        this.damage = damage;
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    public String getFlavour() {
        return flavour;
    }

    public void setFlavour(String flavour) {
        this.flavour = flavour;
    }

    public String getEd_edid() {
        return ed_edid;
    }

    public void setEd_edid(String ed_edid) {
        this.ed_edid = ed_edid;
    }

    public String getEd_slug() {
        return ed_slug;
    }

    public void setEd_slug(String ed_slug) {
        this.ed_slug = ed_slug;
    }

    public Format getFormats() {
        return formats;
    }

    public void setFormats(Format formats) {
        this.formats = formats;
    }
}
