package CMP330.database;

import CMP330.Utils.DateFns;
import CMP330.Utils.UserSingleton;
import CMP330.model.Invoices;
import com.google.inject.Inject;

import java.sql.SQLException;
import java.util.List;

public class InvoiceService {
    private final Database db = new Database();
    private final DateFns date =  new DateFns();
    private Invoices Invoices;

    @Inject
    public InvoiceService() {

    }

    // Create
    public Invoices create(Invoices invoices){
        try{
            db.getInvoiceDao().create(invoices);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AuditLogService.Logger("Created a new invoice for" + invoices.getCustomerId().getName(), UserSingleton.getInstance().getUser());

        return invoices;
    }

    public List<Invoices> getAllInvoicess()  {
        try{
            return db.getInvoiceDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AuditLogService.Logger("Fetched All Invoices", UserSingleton.getInstance().getUser());

        return null;
    }

    public void delete(Invoices Invoices) {
        try{
            db.getInvoiceDao().delete(Invoices);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AuditLogService.Logger("Deleted invoice for customer" + Invoices.getCustomerId().getName(), UserSingleton.getInstance().getUser());

    }

    public Invoices update(Invoices updatedInvoices) {
        try{
            db.getInvoiceDao().update(Invoices);
        }catch (SQLException e){
            e.printStackTrace();
        }
        AuditLogService.Logger("Updated invoice for customer" + updatedInvoices.getCustomerId().getName(), UserSingleton.getInstance().getUser());

        return updatedInvoices;

    }

}
