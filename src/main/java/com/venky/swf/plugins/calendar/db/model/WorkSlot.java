package com.venky.swf.plugins.calendar.db.model;

import com.venky.swf.db.annotations.column.IS_NULLABLE;
import com.venky.swf.db.annotations.column.UNIQUE_KEY;
import com.venky.swf.db.annotations.column.ui.HIDDEN;
import com.venky.swf.db.annotations.column.ui.WATERMARK;
import com.venky.swf.db.annotations.column.validations.RegEx;
import com.venky.swf.db.model.Model;

public interface WorkSlot extends Model { 
	@HIDDEN
	@UNIQUE_KEY
	@IS_NULLABLE(false)
	public Long getWorkCalendarId();
	public void setWorkCalendarId(Long id);
	public WorkCalendar getWorkCalendar();
	
	@UNIQUE_KEY
	public String getName();
	public void setName(String Name);

	@RegEx("([0-1][0-9]|[2][0-3]):[0-5][0-9]")
	@WATERMARK("HH:mm")
	@IS_NULLABLE(false)
	public String getStartTime();
	public void setStartTime(String time);
	
	@RegEx("([0-1][0-9]|[2][0-3]):[0-5][0-9]")
	@WATERMARK("HH:mm")
	@IS_NULLABLE(false)
	public String getEndTime();
	public void setEndTime(String time);
	
}
