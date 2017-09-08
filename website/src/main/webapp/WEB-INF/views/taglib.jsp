<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="bt" uri="http://www.lld360.com/tags" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<c:set var="assets" value="${ctx}/assets" scope="request"/>
<c:set var="files" value="/files" scope="request"/>