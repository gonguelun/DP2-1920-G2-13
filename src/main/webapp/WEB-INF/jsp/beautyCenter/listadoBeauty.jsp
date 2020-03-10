<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="beautyCenter">
    <h2>Beauty Center</h2>

    <table id="beautyTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Name</th>
            <th style="width: 200px;">Description</th>
            <th>Duration</th>
            <th>Pets</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${beautyCenter}" var="beautyCenter">
            <tr>
                <td>
                   <c:out value="${beautyCenter.name}"/>
                </td>
                <td>
                    <c:out value="${beautyCenter.description}"/>
                </td>
                <td>
                    <c:out value="${beautyCenter.duration}"/>
                </td>
                <td>
                    <c:forEach var="pet" items="${beautyCenter.petType}">
                        <c:out value="${pet.name} "/>
                    </c:forEach>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>
