package me.zzp.calendiary.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.zzp.ar.DB;
import me.zzp.ar.Record;
import me.zzp.ar.Table;
import me.zzp.jac.Service;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

public final class Users extends Service {
  private final Table User;

  public Users(HttpServletRequest request, HttpServletResponse response) {
    super(request, response);
    DB dbo = get("dbo");
    User = dbo.active("users");
  }

  @Override
  public void query() {
    if (get("whoami") == null) {
      forward("/login-view");
    } else {
      set("users", User.all());
      forward("/users-view");
    }
  }

  @Override
  public void create() {
    String email = get("email");
    email = email.toLowerCase();
    String password = get("password");
    password = DigestUtils.md5Hex(Base64.encodeBase64(email.concat(password).getBytes()));

    Record user = User.first("lower(email) = ? and password = ?", email, password);
    if (user != null) {
      session.setAttribute("whoami", user);
    } else {
      set("error", "Incorrect user name or password.");
      set("email", email);
    }
    query();
  }
}
