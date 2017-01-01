package dbService.dataSets;

import tests.RightAnswer;

/**
 * Created by disqustingman on 27.05.16.
 */
public class RightAnswersDataSet {
    private long id;
    private long questionId;
    private String text;

    public long getId() {
        return id;
    }

    public long getQuestionId() {
        return questionId;
    }

    public String getText() {
        return text;
    }

    public RightAnswersDataSet(long id, long questionId, String text) {
        this.id = id;
        this.questionId = questionId;
        this.text = text;
    }
    public RightAnswer getRightAnswer(){
        return new RightAnswer(questionId,text);
    }
}
