package ar.com.mylback.dal.entities.users;

import jakarta.persistence.*;

@Entity
@Table(name = "stores")
@AttributeOverrides({
        @AttributeOverride(name = "uuid",
                column = @Column(name = "store_uuid", length = 100)
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
}
