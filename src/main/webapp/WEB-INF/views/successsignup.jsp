<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SignUp Success Page</title>

<link rel="shortcut icon" href="<c:url value='/resources/img/titleLogo.png'/>" />
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
<script src="https://code.jquery.com/jquery-3.3.1.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>


<link href="<c:url value="/resources/css/main/main.css" />" rel="stylesheet" />
<link href="<c:url value="/resources/css/style2.css" />" rel="stylesheet" />
<script type="text/javascript" src="<c:url value="/resources/js/modernizr.custom.86080.js"/>"></script>
<script>
   $(document).ready(function() {
	   
	  
	   $("#loginPage").click(function(){
			
		   
	   })
	   
      $("#homeButton").click(function(){
         $(location).attr("href","homeMain.do")
      })
   })
</script>
<style>
   @font-face {
        font-family: font;
      src: url('<c:url value='/resources/fonts/BMJUA.ttf'/>');  
   }
   #loginPage{
   		
   		width:25%;
   		padding-top:1%;
   		padding-bottom:1%;
   		margin-right:5%;
   		background-color:#c9211c;
   
   }
   #welcome h3{
   		margin-right:10%;
   		margin-bottom:5%;
   }
   
</style>
</head>
<body>
	<script>
	this.resizeTo(screen.availWidth, screen.availHeight);
	</script>
	<%@ include file="../resource/include/header.jsp"%>
	<ul class="cb-slideshow1">
      <li><span>Image 01</span></li>
      <li><span>Image 02</span></li>
      <li><span>Image 03</span></li>
      <li><span>Image 04</span></li>
      <li><span>Image 05</span></li>
      <li><span>Image 06</span></li>
   </ul>
	
	<div class="jumbotron">
      <div id="welcome">
         <p><font>회원가입에 성공하셨습니다.</font></p>
         <h3><strong><font>로그인을 통해 더 많은 빌림의 서비스를 이용하세요</font></strong></h3>
         <button class="btn btn-danger" id="loginPage"><strong><h4>로그인</h4></strong></button>
      </div>
    </div>

<%@ include file="../resource/include/footer.jsp"%> 
</body>
</html>