package me.zzp.calendiary;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import me.zzp.calendiary.calendar.CalendarItem;
import me.zzp.calendiary.calendar.Calendars;
import me.zzp.calendiary.session.Users;
import me.zzp.jac.Dispatcher;

public class Route implements ServletContextListener {
  @Override
  public void contextInitialized(ServletContextEvent e) {
    Dispatcher.add("/", Users.class);
//    Dispatcher.add("/{user:[^/]+/?}", null);
    Dispatcher.add("/{user}/calendars", Calendars.class);
    Dispatcher.add("/{user}/calendar/{id:\\d+}", CalendarItem.class);
//    Dispatcher.add("/{user}/{year}-{month}", null);
//    Dispatcher.add("/{user}/{year}-{month}-{day}", null);
  }

  @Override
  public void contextDestroyed(ServletContextEvent e) {
  }
}
