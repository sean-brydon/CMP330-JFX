package CMP330.database;

import CMP330.model.*;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class Database {
    private final static String DATABASE_URL = "jdbc:sqlite:CMP330_TEST.db";

    //    Setup Database access objects
    private Dao<AuditLog, Integer> auditLogDao;
    private Dao<Customer, Integer> customerDao;
    private Dao<Invoices, Integer> invoiceDao;
    private Dao<Project, Integer> projectDao;
    private Dao<Tasks, Integer> taskDao;
    private Dao<User, Integer> userDao;
    private ConnectionSource connectionSource;

    // Handle Connection
    public Database() {
        try {
            // Get Connection to the DB
            this.connectionSource
                    = new JdbcPooledConnectionSource(DATABASE_URL);

            setupDAOs(this.connectionSource);
        } catch (SQLException throwables) {
            // Throw any errors
            // TODO: This causes console spam whenever we throw the user an error message
            throwables.printStackTrace();
        }
    }

    private void setupDAOs(ConnectionSource connectionSource) throws SQLException {
//        // Setup DAOs for all of our entites
        auditLogDao = DaoManager.createDao(connectionSource, AuditLog.class);
        customerDao = DaoManager.createDao(connectionSource, Customer.class);
        invoiceDao = DaoManager.createDao(connectionSource, Invoices.class);
        projectDao = DaoManager.createDao(connectionSource, Project.class);
        taskDao = DaoManager.createDao(connectionSource, Tasks.class);
        userDao = DaoManager.createDao(connectionSource, User.class);
        // Setup classes

        setupTablesIfNotExist(connectionSource);
    }

    private void setupTablesIfNotExist(ConnectionSource connectionSource) throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, AuditLog.class);
        TableUtils.createTableIfNotExists(connectionSource, Customer.class);
        TableUtils.createTableIfNotExists(connectionSource, Invoices.class);
        TableUtils.createTableIfNotExists(connectionSource, Project.class);
        TableUtils.createTableIfNotExists(connectionSource, Tasks.class);
        TableUtils.createTableIfNotExists(connectionSource, User.class);
    }

    // Get connection property


    public ConnectionSource getConnectionSource() {
        return connectionSource;
    }

    public Dao<AuditLog, Integer> getAuditLogDao() {
        return auditLogDao;
    }

    public Dao<Customer, Integer> getCustomerDao() {
        return customerDao;
    }

    public Dao<Invoices, Integer> getInvoiceDao() {
        return invoiceDao;
    }

    public Dao<Project, Integer> getProjectDao() {
        return projectDao;
    }

    public Dao<Tasks, Integer> getTaskDao() {
        return taskDao;
    }

    public Dao<User, Integer> getUserDao() {
        return userDao;
    }
}
