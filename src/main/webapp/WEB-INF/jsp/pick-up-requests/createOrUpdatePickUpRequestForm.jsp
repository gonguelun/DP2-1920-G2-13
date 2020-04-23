<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="pickUpRequest">

<jsp:body>

    <h2>
        <c:if test="${pickUpRequest['new']}">New </c:if> Pick Up Request
    </h2>
    <form:form modelAttribute="pickUpRequest" class="form-horizontal" id="add-pickUpRequest-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="Description" name="description"/>
            <div class="control-group">
                    <petclinic:selectField name="petType" label="Type " names="${types}" size="5"/>
            </div>
            <petclinic:inputField label="PhysicalStatus" name="physicalStatus"/>
            <petclinic:inputField label="Address" name="address"/>
            <input type="hidden" name="isAccepted" value="${pickUpRequest.isAccepted}" />
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${pickUpRequest['new']}">
                        <button class="btn btn-default" type="submit">Send pick up request</button>
                    </c:when>
                </c:choose>
            </div>
        </div>
    </form:form>
 </jsp:body>
</petclinic:layout>

