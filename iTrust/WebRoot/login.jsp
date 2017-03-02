<%@page errorPage="/auth/exceptionHandler.jsp"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.TransactionType"%>
<%@ page import="net.tanesha.recaptcha.ReCaptcha" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaImpl" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaResponse" %>
<%@ page import="org.apache.commons.codec.digest.DigestUtils" %>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Login";
%>
<%
String remoteAddr = request.getRemoteAddr();
//recaptcha.properties file found in WEB-INF/classes (usually not seen in Eclipse)
ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
//ResourceBundle reCaptchaProps = ResourceBundle.getBundle("recaptcha"); 
reCaptcha.setPrivateKey("6Lcpzb4SAAAAAGbscE39L3UmHQ_ferVd7RyJuo5Y");

String challenge = request.getParameter("recaptcha_challenge_field");
String uresponse = request.getParameter("recaptcha_response_field");

String user = request.getParameter("j_username");
String pass = request.getParameter("j_password");
if(pass!=null){
	String salt = "";
	try {
		long tempID = Long.parseLong(user);
		salt = authDAO.getSalt(tempID);
	} catch (NumberFormatException e){
		salt = "";
	}
	pass = DigestUtils.sha256Hex(pass + salt);
}

if(challenge != null) {
	ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);

	if (reCaptchaResponse.isValid()) {
		loginFailureAction.setCaptcha(true);
		validSession = true;
		response.sendRedirect("/iTrust/j_security_check?j_username=" + user + "&j_password=" + pass);
	} else {
		if(request.getParameter("loginError") == null) {
			loginFailureAction.setCaptcha(false);
			long userMID;
			try{
				userMID= Long.parseLong(user);
				loggingAction.logEvent(TransactionType.LOGIN_FAILURE, userMID, userMID, "");
			}catch(NumberFormatException e){
				loggingAction.logEvent(TransactionType.LOGIN_FAILURE, 0, 0, "Username: "+user);
			}
						
			pageContext.forward("/login.jsp?loginError=true");
		}
	}
} else if(loginFailureAction.needsCaptcha() && user != null ) {
	loginFailureAction.setCaptcha(false);
} else if(user != null && !"true".equals(request.getParameter("loginError"))) {
	session.setAttribute("loginFlag", "true");
	response.sendRedirect("/iTrust/j_security_check?j_username=" + user + "&j_password=" + pass);
}

if(request.getParameter("loginError") != null) {
	loginMessage = loginFailureAction.recordLoginFailure();
	long userMID;
	try{
		userMID= Long.parseLong(user);
		loggingAction.logEvent(TransactionType.LOGIN_FAILURE, userMID, userMID, "");
	}catch(NumberFormatException e){
		loggingAction.logEvent(TransactionType.LOGIN_FAILURE, 0, 0, "Username: "+user);
	}
	response.sendRedirect("/iTrust/login.jsp");
}

%>

<%@include file="/header.jsp" %>
<script type="text/javascript">
	$( document ).ready(function(){
		$('#home-content').delay(1000).animate({opacity:1},3000);
		var sayings = "DISQUALIFIED! (There's one every semester);Get schwifty!;Ohh yea, you gotta get schwifty.;I'm not looking for judgement, just a yes or no. Can you assimilate a giraffe?;I'll tell you how I feel about school, Jerry: it's a waste of time.;Sometimes science is a lot more art, than science. A lot of people don't get that.;It's a figure of speech, Morty! They're bureaucrats! I don't respect them. Just keep shooting, Morty!;They're robots Morty! It's okay to shoot them! They're just robots!;You have to turn them on Morty! The shoes need to be turned on!;Wubbalubbadubdub!;".split(';');
		var index = Math.floor((Math.random()*sayings.length)+1);
		$('.jenkins-quote').html(sayings[index]);
		//$('.jenkins-quote').delay(1000).animate({opacity:1},4000);
		
	});
</script>
<div id="home-content">
	<blockquote><span class="jenkins-quote"></span></blockquote>
	<h1>- quotes by Dr. Jenkins </h1>
	<!-- patient-centered -->
</div>
<%
	if(!loginFailureAction.needsCaptcha()) {
%>
<%
	} else {
%>


<%
	}
%>

<%@include file="/footer.jsp" %>

