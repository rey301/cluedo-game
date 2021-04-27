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
import javafx.util.Pair;

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
     * @return cardDistributor
     */
    public CardDistributor getCardDistributor() {
        return cardDistributor;
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
     * Moves a given weapon into a given room
     *
     * @param room
     * @param weapon
     */
    public void moveWeaponToRoom(Room room, Weapon weapon) {
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
                if ((rooms != null) && !tileMap[_w][_h].isOccupied()) {
                    //iterate through each room
                    for (Room room : rooms) {
                        if (room.checkTileInRoom(tileMap[_w][_h])) {
                            s += "R";
                        }
                    }
                    s += "R";
                } else if (tileMap[_w][_h].isOccupied()) {
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
                if (getRoomOfPlayer(currentPlayer) != null && !currentPlayer.isAgent()) {
                    alertMsg = "Choose door number to exit";
                } else {
                    movePlayer(currentPlayer, x, y);
                    if (!currentPlayerPos.equals(getCurrentPlayer().getToken().getTokenLocation())) {
                        alertMsg = getCurrentPlayer().getName() + " Moves To " + getCurrentPlayer().getToken().getTokenLocation().getColIndex()
                                + "," + getCurrentPlayer().getToken().getTokenLocation().getRowIndex();
                        counter++;
                    }
                    if ((getRoomOfPlayer(getCurrentPlayer())) != null) {
                        alertMsg = getCurrentPlayer().getName() + " Is In " + getRoomOfPlayer(getCurrentPlayer()).getRoomName();
                        counter = diceTotal;
                    }
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
     * moves a given player to a random space in a given room
     *
     * @param player
     * @param room
     */
    public void playerEntersRoom(Player player, Room room) {
        //move player to a random free tile in the room
        int i = (int) (Math.random() * room.getFreeSpace().size());
        int newX = room.getFreeSpace().get(i).getColIndex();
        int newY = room.getFreeSpace().get(i).getRowIndex();
        player.getToken().getTokenLocation().setOccupied(false);
        if (player == currentPlayer) {
            for (int j = 0; j < room.getRoomDoors().size(); j++) {
                room.getRoomDoors().get(j).setText("" + (j + 1));
            }
        }
        player.moveToken(getTileMap()[newX][newY]);
    }

    /**
     * move a specified player p to a specified set of coordinates x and y only
     * allows valid moves and handles agent player enter/exit room
     *
     * @param p
     * @param x
     * @param y
     */
    public void movePlayer(Player p, int x, int y) {
        try {
            Room playerRoom = getRoomOfPlayer(p);
            //if agent player is exiting room get a random door from room doors
            if (playerRoom != null && p.isAgent()) {
                Random random = new Random();
                int doorToExit = random.nextInt(playerRoom.getRoomDoors().size());
                int exitX = playerRoom.getRoomDoors().get(doorToExit).getColIndex();
                int exitY = playerRoom.getRoomDoors().get(doorToExit).getRowIndex();
                //use the coordinates of the door exit to move player
                p.getToken().getTokenLocation().setOccupied(false);
                if (!getDoorExit(getTileMap()[exitX][exitY]).isOccupied()) {
                    //also remove door text
                    for (Tile t : playerRoom.getRoomDoors()) {
                        t.setText("");
                    }
                    p.moveToken(getDoorExit(getTileMap()[exitX][exitY]));
                }
            } //If the tile to be moved is not wall or occupied
            else if (!getTileMap()[x][y].isWall() && !getTileMap()[x][y].isOccupied()) {
                //if the tile is a door
                if (getTileMap()[x][y].isDoor()) {
                    //find the room that the door belongs to
                    for (Room room : getRooms()) {
                        if (room.getRoomDoors().contains(getTileMap()[x][y])) {
                            //allow player to enter room
                            playerEntersRoom(currentPlayer, room);
                        }
                    }
                } else {
                    //if the tile is not a wall or door then just make move
                    p.getToken().getTokenLocation().setOccupied(false);
                    p.moveToken(getTileMap()[x][y]);
                }
            } else {
                //if the tile to be moved is a wall or occupied don't move 
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
        return getRoomOfTile(p.getToken().getTokenLocation());
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
        Card[] rCards = {new Card(CardType.Room, "Lounge"), new Card(CardType.Room, "Diningroom"), new Card(CardType.Room, "Kitchen"),
            new Card(CardType.Room, "Ballroom"), new Card(CardType.Room, "Conservatory"), new Card(CardType.Room, "Billiardroom"),
            new Card(CardType.Room, "Library"), new Card(CardType.Room, "Hall"), new Card(CardType.Room, "Study")};
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
        if (!door.isDoor()) {
            System.out.println("Tile is not a door");
            return null;
        } else {
            if (getRoomOfTile(tileMap[door.getColIndex() + 1][door.getRowIndex()]) == null && !tileMap[door.getColIndex() + 1][door.getRowIndex()].isWall()) {
                return (tileMap[door.getColIndex() + 1][door.getRowIndex()]);
            } else if (getRoomOfTile(tileMap[door.getColIndex() - 1][door.getRowIndex()]) == null && !tileMap[door.getColIndex() - 1][door.getRowIndex()].isWall()) {
                return (tileMap[door.getColIndex() - 1][door.getRowIndex()]);
            } else if (getRoomOfTile(tileMap[door.getColIndex()][door.getRowIndex() + 1]) == null && !tileMap[door.getColIndex()][door.getRowIndex() + 1].isWall()) {
                return (tileMap[door.getColIndex()][door.getRowIndex() + 1]);
            } else if (getRoomOfTile(tileMap[door.getColIndex()][door.getRowIndex() - 1]) == null && !tileMap[door.getColIndex()][door.getRowIndex() - 1].isWall()) {
                return (tileMap[door.getColIndex()][door.getRowIndex() - 1]);
            } else {
                System.out.println("No valid tile to move to");
                return null;
            }
        }
    }

    /**
     * returns the current room of tile t if it is in a room, null
     * otherwise
     * 
     * @param t
     * @return null or room t's room
     */
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

    public void currentPlayerExitsRoom(int i, boolean diceRolled, int diceTotal) {
        Tile currentPlayerPos = getCurrentPlayer().getToken().getTokenLocation();
        if (diceRolled) {
            if ((counter < diceTotal)) {
                Room r = getRoomOfPlayer(currentPlayer);
                if (r != null) {
                    if (i <= r.getRoomDoors().size()) {
                        int x = getDoorExit(r.getRoomDoors().get(i - 1)).getColIndex();
                        int y = getDoorExit(r.getRoomDoors().get(i - 1)).getRowIndex();
                        if (!tileMap[x][y].isOccupied()) {
                            movePlayer(currentPlayer, x, y);
                            alertMsg = getCurrentPlayer().getName() + " Moves To " + getCurrentPlayer().getToken().getTokenLocation().getColIndex()
                                    + "," + getCurrentPlayer().getToken().getTokenLocation().getRowIndex();
                            for (Tile t : r.getRoomDoors()) {
                                t.setText("");
                            }
                            if (!currentPlayerPos.equals(getCurrentPlayer().getToken().getTokenLocation())) {
                                counter++;
                            }
                            if ((getRoomOfPlayer(getCurrentPlayer())) != null) {
                                alertMsg = getCurrentPlayer().getName() + " Is In " + getRoomOfPlayer(getCurrentPlayer()).getRoomName();
                                counter = diceTotal;
                            }
                        } else {
                            alertMsg = "Door is blocked!";
                        }
                    } else {
                        alertMsg = "Operation not currently valid";
                    }
                } else {
                    alertMsg = "Operation not currently valid";
                }
            } else {
                alertMsg = "Please End Turn";
            }
        } else {
            alertMsg = "Please Roll The Dice";
        }
    }

    /**
     * initialises the player detective cards for all playing players,
     * using a predefined list of characters, weapons and rooms,
     * initialises all the check list values as false and case notes values
     * as empty
     */
    public void initialisePlayerDetectiveCards(){
        String[] characterNames = {"Miss Scarlett", "Colonel Mustard", "Mrs.White", "Mr.Green", "Mrs.Peacock", "Professor Plum"};
        String[] weaponNames = {"Dagger", "Candlestick", "Revolver", "Rope", "Leadpiping", "Spanner"};
        String[] roomNames = {"Lounge", "Diningroom", "Kitchen", "Ballroom", "Conservatory", "Billiardroom", "Library", "Hall", "Study"};
        for(Player p: playerList){
            if(p.getIsPlaying()){
                HashMap<String, Boolean> currDetectCard = new HashMap<>();
                String currString = "";
                
                for (String c : characterNames) {
                    currDetectCard.put(c, Boolean.FALSE);
                }
                for (String w : weaponNames) {
                    currDetectCard.put(w, Boolean.FALSE);
                }
                for (String r : roomNames) {
                    currDetectCard.put(r, Boolean.FALSE);
                }
                p.setDetectiveCard(currDetectCard);
                p.setDetectiveNotes(currString);
                
                if(p.isAgent()){
                    //auto update agent players detective card
                    p.markHandAsSeen();
                }
            }
        }
    }

    /**
     * returns a decision for what the current players turn should be, assuming the current player
     * is an agent
     * @return string representing turn decision
     */
    public String getAgentTurn(){
        Room currentPlayerRoom = getRoomOfPlayer(currentPlayer);
        if(currentPlayer.getUnseenCards().size() < 4){
            //make accusation
            return "Accuse";
        }
        else if(currentPlayerRoom == null){
            if(isPlayerTrapped(currentPlayer)){
                //skip turn
                return "Skip";
            }
            else{
                //move token
                return "Move";
            }
        }
        else if(currentPlayerRoom.getRoomName().equals(currentPlayer.getMostRecentlySuggestedRoom())){
            //move token
            return "Move";
        }
        else{
            //make suggestion
            return "Suggest";
        }
    }
    
    /**
     * given a string s return a room with a name equal to s if one exists, else null
     * @param s
     * @return room or null
     */
    public Room getRoomFromName(String s){
        int i = 0;
        boolean found = false;
        Room r = null;
        while(!found && i < rooms.size()){
            if(rooms.get(i).getRoomName().equals(s)){
                r = rooms.get(i);
                found = true;
            }
            i ++;
        }
        return r;
    }

    private boolean isPlayerTrapped(Player p) {
        boolean trapped = true;
        int x = p.getToken().getTokenLocation().getColIndex();
        int y = p.getToken().getTokenLocation().getRowIndex();
        if((x-1) > -1 && (x-1) < 28){
            if(!tileMap[x-1][y].isOccupied() && !tileMap[x-1][y].isWall()){
                trapped = false;
            }
        }
        if((x+1) > -1 && (x+1) < 28){
            if(!tileMap[x+1][y].isOccupied() && !tileMap[x+1][y].isWall()){
                trapped = false;
            }
        }
        if((y-1) > -1 && (y-1) < 28){
            if(!tileMap[x][y-1].isOccupied() && !tileMap[x][y-1].isWall()){
                trapped = false;
            }
        }
        if((y+1) > -1 && (y+1) < 28){
            if(!tileMap[x][y+1].isOccupied() && !tileMap[x][y+1].isWall()){
                trapped = false;
            }
        }
        return trapped;
    }

}
