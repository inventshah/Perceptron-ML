/** 
 * Sachin Shah
 * version 1
 */

import java.awt.Color;
import java.awt.image.BufferedImage;

import java.io.IOException;
import java.io.File;

import java.util.Scanner;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Condenser
{
	private String folderName = "";

	public static void main(String[] args)
	{
		Condenser n = new Condenser();
		n.run();
	}
	
	public void run()
	{
		System.out.println("Welcome to the Condenser");
		
		Reader reader = new Reader();
		Scanner input = new Scanner(System.in);

		System.out.println("What folder should be condensed?");
		folderName = input.nextLine().trim();
		reader.changeFolder(folderName);

		ArrayList<BufferedImage> listImg = reader.loadBufferedImages();
		ArrayList<String> names = reader.fileNames();

		System.out.println("Finished Importing Data");

		uniqueCount(listImg, names, reader);
		
		System.out.println("Done");
	}
	
	public boolean unique(int r, int g, int b, ArrayList<Integer[]> list)
	{
		for (Integer[] i : list)
		{
			if (i[0] == r && i[1] == g && i[2] == b) return false;
		}

		list.add(array(r, g, b));
		return true;
	}
	
	public void uniqueCount(ArrayList<BufferedImage> listImg, ArrayList<String> names, Reader r)
	{
		int uniqueCount = 0;
		int place = 0;

		ArrayList<Integer[]> pixels = new ArrayList<Integer[]>();

		for (BufferedImage s : listImg)
		{
			int[][] t = r.imgToArray(s);

			System.out.println("ON TO: " + names.get(place));
			for (int i = 0; i < t[0].length; i++){
				if (unique(t[0][i], t[1][i], t[2][i], pixels)) uniqueCount++;
			}
			
			place++;
		}

		System.out.printf("Unique Total: %d%n", uniqueCount);
		System.out.printf("Image Total:  %d%n", listImg.size());
		
		create(pixels, "condensed" + folderName);
	}
	
	public BufferedImage create(ArrayList<Integer[]> list, String name)
	{
		if (list.size() == 0) return null;

		int start_val = (int) Math.ceil(Math.sqrt(list.size()));
		int current_val = start_val;

		int size = list.size();

		int width = start_val;
		int height = start_val;

		int[] nums = new int[3];
		int place = 0;
				
		while (current_val > 20)
		{
			if (size % current_val == 0)
			{
				width = current_val;
				height = size / current_val;
				break;
			}
			else
			{
				current_val--;
			}
		}

		System.out.printf("Dimensions: %dx%d", width, height);

		BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				Color temp = (place < size) ? new Color(
					list.get(place)[0],
					list.get(place)[1],
					list.get(place)[2],
					1) : new Color(0, 0, 0, 1);

				output.setRGB(j, i, temp.getRGB());
				place++;
			}
		}

		save(output, name);
		return output;
	}
	
	public void save(BufferedImage pic, String name)
	{
		try
		{
			ImageIO.write(pic, "png", new File(name + ".png"));
		}
		catch(IOException e)
		{
			System.out.println("Unable to save image");
		}
	}
	
	private Integer[] array(int r, int g, int b)
	{
		return new Integer[] {r, g, b};
	}
}
