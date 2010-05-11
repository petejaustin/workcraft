/*
*
* Copyright 2008,2009 Newcastle University
*
* This file is part of Workcraft.
*
* Workcraft is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* Workcraft is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with Workcraft.  If not, see <http://www.gnu.org/licenses/>.
*
*/
package org.workcraft.plugins.cpog.optimisation;

import java.io.File;
import java.io.IOException;

import org.workcraft.plugins.cpog.optimisation.booleanvisitors.FormulaToString;

@SuppressWarnings("all")
public class Main {
	static String [] scenarios =
	{
		"0000",
		"0100",
		"1000",
		"1101",
		"0010",
		"0110",
		"1010",
		"1111",
	};

	static String [] paper_simple =
	{
		"011--",
		"10---",
		"11010",
		"11-01",
	};

	static String [] smallscenarios =
	{
		"110",
		"101",
		"011",
	};

	static String [] processor =
	{
		"11111100",
		"------01",
		"-----011",
		"-0-----1",
		"0-0000-1",
		"00000111",
		"00001000",
		"0010-011",
		"01000010",
		"0101-000",
		"01010011",
		"11011111",
		"11110111",
	};

	static String [] withGe = new String[]
	                                 {

			"1---00001100",
			"1--000111110",
			"1---01000100",
			"1---00111100",
			"1---1--01000",
			"1-0-0000110A",
			"A01-0101111A",
			"A111010111aA",
	                                 };
	public static void main(String[] args) throws Exception, IOException
	{

		int freeVariables = 4;
		int derivedVariables = 7;

		System.out.println(freeVariables + " " + derivedVariables);

		MiniSatBooleanSolver solver = new MiniSatBooleanSolver();

		CnfGeneratingOptimiser cnfGenerator = new CnfGeneratingOptimiser();
		//Optimiser<ThermometerBooleanFormula> optimiser = new Optimiser<ThermometerBooleanFormula>(new ThermometerNumberProvider());
		//Optimiser<BinaryIntBooleanFormula> optimiser = new Optimiser<BinaryIntBooleanFormula>(new BinaryNumberProvider());
		Optimiser<OneHotIntBooleanFormula> oneHot = new Optimiser<OneHotIntBooleanFormula>(new OneHotNumberProvider());

		CpogSolver solverCnf = new DefaultCpogSolver<Cnf>(cnfGenerator, new SimpleCnfTaskProvider());
		CpogSolver solverCnfLimboole = new DefaultCpogSolver<BooleanFormula>(cnfGenerator, new LimBooleCnfGenerator());
		CpogSolver solverOneHot = new DefaultCpogSolver<BooleanFormula>(oneHot, new LimBooleCnfGenerator());

		CpogOptimisationTask<BooleanFormula> formula = oneHot.getFormula(withGe, 3, 11);
		Cnf cnf = new CleverCnfGenerator().generateCnf(formula.getTask());
		CnfTask task = new SimpleCnfTaskProvider().getCnf(cnf);

		BooleanSolution sol = SolutionReader.readSolution(new CnfTask(
				 ProcessIO.readFile(new File("C:\\Documents and Settings\\User\\My Documents\\weirdCpog\\stream3974298848440370075in")),
				 task.getVars()
		),
		ProcessIO.readFile(new File("C:\\Documents and Settings\\User\\My Documents\\weirdCpog\\stream3616042594776122168out")));

		for(BooleanVariable var : sol.getVariables())
		{
			System.out.println(var.getLabel()+"\t"+sol.getSolution(var));
		}
		if(true)
			throw new RuntimeException("qwe");


		//System.out.println(cnf.toString(new MiniSatCnfPrinter()));

		//System.out.println(cnfGenerator.getFormula(smallscenarios, null, freeVariables, derivedVariables).getTask().toString(new HumanReadableCnfPrinter()));
		//System.out.println(FormulaToString.toString(oneHot.getFormula(smallscenarios, null, freeVariables, derivedVariables).getTask()));
		long start = System.currentTimeMillis();

		CpogEncoding solution = solverCnf.solve(processor, freeVariables, derivedVariables);
		long end = System.currentTimeMillis();

		System.out.println("time: " + (end-start)/1000.0);


		if(solution == null)
			System.out.println("No solution.");
		else
		{
			boolean[][] encoding = solution.getEncoding();
			for(int i=0;i<encoding.length;i++)
			{
				for(int j=0;j<encoding[i].length;j++)
					System.out.print(encoding[i][j]?1:0);
				System.out.println();
			}

			System.out.println("Functions:");
			BooleanFormula[] functions = solution.getFunctions();
			for(int i=0;i<functions.length;i++)
			{
				System.out.println(FormulaToString.toString(functions[i]));
			}
		}


	}
}
