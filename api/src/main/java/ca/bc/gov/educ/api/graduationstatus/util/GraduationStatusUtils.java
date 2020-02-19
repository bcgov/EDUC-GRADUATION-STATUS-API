package ca.bc.gov.educ.api.graduationstatus.util;

import ca.bc.gov.educ.api.graduationstatus.model.dto.CourseAchievement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GraduationStatusUtils {

    private static Logger logger = LoggerFactory.getLogger(GraduationStatusUtils.class);

    public static List<CourseAchievement> removeFails(List<CourseAchievement> courseAchievements) {

        return courseAchievements
                .stream()
                .filter(courseAchievement -> "C+".compareTo(courseAchievement.getFinalLetterGrade()) != 0)
                .collect(Collectors.toList());
    }

    //sort on course_code A on course_grade_level A on final_percent D on course_session A
    // Commented on Rocket-chat[student-achievement-mvp] Kim Gray - Feb 5, 2020 2:18 PM
    public static List<CourseAchievement> removeDuplicates(List<CourseAchievement> courseAchievements) {

        List<CourseAchievement> copy = new ArrayList<CourseAchievement>(courseAchievements);

        logger.debug("\n###################### Removing Duplicates ######################");

        for (int i=0; i < copy.size()-1; i++) {

            for (int j=i+1; j<copy.size(); j++) {

                if (copy.get(i).getCourseId().equals(copy.get(j).getCourseId())) {
                    logger.debug("comparing " + copy.get(i).getCourseAchievementId() + " with "
                            + copy.get(j).getCourseAchievementId() + " -> Duplicate FOUND - CourseID:" + copy.get(i).getCourseId());

                    //TODO: If finalPercent of A greater than finalPercent of B -> SELECT A copy to B
                    //      IF finalPercent of B greater than finalPercent of A -> SELECT B copy to A
                    //      IF finalPercent of A equals to finalPercent of B ->
                    //              IF sessionDate of A is older than sessionDate of B -> SELECT A copy to B
                    //              IF sessionDate of B is older than sessionDate of A -> SELECT B copy  to A
                    //              IF sessionDate of A is equal to sessionDate of B -> SELECT A copy to B

                    if (copy.get(i).getFinalPercent() > copy.get(j).getFinalPercent()) {
                        copy.set(j, copy.get(i));
                    }else if (copy.get(i).getFinalPercent() < copy.get(j).getFinalPercent()) {
                        copy.set(i, copy.get(j));
                    }else if (copy.get(i).getFinalPercent() == copy.get(j).getFinalPercent()) {

                        if (parseDate(copy.get(i).getSessionDate())
                                .compareTo(parseDate(copy.get(j).getSessionDate())) < 0) {
                            copy.set(j, copy.get(i));
                        }else if (parseDate(copy.get(i).getSessionDate())
                                .compareTo(parseDate(copy.get(j).getSessionDate())) > 0) {
                            copy.set(i, copy.get(j));
                        }else if (parseDate(copy.get(i).getSessionDate())
                                .compareTo(parseDate(copy.get(j).getSessionDate())) == 0) {
                            copy.set(j, copy.get(i));
                        }
                    }
                }
                else {
                    //Do Nothing
                }
            }
        }

        //Remove duplicates
        copy = copy.stream().distinct().collect(Collectors.toList());

        return copy;
    }

    public static String formatDate (Date date) {
        if (date == null)
            return null;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(GraduationStatusApiConstants.DEFAULT_DATE_FORMAT);
        return simpleDateFormat.format(date);
    }

    public static String formatDate (Date date, String dateFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat.format(date);
    }

    public static Date parseDate (String dateString) {
        if (dateString == null || "".compareTo(dateString) == 0)
            return null;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(GraduationStatusApiConstants.DEFAULT_DATE_FORMAT);
        Date date = new Date();

        try {
            date = simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static Date parseDate (String dateString, String dateFormat) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        Date date = new Date();

        try {
            date = simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }
}
