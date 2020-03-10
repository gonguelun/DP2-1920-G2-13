<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="beauticians">
    <jsp:body>
        <h2><c:if test="${beautyCenter['new']}">New </c:if>Beauty Center</h2>

        <b>Beautician</b>
        <table class="table table-striped">
            <thead>
            <tr>
                <th>Name</th>
                <th>Surname</th>
                <th>Specialities</th>
            </tr>
            </thead>
            <tr>
                <td><c:out value="${beautyCenter.beautician.name}"/></td>
                <td><c:out value="${beautyCenter.beautician.surname}"/></td>
                <c:forEach var="speciality" items="${beautyCenter.beautician.specialities}">
                        <td><c:out value="${speciality}"/></td>
            	</c:forEach>
            </tr>
        </table>

        <form:form modelAttribute="beautyCenter" class="form-horizontal">
            <div class="form-group has-feedback">
                <petclinic:inputField label="Description" name="description"/>
                <petclinic:inputField label="Duration" name="duration"/>
                <petclinic:inputField label="Pet Type" name="petType"/>
            </div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input type="hidden" name="beauticianId" value="${beautyCenter.beautician.id}"/>
                    <button class="btn btn-default" type="submit">Add Beauty Center</button>
                </div>
            </div>
        </form:form>

    </jsp:body>

</petclinic:layout>
