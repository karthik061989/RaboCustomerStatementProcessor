<%@page language="java" session="true" 
contentType="text/html;charset=ISO-8859-1" %> 

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 
ransitional//EN" "http://www.w3.org/
  TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<font color="blue">RaboBank Customer Statement Processor</font><br><br>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>RaboBank Customer Statement Processor</title>
<link rel="stylesheet" href="../css/style.css" type="text/css"></link>
<script language="JavaScript" type="text/JavaScript" 
src="../script/validate.js"></script>
</head>



		<form enctype="multipart/form-data" method="post"
			action="/RaboCustomerStatementProcessor/rabo/statement/report">

			<table border="0">
				<tr align="left" valign="top">
					<td>Please upload the file :</td>
					<td><input type="file" name="file" id="fileToUpload" class="inputbox"/></td>
				</tr>
				<tr align="left" valign="top">
					<td></td>
					<td><input type="submit" name="submit" value="Upload" class="submitButton"/></td>
				</tr>
			</table>            
		</form>


</html>