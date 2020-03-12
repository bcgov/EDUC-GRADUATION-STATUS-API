package ca.bc.gov.educ.api.graduationstatus.report;

import java.util.Map;

public class AchievementReport implements StudentReport {

    StringBuffer htmlTemplate;
    Map<String, String> parameters;

    public AchievementReport(Map<String, String> parameters) {
        htmlTemplate = new StringBuffer("");
        this.parameters = parameters;
        buildAchievementReportHtml();
    }

    @Override
    public StringBuffer getHtmlTemplate() {
        return htmlTemplate;
    }

    public void buildAchievementReportHtml() {
        String template = "<html>\n" +
                "\t<head>\n" +
                "\t <style>\n" +
                "\t    body {font-size: .5em; padding: 20px; width: 90%; } table {border-collapse: collapse; width: 100%; }\n" +
                "\t    td, th {border: 1px solid #dddddd; text-align: left; padding: 8px; } tr:nth-child(even) {background-color: #dddddd; }\n" +
                "\t </style>\n" +
                "\t</head>\n" +
                "\t<body>\n" +
                "    \t<h1 style=\"text-align: center;\"><strong>Ministry of Education</strong></h1>\n" +
                "    \t<h2 style=\"text-align: center;\"><strong>Student Achievement Report</strong></h2>\n" +
                "    \t<p style=\"text-align: center;\"><strong>(Transcript Verification)</strong></p>\n" +
                "    \t<p style=\"text-align: center;\">Date: {$UPDATE_DATE}</p>\n" +
                "    \t<h2>Student</h2>\n" +
                "        <p><strong>{$NAME}</strong></p> <p>PEN: {$PEN}</p> <p>Graduation Program : {$GRAD_PROGRAM}</p>\n" +
                "    \t<p>School: {$SCHOOL_NAME}</p> <p>Date of Birth: {$DOB}</p> <p>Grade: {$GRADE}</p>\n" +
                "    \t<p>Gender: {$GENDER}</p> <p>Citizenship: {$CITIZENSHIP}</p> <p>Address: {$ADDRESS}</p>\n" +
                "    \t<h2 style=\"color: blue\">Student Graduation Status </h2>\n" +
                "    \t<h3 style=\"color: blue\">{$GRADUATION_MESSAGE}</h3>\n" +
                "        <h2>Student Course / Assessment </h2>\n" +
                "        <table>\n" +
                "        \t<tr style=\"height: 39px;\">\n" +
                "               <th>Course / Assessment Name</th> <th> Course / Assessment Code</th> <th> Graduation Requirement Met</th> \n" +
                "               <th> Course Type</th> <th> Session Date</th> <th> Interim %</th> <th> Final %</th> <th> Final Letter Grade</th> \n" +
                "               <th> Credits</td> <th> Comments</th> \n" +
                "            </tr>{$TABLE_DATA}\n" +
                "        </table><br/><br/><hr/><br/><br/>\n" +
                "        <h2>Ancillary Programs</h2>\n" +
                "        {$SUB_PROGRAM_LIST}\n" +
                "        <h2>Graduation Program {$GRAD_PROGRAM} Requirements Met</h2>\n" +
                "        {$REQ_MET_LIST}\n" +
                "        <h2>Graduation Program {$GRAD_PROGRAM} Requirements Not Met</h2>\n" +
                "        {$REQ_NOT_MET_LIST}\n" +
                "   </body>\n" +
                "</html>";

        String temp = "";

        for (String k : parameters.keySet()) {
            System.out.println("key: " + k + " value: " + parameters.get(k));
            temp = template.replaceAll("\\{\\$" + k + "\\}", parameters.get(k));
            template = temp;
        }
        htmlTemplate.append(temp);
    }
}
