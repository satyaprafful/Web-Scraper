# Web-Scraper
CIA Factbook Web Scraper

Note: The project was initially done as part of homework, so many of the "Tasks" / "Questions" being addressed are queries intended to test the quality of the data parser. These queries are actually easily modifiable based on requirement since all the necessary information is stored in Country/Organization objects. 

------------------------------------------------------------------------------------------------------------------------------General Design: 
------------------------------------------------------------------------------------------------------------------------------

There are only 3 classes FactbookParser, Country and Organization. 
The main bulk of the heavy lifting happens in FactbookParser. The function parseEverything() quite 
literally parses everything. The model is quite efficient and it begins by identifying a list 
of countries and maps those countries to their URLs. Subsequently, we create Country Objects for each of 
these, and continually store information about the countries within these objects. Eg. Population, Area etc. 
The advantage of this is we store everything in one pass and do not repeatedly have to access pages for bits 
of information every time. 

parseEverything is commented heavily so as to make reading easier. I am particularly proud of my parsing algorithm 
for "religion" Q6 and the 10 oldest organizations Q5. They truly go the extra mile to capture as many edge cases 
as possible, reducing the number of assumptions I needed to make resulting in greater precision.  

Country and Organization are both just Objects to store properties. Organization implements Comparable such 
that the Organization objects can be compared based on their Dates. I have overridden the compareTo function 
accordingly. 

Aside from that, the design is very clean, easy to understand and highly modular given that I only
hardcoded 2 URLs. In fact, even Task 7 allows users to choose if they want singly - landlocked or doubly-landlocked etc. 

Disclaimer: Apologies for the lack of a User Interface, I spent all my time working on the parsing algorithms :( 
But, I tried to make it as interactive as possible within in main method, so it shouldnt be too difficult to play with! 

------------------------------------------------------------------------------------------------------------------------------
Original Task List: 
------------------------------------------------------------------------------------------------------------------------------
1. List countries in South Americathat are prone to earthquakes.

2. List all countries that havea starin their flag.

3. Find the country with the smallest populationin Europe.

4. Listthe countries  in Asia that have  a  smaller  total  area  than Pennsylvania.

5. List the 10 oldest International Organizations and Groups in chronological order of their dates of establishment. 

6. Certain countries have one dominant religion (in terms of fraction of the population) whereas other  countries  don’t.     List  countries  (along  with  the  religion)  where  the  dominant  religion accounts for more than 80% of  the  population.  List  countries  (along  with  the  religions)  where the dominant religion accounts for less than 50% of the population.

7. A landlocked country is one that is entirely enclosed by land. For example, Austria is landlocked and shares its borders with Germany, Czech Republic, Hungary, etc.There are certain countries that are entirely landlocked by a single country. Find these countries.

8. Wild card – come up with an interesting question. -> Which colours are used most often in National Flags? What percentage of flags does the most frequent colour appear on? 

------------------------------------------------------------------------------------------------------------------------------
Assumptions: 
------------------------------------------------------------------------------------------------------------------------------
1. All the content in "map references" refer to unique continents. We see that this is not the case below in Qn 1's formatting requirements, however,
for the purposes of our crawling, this fortunately does not cause any negative effects to our results. Eg. "Asia" is a substring of "Southeast Asia" 
etc. 
 
2. Only considered countries with their own unique flags. Those using other country's flags were not considered.

3. We assume for Qn 6 that a lack of a percentage in religious demographic means 100% of the community belongs to 
that particular religion. Notably, the Vatican and a few other religious locations especially. 

4. Qn 5, For dates that were unavailable, the earliest possible was chosen. Eg. If no Day then Day = 1. no Month then Month = January
------------------------------------------------------------------------------------------------------------------------------
Input Format Instructions / Some other Assumptions 
------------------------------------------------------------------------------------------------------------------------------
Task 1: Well Formatted Continents are as follow
		Asia, Europe
		Europe
		Africa
		Political Map of the World
		Arctic Region
		North America
		Oceania
		Antarctic Region, Africa
		Middle East
		Southeast Asia
		Antarctic Region
		Asia
		Central America and the Caribbean
		metropolitan France: Europe
	
Well Formatted Hazards -> Not to be capitalized. Eg. "earthquake", "drought", "flood"

Task 2: Well Formatted Shapes/Colours -> Not to be capitalized. Eg."star", "red". 

Task 3, 4: Refer to Qn 1 for "continent" options. Note there are some additional "continents" for specificity 
purposes due to the nature of the crawling. Not to worry, selecting "Asia" will also include "Southeast Asia"

Task 5: Integers smaller than 200. Since the database only has approximately 200 countries.

Task 6: The percentage you wish to check must be in Double form without the % sign. Eg. 80% -> 80.0 or 24% -> 24.0
Additionally, you must also provide another parameter "greater" or "smaller" to determine which bound you would like.  

Task 7: Any realistic integer.  

Task 8: Sit back and relax!
