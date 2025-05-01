package ar.com.test.loaddata.models;

public class CardDetails {
    private Format valid_formats;
    private Collection edition;

    public Format getValid_formats() {
        return valid_formats;
    }

    public void setValid_formats(Format valid_formats) {
        this.valid_formats = valid_formats;
    }

    public Collection getEdition() {
        return edition;
    }

    public void setEdition(Collection edition) {
        this.edition = edition;
    }
}
