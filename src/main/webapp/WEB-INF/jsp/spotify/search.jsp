<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<petclinic:layout pageName="Search">


   
    
    	<jsp:body>
    	<script>
            function search() {
            	  var x = document.getElementById("query").value;
            	  window.location='/api/spotify/search/'+x;
            };
        </script>
    <h1>Search for an Artist</h1>
    <p>You will see his wonderful albums.</p>
    <form>
		<div class="control-group">
                    <input type="text" id="query" value="" class="form-control" placeholder="Type an Artist Name"/>
                </div>
                </form>
                <br>
                <button type="button" onclick="search()">Search albums</button>	
	</jsp:body>
    
       
     
 

</petclinic:layout>