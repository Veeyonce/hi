import java.util.Random;

public class Agent extends Thread {
	//* Ingredients that the agent has 
	protected Ingredient[] agentIngredients = new Ingredient[2];
	//* Number of pizza made
	protected int myPizza = 0;
	//* Number of pizza to be made
	protected int maxNumOfPizza = 20;
	//* Used to generate a random number of ingredients/pizza
	protected Random randomize = new Random();
	//* The ingredients to be used
	protected static Ingredient[] myIngredients = Ingredient.values();
	//* Used to see if there are ingredients on the table
	protected boolean checkIngredients = false;
	//* Thread for the agent
	public static Agent aThread;

	
	public static void main(String[] args) {
		//* Creates an agent thread and starts it 
		aThread = new Agent();
		aThread.start();
		
		//* Creates a chef thread for each ingredients and starts it
		for (Ingredient ingredient : myIngredients) {
			new Chef(ingredient).start();
		}
	}
	//* Function that is used to put ingredients on the table while
	//* we're not finished making pizza
	public void run() {
		while (!finished()) {
			putOnTable();
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e ) {
			e.printStackTrace();
			System.exit(1);
		}
		//* Prints number of pizza made
		System.out.printf("%d boxes of pizza were made and eaten. %n",
				myPizza);
	}
	//* Function that puts 2 random ingredients on the table that are different
	public void putOnTable() {
		//* Creates an array of ingredients with size of 2
		Ingredient[] ingredients = new Ingredient[2];

		//* Assigns a random ingredient to the first index
		ingredients[0] = myIngredients[randomize.nextInt(myIngredients.length)];

		//* Assigns a random different from the first one
		do {
			ingredients[1] = myIngredients[randomize.nextInt(myIngredients.length)];
		} while (ingredients[0] == ingredients[1]);
		//* Checks if there are ingredients on the table,
		//* if there are, wait. If not put them on the table
		// LOCK ?
		synchronized (this) {
			try {
				while (checkIngredients()) {
					wait();
				}
				//* Puts ingredients on the table
				this.agentIngredients = ingredients;
				this.setIngredients(true);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//* Notifies all
			// RELEASE LOCK?
			notifyAll();
		}
	}
	//Function that checks if the number of pizza is not over the maximum
	//* Returns false if it hasn't reached the max, true if so.
	public boolean finished() {
		return (myPizza >= maxNumOfPizza);
	}
	//* Function that returns the boolean if there are ingredients on the table
	public boolean checkIngredients() {
		return checkIngredients;
	}
	
	//* Function that sets the boolean value if there are ingredients on the table
	public void setIngredients(boolean checkIngredients) {
		this.checkIngredients = checkIngredients;
	}
	//Synchronized functions 
	//* Function that increments the number of pizza made
	synchronized public int addPizza() {
		return myPizza++;
	}

	//* Function that returns the ingredients on the table
	synchronized public Ingredient[] getIngredients() {
		return this.agentIngredients;
	}


}
