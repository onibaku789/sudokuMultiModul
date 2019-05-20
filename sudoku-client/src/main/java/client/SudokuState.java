package client;

import com.google.inject.Guice;
import com.google.inject.Injector;
import guice.PersistenceModule;
import model.GameResult;
import model.GameResultDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import solver.Difficulty;
import solver.SudokuGen;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Scanner;


/**
 * Sudoku user interface
 */
public class SudokuState implements Cloneable {
    private static Logger logger = LoggerFactory.getLogger( SudokuState.class );
    ZonedDateTime start, stop;
    boolean wantsToQuit = false;
    public int row , col , number;
    private SudokuGen sudokuGen = new SudokuGen();

    public static void main(String[] args) {
        Injector injector = Guice.createInjector( new PersistenceModule( "game" ) );
        GameResultDao gameDao = injector.getInstance( GameResultDao.class );

        String name;


        SudokuState state = new SudokuState();
        Scanner in = new Scanner( System.in );
        System.out.println( "Type in your name!" );

        name = in.nextLine();
        logger.info( "Inserteted name: {}", name );


        while (true) {
            System.out.println( "What would you want to do?" );
            state.printMenu();
            String menuItem = in.nextLine();
            try {
                switch (menuItem.toLowerCase()) {
                    case "play":

                        state.play();

                        gameDao.persist( state.createGameResult( name ) );
                        break;
                    case "top":
                        System.out.println( state.getTops( gameDao.findBest( 10 ) ) );

                        break;
                    case "exit":
                        System.exit( 0 );
                    default:
                        logger.error( "Invalid option was chosen: {}", menuItem );

                        throw new IllegalArgumentException( "Invalid option" );
                }
            } catch (Exception e) {
                System.out.println( e.getMessage() );
            }
        }


    }

    private Difficulty getDifficulty() {

        return sudokuGen.getDifficulty();
    }


    private void printMenu() {
        System.out.println( "Play: play" );
        System.out.println( "Tops: top" );
        System.out.println( "Exit: exit" );
    }
    private void printDiffSelect(){
        System.out.println( "Select a diffculty!(0-2)" );
        System.out.println( "Easy" );
        System.out.println( "Medium" );
        System.out.println( "Hard" );
    }

    /**
     * Creates a {@code GameResult} instance .
     *
     * @param name the name of the player.
     * @return Returns a {@code GameResult} with the desired name.
     */
    private GameResult createGameResult(String name) {
        logger.info( "Saving to database." );
        return GameResult.builder()
                .player( name )
                .difficulty( getDifficulty() )
                .solved( sudokuGen.isEnd() )
                .created( ZonedDateTime.now() )
                .duration( Duration.between( start, stop ) )
                .build();
    }

    /**
     * Build a string about the top ten games difficulty ,duration the game lasted and the player name
     *
     * @param best contains the top ten games
     * @return returns a string with the top ten games attributes
     */
    private String getTops(List<GameResult> best) {
        logger.info( "Top 10 players" );
        StringBuilder sb = new StringBuilder();
        sb.append( "Top 10 players \n" );
        sb.append( "Difficulty\tDuration\tName \n" );
        if (best.isEmpty()) {
            sb.append( "Empty :(" );
        } else {
            for (var element : best) {
                sb.append( " \t" + element.getDifficulty() + "\t\t" + element.getDuration().getSeconds() + "s \t\t" + element.getPlayer() + "\n" );
            }

        }
        return sb.toString();
    }

    private void play() {
        logger.info( "START GAME" );


        String menuItem;

        start = ZonedDateTime.now();
        Scanner in = new Scanner( System.in );

        printDiffSelect();
        while (true) {
            menuItem = in.nextLine();

            if (menuItem.equals( "0" )) {
                sudokuGen.initBoard( Difficulty.EASY );
                break;
            } else if (menuItem.equals( "1" )) {
                sudokuGen.initBoard( Difficulty.MEDIUM );
                break;
            } else if (menuItem.equals( "2" )) {
                sudokuGen.initBoard( Difficulty.HARD );
                break;
            } else {
                logger.error( "Invalid difficulty: {}", menuItem );
                System.out.println("Invalid input, try again.");
             printDiffSelect();
            }

        }

        sudokuGen.initBoard( Difficulty.TEST3 );


        while (!sudokuGen.isEnd()) {
            System.out.println( sudokuGen );
            System.out.println( "Enter the row, column and the number you want to add " );


            try {
                parseInput( in.nextLine() );
            } catch (Exception e) {
                System.out.println( e.getMessage() );
            }
            if(wantsToQuit)
                break;

            try {

                sudokuGen.writeToSudokuGrid( row - 1, col - 1, number );
            } catch (Exception e) {
                logger.error( "Cannot write to SudokuGrid. Exception: {}", e.getMessage() );
                System.out.println( e.getMessage() );

            }

        }
        stop = ZonedDateTime.now();
        if (sudokuGen.isEnd() && !wantsToQuit) {
            System.out.println( "You won" );
        }


    }

    /**
     * Check if the {@code object} param is a {@code Integer}
     *
     * @param object the user input
     * @return returns {@code true} if {@code object} is Integer,returns {@code false} {@code object} is not Integer
     */
    private static boolean isInteger(Object object) {
        if (object instanceof Integer) {
            return true;

        } else {
            String string = object.toString();
            try {
                Integer.parseInt( string );
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    private void parseInput(String input) {
        if(input.contains( "quit" )){
            wantsToQuit = true;
        }else {

            String[] inputArray = input.split( "\\s+" );


            if (inputArray.length == 3) {
                this.row = Integer.parseInt( inputArray[0] );
                this.col = Integer.parseInt( inputArray[1] );
                this.number = Integer.parseInt( inputArray[2] );
            } else {
                throw new IllegalArgumentException( "Invalid input" );
            }
        }

    }




}
