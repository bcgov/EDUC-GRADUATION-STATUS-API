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
                "\t<head>\n" +
                "\t <style>\n" +
                "\t    body {font-size: .5em; padding: 20px; width: 90%; } table {border-collapse: collapse; width: 100%; }\n" +
                "\t    td, th {border: 1px solid #dddddd; text-align: left; padding: 8px; } tr:nth-child(even) {background-color: #dddddd; }\n" +
                "\t </style>\n" +
                "\t</head>\n" +
                "\t<body>\n" +
                "\t\t<! Header Information>\n" +
                "    \t<h1 style=\"text-align: center;\"><strong>Ministry of Education</strong></h1>\n" +
                "    \t<h2 style=\"text-align: center;\"><strong>Student Achievement Report</strong></h2>\n" +
                "    \t<p style=\"text-align: center;\"><strong>(Transcript Verification)</strong></p>\n" +
                "    \t<p style=\"text-align: center;\">Date: 2020-02-20</p>\n" +
                "    \t<! Student Information>\n" +
                "    \t<h2>Student</h2>\n" +
                "    \t<p><strong>Matthews, John Middle</strong></p>\n" +
                "    \t<p>PEN: 123456789</p>\n" +
                "    \t<p>Graduation Program : 2018</p>\n" +
                "    \t<p>School: Oak Bay High School</p>\n" +
                "    \t<p>Date of Birth: 2009-06-12</p>\n" +
                "    \t<p>Grade: 12</p>\n" +
                "    \t<p>Gender: M</p>\n" +
                "    \t<p>Citizenship: C</p>\n" +
                "    \t<p>Address: 1234 Sunshine Way, Victoria BC V8V 4Y9</p>\n" +
                "    \t<! Student Grad Status>\n" +
                "    \t<h2 style=\"color: blue\">Student Graduation Status </h2>\n" +
                "    \t<h3 style=\"color: blue\">This student has not yet graduated in the 2018 graduation program.</h3>\n" +
                "    \t<! Student List of Courses and Assessments>\n" +
                "        <h2>Student Course / Assessment </h2>\n" +
                "        <table>\n" +
                "        \t<tr style=\"height: 39px;\">\n" +
                "               <th>Course / Assessment Name</th> <th> Course / Assessment Code</th> <th> Graduation Requirement Met</strong</th> <th> Course Type</th> <th> Session Date</th> <th> Interim %</th> <th> Final %</th> <th> Final Letter Grade</th> <th> Credits</td> <th> Comments</th> \n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "               <td>English 10</td> <td>EN 10</td> <td>101</td> <td>&nbsp;</td> <td>2016-06</td> <td>&nbsp;</td> <td>100</td> <td>A</td> <td>4</td> <td>&nbsp;</td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "               <td>English 10</td> <td>EN 10</td> <td>&nbsp;</td> <td>&nbsp;</td> <td>2017-06</td> <td>&nbsp;</td> <td>88</td> <td>A</td> <td>4</td> <td>duplicate</td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "               <td>Physical Education 10</td> <td>PE 10</td> <td>110</td> <td>&nbsp;</td> <td>2018-11</td> <td>&nbsp;</td> <td>100</td> <td>A</td> <td>4</td><td>&nbsp;</td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "               <td>Science 10</td> <td>SC 10</td> <td>&nbsp;</td> <td>&nbsp;</td> <td>2016-01</td> <td>&nbsp;</td> <td>44</td> <td>F</td> <td>4</td> <td>fail</td> </tr>\n" +
                "            <tr>\n" +
                "               <td>Social Studies 11</td> <td>SS 11</td> <td>105</td> <td>&nbsp;</td> <td>2018-01</td> <td>&nbsp;</td> <td>100</td>  <td>A</td> <td>4</td> <td>&nbsp;</td>\n" +
                "            </tr>\n" +
                "        </table><br/><br/><hr/><br/><br/>\n" +
                "        <! Student List of sub-programs>\n" +
                "        <h2>Ancillary Programs</h2>\n" +
                "        <ul> <li>Advanced Placement</li> <li>Career Program: Agriculture</li> </ul>\n" +
                "        <! Student List of Requirements Met>\n" +
                "        <h2>Graduation Program 2018 Requirements Met</h2>\n" +
                "        <ul> <li>Language Arts 10</li> <li>Physical and Health Education 10</li> <li>Social Studies 11 or 12 </li> </ul>\n" +
                "        <! Student List of Requirements Not Met>\n" +
                "        <h2>Graduation Program 2018 Requirements Not Met</h2>\n" +
                "        <ul> <li>No Language Arts 12</li> <li>No Literacy 10 Assessment</li> <li>Fewer than 16 Grade 12 credits</li> </ul>\n" +
                "   </body>\n" +
                "</html>");
    }
}
