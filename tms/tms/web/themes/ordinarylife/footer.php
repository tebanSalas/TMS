<?php
#Application name: PhpCollab
#Status page: 0
echo "<p id=\"footer\">PhpCollab v$version";
if ($notLogged != "true" && $blank != "true") {
echo " - Connected users: $connectedUsers";
}

if ($footerDev == "true") {
	$parse_end = getmicrotime();
	$parse = $parse_end - $parse_start;
	$parse = round($parse,3);
	echo " - $parse secondes - databaseType $databaseType - select requests $comptRequest";
}

echo "</p>\n\n";

echo "</body>
</html>";
?>