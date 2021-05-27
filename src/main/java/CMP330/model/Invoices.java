package CMP330.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tblInvoices")
public class Invoices {
    @DatabaseField(generatedId = true)
    private long projectId;

    @DatabaseField(canBeNull = false)

    private String createdAt;

    @DatabaseField(canBeNull = false)
    private String updatedAt;

    @DatabaseField(canBeNull = false,foreign = true,foreignAutoRefresh=true)
    private Customer customerId;

    @DatabaseField(canBeNull = false)
    private Float amountOwed;

    @DatabaseField(canBeNull = false)
    private Float amountPaid;

    @DatabaseField(canBeNull = false)
    private String paymentSchedule;

    Invoices() {

    }

    public Invoices(String createdAt, String updatedAt, Customer customerId, Float amountOwed, Float amountPaid, String paymentSchedule) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.customerId = customerId;
        this.amountOwed = amountOwed;
        this.amountPaid = amountPaid;
        this.paymentSchedule = paymentSchedule;
    }

    public long getProjectId() {
        return projectId;
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

    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }

    public Float getAmountOwed() {
        return amountOwed;
    }

    public void setAmountOwed(Float amountOwed) {
        this.amountOwed = amountOwed;
    }

    public Float getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Float amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getPaymentSchedule() {
        return paymentSchedule;
    }

    public void setPaymentSchedule(String paymentSchedule) {
        this.paymentSchedule = paymentSchedule;
    }
}
