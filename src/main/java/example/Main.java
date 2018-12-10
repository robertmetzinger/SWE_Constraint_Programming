package example;

import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.search.*;

public class Main {

    static Main m = new Main ();
    public static void main(String[] args) {

        //Modelling

        int[][] values = {
                {0, 0, 0, 0, 0, 5, 0, 6, 7},
                {0, 0, 0, 2, 0, 1, 0, 5, 0},
                {5, 0, 0, 3, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {3, 0, 4, 0, 0, 0, 1, 0, 9},
                {2, 0, 1, 7, 0, 9, 0, 0, 0},
                {0, 4, 7, 9, 8, 6, 0, 0, 5},
                {9, 0, 2, 0, 0, 0, 0, 1, 8},
                {0, 0, 0, 0, 0, 3, 0, 9, 0}
        };
        Sudoku sudoku = new Sudoku();
        sudoku.values = values;
        Store store = sudoku.model();

        //Solving

        Search<IntVar> search = new DepthFirstSearch<IntVar>();
        SelectChoicePoint<IntVar> select =
                new InputOrderSelect<IntVar>(store, sudoku.flatArray(sudoku.variables),
                        new IndomainMin<IntVar>());
        boolean result = search.labeling(store, select);
        if (result) sudoku.printSudoku(sudoku.variables);
        else System.out.println("*** No Solution ***");
    }
}
