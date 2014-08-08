package net.lodoma.lime.localization;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Language
{
    public Map<String, String> names;
    
    public Language(File langFile) throws IOException
    {
        names = new HashMap<String, String>();
        
        FileReader fileReader = new FileReader(langFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        
        String line;
        while((line = bufferedReader.readLine()) != null)
        {
            String[] split = line.split("->");
            String unlocalizedName = split[0].trim();
            String localizedName = split[1].trim();
            
            names.put(unlocalizedName, localizedName);
        }
        
        bufferedReader.close();
        fileReader.close();
    }
    
    public String getLocalizedName(String unlocalizedName)
    {
        String localizedName = names.get(unlocalizedName);
        if(localizedName == null)
            return unlocalizedName;
        return localizedName;
    }
}
