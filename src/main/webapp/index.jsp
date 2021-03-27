<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1" name="viewport">
    <link href="https://www.w3schools.com/w3css/4/w3.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/main.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Raleway:400,800,900" rel="stylesheet">
    <title>Battleship</title>
</head>
<body>

<div class="container w3-display-container w3-animate-opacity w3-text-white">
    <div class="w3-display-topleft w3-padding-large w3-xlarge">
        <img alt="logo" src="img/logo.png" width="80">
    </div>
    <div class="box w3-display-middle w3-round-xxlarge"></div>
    <form class="w3-display-middle w3-center" method="post" action="${pageContext.request.contextPath}/registration">
        <input type="hidden" name="landing" value="true">
        <h1 class="w3-xxlarge w3-animate-top w3-center w3-text-brown">WELCOME TO</h1>
        <h1 class="w3-jumbo w3-animate-top w3-center w3-text-orange">BATTLESHIP</h1>
        <p><input type="text" name="playerName" value="${playerName}" class="w3-input w3-border w3-black" placeholder="your name"></p>
        <p><button type="submit" class="w3-button w3-large w3-brown w3-animate-top w3-round">Start the game</button></p>
    </form>
    <div class="w3-display-bottomleft w3-black" style="width:250px;height:50px;opacity: 0.5"></div>
    <div class="w3-display-bottomleft w3-padding-large">
        Powered by <a href="https://www.developithecus.net/" target="_blank">developithecus SIA</a>
    </div>
</div>

</body>
</html>