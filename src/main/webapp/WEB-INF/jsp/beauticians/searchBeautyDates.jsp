<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="searchBeautyDate">

	<jsp:body>
	    	<script>
            function selectType() {
            	  var x = document.getElementById("date").value;
            	  var a= document.getElementById("beauticianId").value;
            	  var b= document.getElementById("hour").value;
            	  window.location='/beauticians/'+a+'/beautyDates/'+x+'/'+b;
            };
        </script>
    <h2>
        Search Beauty Date
    </h2>
 <form>
        <div class="form-group has-feedback">
             <label for="date">Maximum date:</label>
 			 <input type="date"  id="date" name="date"><br>
 			 <label for="date">Maximum Hour:</label>
 			 <input type="number" id="hour" name="hour" min="16" max="19"><br>
 			 <input type=hidden id="beauticianId" name="beauticianId" value="${beautician.id}"/>
        </div>
        <div class="form-group">
    <button type="button" class="btn btn-default" onclick="selectType()">Search beauty center</button>	
                 
               
            
        </div>
    </form>
 </jsp:body>
</petclinic:layout>

