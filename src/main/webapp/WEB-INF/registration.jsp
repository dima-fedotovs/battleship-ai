<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1" name="viewport">
    <link href="https://www.w3schools.com/w3css/4/w3.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Raleway:400,800,900" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/main.css" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
    <title>Battleship - Registration</title>
</head>
<body>
<div class="w3-display-container" style="min-height: 100%">
    <c:if test="${playerNameTooShort}">
        <div class="w3-display-topcenter">
            <div class="w3-panel w3-red w3-display-container">
                <span onclick="this.parentElement.style.display='none'" class="w3-button w3-display-topright">&times;</span>
                <h3>Error!</h3>
                <p>Player name should be at least 3 characters long!</p>
            </div>
        </div>
    </c:if>
    <c:if test="${playerNameTooLong}">
        <div class="w3-display-topcenter">
            <div class="w3-panel w3-red w3-display-container">
                <span onclick="this.parentElement.style.display='none'" class="w3-button w3-display-topright">&times;</span>
                <h3>Error!</h3>
                <p>Player name should be at most 12 characters long!</p>
            </div>
        </div>
    </c:if>
    <div class="w3-display-middle w3-card" style="width: 40%">
        <form class="w3-container w3-padding-large" method="post" action="registration">

            <label for="nameInput">Enter your name</label>
            <input id="nameInput" name="playerName" type="text" value="${playerName}" class="w3-input w3-border">

            <button type="submit" class="w3-button w3-green w3-block w3-margin-top">Start</button>
        </form>
    </div>
</div>
</body>
</html>
