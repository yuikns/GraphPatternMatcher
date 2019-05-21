<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="shortcut icon" href="favicon.png">
	<title>Graph Pattern Matcher</title>
	<link rel="stylesheet" href="css/bootstrap.min.css">
	<link rel="stylesheet" href="css/style.css">
	<script src="js/jquery.js"></script>
    <script src="js/jquery.form.js"></script>
    <script src="js/bootstrap.min.js"></script>
	<script type="text/javascript">
	     $(document).ready(function() {
	     	var options = {
	     		beforeSend : function() {
					$("#progress").show();
					//clear everything
					$("#bar").width('0%');
					$("#message").html("");
					$("#percent").html("0%");
				},
				uploadProgress : function(event, position,
						total, percentComplete) {
					$("#bar").width(percentComplete + '%');
					$("#percent").html(percentComplete + '%');

				},
				success : function() {
					$("#bar").width('100%');
					$("#percent").html('100%');

				},
				complete : function(response) { // response.responseText
					console.log(response.responseText);
					var jsonData = JSON.parse(response.responseText);
					var result = jsonData.result;
					var msg_head = "time cost :<e>" + jsonData.status.cost_time + "</e>sec. find image :<e>" + jsonData.status.all_result + "</e>";
					console.log(msg_head);
					var msg = "<div>";
					for(var i in result) {
						msg += "<img class=\"thumb\" src=\""+result[i].image+"\" /><br/>";
					}
					msg += "</div>";
					console.log(msg);
					$('#message').html(msg);
					$('#resultMessage').html(msg_head);
				},
				error : function() {
					$("#message")
							.html(
									"<font color='red'> ERROR: unable to upload files</font>");

				}

			};

			$("#myForm").ajaxForm(options);
			$("#myForm").change(function (){
				$("#myForm").submit();
			});

		});
		
		
	    </script>
	    
</head>
<body>

	<!-- Wrap all page content here -->
	<div id="wrap">
		
		<div class="navbar navbar-default navbar-fixed-top" >
			<div class="container">
				<div class="navbar-header">
					<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
						<span class="sr-only">Toggle navigation</span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
					</button>
					<a class="navbar-brand" href="#">Graph Pattern Matcher</a>
				</div>
				<div class="collapse navbar-collapse">
					<ul class="nav navbar-nav">
						<li><a href="#"></a></li>
					</ul>
				</div><!--/.nav-collapse -->
			</div>
		</div>
	
		<!-- Begin page content -->
		<div class="container">
			<div class="page-header">
				<form id="myForm" action="upload_Search_searchImageUpload.action"
					method="post" enctype="multipart/form-data">
					<input type="file"  id="imgtosrch" name="imgtosrch" />
				</form>
				<div id="progress">
					<div id="bar"></div>
					<div id="percent">0%</div>
				</div>
				<h4><span id="resultMessage"></span></h4>
			</div>
			<span id="lisg"></span>
			<div id="message"></div>
		</div>
	</div>

	<div id="footer">
		<div class="container">
			<p class="text-muted credit">Graph Pattern Matcher Powered by <a href="http://argcv.com">argcv</a></p>
		</div>
	</div>

</body>

</html>


