package answers;

import dbService.DBException;
import dbService.DBService;
import dbService.DBServiceImpl;
import dbService.dataSets.AnswersDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by disqustingman on 08.06.16.
 */
public class AnswersServiceImpl implements AnswersService {
    private DBService dbService;

    public AnswersServiceImpl() throws DBException{
        this.dbService = DBServiceImpl.instance();
    }
    @Override
    public Long addAnswer(long userId, long questionId, String text) throws DBException {
        return dbService.addAnswer(userId,questionId,text);
    }

    @Override
    public Answer getAnswer(long id) throws DBException{
        return dbService.getAnswer(id).getAnswer();
    }

    @Override
    public Long updateAnswer(long userId, long questionId, String text) throws DBException {
        return dbService.updateAnswer(userId, questionId, text);
    }

    @Override
    public List<Answer> getUserAnswersByThemeId(long userId, long themeId) throws DBException{
        List<AnswersDataSet> temp = dbService.getUserAnswersByThemeId(userId,themeId);
        List<Answer> res = new ArrayList<>();
        if (temp==null){
            return res;
        }
        for (AnswersDataSet answer : temp){
            res.add(answer.getAnswer());
        }
        return res;
    }
}
