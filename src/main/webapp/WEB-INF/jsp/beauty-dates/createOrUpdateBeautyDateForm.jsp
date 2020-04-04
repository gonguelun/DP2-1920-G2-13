<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="beautyDate">

<jsp:body>
    <h2>
        Beauty Date
    </h2>
    <form:form modelAttribute="beautyDate" class="form-horizontal" id="add-beautyDate-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="Description" name="description"/>
            <div class="control-group">
                    <petclinic:selectField name="startDate" label="Start Date" names="${dateWeek}" size="28"/>
            </div>
            <div class="control-group">
             	<petclinic:selectField name="pet" label="Pets " names="${ownerPets}" size="5"/>  
             </div>
             <div class="control-group">
             	<petclinic:selectField name="products" label="Products " names="${products}" size="5"/>  
             </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${beautyDate['new']}">
                        <button class="btn btn-default" type="submit">Ask for a date</button>
                    </c:when>
                    <c:when test="${!beautyDate['new']}">
                        <button class="btn btn-default" type="submit">Modify Beauty Date</button>
                    </c:when>
                </c:choose>
            </div>
        </div>
    </form:form>
 </jsp:body>
</petclinic:layout>

