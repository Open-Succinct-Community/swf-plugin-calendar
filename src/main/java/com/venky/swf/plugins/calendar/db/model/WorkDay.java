package com.venky.swf.plugins.calendar.db.model;

import com.venky.swf.db.annotations.column.IS_NULLABLE;
import com.venky.swf.db.annotations.column.UNIQUE_KEY;
import com.venky.swf.db.annotations.column.ui.HIDDEN;
import com.venky.swf.db.annotations.column.validations.Enumeration;
import com.venky.swf.db.model.Model;

public interface WorkDay extends Model{
	@UNIQUE_KEY
	@IS_NULLABLE(false)
	@HIDDEN
	public Integer getWorkCalendarId();
	public void setWorkCalendarId(Integer id);
	public WorkCalendar getWorkCalendar();

	public static final String DOWS = "SUNDAY,MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY" ;
	@Enumeration(DOWS)
	@UNIQUE_KEY
	public String getDayOfWeek();
	public void setDayOfWeek(String dow);
	
	
	public Integer getWorkSlotId();
	public void setWorkSlotId(Integer id);
	public WorkSlot getWorkSlot();

}
