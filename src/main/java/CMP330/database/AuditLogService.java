package CMP330.database;

import CMP330.Utils.DateFns;
import CMP330.model.AuditLog;
import CMP330.model.User;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class AuditLogService {
    private static final Database db = new Database();

    public static void Logger(String sql, User user) {

        AuditLog log = new AuditLog(DateFns.customDateFormat(DateFns.DateFormatOptions.Default),
                DateFns.customDateFormat(DateFns.DateFormatOptions.Default),
                sql,
                user
        );
        try {
            db.getAuditLogDao().create(log);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<AuditLog> getAllLogs() {
        try {
            return db.getAuditLogDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
