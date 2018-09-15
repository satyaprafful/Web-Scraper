import java.util.HashMap;

public class Country {
	
	private String name; 
	private String continent;
	private String flagDescription;
	private int population; 
	private String geography;
	private HashMap<String, Double> religion = null;
	private String hazards;
	private String dominantReligion; 
	private Double area; 
	private boolean landlockedness = false;
	private Integer numberOfBorderingCountries = 0; 
	

	public Country(String name) {
		this.name = name; 
		religion = new HashMap<>();
	}
		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContinent() {
		return continent;
	}
	public void setContinent(String continent) {
		this.continent = continent;
	}
	public String getFlagDescription() {
		return flagDescription;
	}
	public void setFlagDescription(String flagDescription) {
		this.flagDescription = flagDescription;
	}
	public int getPopulation() {
		return population;
	}
	public void setPopulation(int population) {
		this.population = population;
	}
	public String getGeography() {
		return geography;
	}
	public void setGeography(String geography) {
		this.geography = geography;
	}
	public HashMap<String, Double> getReligionMap() {
		return religion;
	}
	public void setReligion(String religionName, Double percentage) {
		religion.put(religionName, percentage);
	}
	public String getHazards() {
		return hazards;
	}
	public void setHazards(String hazards) {
		this.hazards = hazards;
	}

	/**
	 * @return the area
	 */
	public Double getArea() {
		return area;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(Double area) {
		this.area = area;
	}

	/**
	 * @return the landlockedness
	 */
	public boolean isLandlockedness() {
		return landlockedness;
	}

	/**
	 * @param landlockedness the landlockedness to set
	 */
	public void setLandlockedness(boolean landlockedness) {
		this.landlockedness = landlockedness;
	}

	/**
	 * @return the numberOfBorderingCountries
	 */
	public Integer getNumberOfBorderingCountries() {
		return numberOfBorderingCountries;
	}

	/**
	 * @param numberOfBorderingCountries the numberOfBorderingCountries to set
	 */
	public void setNumberOfBorderingCountries(Integer numberOfBorderingCountries) {
		this.numberOfBorderingCountries = numberOfBorderingCountries;
	}

	/**
	 * @return the dominantReligion
	 */
	public String getDominantReligion() {
		return dominantReligion;
	}

	/**
	 * @param dominantReligion the dominantReligion to set
	 */
	public void setDominantReligion(String dominantReligion) {
		this.dominantReligion = dominantReligion;
	}
	
}
