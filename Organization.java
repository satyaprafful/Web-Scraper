import java.util.Date;


public class Organization implements Comparable<Organization> {
	private Date establishedDate = null;
	private int day;
	private int month;
	private int year; 
	private String name = null; 
	
	public Organization (String name) {
		this.setName(name); 
	}
	
	@Override
	public int compareTo(Organization o2) {
		return this.getEstablishedDate().compareTo(o2.getEstablishedDate());
	}
	
	/**
	 * @return the day
	 */
	public int getDay() {
		return day;
	}

	/**
	 * @param day the day to set
	 */
	public void setDay(int day) {
		this.day = day;
	}

	/**
	 * @return the month
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * @param month the month to set
	 */
	public void setMonth(int month) {
		this.month = month;
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * @return the establishedDate
	 */
	public Date getEstablishedDate() {
		return establishedDate;
	}

	/**
	 * @param establishedDate the establishedDate to set
	 */
	public void setEstablishedDate(Date establishedDate) {
		this.establishedDate = establishedDate;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
