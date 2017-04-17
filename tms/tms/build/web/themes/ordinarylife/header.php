<?php
#Application name: PhpCollab
#Status page: 0
echo "$setDoctype
$setCopyright
<html>
<head>
<meta http-equiv=\"Content-Type\" content=\"text/html; charset=$setCharset\">
<title>$setTitle</title>
<meta name=\"robots\" content=\"none\">
<meta name=\"description\" content=\"$setDescription\">
<meta name=\"keywords\" content=\"$setKeywords\">\n";

echo "<script type=\"text/javascript\" src=\"../javascript/general.js\"></script>\n";
echo "<script type=\"text/JavaScript\" src=\"../javascript/overlib_mini.js\"></script>
<script type=\"text/Javascript\">
<!--
var gBrowserOK = true;
var gOSOK = true;
var gCookiesOK = true;
var gFlashOK = true;
// -->
</script>\n";

echo "<link rel=\"stylesheet\" href=\"../themes/".THEME."/stylesheet.css\" type=\"text/css\">\n";

echo "$headBonus
</head>
<body $bodyCommand>\n";
echo "<div id=\"overDiv\" style=\"position:absolute; visibility:hidden; z-index:1000;\"></div>\n\n";

if ($blank != "true" && $version >= "2.0") {
$tmpquery = "WHERE org.id = '1'";
$clientHeader = new request();
$clientHeader->openOrganizations($tmpquery);
}
if (file_exists("../logos_clients/1.".$clientHeader->org_extension_logo[0]) && $blank != "true" && $version >= "2.0") {
echo "<p id=\"header\"><img src=\"../logos_clients/1.".$clientHeader->org_extension_logo[0]."\" border=\"0\" alt=\"".$clientHeader->org_name[0]."\"></p>\n\n";
} else {
echo "<p id=\"header\">".$setTitle."</p>\n\n";
}

if ($blank == "true") {
echo "<p id=\"account\">&nbsp;</p>\n\n";
} else if ($notLogged == "true") {
echo "<p id=\"account\">&nbsp;</p>\n\n";
} else {
echo "<p id=\"account\">".$strings["user"].": $nameSession <a href=\"../general/login.php?$transmitSid&logout=true\">".$strings["logout"]."</a> <a href=\"../preferences/updateuser.php?$transmitSid\">".$strings["preferences"]."</a> <a href=\"../projects_site/home.php?$transmitSid&changeProject=true\" target=\"_blank\">".$strings["go_projects_site"]."</a></p>\n\n";
}

if ($blank == "true") {
echo "<p id=\"navigation\">&nbsp;</p>\n\n";
} else if ($notLogged == "true") {
echo "<p id=\"navigation\"><a href=\"../general/login.php?$transmitSid\">".$strings["login"]."</a>&nbsp;&nbsp;<a href=\"../general/systemrequirements.php?$transmitSid\">".$strings["requirements"]."</a>&nbsp;&nbsp;<a href=\"../general/license.php?$transmitSid\">".$strings["license"]."</a></p>\n\n";
} else {
echo "<p id=\"navigation\"><a href=\"../general/home.php?$transmitSid\">".$strings["home"]."</a>&nbsp;&nbsp;<a href=\"../projects/listprojects.php?$transmitSid\">".$strings["projects"]."</a>&nbsp;&nbsp;<a href=\"../clients/listclients.php?$transmitSid\">".$strings["clients"]."</a>&nbsp;&nbsp;<a href=\"../reports/createreport.php?$transmitSid\">".$strings["reports"]."</a>&nbsp;&nbsp;<a href=\"../search/createsearch.php?$transmitSid\">".$strings["search"]."</a>&nbsp;&nbsp;<a href=\"../calendar/viewcalendar.php?$transmitSid\">".$strings["calendar"]."</a>&nbsp;&nbsp;<a href=\"../bookmarks/listbookmarks.php?$transmitSid&view=all\">".$strings["bookmarks"]."</a>&nbsp;&nbsp;<a href=\"../administration/admin.php?$transmitSid\">".$strings["admin"]."</a></p>\n\n";
}
?>