package CMP330.database;

import CMP330.Utils.DateFns;
import CMP330.model.Customer;
import CMP330.model.User;
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

        return customer;
    }

    public List<Customer> getAllCustomers()  {
        try{
            return db.getCustomerDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void delete(Customer customer) {
        try{
            db.getCustomerDao().delete(customer);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Customer update(Customer updatedCustomer) {
        try{
            db.getCustomerDao().update(customer);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return updatedCustomer;
    }
}
