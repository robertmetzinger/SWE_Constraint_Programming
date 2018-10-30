package example;

import org.jacop.core.*;
import org.jacop.constraints.*;
import org.jacop.search.*;

public class Sudoku {

    int size = 9;
    int blocksize = 3;

    int[][] values;

    IntVar[][] variables;

    public Store model() {

        Store store = new Store();

        //Variables
        variables = new IntVar[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int value = values[i][j];
                if (value != 0) {
                    variables[i][j] = new IntVar(store, "v" + i + j, value, value);
                } else
                    variables[i][j] = new IntVar(store, "v" + i + j, 1, size);
            }
        }
        printSudoku(variables);
        //Constraints
        //Rows
        int t = 0;
        for (IntVar[] row : variables) {
            //System.out.print("Row" + t + ": ");
            //printVarArray(row);
            store.impose(new Alldistinct(row));
            t++;
        }
        //Columns
        IntVar[] column;
        for (int j = 0; j < size; j++) {
            column = new IntVar[size];
            for (int i = 0; i < size; i++) {
                column[i] = variables[i][j];
            }
            //System.out.print("Column" + j + ": ");
            //printVarArray(column);
            store.impose(new Alldistinct(column));
        }
        //Blocks
        IntVar[] block;
        int b;
        for (int i = 0; i < size; i = i + blocksize) {
            for (int j = 0; j < size; j = j + blocksize) {
                block = new IntVar[size];
                b = 0;
                for (int ii = i; ii < i + blocksize; ii++) {
                    for (int jj = j; jj < j + blocksize; jj++) {
                        block[b] = variables[ii][jj];
                        b++;
                    }
                }
                //System.out.print("Block" + i + j + ": ");
                //printVarArray(block);
                store.impose(new Alldistinct(block));
            }
        }

        return store;
    }

    public IntVar[] flatArray(IntVar[][] matrix) {
        int length = matrix.length;
        int width = matrix[0].length;
        IntVar[] flat = new IntVar[length * width];
        int index = 0;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                flat[index] = matrix[i][j];
                index++;
            }
        }
        return flat;
    }

    public void printVarArray(IntVar[] array) {
        System.out.print("|");
        for (IntVar v : array) {
            if (v.min() == v.max()) System.out.print(v.min());
            else System.out.print(0);
            System.out.print("|");
        }
        System.out.println();
    }

    public void printSudoku(IntVar[][] sudoku) {
        System.out.println();
        for (IntVar[] array : sudoku) {
            printVarArray(array);
        }
    }

    public static void main(String[] args) {
/*
        int[][] values = {
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
*/

        int[][] values = {
                {8, 1, 0, 0, 0, 5, 0, 6, 7},
                {0, 0, 0, 2, 0, 1, 0, 5, 0},
                {5, 2, 0, 3, 0, 0, 0, 0, 0},
                {0, 9, 0, 0, 0, 0, 0, 0, 0},
                {3, 0, 4, 0, 0, 0, 1, 0, 9},
                {2, 0, 1, 7, 0, 9, 0, 0, 0},
                {0, 4, 7, 9, 8, 6, 0, 0, 5},
                {9, 0, 2, 0, 0, 0, 0, 1, 8},
                {0, 0, 0, 0, 0, 3, 0, 9, 0}
        };
        Sudoku sudoku = new Sudoku();
        sudoku.values = values;
        Store store = sudoku.model();

        Search<IntVar> search = new DepthFirstSearch<IntVar>();
        SelectChoicePoint<IntVar> select =
                new InputOrderSelect<IntVar>(store, sudoku.flatArray(sudoku.variables),
                        new IndomainMin<IntVar>());
        boolean result = search.labeling(store, select);
        if (result) sudoku.printSudoku(sudoku.variables);
        else System.out.println("*** No");
    }
}
