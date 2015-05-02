package algorithme;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.math3.optim.MaxIter;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.NonNegativeConstraint;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

public class Demo {

	public static void main(String[] args) {

		LinearObjectiveFunction f = new LinearObjectiveFunction(new double[] {
				50, 60 }, 0);
		Collection<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
		constraints.add(new LinearConstraint(new double[] { 1, 2 },
				Relationship.LEQ, 8));
		constraints.add(new LinearConstraint(new double[] { 1, 1 },
				Relationship.LEQ, 5));
		constraints.add(new LinearConstraint(new double[] { 9, 4 },
				Relationship.LEQ, 36));

		SimplexSolver solver = new SimplexSolver();
		PointValuePair solution = solver.optimize(new MaxIter(100), f,
				new LinearConstraintSet(constraints), GoalType.MAXIMIZE,
				new NonNegativeConstraint(true));
		System.out.println(solution.getValue().toString());
		for (double b : solution.getPoint()) {
			System.out.println(b + ";");
		}

	}

	public void test() {
		LinearObjectiveFunction f = new LinearObjectiveFunction(new double[] {
				1, 1, 1 }, 0);
		Collection<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
		constraints.add(new LinearConstraint(new double[] { 1, 0, 0 },
				Relationship.GEQ, 514));
		constraints.add(new LinearConstraint(new double[] { 1, 2, 1 },
				Relationship.GEQ, 321));
		constraints.add(new LinearConstraint(new double[] { 0, 3, 1 },
				Relationship.GEQ, 720));
		constraints.add(new LinearConstraint(new double[] { 3, 4, 1 },
				Relationship.GEQ, 162));

		SimplexSolver solver = new SimplexSolver();
		PointValuePair solution = solver.optimize(new MaxIter(100), f,
				new LinearConstraintSet(constraints), GoalType.MINIMIZE,
				new NonNegativeConstraint(true));
		System.out.println(solution.getValue().toString());
		System.out.println(solution.getKey().toString());
		for (double b : solution.getPoint()) {
			System.out.println(b + ";");
		}
	}
}