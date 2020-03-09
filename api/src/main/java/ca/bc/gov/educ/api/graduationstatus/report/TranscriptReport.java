package ca.bc.gov.educ.api.graduationstatus.report;

public class TranscriptReport implements StudentReport {

    StringBuffer html;

    public TranscriptReport() {
        html = new StringBuffer("");
        buildTranscriptReportHtml();
    }

    @Override
    public StringBuffer getHtml() {
        return html;
    }

    public void buildTranscriptReportHtml() {
        html.append("<!DOCTYPE html>\n" +
                "<html>\n" +
                "      \n" +
                "      <head>\n" +
                "         <style>\n" +
                "            body {\n" +
                "            font-size: .5em;\n" +
                "            padding: 20px;\n" +
                "            width: 90%;\n" +
                "            }\n" +
                "            table {\n" +
                "            border-collapse: collapse;\n" +
                "            width: 100%;\n" +
                "            }\n" +
                "            td, th {\n" +
                "            border: 1px solid #dddddd;\n" +
                "            text-align: left;\n" +
                "            padding: 8px;\n" +
                "            }\n" +
                "            tr:nth-child(even) {\n" +
                "            background-color: #dddddd;\n" +
                "            }\n" +
                "         </style>\n" +
                "      </head>\n" +
                "      <body>\n" +
                "\t\t  <! Header Information>\n" +
                "      <h1 style=\"text-align: center;\"><strong>Ministry of Education</strong></h1>\n" +
                "      <h2 style=\"text-align: center;\"><strong>Student Achievement Report</strong></h2>\n" +
                "      <p style=\"text-align: center;\"><strong>(Transcript Verification)</strong></p>\n" +
                "      <p style=\"text-align: center;\">Date: 2020-02-20</p>\n" +
                "      <! Student Information>\n" +
                "      <h2>Student</h2>\n" +
                "      <p><strong>Matthews, John Middle</strong></p>\n" +
                "      <p>PEN: 123456789</p>\n" +
                "      <p>Graduation Program : 2018</p>\n" +
                "      <p>School: Oak Bay High School</p>\n" +
                "      <p>Date of Birth: 2009-06-12</p>\n" +
                "      <p>Grade: 12</p>\n" +
                "      <p>Gender: M</p>\n" +
                "      <p>Citizenship: C</p>\n" +
                "      <p>Address: 1234 Sunshine Way, Victoria BC V8V 4Y9</p>\n" +
                "      <! Student Grad Status>\n" +
                "      <h2 style=\"color: blue\">Student Graduation Status </h2>\n" +
                "      <h3 style=\"color: blue\">This student has not yet graduated in the 2018 graduation program.</h3>\n" +
                "      <! Student List of Courses and Assessments>\n" +
                "         <h2>Student Course / Assessment </h2>\n" +
                "         <table>\n" +
                "            <tr>\n" +
                "            <tr style=\"height: 39px;\">\n" +
                "               <th>Course / Assessment Name</th>\n" +
                "               <th> Course / Assessment Code</th>\n" +
                "               <th> Graduation Requirement Met</strong</th>\n" +
                "               <th> Course Type</th>\n" +
                "               <th> Session Date</th>\n" +
                "               <th> Interim %</th>\n" +
                "               <th> Final %</th>\n" +
                "               <th> Final Letter Grade</th>\n" +
                "               <th> Credits</td>\n" +
                "               <th> Comments</th>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "               <td>English 10</td>\n" +
                "               <td>EN 10</td>\n" +
                "               <td>101</td>\n" +
                "               <td>&nbsp;</td>\n" +
                "               <td>2016-06</td>\n" +
                "               <td>&nbsp;</td>\n" +
                "               <td>100</td>\n" +
                "               <td>A</td>\n" +
                "               <td>4</td>\n" +
                "               <td>&nbsp;</td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "               <td>English 10</td>\n" +
                "               <td>EN 10</td>\n" +
                "               <td>&nbsp;</td>\n" +
                "               <td>&nbsp;</td>\n" +
                "               <td>2017-06</td>\n" +
                "               <td>&nbsp;</td>\n" +
                "               <td>88</td>\n" +
                "               <td>A</td>\n" +
                "               <td>4</td>\n" +
                "               <td>duplicate</td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "               <td>Physical Education 10</td>\n" +
                "               <td>PE 10</td>\n" +
                "               <td>110</td>\n" +
                "               <td>&nbsp;</td>\n" +
                "               <td>2018-11</td>\n" +
                "               <td>&nbsp;</td>\n" +
                "               <td>100</td>\n" +
                "               <td>A</td>\n" +
                "               <td>4</td>\n" +
                "               <td>&nbsp;</td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "               <td>Science 10</td>\n" +
                "               <td>SC 10</td>\n" +
                "               <td>&nbsp;</td>\n" +
                "               <td>&nbsp;</td>\n" +
                "               <td>2016-01</td>\n" +
                "               <td>&nbsp;</td>\n" +
                "               <td>44</td>\n" +
                "               <td>F</td>\n" +
                "               <td>4</td>\n" +
                "               <td>fail</td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "               <td>Social Studies 11</td>\n" +
                "               <td>SS 11</td>\n" +
                "               <td>105</td>\n" +
                "               <td>&nbsp;</td>\n" +
                "               <td>2018-01</td>\n" +
                "               <td>&nbsp;</td>\n" +
                "               <td>100</td>\n" +
                "               <td>A</td>\n" +
                "               <td>4</td>\n" +
                "               <td>&nbsp;</td>\n" +
                "            </tr>\n" +
                "         </table><br/><br/>\n" +
                "         <hr/><br/><br/>\n" +
                "         <! Student List of sub-programs>\n" +
                "         <h2>Ancillary Programs</h2>\n" +
                "         <ul>\n" +
                "            <li>Advanced Placement</li>\n" +
                "            <li>Career Program: Agriculture</li>\n" +
                "         </ul>\n" +
                "         <! Student List of Requirements Met>\n" +
                "         <h2>Graduation Program 2018 Requirements Met</h2>\n" +
                "         <ul>\n" +
                "            <li>Language Arts 10</li>\n" +
                "            <li>Physical and Health Education 10</li>\n" +
                "            <li>Social Studies 11 or 12 </li>\n" +
                "         </ul>\n" +
                "         <! Student List of Requirements Not Met>\n" +
                "         <h2>Graduation Program 2018 Requirements Not Met</h2>\n" +
                "         <ul>\n" +
                "            <li>No Language Arts 12</li>\n" +
                "            <li>No Literacy 10 Assessment</li>\n" +
                "            <li>Fewer than 16 Grade 12 credits</li>\n" +
                "         </ul>\n" +
                "   </body>\n" +
                "</html>");
    }
}
