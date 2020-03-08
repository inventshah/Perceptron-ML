/** 
 * Randomly sort a training point array
 *
 * Sachin Shah
 * version 1
 */

import java.util.Random;

public class RandomSort
{
	private Random random;

	public RandomSort()
	{
		random = new Random();
	}

	public TrainingPoint[] random(TrainingPoint[] array)
	{
		int n = array.length;
		random.nextInt();

		for (int i = 0; i < n; i++)
		{
			int change = i + random.nextInt(n - i);
			array = swap(array, i, change);
		}

		return array;
	}

	private TrainingPoint[] swap(TrainingPoint[] a, int i, int change)
	{
		TrainingPoint helper = a[i];
		a[i] = a[change];
		a[change] = helper;
		return a;
	}
}