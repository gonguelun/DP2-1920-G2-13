<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="beautyCenterList">

    <h2>Beauty Centers (services)</h2>

	<sec:authentication var="principal" property="principal" />

    <table id="beautyTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Name</th>
            <th style="width: 200px;">Description</th>
            <th>Pet Type</th>
            <th>Beautician</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${beautyCenters}" var="beautyCenter">
            <tr>
                <td>
                   <c:out value="${beautyCenter.name}"/>
                </td>
                <td>
                    <c:out value="${beautyCenter.description}"/>
                </td>
                <td>
                    <c:out value="${beautyCenter.petType}"/>
                </td>
                <td>
                	<c:out value="${beautyCenter.beautician.firstName} ${beautyCenter.beautician.lastName}"/>
                </td>
                <td>
                  	<spring:url value="/owners/{ownerUsername}/beauty-centers/{beautyCenterId}/{petTypeId}/beauty-dates/new" var="dateUrl">
                         <spring:param name="ownerUsername" value="${principal.username}"/>
                         <spring:param name="beautyCenterId" value="${beautyCenter.id}"/>
                         <spring:param name="petTypeId" value="${beautyCenter.petType.id}"/>
                     </spring:url>
                     <a href="${fn:escapeXml(dateUrl)}">Book a date</a>
                
                
                
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>


</petclinic:layout>