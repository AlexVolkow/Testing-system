package dbService.dataSets;

import answers.Answer;

/**
 * Created by disqustingman on 27.05.16.
 */
public class AnswersDataSet {
    private long id;
    private long userId;
    private long questionId;
    private String text;

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public long getQuestionId() {
        return questionId;
    }

    public String getText() {
        return text;
    }

    public AnswersDataSet(long id, long userId, long questionId, String text) {

        this.id = id;
        this.userId = userId;
        this.questionId = questionId;
        this.text = text;
    }
    public Answer getAnswer(){
        return new Answer(id,userId,questionId,text);
    }
}
