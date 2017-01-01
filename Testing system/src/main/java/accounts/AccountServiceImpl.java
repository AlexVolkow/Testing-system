package accounts;

import dbService.DBException;
import dbService.DBServiceImpl;
import dbService.*;
import dbService.dataSets.UsersDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by disqustingman on 28.05.16.
 */
public class AccountServiceImpl implements AccountService{
    private DBService dbService;
    private final Map<String, UserProfile> sessionIdToProfile;

    public AccountServiceImpl() throws DBException{
        sessionIdToProfile = new HashMap<>();
        this.dbService = DBServiceImpl.instance();
    }

    @Override
    public void deleteUser(long id) throws DBException {
        dbService.deleteUser(id);
    }

    @Override
    public Long getClasId(String name) throws DBException {
        Long clasId = dbService.getClasId(name);
        if (clasId==null){
            throw new NullPointerException();
        }
        return clasId;
    }

    @Override
    public String getClasName(long id) throws DBException {
        return dbService.getClasName(id);
    }

    @Override
    public List<UserProfile> getAllUsers() throws DBException {
        List<UserProfile> res = new ArrayList<>();
        List<UsersDataSet> temp = dbService.getAllUsers();
        if (temp==null){
            return null;
        }
        for (UsersDataSet user:temp){
            res.add(user.getUserProfile());
        }
        return res;
    }

    public Long addNewUser(UserProfile userProfile) throws DBException {
        Long id = dbService.addUser(userProfile.getLogin(),userProfile.getEmail(),userProfile.getPass(),userProfile.getClasId());
        return id;
    }

    @Override
    public void updateUser(UserProfile userProfile) throws DBException {
        dbService.updateUser(userProfile.getId(),userProfile.getLogin(),userProfile.getEmail(),userProfile.getClasId());
    }

    public UserProfile getUserByEmail(String email) throws DBException {
        UsersDataSet user = dbService.getUserByEmail(email);
        if (user!=null){
            return user.getUserProfile();
        }else{
            return null;
        }
    }

    public UserProfile getUserBySessionId(String sessionId) {
        return sessionIdToProfile.get(sessionId);
    }

    public void addSession(String sessionId, UserProfile userProfile) {
        sessionIdToProfile.put(sessionId, userProfile);
    }
    public boolean isEnter(String sessionId){
        if (sessionIdToProfile.containsKey(sessionId)){
            return true;
        }else{
            return false;
        }
    }
    public void deleteSession(String sessionId) {
        sessionIdToProfile.remove(sessionId);
    }
}
