<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="products">
    <jsp:body>
        <h2>
            New Product
        </h2>
        <form:form modelAttribute="product"
                   class="form-horizontal">
            <div class="form-group has-feedback">
                <petclinic:inputField label="Name" name="name"/>
                <petclinic:inputField label="Description" name="description"/>
                <div class="control-group">
                    <petclinic:selectField name="type" label="Type" names="${specialization}" size="5"/>
                </div>
                <input type="checkbox" name="avaliable" value="true">
 					 <label for="avaliable"> Avaliable</label><br>
            </div>
                 <button class="btn btn-default" type="submit">Create</button>
        </form:form>
    </jsp:body>
</petclinic:layout>
