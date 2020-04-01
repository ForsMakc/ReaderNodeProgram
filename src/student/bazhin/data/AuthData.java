package student.bazhin.data;

import java.io.Serializable;

public class AuthData implements Serializable {
    protected String login;
    protected String password;

    public AuthData(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
