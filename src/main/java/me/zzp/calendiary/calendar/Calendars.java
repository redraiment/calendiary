package me.zzp.calendiary.calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.zzp.ar.DB;
import me.zzp.ar.Record;
import me.zzp.ar.Table;
import me.zzp.jac.Service;

public class Calendars extends Service {
  private final Table User;

  public Calendars(HttpServletRequest request, HttpServletResponse response) {
    super(request, response);
    DB dbo = get("dbo");
    User = dbo.active("users");
  }

  @Override
  public void query() {
    String name = get("user");
    Record user = User.findA("lower(name)", name.toLowerCase());
    Table Calendar = user.get("calendars");
    set("calendars", Calendar.all());
    forward("/api-calendars-view");
  }

  @Override
  public void create() {
    if (get("whoami") == null) {
      redirect("/");
    }

    Record user = get("whoami");
    String name = get("user");
    if (!name.equalsIgnoreCase(user.getStr("name"))) {
      redirect("/");
    }

    Table Calendar = user.get("calendars");
    Record sort = Calendar.select("count(*) as sort").where("parent_id = ?").orderBy("sort").one(0);
    Calendar.create("parent_id:", 0,
                    "name:", get("name"),
                    "color:", get("color"),
                    "sort:", (long)sort.get("sort") + 1,
                    "refer_id:", 0);
  }
}
