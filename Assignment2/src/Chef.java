public class Chef extends Thread {
	//* Ingredients of the chef
	protected Ingredient infiniteIngredients;
	//* Holds the number of pizza made
	protected int numOfPizza = 0;

	//* Constructor that sets the ingredients  
	public Chef(Ingredient ing) {
		this.infiniteIngredients = ing;
	}

	public void run() {
		//* Creates an agent thread 
		Agent agent = Agent.aThread;
		//* Runs while the agent is not finished making pizza
		while (!agent.finished()) {
			//* Synchronize with agent
			synchronized (agent) {
				try {
					//* Checks if there are ingredients on the table and
					//* if the agent is done making pizza.
					//* If not done, makes agent wait
					while (!agent.checkIngredients()
							&& !agent.finished()) {
						agent.wait();
					}
					//* Checks if agent is finished making pizza
					//* if not, we can make more pizza
					// Check if all pizza are done
					if (!agent.finished()) {
						boolean canMakePizza = true;
						//* Parses through the ingredients to see if we need them
						for (Ingredient ing : agent.getIngredients()) {
							if (ing == this.infiniteIngredients) {
								canMakePizza = false;
								break;
							}
						}

						//* If we can make a pizza, it makes a pizza
						//* Increments number of pizza
						if (canMakePizza) {
							agent.addPizza();
							agent.setIngredients(false);
							numOfPizza++;
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				//* Notifies all
				agent.notifyAll();
			}
		}

		//* Prints out the number of pizza made by the ingredients
		System.out.printf("There were %d %s ingredients used by this chef. %n", 
				numOfPizza, infiniteIngredients.toString());
	}
}
