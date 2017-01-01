package tests;

/**
 * Created by disqustingman on 07.06.16.
 */
public class RightAnswer {
    private long questionId;
    private String text;

    public RightAnswer(long questionId, String text) {
        this.questionId = questionId;
        this.text = text.toLowerCase();
    }

    public boolean isRight(String answer){
        if (text.equals(answer.toLowerCase())){
            return true;
        }else{
            return false;
        }
    }
    public long getQuestionId() {
        return questionId;
    }

    public String getText() {
        return text;
    }
}
