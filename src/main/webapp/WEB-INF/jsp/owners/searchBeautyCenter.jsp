<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<petclinic:layout pageName="beautySearch">
    <jsp:body>
    	<script>
            function selectType() {
            	  var x = document.getElementById("typePet").value;
            	  window.location='/owners/beauty-centers/'+x;
            };
        </script>
    <h2>Choose a Pet Type filter for the Beauty Centers</h2>
    <form>
		<div class="control-group">
                    <select id="typePet">
                      <c:forEach items="${type}" var="type">
    					<option value="${type.id}"><c:out value="${type}"/></option>
    					</c:forEach>
   						 
 						 </select>
                </div>
                </form>
                <br>
                <button type="button" onclick="selectType()">Search beauty center</button>	
	</jsp:body>

</petclinic:layout>