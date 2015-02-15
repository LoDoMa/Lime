package net.lodoma.lime;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.lodoma.lime.logger.Logger;
import net.lodoma.lime.util.OsHelper;

public class Lime
{
    public static final Logger LOGGER = new Logger();
    
    public static void forceExit()
    {
        System.err.printf("Lime is about to forcefully exit.\nBut first; an error report.\n");
        
        File file = null;
        PrintStream crashlogStream = System.err;
        long time = System.nanoTime();
        
        try
        {
            crashlogStream = new PrintStream(file = new File(OsHelper.JARPATH + "LimeCrashlog__" + time + ".txt"));
        }
        catch (IOException e)
        {
            
        }

        writeCrashlogHeader(crashlogStream, time);
        writeCrashlogStacktrace(crashlogStream);
        writeCrashlogLogger(crashlogStream);
        
        if (crashlogStream != System.err)
            System.err.printf("Error report at file " + file.getPath() + "\n");
        
        crashlogStream.close();
        
        System.exit(1);
    }
    
    private static void writeCrashlogHeader(PrintStream crashlogStream, long time)
    {
        writeCrashlogLine("Hi there! It looks like the game crashed :(", crashlogStream);
        if (crashlogStream == System.err)
            writeCrashlogLine("It also looks like we failed to write this into a file...", crashlogStream);
        
        writeCrashlogLine("", crashlogStream);
        writeCrashlogLine("Anyway, we've managed to get some data about the crash.", crashlogStream);
        writeCrashlogLine("This is the full report:", crashlogStream);
        writeCrashlogLine("", crashlogStream);
        writeCrashlogLine("", crashlogStream);
        writeCrashlogLine("  --- Lime error report " + time + " --- ", crashlogStream);
    }
    
    private static void writeCrashlogStacktrace(PrintStream crashlogStream)
    {
        Map<Thread, StackTraceElement[]> steMap = Thread.getAllStackTraces();
        
        Set<Entry<Thread, StackTraceElement[]>> steSet = steMap.entrySet();
        for (Entry<Thread, StackTraceElement[]> steEntry : steSet)
        {
            Thread thread = steEntry.getKey();
            StackTraceElement[] steList = steEntry.getValue();
            
            writeCrashlogLine("", crashlogStream);
            writeCrashlogLine("Stack trace, thread " + thread.getId() + "/" + thread.getName() + ": " + ((thread.getId() == Thread.currentThread().getId()) ? "[current]" : ""), crashlogStream);
            for (StackTraceElement elem : steList)
                writeCrashlogLine(String.format("    %s", elem.getClassName() + "." + elem.getMethodName() + "() : line " + elem.getLineNumber()), crashlogStream);
        }
        writeCrashlogLine("", crashlogStream);
    }
    
    private static void writeCrashlogLogger(PrintStream crashlogStream)
    {
        LinkedList<String> list = LOGGER.getList();
        writeCrashlogLine("Lime log; the last " + list.size() + " log elements", crashlogStream);
        for (String r : list)
        {
            String[] rs = r.split("\n");
            for (String rr : rs)
                writeCrashlogLine("LOG:    " + rr, crashlogStream);
        }
        writeCrashlogLine("", crashlogStream);
    }
    
    private static void writeCrashlogLine(String line, PrintStream crashlogStream)
    {
        crashlogStream.println(String.format("%1$-140s", line));
    }
}
