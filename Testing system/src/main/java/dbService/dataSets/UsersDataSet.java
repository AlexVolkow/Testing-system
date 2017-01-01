package dbService.dataSets;

import accounts.UserProfile;
import dbService.DBException;
import dbService.DBService;
import dbService.DBServiceImpl;

/**
 * Created by disqustingman on 27.05.16.
 */
public class UsersDataSet {
    private long id;
    private String name;
    private String email;
    private String password;
    private long clasId;

    public UsersDataSet(long id, String name, String email, String password, long clasId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.clasId = clasId;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public long getClasId() {
        return clasId;
    }
    public UserProfile getUserProfile() throws DBException{
        return new UserProfile(id,name,email,password,clasId);
    }
}
