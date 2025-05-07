
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.util.Collections;

public class Prog4test {
    
    public static void main(String[] args) {

	String filename = args[0];
	int n = 0, e = 0, m = 0;
        float [][] prob = new float[1][1]; 

        try (Scanner fin = new Scanner(new File(filename))) {

		n = fin.nextInt();
		e = fin.nextInt();
		m = fin.nextInt();
	        prob = new float[n][m];
		for (int i = 0; i < n; i++)
		{
		    for (int j = 0; j < m; j++)
		      prob[i][j] = fin.nextFloat();
		}


	} catch (FileNotFoundException ex) {
		System.out.println("File not found");
	}

        ArrayList<Integer> result = new ArrayList<Integer>();
        float res = Assign.Assign(n, e, m, prob, result);
	System.out.print(filename + "  Base : Energy assignment : ");
        for (int i = 0; i < result.size(); i++) {
	 	System.out.print(result.get(i) + " ");
	}
	System.out.println("   Base prob : " + res);
		
        ArrayList<Integer> result2 = new ArrayList<Integer>();
        float res2 = Assign.Assign2(n, e, m, prob, result2);
	System.out.print(filename + "  Bonus : Energy assignment : ");
        for (int i = 0; i < result2.size(); i++) {
	 	System.out.print(result2.get(i) + " ");
	}
	System.out.println("   Bonus prob : " + res2);
     }

}


