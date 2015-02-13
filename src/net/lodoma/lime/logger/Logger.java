package net.lodoma.lime.logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;

public class Logger
{
    private int minimum = LogLevel.FINEST.ordinal();
    
    public synchronized void setMinimumLevel(LogLevel minimum)
    {
        this.minimum = minimum.ordinal();
    }
    
    public synchronized void F(String message)
    {
        log(LogLevel.FINEST, message, getStackTraceElement(2));
    }
    
    public synchronized void D(String message)
    {
        log(LogLevel.DEBUG, message, getStackTraceElement(2));
    }
    
    public synchronized void I(String message)
    {
        log(LogLevel.INFO, message, getStackTraceElement(2));
    }
    
    public synchronized void N(String message)
    {
        log(LogLevel.NORMAL, message, getStackTraceElement(2));
    }
    
    public synchronized void W(String message)
    {
        log(LogLevel.WARNING, message, getStackTraceElement(2));
    }
    
    public synchronized void C(String message)
    {
        log(LogLevel.CRITICAL, message, getStackTraceElement(2));
    }
    
    public synchronized void log(LogLevel level, String message)
    {
        log(level, message, getStackTraceElement(2));
    }
    
    public synchronized void log(Throwable throwable)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        
        log(LogLevel.CRITICAL, sw.toString(), getStackTraceElement(2));
    }
    
    private void log(LogLevel level, String message, StackTraceElement ste)
    {
        if (level.ordinal() < minimum)
            return;
        
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        
        String timeAndDate = String.format("%04d/%02d/%02d %02d:%02d:%02d", year, month, day, hour, minute, second);
        String caller = String.format("%s", ste.getClassName() + "." + ste.getMethodName() + "()");
        String head = String.format("%-8s %s %s", level.name(), timeAndDate, caller);
        
        System.err.printf("%s\n         %s\n", head, message);
    }
    
    private StackTraceElement getStackTraceElement(int off)
    {
        return Thread.currentThread().getStackTrace()[1 + off];
    }
}
