package ar.com.mylback.dal.entities.users;

import jakarta.persistence.*;

@Entity
@Table(name = "stores")
@AttributeOverrides({
        @AttributeOverride(name = "uuid",
                column = @Column(name = "store_uuid", length = 128)
        )
})
public class Store extends User {
    @Column(name = "url", length = 100)
    private String url;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "address", nullable = false, length = 200)
    private String address;

    @Column(name = "is_valid", nullable = false)
    private Boolean isValid;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean isValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }
}
