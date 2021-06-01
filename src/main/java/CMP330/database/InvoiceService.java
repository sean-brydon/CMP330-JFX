package CMP330.database;

import CMP330.Utils.DateFns;
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

        return invoices;
    }

    public List<Invoices> getAllInvoicess()  {
        try{
            return db.getInvoiceDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void delete(Invoices Invoices) {
        try{
            db.getInvoiceDao().delete(Invoices);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Invoices update(Invoices updatedInvoices) {
        try{
            db.getInvoiceDao().update(Invoices);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return updatedInvoices;
    }

    public Invoices findOneByName(String Invoices) {
        try{
            return db.getInvoiceDao().queryBuilder().where().eq("name",Invoices).queryForFirst();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
