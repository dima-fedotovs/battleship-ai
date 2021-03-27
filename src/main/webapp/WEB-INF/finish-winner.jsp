<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1" name="viewport">
    <link href="https://www.w3schools.com/w3css/4/w3.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Raleway:400,800,900" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/main.css" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
    <title>Battleship - Winner</title>
</head>
<body>
<div class="w3-display-container" style="min-height: 100%">
    <div class="w3-display-middle w3-card">
        <form class="w3-container w3-padding-large w3-center" method="post">
            <h2>Congratulations, ${player.name}!</h2>
            <h3>You won the game</h3>
            <button type="submit" name="action" value="cancel" class="w3-button w3-green">Start over</button>
        </form>
    </div>
</div>
</body>
</html>
