<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<petclinic:layout pageName="beauticians">
    <h2>Beauty Dates</h2>

    <table id="beauticiansTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 15%;">Pet name</th>
            <th style="width: 25%;">Beauty Center name</th>
            <th style="width: 25%;">Description</th>
            <th style="width: 20%">Start time</th>
            <th style="width: 15%"></th>
       
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