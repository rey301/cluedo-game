/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cluedo_board_game;
//Hey

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javafx.scene.text.Text;

/**
 * Represents board of the game
 *
 * @author Anilz
 */
public class Board implements BoardInterface {

    //These should not be on board 
    private Random idGenerator; //used to generate unique player Ids
    private ArrayList<Player> playerList; //map of players and their Ids
    private CardDistributor cardDistributor; //card distributor for board
    //To measure steps not surpassing value of dice
    // private int counter = 0;

    Tile[][] tileMap;   //Map of Tiles
    private int columns, rows;  // Parameters
    private ArrayList<Room> rooms = new ArrayList<Room>(); // Rooms are stored in
    private ArrayList<Weapon> weapons = new ArrayList<Weapon>(); //Weapons are stored in 
    //represents the player whos turn it currently is
    private Player currentPlayer;
    private int counter = 0;
    private String alertMsg = "";

    //Sets Up the Board of Tiles
    public Board(int columns, int rows) {
        // fields determining columns and rows of map
        this.columns = columns;
        this.rows = rows;
        // initiate tileMap 
        tileMap = new Tile[columns][rows];
        for (int _row = 0; _row < rows; _row++) {
            for (int _column = 0; _column < columns; _column++) {
                //Created TileMap of Tiles,and indexes are provided for each tile created
                tileMap[_column][_row] = new Tile(_column, _row);
            }
        }

        playerList = new ArrayList<>();
        idGenerator = new Random();
    }

    /**
     *
     * @return board
     */
    @Override
    public Tile[][] getTileMap() {
        return tileMap;
    }

    /**
     *
     * @return column size of the board
     */
    @Override
    public int getColumns() {
        return columns;
    }

    /**
     *
     * @return row size of the board
     */
    @Override
    public int getRows() {
        return rows;
    }

    /**
     * returns to list of weapons
     *
     * @return weapons
     */
    public ArrayList<Weapon> getWeapons() {
        return weapons;
    }

    /**
     * Initialise a new weapon and add to the list Then returns to the
     * Initialised weapon
     *
     * @param weapon
     * @return
     */
    public Weapon initialiseWeapon(String weapon) {
        weapons.add(new Weapon(weapon));
        return weapons.get(weapons.size() - 1);
    }

    /**
     * Places selected weapon into selected room
     *
     * @param room
     * @param weapon
     */
    public void placeWeaponToRoom(Room room, Weapon weapon) {
        //If weapon is already in any room, remove from that room
        for (Room potentialRoom : rooms) {
            if (potentialRoom.getRoomWeapons().contains(weapon)) {
                potentialRoom.getRoomWeapons().remove(weapon);
            }
        }
        //Add to new Room 
        room.addRoomWeapon(weapon);
    }

    /**
     * Returns list of rooms
     *
     * @return rooms
     */
    @Override
    public ArrayList<Room> getRooms() {
        return rooms;
    }

    /**
     * Initialise a new Room taking parameters of
     *
     * @param name
     * @param roomSpace
     * @param roomDoors
     * @return newRoom
     */
    @Override
    public Room initialiseRoom(String name, ArrayList<Tile> roomSpace, ArrayList<Tile> roomDoors) {
        try {
            //Creates Room
            Room newRoom = new Room(name, roomSpace);
            rooms.add(newRoom);
            int roomIndex = rooms.indexOf(newRoom);
            Room newAddedRoom = rooms.get(roomIndex);
            //Surrounds Room with Walls
            for (int _row = 0; _row < rows; _row++) {
                for (int _column = 0; _column < columns; _column++) {
                    //If the tile is room
                    if (newAddedRoom.checkTileInRoom(tileMap[_column][_row])) {
                        //Sets up wall if surrounding tiles are not rooms
                        if (!newAddedRoom.checkTileInRoom(tileMap[_column + 1][_row])) {
                            tileMap[_column + 1][_row].setWall(true);
                        }
                        if (!newAddedRoom.checkTileInRoom(tileMap[_column][_row + 1])) {
                            tileMap[_column][_row + 1].setWall(true);
                        }
                        if (!newAddedRoom.checkTileInRoom(tileMap[_column][_row - 1])) {
                            tileMap[_column][_row - 1].setWall(true);
                        }
                        if (!newAddedRoom.checkTileInRoom(tileMap[_column - 1][_row])) {
                            tileMap[_column - 1][_row].setWall(true);
                        }
                        if (!newAddedRoom.checkTileInRoom(tileMap[_column + 1][_row + 1])) {
                            tileMap[_column + 1][_row + 1].setWall(true);
                        }
                        if (!newAddedRoom.checkTileInRoom(tileMap[_column - 1][_row + 1])) {
                            tileMap[_column - 1][_row + 1].setWall(true);
                        }
                        if (!newAddedRoom.checkTileInRoom(tileMap[_column + 1][_row - 1])) {
                            tileMap[_column + 1][_row - 1].setWall(true);
                        }
                        if (!newAddedRoom.checkTileInRoom(tileMap[_column - 1][_row - 1])) {
                            tileMap[_column - 1][_row - 1].setWall(true);
                        }
                    }
                }
            }
            //Creates Door for the room, if only by replacing one of the surrounding wall tile with door Tile
            //set tile makes the tile no longer wall but door 
            for (Tile roomDoor : roomDoors) {
                rooms.get(rooms.size() - 1).addRoomDoor(roomDoor);
            }
            return newRoom;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("You can't initialise here ");
            return null;
        }
    }

    /**
     * Initialise token for each playing and non-playing players of board
     *
     * @param player
     * @param tokenName
     */
    public void initialisePlayerToken(Player player, String tokenName) {
        try {
            Token token = new Token(tokenName);
            //Sets tokens location on board
            //token.setTokenLocation(getTileMap()[x][y]);
            //Adds the token last
            player.setToken(token);
        } catch (Exception e) {
            System.out.println("Cannot initialise");

        }
    }

    /**
     * @return textual representation of board
     */
    @Override
    public String toString() {
        String s = "";
        for (int _h = 0; _h < rows; _h++) {
            for (int _w = 0; _w < columns; _w++) {
                if ((rooms != null) && !tileMap[_w][_h].IsOccupied()) {
                    //iterate through each room
                    for (Room room : rooms) {
                        if (room.checkTileInRoom(tileMap[_w][_h])) {
                            s += "R";
                        }
                    }
                    s += "R";
                } else if (tileMap[_w][_h].IsOccupied()) {
                    s += "O";
                } else {
                    s += "X";
                }
            }
            s += "\n";
        }
        return s;
    }

    /**
     * Instantiates the cardDistributor for the board
     *
     * @param cardList a list of card Objects used to instantiate the
     * cardDistributor
     */
    @Override
    public void setCardDistributor(List<Card> cardList) {
        cardDistributor = new CardDistributor(cardList);
    }

    /**
     * Instantiates Player objects and adds them to the player map of the board
     * game
     *
     * @param playerNames a list of player name Strings to be made into player
     * Objects and added to the player Map
     */
    @Override
    public void addPlayers(List<String> playerNames, List<Character> playerTypes) {
        boolean playerAdded; //represents if a player is added successfully
        int potentialId; //stores potential ids for players
        Set<Integer> playerIds = new HashSet<>();
        for (String s : playerNames) { //iterate through a list of player names and generate players for each name
            playerAdded = false;
            while (!playerAdded) { //generate new player ID until a unique one is found so player can be added
                potentialId = idGenerator.nextInt(1000);//ids can be in range 0,999
                if (!playerIds.contains(potentialId)) { //if id is unique add player to playerList
                    if (playerTypes.get(0) == 'a') {
                        playerList.add(new Agent(potentialId, s));
                        playerTypes.remove(0);
                    } else if (playerTypes.get(0) == 'h') {
                        playerList.add(new Human(potentialId, s));
                        playerTypes.remove(0);
                    }
                    playerIds.add(potentialId);
                    playerAdded = true;
                }
            }
        }
    }

    @Override
    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    /**
     * moves the current player to coordinates x y as long as dice is rolled and
     * counter is less that dice total, increments counter if move is made, max
     * out counter if player enters room
     *
     * @param x
     * @param y
     * @param diceRolled
     * @param diceTotal
     */
    @Override
    public void moveCurrentPlayer(int x, int y, boolean diceRolled, int diceTotal) {
        Tile currentPlayerPos = getCurrentPlayer().getToken().getTokenLocation();
        if (diceRolled) {
            if ((counter < diceTotal)) {
                alertMsg = getCurrentPlayer().getName() + " Moves To " + getCurrentPlayer().getToken().getTokenLocation().getColIndex() + "," + getCurrentPlayer().getToken().getTokenLocation().getRowIndex();
                movePlayer(currentPlayer, x, y);
                System.out.println(getCurrentPlayer().getToken().getTokenLocation().getColIndex() + "," + getCurrentPlayer().getToken().getTokenLocation().getRowIndex());
                if (!currentPlayerPos.equals(getCurrentPlayer().getToken().getTokenLocation())) {
                    counter++;
                }
                if ((getRoomOfPlayer(getCurrentPlayer())) != null) {
                    alertMsg = getCurrentPlayer().getName() + " Is In " + getRoomOfPlayer(getCurrentPlayer()).getRoomName();
                    counter = diceTotal;
                }
            } else {
                alertMsg = "Please End Turn";
            }
        } else {
            alertMsg = "Please Roll the Dice";
        }
    }

    /**
     * orders the player list based on the name of their character token in the
     * order: "Miss Scarlett","Colonel
     * Mustard","Mrs.White","Mr.Green","Mrs.Peacock","Professor Plum"
     */
    @Override
    public void orderPlayerList() {
        ArrayList<Player> tempList = new ArrayList<>();
        //creates array of character names in appropriate order
        String[] playerNames = {"Miss Scarlett", "Colonel Mustard", "Mrs.White", "Mr.Green", "Mrs.Peacock", "Professor Plum"};
        //gets each player in the correct order using playerNames and stores them in list
        for (String name : playerNames) {
            if (getPlayerByCharacterName(name) != null) {
                tempList.add(getPlayerByCharacterName(name));
            } //if any player is missing alert console
            else {
                //in actual game this should never happen
                System.out.println("missing character " + name);
            }
        }
        //reset Player list then set it to tempList
        playerList = new ArrayList<>();
        playerList = tempList;
    }

    @Override
    public void initialiseWeapons() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * increments the current Player to the next playing player
     */
    @Override
    public void incrementCurrentPlayer() {
        currentPlayer = getNextActivePlayer(currentPlayer);
    }

    /**
     * returns the next playing player from a given player p
     *
     * @param p
     * @return the next playing player
     */
    public Player getNextActivePlayer(Player p) {
        if (playerList.contains(p)) {
            int i = 0;
            do {
                if (playerList.indexOf(p) == playerList.size() - 1) {
                    p = playerList.get(0);
                } else {
                    p = playerList.get(playerList.indexOf(p) + 1);
                }
                i++;
            } while (!p.getIsPlaying() && i < playerList.size());
            if (p.getIsPlaying()) {
                return p;
            } else {
                System.out.println("No remaining active players");
                return null;
            }
        } else {
            System.out.println("Player is not in PlayerList");
            return null;
        }
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * moves the current player to a random space in a given room
     *
     * @param room
     */
    public void currentPlayerEntersRoom(Room room) {
        //move player to a random free tile in the room
        int i = (int) (Math.random() * room.getFreeSpace().size());
        int newX = room.getFreeSpace().get(i).getColIndex();
        int newY = room.getFreeSpace().get(i).getRowIndex();
        for (int j = 0; j < room.getRoomDoors().size(); j++) {
            room.getRoomDoors().get(j).setText("" + (j + 1));
        }
        movePlayer(currentPlayer, newX, newY);
    }

    /**
     * summons and moves player token into Room
     *
     * @param player
     * @param room
     */
    public void callPlayerToRoom(Player player, Room room) {
        //move player to a random free tile in the room
        int i = (int) (Math.random() * room.getFreeSpace().size());
        int newX = room.getFreeSpace().get(i).getColIndex();
        int newY = room.getFreeSpace().get(i).getRowIndex();
        for (int j = 0; j < room.getRoomDoors().size(); j++) {
            room.getRoomDoors().get(j).setText("" + (j + 1));
        }
        movePlayer(player, newX, newY);
    }

    /**
     * move a specified player p to a specified set of coordinates x and y only
     * allows valid moves and handles player enter/exit room
     *
     * @param p
     * @param x
     * @param y
     */
    public void movePlayer(Player p, int x, int y) {
        try {
            Room playerRoom = getRoomOfPlayer(p);
            if (playerRoom != null) {
                //Spawn at one of the doors
                Random random = new Random();
                int doorToExit = random.nextInt(playerRoom.getRoomDoors().size());
                //Gets the coordinates of doors 
                int newX = playerRoom.getRoomDoors().get(doorToExit).getColIndex();
                int newY = playerRoom.getRoomDoors().get(doorToExit).getRowIndex();
                //Place the player at the door
                p.getToken().getTokenLocation().setOccupied(false);
                if (!getDoorExit(getTileMap()[newX][newY]).IsOccupied()) {
                    for (Tile t : playerRoom.getRoomDoors()) {
                        t.setText("");
                    }
                    p.moveToken(getDoorExit(getTileMap()[newX][newY]));
                }
            } //If the tile to be moved is not Wall or occupied,confirm movement
            else if (!getTileMap()[x][y].getIsWall() && !getTileMap()[x][y].IsOccupied()) {
                // Loops through all rooms
                if (getTileMap()[x][y].getIsDoor()) {
                    //Iterate through rooms
                    for (Room room : getRooms()) {
                        //If the board tile to be moved is door then of specified room
                        if (room.getRoomDoors().contains(getTileMap()[x][y])) {
                            //And if the player has not in room yet,then move to room
                            if (!room.getRoomSpace().contains(p.getToken().getTokenLocation())) {
                                //Prints which room of entry
                                System.out.println("You are entering the " + room.getRoomName());
                                //Hit the door And spawn player at random location in room
                                currentPlayerEntersRoom(room);
                                //Ends token movements
                            } else {
                                //If is in the player is in the room and about to move to door,let player move
                                //If it is already in room, then do not spawn in room
                                p.getToken().getTokenLocation().setOccupied(false);
                                p.moveToken(getTileMap()[x][y]);
                            }
                        }
                    }
                } else {
                    //if neither wall nor door, make just a movement
                    p.getToken().getTokenLocation().setOccupied(false);
                    p.moveToken(getTileMap()[x][y]);
                }
            } else {
                //if tile to be moved is wall , then cannot move 
                System.out.println("You cannot go through Wall or Occupied Tile");
                alertMsg = "You cannot go through walls or occupied tiles!";
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("You cant go here");
            alertMsg = "Invalid Move!";
        }
    }

    /**
     * returns the current room of player p if they are in a room, null
     * otherwise
     *
     * @param p
     * @return null or player p's room
     */
    public Room getRoomOfPlayer(Player p) {
        Room room = null;
        boolean roomFound = false; //whether a room that contains the player is found
        int i = 0;
        while (!roomFound && i < getRooms().size()) {
            room = getRooms().get(i);
            if (room.checkTileInRoom(currentPlayer.getToken().getTokenLocation())) {
                roomFound = true;
            } else {
                room = null;
            }
            i++;
        }
        return room;
    }

    /**
     * returns a player p given a character name cName or null if the player
     * doesn't exist
     *
     * @param cName
     * @return null or Player p
     */
    public Player getPlayerByCharacterName(String cName) {
        Boolean found = false;
        int i = 0;
        Player p = null;
        while (!found && i < playerList.size()) {
            if (playerList.get(i).getToken().getName().equals(cName)) {
                p = playerList.get(i);
                found = true;
            }
            i++;
        }
        return p;
    }

    /**
     * utilises the card distributor object to distribute cards to each playing
     * player fairly, current build uses a predefined list of card names
     */
    public void distributeCards() {
        //use predefined list of card names for now
        Card[] pCards = {new Card(CardType.Person, "Miss Scarlett"), new Card(CardType.Person, "Colonel Mustard"), new Card(CardType.Person, "Mrs.White"),
            new Card(CardType.Person, "Mr.Green"), new Card(CardType.Person, "Mrs.Peacock"), new Card(CardType.Person, "Professor Plum")};
        Card[] wCards = {new Card(CardType.Weapon, "Dagger"), new Card(CardType.Weapon, "Candlestick"), new Card(CardType.Weapon, "Revolver"),
            new Card(CardType.Weapon, "Rope"), new Card(CardType.Weapon, "Leadpiping"), new Card(CardType.Weapon, "Spanner")};
        Card[] rCards = {new Card(CardType.Room, "Bathroom"), new Card(CardType.Room, "Diningroom"), new Card(CardType.Room, "Kitchen"),
            new Card(CardType.Room, "Ballroom"), new Card(CardType.Room, "Conservatory"), new Card(CardType.Room, "Gamesroom"),
            new Card(CardType.Room, "Library"), new Card(CardType.Room, "Hallway"), new Card(CardType.Room, "Office")};
        ArrayList<Card> cList = new ArrayList<>();
        cList.addAll(Arrays.asList(pCards));
        cList.addAll(Arrays.asList(wCards));
        cList.addAll(Arrays.asList(rCards));
        setCardDistributor(cList);
        cardDistributor.setEnvelope();
        cardDistributor.shuffleCards();
        cardDistributor.dealCards(playerList);

    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public String getAlertMsg() {
        return alertMsg;
    }

    public Tile getDoorExit(Tile door) {
        if (!door.getIsDoor()) {
            System.out.println("Tile is not a door");
            return null;
        } else {
            if (getRoomOfTile(tileMap[door.getColIndex() + 1][door.getRowIndex()]) == null && !tileMap[door.getColIndex() + 1][door.getRowIndex()].getIsWall()) {
                return (tileMap[door.getColIndex() + 1][door.getRowIndex()]);
            } else if (getRoomOfTile(tileMap[door.getColIndex() - 1][door.getRowIndex()]) == null && !tileMap[door.getColIndex() - 1][door.getRowIndex()].getIsWall()) {
                return (tileMap[door.getColIndex() - 1][door.getRowIndex()]);
            } else if (getRoomOfTile(tileMap[door.getColIndex()][door.getRowIndex() + 1]) == null && !tileMap[door.getColIndex()][door.getRowIndex() + 1].getIsWall()) {
                return (tileMap[door.getColIndex()][door.getRowIndex() + 1]);
            } else if (getRoomOfTile(tileMap[door.getColIndex()][door.getRowIndex() - 1]) == null && !tileMap[door.getColIndex()][door.getRowIndex() - 1].getIsWall()) {
                return (tileMap[door.getColIndex()][door.getRowIndex() - 1]);
            } else {
                System.out.println("No valid tile to move to");
                return null;
            }
        }
    }

    private Room getRoomOfTile(Tile t) {
        boolean found = false;
        int i = 0;
        Room r = null;
        while (!found && i < rooms.size()) {
            if (rooms.get(i).getRoomSpace().contains(t) || rooms.get(i).getRoomDoors().contains(t)) {
                r = rooms.get(i);
                found = true;
            }
            i++;
        }
        return r;
    }

}
