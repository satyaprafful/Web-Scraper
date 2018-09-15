import org.jsoup.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FactbookParser {
	
    private static HashMap<String, String> countryToUrlMap = null;
    private static HashSet<Country> countries = null; 
    private static HashSet<Organization> organizations = null; 
    private static ArrayList<Organization> arrayList = null; 
	public FactbookParser(String baseUrl) {	
	}
	
	public static void main(String[] args) throws IOException {
		parseEverything();
		
//------------------------------------------------------THE FUN STUFF IS HERE ---------------------------------
		
		// Please ensure that the input is well formatted. INPUT IS CASE SENSITIVE Please refer to ReadMe!
		// An example is provided to begin with, so follow along and have fun (:  
		
//		Countries prone to natural disasters?
//		Parameters: "Continent" , "hazard"  
		question1("South America", "earthquake");
		
//		Country flag characteristics?
//		Parameters: "description". Eg "stripe"  
		question2("star");
		
//		Smallest population by Continent?
//		Parameters: "Continent"
		question3("Europe");
		
//		Countries with a smaller area than Pennsylvania
//		Parameters: "Continent"
		question4("Asia");
		
//		Oldest International Organizations
//		Parameters: Any integer less than 200!  
		question5(10);
		
//		Dominant religion percentages!
//		Parameters: "benchmark" , "greaterORsmaller"   
		question6(80.0,"greater");
		question6(50.0,"smaller");
		
//		Landlocked Countries?
//		Parameters: Any realistic integer should have results. Countries are usually not landlocked by > 9  
		question7(1);
		
//		Most frequent colours on Flags!
		question8();
//------------------------------------------------------THE FUN STUFF IS ABOVE ---------------------------------------
	}
	
	public static void parseEverything() throws IOException{
		String portalURL = "https://www.cia.gov/library/publications/the-world-factbook/";
		Document mainDocument = Jsoup.connect(portalURL).get();
		Elements links = mainDocument.select("a[href]");
		String correctLink = "print/textversion.html";
		countryToUrlMap = new HashMap<>();

		boolean containsCorrectLink = false;
		for (Element e : links) {
			// If you find the correct text-version link, set boolean value accdly
			if (e.toString().contains(correctLink)) {
				containsCorrectLink = true;
			}
		}
		if (containsCorrectLink) {
			// Navigating to the textVersion List of Countries
			Document textVersionDocument = Jsoup.connect(portalURL + correctLink).get();
			Elements countryLink = textVersionDocument.select("option[value]");
			for (Element e : countryLink) {
				String countryLinkUrl = e.attr("value");
				if (countryLinkUrl.length() > 0) {
					countryLinkUrl = countryLinkUrl.substring(3);
					if (!e.text().equals("World")) {
						countryToUrlMap.put(e.text(), countryLinkUrl);
					}
				}
			}
		} else {
			System.out
					.println("The text version was not available at this URL. " + 
			"Please update variable");
		}
		// At this point, we have the countries parsed into a StringName - URL Map
		// Further parsing will be done at a country level
		countries = new HashSet<>();
		for (String s : countryToUrlMap.keySet()) {
			countries.add(new Country(s));
		}
		// Parses each country for information
		for (Country c : countries) {
			String countryURL = countryToUrlMap.get(c.getName());
			Document countryDocument = Jsoup.connect(portalURL + countryURL).get();

			// For its continent
			Element eContinent = countryDocument.selectFirst("a[href]:contains(Map references:)");
			if (eContinent != null) {
				String continent = eContinent.parent().nextElementSibling().text();
				c.setContinent(continent);
			}

			// For its natural hazards
			Element eHazards = countryDocument.selectFirst("a[href]:contains(Natural hazards:)");
			if (eHazards != null) {
				String hazards = eHazards.parent().nextElementSibling().text();
				c.setHazards(hazards);
			}

			// For its flag description content
			Element eFlagDescriptions = countryDocument.selectFirst("div[style]:contains(Flag Description)");
			if (eFlagDescriptions != null) {
				String flagText = eFlagDescriptions.text();
				c.setFlagDescription(flagText);
			}

			// For its population
			Element ePopulation = countryDocument.selectFirst("a[href]:contains(Population:)");
			if (ePopulation != null) {
				String populationString = ePopulation.parent().nextElementSibling().text();
				String[] str = populationString.split(" ", 2);
				Integer population = 0;
				try {
					str[0] = str[0].replaceAll(",", "");
					population = Integer.parseInt(str[0]);
				} catch (NumberFormatException e) {
				}
				c.setPopulation(population);
			}

			// For its Area
			Element eArea = countryDocument.selectFirst("a[href]:contains(Area:)");
			if (eArea != null) {
				String areaString = eArea.parent().nextElementSibling().text();
				boolean million = areaString.contains("million");
				boolean hyphen = areaString.contains("-");
				int valArrayPos = 0;
				if (!hyphen) {
					valArrayPos = 1;
				} else {
					valArrayPos = 2;
				}
				String[] areaStr = areaString.split(" ", 5);
				try {
					areaStr[valArrayPos] = areaStr[valArrayPos].replace(",", "");
					Double area = Double.parseDouble(areaStr[valArrayPos]);
					if (million) {
						area = area * 1000000;
					}
					c.setArea(area);
				} catch (Exception e) {
				}
			}

			// For its religion breakdown
			Element eReligion = countryDocument.selectFirst("a[href]:contains(Religions:)");
			if (eReligion != null) {
				String religionString = eReligion.parent().nextElementSibling().text();
				String[] religionParsingArray = religionString.split(" ");
				int dominantReligionIndices = -1;
				String parsedStringReligionName = "";
				Double dominantReligionPercent = 0.0;
				String dominantReligionPercentParser = "";
				// Look thru parsing array
				for (int i = 0; i < religionParsingArray.length; i++) {
					// for first appearance of %.
					if (religionParsingArray[i].contains("%")) {
						dominantReligionIndices = i;
						dominantReligionPercentParser = religionParsingArray[i].substring(0,
								religionParsingArray[i].indexOf("%"));

						// Checks for any hyphens
						if (religionParsingArray[i].contains("-")) {
							dominantReligionPercentParser = religionParsingArray[i].substring(
									religionParsingArray[i].indexOf("-") + 1, religionParsingArray[i].indexOf("%"));
						}

						try {
							dominantReligionPercent = Double.parseDouble(dominantReligionPercentParser);
						} catch (Exception e) {
						}
						// Building Religion Name
						for (int j = 0; j < dominantReligionIndices; j++) {
							parsedStringReligionName = parsedStringReligionName + religionParsingArray[j] + " ";
						}
						i = religionParsingArray.length;
					}
				}
				// If the entry has no % value, then assume 100% and return the whole entry
				if (!religionString.contains("%")) {
					dominantReligionPercent = 100.0;
					parsedStringReligionName = religionString;
				}
				c.setReligion(parsedStringReligionName, dominantReligionPercent);
				c.setDominantReligion(parsedStringReligionName);
			}

			// For its landlockedness
			Element eCoastLine = countryDocument.selectFirst("a[href]:contains(Coastline:)");
			Element eLandBoundaries = countryDocument.selectFirst("a[href]:contains(Land Boundaries:)");
			if (eCoastLine != null && eLandBoundaries != null) {
				String coastLine = eCoastLine.parent().nextElementSibling().text();
				if (coastLine.contains("landlock")) {
					c.setLandlockedness(true);
				}
				String landBoundaries = eLandBoundaries.parent().nextElementSibling().nextElementSibling().text();
				String[] landBoundariesParsingArray = landBoundaries.split(" ");
				String importantValue = null;
				for (String s : landBoundariesParsingArray) {
					if (s.contains(":")) {
						importantValue = s;
					}
				}
				try {
					String evenMoreParsed = importantValue.substring(importantValue.indexOf("(") + 1,
							importantValue.indexOf(")"));
					Integer finalValue = Integer.parseInt(evenMoreParsed);
					c.setNumberOfBorderingCountries(finalValue);
				} catch (Exception e) {
				}
			}
		}
		// End For-each country loop. All country information has been parsed!

		// Connect to Appendix B
		Document organizationDoc = Jsoup
				.connect("https://www.cia.gov/library/publications/" + "the-world-factbook/appendix/appendix-b.html")
				.get();
		Elements orgs = organizationDoc.select("SPAN.category");
		//Creating organizations set
		organizations = new HashSet<>();
		//Finding Name and Data about each organization
		for (Element currOrganization : orgs) {
			Elements data = currOrganization.parent().select("TD.category_data");
			for (Element e : data) {
				//Sieving out those without established years
				if (e.text().contains("established")) {
					Integer year = 0;
					Organization newOrg = new Organization(currOrganization.text());
					organizations.add(newOrg);
					String[] dateParsingArray = e.text().split(" ", 0);
					//Once you find the word "established", check the next 5 words, find the one
					// that goes between 1750 - 2020 because thats a reasonable time frame for a "year" value
					for (int i = 0; i < dateParsingArray.length; i++) {
						if (dateParsingArray[i].equals("established")) {
							for (int k = i; k < i + 5; k++) {
								for (int j = 1750; j < 2020; j++) {
									try {
										//removing colons
										if (dateParsingArray[k].contains(";")) {
											dateParsingArray[k] = dateParsingArray[k].replace(";", "");
										}
										//removing commas
										if (dateParsingArray[k].contains(",")) {
											dateParsingArray[k] = dateParsingArray[k].replace(",", "");
										}
										year = Integer.parseInt(dateParsingArray[k]);
										if (year.equals(new Integer(j))) {
											Integer day = 1;
											try {
												day = Integer.parseInt(dateParsingArray[k - 2]);
											} catch (Exception execpt) {
											}
											newOrg.setDay(day);
											newOrg.setMonth(convertMonthToInt(dateParsingArray[k - 1]));
											newOrg.setYear(j);
										}
									} 
									catch (Exception ex) {
									}
								}
							}
						}
					}
				}
			}
		}
		//Creating date object for each organization
		arrayList = new ArrayList<>();
		for (Organization org : organizations) {
			if (org.getDay() > 31 || org.getDay() < 1) {
				org.setDay(1);
			} 
			int day = org.getDay();
			int month = org.getMonth();
			int year = org.getYear();
			if (year > 1700) {
				Calendar cal = Calendar.getInstance();
				cal.set(year, month, day);
				Date dateRep = cal.getTime();
				org.setEstablishedDate(dateRep);
				arrayList.add(org);
			}
		}
		//Sorting because we can now compare using dates
		Collections.sort(arrayList);
	}
	//End main
	
//	---------------------------------------Question 1 - 8 Code--------------------------------------------------------
	 public static List<String> question1(String continent, String hazard) {
		  List<String> ret = new ArrayList<String>();
		  System.out.print("Qn 1 Ans: ");
		  for (Country c: countries) {
			  if (c.getContinent()!= null && c.getHazards() != null) {
				  if (c.getContinent().contains(continent) && c.getHazards().contains(hazard)) {
					  ret.add(c.getName());
					  System.out.print(c.getName() + " | ");
				  }
			  }
		  }
		  return ret;
	  }
	  
	  public static List<String> question2(String descriptor){
		  List<String> ret = new ArrayList<String>();
		  System.out.println();
		  System.out.print("Qn 2 Ans: ");
		  for (Country c: countries) {
			  if (c.getFlagDescription() != null) {
				  if (c.getFlagDescription().contains(descriptor)) {
					  ret.add(c.getName());
					  System.out.print(c.getName() + " | ");
				  }
			  }
		  }
		  return ret;
	  }
	  
	  public static String question3(String continent) {
		  String smallestPopCountry = "";
		  int smallestPopulation = (int) Math.pow(2, 31);
		  for (Country c: countries) {
			  if (c.getContinent()!= null) {
				  if (c.getPopulation() != 0 && c.getContinent().contains(continent)) {
					   if (c.getPopulation() < smallestPopulation) {
						   smallestPopulation = c.getPopulation();
						   smallestPopCountry = c.getName();
					   }
				  }
			  }
		  }
		  System.out.println();
		  System.out.println("Qn 3 Ans: " + smallestPopCountry);
		  return smallestPopCountry;
	  }
	  
	  public static List<String> question4(String continent) {
		  List<String> ret = new ArrayList<String>();
		  Double pennsylvaniaArea = 119280.0;
		  System.out.print("Qn 4 Ans: ");
		  for (Country c: countries) {
			  if (c.getContinent()!= null && c.getArea() != null) {
				  if (!c.getArea().equals(0.0) && c.getContinent().contains(continent)) {
					   if (c.getArea() < pennsylvaniaArea) {
						   ret.add(c.getName());
						   System.out.print(c.getName() + " | ");
					   }
				  }
			  }
		  }
		  return ret;
	  }
	  
	  public static List<Organization> question5 (int number){
		  List<Organization> ret = new ArrayList<Organization>();
		  System.out.println();
		  System.out.print("Qn 5 Ans: ");
		  for (int i = 0; i < number; i++) {
			  ret.add(arrayList.get(i));
			  System.out.print(arrayList.get(i).getName() + " | ");
		  }
		  return ret;
	  }
	  
	  public static List<Country> question6(Double benchmark, String greaterOrsmaller){
		  List<Country> retGreater = new ArrayList<Country>(); 
		  List<Country> retSmaller = new ArrayList<Country>(); 
		  for (Country c: countries) {
			  if(c.getReligionMap().keySet() != null && c.getDominantReligion() != null) {
				  if(c.getReligionMap().get(c.getDominantReligion()).compareTo(benchmark) > 0) {
					retGreater.add(c);  
				  }
				  else {
					retSmaller.add(c);  
				  }
			  }
		  }
		  System.out.println();
		  System.out.print("Qn 6 Ans: ");
		  if (greaterOrsmaller.equals("greater")) {
			  for (Country c: retGreater) {
				   System.out.print(c.getName() + " - " + c.getDominantReligion() + " | ");
			  }
			  return retGreater; 
		  }
		  else {
			  for (Country c: retSmaller) {
				   System.out.print(c.getName() + " - " + c.getDominantReligion() + " | ");
			  }
			  return retSmaller;
		  }
	  }
	  
	  public static List<String> question7(int noOfCountries){
		  System.out.println();
		  System.out.print("Qn 7 Ans: ");
		  List<String> ret = new ArrayList<String>(); 
		  for (Country c : countries) {
			  if ( c.isLandlockedness() && c.getNumberOfBorderingCountries().equals(noOfCountries)) {
				   ret.add(c.getName());
				   System.out.print(c.getName() + " | ");
			  }
		  }
		  return ret; 
	  }
	  
	  public static List<String> question8(){
		  List<String> ret = new ArrayList<String>(); 
		  int redCount = 0, greenCount = 0, blueCount = 0, yellowCount = 0, orangeCount = 0,
				  blackCount = 0, whiteCount = 0;
		  int totalCountries = countries.size();
		  System.out.println();
		  System.out.print("Qn 8A Ans: ");
		  for (Country c: countries) {
			  if (c.getFlagDescription() != null) {
				  if (c.getFlagDescription().contains("red")) {
					  redCount++;
				  }
				  if (c.getFlagDescription().contains("green")) {
					  greenCount++;
				  }
				  if (c.getFlagDescription().contains("blue")) {
					  blueCount++;
				  }
				  if (c.getFlagDescription().contains("yellow")) {
					  yellowCount++;
				  }
				  if (c.getFlagDescription().contains("orange")) {
					  orangeCount++;
				  }
				  if (c.getFlagDescription().contains("black")) {
					  blackCount++;
				  }
				  if (c.getFlagDescription().contains("white")) {
					  whiteCount++;
				  }
			  }
		  }
		  System.out.print("Red: " + redCount + " | ");
		  System.out.print("White: " + whiteCount + " | ");
		  System.out.print("Blue: " + blueCount + " | ");
		  System.out.print("Green: " + greenCount + " | ");
		  System.out.print("Yellow: " + yellowCount + " | ");
		  System.out.print("Black: " + blackCount + " | ");
		  System.out.print("Orange: " + orangeCount + " | ");
		  System.out.println();
		  System.out.print("Qn 8B Ans: " + "Total % of flags that Red is found in "
		  + ((double) redCount*100/(double) totalCountries));
		  return ret;
	  }
	  
	  //Helper Function to build month 
		private static int convertMonthToInt(String month) {
			if (month.equals("January")) {
				return 0; 
			}
			if (month.equals("February")) {
				return 1;
			}
			if (month.equals("March")) {
				return 2;
			}
			if (month.equals("April")) {
				return 3;
			}
			if (month.equals("May")) {
				return 4;
			}
			if (month.equals("June")) {
				return 5;
			}
			if (month.equals("July")) {
				return 6;
			}
			if (month.equals("August")) {
				return 7;
			}
			if (month.equals("September")) {
				return 8;
			}
			if (month.equals("October")) {
				return 9;
			}
			if (month.equals("Novemeber")) {
				return 10;
			}
			if (month.equals("December")) {
				return 11;
			}
			else return 0;
		}
}
