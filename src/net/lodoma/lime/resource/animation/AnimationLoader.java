package net.lodoma.lime.resource.animation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import net.lodoma.lime.util.OsHelper;

public class AnimationLoader
{
    public static Animation load(String animationName) throws IOException
    {
        File animationFile = new File(OsHelper.JARPATH + "res/textures/" + animationName);
        if (animationFile.getName().endsWith(".san"))
            return loadSkeletalAnimation(animationFile);
        throw new UnsupportedOperationException("Animation file " + animationFile.getPath() + " not supported.");
    }
    
    private static SkeletalAnimation loadSkeletalAnimation(File animationFile) throws IOException
    {
        SkeletalAnimation animation = new SkeletalAnimation();
        
        try (Scanner scanner = new Scanner(new FileInputStream(animationFile)))
        {
            // Apparently, Scanner#nextFloat() works different between OracleJDK and OpenJDK
            // Parsing the float manually fixes that.
            animation.totalDuration = Float.parseFloat(scanner.next());
            
            Map<String, Bone> bones = new HashMap<String, Bone>();
            
            while (scanner.hasNext())
            {
                String objectName = scanner.next();
                
                Bone newBone = new Bone();
                if (objectName.equals("ROOT"))
                {
                    animation.root = newBone;
                }
                else
                {
                    String parentName = scanner.next();
                    bones.get(parentName).children.add(newBone);
                }
                bones.put(objectName, newBone);
                
                newBone.length = Float.parseFloat(scanner.next());
                newBone.width = Float.parseFloat(scanner.next());
                
                int frameCount = scanner.nextInt();
                newBone.keyFrames = new float[frameCount];
                newBone.frameDurations = new float[frameCount];
                
                for (int i = 0; i < frameCount; i++)
                {
                    newBone.keyFrames[i] = Float.parseFloat(scanner.next());
                    newBone.frameDurations[i] = Float.parseFloat(scanner.next());
                }
            }
        }
        
        return animation;
    }
}
