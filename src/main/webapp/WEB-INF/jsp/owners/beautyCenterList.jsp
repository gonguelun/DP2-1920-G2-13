<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="beautyCenterList">

    <h2>Beauty Centers</h2>


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
            </tr>
        </c:forEach>
        </tbody>
    </table>


</petclinic:layout>