<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="beauticians">

    <h2>Owner Information</h2>


    <table class="table table-striped">
        <tr>
            <th>Name</th>
            <td><b><c:out value="${beautician.firstName} ${beautician.lastName}"/></b></td>
        </tr>
        <tr>
            <th>Username</th>
            <td><b><c:out value="${beautician.user.username}"/></b></td>
        </tr>
        <tr>
            <th>Animal type specializations</th>
            <td><div class="control-group">
            	<c:forEach var="spec" items="${beautician.specializations}">
            		<c:out value="${spec}"/><br/>
            	</c:forEach>
            </div></td>
        </tr>

    </table>

    <spring:url value="{beauticianId}/edit" var="editUrl">
        <spring:param name="beauticianId" value="${beautician.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Edit Beautician</a>
    <br/>
    <br/>
    <br/>
    
    
    
</petclinic:layout>
