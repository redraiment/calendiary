package me.zzp.calendiary;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;
import me.zzp.ar.DB;
import me.zzp.ar.Table;
import me.zzp.ar.ex.DBOpenException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

public class Database implements ServletContextListener {
  private DataSource getDataSource() {
    ComboPooledDataSource pool = new ComboPooledDataSource();
    try {
      pool.setDriverClass("org.postgresql.Driver");
    } catch (PropertyVetoException ex) {
      throw new DBOpenException(ex);
    }
    pool.setJdbcUrl("jdbc:postgresql:calendiary");
    pool.setUser("redraiment");
    pool.setPassword("123");

    pool.setMinPoolSize(8);
    pool.setMaxPoolSize(128);
    pool.setMaxIdleTime(5000);
    pool.setAcquireIncrement(8);
    
    return pool;
  }

  private void define(DB dbo) {
    dbo.createTable("users", "email text",
                             "password char(32)",
                             "name text",
                             "admin bool",
                             "key char(32)");
    dbo.createTable("invitations", "inviter_id integer",
                                   "invitee_id integer",
                                   "email text",
                                   "key char(32)");
    dbo.createTable("calendars", "user_id integer",
                                 "parent_id integer",
                                 "name text",
                                 "color char(6)",
                                 "sort integer",
                                 "refer_id integer");
    dbo.createTable("diaries", "calendar_id integer",
                               "date char(10)");
    dbo.createTable("events", "diary_id integer",
                              "period varchar(16)",
                              "title varchar(256)",
                              "content text",
                              "html text");
    dbo.createTable("comments", "event_id integer",
                                "user_id integer",
                                "content text",
                                "html text");
  }

  private void assoc(DB dbo) {
    Table User = dbo.active("users");
    User.hasOne("inviter").in("invitations");
    User.hasMany("invitees").by("invitee_id").in("invitations");
    User.hasMany("calendars").by("user_id");
    User.hasMany("diaries").by("calendar_id").through("calendars");
    User.hasMany("events").by("diary_id").through("diaries");
    User.hasMany("received_comments").by("event_id").through("events");
    User.hasMany("sent_comments").by("user_id");

    Table Invitation = dbo.active("invitations");
    Invitation.belongsTo("inviter").in("users");
    Invitation.belongsTo("invitee").in("users");

    Table Calendar = dbo.active("calendars");
    Calendar.belongsTo("user").in("users");
    Calendar.hasMany("children").by("parent_id").in("calendars");
    Calendar.belongsTo("parent").in("calendars");
    Calendar.hasMany("followers").by("refer_id").in("calendars");
    Calendar.belongsTo("refer").in("calendars");
    Calendar.hasMany("diaries").by("calendar_id");
    Calendar.hasMany("events").by("diary_id").through("diaries");
    Calendar.hasMany("comments").by("event_id").through("events");

    Table Diary = dbo.active("diaries");
    Diary.belongsTo("calendar").in("calendars");
    Diary.belongsTo("user").in("users").through("calendar");
    Diary.hasMany("events").by("diary_id");
    Diary.hasMany("comments").by("event_id").through("events");

    Table Event = dbo.active("events");
    Event.belongsTo("diary").in("diaries");
    Event.belongsTo("calendar").in("calendars").through("diary");
    Event.belongsTo("user").in("users").through("calendar");
    Event.hasMany("comments").by("event_id");

    Table Comment = dbo.active("comments");
    Comment.belongsTo("event").in("events");
    Comment.belongsTo("user").in("users");
  }

  private void seed(DB dbo) {
    Table User = dbo.active("users");

    String email = "redraiment@gmail.com";
    String password = "123";
    String code = DigestUtils.md5Hex(Base64.encodeBase64(email.concat(password).getBytes()));
    User.create("email:", email,
                "password:", code,
                "name:", "Joe",
                "admin:", true);
  }

  private DB open(DataSource pool) {
    DB dbo = DB.open(pool);
    if (dbo.getTableNames().isEmpty()) {
      define(dbo);
      assoc(dbo);
      seed(dbo);
    } else {
      assoc(dbo);
    }
    return dbo;
  }

  @Override
  public void contextInitialized(ServletContextEvent e) {
    DataSource pool = getDataSource();
    DB dbo = open(pool);

    ServletContext context = e.getServletContext();
    context.setAttribute("pool", pool);
    context.setAttribute("dbo", dbo);
  }

  @Override
  public void contextDestroyed(ServletContextEvent e) {
    ServletContext context = e.getServletContext();
    Object o = context.getAttribute("pool");
    if (o != null && o instanceof ComboPooledDataSource) {
      context.removeAttribute("pool");
      ComboPooledDataSource pool = (ComboPooledDataSource)o;
      pool.close();
    }
  }
}
