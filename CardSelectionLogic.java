/**
 * Represents the model of the card selection process of players
 */
public class CardSelectionLogic {
    private String[] animals = {"Elephant", "Lion", "Tiger", "Leopard", "Wolf", "Dog", "Cat", "Rat"};
    private int[] animalStrength = {8, 7, 6, 5, 4, 3, 2, 1};
    private String player1Name;
    private String player2Name;
    
    /**
     * Instantiates the model architecture of the card selection process
     * @param player1Name name of player 1
     * @param player2Name name of player 2
     */
    public CardSelectionLogic(String player1Name, String player2Name) {
        this.player1Name = player1Name;
        this.player2Name = player2Name;
    }
    
	/**
     * Returns animal name
     * @param index index of the animal
     * @return string of the animal name
     */
    public String getAnimalName(int index) {
        return animals[index];
    }
	
	/**
     * Returns the index of the animal based on its strength
     * @param strength strength of the animal
     * @return index index of the animal
     */
	public int getAnimalIndex(int strength) {
		for (int i = 0; i < animalStrength.length; i++) {
			if (animalStrength[i] == strength) {
				return i;  // Return the index where the strength matches
			}
		}
		return -1;  // If no match, return -1 (supposedly, impossible)
	}
	
	/**
     * Returns strength of the animal
     * @param index index of the animal
     * @return correct strength value of the animal
     */
	public int getAnimalStrength(int index) {
		return animalStrength[index];  // Correctly maps index to strength
	}
    
	/**
     * Compares the strengths of two chosen cards' animal piece
     * @param player1Strength strength of player 1's animal piece
     * @param player2Strength strength of player 2's animal piece
     * @return name of the player the stronger animal
     */
    public String compareAnimals(int player1Strength, int player2Strength) {
		//System.out.println("p1 "+player1Strength +" p2 "+ player2Strength);
		
        if (player1Strength > player2Strength) {
            return player1Name;  // Player 1's animal is stronger, so Player 1 goes first
			
        } 
        else 
        {
            return player2Name;  // Player 2's animal is stronger, so Player 2 goes first
        }
    }
}