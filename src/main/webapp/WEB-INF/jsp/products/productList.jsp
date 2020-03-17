<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="createproducts">
    <h2>Products</h2>

    <table id="productsTable" class="table table-striped">
        <thead>
        <tr>
            <th>Name</th>
            <th>Description</th>
            <th>Type</th>
            <th>Avaliable</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${products}" var="product">
            <tr>
                <td>
                    <c:out value="${product.name}"/>
                </td>
                <td>
                	<c:out value="${product.description}"/>
                </td>
                <td>
                    <c:out value="${product.type.name}"/>
                </td>
                <td>
                	<c:if test="${product.avaliable == true}">Yes</c:if>
                	<c:if test="${product.avaliable == false}">No</c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>
