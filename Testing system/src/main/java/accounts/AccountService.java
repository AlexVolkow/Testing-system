package accounts;

import dbService.DBException;

import java.util.List;

/**
 * Created by disqustingman on 06.06.16.
 */
public interface AccountService {
    Long addNewUser(UserProfile userProfile) throws DBException;
    void updateUser(UserProfile userProfile) throws DBException;
    UserProfile getUserByEmail(String email) throws DBException;
    UserProfile getUserBySessionId(String sessionId);
    void addSession(String sessionId, UserProfile userProfile);
    boolean isEnter(String sessionId);
    void deleteSession(String sessionId);
    List<UserProfile> getAllUsers() throws DBException;
    void deleteUser(long id) throws DBException;
    String getClasName(long id) throws DBException;
    Long getClasId(String name) throws DBException;
}
