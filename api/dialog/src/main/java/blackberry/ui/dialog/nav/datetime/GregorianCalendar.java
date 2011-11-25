/*
 * @(#)GregorianCalendar.java   1.47 99/04/22
 *
 * (C) Copyright Taligent, Inc. 1996-1998 - All Rights Reserved
 * (C) Copyright IBM Corp. 1996-1998 - All Rights Reserved
 *
 * Portions copyright (c) 1996-1998 Sun Microsystems, Inc. All Rights Reserved.
 *
 *   The original version of this source code and documentation is copyrighted
 * and owned by Taligent, Inc., a wholly-owned subsidiary of IBM. These
 * materials are provided under terms of a License Agreement between Taligent
 * and Sun. This technology is protected by multiple US and International
 * patents. This notice and attribution to Taligent may not be removed.
 *   Taligent is a registered trademark of Taligent, Inc.
 *
 * Permission to use, copy, modify, and distribute this software
 * and its documentation for NON-COMMERCIAL purposes and without
 * fee is hereby granted provided that this copyright notice
 * appears in all copies. Please refer to the file "copyright.html"
 * for further important copyright and licensing information.
 *
 * SUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */

package blackberry.ui.dialog.nav.datetime;

import java.util.*;
import net.rim.device.api.i18n.*;

import blackberry.ui.dialog.nav.datetime.CalendarExtensions;


/**
 * <code>GregorianCalendar</code> is a concrete subclass of
 * {@link Calendar}
 * and provides the standard calendar used by most of the world.
 *
 * <p>
 * The standard (Gregorian) calendar has 2 eras, BC and AD.
 *
 * <p>
 * This implementation handles a single discontinuity, which corresponds by
 * default to the date the Gregorian calendar was instituted (October 15, 1582
 * in some countries, later in others).  The cutover date may be changed by the
 * caller by calling <code>setGregorianChange()</code>.
 *
 * <p>
 * Historically, in those countries which adopted the Gregorian calendar first,
 * October 4, 1582 was thus followed by October 15, 1582. This calendar models
 * this correctly.  Before the Gregorian cutover, <code>GregorianCalendar</code>
 * implements the Julian calendar.  The only difference between the Gregorian
 * and the Julian calendar is the leap year rule. The Julian calendar specifies
 * leap years every four years, whereas the Gregorian calendar omits century
 * years which are not divisible by 400.
 *
 * <p>
 * <code>GregorianCalendar</code> implements <em>proleptic</em> Gregorian and
 * Julian calendars. That is, dates are computed by extrapolating the current
 * rules indefinitely far backward and forward in time. As a result,
 * <code>GregorianCalendar</code> may be used for all years to generate
 * meaningful and consistent results. However, dates obtained using
 * <code>GregorianCalendar</code> are historically accurate only from March 1, 4
 * AD onward, when modern Julian calendar rules were adopted.  Before this date,
 * leap year rules were applied irregularly, and before 45 BC the Julian
 * calendar did not even exist.
 *
 * <p>
 * Prior to the institution of the Gregorian calendar, New Year's Day was
 * March 25. To avoid confusion, this calendar always uses January 1. A manual
 * adjustment may be made if desired for dates that are prior to the Gregorian
 * changeover and which fall between January 1 and March 24.
 *
 * </blockquote>
 *
 * @see          Calendar
 * @see          TimeZone
 * @version      1.47
 * @author David Goldsmith, Mark Davis, Chen-Lieh Huang, Alan Liu    
  * @category Signed
 */
public final class GregorianCalendar extends Calendar implements CalendarExtensions {
    /*
     * Implementation Notes
     *
     * The Julian day number, as used here, is a modified number which has its
     * onset at midnight, rather than noon.
     *
     * The epoch is the number of days or milliseconds from some defined
     * starting point. The epoch for java.util.Date is used here; that is,
     * milliseconds from January 1, 1970 (Gregorian), midnight UTC.  Other
     * epochs which are used are January 1, year 1 (Gregorian), which is day 1
     * of the Gregorian calendar, and December 30, year 0 (Gregorian), which is
     * day 1 of the Julian calendar.
     *
     * We implement the proleptic Julian and Gregorian calendars.  This means we
     * implement the modern definition of the calendar even though the
     * historical usage differs.  For example, if the Gregorian change is set
     * to new Date(Long.MIN_VALUE), we have a pure Gregorian calendar which
     * labels dates preceding the invention of the Gregorian calendar in 1582 as
     * if the calendar existed then.
     *
     * Likewise, with the Julian calendar, we assume a consistent 4-year leap
     * rule, even though the historical pattern of leap years is irregular,
     * being every 3 years from 45 BC through 9 BC, then every 4 years from 8 AD
     * onwards, with no leap years in-between.  Thus date computations and
     * functions such as isLeapYear() are not intended to be historically
     * accurate.
     *
     * Given that milliseconds are a long, day numbers such as Julian day
     * numbers, Gregorian or Julian calendar days, or epoch days, are also
     * longs. Years can fit into an int.
     */

    // Internal notes:
    // Calendar contains two kinds of time representations: current "time" in
    // milliseconds, and a set of time "fields" representing the current time.
    // The two representations are usually in sync, but can get out of sync
    // as follows.
    // 1. Initially, no fields are set, and the time is invalid.
    // 2. If the time is set, all fields are computed and in sync.
    // 3. If a single field is set, the time is invalid.
    // Recomputation of the time and fields happens when the object needs
    // to return a result to the user, or use a result for a computation.
    
    //////////////////////////////////////////
    //
    // private data members. 
    // NOTE: The order and number of these fields must not change as they are referred to from native methods
    //
    //////////////////////////////////////////
     
    private int _eraVal;
    private int _eraStamp;
    private int _yearVal;
    private int _yearStamp;
    private int _monthVal;
    private int _monthStamp;
    private int _dateVal;
    private int _dateStamp;
    private int _doyVal;
    private int _doyStamp;
    private int _dowVal;
    private int _dowStamp;
    private int _ampmVal;
    private int _ampmStamp;    
    private int _hourVal;
    private int _hourStamp;
    private int _hourOfDayVal;
    private int _hourOfDayStamp;
    private int _minVal;
    private int _minStamp;
    private int _secVal;
    private int _secStamp;
    private int _milliVal;
    private int _milliStamp;

    /**
     * True if then the value of <code>time</code> is valid.
     * The time is made invalid by a change to an item of <code>field[]</code>.
     * @see #time
     * @serial
     */
    private boolean       _isTimeSet; 

    /**
     * True if fields are in sync with the currently set time.
     * If false, then the next attempt to get the value of a field will
     * force a recomputation of all fields from the current value of
     * time.
     * @serial
     */
    private boolean       _areFieldsSet; 

    /**
     * The <code>TimeZone</code> used by this calendar. </code>Calendar</code>
     * uses the time zone data to translate between locale and GMT time.
     * @serial
     */
    private int             _zoneOffset;
    private int             _dstOffset;

    /**
     * The next available value for <code>stamp[]</code>, an internal array.
     * This actually should not be written out to the stream, and will probably
     * be removed from the stream in the near future.  In the meantime,
     * a value of <code>MINIMUM_USER_STAMP</code> should be used.
     *
     * NOTE:  a *lot* of operations will eventually cause wrap around of this value.
     */
    private int             _nextStamp = MINIMUM_USER_STAMP;
    
    /**
     * Gets the value for a given time field.
     * @param field the given time field.
     * @return the value for the given time field.
     */
    public final int internalGet(int field, boolean performComplete)
    {
        if( performComplete ) {
            complete();
        }
        switch (field) {
        case CalendarExtensions.ERA:
            return _eraVal;
        case YEAR:
            return _yearVal;
        case MONTH:
            return _monthVal;
        case DATE:
            return _dateVal;
        case CalendarExtensions.DAY_OF_YEAR:
            return _doyVal;
        case DAY_OF_WEEK:
            return _dowVal;
        case AM_PM:
            return _ampmVal;
        case HOUR:
            return _hourVal;
        case HOUR_OF_DAY:
            return _hourOfDayVal;
        case MINUTE:
            return _minVal;
        case SECOND:
            return _secVal;
        case MILLISECOND:
            return _milliVal;
        case CalendarExtensions.DST_OFFSET:
            return _dstOffset;
        }
        if( performComplete ) {
            throw new ArrayIndexOutOfBoundsException( field );
        }
        return 0;
    }

    /**
     * Sets the time field with the given value.
     * @param field the given time field.
     * @param value the value to be set for the given time field.
     */
    public final void internalSet(int field, int value)
    {
        if( _isTimeSet && !_areFieldsSet ) {
            computeFields(); // populate fields so that changes will be relative to the set time MKS0459447
        }
        _isTimeSet = false;
        _areFieldsSet = false; //forces recomputation back through to fields (since fields are lenient... i.e setting feb 30)
        int nextStamp = _nextStamp;
        
        switch (field) {
        case CalendarExtensions.ERA:
            _eraVal = value;
            _eraStamp = nextStamp;
            break;
        case YEAR:
            _yearVal = value;
            _yearStamp = nextStamp;
            break;
        case MONTH:
            _monthVal = value;
            _monthStamp = nextStamp;
            break;
        case DATE:
            _dateVal = value;
            _dateStamp = nextStamp;
            break;
        case CalendarExtensions.DAY_OF_YEAR:
            _doyVal = value;
            _doyStamp = nextStamp;
            break;
        case DAY_OF_WEEK:
            //Update the day of the month as well.
            add(DATE, value - _dowVal);
            _dowVal = value;
            _dowStamp = nextStamp;
            break;
        case AM_PM:
            _ampmVal = value;
            _ampmStamp = nextStamp;
            break;
        case HOUR:
            _hourVal = value;
            _hourStamp = nextStamp;
            break;
        case HOUR_OF_DAY:
            _hourOfDayVal = value;
            _hourOfDayStamp = nextStamp;
            break;
        case MINUTE:
            _minVal = value;
            _minStamp = nextStamp;
            break;
        case SECOND:
            _secVal = value;
            _secStamp = nextStamp;
            break;
        case MILLISECOND:
            _milliVal = value;
            _milliStamp = nextStamp;
            break;
        default:
            throw new ArrayIndexOutOfBoundsException( field );
        }
        _nextStamp = nextStamp + 1;
    }

    /**
     * RIM INTERNAL ONLY
     */
    public int getActualMaximum(int field) {
        int max = 0;
        
        switch(field) {
        case YEAR:
            max = 9999;
            break;
        case MONTH:
            max = 11;
            break;
        case DAY_OF_MONTH:
            if(isLeapYear(_yearVal)) {
                max = LEAP_MONTH_LENGTH[_monthVal];
            } else {
                max = MONTH_LENGTH[_monthVal];
            }
            break;
        case DAY_OF_WEEK:
            max = 6;
            break;
        case HOUR:
            max = 11;
            break;
        case AM_PM:
            max = 1;
            break;
        case HOUR_OF_DAY:
            max = 23;
            break;
        case MINUTE:
        case SECOND:
            max = 59;
            break;
        case MILLISECOND:
            max = 1000;
            break;
        }
        return max;
    }


    /**
     * RIM INTERNAL ONLY
     */
    public void add(int field, int amount) {
        if (amount == 0) return;   // Do nothing!
        complete();

        if (field == YEAR) {
            int year = _yearVal;
            if (_eraVal == CalendarExtensions.AD) {
                year += amount;
                if (year > 0)
                    this.set(YEAR, year);
                else { // year <= 0
                    this.set(YEAR, 1 - year);
                    // if year == 0, you get 1 BC
                    this.set(CalendarExtensions.ERA, CalendarExtensions.BC);
                }
            }
            else { // era == BC
                year -= amount;
                if (year > 0)
                    this.set(YEAR, year);
                else { // year <= 0
                    this.set(YEAR, 1 - year);
                    // if year == 0, you get 1 AD
                    this.set(CalendarExtensions.ERA, CalendarExtensions.AD);
                }
            }
            pinDayOfMonth();
        }
        else if (field == MONTH) {
            int month = _monthVal + amount;
            if (month >= 0) {
                set(YEAR, _yearVal + (month / 12));
                set(MONTH, (int) (month % 12));
            }
            else { // month < 0

                set(YEAR, _yearVal + ((month + 1) / 12) - 1);
                month %= 12;
                if (month < 0) month += 12;
                set(MONTH, JANUARY + month);
            }
            pinDayOfMonth();
        }
        else if (field == CalendarExtensions.ERA) {
            int era = _eraVal + amount;
            if (era < 0) era = 0;
            if (era > 1) era = 1;
            set(CalendarExtensions.ERA, era);
        }
        else {
            // We handle most fields here.  The algorithm is to add a computed amount
            // of millis to the current millis.  The only wrinkle is with DST -- if
            // the result of the add operation is to move from DST to Standard, or vice
            // versa, we need to adjust by an hour forward or back, respectively.
            // Otherwise you get weird effects in which the hour seems to shift when
            // you add to the DAY_OF_MONTH field, for instance.

            // We only adjust the DST for fields larger than an hour.  For fields
            // smaller than an hour, we cannot adjust for DST without causing problems.
            // for instance, if you add one hour to April 5, 1998, 1:00 AM, in PST,
            // the time becomes "2:00 AM PDT" (an illegal value), but then the adjustment
            // sees the change and compensates by subtracting an hour.  As a result the
            // time doesn't advance at all.

            long delta = amount;
            boolean adjustDST = true;

            switch (field) {
            case AM_PM:
                delta *= 12 * 60 * 60 * 1000; // 12 hrs
                break;

            case DATE: // synonym of DAY_OF_MONTH
            case CalendarExtensions.DAY_OF_YEAR:
            case DAY_OF_WEEK:
                delta *= 24 * 60 * 60 * 1000; // 1 day
                break;

            case HOUR_OF_DAY:
            case HOUR:
                delta *= 60 * 60 * 1000; // 1 hour
                adjustDST = false;
                break;

            case MINUTE:
                delta *= 60 * 1000; // 1 minute
                adjustDST = false;
                break;

            case SECOND:
                delta *= 1000; // 1 second
                adjustDST = false;
                break;

            case MILLISECOND:
                adjustDST = false;
                break;

            default:
                throw new IllegalArgumentException();
            }

            // Save the current DST state.
            long dst = 0;
            if (adjustDST)
            {
                dst = _dstOffset;
            }
            

            setTimeInMillis(time + delta); // Automatically computes fields if necessary
            
             if (adjustDST) {
                // Now do the DST adjustment alluded to above.
                // Only call setTimeInMillis if necessary, because it's an expensive call.
                dst -= _dstOffset;
                if (dst != 0) setTimeInMillis(time + dst);
            }
        }
    }

    /**
     * RIM INTERNAL ONLY
     */
    public void roll(int field, int amount) {
        if (amount == 0) return; // Nothing to do

        int min = 0, max = 0, gap;
        if (field >= 0) {
            complete();
            min = getMinimum(field);
            max = getMaximum(field);
        }

        switch (field) {
        case CalendarExtensions.ERA:
        case YEAR:
        case AM_PM:
        case MINUTE:
        case SECOND:
        case MILLISECOND:
            // These fields are handled simply, since they have fixed minima
            // and maxima.  The field DAY_OF_MONTH is almost as simple.  Other
            // fields are complicated, since the range within they must roll
            // varies depending on the date.
            break;

        case HOUR:
        case HOUR_OF_DAY:
            // Rolling the hour is difficult on the ONSET and CEASE days of
            // daylight savings.  For example, if the change occurs at
            // 2 AM, we have the following progression:
            // ONSET: 12 Std -> 1 Std -> 3 Dst -> 4 Dst
            // CEASE: 12 Dst -> 1 Dst -> 1 Std -> 2 Std
            // To get around this problem we don't use fields; we manipulate
            // the time in millis directly.
            {
                // Assume min == 0 in calculations below
                long start = getTimeInMillis();
                int oldHour = internalGet(field,false);
                int newHour = (oldHour + amount) % (max + 1);
                if (newHour < 0) {
                    newHour += max + 1;
                }
                setTimeInMillis(start + ONE_HOUR * (newHour - oldHour));
                return;
            }
        case MONTH:
            // Rolling the month involves both pinning the final value to [0, 11]
            // and adjusting the DAY_OF_MONTH if necessary.  We only adjust the
            // DAY_OF_MONTH if, after updating the MONTH field, it is illegal.
            // E.g., <jan31>.roll(MONTH, 1) -> <feb28> or <feb29>.
            {
                int mon = (_monthVal + amount) % 12;
                if (mon < 0) mon += 12;
                set(MONTH, mon);
                
                // Keep the day of month in range.  We don't want to spill over
                // into the next month; e.g., we don't want jan31 + 1 mo -> feb31 ->
                // mar3.
                // NOTE: We could optimize this later by checking for dom <= 28
                // first.  Do this if there appears to be a need. [LIU]
                int monthLen = monthLength(mon);
                int dom = _dateVal;
                if (dom > monthLen) set(DAY_OF_MONTH, monthLen);
                return;
            }

        case DAY_OF_MONTH:
            max = monthLength(_monthVal);
            break;
        case CalendarExtensions.DAY_OF_YEAR:
            {
                // Roll the day of year using millis.  Compute the millis for
                // the start of the year, and get the length of the year.
                long delta = amount * ONE_DAY; // Scale up from days to millis
                long min2 = time - (_doyVal - 1) * ONE_DAY;
                int yearLength = yearLength();
                time = (time + delta - min2) % (yearLength*ONE_DAY);
                if (time < 0) time += yearLength*ONE_DAY;
                setTimeInMillis(time + min2);
                return;
            }
        case DAY_OF_WEEK:
            {
                // Roll the day of week using millis.  Compute the millis for
                // the start of the week, using the first day of week setting.
                // Restrict the millis to [start, start+7days).
                long delta = amount * ONE_DAY; // Scale up from days to millis
                // Compute the number of days before the current day in this
                // week.  This will be a value 0..6.
                int leadDays = _dowVal - Calendar.SUNDAY; //assumes a sunday start week
                if (leadDays < 0) leadDays += 7;
                long min2 = time - leadDays * ONE_DAY;
                time = (time + delta - min2) % ONE_WEEK;
                if (time < 0) time += ONE_WEEK;
                setTimeInMillis(time + min2);
                return;
            }
        default:
            // These fields cannot be rolled
            throw new IllegalArgumentException();
        }

        // These are the standard roll instructions.  These work for all
        // simple cases, that is, cases in which the limits are fixed, such
        // as the hour, the month, and the era.
        gap = max - min + 1;
        int value = internalGet(field,false) + amount;
        value = (value - min) % gap;
        if (value < 0) value += gap;
        value += min;

        set(field, value);
    }

    /**
     * A bit is set for each field that has a minimum of 1 else the minimum is 0.
     */
    private static final int ACTUAL_MINIMUM_MASK =
        //0 << ERA
        +(1 << YEAR)
        //0 << MONTH
        +(1 << DAY_OF_MONTH)
        +(1 << DAY_OF_YEAR)
        +(1 << DAY_OF_WEEK)
        //0 << HOUR_OF_DAY
        //0 << MINUTE
        //0 << SECOND
        //0 << MILLISECOND
        ;
    
    /**
     * returns the minimum of a field given the values of the other fields.
     */
    public int getActualMinimum(int field) {
        return (ACTUAL_MINIMUM_MASK >> field) & 1;
        /*
        switch(field) {
            case ERA: return 0;
            case YEAR: return 1;
            case MONTH: return 0;
            case DAY_OF_MONTH: return 1;
            case DAY_OF_YEAR: return 1;
            case DAY_OF_WEEK: return 1;
            case HOUR_OF_DAY: return 0;
            case MINUTE: return 0;
            case SECOND: return 0;
            case MILLISECOND: return 0;
        }
        
        return 0;
        */
    }

    public void setTimeLong(long millis)
    {
        setTimeInMillis(millis);
        return;
    }
    public long getTimeLong()
    {
        return getTimeInMillis();
    }
    
    /**
     * Converts UTC as milliseconds to time field values.
     */
    protected void computeFields()
    {
        TimeZone tz = getTimeZone();
        int rawOffset = tz.getRawOffset();
        long localMillis = time + rawOffset;
        
        /* Check for very extreme values -- millis near Long.MIN_VALUE or
         * Long.MAX_VALUE.  For these values, adding the zone offset can push
         * the millis past MAX_VALUE to MIN_VALUE, or vice versa.  This produces
         * the undesirable effect that the time can wrap around at the ends,
         * yielding, for example, a Date(Long.MAX_VALUE) with a big BC year
         * (should be AD).  Handle this by pinning such values to Long.MIN_VALUE
         * or Long.MAX_VALUE. - liu 8/11/98 bug 4149677 */
        if (time > 0 && localMillis < 0 && rawOffset > 0) {
            localMillis = Long.MAX_VALUE;
        } else if (time < 0 && localMillis > 0 && rawOffset < 0) {
            localMillis = Long.MIN_VALUE;
        }

        // Time to fields takes the wall millis (Standard or DST, but won't be correct for DST in the first part 
        //of the day (where dst can move you to another day) this is handled below).
        int millisInDay = timeToDateFields(localMillis);


        // Call getOffset() to get the TimeZone offset.  The millisInDay value must
        // be standard local millis.
        int dstOffset = tz.getOffset(internalGetEra(),
            _yearVal,
            _monthVal,_dateVal,
            _dowVal,
            millisInDay) - rawOffset;
            
        _dstOffset = dstOffset;
        _zoneOffset = rawOffset;
            

        // Adjust our millisInDay for DST, if necessary.
        millisInDay += dstOffset;

        // If DST has pushed us into the next day, we must call timeToFields() again.
        // This happens in DST between 12:00 am and 1:00 am every day.  The call to
        // timeToFields() will give the wrong day, since the Standard time is in the
        // previous day.
        if (millisInDay >= ONE_DAY) {
            long dstMillis = localMillis + dstOffset;
            // As above, check for and pin extreme values
            if (localMillis > 0 && dstMillis < 0 && dstOffset > 0) {
                dstMillis = Long.MAX_VALUE;
            } else if (localMillis < 0 && dstMillis > 0 && dstOffset < 0) {
                dstMillis = Long.MIN_VALUE;
            }
            millisInDay = timeToDateFields(dstMillis);
        }


        timeToRemainingFields(millisInDay);
    }
    
    /**
     * Overrides Calendar
     * Converts time field values to UTC as milliseconds.
     * @exception IllegalArgumentException if any fields are invalid.
     */
    protected void computeTime()
    {
        int normalizedMillisInDay;        
        TimeZone tz = getTimeZone();
        
        // Compute the time zone offset and DST offset.  There are two potential
        // ambiguities here.  We'll assume a 2:00 am (wall time) switchover time
        // for discussion purposes here.
        // 1. The transition into DST.  Here, a designated time of 2:00 am - 2:59 am
        //    can be in standard or in DST depending.  However, 2:00 am is an invalid
        //    representation (the representation jumps from 1:59:59 am Std to 3:00:00 am DST).
        //    We assume standard time.
        // 2. The transition out of DST.  Here, a designated time of 1:00 am - 1:59 am
        //    can be in standard or DST.  Both are valid representations (the rep
        //    jumps from 1:59:59 DST to 1:00:00 Std).
        //    Again, we assume standard time.
        // We use the TimeZone object, unless the user has explicitly set the ZONE_OFFSET
        // or DST_OFFSET fields; then we use those fields.

        
        int zoneOffset = tz.getRawOffset(); 


        normalizedMillisInDay  = fieldsToLocalTime();
        //time is set to local standard time

        


        int dstOffset = 0;

        //can use all these field since the computeLocalStandardTimeFromFields calls timeToFields 
            //to ensure the fields are all correct
            
        dstOffset = tz.getOffset(_eraVal,
                                    _yearVal,
                                    _monthVal,
                                    _dateVal,
                                    _dowVal,
                                    normalizedMillisInDay) -
            zoneOffset;
        // Note: Because we pass in wall millisInDay, rather than
        // standard millisInDay, we interpret "1:00 am" on the day
        // of cessation of DST as "1:00 am Std" (assuming the time
        // of cessation is 2:00 am).
        
        _dstOffset = dstOffset;
        _zoneOffset = zoneOffset;

        // Store our final computed GMT time, with timezone adjustments.
        time = time - zoneOffset - dstOffset;
    }

    /**
     * Gets this Calendar's current time as a long.
     * @return the current time as UTC milliseconds from the epoch.
     */
    protected long getTimeInMillis() {
        if (!_isTimeSet)
        {
           updateTime();
        }
        return time;
    }
    
    /**
     * Sets this Calendar's current time from the given long value.
     * @param date the new time in UTC milliseconds from the epoch.
     */    
    protected void setTimeInMillis( long millis ) {
        _isTimeSet = true;
        time = millis;
        computeFields();
        _areFieldsSet = true;
    }

    /**
     * Sets the time zone with the given time zone value.
     * @param value the given time zone.
     */
    public void setTimeZone(TimeZone value)
    {
        if( getTimeZone() != value ) {
            super.setTimeZone( value );
            _areFieldsSet = false; //forces recomputation of fields on next get() operation
        }
    }

    /**
     * Recompute the time and update the status fields isTimeSet
     * and areFieldsSet.  Callers should check isTimeSet and only
     * call this method if isTimeSet is false.
     */
    private void updateTime() {
        computeTime();
        // we need to recompute the fields to normalize the values. 
        _areFieldsSet = false;
        _isTimeSet = true;
    }

    /**
     * Determines if the given time field has a value set.
     * @return true if the given time field has a value set; false otherwise.
     */
    private boolean isSet(int field)
    {
        switch (field) {
        case CalendarExtensions.ERA:
            return _eraStamp!= UNSET;
        case YEAR:
            return _yearStamp!= UNSET;
        case MONTH:
            return _monthStamp!= UNSET;
        case DATE:
            return _dateStamp!= UNSET;
        case CalendarExtensions.DAY_OF_YEAR:
            return _doyStamp!= UNSET;
        case DAY_OF_WEEK:
            return _dowStamp!= UNSET;
        case AM_PM:
            return _ampmStamp!= UNSET;
        case HOUR:
            return _hourStamp!= UNSET;
        case HOUR_OF_DAY:
            return _hourOfDayStamp!= UNSET;
        case MINUTE:
            return _minStamp!= UNSET;
        case SECOND:
            return _secStamp!= UNSET;
        case MILLISECOND:
            return _milliStamp!= UNSET;
        }
        throw new IllegalArgumentException();
    }


    /**
     * Fills in any unset fields in the time field list.
     */
    private void complete()
    {
        if (!_isTimeSet)
        {
            updateTime();
        }
        if (!_areFieldsSet) {
            computeFields(); // fills in unset fields
            _areFieldsSet = true;
        }
    }

    /**
     * Determines if the given year is a leap year. Returns true if the
     * given year is a leap year.
     * @param year the given year.
     * @return true if the given year is a leap year; false otherwise.
     */
    private boolean isLeapYear(int year) {
        return year >= GREGORIAN_CUTOVER_YEAR ?
            ((year%4 == 0) && ((year%100 != 0) || (year%400 == 0))) : // Gregorian
            (year%4 == 0); // Julian
    }


    private final int monthLength(int month, int year) {
        return isLeapYear(year) ? LEAP_MONTH_LENGTH[month] : MONTH_LENGTH[month];
    }

    private final int monthLength(int month) {
        int year = _yearVal;
        if (internalGetEra() == CalendarExtensions.BC) {
            year = 1-year;
        }
        return monthLength(month, year);
    }

    private final int yearLength() {
        return isLeapYear(_yearVal) ? 366 : 365;
    }

    /**
     * After adjustments such as add(MONTH), add(YEAR), we don't want the
     * month to jump around.  E.g., we don't want Jan 31 + 1 month to go to Mar
     * 3, we want it to go to Feb 28.  Adjustments which might run into this
     * problem call this method to retain the proper month.
     */
    private final void pinDayOfMonth() {
        int monthLen = monthLength(_monthVal);
        int dom = _dateVal;
        if (dom > monthLen) set(DAY_OF_MONTH, monthLen);
    }


    /**
     * Return the ERA.  We need a special method for this because the
     * default ERA is AD, but a zero (unset) ERA is BC.
     */
    private final int internalGetEra() {
        return isSet(CalendarExtensions.ERA) ? _eraVal : CalendarExtensions.AD;
    }        

    /**
     * Returns minimum value for the given field.
     * e.g. for Gregorian DAY_OF_MONTH, 1
     */
    private int getMinimum(int field) {
        return MIN_VALUES[field];
    }

    /**
     * Returns maximum value for the given field.
     * e.g. for Gregorian DAY_OF_MONTH, 31
     */
    private int getMaximum(int field) {
        return MAX_VALUES[field];
    }
    
    // Special values of stamps
    private static final int        UNSET = 0;
    private static final int        INTERNALLY_SET = 1;
    private static final int        MINIMUM_USER_STAMP = 2;

    private static final int GREGORIAN_CUTOVER_YEAR = 1582;
    
    private static final int MONTH_LENGTH[]
        = {31,28,31,30,31,30,31,31,30,31,30,31}; // 0-based
    private static final int LEAP_MONTH_LENGTH[]
        = {31,29,31,30,31,30,31,31,30,31,30,31}; // 0-based

    // Useful millisecond constants.  Although ONE_DAY and ONE_WEEK can fit
    // into ints, they must be longs in order to prevent arithmetic overflow
    // when performing operations (bug 4173516).
    private static final int  ONE_SECOND = 1000;
    private static final int  ONE_MINUTE = 60*ONE_SECOND;
    private static final int  ONE_HOUR   = 60*ONE_MINUTE;
    private static final long ONE_DAY    = 24*ONE_HOUR;
    private static final long ONE_WEEK   = 7*ONE_DAY;

    /*
     * 
     * <pre>
     *                            Greatest       Least 
     * Field name        Minimum   Minimum     Maximum     Maximum
     * ----------        -------   -------     -------     -------
     * ERA                     0         0           1           1
     * YEAR                    1         1   292269054   292278994
     * MONTH                   0         0          11          11
     * WEEK_OF_YEAR            1         1          52          53 //NOT USED 
     * WEEK_OF_MONTH           0         0           4           6 //NOT USED 
     * DAY_OF_MONTH            1         1          28          31
     * DAY_OF_YEAR             1         1         365         366
     * DAY_OF_WEEK             1         1           7           7
     * DAY_OF_WEEK_IN_MONTH   -1        -1           4           6 //NOT USED 
     * AM_PM                   0         0           1           1
     * HOUR                    0         0          11          11
     * HOUR_OF_DAY             0         0          23          23
     * MINUTE                  0         0          59          59
     * SECOND                  0         0          59          59
     * MILLISECOND             0         0         999         999
     * ZONE_OFFSET           -12*      -12*         12*         12*
     * DST_OFFSET              0         0           1*          1*
     * </pre>
     * (*) In units of one-hour
     */
     /* These internal static members must align with the calendar constants*/
    private static final int MIN_VALUES[] = {
        0,1,0,1,0,1,1,1,-1,0,0,0,0,0,0,-12*ONE_HOUR,0
    };
    private static final int MAX_VALUES[] = {
        1,292278994,11,53,6,31,366,7,6,1,11,23,59,59,999,12*ONE_HOUR,1*ONE_HOUR
    };
    
    /**
     * computes the the standard local time 
     * since we are lenient it then calls back through to timeToDateFields
     * method returns normalized millis in day and leaves the time field
     * set to the local standard time from fields
     *
     */ 
    private final native int fieldsToLocalTime();
    
    /**
     * Convert the time as milliseconds to the date fields.  Millis must be
     * given as local wall millis to get the correct local day.  For example,
     * if it is 11:30 pm Standard, and DST is in effect, the correct DST millis
     * must be passed in to get the right date.
     * <p>
     * Returns millis in the day as part of the calculation
     * <p>
     * Fields that are completed by this method: ERA, YEAR, MONTH, DATE,
     * DAY_OF_WEEK, DAY_OF_YEAR,
     * @param theTime the time in wall millis (either Standard or DST),
     * whichever is in effect
     */
    private final native int timeToDateFields(long theTime);
    
    /**
     * Convert the time as milliseconds to the fields <i>other than</i> the date fields.  
     * Millis must be
     * given as local wall millis to get the correct local day.  For example,
     * if it is 11:30 pm Standard, and DST is in effect, the correct DST millis
     * must be passed in to get the right date.
     * <p>
     * This method will also flag ALL the fields (including date) as being internally set.
     * <p>
     * Fields that are completed by this method: ERA, YEAR, MONTH, DATE,
     * DAY_OF_WEEK, DAY_OF_YEAR,
     * @param theTime the time in wall millis (either Standard or DST),
     * whichever is in effect
     */
    private final native void timeToRemainingFields(int millisInDay); 


    /**
     * Converts this <code>Date</code> object to a <code>String</code>
     * of the form:
     * <blockquote><pre>
     * dow mon dd hh:mm:ss zzz yyyy</pre></blockquote>
     * where:<ul>
     * <li><tt>dow</tt> is the day of the week (<tt>Sun, Mon, Tue, Wed,
     *     Thu, Fri, Sat</tt>).
     * <li><tt>mon</tt> is the month (<tt>Jan, Feb, Mar, Apr, May, Jun,
     *     Jul, Aug, Sep, Oct, Nov, Dec</tt>).
     * <li><tt>dd</tt> is the day of the month (<tt>01</tt> through
     *     <tt>31</tt>), as two decimal digits.
     * <li><tt>hh</tt> is the hour of the day (<tt>00</tt> through
     *     <tt>23</tt>), as two decimal digits.
     * <li><tt>mm</tt> is the minute within the hour (<tt>00</tt> through
     *     <tt>59</tt>), as two decimal digits.
     * <li><tt>ss</tt> is the second within the minute (<tt>00</tt> through
     *     <tt>61</tt>, as two decimal digits.
     * <li><tt>zzz</tt> is the time zone (and may reflect daylight savings
     *     time). If time zone information is not available,
     *     then <tt>zzz</tt> is empty - that is, it consists
     *     of no characters at all.
     * <li><tt>yyyy</tt> is the year, as four decimal digits.
     * </ul>
     *
     * @return  a string representation of this date.
     */
    public static String toString(Calendar calendar) {
        // Printing in the absence of a Calendar
        // implementation class is not supported
        if (calendar == null) {
            return "Thu Jan 01 00:00:00 UTC 1970";
        }
        DateFormatSymbols dfs = DateFormatSymbols.getInstance();
        String months[] = dfs.getShortMonths();
        String days[] = dfs.getShortWeekdays(); 

        int dow = calendar.get(Calendar.DAY_OF_WEEK);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour_of_day = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);
        int year = calendar.get(Calendar.YEAR);

        String yr = Integer.toString(year);

        TimeZone zone = calendar.getTimeZone();
        String zoneID = zone.getID();
        if (zoneID == null) zoneID = "";

        // The total size of the string buffer
        // 3+1+3+1+2+1+2+1+2+1+2+1+zoneID.length+1+yr.length
        //  = 21 + zoneID.length + yr.length
        StringBuffer sb = new StringBuffer(25 + zoneID.length() + yr.length());

        sb.append(days[dow-1]).append(' ');
        sb.append(months[month]).append(' ');
        appendTwoDigits(sb, day).append(' ');
        appendTwoDigits(sb, hour_of_day).append(':');
        appendTwoDigits(sb, minute).append(':');
        appendTwoDigits(sb, seconds).append(' ');
        if (zoneID.length() > 0) sb.append(zoneID).append(' ');
        appendFourDigits(sb, year);

        return sb.toString();
    }

    private static final StringBuffer appendFourDigits(StringBuffer sb, int number) {
        if (number >= 0 && number < 1000) {
            sb.append('0');
            if (number < 100) {
                sb.append('0');
            }
            if (number < 10) {
                sb.append('0');
            }
        }
        return sb.append(number);
    }

    private static final StringBuffer appendTwoDigits(StringBuffer sb, int number) {
        if (number < 10) {
            sb.append('0');
        }
        return sb.append(number);
    }
}
