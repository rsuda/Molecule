// COURSE: CSCI1620
// TERM: Fall 2018
//
// NAME: Tyler Labreck and Robin Suda
// RESOURCES: The java doc and the linked list power point was used.
package molecule;
import java.util.LinkedList;

import molecule.exceptions.InvalidAtomException;
import molecule.exceptions.InvalidSequenceException;

/**
 * A collection of Molecules maintained in a LinkedList. Allows for adding, 
 * sorting, and updating Molecules in the collection. A List of the Molecules 
 * can also be retrieved; the returned list is a deep copy of the list held 
 * in the MoleculeCollection.
 * @author robin
 *
 */
public class MoleculeCollection 
{
	/**
	 * List list of molecules.
	 */
	private LinkedList<Molecule> collection;
	
	/**
	 * Creates a new MoleculeCollection containing no Molecules yet.
	 */
	public MoleculeCollection()
	{
		collection = new LinkedList<Molecule>();
	}
	
	/**
	 * Creates a new MoleculeCollection based upon an existing list of 
	 * Molecules. Thenewly created MoleculeCollection will store a deep 
	 * copy of the data in moleculeListInto enforce encapsulation.
	 * 
	 * If the passed reference is null, the created MoleculeCollection will 
	 * be empty.
	 * @param moleculeListIn LinkedList of Molecules used to create a new 
	 * MoleculeCollection.
	 */
	public MoleculeCollection(java.util.LinkedList<Molecule> moleculeListIn)
	{
		collection = moleculeListIn;
	}
	
	/**
	 * Adds a copy of a given Molecule to this MoleculeCollection at the 
	 * given index.Future external changes to the original Molecule will 
	 * not impact values in the collection. If add is null, this 
	 * MoleculeCollection is unchanged.If the given index is out of range, 
	 * the Molecule will be added to the end of the Collection
	 * @param index The index in which to add the Molecule
	 * @param add Molecule to be added to the Collection
	 */
	public void addMolecule(int index, Molecule add)
	{
		if (add == null)
		{
			assert true;
		}
		else if (index >= 1 && index < collection.size() - 1)
		{
			collection.add(index, add);
		}
		else
		{
			collection.add(add);
		}
	}

	/**
	 * Reorders the MoleculeCollection based on atomic weight. Molecules with 
	 * the same weights should appear in their original order of insertion 
	 * relative to one another(ie., sort() is a stable sorting algorithm).
	 */
	public void sort()
	{
		int length = collection.size(); 
		for (int i = 1; i < length; ++i) 
		{ 
			Molecule temp = collection.get(i); 
			int j = i - 1; 
			while (j >= 0 && collection.get(j).compareTo(temp) > 0) 
			{ 
				collection.set(j + 1, collection.get(j)); 
				j = j - 1; 
			} 
			collection.set(j + 1, temp); 
		} 
	}
	
	/**
	 * Sums the weights of all Molecules in the MoleculeCollection.
	 * @return The sum of all weights in the collection
	 */
	public int moleculeWeights()
	{
		int sum = 0;
		for (int i = 0; i < collection.size(); i++)
		{
			sum += collection.get(i).getWeight();
		}
		return sum;
	}
	
	/**
	 * Generates and returns a deep copy of a list containing all of the Molecules 
	 * in this MoleculeCollection. Modifying the returned list will not 
	 * impact the contents of this MoleculeCollectionin any way.
	 * @return Deep copy of the Molecules
	 */
	public java.util.LinkedList<Molecule> getMoleculeList()
	{
		LinkedList<Molecule> deepCopyCollection = new LinkedList<Molecule>();
		for (int i = 0; i < collection.size(); i++)
		{
			deepCopyCollection.add((Molecule) collection.get(i).clone());
			
		}
		return deepCopyCollection;
	}
	
	/**
	 * Changes the sequence of a Molecule in the collection at the specified index. 
	 * This does not create a new Molecule, rather modifies an existing Molecule. 
	 * If the provided sequence is not valid due to either an invalid sequence or 
	 * an invalid atom, the original state of the Molecule at the specified 
	 * index should be unaffected and the resulting exception will be 
	 * thrown to the caller.
	 * @param index Location of the Molecule to update
	 * @param newSequence New sequence of the specified Molecule
	 * @throws InvalidAtomException Thrown if the new sequence is invalid due to 
	 * unknown atom
	 * @throws InvalidSequenceException Thrown if the new sequence is invalid 
	 * due to format
	 */
	public void changeSequence(int index, java.lang.String newSequence) throws InvalidAtomException, 
		InvalidSequenceException
	{
		Molecule temp = new Molecule(newSequence);
		if (!temp.validSequence(newSequence))
		{
			throw new InvalidSequenceException();
		}
		else if (!temp.validAtom(newSequence))
		{
			throw new InvalidAtomException(temp.validAtomOffender(newSequence));
		}
		else
		{
			collection.set(index, temp);
		}
	}
	
	/**
	 * Testing only.
	 * @param index the index.
	 * @return the molecule.
	 */
	public Molecule getMolecule(int index)
	{
		return collection.get(index);
	}
}
