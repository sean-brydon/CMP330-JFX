package CMP330.database;

import CMP330.Utils.DateFns;
import CMP330.Utils.UserSingleton;
import CMP330.model.Customer;
import com.google.inject.Inject;

import java.sql.SQLException;
import java.util.List;

public class CustomerService {
    private final Database db = new Database();
    private final DateFns date =  new DateFns();
    private Customer customer;

    @Inject
    public CustomerService() {

    }


    // Create
    public Customer create(Customer customer){
        try{
            db.getCustomerDao().create(customer);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AuditLogService.Logger("Customer created"+customer.getName(), UserSingleton.getInstance().getUser());
        return customer;
    }

    public List<Customer> getAllCustomers()  {
        try{
            return db.getCustomerDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AuditLogService.Logger("Fetched all Customers", UserSingleton.getInstance().getUser());

        return null;
    }

    public void delete(Customer customer) {
        try{
            db.getCustomerDao().delete(customer);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AuditLogService.Logger("Deleted Customer"+customer.getName(), UserSingleton.getInstance().getUser());

    }

    public Customer update(Customer updatedCustomer) {
        try{
            db.getCustomerDao().update(customer);
        }catch (SQLException e){
            e.printStackTrace();
        }

        AuditLogService.Logger("Updated Customer"+customer.getName(), UserSingleton.getInstance().getUser());

        return updatedCustomer;
    }

    public Customer findOneByName(String customer) {
        try{
            return db.getCustomerDao().queryBuilder().where().eq("name",customer).queryForFirst();
        }catch (SQLException e){
            e.printStackTrace();
        }
        AuditLogService.Logger("Found "+customer+" By Name", UserSingleton.getInstance().getUser());

        return null;
    }
}
