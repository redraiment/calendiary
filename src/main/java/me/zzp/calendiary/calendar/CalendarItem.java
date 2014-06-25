package me.zzp.calendiary.calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.zzp.ar.DB;
import me.zzp.ar.Record;
import me.zzp.ar.Table;
import me.zzp.jac.Service;

public final class CalendarItem extends Service {
  public CalendarItem(HttpServletRequest request, HttpServletResponse response) {
    super(request, response);
  }

  private boolean auth() {
    Record user = get("whoami");
    if (user == null) {
      return false;
    }
    String name = get("user");
    return name.equalsIgnoreCase(user.getStr("name"));
  }

  private Record getCalendar() {
    if (auth()) {
      Record user = get("whoami");
      
    }
    return null;
  }

  @Override
  public void delete() {
    Record user = get("whoami");
    if (user != null) {
      if (getStr("user").equalsIgnoreCase(user.getStr("name"))) {
        Table Calendar = user.get("calendars");
        if (Calendar != null) {
          Record calendar = Calendar.find(getInt("id"));
          if (calendar != null) {
            calendar.destroy();
          }
        }
      }
    }
  }

  @Override
  public void update() {
    final Record calendar = getCalendar();
    if (calendar != null) {
      if (get("name") != null && get("color") != null) {
        calendar.update("name:", get("name"), "color:", get("color"));
      } else if (get("pid") != null && get("sort") != null) {
        DB dbo = get("dbo");
        dbo.tx(new Runnable() {
          @Override
          public void run() {
            // TODO
          }
        });
      }
    }
  }

  @Override
  public void create() {
    // clone
    Record user = get("whoami");
    if (user != null) {
      DB dbo = get("dbo");
      Table Calendar = dbo.active("calendars");
      Record refer = Calendar.find(getInt("id"));
      if (refer != null) {
        Record owner = refer.get("user");
        String name = String.format("%s - %s", owner.get("name"), refer.get("name"));
        Calendars.createItem(user, 0, name, refer.getStr("color"), refer.getInt("id"));
      }
    }
  }
}
