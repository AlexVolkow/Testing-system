package tests;

/**
 * Created by disqustingman on 23.05.16.
 */
public class Question {
    private long id;
    private String text;
    private long themeId;

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public long getThemeId() {
        return themeId;
    }

    public Question(String text, long themeId) {
        this.text = text;
        this.themeId = themeId;
    }


    public Question(long id, String text, long themeId) {

        this.id = id;
        this.themeId = themeId;
        this.text = text;
    }

}
