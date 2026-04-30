package game.engine.exceptions;

//import java.io.*;

abstract public class GameActionException extends Exception {
	
	public GameActionException(){
	}
	
	public GameActionException(String message){
		super(message);
	}

}
