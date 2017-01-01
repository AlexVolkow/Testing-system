package accounts;

/**
 * Created by disqustingman on 27.05.16.
 */
public class UserProfile {
    private long id;
    private String login;
    private String email;
    private String pass;
    private long clasId;

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }

    public long getClasId() {
        return clasId;
    }

    public long getId(){
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserProfile(String login, String email, String pass, long clasId) {

        this.login = login;
        this.email = email;
        this.pass = pass;
        this.clasId = clasId;
    }

    public UserProfile(long id, String login, String email, String pass, long clasId) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.pass = pass;
        this.clasId = clasId;
    }
}
