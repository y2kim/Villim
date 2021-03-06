<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
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
<link rel="shortcut icon" href="<c:url value='/resources/img/titleLogo.png'/>" />
<title>후기</title>


<link rel= "stylesheet" type="text/css" href="/resources/css/member/reviewWrite.css">
<Style>
/* =============================================
* RADIO BUTTONS
=============================================== */

#radios label {
	cursor: pointer;
	position: relative;
}

#radios label + label {
	margin-left: 15px;
}

input[type="radio"] {
	opacity: 0.1; /* hidden but still tabable */
	position: absolute;
}

input[type="radio"] + span {
	font-family: 'Material Icons';
	color: white;
	background-color:#ff5e6b;
	border-radius: 2px;
	padding: 12px;
	transition: all 0.4s;
	-webkit-transition: all 0.4s;
}

input[type="radio"]:checked + span {
	color: black;
  background-color: #ff4242;
  
  
}

input[type="radio"]:focus + span {
	color: black;
}

/* ================ TOOLTIPS ================= */

#radios label:hover::before {
	content: attr(for);
	font-family: Roboto, -apple-system, sans-serif;
	text-transform: capitalize;
	font-size: 11px;
	position: absolute;
	top: 170%;
	left: 0;
	right: 0;
	opacity: 0.8;
	background-color: #323232;
	color: #fff;	
	padding: 4px;
	border-radius: 3px;
  display: block;
}

/* =============================================
* CENTERING, CONTAINER STYLING ETC || IGNORE
=============================================== */

h1 {
	font-weight: 300;
	color: white;
	text-align: center;
	margin-bottom: 50px;
}

#radios {
	text-align: center;
	margin 0 auto;
}

.container {
	margin: 0 auto;
}



</Style>


<script>

$(document).ready(function(){
	
	$('#openReview').keyup(function (e){
        var content = $(this).val();
        $(this).height(((content.split('\n').length + 1) * 1.5) + 'em');
        $('#counter').html(content.length + '/500');
    });
    $('#openReview').keyup(); 
	
     
    $('#submitBt').click(function(){
  
    	var satisfaction=Number($(':radio[name="satisfaction"]:checked').val());
    	if(!satisfaction){
    		$('#review_satisfaction').focus();
    	     alert("만족도는 필수적으로 선택해야할 사항입니다.");
    		return false;
    	}else{
    		console.log(satisfaction);
    		$('#review_satisfaction').val(satisfaction);
    	}
    	
    	var accuracy=Number($(':radio[name="accuracy"]:checked').val());
    	if(!accuracy){
    		alert("정확도는 필수적으로 선택해야할 사항입니다.");
    		return false;
    	}else{
    		$('#review_accuracy').val(accuracy);
    	}
    	
    	
    	var cleanliness=Number($(':radio[name="cleanliness"]:checked').val());
    	if(!cleanliness){
    		alert("청결도는 필수적으로 선택해야할 사항입니다.");
    		return false;
    	}else{
    		$('#review_cleanliness').val(cleanliness);
    	}
    	
    	
         var checkIn=Number($(':radio[name="checkIn"]:checked').val());
         if(!checkIn){
        	 alert("체크인은 필수적으로 선택해야할 사항입니다.");
     		return false;
         }else{
        	 $('#review_checkIn').val(checkIn);
         }
         
    	
    	var amenities=Number($(':radio[name="amenities"]:checked').val());
    	if(!amenities){
    		alert("편의시설은 필수적으로 선택해야할 사항입니다.");
    		return false;
    	}else{
    		$('#review_amenities').val(amenities);
    	}
    	
    	var communication=Number($(':radio[name="communication"]:checked').val());
    	if(!communication){
    		alert("커뮤니케이션은 필수적으로 선택해야할 사항입니다.");
    		return false;
    	}else{
    		$('#review_communication').val(communication);
    	}
    	
    	var location=Number($(':radio[name="location"]:checked').val());
    	if(!location){
    		alert("위치는 필수적으로 선택해야할 사항입니다.");
    		return false;
    	}else{
    	 	$('#review_location').val(location);
    	}
      
    	
       	var value=Number($(':radio[name="value"]:checked').val());
       	if(!value){
       		alert("가치는 필수적으로 선택해야할 사항입니다.");
    		return false;
       	}else{
       		$('#review_value').val(value);
       	}
    	
    	
    	 var repurchase=$(':radio[name="repurchase"]:checked').val();
    	if(!repurchase){
    		$('#review_repurchase').val("?");
        }else{
        	$('#review_repurchase').val(repurchase);
        } 
    	
    	
    	 if(!$("#openReview").val()){
    		alert("공개 후기는 필수 입력 사항입니다.")
    		return false;
    	}
    	
    	if(!$("#unopenReview").val()){
    	alert("비공개 후기는 빌림에게 아주 소중한 후기가 될 것입니다.")
    	return false;
    	} 
    	$("#formId").submit();
    	
    	
    })
    
    
    
    
    
    
    
    
    
})
	    
	    
	    
	    
	    
	    
	    
	    </script>
</head>
<body>
	<%@ include file="../../resource/include/header_profile.jsp" %>

	<div id="wrapper" style="height: 2500px; margin: 10px auto">
		<h3
			style="color: #595c63; position: relative; left: 180px; top: 10px;">평점
			및 후기</h3>

<form id="formId" name="formId" action="guestReview.mo" method="post">
		<div class="container"
			style="position: relative; left: -287px; top: 20px;">
			<div class="card hovercard">
				<img src="files/${home_main_pic}"
					alt="" />
				<div class="avatar"></div>
				<div class="info" style="height: 130px;">
					<img src="files/${member_picture}" alt=""
						style="position: relative; left: -95px; top: 53px; height: 50px; width: 50px;" />
					<div class="title"
						style="font-size: 15px; position: relative; left: 18px; top: 10px;">
						${home_name}</div>
					<div class="desc"
						style="position: relative; left: -8px; top: 5px; font-weight: bold;">호스트: ${member_name}</div>
					<div class="desc" style="position: relative; left: 33px;">${checkin} ~ ${checkout}</div>
					<input type="hidden" id="home_seq" name="home_seq" value="${home_seq}">
					
				</div>

			</div>
		</div>

		<div class="container">
			<div class="row">
				<div class="col-sm-8"
					style="position: relative; left: 180px; top: -360px;">
					<h3 style="font-weight: bold;">${member_name}님에 대한 후기를 쓰세요</h3>
					<h5>14일동안 후기를 작성하실수 있습니다. 회원님이 후기를 작성하셔야 호스트도 회원님의 피드백을 읽을 수
						있습니다. 후기 작성 기간이 종료되면, 회원님의 피드백이 공개됩니다.</h5>
					<h5>호스트는 카테고리별 평점은 보지 못하고 전체 평균 별점만 알 수 있습니다.</h5>
				</div>
			</div>
		</div>



		<div class="container"
			style="position: relative; left: 180px; top: -300px;">
			<div class="row">

				<div class="col-sm-5">
					<h3 style="font-weight: bold; position: relative; top: -30px;">숙박이
						어떠셨나요?</h3>
					<div style="position: relative; top: 15px; font-weight: bold;">
						<h3 style="display: inline !important;">전체적 만족도</h3>
						<h5 style="display: inline !important;">(필수항목)</h5>
					</div>

<!-- satisfaction -->
<section id="satisfaction" class="rating">
  <!-- FIFTH HEART -->
  <input type="radio" id="heart_5" name="satisfaction" value="5" />
  <label for="heart_5" title="Five">&#10084;</label>
  <!-- FOURTH HEART -->
  <input type="radio" id="heart_4" name="satisfaction" value="4" />
  <label for="heart_4" title="Four">&#10084;</label>
  <!-- THIRD HEART -->
  <input type="radio" id="heart_3" name="satisfaction" value="3" />
  <label for="heart_3" title="Three">&#10084;</label>
  <!-- SECOND HEART -->
  <input type="radio" id="heart_2" name="satisfaction" value="2" />
  <label for="heart_2" title="Two">&#10084;</label>
  <!-- FIRST HEART -->
  <input type="radio" id="heart_1" name="satisfaction" value="1" />
  <label for="heart_1" title="One">&#10084;</label>
  <input id="review_satisfaction" type="hidden"  name="g_review_satisfaction" >
</section>


				</div>

			</div>
<hr style="position:relative;color:gray;left:-290px;width:600px;">
			<div class="row">

				<div class="col-sm-5">
					
					<div style="position: relative; top: 15px; font-weight: bold;">
						<h3 style="display: inline !important;">정확성</h3>
						<h5 style="display: inline !important;">(필수항목)</h5>
						<h5 >숙소 사진과 설명이 실제 숙소와 얼마나 일치하나요?</h5>
					</div>

<!-- accuracy -->

<section id="accuracy" class="rating" >
  <!-- FIFTH HEART -->
  <input type="radio" id="accuracy_5" name="accuracy" value="5" />
  <label for="accuracy_5" title="Five">&#10084;</label>
  <!-- FOURTH HEART -->
  <input type="radio" id="accuracy_4" name="accuracy" value="4" />
  <label for="accuracy_4" title="Four">&#10084;</label>
  <!-- THIRD HEART -->
  <input type="radio" id="accuracy_3" name="accuracy" value="3" />
  <label for="accuracy_3" title="Three">&#10084;</label>
  <!-- SECOND HEART -->
  <input type="radio" id="accuracy_2" name="accuracy" value="2" />
  <label for="accuracy_2" title="Two">&#10084;</label>
  <!-- FIRST HEART -->
  <input type="radio" id="accuracy_1" name="accuracy" value="1" />
  <label for="accuracy_1" title="One">&#10084;</label>
  <input type="hidden" id="review_accuracy" name="g_review_accuracy">
</section>


				</div>

			</div>

<hr style="position:relative;color:gray;left:-290px;width:600px;">


			<div class="row">

				<div class="col-sm-5">
					
					<div style="position: relative; top: 15px; font-weight: bold;">
						<h3 style="display: inline !important;">청결도</h3>
						<h5 style="display: inline !important;">(필수항목)</h5>
						<h5 >숙소는 회원님이 예상한 것만큼 깨끗했나요?</h5>
					</div>

<!-- cleanliness -->

<section id="cleanliness" class="rating" >
  <!-- FIFTH HEART -->
  <input type="radio" id="cleanliness_5" name="cleanliness" value="5" />
  <label for="cleanliness_5" title="Five">&#10084;</label>
  <!-- FOURTH HEART -->
  <input type="radio" id="cleanliness_4" name="cleanliness" value="4" />
  <label for="cleanliness_4" title="Four">&#10084;</label>
  <!-- THIRD HEART -->
  <input type="radio" id="cleanliness_3" name="cleanliness" value="3" />
  <label for="cleanliness_3" title="Three">&#10084;</label>
  <!-- SECOND HEART -->
  <input type="radio" id="cleanliness_2" name="cleanliness" value="2" />
  <label for="cleanliness_2" title="Two">&#10084;</label>
  <!-- FIRST HEART -->
  <input type="radio" id="cleanliness_1" name="cleanliness" value="1" />
  <label for="cleanliness_1" title="One">&#10084;</label>
  <input type="hidden" id="review_cleanliness"  name="g_review_cleanliness">
</section>


				</div>

			</div>

<hr style="position:relative;color:gray;left:-290px;width:600px;">



			<div class="row">

				<div class="col-sm-5">
					
					<div style="position: relative; top: 15px; font-weight: bold;">
						<h3 style="display: inline !important;">체크인</h3>
						<h5 style="display: inline !important;">(필수항목)</h5>
						<h5 >호스트는 회원님의 원활한 체크인 절차를 위해 최선을 다했나요?</h5>
					</div>

<!-- checkIn -->

<section id="checkIn" class="rating" >
  <!-- FIFTH HEART -->
  <input type="radio" id="checkIn_5" name="checkIn" value="5" />
  <label for="checkIn_5" title="Five">&#10084;</label>
  <!-- FOURTH HEART -->
  <input type="radio" id="checkIn_4" name="checkIn" value="4" />
  <label for="checkIn_4" title="Four">&#10084;</label>
  <!-- THIRD HEART -->
  <input type="radio" id="checkIn_3" name="checkIn" value="3" />
  <label for="checkIn_3" title="Three">&#10084;</label>
  <!-- SECOND HEART -->
  <input type="radio" id="checkIn_2" name="checkIn" value="2" />
  <label for="checkIn_2" title="Two">&#10084;</label>
  <!-- FIRST HEART -->
  <input type="radio" id="checkIn_1" name="checkIn" value="1" />
  <label for="checkIn_1" title="One">&#10084;</label>
  <input type="hidden" id="review_checkIn"  name="g_review_checkIn">
</section>


				</div>

			</div>


<hr style="position:relative;color:gray;left:-290px;width:600px;">


			<div class="row">

				<div class="col-sm-5">
					
					<div style="position: relative; top: 15px; font-weight: bold;">
						<h3 style="display: inline !important;">편의시설</h3>
						<h5 style="display: inline !important;">(필수항목)</h5>
						<h5 >호스트가 숙소 설명에 약속된 모든 것들을 제공하였나요?</h5>
					</div>

<!-- amenities -->

<section id="amenities" class="rating" >
  <!-- FIFTH HEART -->
  <input type="radio" id="amenities_5" name="amenities" value="5" />
  <label for="amenities_5" title="Five">&#10084;</label>
  <!-- FOURTH HEART -->
  <input type="radio" id="amenities_4" name="amenities" value="4" />
  <label for="amenities_4" title="Four">&#10084;</label>
  <!-- THIRD HEART -->
  <input type="radio" id="amenities_3" name="amenities" value="3" />
  <label for="amenities_3" title="Three">&#10084;</label>
  <!-- SECOND HEART -->
  <input type="radio" id="amenities_2" name="amenities" value="2" />
  <label for="amenities_2" title="Two">&#10084;</label>
  <!-- FIRST HEART -->
  <input type="radio" id="amenities_1" name="amenities" value="1" />
  <label for="amenities_1" title="One">&#10084;</label>
   <input type="hidden" id="review_amenities"  name="g_review_amenities">
</section>

				</div>

			</div>


<hr style="position:relative;color:gray;left:-290px;width:600px;">



			<div class="row">

				<div class="col-sm-5">
					
					<div style="position: relative; top: 15px; font-weight: bold;">
						<h3 style="display: inline !important;">커뮤니케이션</h3>
						<h5 style="display: inline !important;">(필수항목)</h5>
						<h5 >숙박시간 전,혹은 도중에 호스트와 연락이 잘 되고,</h5>
						<h5 >문의한 사항에 대하여 호스트가 빨리 응답해 왔습니까?</h5>
					</div>

<!-- amenities -->

<section id="communication" class="rating" >
  <!-- FIFTH HEART -->
  <input type="radio" id="communication_5" name="communication" value="5" />
  <label for="communication_5" title="Five">&#10084;</label>
  <!-- FOURTH HEART -->
  <input type="radio" id="communication_4" name="communication" value="4" />
  <label for="communication_4" title="Four">&#10084;</label>
  <!-- THIRD HEART -->
  <input type="radio" id="communication_3" name="communication" value="3" />
  <label for="communication_3" title="Three">&#10084;</label>
  <!-- SECOND HEART -->
  <input type="radio" id="communication_2" name="communication" value="2" />
  <label for="communication_2" title="Two">&#10084;</label>
  <!-- FIRST HEART -->
  <input type="radio" id="communication_1" name="communication" value="1" />
  <label for="communication_1" title="One">&#10084;</label>
    <input type="hidden" id="review_communication"  name="g_review_communication">
</section>

				</div>

			</div>


<hr style="position:relative;color:gray;left:-290px;width:600px;">


	<div class="row">

				<div class="col-sm-5">
					
					<div style="position: relative; top: 15px; font-weight: bold;">
						<h3 style="display: inline !important;">위치</h3>
						<h5 style="display: inline !important;">(필수항목)</h5>
						<h5 >숙소 지역은 만족하셨나요? 안정성과 편리성을 고려해 주세요</h5>
					</div>

<!-- amenities -->

<section id="location" class="rating" >
  <!-- FIFTH HEART -->
  <input type="radio" id="location_5" name="location" value="5" />
  <label for="location_5" title="Five">&#10084;</label>
  <!-- FOURTH HEART -->
  <input type="radio" id="location_4" name="location" value="4" />
  <label for="location_4" title="Four">&#10084;</label>
  <!-- THIRD HEART -->
  <input type="radio" id="location_3" name="location" value="3" />
  <label for="location_3" title="Three">&#10084;</label>
  <!-- SECOND HEART -->
  <input type="radio" id="location_2" name="location" value="2" />
  <label for="location_2" title="Two">&#10084;</label>
  <!-- FIRST HEART -->
  <input type="radio" id="location_1" name="location" value="1" />
  <label for="location_1" title="One">&#10084;</label>
    <input type="hidden" id="review_location"  name="g_review_location">
</section>

				</div>

			</div>


<hr style="position:relative;color:gray;left:-290px;width:600px;">


	<div class="row">

				<div class="col-sm-5">
					
					<div style="position: relative; top: 15px; font-weight: bold;">
						<h3 style="display: inline !important;">가치</h3>
						<h5 style="display: inline !important;">(필수항목)</h5>
						<h5 >이 숙소를 평가하자면?</h5>
					</div>

<!-- value -->

<section id="value" class="rating" >
  <!-- FIFTH HEART -->
  <input type="radio" id="value_5" name="value" value="5" />
  <label for="value_5" title="Five">&#10084;</label>
  <!-- FOURTH HEART -->
  <input type="radio" id="value_4" name="value" value="4" />
  <label for="value_4" title="Four">&#10084;</label>
  <!-- THIRD HEART -->
  <input type="radio" id="value_3" name="value" value="3" />
  <label for="value_3" title="Three">&#10084;</label>
  <!-- SECOND HEART -->
  <input type="radio" id="value_2" name="value" value="2" />
  <label for="value_2" title="Two">&#10084;</label>
  <!-- FIRST HEART -->
  <input type="radio" id="value_1" name="value" value="1" />
  <label for="value_1" title="One">&#10084;</label>
     <input type="hidden" id="review_value"  name="g_review_value">
</section>

				</div>

			</div>

<hr style="position:relative;color:gray;left:-290px;width:600px;">



<div class="row" style="height:180px;">

				<div class="col-sm-6">
					
					<div style="position: relative; top: 15px; font-weight: bold;">
						<h3 style="display: inline !important;">이 숙소를 다시 이용하고 싶으신가요?</h3>
						<h5 >답변은 회원님의 프로필 또는 호스트의 숙소 페이지에 공개되지 않습니다.</h5>
					</div>
<div id="likeordislikeBt" style="position:relative;top:55px;left:140px;">
        <label for="yesBt" class="material-icons" style="margin-right:20px;width:80px;">
			<input type="radio" name="repurchase" id="yesBt" value="yes" />
			<span><i class="glyphicon glyphicon-thumbs-up"></i>&nbsp;예</span>
		</label>
		<label for="noBt" class="material-icons" style="margin-left:13px;">
			<input type="radio" name="repurchase" id="noBt" value="no" />
			<span><i class="glyphicon glyphicon-thumbs-down"></i>&nbsp;아니요</span>
		</label>
		<input type="hidden" id="review_repurchase" name="g_review_repurchase">
</div>
				</div>

			</div>
			
			<hr style="position:relative;color:gray;left:-290px;width:600px;">
	
	<div class="row">

				<div class="col-sm-5">
					
					
						<h3 style="display: inline !important;">공개 후기</h3>
						<h5 style="display: inline !important;">(필수항목)</h5>
						<h5>회원님의 후기는 회원님의 프로필과 회원님의 호스트 숙소 페이지에 전체 공개 됩니다.</h5>
						<div class="col-sm-18" style="height:auto;">
						<textarea class="form-control"   id="openReview" name="g_review_public"
							style="resize: vertical;margin-top:10px;height:200px;max-height: 80px;min-height: 200px;line-height:1.6em;max-height: 400px;" maxlength='500' ></textarea>
							<span id="counter">###</span>
					</div>
					
						
							</div>

			</div>
								
	<div class="row" style="margin-top:10px;">

				<div class="col-sm-5">
					
						<h3 style="margin-bottom:10px;">호스트 비공개 피드백</h3>
						<h5>이 피드백은 비공개이며,해당 호스트와 빌림 직원 및 서비스 제공자만 볼 수 있습니다</h5>
						<div class="col-sm-18" style="height:auto;">
						<textarea class="form-control" id="unopenReview" name="g_review_private"
							style="resize: vertical;margin-top:15px;height:200px;min-height: 200px;line-height:1.6em;max-height: 380px;" maxlength='500'></textarea>
					</div>
				


				</div>

			</div>

<div class="row">

<input id="submitBt" class="btn btn default" type="button" style="background-color:#ff5e6b;color:white;font-weight:bold;position:relative;left:385px;top:50px;" value="제출 하기">

</div>






		</div>
		</form>
	</div>





	<%@ include file="../../resource/include/footer.jsp"%>
	<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
	<script src="js/bootstrap.js"></script>
</body>
</html>