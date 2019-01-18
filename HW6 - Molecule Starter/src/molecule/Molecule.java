// COURSE: CSCI1620
// TERM: Fall 2018
//
// NAME: Tyler Labreck and Robin Suda
// RESOURCES: The java doc and bracket code in class was used.

package molecule;
import java.util.Stack;
import molecule.exceptions.InvalidAtomException;
import molecule.exceptions.InvalidSequenceException;

/**
 * Objects of the Molecule class represent a single chemical molecule 
 * madeup of any number of hydrogen, carbon, and oxygen atoms. The 
 * classprovides functionality to compute the atomic weight of the 
 * molecule.
 * @author tlabreck and rsuda
 *
 */
public class Molecule implements java.lang.Comparable<Molecule>, java.lang.Cloneable
{
	/**
	 * Hydrogen atomic weight.
	 */
	private static final int HYDROGEN_WEIGHT = 1;
	
	/**
	 * Carbon atomic weight.
	 */
	private static final int CARBON_WEIGHT = 12;
	
	/**
	 * Oxygen atomic weight.
	 */
	private static final int OXYGEN_WEIGHT = 16;
	
	/**
	 * The molecule sequence.
	 */
	private String moleculeSequence;
	
	/**
	 * The molecule weight.
	 */
	private int moleculeWeight;
	
	/**
	 * Creates a new Molecule made up of the H, C, and O atoms in the 
	 * configuration specified by sequenceIn.
	 * @param sequenceIn The sequence of atoms for this Molecule.
	 * @throws molecule.exceptions.InvalidAtomException - if any non C, H,
	 *  O atom exists in sequenceIn
	 * @throws molecule.exceptions.InvalidSequenceException - if unmatched 
	 * parentheses exist in sequenceIn or incoming sequence is null or empty String.
	 */
	public Molecule(java.lang.String sequenceIn)
	{
		setSequence(sequenceIn);
	}

	/**
	 * Updates the sequence of atoms represented by this Molecule.
	 * @param sequenceIn The new molecular sequence to be used for this Molecule.
	 * @throws molecule.exceptions.InvalidAtomException - if any non C, H,
	 *  O atom exists in sequenceIn
	 * @throws molecule.exceptions.InvalidSequenceException - if unmatched 
	 * parentheses exist in sequenceIn or incoming sequence is null or empty String.
	 */
	public void setSequence(java.lang.String sequenceIn)
	{
		if (!validSequence(sequenceIn) || sequenceIn == null || "".equals(sequenceIn) 
				|| Character.isDigit(sequenceIn.charAt(0)))
		{
			throw new InvalidSequenceException();
		}
		else if (!validAtom(sequenceIn))
		{
			throw new InvalidAtomException(validAtomOffender(sequenceIn));
		}
		else
		{
			calculateWeight(sequenceIn);
			moleculeSequence = sequenceIn;
		}
	}
	
	/**
	 * Calculates the molecules weight.
	 * @param sequenceIn molecule sequence.
	 */
	public void calculateWeight(String sequenceIn)
	{
		if (sequenceIn == null || sequenceIn.equals(""))
		{
			moleculeWeight = 0;
		}
		else
		{
			String toMultiplyBy = "";
			int multiplyBy = 1;
			int sum = 0;
			Stack<Character> sequenceStack = new Stack<Character>();
			for (int i = 0; i < sequenceIn.length(); i++)
			{
				sequenceStack.add(sequenceIn.charAt(i));
			}
			while (!sequenceStack.isEmpty())
			{
				if (Character.isDigit(sequenceStack.peek()))
				{
					toMultiplyBy = sequenceStack.pop() + toMultiplyBy;
					multiplyBy = Integer.parseInt(toMultiplyBy);
				}
				else if (sequenceStack.peek() == ')')
				{
					sequenceStack.pop();
					sum = sum + calculateWeightHelper(sequenceStack, multiplyBy);
					multiplyBy = 1;
					toMultiplyBy = "";
				}
				else 
				{
					int firstRound = atomWeight(sequenceStack.pop()) * multiplyBy;
					sum = sum + firstRound;
					multiplyBy = 1;
				}
			}
			moleculeWeight = sum;
		}
	}
	
	/**
	 * Calculates the weight of the molecule helper.
	 * @param sequenceIn the molecule.
	 * @param intToMultiplyIn the multiple digit.
	 * @return sum of that part.
	 */
	public int calculateWeightHelper(Stack<Character> sequenceIn, int intToMultiplyIn)
	{
		int sum = 0; 
		String toMultiply = "";
		int intToMultiply = 1;
		while (!sequenceIn.isEmpty())
		{
			if (Character.isDigit(sequenceIn.peek()))
			{
				toMultiply = sequenceIn.pop() + toMultiply;
				intToMultiply = Integer.parseInt(toMultiply) * intToMultiplyIn;
			}
			else if (sequenceIn.peek() == ')')
			{
				sequenceIn.pop();
				calculateWeightHelper(sequenceIn, intToMultiplyIn);
			}
			else if (sequenceIn.peek() == '(')
			{
				sequenceIn.pop();
				return sum;
			}
			else 
			{
				int firstRound = atomWeight(sequenceIn.pop()) * intToMultiply; 
				int secondRound = firstRound * intToMultiplyIn;
				sum = sum + secondRound;
				intToMultiply = 1;
			}
		}
		return sum;
	}
	/**
	 * Checks to see if the molecule has valid parenthesis.
	 * @param sequenceIn molecule sequence.
	 * @return returns true if valid, false if not.
	 */
	public boolean validSequence(String sequenceIn)
	{
		Stack<Character> paranthesisCount = new Stack<Character>();
		boolean valid = true;
		
		for (int i = 0; i < sequenceIn.length(); i++)
		{
			char temp = sequenceIn.charAt(i);
		
			switch (temp)
			{
				case '(':
					paranthesisCount.push(temp);
					break;
				case ')':
					if (!paranthesisCount.isEmpty() && paranthesisCount.peek() == '(') 
					{
						paranthesisCount.pop();
					}
					else
					{
						valid = false;
					}
					break;
				default:
			}
		}
		
		if (!paranthesisCount.isEmpty())
		{
			valid = false;
		}
		return valid;
	}
	
	/**
	 * Checks to see if the molecules atoms are valid.
	 * @param sequenceIn molecule sequence.
	 * @return true if valid, false if not.
	 */
	public boolean validAtom(String sequenceIn)
	{
		String tempSequence = sequenceIn.toLowerCase();
		boolean flag = true;
		for (int i = 0; i < sequenceIn.length(); i++)
		{
			if (Character.isDigit(tempSequence.charAt(i)) || tempSequence.charAt(i) == 'h' 
					|| tempSequence.charAt(i) == 'c' 
					|| tempSequence.charAt(i) == 'o' 
					|| tempSequence.charAt(i) == '(' 
					|| tempSequence.charAt(i) == ')')
			{
				flag = true;
			}
			else
			{
				flag = false;
				return flag;
			}
		}
		return flag;
	}
	
	/**
	 * Finds the atom that is invalid.
	 * @param sequenceIn molecule sequence.
	 * @return the offender.
	 */
	public char validAtomOffender(String sequenceIn)
	{
		String tempSequence = sequenceIn.toLowerCase();
		char flag = 'e';
		for (int i = 0; i < sequenceIn.length(); i++)
		{
			if (Character.isDigit(tempSequence.charAt(i)) 
					|| tempSequence.charAt(i) == 'h' || tempSequence.charAt(i) == 'c' 
					|| tempSequence.charAt(i) == 'o' || tempSequence.charAt(i) == '(' 
					|| tempSequence.charAt(i) == ')')
			{
				flag = 'e';
				//intentionally left blank.
			}
			else
			{
				flag = sequenceIn.charAt(i);
				return flag;
			}
		}
		return flag;
	}
	
	
	/**
	 * Retrieves a String containing this Molecule's sequence of atoms.
	 * @return Molecular sequence of the Molecule.
	 */
	public java.lang.String getSequence()
	{
		return moleculeSequence;
	}
	
	/**
	 * Retrieves this Molecule's weight, which is calculated based on the Molecule'
	 * sequence per the algorithm specified.
	 * @return Weight of the Molecule.
	 */
	public int getWeight()
	{
		return moleculeWeight;
	}
	
	/**
	 * Generates and returns a String with the Molecule's sequence and weight. The 
	 * format of the String is
	 * 
	 * [SEQUENCE               ]: WEIGHT
	 * 
	 * Where SEQUENCE has a field width of 25 and is left justified (square 
	 * brackets are just place holders and will not appear in actual return 
	 * values.WEIGHT should be left-justified. There is no space following 
	 * the SEQUENCE field and the colon.
	 * @return the string representing the molecule.
	 */
	public String toString()
	{
		return String.format("%-25s: %d", moleculeSequence, moleculeWeight);
	}
	
	/**
	 * Static utility method to return the atomic weight of a given atom.
	 * Supported atoms are Carbon (C), Hydrogen (H), and Oxygen (O), and
	 * the value of the atom parameter corresponds to the single letter
	 * abbreviation for these atoms (case insensitive). Atomic weightsare 
	 * given in their nearest whole number: 
	 * 
	 * Hydrogen - 1
	 * Carbon - 12
	 * Oxygen - 16

	 * @param atom Character for atom abbreviation
	 * @return Atomic weight of passed atom
	 * @throws molecule.exceptions.InvalidAtomException Thrown if an unsupported 
	 * atom is passed
	 */
	public static int atomWeight(char atom) throws molecule.exceptions.InvalidAtomException
	{
		int atomWeight;
		if (atom == 'H' || atom == 'h')
		{
			atomWeight = HYDROGEN_WEIGHT;
		}
		else if (atom == 'C' || atom == 'c')
		{
			atomWeight = CARBON_WEIGHT;
		}
		else if (atom == 'O' || atom == 'o')
		{
			atomWeight = OXYGEN_WEIGHT;
		}
		else
		{
			throw new InvalidAtomException(atom);
		}
		return atomWeight;
	}
	
	/**
	 * Compares this Molecule to a passed Molecule, determining natural 
	 * order. Molecules with the same weight (regardless of sequence) are 
	 * considered equal. All others are ordered relative to the magnitude 
	 * of their weights.
	 * 
	 * @param other - Incoming Molecule to be compared with this (local) Molecule.
	 * @return Returns an int less than 0 if the local Molecule is less than the passed 
	 * Molecule, an int greater than 0 if the local Molecule is greater than the 
	 * passed Molecule, and a 0 if the Molecules are equal.
	 */
	public int compareTo(Molecule other)
	{
		int compareToValue;
		if (this.getWeight() > other.getWeight())
		{
			compareToValue = 1;
		}
		else if (this.getWeight() < other.getWeight())
		{
			compareToValue = -1;
		}
		else
		{
			compareToValue = 0;
		}
		return compareToValue;
	}
	
	/**
	 * Returns a deep copy of the Molecule. The reference returned should 
	 * refer to acompletely separate molecule with no direct or indirect 
	 * aliasing of any instance data in the originalMolecule. 
	 * NOTE: This method should NOT throw a CloneNotSupportedException.
	 * 
	 * @return Deep copy of the calling Molecule. 
	 */
	public java.lang.Object clone()
	{
		Molecule clone = null;
		try
		{
			clone = (Molecule) super.clone();
			return clone;
		}
		catch (CloneNotSupportedException e)
		{
			System.out.println("Woah");
		}
		return clone;
	}
}
