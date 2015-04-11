package net.lodoma.lime.localization;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.lodoma.lime.Lime;
import net.lodoma.lime.rui.RUI;
import net.lodoma.lime.util.OsHelper;

public class Language
{
    private static Language currentLanguage;
    private static String currentLanguageName;
    private static List<String> langNames = new ArrayList<String>();
    private static Map<String, Language> languages = new HashMap<String, Language>();
    
    public static void loadLangFiles()
    {
        File[] files = new File(OsHelper.JARPATH + "lang").listFiles();
        for (File file : files)
            if (file.getName().endsWith(".lang"))
            {
                Language language = new Language(file);
                String langname = language.names.get("langname");
                if (langname == null)
                {
                    Lime.LOGGER.C("\"langname\" not found in language file \"" + file.getPath() + "\"");
                    Lime.forceExit(null);
                }
                languages.put(langname, language);
                langNames.add(langname);
            }
        Collections.sort(langNames);
    }
    
    public static List<String> getLanguageNameList()
    {
        return new ArrayList<String>(langNames);
    }
    
    public static void selectLanguage(String langname)
    {
        currentLanguage = languages.get(langname);
        currentLanguageName = langname;
        if (currentLanguage == null)
        {
            Lime.LOGGER.C("\"" + langname + "\" not found");
            Lime.forceExit(null);
        }
        RUI.reload();
    }
    
    public static String getSelectedLanguageName()
    {
        return currentLanguageName;
    }
    
    public static String getLocalized(String unlocalized)
    {
        if (currentLanguage == null)
        {
            Lime.LOGGER.C("Language is not selected");
            Lime.forceExit(null);
        }
        
        String localizedName = currentLanguage.names.get(unlocalized);
        return (localizedName == null) ? unlocalized : localizedName;
    }
    
    public static char[] getCharset()
    {
        if (currentLanguage == null)
        {
            Lime.LOGGER.C("Language is not selected");
            Lime.forceExit(null);
        }
        
        return currentLanguage.charset;
    }
    
    private char[] charset;
    private Map<String, String> names = new HashMap<String, String>();
    
    public Language(File langFile)
    {
        try(FileReader fileReader = new FileReader(langFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader))
        {
            Set<Character> charsetSet = new HashSet<Character>();
            
            String line;
            while((line = bufferedReader.readLine()) != null)
            {
                line = line.trim();
                if (line.length() == 0)
                    continue;
                
                int index = line.indexOf('=');
                if (index == -1)
                    throw new IOException("Invalid language file format");
                String unlocalized = line.substring(0, index).trim();
                String localized = line.substring(index + 1).trim();
                for (int i = 0; i < localized.length(); i++)
                {
                    char c = localized.charAt(i);
                    if (c > 127) charsetSet.add(c);
                }
                names.put(unlocalized, localized);
            }
            
            if (charsetSet.size() > 0)
            {
                charset = new char[charsetSet.size()];
                int i = 0;
                for (char c : charsetSet)
                    charset[i++] = c;
            }
        }
        catch (IOException e)
        {
            Lime.LOGGER.C("Failed to load language file \"" + langFile.getPath() + "\"");
            Lime.LOGGER.log(e);
            Lime.forceExit(e);
        }
    }
}
