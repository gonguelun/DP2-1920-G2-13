<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<petclinic:layout pageName="tracksList">
    <h2>Beauty Dates</h2>

    <table id="tracksTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 15%;">Track name</th>
            <th style="width: 25%;">Preview</th>
            <th style="width: 25%;">Album</th>
            <th style="width: 25%;">Go to Spotify</th>

       
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${tracks}" var="track">
            <tr>
                <td>
                   <c:out value="${track.name}"/>
                </td>
                <td>
                    <audio src="${track.previewUrl}" controls="controls" type="audio/mpeg" preload="preload">
					</audio>
                </td>
                <td>
                   <c:out value="${track.album.name}"/>
                </td>
                <td>
                	<a href="${track.externalUrls.spotify}">${track.uri}</a>            
                </td>           
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>