package dbService.dataSets;

/**
 * Created by disqustingman on 09.06.16.
 */
public class UsersHasThemesDataSet {
    private long userId;
    private long themeId;

    public long getUserId() {
        return userId;
    }

    public long getThemeId() {
        return themeId;
    }

    public UsersHasThemesDataSet(long userId, long themeId) {

        this.userId = userId;
        this.themeId = themeId;
    }
}
