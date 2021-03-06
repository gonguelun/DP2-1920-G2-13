<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="beauticians">
    <h2>
        Update Beautician
    </h2>
    <form:form modelAttribute="beautician" class="form-horizontal" id="add-beautician-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="First Name" name="firstName"/>
            <petclinic:inputField label="Last Name" name="lastName"/>
            <label>Username</label>
            <input type="text" id="user.username" name="user.username" value="${beautician.user.username}" readonly/>
            <petclinic:inputField label="Password" name="user.password"/>
            <div class="control-group">
            	<petclinic:selectField name="Specializations" label="Specializations" names="${types}" size="6"/>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">        
                <button class="btn btn-default" type="submit">Update Beautician</button>
            </div>
        </div>
    </form:form>
</petclinic:layout>
