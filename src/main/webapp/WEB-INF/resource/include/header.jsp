<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!-- header css -->
<link rel="shortcut icon" href="../favicon.ico">
<link href="<c:url value="/resources/css/main/header.css?var=3" />"
	rel="stylesheet">
<link href="<c:url value="/resources/css/main/demo.css?var=2" />"
	rel="stylesheet" />
<link href="<c:url value="/resources/css/main/component.css?var=2" />"
	rel="stylesheet" />
<link href="<c:url value="/resources/css/main/normalize.css" />"
	rel="stylesheet" />
<script src="<c:url value="/resources/js/modernizr.custom.js"/>"></script>
<script src="https://code.jquery.com/jquery-3.3.1.js"></script>

<!-- 재호  -->
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.2.0/css/all.css">
<script src="//developers.kakao.com/sdk/js/kakao.min.js"></script>

<script>
	$(document).ready(function(){
		$(document).click(function(event) {
			  if(
			    $('.toggle > input').is(':checked') &&
			    !$(event.target).parents('.toggle').is('.toggle')
			  ) {
			    $('.toggle > input').prop('checked', false);
			  }
			})
			
/* facebook */
  function statusChangeCallback(response) {
    console.log('statusChangeCallback');
    console.log(response);

    if (response.status === 'connected') {
      // Logged into your app and Facebook.
      testAPI();
    } else {
      // The person is not logged into your app or we are unable to tell.
      document.getElementById('status').innerHTML = 'Please log ' +
        'into this app.';
    }
  }

  function checkLoginState() {
    FB.getLoginStatus(function(response) {
      statusChangeCallback(response);
    });
  }

  window.fbAsyncInit = function() {
    FB.init({
      appId      : '211147649564685',
      cookie     : true,  // enable cookies to allow the server to access 
                          // the session
      xfbml      : true,  // parse social plugins on this page
      version    : 'v2.8' // use graph api version 2.8
    });



    FB.getLoginStatus(function(response) {
      statusChangeCallback(response);
    });

  };


  (function(d, s, id) {
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) return;
    js = d.createElement(s); js.id = id;
    js.src = "https://connect.facebook.net/ko_KR/sdk.js";
    fjs.parentNode.insertBefore(js, fjs);
  }(document, 'script', 'facebook-jssdk'));

  // Here we run a very simple test of the Graph API after login is
  // successful.  See statusChangeCallback() for when this call is made.
  function testAPI() {
    console.log('Welcome!  Fetching your information.... ');
    FB.api('/me',  {fields: 'email,name'},function(response) {
      console.log('Successful login for: ' + response.name +":" + response.email);
      document.getElementById('status').innerHTML =
        'Thanks for logging in, ' + response.name + ':' + response.email;
    });
  }
  
  /* kakao */
//<![CDATA[
  // 사용할 앱의 JavaScript 키를 설정해 주세요.
  Kakao.init('1eb250eb8bf7adbda4d75c43029ba939');
  function loginWithKakao() {
    // 로그인 창을 띄웁니다.
    Kakao.Auth.login({
  	  
      success: function(authObj) {
      	 Kakao.API.request({
               url: '/v1/user/me',
               success: function(res) {
                 alert(JSON.stringify(res));
                   alert(res.kaccount_email);
               },
               fail: function(error) {
                 alert(JSON.stringify(error));
               }
             });
      },
      fail: function(err) {
        alert(JSON.stringify(err));
      }
    });
  };
//]]>
  
  /* google */
  function loginWithGoogle(){
		
		/* location.href ='${google_url}'; */
		/* var google_url = '${google_url}'
		location.href = google_url; */
		var popupX = (window.screen.width/2) - (500 / 2);
		var popupY= (window.screen.height/2) - (500 / 2);
		
		
		window.open('${google_url}', '', 'status=no, height=500, width=500, left='+ popupX + ', top='+ popupY + ', screenX='+ popupX + ', screenY= '+ popupY);
		

		
	}
	})
</script>


<div id="header">
	<div id="header-logo">
		<img src="<c:url value='/resources/img/logo2.png'/>">
	</div>
	<div id="header-search" class="form-search form-inline">
		<img src="<c:url value='/resources/img/search.png'/>"> <input
			type="text" class="search-query form-control" placeholder="모든 위치·숙소" />
	</div>
	<div id="header-menu" class="container">
		<section class="color-10">
			<nav class="cl-effect-10">
				<div id="header-menu-div" class="dropdown hover">
					<a href="#"
						style="width: 100%; color: black; overflow: hidden; margin: 0px; font-size: 0.93vw; font-weight: 500; padding-left: 14px;">
						<span data-hover="숙소추가">숙소추가</span>
						<ul>
							<li><a href="#">Item</a></li>
							<li><a href="#">Product</a></li>
							<li><a href="#">Text</a></li>
							<li><a href="#">Page</a></li>
							<li><a href="#">Thing</a></li>
							<li><a href="#">Product</a></li>
							<li><a href="#">Text</a></li>
						</ul>
					</a>
				</div>
				<div id="header-menu-div" class="dropdown hover">
					<a href="hostMain.do"
						style="width: 100%; color: black; overflow: hidden; margin: 0px; font-size: 0.93vw; font-weight: 500; padding-left: 14px;">
						<span data-hover="호스트">호스트</span>
						<ul>
							<li><a href="#">Item</a></li>
							<li><a href="#">Product</a></li>
							<li><a href="#">Text</a></li>
							<li><a href="#">Page</a></li>
							<li><a href="#">Thing</a></li>
							<li><a href="#">Product</a></li>
							<li><a href="#">Text</a></li>
						</ul>
					</a>
				</div>
				<div id="header-menu-div" class="dropdown hover">
					<a href="hostMain.do"
						style="width: 100%; color: black; overflow: hidden; margin: 0px; font-size: 0.93vw; font-weight: 500; padding-left: 14px;">
						<span data-hover="저장목록">저장목록</span>
						<ul>
							<li><a href="#">Item</a></li>
							<li><a href="#">Product</a></li>
							<li><a href="#">Text</a></li>
							<li><a href="#">Page</a></li>
							<li><a href="#">Thing</a></li>
							<li><a href="#">Product</a></li>
							<li><a href="#">Text</a></li>
						</ul>
					</a>
				</div>
				<div id="header-menu-div" class="dropdown hover">
					<a href="hostMain.do"
						style="width: 100%; color: black; overflow: hidden; margin: 0px; font-size: 0.93vw; font-weight: 500; padding-left: 14px;">
						<span data-hover="여행">여행</span>
					</a>
				</div>
				<div id="header-menu-div" class="dropdown hover">
					<a href="hostMain.do"
						style="width: 100%; color: black; overflow: hidden; margin: 0px; font-size: 0.93vw; font-weight: 500; padding-left: 14px;">
						<span data-hover="메세지">메세지</span>
						<ul>
							<li><a href="#">Item</a></li>
							<li><a href="#">Product</a></li>
							<li><a href="#">Text</a></li>
						</ul>
					</a>
				</div>
				<div id="header-menu-div" class="dropdown hover">
					<a href="hostMain.do"
						style="width: 100%; color: black; overflow: hidden; margin: 0px; font-size: 0.93vw; font-weight: 500; padding-left: 14px;">
						<span data-hover="도움말">도움말</span>

					</a>
				</div>
				<div id="header-menu-div" class="dropdown hover">
					<a data-toggle="modal" href="#myModal"
						style="width: 100%; color: black; overflow: hidden; margin: 0px; font-size: 0.93vw; font-weight: 500; padding-left: 14px;">
						<span data-hover="회원가입">회원가입</span>

					</a>
				</div>
				<div id="header-menu-div" class="dropdown hover">
					<a data-toggle="modal" href="#myModal1"
						style="width: 100%; color: black; overflow: hidden; margin: 0px; font-size: 0.93vw; font-weight: 500; padding-left: 14px;">
						<span data-hover="로그인">로그인</span>

					</a>
				</div>
				
			</nav>
		</section>
	</div>
	<!-- 재호 -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="outline: none">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        
      </div>
      
      <div class="modal-body">
      <button type="button" class="btn btn-primary" onclick="FB.login();" id="facebook">
      	<i class="fab fa-facebook-f fa-2x" style="color:white"></i>  
      	<font> 페이스북 계정으로 회원가입</font>
      </button><br>
      
      <button type="button" class="btn btn-warning" onclick="loginWithKakao();" id="kakao">
      	<i class="fas fa-comments fa-2x" style="color:black"></i>
      	<font> 카카오 계정으로 회원가입</font>
      </button><br>
  
      <button type="button" class="btn btn-default" onclick="loginWithGoogle();" id="google">
      	<i class="fab fa-google fa-2x" style="color:red"></i>
      	<font> 구글 계정으로 회원가입</font>
      </button><br>
      <span class="_1xktqfm"><span class="_1cd6lfn9"><span>또는</span></span></span><br>
      <!-- <div id="status"></div> -->
      
      <button type="button" class="btn btn-danger" onclick="" id="email">
      	<i class="" style="color:red"></i>
      	<font> 이메일로 회원가입</font>
      </button><br>
      <!-- <button type="button" class="btn btn-primary" onclick="FB.logout();">logout</button> -->
      	
      </div>
      <div class="modal-footer">
      <!--   <button type="button" class="btn btn-default" data-dismiss="modal">Close</button> -->
       <font>이미 계정이 있나요?</font><a href="#"><font>로그인</font></a>
       
      </div>
    </div>
  </div>
</div>
	<div id="header-pic">
		<img src="<c:url value='/resources/img/1.jpg'/>">
	</div>
</div>