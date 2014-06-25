package me.zzp.calendiary.calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.zzp.ar.DB;
import me.zzp.ar.Record;
import me.zzp.ar.Table;
import me.zzp.jac.Service;

public final class Calendars extends Service {
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

    createItem(user, 0, getStr("name"), getStr("color"), 0);
  }

  static void createItem(Record user, int parentId, String name, String color, int refer) {
    Table Calendar = user.get("calendars");
    Record sort = Calendar.select("max(sort) + 1 as sort").where("parent_id = ?").orderBy("sort").one(parentId);
    Calendar.create("parent_id:", parentId,
                    "name:", name,
                    "color:", color,
                    "sort:", sort.get("sort"),
                    "refer_id:", refer);
  }
}
