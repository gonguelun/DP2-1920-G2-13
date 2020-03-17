<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="beauticians">

    <h2>Owner Information</h2>


    <table class="table table-striped">
        <tr>
            <th>Name</th>
            <td><b><c:out value="${beautician.firstName} ${beautician.lastName}"/></b></td>
        </tr>
        <tr>
            <th>Username</th>
            <td><b><c:out value="${beautician.user.username}"/></b></td>
        </tr>
        <tr>
            <th>Animal type specializations</th>
            <td><div class="control-group">
            	<c:forEach var="spec" items="${beautician.specializations}">
            		<c:out value="${spec}"/><br/>
            	</c:forEach>
            </div></td>
        </tr>

    </table>

    <spring:url value="{beauticianId}/edit" var="editUrl">
        <spring:param name="beauticianId" value="${beautician.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Edit Beautician</a>
    
     <spring:url value="/beauticians/{beauticianId}/beauty-centers/new" var="beautyCenterUrl">
        <spring:param name="beauticianId" value="${beautician.id}"/>
     </spring:url>
     <a href="${fn:escapeXml(beautyCenterUrl)}" class="btn btn-default">Add Beauty Center</a>
     
     <spring:url value="/beauticians/{beauticianId}/products/new" var="productNewUrl">
     	<spring:param name="beauticianId" value="${beautician.id}"/>
     </spring:url>
     <a href="${fn:escapeXml(productNewUrl)}" class="btn btn-default">Create Product</a>
    <br/>
    <br/>
    
    <h2>Beauty Center</h2>

    <table id="beautyTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Name</th>
            <th style="width: 200px;">Description</th>
            <th>Pet Type</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${beautician.beautyCenters}" var="beautyCenters">
            <tr>
                <td>
                   <c:out value="${beautyCenters.name}"/>
                </td>
                <td>
                    <c:out value="${beautyCenters.description}"/>
                </td>
                <td>
                    <c:out value="${beautyCenters.petType}"/>
                </td>
                <td>
                	 <spring:url value="/{beautyCenterId}/products" var="productUrl">
                                    <spring:param name="beautyCenterId" value="${beautyCenters.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(productUrl)}">Product List</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    

    
    
    
</petclinic:layout>
