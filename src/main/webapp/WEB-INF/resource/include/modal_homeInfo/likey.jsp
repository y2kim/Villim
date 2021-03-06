<%@ page pageEncoding="utf8"%>

<style>
	#likeyModal{
		margin-top: 50px;
		margin: 0 auto;
		height:auto;
		font-family:font2 !important;
	}
	
	.modal-dialog.likey-dialog{
		width: 550px;
		height: 100%;
	}
	
	.modal-content.likey-content{
		height: auto;
	}
	
	.modal-header.likey-header{
		width:100%;
		border: 0px !important;
	}
	
	.basicAmenities{
		border-bottom:  1px solid #d6d6d6;
		margin: 20px 0px;
		
	}
	
	.modal-body.likey-body{
		padding:0 30px;
		height: auto;
	}
	
	#makeList{
		color:#008489;
		font-weight: 600;
	}
	
	#makeName{
		margin: 10px 0px;
		width:100%;
	}
	
	#makeName:focus{
		border: 1px solid #008489;
	}
	
	#cancelListBT{
		background-color: white;
		color:#008489;
		border:2px solid #008489;
	}
	
	#makeListBT{
		background-color: #008489;
		color:white;
		border:2px solid #008489;
	}
	
	#likeyListDiv{
		display: none;
	}
	
	.likeyBT{
		width: 25px;
		float: right;
	}
	
	.btn.btn-secondary.likeyButton{
		float: right;
		background-color: white;
	}
	
	.likeyBT:hover{
		cursor: pointer;
	}
	
	.likeyList-content{
		width:100%;
		height: 60px;
		border-top: 1px solid #d6d6d6;
		padding: 15px;
		margin-top: 10px;
	}
	
	#likey-homeInfo{
		padding: 10px 20px;
		height:auto;
	}
	
	#likey-homeInfo:after{
		content: "";
   		display: block;
   		clear: both;
	}
	
	#likey-homeInfo-img{
		float: left;
		width:20%;
		height: 80px;
	}
	
	#likey-homeInfo-content{
		float: left;
		width:80%;
		height: 100%;
		padding-top: 7px;
		padding-left: 15px;
	}
	
	.likey-footer{
		border-top: 1px solid #d6d6d6;
		
	}
</style>

<script>
	$(document).ready(function(){
		$(".likeyButton").click(function(){
			alert("버튼!");
			
			var srcBT = $(this).parent().find('img').attr('src');
			
			if(srcBT == '../resources/img/like.png'){
				$(this).parent().find('img').attr("src","<c:url value='../resources/img/like2.png'/>");
				$("#likeImg").attr("src","<c:url value='../resources/img/like2.png'/>");
				$("#scrollLike").attr("src","<c:url value='../resources/img/like2.png'/>");
				
				var likeylist_Seq = $(this).parent().find('input').val();
				var home_seq = ${hdto.home_seq}
				
				$.ajax({
					url:"likey.do",
					type:"get",
					data:{
						likeylist_Seq:likeylist_Seq,
						home_seq:home_seq
						},
				success:function(){
					console.log("전달 성공!")
					},
				error : function(request,status,error) {
					console.log(request.status + " : " + status + " : " + error);
					}
				})
				
			}else{
				$(this).parent().find('img').attr("src","<c:url value='../resources/img/like.png'/>");
				$("#likeImg").attr("src","<c:url value='../resources/img/like.png'/>");
				$("#scrollLike").attr("src","<c:url value='../resources/img/like.png'/>");
				
				var likeylist_Seq = $(this).parent().find('input').val();
				var home_seq = ${hdto.home_seq}
				
				$.ajax({
					url:"removeLikey.do",
					type:"get",
					data:{
						likeylist_Seq:likeylist_Seq,
						home_seq:home_seq
						},
				success:function(){
					console.log("전달 성공!")
					},
				error : function(request,status,error) {
					console.log(request.status + " : " + status + " : " + error);
					}
				})
				
			}
			
		})
		
		<c:forEach items="${likeyList }" var="likeyList">
			<c:forEach items="${likeyHeart }" var="likeyHeart">
				<c:if test="${likeyList.likeyList_seq eq likeyHeart.likeyList_seq }">
		 			$("#likeyBTID${likeyList.likeyList_seq }").attr('src','<c:url value='../resources/img/like2.png'/>')
		 			
				</c:if>
		 	</c:forEach>	
		</c:forEach>
		
	})
                
</script>

<!-- 모달 팝업 -->
      <div class="modal fade" id="likeyModal" tabindex="-1" role="dialog"
      aria-labelledby="myModalLabel" aria-hidden="true"
      style="z-index:1000000000">
         <div class="modal-dialog likey-dialog">
            <div class="modal-content likey-content">
               <div class="modal-header likey-header">
                  <button type="button" class="close" data-dismiss="modal">
                     <span aria-hidden="true" >×</span><span class="sr-only">Close</span>
                  </button>
               </div>
               <div class="modal-body likey-body">
               		<h2 style="font-weight: 600;">목록에 저장</h2>
               		<div id="likeyListDiv">
               			<h4 style="font-weight: 600; color: black;">이름</h4>	
               			<input type="text" class="form-control" placeholder="이름 정하기" id="makeName"/>
               			<button id="makeListBT" class="btn btn-secondary" style="float:right;">만들기</button>
               			<button id="cancelListBT" class="btn btn-secondary" style="float:right; margin-right: 10px;">취소</button>
               			<br><br>
               		</div>
               		<a href='javascript:void(0);' id="makeList" onclick='newListFunction();'><br>새 목록 만들기</a>
               		<div id="afterLineDiv"></div>
               		<script>
               			function newListFunction() {
               				$("#makeList").css({"display":"none"});
               				$("#likeyListDiv").css({"display":"block"});
               			}
               			
               			$("#cancelListBT").click(function(){
               				$("#likeyListDiv").css({"display":"none"});
               				$("#makeList").css({"display":"block"});
               			});
               			
               			$("#makeListBT").click(function(){
               				var name = $("#makeName").val();
               				
               				
               				if(name == ''){
               					alert("저장 목록 이름을 입력하세요");
               				}else{
               					var likeyListName = $("#makeName").val();
               					$('#makeName').val("");
               					var home_seq = ${hdto.home_seq};
               					
               					$.ajax({
               						url:"likeList.do",
               						type:"get",
               						data:{
               							likeyListName:likeyListName,
               							home_seq:home_seq
               							},
               						success:function(resp){
               							
               							$("#likeyListDiv").css({"display":"none"});
                           				$("#makeList").css({"display":"block"});
               							
               							$('.likeyList-content').remove();
               							
               							for(var i=0; i<resp.likeyList.length;i++){
               								$("#afterLineDiv").after(
               									$('<div>').attr('class','likeyList-content').attr('id','likeyList-content'+resp.likeyList[i].likeyList_seq).append(
               										$('<p>').attr('style','display: inline; float:left; margin-top: 10px;').attr('id','likeyListPtag'+resp.likeyList[i].likeyList_seq).attr('class','likeyList_name').html(resp.likeyList[i].likeyList_name)
               									)	
               								)
               							}
               							
               							for(var i=0; i<resp.likeyList.length;i++){
               								$("#likeyListPtag"+resp.likeyList[i].likeyList_seq).after(
               									$('<button>').attr('class','btn btn-secondary likeyButton').attr('id','likeyButtonID'+resp.likeyList[i].likeyList_seq).attr('onClick',"clickclick('likeyButtonID"+resp.likeyList[i].likeyList_seq+"','likeyListPtag"+resp.likeyList[i].likeyList_seq+"','"+resp.likeyList[i].likeyList_seq+"')").append(
               										$('<img>').attr('src','<c:url value='../resources/img/like.png'/>').attr('class','likeyBT').attr('id','llImgId'+resp.likeyList[i].likeyList_seq)
               									)
       										)
               							}
               							
               							for(var i=0; i<resp.likeyList.length;i++){
               								for(var j=0; j<resp.likeyLikey.length;j++){
               									if(resp.likeyList[i].likeyList_seq === resp.likeyLikey[j].likeyList_seq){
               										$("#llImgId"+resp.likeyList[i].likeyList_seq).attr('src','<c:url value='../resources/img/like2.png'/>')
               									}
               								}
               							}
               							
               						},
               						error : function(request,status,error) {
               							console.log(request.status + " : " + status + " : " + error);
               						}
               					})
               				}
               			});
               		</script>
               		
               		
               		<c:if test="${likeyList ne null}">
               			<c:forEach items="${likeyList }" var="likeyList">
               					<div class="likeyList-content">
               						<p style="display: inline; float:left; margin-bottom: 0px; margin-top: 10px;" class="likeyList_name">${likeyList.likeyList_name }</p>
               						<input type="hidden" class="hiddenSeq" value="${likeyList.likeyList_seq }">
               						<button class="btn btn-secondary likeyButton">
               							<img src="<c:url value='../resources/img/like.png'/>" class="likeyBT" id="likeyBTID${likeyList.likeyList_seq }">
         							</button>
               					</div>
               			</c:forEach>
               		</c:if>
               </div>
               
               
               
               <p>&nbsp;</p>
               <div class="likey-footer">
                	<div id="likey-homeInfo">
                   		<div id="likey-homeInfo-img">
                   			<img src="<c:url value='../resources/img/home.jpg'/>" style="width:100%; height:100%;">
                   		</div>
                   		<div id="likey-homeInfo-content">
                   			<p style="display: inline; font-weight: 600; font-size: 15px;">${hdto.home_name}</p><br>
                   			<p style="margin-top: 7px; margin-bottom: 0px;">${hdto.home_nation }, ${hdto.home_addr1 }, ${hdto.home_addr2 }</p>
                   			<p style="display: inline; color: #008489;">★★★★★</p><p style="display: inline; font-size: 13px;">후기 137개</p>
                   		</div>
					</div>
               </div>
            </div>
         </div>
         <script>
         var likeState1 = 0;
         function clickclick(BTid,Pid,seq) {
        	 
        	 var home_seq = ${hdto.home_seq }
     		var likeylist_Seq = seq;
     		
     		var listName = $("#"+Pid).html();
			alert(listName);
     		alert(seq);
     		alert(home_seq);
     			
     		var src = $("#"+BTid).find("img").attr("src");
     			
     		if(src == '../resources/img/like.png'){
     			$("#"+BTid)
                 .find("img")
                 .attr("src","<c:url value='../resources/img/like2.png'/>");
     				
     			$("#likeImg").attr("src","<c:url value='../resources/img/like2.png'/>");
     			
     			$.ajax({
					url:"likey.do",
					type:"get",
					data:{
						likeylist_Seq:likeylist_Seq,
						home_seq:home_seq
						},
				success:function(){
					console.log("전달 성공!")
					},
				error : function(request,status,error) {
					console.log(request.status + " : " + status + " : " + error);
					}
				})
     			
     		}else{
     			$("#"+BTid)
                 .find("img")
                 .attr("src","<c:url value='../resources/img/like.png'/>");
     				
     			("#likeImg").attr("src","<c:url value='../resources/img/like.png'/>");
     		}
     		
 		}
         </script>
      </div>