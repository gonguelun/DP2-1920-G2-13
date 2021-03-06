<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!--  >%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%-->
<%@ attribute name="name" required="true" rtexprvalue="true"
	description="Name of the active menu: home, owners, vets or error"%>

<nav class="navbar navbar-default" role="navigation">
	<div class="container">
		<div class="navbar-header">
			<a class="navbar-brand"
				href="<spring:url value="/" htmlEscape="true" />"><span></span></a>
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target="#main-navbar">
				<span class="sr-only"><os-p>Toggle navigation</os-p></span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
		</div>
		<div class="navbar-collapse collapse" id="main-navbar">
			<ul class="nav navbar-nav">

				<petclinic:menuItem active="${name eq 'home'}" url="/"
					title="home page">
					<span class="glyphicon glyphicon-home" aria-hidden="true"></span>
					<span>Home</span>
				</petclinic:menuItem>
				<sec:authentication var="principal2" property="principal" />
				<sec:authorize access="hasAnyAuthority('owner')">
					<petclinic:menuItem active="${name eq 'owners'}" url="/owners/find"
						title="find owners">
						<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
						<span>Find owners</span>
					</petclinic:menuItem>
					<petclinic:menuItem active="${name eq 'owners'}" url="/owners/search-beauty-center"
						title="search beauty center">
						<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
						<span>Search Beauty Centers</span>
					</petclinic:menuItem>
					<petclinic:menuItem url="/owners/${principal2.username}/beauty-dates" active="${name eq 'owners'}"
						title="view my beauty dates">
						<span>View my beauty dates</span>
					</petclinic:menuItem>
					<petclinic:menuItem url="/owners/${principal2.username}/pick-up-requests" active="${name eq 'owners'}"
						title="view my pick up requests">
						<span>View my pick up requests</span>
					</petclinic:menuItem>
				</sec:authorize>
	
				<sec:authorize access="hasAnyAuthority('vet')">
					<petclinic:menuItem active="${name eq 'vets'}" url="/vets"
						title="veterinarians">
						<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
						<span>Veterinarians</span>
					</petclinic:menuItem>
					<petclinic:menuItem active="${name eq 'pickUpRequests'}" url="/vets/pick-up-requests"
						title="pickUpRequests">
						<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
						<span>Pick Up Requests</span>
					</petclinic:menuItem>
				</sec:authorize>
				
			
				<sec:authentication var="principal" property="principal" />
				
				<sec:authorize access="hasAnyAuthority('beautician')">	
					<petclinic:menuItem active="${name eq 'beauticians'}" url="/beauticians/principal/${principal.username}"
						title="beauticians">
						<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
						<span>Beautician</span>
					</petclinic:menuItem>
					<petclinic:menuItem active="${name eq 'beauticians'}" url="/beauticians/searchBeautyDates/${principal.username}"
						title="beauticians">
						<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
						<span>Search Beauty Dates</span>
					</petclinic:menuItem>
				</sec:authorize>

				<petclinic:menuItem active="${name eq 'spotify'}" url="/api/spotify/query"
					title="Spotify search">
					<span>Spotify search</span>
				</petclinic:menuItem>
				
				<petclinic:menuItem active="${name eq 'error'}" url="/oups"
					title="trigger a RuntimeException to see how it is handled">
					<span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span>
					<span>Error</span>
				</petclinic:menuItem>
			</ul>




			<ul class="nav navbar-nav navbar-right">
				<sec:authorize access="!isAuthenticated()">
					<li id="login"><a href="<c:url value="/login" />">Login</a></li>
					<li><a href="<c:url value="/users/new-owner" />">Owner Register</a></li>
					<li><a href="<c:url value="/users/new-vet" />">Vet Register</a></li>
					<li><a href="<c:url value="/users/new-beautician" />">Beautician register</a></li>
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown"> <span class="glyphicon glyphicon-user"></span>
							<strong><sec:authentication property="name" /></strong> <span
							class="glyphicon glyphicon-chevron-down"></span>
					</a>
						<ul class="dropdown-menu">
							<li>
								<div class="navbar-login">
									<div class="row">
										<div class="col-lg-4">
											<p class="text-center">
												<span class="glyphicon glyphicon-user icon-size"></span>
											</p>
										</div>
										<div class="col-lg-8">
											<p class="text-left">
												<strong><sec:authentication property="name" /></strong>
											</p>
											<p class="text-left">
												<a href="<c:url value="/logout" />"
													class="btn btn-primary btn-block btn-sm">Logout</a>
											</p>
										</div>
									</div>
								</div>
							</li>
							<li class="divider"></li>
<!-- 							
                            <li> 
								<div class="navbar-login navbar-login-session">
									<div class="row">
										<div class="col-lg-12">
											<p>
												<a href="#" class="btn btn-primary btn-block">My Profile</a>
												<a href="#" class="btn btn-danger btn-block">Change
													Password</a>
											</p>
										</div>
									</div>
								</div>
							</li>
-->
						</ul></li>
				</sec:authorize>
			</ul>
		</div>



	</div>
</nav>
