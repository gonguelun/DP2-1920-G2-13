<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<petclinic:layout pageName="pickUpRequests">
    <h2>All Pick Up Requests</h2>
    

    <table id="pickUpRequestsTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 15%;">Description</th>
            <th style="width: 10%;">Pet type</th>
            <th style="width: 10%">Physical status</th>
            <th style="width: 7%;">Is accepted</th>
            <th style="width: 15%">Location</th>
            <th style="width: 7%;">User reporter</th>
            <th style="width: 10%;"> Contact</th>
            <th></th>
            <th></th>
      
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${pickUpRequests}" var="pickUpRequest">
            <tr>
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
                    <c:out value="${pickUpRequest.owner.user.username} "/>
                </td>
                <td>
                	<c:out value="${pickUpRequest.contact}"/>
                </td>
                  <c:if test="${pickUpRequest.isClosed==false}"> 
                <td>
                <spring:url value="/vets/pick-up-requests/{pickUpId}/update" var="updatePickUpUrl">
                    <spring:param name="pickUpId" value="${pickUpRequest.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(updatePickUpUrl)}">Update PickUp Request</a>
                </td>
                </c:if>
                <c:if test="${pickUpRequest.isAccepted==true}"> 
                <td>
                	 
      				<spring:url value="/vets/pick-up-requests/{pickUpId}/delete" var="deletePickUpUrl">
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