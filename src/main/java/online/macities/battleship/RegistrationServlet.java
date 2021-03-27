package online.macities.battleship;

import online.macities.battleship.model.Game;
import online.macities.battleship.model.GameStatus;
import online.macities.battleship.model.Player;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RegistrationServlet", urlPatterns = "/registration")
public class RegistrationServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        openNext(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (incorrectState(GameStatus.INCOMPLETE, request, response)) {
            return;
        }

        var fromLanding = request.getParameter("landing") != null;
        var playerName = stripName(request.getParameter("playerName"));
        request.getSession().setAttribute("playerName", playerName);

        if (playerName == null) {
            request.setAttribute("playerNameTooShort", !fromLanding);
        } else if (playerName.length() < 3) {
            request.setAttribute("playerNameTooShort", true);
        } else if (playerName.length() > 12) {
            request.setAttribute("playerNameTooLong", true);
        } else {
            var mgr = getGameManager(request);
            var player = new Player(playerName);
            var game = mgr.join(player);
            setPlayer(request, player);
            setGame(request, game);
        }
        openNext(request, response);
    }

    private String stripName(String str) {
        if (str == null) {
            return null;
        }
        var result = str.strip();
        return result.isEmpty() ? null : result;
    }

    private void openNext(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (incorrectState(GameStatus.INCOMPLETE, request, response)) {
            return;
        }

        var game = getGame(request);
        if (game == null) {
            openRegistration(request, response);
        } else if (game.getStatus() == GameStatus.INCOMPLETE) {
            openRegistrationAwait(request, response);
        }
    }

    protected void openRegistration(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/registration.jsp")
                .forward(request, response);
    }

    protected void openRegistrationAwait(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/registration-await.jsp")
                .forward(request, response);
    }

}