<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="https://code.jquery.com/jquery-3.3.1.js"></script>
<title>Insert title here</title>
</head>
<body>
	<c:if test="${result1>0 && result2 > 0}">
		<script>
			location.href="hostHomeTab.do?seq=${hdto.home_seq}";
		</script>  
	</c:if>
</body>
</html>