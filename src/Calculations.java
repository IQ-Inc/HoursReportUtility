import java.util.ArrayList;

public class Calculations extends NewHoursReport {
	private double projectCost;
	private double rate;
	private double projectHours;
	private String employee;
	
	public static void main(String[] args) {
		ArrayList<Object> test = new ArrayList<>();
		
		test.add("A");
		test.add("A");
		test.add("B");
		test.add("B");
		test.add("C");
		test.add("C");
		test.add("C");
		System.out.println(test);
		removeDuplicate(test);
		
		System.out.println(input);
	}
	
	
	
	
	public double costForProject(double rate, double hours, String employee) {
		this.employee = employee;
		this.rate = rate;
		this.projectHours = hours;
		
		int cost = 0;
		
		//For loop? Once I figure out how to manipulate array data
		
		return cost;
	}
	
	//Getters and Setters
	public String getEmployee() {
		return employee;
	}

	public void setEmployee(String employee) {
		this.employee = employee;
	}

	public double getProjectCost() {
		return projectCost;
	}
	public void setProjectCost(double projectCost) {
		this.projectCost = projectCost;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public double getProjectHours() {
		return projectHours;
	}
	public void setProjectHours(double projectHours) {
		this.projectHours = projectHours;
	}
	
	
}
