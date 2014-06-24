package net.lodoma.lime.shader;

import java.util.HashMap;
import java.util.Map;

public class ProgramPool
{
    private Map<String, Program> programs = new HashMap<String, Program>();
    
    public void addProgram(String programKey, Program program)
    {
        if(programs.containsKey(programKey))
            throw new DuplicateProgramException(programKey);
        programs.put(programKey, program);
    }
    
    public Program getProgram(String programKey)
    {
        if(!programs.containsKey(programKey))
            throw new NullPointerException(programKey);
        return programs.get(programKey);
    }
    
    public void removeProgram(String programKey)
    {
        if(!programs.containsKey(programKey))
            throw new NullPointerException(programKey);
        programs.remove(programKey);
    }
    
    public boolean containsProgram(String programKey)
    {
        return programs.containsKey(programKey);
    }
}
