<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="owners">
    <h2>Beauty Dates</h2>

    <table id="ownersTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 20%;">Pet name</th>
            <th style="width: 30%;">Beauty Center name</th>
            <th style="width: 30%;">Description</th>
            <th style="width: 25%">Start time</th>
       
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${beautyDates}" var="beautyDate">
            <tr>
                <td>
                   <c:out value="${beautyDate.pet.name}"/>
                </td>
                <td>
                    <c:out value="${beautyDate.beautyCenter.name}"/>
                </td>
                <td>
                    <c:out value="${beautyDate.description}"/>
                </td>
                <td>
                    <c:out value="${beautyDate.startDate} "/>
                </td>
                <td>
                	<spring:url value="/owners/{ownerUsername}/beauty-dates/{beautyDateId}/delete" var="beautyDateUrl">
                		<spring:param name="ownerUsername" value ="${beautyDate.pet.owner.user.username}" />
                		<spring:param name="beautyDateId" value="${beautyDate.id}"/>
                	</spring:url>
                    <a href="${fn:escapeXml(beautyDateUrl)}">Delete Beauty Date</a>
                </td>
           
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>