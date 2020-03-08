/** 
 * Sachin Shah
 * version 1
 */

import java.awt.image.BufferedImage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.util.ArrayList;

public class Reader
{
	public File dir;

	// array of supported extensions
	public String[] EXTENSIONS = new String[]{
		"gif", "png", "bmp", "jpeg", "jpg"
	};
	
	public FilenameFilter IMAGE_FILTER = new FilenameFilter() {

		@Override
		public boolean accept(File dir, String name)
		{
			for (String ext : EXTENSIONS)
			{
				if (name.endsWith("." + ext))
				{
					return true;
				}
			}
			return false;
		}
	};
	
	// Create reader with default folder
	public Reader()
	{
		this.dir = new File("dataDense");
	}

	public Reader(String folder)
	{
		this.dir = new File(folder);
	}
	
	public void changeFolder(String name)
	{
		File temp = new File(name);
		if (temp.isDirectory())
		{
			dir = temp;
		}
		else
		{
			System.err.println(name + " is not a directory.");
		}
	}
	
	// Get all image files in directory
	public ArrayList<String> fileNames()
	{
		ArrayList<String> list = new ArrayList<String>();

		if (dir.isDirectory())
		{
			for (File f : dir.listFiles(IMAGE_FILTER))
			{
				list.add(f.getName());
			}
		}

		return list;
	}
	
	// Load image files as BufferedImages
	public ArrayList<BufferedImage> loadBufferedImages()
	{
		System.out.println("Loading Data...");
		ArrayList<BufferedImage> list = new ArrayList<BufferedImage>();

		if (dir.isDirectory())
		{
			for (File f : dir.listFiles(IMAGE_FILTER))
			{
				try
				{
					BufferedImage img = ImageIO.read(f);
					list.add(img);

					System.out.println("Loaded File: " + f.getName());
					
					System.out.println("  width : " + img.getWidth());
					System.out.println("  height: " + img.getHeight());					
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}

		return list;
	}
	
	// Convert an image into a 2D array of RGB
	public int[][] imgToArray(BufferedImage img)
	{
		int c, r, g, b;
		int place = 0;

		int[][] output = new int[3][img.getHeight() * img.getWidth()];

		for (int y = 0; y < img.getHeight(); y++)
		{
			for (int x = 0; x < img.getWidth(); x++)
			{
				c = img.getRGB(x, y);
				r = getRed(c);
				g = getGreen(c);
				b = getBlue(c);

				output[0][place] = r;
				output[1][place] = g;
				output[2][place] = b;
				place++;
			}
		}

		return output;
	}

	private TrainingPoint[] labelData(int one, int two, int neither)
	{
		System.out.printf("Labeling data with: <%d, %d, %d>%n", one, two, neither);

		ArrayList<BufferedImage> imgs = loadBufferedImages();
		ArrayList<String> names = fileNames();
		TrainingPoint[] output = new TrainingPoint[imgsLength(imgs)];

		int counter = 0;
		int place = 0;
		int l;

		for (BufferedImage s : imgs)
		{
			int[][] rgb = imgToArray(s);

			if (names.get(counter).toLowerCase().contains(Constants.label1))
			{
				l = one;
			}
			else if (names.get(counter).toLowerCase().contains(Constants.label2))
			{
				l = two;
			}
			else
			{
				l = neither;
			}

			for (int i = 0; i < rgb[0].length; i++)
			{
				Vector v = new Vector(rgb[0][i], rgb[1][i], rgb[2][i]);
				TrainingPoint pt = new TrainingPoint(l, v);
				output[place] = pt;
				place++;
			}

			counter++;
		}

		return output;
	}

	public TrainingPoint[] labeledNeither()
	{
		return labelData(1, 1, -1);
	}

	public TrainingPoint[] labeledOne()
	{
		return labelData(1, -1, -1);
	}

	public TrainingPoint[] labeledTwo()
	{
		return labelData(-1, 1, -1);
	}
	
	public int imgsLength(ArrayList<BufferedImage> imgs)
	{
		int length = 0;

		for (BufferedImage img : imgs)
		{
			int size = img.getHeight() * img.getWidth();
			length += size;
		}

		return length;
	}
	
	public int getRed(int colorInt)
	{
		return (colorInt >> 16) & 0xff;
	}

	public int getGreen(int colorInt)
	{
		return (colorInt >>  8) & 0xff;
	}

	public int getBlue(int colorInt)
	{
		return (colorInt	  ) & 0xff;
	}
}
