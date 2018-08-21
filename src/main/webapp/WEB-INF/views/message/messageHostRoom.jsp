<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>   
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://fonts.googleapis.com/css?family=Droid+Sans:400,700">

<!-- jQuery library -->
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>

<!-- Latest compiled JavaScript -->
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<!-- animated -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/3.5.2/animate.min.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<title>${guest_name}님과의 대화</title>
<style>
.card {
	transition: 0.3s;
	width: 18vw;
	height: 170vh;
	border: 1px solid #c9cacc;
	position: relative;
	left:22.5vw;
	margin-top:1vh;
	margin-bottom:10vh;
}

 
/* * {
  box-sizing: border-box;
  margin: 0;
  padding: 0;

  -webkit-box-sizing: border-box;
 	-moz-box-sizing: border-box;
}

body{

  font-family: "Roboto", "Tahoma", "Arial", sans-serif;,
} */

.text-right{ text-align: right; }

.comments-app{
  margin: 50px auto;
  max-width: 680px;
  padding: 0 50px;
  width: 100%;
}

.comments-app h1{
  color: #191919;
  margin-bottom: 1.5em;
  text-align: center;
  text-shadow: 0 0 2px rgba(152, 152, 152, 1);
}

.comment-form{  }
.comment-form .comment-avatar{  }

.comment-form .form{ margin-left: 100px; }

.comment-form .form .form-row{ /* margin-bottom: 10px; */ }
 .comment-form .form .form-row:last-child{ margin-bottom: 0; }

.comment-form .form .input{
  background-color: #fcfcfc;
  border: none;
  border-radius: 4px;
  box-shadow: 0 1px 1px rgba(0, 0, 0, .15);
  color: #555f77;
  font-family: inherit;
  font-size: 14px;
  padding: 5px 10px;
  outline: none;
  width: 100%;

  -webkit-transition: 350ms box-shadow;
  -moz-transition: 350ms box-shadow;
  -ms-transition: 350ms box-shadow;
  -o-transition: 350ms box-shadow;
  transition: 350ms box-shadow;
}

.comment-form .form textarea.input{
  height:14vh;
  padding: 0px;
  position:relative;
  left:-25vw;
  top:-2.5vh;
}

.comment-form .form label{
  color: #555f77;
  font-family: inherit;
  font-size: 14px;
}

.comment-form .form input[type=submit]{
  background-color: #555f77;
 border: 4px solid #969696;
  border-radius: 4px;
  box-shadow: 0 1px 1px rgba(0, 0, 0, .15);
  color: #fff;
  cursor: pointer;
  display: block;
  margin-left: auto;
  outline: none;
  padding: 6px 15px;

  -webkit-transition: 350ms box-shadow;
  -moz-transition: 350ms box-shadow;
  -ms-transition: 350ms box-shadow;
  -o-transition: 350ms box-shadow;
  transition: 350ms box-shadow;
}

.comment-form .form .input:focus,
.comment-form .form input[type=submit]:focus,
.comment-form .form input[type=submit]:hover{

}

.comment-form .form.ng-submitted .input.ng-invalid,
.comment-form .form .input.ng-dirty.ng-invalid{
  
}

.comment-form .form .input.disabled {
    background-color: #E8E8E8;
}



.comment-form,
.comment{ 
   margin-bottom: 0vh; */
  height:auto;
  position: relative;
  z-index: 0;
} 


.comment-form .comment-avatar,
.comment .comment-avatar{
  border: 2px solid #fff;
  border-radius: 50%;
  box-shadow: 0 1px 2px rgba(0, 0, 0, .2);
  height: 80px;
  left: 16;
  overflow: hidden;
  position: absolute;
  top: 0;
  width: 80px;
}

.comment-form .comment-avatar img,
.comment .comment-avatar img{
  /* display: block; */
  height: auto;
  width: 100%;
}

.comment .comment-box{
  background-color: #fcfcfc;
  border-radius: 4px;
  box-shadow: 0 1px 1px rgba(0, 0, 0, .15);
  margin-left: 100px;
  position: relative;
  padding: 15px;
}

.comment .comment-box:before,
.comment .comment-box:after{
  border-width: 10px 10px 10px 0;
  border-style: solid;
  border-color: transparent #FCFCFC;
  content: "";
  left: -10px;
  position: absolute;
  top: 20px;
}

.comment .comment-box:before{
  border-color: transparent rgba(0, 0, 0, .05);
   top: 22px;
}

.comment .comment-text{
  color: #555f77;
  font-size: 15px;
  margin-bottom: 21px;
}

.comment .comment-footer{
  color: #acb4c2;
  font-size: 13px;
}

.comment .comment-footer:after{
  content: "";
  display: table;
  clear: both;
}

.comment .comment-footer a{
  color: #acb4c2;
  text-decoration: none;

  -webkit-transition: 350ms color;
  -moz-transition: 350ms color;
  -ms-transition: 350ms color;
  -o-transition: 350ms color;
  transition: 350ms color;
}

.comment .comment-footer a:hover{
  color: #555f77;
  text-decoration: underline;
}

.comment .comment-info{
  float: left;
  width: 85%;
}

.comment .comment-author{ }
.comment .comment-date{ }

.comment .comment-date:before{
  content: "|";
  margin: 0 10px;
}

.comment-actions{
  float: left;
  text-align: right;
  width: 15%;
}


/* * {
  margin: 0px;
  padding: 0px;
} */

.box1 {
  width: 300px;
  margin: 50px auto;
  border: 4px solid #e2e2e2;
  background:#e2e2e2;
  padding: 20px;
  text-align: center;
  font-weight: 900;
  color: #595858;
  font-family: arial;
  position: relative;
}


/* speech bubble 5 */

.sb5:before {
  content: "";
  width: 0px;
  height: 0px;
  position: absolute;
  border-left: 10px solid #e2e2e2;
  border-right: 10px solid transparent;
  border-top: 10px solid #e2e2e2;
  border-bottom: 10px solid transparent;
  right: -21px;
  top: 6px;
}

.sb5:after {
  content: "";
  width: 0px;
  height: 0px;
  position: absolute;
  border-left: 7px solid #e2e2e2;
  border-right: 7px solid transparent;
  border-top: 7px solid #e2e2e2;
  border-bottom: 7px solid transparent;
  right: -11px;
  top: 10px;
}


/* speech bubble 6 */

.sb6:before {
  content: "";
  width: 0px;
  height: 0px;
  position: absolute;
  border-left: 10px solid transparent;
  border-right: 10px solid #e2e2e2;
  border-top: 10px solid #e2e2e2;
  border-bottom: 10px solid transparent;
  left: -21px;
  top: 6px;
}

.sb6:after {
  content: "";
  width: 0px;
  height: 0px;
  position: absolute;
  border-left: 7px solid transparent;
  border-right: 7px solid #e2e2e2;
  border-top: 7px solid #e2e2e2;
  border-bottom: 7px solid transparent;
  left: -11px;
  top: 10px;
}

.img-circle {
    border-radius: 50%;
}


.card2 {
    border: 1px solid #c9cacc;
    transition: 0.3s;
    width: 30%;
    height:auto;
    margin: 0 auto;
    position:relative;
    left:10vw;
    top:-125vh;
}

.container {
    padding: 2px 16px;
}

/* Reset Select */
select {
  -webkit-appearance: none;
  -moz-appearance: none;
  -ms-appearance: none;
  appearance: none;
  outline: 0;
  box-shadow: none;
  border: 0 !important;
  background: #2c3e50;
  background-image: none;
}
/* Custom Select */
.select {
  position: relative;
  display: block;
  width: 20em;
  height: 3em;
  line-height: 3;
  background: #2c3e50;
  overflow: hidden;
  border-radius: .25em;
}
select {
  width: 100%;
  height: 100%;
  margin: 0;
  padding: 0 0 0 .5em;
  color: #fff;
  cursor: pointer;
}
select::-ms-expand {
  display: none;
}
/* Arrow */
.select::after {
  content: '\25BC';
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  padding: 0 1em;
  background: #34495e;
  pointer-events: none;
}
/* Transition */
.select:hover::after {
  color: #f39c12;
}
.select::after {
  -webkit-transition: .25s all ease;
  -o-transition: .25s all ease;
  transition: .25s all ease;
}
</style>
<script>
$(document).ready(function(){
	var week = ['일', '월', '화', '수', '목', '금', '토'];

		var dayOfCI = week[new Date($('#checkIn').val()).getDay()];
		var dayOfCO = week[new Date($('#checkOut').val()).getDay()];
		
		$('#dayCI').html(dayOfCI+"요일");
		$('#dayCO').html(dayOfCO+"요일");
		
	
	
	
})
</script>
</head>
<body>
<%@ include file="../../resource/include/header_profile.jsp" %>

<div class="card">
						<div class="row" style="font-weight:700;width:100%;height:30vh;position:relative;left:0.8vw;">
			<div style="position:relative;top:2vh;">
					<h4 style="position:relative;top:1.6vh;left:2.5vw;color:#5e5e5e;font-weight:700;">예약 세부정보</h4>
					<hr style="color:gray;width:70%;position:relative;top:2vh;">
				<div style="position:relative;  top:4vh;left:5.3vw;font-weight:700;"><h6 style="display: inline !important;position:relative;left:-1.5vw;top:-2.8vh;margin:1vw;font-weight:700;color:#9e9e9e;">체크인</h6><h6 style="display: inline !important;top:-2.8vh; margin:1vw;position:relative; left:1vw;font-weight:700;color:#9e9e9e;">체크 아웃</h6></div>
				<div style="position:relative;  top:7vh;left:5vw;font-weight:700;"><h6  style="display: inline !important;position:relative; top:-4.5vh;left:-1.4vw;margin:1vw;font-weight:700;">${msgRoom.checkIn}</h6><h6 style="display: inline !important; margin:1vw;position:relative; left:1vw;top:-4.5vh;font-weight:700;">${msgRoom.checkOut}</h6></div>
				<div style="position:relative;  top:7vh;left:5.5vw;font-weight:700;"><h6 id="dayCI" style="display: inline !important;position:relative; top:-4.5vh;left:-1.7vw;margin:1vw;font-weight:700;">$요일</h6><h6 id="dayCO" style="display: inline !important; margin:1vw;position:relative; left:1.2vw;top:-4.5vh;font-weight:700;">$요일</h6></div>
				<input id="checkIn" type="hidden" value="${splitCheckIn}">
				<input id="checkOut"  type="hidden" value="${splitCheckOut}">
				<span class="glyphicon glyphicon-triangle-right" style="position:relative;left:8.8vw;top:-1vh;"></span>
				<hr style="color:gray;width:70%;position:relative;top:1vh;">
				<div style="position:relative;  top:3vh;left:5.3vw;font-weight:700;"><h6 style="display: inline !important;position:relative;left:-1.5vw;top:-2.8vh;margin:1vw;font-weight:700;color:#9e9e9e;">인원</h6></div>
				<div style="position:relative;  top:3vh;left:5.4vw;font-weight:700;"><h6 style="display: inline !important;position:relative;left:-1.5vw;top:-2.8vh;margin:1vw;font-weight:700;">${msgRoom.totalNumber}명</h6></div>
		
			</div>
			</div>
			
			
			<div class="row" style="height:24vh;position:relative;left:0.8vw;top:0vh;width:100%;">
			
			<div style="position:relative;top:1vh;">
			<h4 style="position:relative;top:1.6vh;left:2.5vw;color:#5e5e5e;font-weight:700;">호스팅 대금</h4>
			<div style="position:relative;  color:#5e5e5e;top:3vh;left:1vw;font-weight:700;height:3vh;"><h5 style="display: inline !important;position:relative;left:2vw;top:0vh;width:15vw;line-height:4vh;">￦${home_price} x ${diffDay}박 </h5><h5 style="display: inline !important;position:relative;left:5.8vw;">￦${totalPrice}</h5></div>
            <div style="position:relative;  color:#5e5e5e;top:3vh;left:1vw;font-weight:700;height:3vh;"><h5 style="display: inline !important;position:relative;left:2vw;top:0vh;width:15vw;line-height:4vh;">서비스 수수료</h5><h5 style="display: inline !important;position:relative;left:6.8vw;">￦0</h5></div>
			<hr style="color:#666666;width:70%;position:relative;top:1.9vh;font-weight:900;height:15px;">
			<div style="position:relative;  top:-1vh;left:0vw;font-weight:700;height:3vh;"><h5 style="display: inline !important;position:relative;left:4.1vw;top:0vh;width:15vw;font-weight:700;">합계</h5><h5 style="display: inline !important;position:relative;left:10vw;font-weight:700;">￦${totalPrice}</h5></div>
			
			<hr style="color:#666666;width:70%;position:relative;top:-2vh;">
			</div>
			
			</div>
			<hr style="color:#666666;width:70%;position:relative;top:-1.5vh;font-weight:900;height:15px;">
			
			
			<div class="row" style="height:24vh;position:relative;left:0.8vw;top:-1vh;width:100%;">
			<div style="width:75%;position:relative;top:0vh;left:3vw;">
			<h4 style="position:relative;top:0vh;left:-0.8vw;color:#5e5e5e;font-weight:700;line-height:1.3;">예약 취소</h4>
			<h5 style="position:relative;top:2vh;left:-0.8vw;color:#5e5e5e;line-height:1.7;">현재 숙소에 유연 환불 정책을 적용하고 계십니다.
이 예약을 취소하면 페널티를 받게 됩니다. 페널티에는 1년간 슈퍼호스트 지위 박탈, 취소 기록을 보여주는 후기 공개, 취소 수수료가 포함되며, 취소한 예약에 해당하는 날짜가 달력에서 차단됩니다.</h5>
			</div>
			</div>
			
			<hr style="color:#666666;width:70%;position:relative;top:0.4vh;font-weight:900;height:15px;">
	
			<div class="row" style="height:22vh;position:relative;left:0.8vw;top:-3vh;width:100%;">
			<div style="width:75%;position:relative;top:1.7vh;left:3vw;">
			<h4 style="position:relative;top:1vh;left:-0.8vw;color:#5e5e5e;font-weight:700;line-height:1.3;">언제나 빌림을 통해 대화하세요</h4>
			<h5 style="position:relative;top:3vh;left:-0.8vw;color:#5e5e5e;line-height:1.7;">안전한 결제를 위해 빌림 웹사이트나 앱 외부에서 송금하거나 대화를 나누지 마세요</h5>
			</div>
			</div>
			
			
			<div class="row" style="position:relative;width:100%;left:0.8vw;top:-3vh;height:60.5vh;border:1px solid green;">
			<div style="position:relative;top:-1vh;">
			<h4 style="color:#5e5e5e;position:relative;top:1vh;left:2.4vw;font-weight:700;">게스트 소개 </h4>
			<div style="width:4.2vw;height:7.7vh;position:relative;left:3vw;top:2.9vh;">
            <img src="files/${guest_picture}" style="width:100%;height:100%;position:relative;" class="img-circle" alt="avatar">
			</div>
			<h4 style="color:#5e5e5e;position:relative;top:-6vh;left:7.8vw;">${guest_name}</h4>
			<h5 style="color:#9e9e9e;position:relative;top:-6vh;left:7.8vw;">${guest_location}</h5>
            <a style="color:#337a46;position:relative;left:7.8vw;top:-6vh;">신고하기</a>
            <hr style="color:#666666;width:70%;position:relative;top:-6vh;font-weight:900;height:15px;">
            <c:if test="${!empty getHomeNames}">
     <div class="select">
    <select name="slct" id="slct">
<c:forEach var="homeList" items="${getHomeNames}" varStatus="i">
 <option value="${getHomeNames.home_name}">${getHomeNames.home_name}</option>
</c:forEach>
    </select>
  </div>
  </c:if>
			</div>
              
				</div>
			

			
		</div>
		
		
		<!--예약 확인 카드  -->
<div class="card2">
 
  <div class="container">
    <h4><b>예약 신청을 하셨습니다.</b></h4> 
    <p>Current Position</p>
    <button class="btn btn-default" style="background-color:#ff5a5f;color:white;font-weight:800;border:1px solid #ff6b6b;">수락</button><button class="btn btn-default" style="border: 1px solid #c9cacc;font-weight:800;">거절</button> 
  </div>
</div>




<div class="comments-app" ng-app="commentsApp" ng-controller="CommentsController as cmntCtrl" style="position:relative; left:10vw;top:-130vh;height:auto;">

 <input type="hidden" id="message_room_seq" value="${message_room_seq}">
<input type="hidden" id="home_seq" value="${home_seq}">
<input type="hidden" id="userId" value="${userId}">
<input type="hidden" id="host_email" value="${host_email}">

    <!-- From -->
  <div class="comment-form" style="position:relative;left:0vw;">
    <!-- Comment Avatar -->
    <div class="comment-image" style="width:3.7vw;height:7vh;position:relative;left:26vw;top:8vh;">
      <img src="files/${guest_picture}" style="width:100%;height:100%;position:relative;" class="img-circle" alt="avatar">
    </div>

    <form class="form" name="form" >
      <div class="form-row">
        <textarea id="message_content" class="input" style="resize:vertical;position:relative;left:-4.9vw;border: 2px solid #e2e2e2;" ng-model="cmntCtrl.comment.text" required></textarea>
      </div>

   



      <div class="form-row" style="width:100%;">
        <input id="msgSendBt" type="button" class="btn btn-default" value="메세지 보내기" style="background:white;position:relative;left:14vw;color:black;border:1px solid #605d58;font-weight:700;">
      </div>
    </form>
  </div>
  


  
  <!-- Comments List -->
  <div class="comments" >
  
  <div class="co"></div>
  
  
<c:forEach items="${message}" var="message" >

 <c:if test="${message.fromID eq userId}">
    <div class="comment" style="height:17vh;">
      <div class="comment-image" style="width:3.7vw;height:7vh;position:relative;left:26vw;top:5vh;">
        <img src="files/${guest_picture}" style="width:100%;height:100%;position:relative;" class="img-circle" alt="avatar">
      </div>

      <div class="box1 sb5" style="position:relative;left:-3vw;width:80%;height:auto;top:-7vh;">${message.message_content}
      <h5 style="position:relative;top:2vh;left:8vw;">${message.message_time}</h5>
      </div>
      
    </div>
</c:if>
<c:if test="${message.fromID eq host_email}">
    <div class="comment" style="height:17vh;">
      <div class="comment-image"  style="width:3.7vw;height:7vh;">
        <img src="files/${host_picture}" style="width:100%;height:100%;position:relative;left:0vw;top:2vw;" class="img-circle" alt="avatar">
      </div>
 
      <div class="box1 sb6" style="width:80%;top:-8vh;left:2.5vw;">${message.message_content}
      <h5 style="position:relative;top:2vh;left:8vw;">${message.message_time}</h5>
      
      </div>
      
    </div>
</c:if>
    </c:forEach>
    
  </div>
  
 

</div>


   <%@ include file="../../resource/include/footer.jsp" %>   

	<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
	<script src="js/bootstrap.js"></script>
</body>
</html>