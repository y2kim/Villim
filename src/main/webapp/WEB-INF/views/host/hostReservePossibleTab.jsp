<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" />
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" />

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="https://code.jquery.com/jquery-3.3.1.js"></script>

<title>예약 가능 설정 탭</title>


<style>
div {
	box-sizing: border-box;  
}

#wrapper {
	border: 1px solid black;
	margin: 30px auto;
	width: 70%;
	height: 1000px;
}

.wrapper-sub {
	width: 70%;
}

.home-reserve {
	margin-top: 60px;
}

.home-reserve div {
	display: inline-block;
	float:left;
}

.home-reserve div button {
	background-color: white;
	border: none;
	text-decoration: none;
	outline: 1;
	border-bottom: thin;
	border-bottom-color: black;
	width: 120px;
	height: 70px;
	padding-left: 0;
	font-size: 17px;
}

.price-line {
	border: 0.5px solid #E6E6E6;
	display: inline-block;
	width: 100%;
	margin-top: 40px;
	margin-bottom: 40px;
}

.stay-wrap {
	width: 100%;
}

.stay-title {
	display: inline-block;
	float:left;
	width: 90%;
}

.stay-mod {
	display: inline-block;
	float:left;
	width: 9%;
	text-align: right;
}

.stayPeriod {
	display: inline-block;
	float:left;
	margin-top: 30px;
	width: 100%;
}

.stayPeriod-sub1 {
	display: inline-block;
	font-size: 17px;
	width: 49%;
}

.stayPeriod-sub2 {
	display: inline-block;
	font-size: 17px;
	width: 49%;
	text-align: right;
}

.check-wrap {
	width: 100%;
}

.check-title {
	display: inline-block;
	float:left;
	width: 90%;
}

.check-mod {
	display: inline-block;
	float:left;
	width: 9%;
	text-align: right;
}

.checkInOut {
	display: inline-block;
	margin-top: 30px;
	width: 100%;
	margin-top: 30px;
}

.checkInOut-sub1 {
	display: inline-block;
	font-size: 17px;
	width: 49%;
}

.checkInOut-sub2 {
	display: inline-block;
	font-size: 17px;
	width: 49%;
	text-align: right;
}

.nav-btn {
	background-color: white;
	border: none;
	text-decoration: none;
	width: 120px;
	height: 70px;
	padding-left: 0;
	font-size: 17px;
}

.nav-btn:hover {
	border-bottom: 2px solid blue;
}

.nav-btn:active {
	text-decoration: none;
	border: none;
	outline: none;
}

.nav-btn:focus {
	text-decoration: none;
	border: none;
	outline: none;
}

.nav-btn:checked {
	text-decoration: none;
}
</style>


</head>
<body>
<%@ include file="../../resource/include/hostHeader.jsp"%>
	<div id="wrapper">
		<div class="home-title">
			<h2>Yuljeon-dong, Jangan-gu, Suwon의 아파트</h2>
		</div>

		<div class="home-reserve">
			<div>
				<button class="nav-btn" type="button"
					onclick="location.href='hostHomeTab.do?seq=${hdto.home_seq}'">숙소
					세부정보</button>
			</div>
			<div>
				<button class="nav-btn" type="button"
					onclick="location.href='hostReserveTab.do?seq=${hdto.home_seq}'">예약 설정</button>
			</div>
			<div>
				<button class="nav-btn" type="button"
					onclick="location.href='hostPriceTab.do?seq=${hdto.home_seq}'">요금</button>
			</div>
			<div>
				<button class="nav-btn" type="button"
					onclick="location.href='hostReservePossibleTab.do?seq=${hdto.home_seq}'">예약 가능
					여부</button>
			</div>
			<div>
				<button type="button">현지 법규</button>
			</div>
			<div>
				<button type="button">공동 호스트</button>
			</div>
		</div>

		<div class="price-line"></div>


		<div class="wrapper-sub">

			<div class="stay-wrap">
				<div class="stay-title">
					<h4>
						<b>숙박 기간</b>
					</h4>
				</div>
				<div class="stay-mod">
					<button type="button" class="btn btn-default">수정</button>
				</div>

				<div class="stayPeriod">
					<div class="stayPeriod-sub1">최소 숙박일</div>
					<div class="stayPeriod-sub2">${hdto.home_min_stay }박</div>

					<div class="price-line"></div>

					<div class="stayPeriod-sub1">최대 숙박일</div>
					<div class="stayPeriod-sub2">${hdto.home_max_stay }박</div>
				</div>
				<div class="price-line"></div>

			</div>

			<div class="check-wrap">
				<div class="check-title">
					<h4>
						<b>체크 인과 체크아웃</b>
					</h4>
				</div>
				<div class="check-mod">
					<button type="button" class="btn btn-default">수정</button>
				</div>

				<div class="checkInOut">
					<div class="checkInOut-sub1">체크 인 가능 시간</div>
					<div class="checkInOut-sub2">${hdto.home_checkin } 이후</div>

					<div class="price-line"></div>

					<div class="checkInOut-sub1">체크 아웃 가능 시간</div>
					<div class="checkInOut-sub2">설정되지 않음</div>

					<div class="price-line"></div>

					<div class="checkInOut-sub1">체크 인 불가 요일</div>
					<div class="checkInOut-sub2">15:00 이후</div>
				</div>
				<div class="price-line"></div>

			</div>
		</div>
	</div>
	<%@ include file="../../resource/include/footer.jsp"%>
</body>
</html>