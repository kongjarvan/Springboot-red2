<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp"%>

<div class="container">
	<form action="/users/${users.id}/update" method="post">
		<div class="mb-3 mt-3">
			<input type="text" class="form-control" placeholder="Enter title" name="password" value="${users.password}" required="이 입력란을 작성하세요" maxlength="15">
		</div>
		<div class="mb-3">
			<textarea class="form-control" name="email" required="이 입력란을 작성하세요">${users.email}</textarea>
		</div>
		<button type="submit" class="btn btn-primary">수정완료</button>
	</form>
</div>

<%@ include file="../layout/footer.jsp"%>