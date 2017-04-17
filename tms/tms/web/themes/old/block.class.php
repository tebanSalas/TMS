<?php
#Application name: PhpCollab
#Status page: 0

class block {

function block() {
	$this->theme = THEME;
	$this->oddColor = "#F5F5F5";
	$this->evenColor = "#EFEFEF";
	$this->highlightOn = "#DEE7EB";
	$this->class = "odd";
	$this->highlightOff = $this->oddColor;
}

function note($content) {
	echo "<p class=\"note\">".$content."</p>\n\n";
}

function heading($title) {
	echo "<h1 class=\"heading\">".$title."</h1>\n\n";
}

function headingToggle($title) {
if ($_COOKIE[$this->form] == "c" || $HTTP_COOKIE_VARS[$this->form] == "c") {
$style = "none";
$arrow = "closed";
} else {
$style = "block";
$arrow = "open";
}
	echo "<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tr><td><a href=\"javascript:showHideModule('".$this->form."','$this->theme')\" onMouseOver=\"javascript:showHideModuleMouseOver('".$this->form."'); return true; \" onMouseOut=\"javascript:window.status=''; return true;\"><img name=\"".$this->form."Toggle\" border=\"0\" src=\"../themes/$this->theme/module_toggle_".$arrow.".gif\"></a></td><td><img width=\"10\" height=\"10\" name=\"".$this->form."tl\" src=\"../themes/$this->theme/spacer.gif\"></td><td width=\"100%\"><h1 class=\"heading\">".$title."</h1></td></tr></table>

<div id=\"".$this->form."\" style=\"display: $style;\">\n\n";
}

function closeToggle() {
	echo "</div>\n\n";
}

function headingError($title) {
	echo "<h1 class=\"headingError\">".$title."</h1>\n";
}

function contentError($content) {
	echo "<table class=\"error\"><tr><td>".$content."</td></tr></table>\n";
}

function returnBorne($current) {
global ${'borne'.$current};
if (${'borne'.$current} == "") {
	$borneValue = "0";
} else {
	$borneValue = ${'borne'.$current};
}
return $borneValue;
}

function bornesFooter($current,$total,$showall,$parameters) {
global $strings;
if ($this->rowsLimit < $this->recordsTotal) {
echo "<table cellspacing=\"0\" width=\"100%\" border=\"0\" cellpadding=\"0\"><tr><td nowrap class=\"footerCell\">&#160;&#160;&#160;&#160;";
$nbpages = ceil($this->recordsTotal/ $this->rowsLimit);
$j = "0";
for($i = 1;$i <= $nbpages;$i ++){
if ($this->borne == $j) {
	echo "<b>$i</b>&#160;";
} else {
	echo "<a href=\"$PHP_SELF?$transmitSid";
	for ($k=1;$k<=$total;$k++) {
		global ${'borne'.$k};
		if ($k != $current) {
			echo "&borne$k=".${'borne'.$k};
		} else if ($k == $current) {
			echo "&borne$k=$j";
		}
	}
	echo "&$parameters#".$this->form."Anchor\">$i</a>&#160;";
}
$j = $j + $this->rowsLimit;
}
echo "</td><td nowrap align=\"right\" class=\"footerCell\">";
if ($showall != "") {
echo "<a href=\"$showall\">".$strings["show_all"]."</a>";
}
echo "&#160;&#160;&#160;&#160;&#160;</td></tr><tr><td height=\"5\" colspan=\"2\"><img width=\"1\" height=\"5\" border=\"0\" src=\"../themes/".THEME."/spacer.gif\" alt=\"\"></td></tr></table>";
}

}

function messageBox($msgLabel) {
echo "<br><table class=\"message\">
<tr><td>$msgLabel</td></tr>
</table>";
}

function openPaletteIcon() {
echo "<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"icons\"><tr>\n";
}

function closePaletteIcon() {
echo "<td align=left><img height=\"26\" width=\"5\" src=\"../themes/$this->theme/spacer.gif\" alt=\"\"></td><td class=\"commandDesc\"><div id=\"".$this->form."tt\" class=\"rel\"><div id=\"".$this->form."tti\" class=\"abs\"><img height=\"1\" width=\"350\" src=\"../themes/$this->theme/spacer.gif\" alt=\"\"></div></div></td></tr></table>\n";
}

function openPaletteScript() {
echo "<script type=\"text/JavaScript\">
<!--
document.".$this->form."Form.buttons = new Array();\n";
}

function closePaletteScript($compt,$values) {
echo "MM_updateButtons(document.".$this->form."Form, 0);document.".$this->form."Form.checkboxes = new Array();";
for ($i=0;$i<$compt;$i++) {
	echo "document.".$this->form."Form.checkboxes[document.".$this->form."Form.checkboxes.length] = new MMCheckbox('$values[$i]',document.".$this->form."Form,'".$this->form."cb$values[$i]');";
}
echo "document.".$this->form."Form.tt = '".$this->form."tt';
// -->
</script>\n\n";
}

function sorting($sortingRef,$sortingValue,$sortingDefault,$sortingFields) {
if ($sortingRef != "") {
	$this->sortingRef = $sortingRef;
}
if ($sortingValue != "") {

	$this->sortingValue = $sortingValue;
}
if ($sortingDefault != "") {
	$this->sortingDefault = $sortingDefault;
}
if ($sortingFields != "") {
	$this->sortingFields = $sortingFields;
}
global $sortingOrders,$sortingFields,$sortingArrows,$sortingStyles,$explode;
if (isset($this->sortingValue) != "") {
$explode = explode(" ",$this->sortingValue);
} else {
$this->sortingValue = $this->sortingDefault;
$explode = explode(" ",$this->sortingValue);
}

for ($i=0;$i<count($sortingFields);$i++) {
if ($sortingFields[$i] == $explode[0] && $explode[1] == "DESC") {
	$sortingOrders[$i] = "ASC";
	$sortingArrows[$i] = "&#160;<img border=\"0\" src=\"../themes/$this->theme/icon_sort_za.gif\" alt=\"\" width=\"11\" height=\"11\">";
	$sortingStyles[$i] = "active";
} else if ($sortingFields[$i] == $explode[0] && $explode[1] == "ASC") {
	$sortingOrders[$i] = "DESC";
	$sortingArrows[$i] = "&#160;<img border=\"0\" src=\"../themes/$this->theme/icon_sort_az.gif\" alt=\"\" width=\"11\" height=\"11\">";
	$sortingStyles[$i] = "active";
} else {
	$sortingOrders[$i] = "ASC";
	$sortingArrows[$i] = "";
	$sortingStyles[$i] = "";
}
}
if ($sortingOrders != "") {
	$this->sortingOrders = $sortingOrders;
}
if ($sortingArrows != "") {
	$this->sortingArrows = $sortingArrows;
}
if ($sortingStyles != "") {
	$this->sortingStyles = $sortingStyles;
}
}

function openForm($address) {
echo "<a name=\"".$this->form."Anchor\"></a>\n
<form accept-charset=\"UNKNOWN\" method=\"POST\" action=\"$address\" name=\"".$this->form."Form\" enctype=\"application/x-www-form-urlencoded\">\n\n";
}

function closeFormResults() {
echo "<input name=\"sor_cible\" type=\"HIDDEN\" value=\"$this->sortingRef\"><input name=\"sor_champs\" type=\"HIDDEN\" value=\"\"><input name=\"sor_ordre\" type=\"HIDDEN\" value=\"\">
</form>\n\n";
}

function labels($labels,$published,$sorting="true",$sortingOff="") {

global $labels,$sortingOrders,$sortingFields,$sortingArrows,$sortingStyles,$strings,$sitePublish;
$sortingFields = $this->sortingFields;
$sortingOrders = $this->sortingOrders;
$sortingArrows = $this->sortingArrows;
$sortingStyles = $this->sortingStyles;

if ($sitePublish == "false" && $published == "true") {
	$comptLabels = count($labels) - 1;
} else {
	$comptLabels = count($labels);
}
for ($i=0;$i<$comptLabels;$i++) {
if ($sorting == "true") {
	echo "<th nowrap class=\"$sortingStyles[$i]\"><a href=\"javascript:document.".$this->form."Form.sor_cible.value='$this->sortingRef';document.".$this->form."Form.sor_champs.value='$sortingFields[$i]';document.".$this->form."Form.sor_ordre.value='$sortingOrders[$i]';document.".$this->form."Form.submit();\" onMouseOver=\"javascript:window.status='".$strings["sort_by"]." ".addslashes($labels[$i])."'; return true;\" onMouseOut=\"javascript:window.status=''; return true\">".$labels[$i]."</a>$sortingArrows[$i]</th>\n";
} else {
if ($sortingOff[1] == "ASC") {
	$sortingArrow = "&#160;<img border=\"0\" src=\"../themes/$this->theme/icon_sort_az.gif\" alt=\"\" width=\"11\" height=\"11\">";

} else if ($sortingOff[1] == "DESC") {
	$sortingArrow = "&#160;<img border=\"0\" src=\"../themes/$this->theme/icon_sort_za.gif\" alt=\"\" width=\"11\" height=\"11\">";
}
if ($i == $sortingOff[0]) {
	echo "<th nowrap class=\"active\">".$labels[$i]."$sortingArrow</a>";
} else {
	echo "<th nowrap>".$labels[$i]."</a>";
}
}
}

echo "</tr>\n";
}

function openResults($checkbox="true") {
echo "<table class=\"listing\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">
<tr>\n";

if ($checkbox == "true") {
echo "<th width=\"1%\" align=\"center\"><a href=\"javascript:MM_toggleSelectedItems(document.".$this->form."Form,'$this->theme')\"><img height=\"13\" width=\"13\" border=\"0\" src=\"../themes/$this->theme/checkbox_off_16.gif\" alt=\"\" vspace=\"3\" hspace=\"3\"></a></th>\n";
} else {
echo "<th width=\"1%\" align=\"center\"><img height=\"13\" width=\"13\" border=\"0\" src=\"../themes/$this->theme/spacer.gif\" alt=\"\" vspace=\"3\"></th>\n";
}
}

function closeResults() {
echo "</table>
<hr />\n";
}

function noresults() {
global $strings;
echo "<table cellspacing=\"0\" border=\"0\" cellpadding=\"2\"><tr><td colspan=\"4\">".$strings["no_items"]."</td></tr></table><hr />";
}

function paletteIcon($num,$type,$text) {
global $strings;
echo "<td width=\"30\" class=\"commandBtn\"><a href=\"javascript:var b = MM_getButtonWithName(document.".$this->form."Form, '".$this->form."$num'); if (b) b.click();\" onMouseOver=\"var over = MM_getButtonWithName(document.".$this->form."Form, '".$this->form."$num'); if (over) over.over(); return true; \" onMouseOut=\"var out = MM_getButtonWithName(document.".$this->form."Form, '".$this->form."$num'); if (out) out.out(); return true; \"><img width=\"23\" height=\"23\" border=\"0\" name=\"".$this->form."$num\" src=\"../themes/$this->theme/btn_".$type."_norm.gif\" alt=\"$text\"></a></td>\n";
}

function paletteScript($num,$type,$link,$options,$text) {

global $strings;
echo "document.".$this->form."Form.buttons[document.".$this->form."Form.buttons.length] = new MMCommandButton('".$this->form."$num',document.".$this->form."Form,'".$link."&".session_name()."=".session_id()."','../themes/$this->theme/btn_".$type."_norm.gif','../themes/$this->theme/btn_".$type."_over.gif','../themes/$this->theme/btn_".$type."_down.gif','../themes/$this->theme/btn_".$type."_dim.gif',$options,'','".$text."',false,'');\n";
}

function openContent() {
	echo "<table class=\"content\" cellspacing=\"0\" cellpadding=\"0\">";
}

function contentRow($left,$right,$altern="false") {
if ($this->class == "") {
	$this->class = "odd";
}
if ($left != "") {
	echo "<tr class=\"$this->class\"><td valign=\"top\" class=\"leftvalue\">".$left." :</td><td>".$right."&nbsp;</td></tr>";
} else {
	echo "<tr class=\"$this->class\"><td valign=\"top\" class=\"leftvalue\">&nbsp;</td><td>".$right."&nbsp;</td></tr>";
}
	if ($this->class == "odd" && $altern == "true") {
		$this->class = "even";
	} else if ($this->class == "even" && $altern == "true") {
		$this->class = "odd";
	}
}

function openRow() {
	$change = "true";
	echo "<tr class=\"$this->class\" onmouseover=\"this.style.backgroundColor='".$this->highlightOn."'\" onmouseout=\"this.style.backgroundColor='".$this->highlightOff."'\">\n";
	if ($this->class == "odd") {
		$this->class = "even";
		$this->highlightOff = $this->evenColor;
		$change = "false";
	} else if ($this->class == "even" && $change != "false") {
		$this->class = "odd";
		$this->highlightOff = $this->oddColor;
	}
}

function checkboxRow($ref,$checkbox="true") {
if ($checkbox == "true") {
	echo "<td align=\"center\"><a href=\"javascript:MM_toggleItem(document.".$this->form."Form, '".$ref."', '".$this->form."cb".$ref."','".THEME."')\"><img name=\"".$this->form."cb".$ref."\" border=\"0\" src=\"../themes/".THEME."/checkbox_off_16.gif\" alt=\"\" vspace=\"3\"></a></td>";
} else {
	echo "<td><img height=\"13\" width=\"13\" border=\"0\" src=\"../themes/".THEME."/spacer.gif\" alt=\"\" vspace=\"3\"></td>";
}
}

function cellRow($content) {
	echo "<td>$content</td>";
}

function closeRow() {
	echo "\n</tr>\n";
}

function contentTitle($title) {
	echo "<tr><th colspan=\"2\">".$title."</th></tr>";
}

function closeContent() {
	echo "</table>\n<hr />\n";
}

function closeForm() {
	echo "</form>\n";
}

function openBreadcrumbs() {
	echo "<p class=\"breadcrumbs\">";
}

function itemBreadcrumbs($content) {
if ($this->breadcrumbsTotal == "") {
	$this->breadcrumbsTotal = "0";
}
	$this->breadcrumbs[$this->breadcrumbsTotal] = $content;
	$this->breadcrumbsTotal = $this->breadcrumbsTotal + 1;
}

function closeBreadcrumbs() {
	$items = $this->breadcrumbsTotal;
	for ($i=0;$i<$items;$i++) {
		echo $this->breadcrumbs[$i];
		if ($items-1 != $i) {
			echo " / ";
		}
	}
	echo "</p>\n\n";
}

}
?>