/**
 * Store label and data together
 * 
 * Sachin Shah
 * version 1
 */

public class TrainingPoint
{
	public int label;
	public Vector point;

	public TrainingPoint(int l, Vector pt)
	{
		this.label = l;
		this.point = pt;
	}

	public String toString()
	{
		return String.format("<%d, %s>", label, point.toString());
	}
}
