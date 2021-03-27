package online.macities.battleship;

import online.macities.battleship.model.Game;
import online.macities.battleship.model.GameManager;
import online.macities.battleship.model.GameStatus;
import online.macities.battleship.model.Player;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class BaseServlet extends HttpServlet {

    protected Game getGame(HttpServletRequest request) {
        return (Game) request.getSession().getAttribute(Game.ATTR);
    }

    protected void setGame(HttpServletRequest request, Game game) {
        request.getSession().setAttribute(Game.ATTR, game);
    }

    protected Player getPlayer(HttpServletRequest request) {
        return (Player) request.getSession().getAttribute(Player.ATTR);
    }

    protected void setPlayer(HttpServletRequest request, Player player) {
        request.getSession().setAttribute(Player.ATTR, player);
    }

    protected GameManager getGameManager(HttpServletRequest request) {
        return (GameManager) request.getServletContext().getAttribute(GameManager.ATTR);
    }

    protected boolean incorrectState(GameStatus expectedStatus, HttpServletRequest request, HttpServletResponse response) throws IOException {
        var action = request.getParameter("action");
        if (action != null && action.equals("cancel")) {
            request.getSession().invalidate();
            response.sendRedirect(request.getContextPath());
            return true;
        }

        var game = getGame(request);
        var actualStatus = game == null ? GameStatus.INCOMPLETE : game.getStatus();
        if (actualStatus == expectedStatus) {
            return false;
        } else {
            openByStatus(actualStatus, request, response);
            return true;
        }
    }

    private void openByStatus(GameStatus status, HttpServletRequest request, HttpServletResponse response) throws IOException {
        switch (status) {
            case INCOMPLETE:
                openRegistrationServlet(request, response);
                break;
            case SETTING_UP:
                openPlacementServlet(request, response);
                break;
            case IN_PROGRESS:
                openTurnServlet(request, response);
                break;
            case FINISHED:
                openFinishServlet(request, response);
                break;
            default:
                // should never happen
                throw new IllegalStateException("Unsupported status " + status);
        }
    }

    private void openRegistrationServlet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/registration");
    }

    private void openFinishServlet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/finish");
    }

    private void openTurnServlet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/turn");
    }

    private void openPlacementServlet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/placement");
    }

}
