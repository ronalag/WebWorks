package blackberry.ui.dialog.nav.datetime;


 /**
  * @category Signed
  */
public interface CalendarExtensions
{


    /**
     * Field number for <code>get</code> and <code>set</code> indicating the
     * year. This is a calendar-specific value.
     */
     public final static int ERA = 0;
    


    /**
     * Value of the <code>ERA</code> field indicating
     * the period before the common era (before Christ), also known as BCE.
     * The sequence of years at the transition from <code>BC</code> to <code>AD</code> is
     * ..., 2 BC, 1 BC, 1 AD, 2 AD,...
     * @see #ERA
     */
    public static final int BC = 0;



    /**
     * Value of the <code>ERA</code> field indicating
     * the common era (Anno Domini), also known as CE.
     * The sequence of years at the transition from <code>BC</code> to <code>AD</code> is
     * ..., 2 BC, 1 BC, 1 AD, 2 AD,...
     * @see #ERA
     */
    public static final int AD = 1;
    


    /**
     * Field number for <code>get</code> and <code>set</code> indicating the
     * day number within the current year.
     */
    public final static int DAY_OF_YEAR = 6;
    
    /**
     * Field number for <code>get</code> and <code>set</code> indicating the 
     * daylight savings offset in milliseconds.
     */
    public static final int DST_OFFSET = 16;

    /**
     * Date Arithmetic function.
     * Adds the specified (signed) amount of time to the given time field,
     * based on the calendar's rules. For example, to subtract 5 days from
     * the current time of the calendar, you can achieve it by calling:
     * <p>add(Calendar.DATE, -5).
     * @param field the time field.
     * @param amount the amount of date or time to be added to the field.
     */
    public void add(int field, int amount);



    /**
     * Time Field Rolling function.
     * Rolls up or down the specified number of units on the given time field.
     * (A negative roll amount means to roll down.)
     * 
     */
    public void roll(int field, int amount);




    /**
     * returns the maximum of a field given the values of the other fields.
     * for example for month of february and a in a leap year this will return 29 days.
     */
    public int getActualMaximum(int field);
    


    /**
     * returns the minimum of a field given the values of the other fields.
     */
    public int getActualMinimum(int field);
    


    /** 
     * Sets the time, in millis, without creating a date object
     */
    public void setTimeLong(long millis);
     


    /** 
     * Gets the time, in millis, without creating a date object
     */
    public long getTimeLong();
}
