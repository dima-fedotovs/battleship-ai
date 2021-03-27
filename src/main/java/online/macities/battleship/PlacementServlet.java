package online.macities.battleship;

import online.macities.battleship.model.GameStatus;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@WebServlet(name = "PlacementServlet", urlPatterns = "/placement")
public class PlacementServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        openNext(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (incorrectState(GameStatus.SETTING_UP, request, response)) {
            return;
        }

        var player = getPlayer(request);
        var game = getGame(request);
        var addresses = request.getParameterValues("addr");
        if (addresses == null) {
            request.setAttribute("incorrectShipsPlacement", true);
            openPlacement(request, response);
            return;
        }
        var shipAddresses = Set.of(addresses);
        player.setShips(shipAddresses);
        if (!player.isPlayerFieldValid()) {
            request.setAttribute("incorrectShipsPlacement", true);
            openPlacement(request, response);
            return;
        }
        game.checkGameStart();
        openNext(request, response);
    }

    private void openNext(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (incorrectState(GameStatus.SETTING_UP, request, response)) {
            return;
        }

        var player = getPlayer(request);
        if (!player.isPlayerFieldValid()) {
            openPlacement(request, response);
        } else {
            openPlacementAwait(request, response);
        }
    }

    private void openPlacementAwait(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/placement-await.jsp")
                .forward(req, resp);
    }


    private void openPlacement(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/placement.jsp")
                .forward(req, resp);
    }

}
