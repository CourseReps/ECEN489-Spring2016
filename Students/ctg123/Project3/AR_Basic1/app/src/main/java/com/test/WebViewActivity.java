package com.test;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by Chaance on 4/29/2016.
 */
public class WebViewActivity extends Activity {

    private WebView webView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        webView = (WebView) findViewById(R.id.webview);
       String myHTMLString = "\n" +
               "<html>\n" +
               "<head>\n" +
               "<title>Lunarfinder Calculator</title>\n" +
               "<link rel=\"stylesheet\" type=\"text/css\" href=\"../standard.css\">\n" +
               "<SCRIPT language=JavaScript SRC=\"../feedback.js\"></SCRIPT>\n" +
               "\n" +
               "<script language=JavaScript>\n" +
               "<!-- hide\n" +
               "\n" +
               "var n0 = parseInt( \"0\" );\n" +
               "var f0 = parseFloat( \"0.0\" );\n" +
               "var AG = f0;   // Moon's age\n" +
               "var DI = f0;   // Moon's distance in earth radii\n" +
               "var LA = f0;   // Moon's ecliptic latitude\n" +
               "var LO = f0;   // Moon's ecliptic longitude\n" +
               "var Phase = \" \";\n" +
               "var Zodiac = \" \";\n" +
               "\n" +
               "function initialize()\n" +
               "{\n" +
               "    var d = new Date();\n" +
               "\n" +
               "    document.calc.year.value  = d.getFullYear();\n" +
               "    document.calc.month.value = d.getMonth() + 1;\n" +
               "    document.calc.day.value   = d.getDate();\n" +
               "}\n" +
               "\n" +
               "function calculate()\n" +
               "{\n" +
               "    var year  = parseInt( document.calc.year.value, 10 );    \n" +
               "    var month = parseInt( document.calc.month.value, 10 );    \n" +
               "    var day   = parseInt( document.calc.day.value, 10 );\n" +
               "\n" +
               "    if( !isdayofmonth( year, month, day ) )\n" +
               "    {\n" +
               "        alert( \"Invalid date\" );\n" +
               "        return;\n" +
               "    }\n" +
               "\n" +
               "    moon_posit( year, month, day );\n" +
               "    document.calc.age.value = round2( AG );\n" +
               "    document.calc.dst.value = round2( DI );\n" +
               "    document.calc.faz.value = Phase;\n" +
               "    document.calc.lat.value = round2( LA );\n" +
               "    document.calc.lon.value = round2( LO );\n" +
               "    document.calc.sgn.value = Zodiac;\n" +
               "}\n" +
               "\n" +
               "var n28 = parseInt( \"28\" );\n" +
               "var n30 = parseInt( \"30\" );\n" +
               "var n31 = parseInt( \"31\" );\n" +
               "var dim = new Array( n31, n28, n31, n30, n31, n30, n31, n31, n30, n31, n30, n31 );\n" +
               "\n" +
               "function isdayofmonth( y, m, d )\n" +
               "{\n" +
               "    if( m != 2 )\n" +
               "    {\n" +
               "        if( 1 <= d && d <= dim[m-1] )\n" +
               "            return true;\n" +
               "        else\n" +
               "            return false;\n" +
               "    }\n" +
               "\n" +
               "    var feb = dim[1];\n" +
               " \n" +
               "    if( isleapyear( y ) )\n" +
               "        feb += 1;                                   // is leap year\n" +
               "\n" +
               "    if( 1 <= d && d <= feb )\n" +
               "        return true;\n" +
               "\n" +
               "    return false;       \n" +
               "}\n" +
               "\n" +
               "function isleapyear( y )\n" +
               "{\n" +
               "    var x = Math.floor( y - 4*Math.floor( y/4 ) );\n" +
               "    var w = Math.floor( y - 100*Math.floor( y/100 ) );\n" +
               "    var z = Math.floor( y - 400*Math.floor( y/400 ) );\n" +
               "\n" +
               "    if( x == 0 )                           // possible leap year\n" +
               "    {\n" +
               "        if( w == 0 && z != 0 )\n" +
               "            return false;                  // not leap year\n" +
               "        else\n" +
               "            return true;                   // is leap year\n" +
               "    }\n" +
               "\n" +
               "    return false;\n" +
               "}\n" +
               "\n" +
               "function backup( n )\n" +
               "{\n" +
               "    var year = parseInt( document.calc.year.value, 10 );\n" +
               "    var month = parseInt( document.calc.month.value, 10 );\n" +
               "    var day = parseInt( document.calc.day.value, 10 );\n" +
               "\n" +
               "    switch( n )\n" +
               "    {\n" +
               "    case 1:\n" +
               "        document.calc.year.value = year - 1;\n" +
               "        calculate();\n" +
               "        break;\n" +
               "    case 2:\n" +
               "        if( month < 2 )\n" +
               "        {\n" +
               "            document.calc.month.value = 12;\n" +
               "            document.calc.year.value = year - 1;\n" +
               "        }\n" +
               "        else\n" +
               "            document.calc.month.value = month - 1;\n" +
               "        calculate();\n" +
               "        break;\n" +
               "    case 3:\n" +
               "        if( day < 2 )\n" +
               "        {\n" +
               "            if( month < 2 )\n" +
               "            {\n" +
               "                document.calc.month.value = 12;\n" +
               "                document.calc.year.value = year - 1;\n" +
               "            }\n" +
               "            else\n" +
               "                document.calc.month.value = month - 1;\n" +
               "            \n" +
               "            month = parseInt( document.calc.month.value, 10 );\n" +
               "            if( month == 2 && isleapyear( year ) )\n" +
               "                document.calc.day.value = 29;\n" +
               "            else \n" +
               "                document.calc.day.value = dim[month-1];\n" +
               "        }\n" +
               "        else\n" +
               "            document.calc.day.value = day - 1;\n" +
               "        calculate();\n" +
               "        break;\n" +
               "    }\n" +
               "}\n" +
               "\n" +
               "function advance( n )\n" +
               "{\n" +
               "    var year = parseInt( document.calc.year.value, 10 );\n" +
               "    var month = parseInt( document.calc.month.value, 10 );\n" +
               "    var day = parseInt( document.calc.day.value, 10 );\n" +
               "\n" +
               "    switch( n )\n" +
               "    {\n" +
               "    case 1:\n" +
               "        document.calc.year.value = year + 1;\n" +
               "        calculate();\n" +
               "        break;\n" +
               "    case 2:\n" +
               "        if( month < 12 )\n" +
               "            document.calc.month.value = month + 1;\n" +
               "        else\n" +
               "        {\n" +
               "            document.calc.month.value = 1;\n" +
               "            document.calc.year.value = year + 1;\n" +
               "        }\n" +
               "        calculate();\n" +
               "        break;\n" +
               "    case 3:\n" +
               "        if( isdayofmonth( year, month, day + 1 ) )\n" +
               "            document.calc.day.value = day + 1;\n" +
               "        else\n" +
               "        {\n" +
               "            if( month < 12 )\n" +
               "                document.calc.month.value = month + 1;\n" +
               "            else\n" +
               "            {\n" +
               "                document.calc.month.value = 1;\n" +
               "                document.calc.year.value = year + 1;\n" +
               "            }\n" +
               "\n" +
               "            document.calc.day.value = 1;\n" +
               "        }\n" +
               "        calculate();\n" +
               "        break;\n" +
               "    }\n" +
               "}\n" +
               "\n" +
               "// compute moon position and phase\n" +
               "function moon_posit( Y, M, D )\n" +
               "{\n" +
               "    var YY = n0;\n" +
               "    var MM = n0;\n" +
               "    var K1 = n0; \n" +
               "    var K2 = n0; \n" +
               "    var K3 = n0;\n" +
               "    var JD = n0;\n" +
               "    var IP = f0;\n" +
               "    var DP = f0;\n" +
               "    var NP = f0;\n" +
               "    var RP = f0;\n" +
               "    \n" +
               "    // calculate the Julian date at 12h UT\n" +
               "    YY = Y - Math.floor( ( 12 - M ) / 10 );       \n" +
               "    MM = M + 9; \n" +
               "    if( MM >= 12 ) MM = MM - 12;\n" +
               "    \n" +
               "    K1 = Math.floor( 365.25 * ( YY + 4712 ) );\n" +
               "    K2 = Math.floor( 30.6 * MM + 0.5 );\n" +
               "    K3 = Math.floor( Math.floor( ( YY / 100 ) + 49 ) * 0.75 ) - 38;\n" +
               "    \n" +
               "    JD = K1 + K2 + D + 59;                  // for dates in Julian calendar\n" +
               "    if( JD > 2299160 ) JD = JD - K3;        // for Gregorian calendar\n" +
               "        \n" +
               "    // calculate moon's age in days\n" +
               "    IP = normalize( ( JD - 2451550.1 ) / 29.530588853 );\n" +
               "    AG = IP*29.53;\n" +
               "    \n" +
               "    if(      AG <  1.84566 ) Phase = \"NEW\";\n" +
               "    else if( AG <  5.53699 ) Phase = \"Evening crescent\";\n" +
               "    else if( AG <  9.22831 ) Phase = \"First quarter\";\n" +
               "    else if( AG < 12.91963 ) Phase = \"Waxing gibbous\";\n" +
               "    else if( AG < 16.61096 ) Phase = \"FULL\";\n" +
               "    else if( AG < 20.30228 ) Phase = \"Waning gibbous\";\n" +
               "    else if( AG < 23.99361 ) Phase = \"Last quarter\";\n" +
               "    else if( AG < 27.68493 ) Phase = \"Morning crescent\";\n" +
               "    else                     Phase = \"NEW\";\n" +
               "\n" +
               "    IP = IP*2*Math.PI;                      // Convert phase to radians\n" +
               "\n" +
               "    // calculate moon's distance\n" +
               "    DP = 2*Math.PI*normalize( ( JD - 2451562.2 ) / 27.55454988 );\n" +
               "    DI = 60.4 - 3.3*Math.cos( DP ) - 0.6*Math.cos( 2*IP - DP ) - 0.5*Math.cos( 2*IP );\n" +
               "\n" +
               "    // calculate moon's ecliptic latitude\n" +
               "    NP = 2*Math.PI*normalize( ( JD - 2451565.2 ) / 27.212220817 );\n" +
               "    LA = 5.1*Math.sin( NP );\n" +
               "\n" +
               "    // calculate moon's ecliptic longitude\n" +
               "    RP = normalize( ( JD - 2451555.8 ) / 27.321582241 );\n" +
               "    LO = 360*RP + 6.3*Math.sin( DP ) + 1.3*Math.sin( 2*IP - DP ) + 0.7*Math.sin( 2*IP );\n" +
               "\n" +
               "    if(      LO <  33.18 ) Zodiac = \"Pisces\";\n" +
               "    else if( LO <  51.16 ) Zodiac = \"Aries\";\n" +
               "    else if( LO <  93.44 ) Zodiac = \"Taurus\";\n" +
               "    else if( LO < 119.48 ) Zodiac = \"Gemini\";\n" +
               "    else if( LO < 135.30 ) Zodiac = \"Cancer\";\n" +
               "    else if( LO < 173.34 ) Zodiac = \"Leo\";\n" +
               "    else if( LO < 224.17 ) Zodiac = \"Virgo\";\n" +
               "    else if( LO < 242.57 ) Zodiac = \"Libra\";\n" +
               "    else if( LO < 271.26 ) Zodiac = \"Scorpio\";\n" +
               "    else if( LO < 302.49 ) Zodiac = \"Sagittarius\";\n" +
               "    else if( LO < 311.72 ) Zodiac = \"Capricorn\";\n" +
               "    else if( LO < 348.58 ) Zodiac = \"Aquarius\";\n" +
               "    else                   Zodiac = \"Pisces\";\n" +
               "\n" +
               "    // so longitude is not greater than 360!\n" +
               "    if ( LO > 360 ) LO = LO - 360;\n" +
               "}\n" +
               "\n" +
               "// round to 2 decimal places    \n" +
               "function round2( x )\n" +
               "{\n" +
               "    return ( Math.round( 100*x )/100.0 );\n" +
               "}\n" +
               "    \n" +
               "// normalize values to range 0...1    \n" +
               "function normalize( v )\n" +
               "{\n" +
               "    v = v - Math.floor( v  ); \n" +
               "    if( v < 0 )\n" +
               "        v = v + 1;\n" +
               "        \n" +
               "    return v;\n" +
               "}\n" +
               "\n" +
               "// clear input\n" +
               "function allclear()\n" +
               "{\n" +
               "    document.calc.year.value='0';\n" +
               "    document.calc.month.value='0';\n" +
               "    document.calc.day.value='0';\n" +
               "}\n" +
               "\n" +
               "function Moondata(distance, ecliptic_latitude, ecliptic_longitude, Moon_phase) {\n" +
               "this distance = distance;\n" +
               "this ecliptic_latitude = ecliptic_latitude;\n" +
               "this ecliptic_longitude = ecliptic_longitude;\n" +
               "this Moon_phase = Moon_phase;\n" +
               "}\n" +
               "// unhide -->\n" +
               "</script>\n" +
               "\n" +
               "</head>\n" +
               "<body bgcolor=\"maroon\" text=\"white\" onLoad=initialize()>\n" +
               "\n" +
               "\n" +
               "<h2>Lunarfinder Calculator</h2>\n" +
               "\n" +
               "<h4>ECEN 489\n" +
               "\tProject 3</h4>\n" +
               "\n" +
               "<center>\n" +
               "<form name=\"calc\">\n" +
               "<table>\n" +
               "<tr>\n" +
               "<td><input type=\"button\" VALUE=\"<<\" onClick=\"backup( 1 )\">\n" +
               "<td align=right>year \n" +
               "<td><input type=\"text\" value=\"0\" NAME=\"year\" size=\"5\" maxlength=\"30\">\n" +
               "<td><input type=\"button\" VALUE=\">>\" onClick=\"advance( 1 )\">\n" +
               "<tr>\n" +
               "<td><input type=\"button\" VALUE=\"<<\" onClick=\"backup( 2 )\">\n" +
               "<td align=right>month \n" +
               "<td><input type=\"text\" value=\"0\" NAME=\"month\" size=\"5\" maxlength=\"30\">\n" +
               "<td><input type=\"button\" VALUE=\">>\" onClick=\"advance( 2 )\">\n" +
               "<tr>\n" +
               "<td><input type=\"button\" VALUE=\"<<\" onClick=\"backup( 3 )\">\n" +
               "<td align=right>day \n" +
               "<td><input type=\"text\" value=\"0\" NAME=\"day\" size=\"5\" maxlength=\"30\">\n" +
               "<td><input type=\"button\" VALUE=\">>\" onClick=\"advance( 3 )\">\n" +
               "</table>\n" +
               "<p><input type=\"button\" VALUE=\"calculate\" onClick=\"calculate()\">\n" +
               "<table>\n" +
               "<tr>\n" +
               "<td align=right>Moon's age from new moon : \n" +
               "<td><input type=\"text\" value=\"0\" NAME=\"age\" size=\"5\" maxlength=\"30\">\n" +
               "<td> days\n" +
               "<td align=right>Moon is : \n" +
               "<td><input type=\"text\" value=\"0\" NAME=\"faz\" size=\"20\" maxlength=\"30\">\n" +
               "<tr>\n" +
               "<td align=right>Distance : \n" +
               "<td><input type=\"text\" value=\"0\" NAME=\"dst\" size=\"5\" maxlength=\"30\">\n" +
               "<td> Earth radii\n" +
               "<tr>\n" +
               "<td align=right>Ecliptic latitude : \n" +
               "<td><input type=\"text\" value=\"0\" NAME=\"lat\" size=\"5\" maxlength=\"30\">\n" +
               "<td> degrees \n" +
               "<tr>\n" +
               "<td align=right>Ecliptic longitude :\n" +
               "<td><input type=\"text\" value=\"0\" NAME=\"lon\" size=\"5\" maxlength=\"30\">\n" +
               "<td> degrees\n" +
               "\n" +
               "</table>\n" +
               "<p><input type=\"button\" VALUE=\"clear\" onClick=\"allclear()\">\n" +
               "</form>\n" +
               "</center>\n" +
               "\n" +
               "<hr>\n" +
               "\n" +
               "<h3><a name=contents>Contents</h3>\n" +
               "<ol>\n" +
               "<li><a href=\"#about\">About</a>\n" +
               "<li><a href=\"#source\">Source code</a>\n" +
               "<li><a href=\"#discussion\">Discussion</a>\n" +
               "</ol>\n" +
               "\n" +
               "<hr>\n" +
               "\n" +
               "<h4>1. <a name=about>About</h4>\n" +
               "\n" +
               "<p>This JavaScript program calculates the phase and position of the moon for a given date. It was adapted from a BASIC program from the <i>Astronomical Computing</i> column of Sky & Telescope</a>, April 1994. \n" +
               "\n" +
               "<p>The calculator will initialize itself, if possible, to your computer's day, month, and year. The day, month, and year can be changed using the buttons on the calculator. To advance press one of the [ &gt;&gt; ] keys; to backup, press one of the [ &lt;&lt; ] keys.\n" +
               "\n" +
               "<p>Return to <a href=\"#contents\">Contents</a> \n" +
               "\n" +
               "<hr>\n" +
               "\n" +
               "<h4>2. <a name=source>Source Code</h4>\n" +
               "\n" +
               "<p>The Java Script source code for this program can be viewed by using the View|Source command of your web browser.\n" +
               "\n" +
               "<p>You may use or modify this source code in any way you find useful, provided that you agree that the author has no warranty, obligations or liability. You must determine the suitablility of this source code for your use.\n" +
               "\n" +
               "<p>Return to <a href=\"#contents\">Contents</a> \n" +
               "\n" +
               "<hr>\n" +
               "\n" +
               "<h4>3. <a name=discussion>Discussion</h4>\n" +
               "\n" +
               "<p>This program helps anyone who needs to know the Moon's phase (age), distance, and position along the ecliptic on any date within several thousand years in the past or future.\n" +
               "\n" +
               "<p>The ecliptic longitude is measured from the vernal equinox along the ecliptic in the direction of the sun's apparent motion through the stars. The moon's ecliptic longitude is calculated as well as the corresponding zodiac constellation. The ecliptic latitude is positive if north of the ecliptic and negative if south. The age of the moon in days as well as its visual phase are given. \n" +
               "\n" +
               "<p>Return to <a href=\"#contents\">Contents</a> \n" +
               "\n" +
               "<hr>\n" +
               "Chaance Graves\n" +
               "</body>";
        webView.loadData(myHTMLString,"text/html",null);
    }
}
