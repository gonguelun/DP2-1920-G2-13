<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="products">
    <jsp:body>
        <h2>
            <c:if test="${product['new']}">New </c:if> Product
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
          
            <div class="form-group">
            	<div class="col-sm-offset-2 col-sm-10">
                	<c:choose>
                    	<c:when test="${product['new']}">
                        	<button class="btn btn-default" type="submit">Create Product</button>
                    	</c:when>
                    	<c:when test="${!product['new']}">
                       		<button class="btn btn-default" type="submit">Modify Product</button>
                   		</c:when>
                	</c:choose>
            	</div>
        	</div>
        </form:form>
    </jsp:body>
</petclinic:layout>
