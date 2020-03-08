/** 
 * Sachin Shah
 * 25 November 2017
 */

import java.util.Scanner;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Perceptron
{
	private int counter;
	private int modelCount;

	private String directory = "models";

	public static void main(String[] args)
	{
		Perceptron p = new Perceptron();
		p.run();
	}
	
	public void run()
	{
		System.out.println("Welcome to Vision Training");
		PrintWriter p;
		Scanner input = new Scanner(System.in);

		System.out.printf("[1] %s Labeled%n[2] %s Labeled%n[3] Neither Labled%n", Constants.label1, Constants.label2);
		int choice = input.nextInt();
		
		Reader r = new Reader();
		r.changeFolder("dataTraining");
		TrainingPoint[] data = null;

		if (choice == 1)
		{
			p = createModelOutput(Constants.label1);

			data = r.labeledOne();
		}
		else if (choice == 2)
		{
			p = createModelOutput(Constants.label2);

			data = r.labeledTwo();
		}
		else if (choice == 3)
		{
			p = createModelOutput("neither");

			data = r.labeledNeither();
		}
		else
		{
			System.out.println("Invalid choice");
			return;
		}
		
		p.println();
		p.println("Files: ");

		ArrayList<String> names = r.fileNames();
		for (String s : names) p.printf("\t%s%n", s);
				
		System.out.println("How many generations?");
		int gens = input.nextInt();

		p.printf("Total number of generations: %d%n", gens);
		p.printf("Total pixel count: %d%n", data.length);

		train(data, gens, p);

		p.close();
	}
	
	private PrintWriter createModelOutput(String s)
	{
		PrintWriter p = null;

		try
		{
			p = new PrintWriter(new File(modelFileName(s)));
			p.printf("Training %s model%n", s);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		return p;
	}

	private PrintWriter createPrinter(String s)
	{
		PrintWriter p = null;

		try
		{
			p = new PrintWriter(new File(directory + s + ".txt"));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		return p;
	}

	private String modelFileName(String label)
	{
		return String.format(directory+"%sModel.txt", label);
	}

	public Vector train(TrainingPoint[] data, int maxGens, PrintWriter p)
	{
		Vector model = new Vector(0, 0, 0);
		Vector old = new Vector (0, 0, 0);

		RandomSort random = new RandomSort();
		
		PrintWriter wrongPrinter = createPrinter("FailCount");
		PrintWriter modelPrinter = createPrinter("Models"); 
		
		boolean passRate100 = false;
		int generation = 0;

		int failed = 0;

		int oldFailed = data.length;
		
		while (!passRate100 && generation < maxGens)
		{
			passRate100 = true;
			failed = 0;

			data = random.random(data);

			for (int i = 1; i < data.length; i++)
			{
				if (data[i].label * (model.dot(data[i].point) + model.d) <= 0)
				{
					model = perceptron(model, data[i]);
				}
			}

			for (int i = 1; i < data.length; i++)
			{
				if (data[i].label * (model.dot(data[i].point) + model.d) <= 0)
				{
					passRate100 = false;
					failed++;
				}
			}

			if (oldFailed > failed)
			{
				p.printf("*** Gen Num: %d %s%n", generation, model.toString());
				oldFailed = failed;
				old.set(model);
			}
			else
			{
				p.printf("Gen Num: %d %s%n", generation, model.toString());
				model.set(old);
			}

			modelPrinter.println(model.toString());
			wrongPrinter.println(failed);

			generation++;
		}

		p.printf("%nFINAL MODEL: %s%n", old.toString());
		p.printf("Success rate: %f", successRate(data.length, failed));
		
		modelPrinter.close();
		wrongPrinter.close();

		return model;
	}

	private double successRate(int total, int failed)
	{
		int correct = total-failed;
		double percent = 100*correct/total;
		return percent;
	}
	
	private Vector perceptron(Vector model, TrainingPoint data)
	{
		model.add(data.point.times(data.label));
		model.d += data.label;
		return model;
	}
}
