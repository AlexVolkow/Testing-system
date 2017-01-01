package answers;

/**
 * Created by disqustingman on 08.06.16.
 */
public class Answer {
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

    public Answer(long id, long userId, long questionId, String text) {
        this.id = id;
        this.userId = userId;
        this.questionId = questionId;
        this.text = text;
    }
}
