package at.fh.swenga.controller;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import at.fh.swenga.model.GameModel;
import at.fh.swenga.model.GameService;

@Controller
public class GameManagerController {
	@Autowired
	private GameService gameService;

	@RequestMapping(value = { "/", "listGames" })
	public String showAllGames(Model model) {
		model.addAttribute("games", gameService.getAllGames());
		return "listGames";
	}

	@RequestMapping("/fillGameList")
	public String fillGameList(Model model) {
		Date now = new Date();

		gameService.addGame(new GameModel(1, "CSGO", "Valve", now));
		gameService.addGame(new GameModel(2, "League of Legends", "Riot Games", now));
		gameService.addGame(new GameModel(3, "PUBG", "Bluehole", now));
		gameService.addGame(new GameModel(4, "Apex: Legends", "Respawn Entertainment", now));

		model.addAttribute("games", gameService.getAllGames());
		return "listGames";
	}

	@GetMapping("/addGame")
	public String showAddGameForm(Model model) {
		return "editGame";
	}

	@PostMapping("/addGame")
	public String addGame(@Valid GameModel newGameModel, BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			String errorMessage = "";
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				errorMessage += fieldError.getField() + " is invalid: " + fieldError.getCode() + "<br>";
			}
			model.addAttribute("errorMessage", errorMessage);

			return "forward:/listGames";
		}

		GameModel game = gameService.getGameByID(newGameModel.getId());

		if (game != null) {
			model.addAttribute("errorMessage", "Game already exists!<br>");
		} else {
			gameService.addGame(newGameModel);
			model.addAttribute("message", "New game " + newGameModel.getId() + " added.");
		}

		return "forward:/listGames";
	}

	@GetMapping("/editGame")
	public String showChangeGameForm(Model model, @RequestParam int id) {

		GameModel game = gameService.getGameByID(id);

		if (game != null) {
			model.addAttribute("game", game);
			return "editGame";
		} else {
			model.addAttribute("errorMessage", "Couldn't find game " + id);
			return "forward:/listGames";
		}
	}

	@PostMapping("/editGame")
	public String editGame(@Valid GameModel changedGameModel, BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			String errorMessage = "";
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				errorMessage += fieldError.getField() + " is invalid: " + fieldError.getCode() + "<br>";
			}
			model.addAttribute("errorMessage", errorMessage);
			return "forward:/listGames";
		}

		// Get the game we want to change
		GameModel game = gameService.getGameByID(changedGameModel.getId());

		if (game == null) {
			model.addAttribute("errorMessage", "Game does not exist!<br>");
		} else {
			// Change the attributes
			game.setId(changedGameModel.getId());
			game.setName(changedGameModel.getName());
			game.setDeveloper(changedGameModel.getDeveloper());
			game.setReleaseDate(changedGameModel.getReleaseDate());

			// Save a message for the web page
			model.addAttribute("message", "Changed game " + changedGameModel.getId());
		}

		return "forward:/listGames";
	}

	@GetMapping("/deleteGame")
	public String delete(Model model, @RequestParam int id) {
		boolean isRemoved = gameService.remove(id);

		if (isRemoved) {
			model.addAttribute("warningMessage", "Game " + id + " deleted");
		} else {
			model.addAttribute("errorMessage", "There is no Game " + id);
		}

		return showAllGames(model);
	}

	@PostMapping("/searchGames")
	public String search(Model model, @RequestParam String searchString) {
		model.addAttribute("games", gameService.getFilteredGames(searchString));
		return "listGames";
	}

	@ExceptionHandler(Exception.class)
	public String handleAllException(Exception ex) {
		return "error";
	}
}
