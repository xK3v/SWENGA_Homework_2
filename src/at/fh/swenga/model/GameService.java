package at.fh.swenga.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

@Repository
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class GameService {

	private List<GameModel> games = new ArrayList<GameModel>();

	/**
	 * Add game to List
	 * 
	 * @param game
	 */
	public void addGame(GameModel game) {
		games.add(game);
	}

	/**
	 * Verify if list contains game with same ID
	 * 
	 * @param game
	 * @return
	 */
	public boolean contains(GameModel game) {
		return games.contains(game);
	}

	/**
	 * convenient method: true if list is empty
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return games.isEmpty();
	}

	/**
	 * try to find GameModel with given ID return model, otherwise null
	 * 
	 * @param id
	 * @return
	 */
	public GameModel getGameByID(int id) {

		for (GameModel gameModel : games) {
			if (gameModel.getId() == id) {
				return gameModel;
			}
		}
		return null;
	}

	/**
	 * return list with all games
	 * 
	 * @return
	 */
	public List<GameModel> getAllGames() {
		return games;
	}

	/**
	 * return a new list with all games where name or developer contains search
	 * string
	 * 
	 * @param searchString
	 * @return
	 */
	public List<GameModel> getFilteredGames(String searchString) {

		if (searchString == null || searchString.equals("")) {
			return games;
		}

		// List for results
		List<GameModel> filteredList = new ArrayList<GameModel>();

		// check every game
		for (GameModel gameModel : games) {

			if ((gameModel.getName() != null && gameModel.getName().contains(searchString))
					|| (gameModel.getDeveloper() != null && gameModel.getDeveloper().contains(searchString))) {
				filteredList.add(gameModel);
			}
		}
		return filteredList;
	}

	/**
	 * remove games with same id
	 * 
	 * @param id
	 * @return
	 */
	public boolean remove(int id) {
		return games.remove(new GameModel(id, null, null, null));
	}
}