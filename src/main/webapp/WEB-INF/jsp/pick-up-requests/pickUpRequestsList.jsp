<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<petclinic:layout pageName="pickUpRequests">
    <h2>My Pick Up Requests</h2>
    
     <td>
     	 <spring:url value="/owners/{ownerId}/pick-up-requests/new" var="newPickUpRequestUrl">
     		<spring:param name="ownerId" value ="${ownerId}" />
     	 </spring:url>
         <a href="${fn:escapeXml(newPickUpRequestUrl)}">New request</a>
     </td>

    <table id="pickUpRequestsTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 25%;">Description</th>
            <th style="width: 10%;">Pet type</th>
            <th style="width: 20%">Physical status</th>
            <th style="width: 7%;">Is accepted</th>
            <th style="width: 15%">Location</th>
            <th style="width: 10%">Contact</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${pickUpRequests}" var="pickUpRequest">
            <tr id="${pickUpRequest.description}">
                <td>
                   <c:out value="${pickUpRequest.description}"/>
                </td>
                <td>
                    <c:out value="${pickUpRequest.petType}"/>
                </td>
                <td>
                    <c:out value="${pickUpRequest.physicalStatus}"/>
                </td>
                <td>
                    <c:out value="${pickUpRequest.isAccepted} "/>
                </td>
                <td>
                    <c:out value="${pickUpRequest.address} "/>
                </td>
                <td>
                	<c:out value="${pickUpRequest.contact}"/>
                </td>
                <c:if test="${pickUpRequest.isAccepted==false}"> 
               <td>
      				<spring:url value="/owners/{ownerUsername}/pick-up-requests/{pickUpId}/delete" var="deletePickUpUrl">
      				<spring:param name="ownerUsername" value="${pickUpRequest.owner.user.username}"/>
                    <spring:param name="pickUpId" value="${pickUpRequest.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(deletePickUpUrl)}">Delete PickUp Request</a>
                </td>           
                </c:if>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>