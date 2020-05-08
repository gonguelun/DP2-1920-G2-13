<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="error">

    <spring:url value="/resources/images/miOgraImage.png" var="miOgraImage"/>
    <img src="${miOgraImage}"/>

    <h2>You don't have permission to see this page.</h2>

    <p>${exception.message}</p>

</petclinic:layout>
