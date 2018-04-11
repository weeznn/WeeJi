package com.weeznn.weeji.util.db;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.weeznn.weeji.util.db.entry.Diary;
import com.weeznn.weeji.util.db.entry.Meeting;
import com.weeznn.weeji.util.db.entry.Note;
import com.weeznn.weeji.util.db.entry.People;

import com.weeznn.weeji.util.db.DiaryDao;
import com.weeznn.weeji.util.db.MeetingDao;
import com.weeznn.weeji.util.db.NoteDao;
import com.weeznn.weeji.util.db.PeopleDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig diaryDaoConfig;
    private final DaoConfig meetingDaoConfig;
    private final DaoConfig noteDaoConfig;
    private final DaoConfig peopleDaoConfig;

    private final DiaryDao diaryDao;
    private final MeetingDao meetingDao;
    private final NoteDao noteDao;
    private final PeopleDao peopleDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        diaryDaoConfig = daoConfigMap.get(DiaryDao.class).clone();
        diaryDaoConfig.initIdentityScope(type);

        meetingDaoConfig = daoConfigMap.get(MeetingDao.class).clone();
        meetingDaoConfig.initIdentityScope(type);

        noteDaoConfig = daoConfigMap.get(NoteDao.class).clone();
        noteDaoConfig.initIdentityScope(type);

        peopleDaoConfig = daoConfigMap.get(PeopleDao.class).clone();
        peopleDaoConfig.initIdentityScope(type);

        diaryDao = new DiaryDao(diaryDaoConfig, this);
        meetingDao = new MeetingDao(meetingDaoConfig, this);
        noteDao = new NoteDao(noteDaoConfig, this);
        peopleDao = new PeopleDao(peopleDaoConfig, this);

        registerDao(Diary.class, diaryDao);
        registerDao(Meeting.class, meetingDao);
        registerDao(Note.class, noteDao);
        registerDao(People.class, peopleDao);
    }
    
    public void clear() {
        diaryDaoConfig.clearIdentityScope();
        meetingDaoConfig.clearIdentityScope();
        noteDaoConfig.clearIdentityScope();
        peopleDaoConfig.clearIdentityScope();
    }

    public DiaryDao getDiaryDao() {
        return diaryDao;
    }

    public MeetingDao getMeetingDao() {
        return meetingDao;
    }

    public NoteDao getNoteDao() {
        return noteDao;
    }

    public PeopleDao getPeopleDao() {
        return peopleDao;
    }

}
