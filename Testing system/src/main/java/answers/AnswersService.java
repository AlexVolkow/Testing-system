package answers;

import dbService.DBException;

import java.util.List;

/**
 * Created by disqustingman on 08.06.16.
 */
public interface AnswersService {
    Long addAnswer(long userId,long questionId,String text) throws DBException;
    Answer getAnswer(long id) throws DBException;
    Long updateAnswer(long userId,long questionId,String text) throws DBException;
    List<Answer> getUserAnswersByThemeId(long userId,long themeId) throws DBException;
}
