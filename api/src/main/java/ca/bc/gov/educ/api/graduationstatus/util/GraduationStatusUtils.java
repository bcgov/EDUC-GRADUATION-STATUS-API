package ca.bc.gov.educ.api.graduationstatus.util;

import ca.bc.gov.educ.api.graduationstatus.model.dto.CourseAchievement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class GraduationStatusUtils {

    private static Logger logger = LoggerFactory.getLogger(GraduationStatusUtils.class);

    public static List<CourseAchievement> markFails(List<CourseAchievement> courseAchievements) {

        for (CourseAchievement ca : courseAchievements) {
            if (GraduationStatusApiConstants.FAIL_GRADE_CODE.compareTo(
                    ca.getFinalLetterGrade()) == 0) {
                ca.setFailed(true);
            }
        }
        return courseAchievements;

        /*return courseAchievements
                .stream()
                .filter(courseAchievement ->
                        GraduationStatusApiConstants.FAIL_GRADE_CODE.compareTo(
                                courseAchievement.getFinalLetterGrade()) != 0)
                .collect(Collectors.toList());*/
    }

    //sort on course_code A on course_grade_level A on final_percent D on course_session A
    // Commented on Rocket-chat[student-achievement-mvp] Kim Gray - Feb 5, 2020 2:18 PM
    public static List<CourseAchievement> markDuplicates(List<CourseAchievement> courseAchievements) {

        //List<CourseAchievement> copy = new ArrayList<CourseAchievement>(courseAchievements);

        logger.debug("###################### Removing Duplicates ######################");

        for (int i=0; i < courseAchievements.size()-1; i++) {

            for (int j=i+1; j<courseAchievements.size(); j++) {

                if (courseAchievements.get(i).getCourseId().equals(courseAchievements.get(j).getCourseId())) {
                    logger.debug("comparing " + courseAchievements.get(i).getCourseAchievementId() + " with "
                            + courseAchievements.get(j).getCourseAchievementId() + " -> Duplicate FOUND - CourseID:"
                            + courseAchievements.get(i).getCourseId());

                    //      IF finalPercent of A greater than finalPercent of B -> SELECT A copy to B
                    //      IF finalPercent of B greater than finalPercent of A -> SELECT B copy to A
                    //      IF finalPercent of A equals to finalPercent of B ->
                    //              IF sessionDate of A is older than sessionDate of B -> SELECT A copy to B
                    //              IF sessionDate of B is older than sessionDate of A -> SELECT B copy  to A
                    //              IF sessionDate of A is equal to sessionDate of B -> SELECT A copy to B

                    if (courseAchievements.get(i).getFinalPercent() > courseAchievements.get(j).getFinalPercent()) {
                        //copy.set(j, copy.get(i));
                        courseAchievements.get(i).setDuplicate(false);
                        courseAchievements.get(j).setDuplicate(true);
                    }else if (courseAchievements.get(i).getFinalPercent() < courseAchievements.get(j).getFinalPercent()) {
                        //courseAchievements.set(i, courseAchievements.get(j));
                        courseAchievements.get(i).setDuplicate(true);
                        courseAchievements.get(j).setDuplicate(false);
                    }else if (courseAchievements.get(i).getFinalPercent() == courseAchievements.get(j).getFinalPercent()) {

                        if (parseDate(courseAchievements.get(i).getSessionDate())
                                .compareTo(parseDate(courseAchievements.get(j).getSessionDate())) < 0) {
                            //courseAchievements.set(j, courseAchievements.get(i));
                            courseAchievements.get(i).setDuplicate(false);
                            courseAchievements.get(j).setDuplicate(true);
                        }else if (parseDate(courseAchievements.get(i).getSessionDate())
                                .compareTo(parseDate(courseAchievements.get(j).getSessionDate())) >= 0) {
                            //courseAchievements.set(i, courseAchievements.get(j));
                            courseAchievements.get(i).setDuplicate(true);
                            courseAchievements.get(j).setDuplicate(false);
                        }
                    }
                }
                else {
                    //Do Nothing
                }
            }
        }

        //Remove duplicates
        //copy = copy.stream().distinct().collect(Collectors.toList());

        return courseAchievements;
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
