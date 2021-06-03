package CMP330.database;

import CMP330.Utils.DateFns;
import CMP330.Utils.UserSingleton;
import CMP330.model.Invoices;
import CMP330.model.Note;
import com.google.inject.Inject;

import java.sql.SQLException;
import java.util.List;

public class NoteService {
    private final Database db = new Database();

    @Inject
    public NoteService() {

    }

    // Create
    public Note create(Note note){
        try{
            db.getNoteDao().create(note);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AuditLogService.Logger("Created a new Note for" + note.getProject().getTitle(), UserSingleton.getInstance().getUser());

        return note;
    }

    public List<Note> getAllNotes()  {
        try{
            return db.getNoteDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AuditLogService.Logger("Fetched All Notes", UserSingleton.getInstance().getUser());

        return null;
    }

    public void delete(Note note) {
        try{
            db.getNoteDao().delete(note);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AuditLogService.Logger("Deleted note: " + note.getId(), UserSingleton.getInstance().getUser());

    }

    public Note update(Note updateNote) {
        try{
            db.getNoteDao().update(updateNote);
        }catch (SQLException e){
            e.printStackTrace();
        }
        AuditLogService.Logger("Updated note: " + updateNote.getId(), UserSingleton.getInstance().getUser());

        return updateNote;

    }

}
