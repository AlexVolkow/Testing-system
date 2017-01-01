package dbService.dataSets;

import tests.Question;

/**
 * Created by disqustingman on 27.05.16.
 */
public class QuestionsDataSet {
    private long id;
    private String name;
    private long themeId;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getThemeId() {
        return themeId;
    }

    public QuestionsDataSet(long id,String name, long themeId) {

        this.id = id;
        this.themeId = themeId;
        this.name = name;
    }

    public Question getQuestion(){
        return new Question(id,name,themeId);
    }
}
