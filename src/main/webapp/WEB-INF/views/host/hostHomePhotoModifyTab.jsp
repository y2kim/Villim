<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="https://code.jquery.com/jquery-3.3.1.js"></script>

<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" />
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" />

<title>사진</title>
<link rel="shortcut icon" href="<c:url value='/resources/img/htitle.png'/>" />

<style>
div {
	box-sizing: border-box;
}
body{
min-width:1280px;
}
#wrapper {
	margin: 30px auto;
	margin-bottom: 100px;
	height: auto;
	width: 70%;
}

#wrapper-sub {
margin:0 auto;
	width: 70%;
}

.wrapper-sub-back {
	margin-top: 30px;
	margin-bottom:50px;
	font-size: 20px;
	width: 100%;
	display: inline-block;
	float: left;
	
}

.back-wrap {
	width: 3%;
	display: inline-block;
	float: left;
}

.back-img {
	width: 100%;
	height: 100%;
}

.back-link {
	display: inline-block;
	float: left;
}

.wrapper-sub-title {
	font-size: 18px;
	margin-top: 30px;
}

.pic {
	margin: 50px auto;
	width: 70%;
	height: 350px;
	border: 4px dotted #D8D8D8;
	position: relative;
	margin-left: 0;
}

.add-mainpic-wrap {
	width: 20%;
	height: 20%;
	margin: 0 auto;
	position: relative;
	top: 120px;  
}
  
.add-img {
	max-width: 100%;
	max-height: 100%;
}

.main-pic {
	width: 99.5%;
	height: 340px;
}

.main-pic-wrap{
	width:100%;
	height: 100%;
	position: relative;
}

.pic-list {
	width: 100%;
	height: 170px;
	margin-top: 3px;
	z-index: 1;
}

.add-pic-wrap {
	display: inline-block;
	float: left;
	width: 30%;
	height: 180px;
	margin: 10px;
	z-index: 1;
	border: 3px dotted #D8D8D8;
}

.add-pic {
	margin: 0 auto;
	width: 98%;
	height: 98%;
}

.add-pic-list {
	width: 100%;
	height: 100%;
}

.add-pic-list-wrap {
	margin: 0 auto;
	width: 30%;
	height: 30%;
	margin-top: 50px;
}

.deltab {
	border: 1px dotted black;
	width: 100px;
	position: absolute;
	z-index: 20;
}
</style>
<script>
function isEmpty(str){
	console.log("isEmpty");
	console.log(str);
    if(typeof str == "undefined" || str == null || str == "")
        return true;
    else
        return false ;
}

function nvl(str, defaultStr){
	console.log("nvl");
	console.log("str:"+str+":"+defaultStr)
    if(typeof str == "undefined" || str == null || str == "")
        str = defaultStr ;
    return str ;
}


//메인사진 삭제--------------------------------------------------------------
function maindel(){
	var seq = ${hdto.home_seq};
	
	$('.main-pic-wrap').hover(function(){
		$("#add-pic1").find("img").each(function() {
			var tomainpic = $(this).attr('src');
			console.log("tomainpic:" +tomainpic); 
		});
	
		console.log("in");
		$(this).prepend("<button id='delmain-btn' class='btn btn-danger' type='button'>삭제</button>");
		$('#delmain-btn').css('position','absolute');
		$('#delmain-btn').css('z-index','10');
		
	},function(){
		console.log("out");
		$("#delmain-btn").remove();
	});
	
	$(document).on('click', '#delmain-btn', function(){
		console.log("delmainbtn");
		var file;
		var separate = 1;
		
	$("#pic").find("img").each(function() {
		console.log("경로: "+ $(this).attr('src'));
		file = $(this).attr('src');
		console.log("file : " + file);
	})
	
	
	$.ajax({
		url:"deletePhoto.do",
		type:"get",
		data:{
			separate:separate,
			file:file,
			seq:seq
		},
		success:function(resp){
			console.log("메인삭제성공 : "+resp);
			
			var toMainPic;
			var separate = 3;
			
			$(".add-pic").find("img").each(function(index) {
				console.log("index:: "+ index);
				console.log("메인사진 삭제 후 경로: "+ $(this).first().attr('src'));
				if(index==0){
					toMainPic = $(this).first().attr('src').split(";")[0];
				}
			});
			
			console.log("home.home_seq" + seq);
			console.log("toMainPic당촘!!!::"+toMainPic);
			
			var mp = $('#main-pic');	
			var mpw = $('#main-pic-wrap');
			
			if(toMainPic==undefined || toMainPic == null){
				alert("없을때");
				$('#ap .add-pic-wrap').first().remove();
				mp.removeClass('main-pic');
				mp.addClass('add-img');
				mp.attr('id', 'upimg');
				mp.attr('src',"<c:url value='/resources/img/imgadd.png'/>")
				mpw.removeClass('main-pic-wrap');
				mpw.addClass('add-mainpic-wrap');
				mpw.attr('id', 'add-mainpic-wrap');
			}
			
			$.ajax({
				url:"deletePhoto.do",
				type:"get",
				data:{ 
					separate:separate,
					file:file,
					seq:seq,
					toMainPic:toMainPic
				},
				success:function(){
					if( $('.add-pic').length ){	// 메인사진을 지울때 homepic이 남아있을때
						$("#main-pic").attr('src', "<c:url value ='"+toMainPic+"'/>");
						$('#ap .add-pic-wrap').first().remove();						
					}
					
				
				}
					
			});
		},
		error:function(){
			console.log("삭제 실패");
		}
	});
	
	
	})
	
}
//메인 사진 삭제 끝-----------------------------------------------------
//homepic 삭제 시작-----------------------------------------
	function hpdel(cnt){
			$(document).on('click', "pic-list"+cnt+"",function(){
				$("#add-pic"+cnt+"").find("img").each(function() {
					console.log("경로: "+ $(this).attr('src'));
					})
				})
									
				$("#add-pic"+cnt+"").hover(
					function(){
						console.log("in");
							$(this).prepend("<button id='delbtn"+cnt+"' class='btn btn-danger' type='button'>삭제</button>");
								$("#delbtn"+cnt+"").css('position','absolute');
								$("#delbtn"+cnt+"").css('z-index','100');
//			 					$('.add-pic-fix').css('z-index','1');
								},function(){
								console.log("out");
								$("#delbtn"+cnt+"").remove();
								}
							);
									
						$(document).on('click', "#delbtn"+cnt+"",function(){
							var file;
							var separate=2;
							$("#add-pic"+cnt+"").find("img").each(function() {
								console.log("경로: "+ $(this).attr('src'));
								file = $(this).attr('src');
								console.log("file : " + file);
										$("#add-pic-wrap"+cnt+"").remove();
									
								$.ajax({
									url:"deletePhoto.do",
										type:"get",
										data:{
										separate:separate,
										file:file
										},
										success:function(resp){
										console.log("삭제성공 : "+resp);
										//	location.reload();
										},
										error:function(resp){
											console.log("삭제 실패");
										}
									});
										
								})
							})
						
						}
//homepic 삭제 끝-----------------------------------------						
</script>
</head>
<body>
<%int cnt=0; %>
	<%@ include file="../../resource/include/hostHeader.jsp"%>
	<div id="wrapper">
		<div id="wrapper-sub">
			<div class="wrapper-sub-back">
				<div class="back-wrap"><img class="back-img" src="<c:url value='/resources/img/back.png'/>"></div>
				<div class="back-link"><a href="hostHomeTab.do?seq=${hdto.home_seq}">${hdto.home_name } 수정으로 돌아가기</a></div>
			</div>
				<p class="wrapper-sub-title">
					<b>사진</b>
				</p>
				<p>게스트에게 숙소의 실제 모습을 보여주는 사진을 추가하세요.</p>
				<div id="pic" class="pic">
			
						
					<form id="photoForm" action="uploadPhoto.do"
						enctype="multipart/form-data" method=post>
						<input type="file" id="file2" name="file" style="display: none;">
						<input type="hidden" name="seq" value="${param.seq }">
					
					
						<c:choose>
							<c:when
								test="${hdto.home_main_pic eq null}">

								<div id="add-mainpic-wrap" class="add-mainpic-wrap">
									<img id="upimg" class="add-img"
										src="<c:url value='/resources/img/imgadd.png'/>">
								</div>

							</c:when>
							<c:otherwise>
							<div id="main-pic-wrap" class="main-pic-wrap">								
								<img id="main-pic" class="main-pic"
									src="<c:url value='files/${hdto.home_main_pic }'/>">
							</div>
							<script>
								maindel();  
							</script> 
							</c:otherwise>
						</c:choose>
					</form>
						<!-- 커버사진 삭제 로직 -->


				</div>

				<div id="ap" class="row" style="margin: auto;">
				<c:if test="${hplist.size() > 0 }">
					<c:forEach var="hplist" items="${hplist }">
						<div id="add-pic-wrap<%=cnt %>" class="add-pic-wrap col-md-4" > 
							<div id="add-pic<%=cnt %>" class="add-pic add-pic-fix" >
								<img id="pic-list<%=cnt %>" class="pic-list" 
									src="<c:url value='files/${hplist.home_pic_name }'/>">
							</div>
<!-- 							<p id="deltab" class="deltab">hi</p> -->
						</div>
					
						<script>
						hpdel(<%=cnt%>);
						
			<%-- 			$(document).on('click', 'pic-list<%=cnt%>',function(){
							$("#add-pic<%=cnt%>").find("img").each(function() {
								console.log("경로: "+ $(this).attr('src'));
							})
						})
						$('#add-pic<%=cnt%>').hover(
							function(){
								console.log("in");
								$(this).prepend("<button id='delbtn<%=cnt%>' class='btn btn-danger' type='button'>삭제</button>");
								$('#delbtn<%=cnt%>').css('position','absolute');
								$('#delbtn<%=cnt%>').css('z-index','100');
// 								$('.add-pic-fix').css('z-index','1');
							},function(){
								console.log("out");
								$("#delbtn<%=cnt%>").remove();
							}
						);
						
						$(document).on('click', '#delbtn<%=cnt%>',function(){
							var file;
							var separate=2;
							$("#add-pic<%=cnt%>").find("img").each(function() {
								console.log("경로: "+ $(this).attr('src'));
								file = $(this).attr('src');
								console.log("file : " + file);
								$('#add-pic-wrap<%=cnt%>').remove();
								
								$.ajax({
									url:"deletePhoto.do",
									type:"get",
									data:{
										separate:separate,
										file:file
									},
									success:function(resp){
										console.log("삭제성공 : "+resp);
										
// 										location.reload();
									},
									error:function(resp){
										console.log("삭제 실패");
									}
								});
								
							})
						}) --%>
						</script>
				<%cnt++; %>
					</c:forEach>
				</c:if>

				<c:if test="${hdto.home_main_pic ne null }">
					<div id="add-pic-wrap<%=cnt%>" class="add-pic-wrap col-md-4">
						<div id="add-pic-list-wrap<%=cnt%>" class="add-pic-list-wrap">
							<form id="photoForm2" action="uploadPhoto.do" enctype="multipart/form-data" method=post>

								<input type="file" id="file3" name="file" style="display: none;">
								<input type="hidden" name="seq" value="${param.seq }">

								<img id="upimgg" class="add-pic-list"
									src="<c:url value='/resources/img/imgadd.png'/>">
							</form>
						</div>
					</div>
				</c:if>
				</div>
		</div>
	</div>
	<script>
		// Home_pic 추가
		$(document).ready(function(){
			var cnt=0;
			cnt = ${hplist.size()};
			console.log("hplist.size()::::"+cnt);
			
		$(document).on('click', "#upimgg", function() {
			console.log("cnt::::"+cnt);
			console.log("upimgg");
			$("#file3").click();
		}); 
		$(document).on('change','#file3', function() {
							console.log("file3");
							var form = $('#photoForm2')[0];
							var formData = new FormData(form);
							
							if($('#file3').val().length){
							
							$.ajax({
										type : "post",
										enctype : "multipart/form-data",
										url : "uploadPhoto.do",
										data : formData,
										processData : false,
										contentType : false,
										cache : false,
										timeout : 6000000,
										success : function(resp) {
											
											if(resp)
											var output="";
											
											
											var addpicw = $("#add-pic-wrap"+cnt+"");
											console.log("#add-pic-wrap+cnt+::"+cnt);
											var upimgg = $("#upimgg");
											var addpiclw = $("#add-pic-list-wrap"+cnt+"");
									addpicw.removeAttr("style");
									upimgg.attr('src',"<c:url value ='files/"+resp.filename+"'/>");
									upimgg.removeClass('add-pic-list');//이미지 없을때의 스타일
									upimgg.addClass('pic-list');//이미지 있을때의 스타일
									upimgg.attr("id", "pic-list"+cnt+"");
									
									
									addpiclw.removeClass('add-pic-list-wrap'); 
									addpiclw.attr("id","add-pic"+cnt+"");
									addpiclw.addClass('add-pic');
									addpiclw.addClass('add-pic-fix');
									console.log("output전의 cnt::"+cnt);
									
									cnt++;
									
									output += "<div id='add-pic-wrap"+cnt+"' class='add-pic-wrap col-md-4'>";
									output += "<div id='add-pic-list-wrap"+cnt+"' class='add-pic-list-wrap'>";
									output += "<form id='photoForm2' action='uploadPhoto.do' enctype='multipart/form-data' method='post'>";
 									output += "<input type='hidden' name='seq' value="+${param.seq}+">";
 									output += "<input type='file' id='file3' name='file' style='display:none;'>"
									output += "<img id='upimgg' class='add-pic-list' src='<c:url value='/resources/img/imgadd.png'/>'>"
									output += "</form>";
									output += "</div>";
									output += "</div>";
									//output += "</div>";
									
									$("#ap").append(output);
									hpdel(cnt-1);
// 									location.reload();
								},
								error : function(resp) {
									console.log("실패");
								}
							});
							}
						})
						
						
						
						
		//main사진 추가 이벤트
		$("#upimg").click(function(e) {
			$("#file2").click();
		});

		$("#file2").change(
						function(event) {
							event.preventDefault();
							var form = $('#photoForm')[0];
							var formData = new FormData(form);

							if($('#file2').val().length){
							$.ajax({
										type : "post",
										enctype : "multipart/form-data", 
										url : "uploadPhoto.do",
										data : formData,
										processData : false,
										contentType : false,
										cache : false,
										timeout : 6000000,
										success : function(resp) {
											
											var output;
											var mainpic = $("#upimg");
											var wrap = $("#add-mainpic-wrap");
											
											if (resp.hdto.home_main_pic != null) {
												wrap.removeClass('add-mainpic-wrap');
												wrap.attr('class','main-pic-wrap');
												wrap.attr('id','main-pic-wrap');
												
												mainpic.attr('src',"<c:url value ='files/"+resp.hdto.home_main_pic+"'/>");
												mainpic.removeClass('add-img');
												mainpic.addClass('main-pic');
												mainpic.attr('id','main-pic');
												

												
// 												<form id="photoForm2" action="uploadPhoto.do"
// 													enctype="multipart/form-data" method=post>

// 													<input type="file" id="file3" name="file" style="display: none;">
// 													<input type="hidden" name="seq" value="${param.seq }">

// 													<img id="upimgg" class="add-pic-list"
// 														src="<c:url value='/resources/img/imgadd.png'/>">
// 												</form>
												
												output += "<div id='add-pic-wrap0' class='add-pic-wrap col-md-4'>";
												output += "<div id='add-pic-list-wrap0' class='add-pic-list-wrap'>";
												output += "<form id='photoForm2' action='uploadPhoto.do' enctype='multipart/form-data' method='post'>";
												output += "<input type='file' id='file3' name='file' style='display:none;'>";
												output += "<input type='hidden' name='seq' value="+${param.seq}+">";
												output += "<img id='upimgg' class='add-pic-list' src='<c:url value='/resources/img/imgadd.png'/>'>";
												output += "</form>";
												output += "</div>";
												output += "</div>";

												$("#ap").append(output);
												maindel();
// 												location.reload();

											}

										},
									});
							}

						})
						
		
						
		})
	</script>
	<%@ include file="../../resource/include/footer.jsp"%>
</body>
</html>