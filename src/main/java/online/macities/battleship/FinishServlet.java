package online.macities.battleship;

import online.macities.battleship.model.GameStatus;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "FinishServlet", urlPatterns = "/finish")
public class FinishServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        openNext(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        openNext(request, response);
    }

    private void openNext(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (incorrectState(GameStatus.FINISHED, request, response)) {
            return;
        }

        var player = getPlayer(request);
        var game = getGame(request);
        if (game.isWinner(player)) {
            openWinner(request, response);
        } else {
            openLooser(request, response);
        }
    }

    private void openLooser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/finish-looser.jsp")
                .forward(request, response);
    }

    private void openWinner(ServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/finish-winner.jsp")
                .forward(request, response);
    }
}
