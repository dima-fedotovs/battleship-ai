package online.macities.battleship;

import online.macities.battleship.model.Game;
import online.macities.battleship.model.GameManager;
import online.macities.battleship.model.Player;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class AppListener implements ServletContextListener, HttpSessionListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        var mgr = new GameManager();
        sce.getServletContext().setAttribute(GameManager.ATTR, mgr);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        var mgr = (GameManager) se.getSession().getServletContext().getAttribute(GameManager.ATTR);
        var game = (Game) se.getSession().getAttribute(Game.ATTR);
        var player = (Player) se.getSession().getAttribute(Player.ATTR);
        if (game != null && player != null) {
            mgr.surrender(game, player);
        }
    }
}
