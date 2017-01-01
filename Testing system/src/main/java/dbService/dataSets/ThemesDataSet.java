package dbService.dataSets;

import dbService.DBException;
import dbService.DBServiceImpl;
import tests.Theme;

/**
 * Created by disqustingman on 27.05.16.
 */
public class ThemesDataSet {
    private long id;
    private String name;
    private long clasId;

    public ThemesDataSet(long id, String name,long clasId) {
        this.id = id;
        this.name = name;
        this.clasId = clasId;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public long getClasId() {
        return clasId;
    }
    public Theme getTheme() throws DBException{
        String clasName = DBServiceImpl.instance().getClasName(clasId);
        return new Theme(id,name,clasName);
    }
}
