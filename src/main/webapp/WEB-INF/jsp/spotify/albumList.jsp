<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<petclinic:layout pageName="albumList">
    <h2>Albums</h2>

    <table id="albumsTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 15%;">Album Name</th>
            <th style="width: 25%;">Image</th>
            <th style="width: 25%;">Go to Spotify</th>

       
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${albums}" var="album">
            <tr>
                <td id="album.name">
                   <c:out value="${album.name}"/>
                </td>
               
                <td>
                <c:forEach items="${album.images}" begin="0" end="0" var="image">
                   <img src="${image.url}" width="160" height="160"/>
                </c:forEach>
                </td> 
                <td>
                	<a href="${album.externalUrls.spotify}">${album.uri}</a>            
                </td>           
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>