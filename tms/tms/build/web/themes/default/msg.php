<?php
#Application name: PhpCollab
#Status page: 0
echo "<br><table class=\"message\">";

if ($msg == "demo") {
echo "<tr><td><b>".$strings["attention"]."</b> : ".$strings["demo_mode"]." <a href=\"$urlContact\" target=\"_blank\">".$strings["sourceforge_link"]."</a></td></tr>";
}

if ($msg == "permissiondenied") {
echo "<tr><td>".$strings["no_permissions"]."</td></tr>";
}

if ($msg == "logout") {
echo "<tr><td>".$strings["success_logout"]."</td></tr>";
}

if ($msg == "noteOwner") {
echo "<tr><td><b>".$strings["attention"]."</b> : ".$strings["note_owner"]."</td></tr>";
}

if ($msg == "taskOwner") {
echo "<tr><td><b>".$strings["attention"]."</b> : ".$strings["task_owner"]."</td></tr>";
}

if ($msg == "projectOwner") {
echo "<tr><td><b>".$strings["attention"]."</b> : ".$strings["project_owner"]."</td></tr>";
}

if ($msg == "email_pwd") {
	echo "<tr><td><b>".$strings["success"]."</b> : ".$strings["email_pwd"]."</td></tr>";
}

if ($msg == "deleteTopic") {
echo "<tr><td><b>".$strings["success"]."</b> : $num of $num discussions were deleted.</td></tr>";
}

if ($msg == "closeTopic") {
echo "<tr><td><b>".$strings["success"]."</b> : $num of $num discussions were closed.</td></tr>";
}

if ($msg == "createProjectSite") {
echo "<tr><td><b>".$strings["success"]."</b> : The project site \"".$projectDetail->pro_name[0]."\" was successfully created.</td></tr>";
}

if ($msg == "removeProjectSite") {
echo "<tr><td><b>".$strings["success"]."</b> : ".$strings["project_site_deleted"]."</td></tr>";
}

if ($msg == "addClientToSite") {
echo "<tr><td><b>".$strings["success"]."</b> : ".$strings["add_user_project_site"]."</td></tr>";
}

if ($msg == "removeClientToSite") {
echo "<tr><td><b>".$strings["success"]."</b> : ".$strings["remove_user_project_site"]."</td></tr>";
}

if ($msg == "deleteTeamOwnerMix") {
echo "<tr><td><b>".$strings["attention"]."</b> : ".$strings["delete_teamownermix"]."</td></tr>";
}

if ($msg == "deleteTeamOwner") {
echo "<tr><td><b>".$strings["attention"]."</b> : ".$strings["delete_teamowner"]."</td></tr>";
}

if ($msg == "addToSite") {
echo "<tr><td><b>".$strings["success"]."</b> : ".$strings["add_project_site_success"]."</td></tr>";
}

if ($msg == "removeToSite") {
echo "<tr><td><b>".$strings["success"]."</b> : ".$strings["remove_project_site_success"]."</td></tr>";
}

if ($msg == "updateFile") {
echo "<tr><td><b>".$strings["success"]."</b> : ".$strings["update_comment_file"]."</td></tr>";
}

if ($msg == "addFile") {
echo "<tr><td><b>".$strings["success"]."</b> : ".$strings["add_file_success"]."</td></tr>";
}

if ($msg == "deleteFile") {
echo "<tr><td><b>".$strings["success"]."</b> : ".$strings["delete_file_success"]."</td></tr>";
}

if ($msg == "add") {
echo "<tr><td><b>".$strings["success"]."</b> : ".$strings["addition_succeeded"]."</td></tr>";
}

if ($msg == "delete") {
echo "<tr><td><b>".$strings["success"]."</b> : ".$strings["deletion_succeeded"]."</td></tr>";
}

if ($msg == "addReport") {
echo "<tr><td><b>".$strings["success"]."</b> : ".$strings["report_created"]."</td></tr>";
}

if ($msg == "deleteReport") {
echo "<tr><td><b>".$strings["success"]."</b> : ".$strings["deleted_reports"]."</td></tr></table></td></tr>";
}

if ($msg == "addAssignment") {
$tmpquery = $tableCollab["assignments"];
last_id($tmpquery);
$num = $lastId[0];
unset($lastId);
echo "<tr><td><b>".$strings["success"]."</b> : ".$strings["addition_succeeded"]." ".$strings["add_optional"]." <a href=\"assignmentcomment.php?$transmitSid&task=".$taskDetail->tas_id[0]."&id=$num\"><b>".$strings["assignment_comment"]."</b></a>.</td></tr>";
}

if ($msg == "updateAssignment") {
$tmpquery = $tableCollab["assignments"];
last_id($tmpquery);
$num = $lastId[0];
unset($lastId);
echo "<tr><td><b>".$strings["success"]."</b> : ".$strings["modification_succeeded"]." ".$strings["add_optional"]." <a href=\"../tasks/assignmentcomment.php?$transmitSid&task=".$taskDetail->tas_id[0]."&id=$num\"><b>".$strings["assignment_comment"]."</b></a>.</td></tr>";
}

if ($msg == "update") {
echo "<tr><td><b>".$strings["success"]."</b> : ".$strings["modification_succeeded"]."</td></tr>";
}

if ($msg == "blankUser") {
echo "<tr><td><b>".$strings["attention"]."</b> : ".$strings["blank_user"]."</td></tr>";
}

if ($msg == "blankClient") {
echo "<tr><td><b>".$strings["attention"]."</b> : ".$strings["blank_organization"]."</td></tr>";
}

if ($msg == "blankProject") {
echo "<tr><td><b>".$strings["attention"]."</b> : ".$strings["blank_project"]."</td></tr>";
}

echo "</table>";
?>