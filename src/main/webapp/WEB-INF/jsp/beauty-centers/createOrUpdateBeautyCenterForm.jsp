<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="beautyCenter">

<jsp:body>

    <h2>
        Beauty Center
    </h2>
    <form:form modelAttribute="beautyCenter" class="form-horizontal" id="add-beautyCenter-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="Name" name="name"/>
            <petclinic:inputField label="Description" name="description"/>
            <div class="control-group">
                    <petclinic:selectField name="petType" label="Type " names="${types}" size="5"/>
                </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${beautyCenter['new']}">
                        <button class="btn btn-default" type="submit">Add Beauty Center</button>
                    </c:when>
                </c:choose>
            </div>
        </div>
    </form:form>
 </jsp:body>
</petclinic:layout>

