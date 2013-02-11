package com.venky.swf.plugins.calendar.db.model;

import java.sql.Date;
import java.util.List;

import com.venky.swf.db.annotations.column.IS_NULLABLE;
import com.venky.swf.db.annotations.column.IS_VIRTUAL;
import com.venky.swf.db.annotations.column.UNIQUE_KEY;
import com.venky.swf.db.annotations.model.MENU;
import com.venky.swf.db.model.Model;

@MENU("Calendar")
public interface WorkCalendar extends Model{
	@UNIQUE_KEY
	public String getName();
	public void setName(String name);
	
	public Integer getParentWorkCalendarId();
	public void setParentWorkCalendarId(Integer id);
	public WorkCalendar getParentWorkCalendar();
	
	@IS_NULLABLE(false)
	public Date getStartDate();
	public void setStartDate(Date date);
	
	@IS_NULLABLE(false)
	public Date getEndDate();
	public void setEndDate(Date date);
	
	public List<WorkSlot> getWorkSlots();
	public List<WorkDay> getWorkDays();
	public List<SpecialWorkDay> getSpecialWorkDays();
	public List<SpecialNonWorkDay> getSpecialNonWorkDays();
	
	
	public List<WorkCalendar> getChildCalendars();
	public static enum DayType {
		NON_WORKING,
		WORKING
	}
	
	@IS_VIRTUAL
	public boolean isWorking(java.util.Date date);
	
	@IS_VIRTUAL
	public DayType getDayType(java.util.Date date);
	
	@IS_VIRTUAL
	public DayType getSpecialDayType(java.util.Date date);
	
	@IS_VIRTUAL
	public  DayType getDefaultDayType(java.util.Date date);
}
