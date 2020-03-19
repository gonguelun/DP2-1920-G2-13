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
           
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>