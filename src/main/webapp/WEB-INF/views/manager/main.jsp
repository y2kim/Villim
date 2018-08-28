<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Admin Page</title>

<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<!-- 부가적인 테마 -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
<link rel="stylesheet"
	href="https://use.fontawesome.com/releases/v5.2.0/css/all.css">	

<!-- 합쳐지고 최소화된 최신 자바스크립트 -->
<script src="https://code.jquery.com/jquery-3.3.1.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>

<style>
	#bg {
	position: fixed;
	top: -50%;
	left: -50%;
	width: 200%;
	height: 200%;



}

#bg img {
	position: absolute;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	margin: auto;
	min-width: 50%;
	min-height: 50%;
}

	#wrapper{
		width:100%;
		position: absolute;
		z-index: 10;
		height:100vh;
	}
	#collapseExample{
	
		
		
	}
	div{
		border:1px solid white;
		color:blue;
	}
	#navi{
		display:inline-block;
		
		
	}
	#content{
	display:inline-block;
	position:absolute;
	width:100%;
	height:100%;
	
	}
	#btn{
		width:100px;
	}
	#title{
		color:white;
	}
	
	#memberChkModal{
		width:100%;
		
	}

</style>	
<script>
/* $("#slide").animate({width:'toggle'},350); */
$(document).ready(function(){
	var j = 0;
	var currentarray = [];
	
	 /* $("#collapseExample").animate({width:'toggle'},350); */
	$("#btn").click(function(){
		 $("#collapseExample").animate({width:'toggle'},350);
		  $("#btn").animate({width:'100px'},350); 
	})
	$("#memberCheck").click(function(){
		var	html = "";
		$("#memberChkModal").modal('show');
		$.ajax({
			url:"mainMemberInfoLoad.admin",
			type : "get",
			
			success : function(response) {
				html += "<tr>"
				html += "<td>회원번호";
				html += "<td>이메일";
				html += "<td>이름";
				html += "<td>생년월일";
				html += "<td>프로필";
				html += "<td>차단여부";
				html += "<td>가입 날짜";
				html += "<td>주소";
				html += "</tr>"
				for(var i=0; i<response.length; i++){					
				/* 	$("#table").after("<tr>"); */
/* 					$("#table1").html("<td>" + response[i].member_location);
					$("#table1").html("<td>" + response[i].member_date);
					if(response[i].member_block == 'N'){
					$("#table1").html("<td><select id="+response[i].member_seq+"><option value='N' selected='selected'>N</option><option value='Y'>Y</option></select>");
					
					}else{
					$("#table1").html("<td><select id="+response[i].member_seq+"><option value='N'>N</option><option value='Y' selected='selected'>Y</option></select>");	
					
					}
					$("#table1").html("<td>" + response[i].member_picture);
					$("#table1").html("<td>" + response[i].member_birth);
					$("#table1").html("<td>" + response[i].member_name);
					$("#table1").html("<td>" + response[i].member_email);
					$("#table1").html("<td>"+ response[i].member_seq); */
				/* 	$("#table").after("/<tr>"); */
					
						  html += "<tr>";
						  html += "<td>"+ response[i].member_seq;
						  html += "<td>" + response[i].member_email;
						  html += "<td>" + response[i].member_name;
						  html += "<td>" + response[i].member_birth;
						  html += "<td>" + response[i].member_picture;
						  if(response[i].member_block == 'N'){
							    html += "<td><select id="+response[i].member_seq+"><option value='N' selected='selected'>N</option><option value='Y'>Y</option></select>";
						  }else{
							    html +=	"<td><select id="+response[i].member_seq+"><option value='N'>N</option><option value='Y' selected='selected'>Y</option></select>";
						  }
						  html += "<td>" + response[i].member_date;
					      html += "<td>" + response[i].member_location;

					   	  html += "</tr>";
					    
					    $("table").html(html);
	
				}
				
				$("select").change(function(){
					/*  alert($("#selectbox"+i+"" "option:selected").val()); */
					/* var currentid = []; */
					var currentid = $(this).attr('id');
					var currentval = $("#"+currentid+" option:selected").val();
					var current = currentid + ":" + currentval;
					
					currentarray.push({
						seq : currentid,
						val : currentval
					})
			
					alert("#"+currentid);
					alert(current);
					alert($(this).val());
					alert(currentarray);				
				
				
				})
			}

		});
	})			
	
	$("#save").click(function(){
		alert(currentarray);
		var jsonData = JSON.stringify(currentarray);
		jQuery.ajaxSettings.traditional = true;
		$.ajax({
			url:"mainMemberBlock.admin",
			dataType: 'json',
			type : "post",
			data : {						
				
				currentarray : jsonData
				
			}, 
			success : function(response) {
				
				alert(response);
				currentarray.splice(0, currentarray.length);
			}
				
	})
	})
	
	(function poll() {
	    $.ajax({
	        url: 'mainTest.admin',
	        type: 'GET',
	        dataType: 'json',
	        success: function(response) {
	           
	        },
	        timeout: 3000,
	        complete: setTimeout(function() { poll(); }, 6000)
	    })
	})();
				

	 
})

</script>


</head>
<body>
<script>
	$.ajax({
		url:"mainMemberCountLoad.admin",
		type : "get",
		success : function(response) {
			
			$("#memberCount").html(response);
		}
			
	})
</script>	

<div id="bg">
		<img src="./resources/images/17.jpg" alt="">
</div>
<div id="wrapper">


<div id="navi">
<ul class="nav nav-pills nav-stacked" id="collapseExample">
<li role="presentation"><img src="../resources/img/logo2.png"></li>
<li role="presentation"><a href="#">admin 계정 정보</a></li>
 <li role="presentation"><a href="#">Home</a></li>
  <li role="presentation"><a href="#">Profile</a></li>
  <li role="presentation"><a href="#">Messages</a></li>
</ul>
</div>

<div id="content">
	<div id="contentHeader">
		<button id="btn">버튼</button>
		<a href="#">
 		 <i class="fas fa-envelope fa-2x"></i> <span class="badge" style="wi">4</span>
		</a>		
	</div>
	<div><h2 id="title">Dashboard</h2>
	
	<div style="background-color:purple; width:15%;">
		<h3 id="memberCount"></h3>
		<p style="display:inline">전체 회원수</p>
		<i class="fas fa-users fa-2x"></i>
		<a href="#" style="display:block" id="memberCheck">조회</a>
	</div>
</div>
</div>

</div>

<div class="modal fade" id="memberChkModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
      </div>
      <div class="modal-body">
 		<table class="table table-striped">
		</table>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary" id="save">Save changes</button>
      </div>
    </div>
  </div>
</div>
</body>
</html>