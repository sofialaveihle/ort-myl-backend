package ar.com.test.loaddata.models;

public class Format {
    private boolean empire;
    private boolean infantry;
    private boolean vcr;
    private boolean first_era;
    private boolean unified;

    public boolean isEmpire() {
        return empire;
    }

    public void setEmpire(boolean empire) {
        this.empire = empire;
    }

    public boolean isInfantry() {
        return infantry;
    }

    public void setInfantry(boolean infantry) {
        this.infantry = infantry;
    }

    public boolean isVcr() {
        return vcr;
    }

    public void setVcr(boolean vcr) {
        this.vcr = vcr;
    }

    public boolean isFirst_era() {
        return first_era;
    }

    public void setFirst_era(boolean first_era) {
        this.first_era = first_era;
    }

    public boolean isUnified() {
        return unified;
    }

    public void setUnified(boolean unified) {
        this.unified = unified;
    }
}
