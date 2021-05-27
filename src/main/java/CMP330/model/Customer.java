package CMP330.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "tblCustomers")
public class Customer {

    @DatabaseField(generatedId = true)
    private long customerId;

    @DatabaseField(canBeNull = false)
    private String createdAt;

    @DatabaseField(canBeNull = false)
    private String updatedAt;

    @DatabaseField(canBeNull = false)
    private String address;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField(canBeNull = false)
    private String postcode;

    @DatabaseField(canBeNull = false)
    private String email;

    Customer() {

    }


    public Customer(String createdAt, String updatedAt, String address, String postcode, String email, String name) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.address = address;
        this.postcode = postcode;
        this.email = email;
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
