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
    <title>Battleship - Setting up</title>
</head>
<body>
<div class="w3-display-container" style="min-height: 100%">
    <c:if test="${incorrectShipsPlacement}">
        <div class="w3-display-topcenter">
            <div class="w3-panel w3-red w3-display-container">
                <span onclick="this.parentElement.style.display='none'" class="w3-button w3-display-topright">&times;</span>
                <h3>Error!</h3>
                <p>Incorrect ships configuration!</p>
            </div>
        </div>
    </c:if>
    <div class="w3-display-middle w3-card">
        <form class="w3-container w3-padding-large w3-center" method="post">
            <h1>${player.name}, please place your ships</h1>
                <table class="field">
                    <tr>
                        <td></td>
                        <c:forEach items="A,B,C,D,E,F,G,H,I,J" var="col">
                            <td>${col}</td>
                        </c:forEach>
                    </tr>
                    <c:forEach items="1,2,3,4,5,6,7,8,9,10" var="row">
                        <tr>
                            <td>${row}</td>
                            <c:forEach items="A,B,C,D,E,F,G,H,I,J" var="col">
                                <c:set var="addr" value="${col}${row}"/>
                                <c:set var="cell" value="${player.playerField[addr]}"/>
                                <td class="${cell}"><input type="checkbox" name="addr" value="${addr}" <c:if test="${cell eq 'SHIP' or cell eq 'HIT'}">checked</c:if>></td>
                            </c:forEach>
                        </tr>
                    </c:forEach>
                </table>
            <p><button type="submit" class="w3-button w3-green w3-margin-top">Ready</button></p>
        </form>
    </div>
</div>
<script>
    $(function () {
        $('table.field input').change(function () {
            if (this.checked) {
                $(this).parent().addClass("SHIP");
            } else {
                $(this).parent().removeClass("SHIP");
                $(this).parent().removeClass("HIT");
            }
        });
    });
</script>
</body>
</html>
