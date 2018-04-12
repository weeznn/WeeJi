package com.weeznn.weeji.util.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.weeznn.weeji.util.db.entry.Note;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "NOTE".
*/
public class NoteDao extends AbstractDao<Note, Long> {

    public static final String TABLENAME = "NOTE";

    /**
     * Properties of entity Note.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property _noteID = new Property(0, long.class, "_noteID", true, "_id");
        public final static Property Time = new Property(1, String.class, "time", false, "TIME");
        public final static Property Image = new Property(2, String.class, "image", false, "IMAGE");
        public final static Property Sub = new Property(3, String.class, "sub", false, "SUB");
        public final static Property Source = new Property(4, String.class, "source", false, "SOURCE");
    }


    public NoteDao(DaoConfig config) {
        super(config);
    }
    
    public NoteDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"NOTE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY NOT NULL ," + // 0: _noteID
                "\"TIME\" TEXT," + // 1: time
                "\"IMAGE\" TEXT," + // 2: image
                "\"SUB\" TEXT," + // 3: sub
                "\"SOURCE\" TEXT);"); // 4: source
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"NOTE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Note entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.get_noteID());
 
        String time = entity.getTime();
        if (time != null) {
            stmt.bindString(2, time);
        }
 
        String image = entity.getImage();
        if (image != null) {
            stmt.bindString(3, image);
        }
 
        String sub = entity.getSub();
        if (sub != null) {
            stmt.bindString(4, sub);
        }
 
        String source = entity.getSource();
        if (source != null) {
            stmt.bindString(5, source);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Note entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.get_noteID());
 
        String time = entity.getTime();
        if (time != null) {
            stmt.bindString(2, time);
        }
 
        String image = entity.getImage();
        if (image != null) {
            stmt.bindString(3, image);
        }
 
        String sub = entity.getSub();
        if (sub != null) {
            stmt.bindString(4, sub);
        }
 
        String source = entity.getSource();
        if (source != null) {
            stmt.bindString(5, source);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public Note readEntity(Cursor cursor, int offset) {
        Note entity = new Note( //
            cursor.getLong(offset + 0), // _noteID
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // time
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // image
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // sub
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) // source
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Note entity, int offset) {
        entity.set_noteID(cursor.getLong(offset + 0));
        entity.setTime(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setImage(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setSub(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setSource(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Note entity, long rowId) {
        entity.set_noteID(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Note entity) {
        if(entity != null) {
            return entity.get_noteID();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Note entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}