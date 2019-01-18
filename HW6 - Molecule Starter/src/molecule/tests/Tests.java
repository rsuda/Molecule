package molecule.tests;
import static org.junit.Assert.*;

import org.junit.Test;

import molecule.exceptions.InvalidAtomException;
import molecule.exceptions.InvalidSequenceException;
import molecule.Molecule;
import molecule.MoleculeCollection;

public class Tests 
{
	private static Molecule one = new Molecule("H");
	
	private static Molecule two = new Molecule("C");
	
	private static Molecule three = new Molecule("O");
	
	private static Molecule four = new Molecule("OH");
	
	private static Molecule five = new Molecule("COH");
	
	private static Molecule six = new Molecule("C4O3");
	
	private static Molecule seven = new Molecule("HO4");
	
	private static Molecule eight = new Molecule("CO(OH)2");
	
	private static Molecule nine = new Molecule("HC(CO)5");
	
	private static Molecule ten = new Molecule("H(CO(OH)3)5");
	
	private static MoleculeCollection molecules = new MoleculeCollection();

	@Test
	public void testAtomWeight() 
	{
		assertEquals(1, one.atomWeight('H'));
		assertEquals(1, one.atomWeight('h'));
		assertEquals(16, one.atomWeight('O'));
		assertEquals(16, one.atomWeight('o'));
		assertEquals(12, one.atomWeight('C'));
		assertEquals(12, one.atomWeight('c'));
	}
	
	@Test (expected = InvalidAtomException.class)
	public void testAtomWeightException() 
	{
		assertEquals(InvalidAtomException.class, one.atomWeight('e'));
	}
	
	@Test 
	public void validSequence() 
	{
		assertEquals(true, one.validSequence("()"));
		assertEquals(true, one.validSequence("(())"));
		assertEquals(true, one.validSequence("((()()))"));
	}
	
	@Test 
	public void invalidSequence() 
	{
		assertEquals(false, one.validSequence("()("));
		assertEquals(false, one.validSequence(")(())"));
		assertEquals(false, one.validSequence("(((()()))"));
	}
	
	@Test 
	public void validAtom() 
	{
		assertEquals(true, one.validAtom("COH3"));
		assertEquals(true, one.validAtom("CH(CO)5"));
		assertEquals(true, one.validAtom("O((CHO)3)6"));
	}
	
	@Test 
	public void invalidAtom() 
	{
		assertEquals(false, one.validAtom("[C]OH73"));
		assertEquals(false, one.validAtom("CH(C+O)5"));
		assertEquals(false, one.validAtom("O((CEO)3)6"));
	}
	
	@Test 
	public void invalidAtomOffender() 
	{
		assertEquals('[', one.validAtomOffender("[C]OH73"));
		assertEquals('+', one.validAtomOffender("CH(C+O)5"));
		assertEquals('E', one.validAtomOffender("O((CEO)3)6"));
		assertEquals('R', one.validAtomOffender("O((CO)3)R6"));
	}
	
	@Test 
	public void moleculeToString() 
	{
		assertEquals("CO(OH)2                  : 62", eight.toString());
		assertEquals("HC(CO)5                  : 153", nine.toString());
		assertEquals("H(CO(OH)3)5              : 1261", ten.toString());
	}
	
	@Test
	public void moleculeCompareTo()
	{
		assertEquals(-1, one.compareTo(eight));
		assertEquals(1, nine.compareTo(two));
		assertEquals(0, ten.compareTo(ten));
	}
	
	@Test
	public void moleculeCalculateWeight()
	{
		one.calculateWeight("H");
		assertEquals(1, one.getWeight());
	}
	
	@Test
	public void collectionSort()
	{
		MoleculeCollection collectionTest = new MoleculeCollection();
		collectionTest.addMolecule(0, one);
		collectionTest.addMolecule(1, two);
		collectionTest.addMolecule(2, three);
		collectionTest.sort();
		assertEquals(one, collectionTest.getMolecule(0));
	}
	
	@Test
	public void collectionWeight()
	{
		MoleculeCollection collectionTest = new MoleculeCollection();
		collectionTest.addMolecule(0, one);
		collectionTest.addMolecule(1, two);
		collectionTest.addMolecule(2, three);
		collectionTest.sort();
		assertEquals(29, collectionTest.moleculeWeights());
	}
	
	@Test
	public void calculateMoleculeWeight()
	{
		Molecule test = new Molecule("OH3(HC)2");
		assertEquals(45, test.getWeight());
	}
	
	@Test
	public void calculateMolecularWeightEasy()
	{
		Molecule test = new Molecule("CO20");
		assertEquals(332, test.getWeight());
	}
	
	@Test
	public void calculateMoleculeWeightHard()
	{
		Molecule test = new Molecule("CH2(H3(CO)5)2");
		assertEquals(154, test.getWeight());
	}
	
	@Test
	public void calculateMoleculeWeightEmpty()
	{
		Molecule test = new Molecule("OH3");
		test.calculateWeight("");
		assertEquals(0, test.getWeight());
	}
	
	@Test
	public void moleculesConstructor()
	{
		molecules.addMolecule(0, one);
		molecules.addMolecule(1, two);
		molecules.addMolecule(2, three);
		molecules.addMolecule(3, four);
		
		MoleculeCollection collectionTest = new MoleculeCollection(molecules.getMoleculeList());
		
		collectionTest.changeSequence(0, "OH7");
		assertEquals("OH7", collectionTest.getMolecule(0).getSequence());
	}
	
	@Test (expected = InvalidSequenceException.class)
	public void moleculesInvalidChangeSequence()
	{
		molecules.addMolecule(0, one);
		molecules.addMolecule(1, two);
		molecules.addMolecule(2, three);
		molecules.addMolecule(3, four);
		
		MoleculeCollection collectionTest = new MoleculeCollection(molecules.getMoleculeList());
		collectionTest.changeSequence(0, "OH)7");
		//assertEquals(InvalidSequenceException.class, collectionTest.getMolecule(0).getSequence());
	}

	@Test (expected = InvalidAtomException.class)
	public void moleculesInvalidAtomChangeSequence()
	{
		molecules.addMolecule(0, one);
		molecules.addMolecule(1, two);
		molecules.addMolecule(2, three);
		molecules.addMolecule(3, four);
		
		MoleculeCollection collectionTest = new MoleculeCollection(molecules.getMoleculeList());
		collectionTest.changeSequence(0, "OE7");
		//assertEquals(InvalidAtomException.class, collectionTest.getMolecule(0).getSequence());
	}
	
	@Test
	public void addNull()
	{
		MoleculeCollection collectionTest = new MoleculeCollection();
		collectionTest.addMolecule(0, null);
		collectionTest.addMolecule(0, two);
		collectionTest.addMolecule(1, three);
		assertEquals(28, collectionTest.moleculeWeights());
	}
}
