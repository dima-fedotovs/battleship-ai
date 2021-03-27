package online.macities.battleship;

import online.macities.battleship.model.Game;
import online.macities.battleship.model.GameStatus;
import online.macities.battleship.model.Player;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "TurnServlet", urlPatterns = "/turn")
public class TurnServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        openNext(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (incorrectState(GameStatus.IN_PROGRESS, request, response)) {
            return;
        }

        var addr = request.getParameter("addr");
        var game = getGame(request);
        game.fire(addr);
        openNext(request, response);
    }

    private void openNext(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (incorrectState(GameStatus.IN_PROGRESS, request, response)) {
            return;
        }

        var game = getGame(request);
        var player = getPlayer(request);
        if (game.getCurrentPlayer() == player) {
            openTurn(request, response);
        } else {
            openTurnAwait(request, response);
        }
    }

    private void openTurnAwait(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/turn-await.jsp")
                .forward(request, response);
    }

    private void openTurn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/turn.jsp")
                .forward(request, response);
    }
}
