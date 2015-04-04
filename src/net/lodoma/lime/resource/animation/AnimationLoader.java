package net.lodoma.lime.resource.animation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.jse.JseMathLib;

import net.lodoma.lime.util.OsHelper;
import net.lodoma.lime.util.Pair;

public class AnimationLoader
{
    public static Animation load(String animationName) throws IOException
    {
        File animationFile = new File(OsHelper.JARPATH + "res/textures/" + animationName);
        if (animationFile.getName().endsWith(".san"))
            return loadSkeletalAnimationOLD(animationFile);
        else if (animationFile.getName().endsWith(".lua"))
            return loadSkeletalAnimation(animationFile);
        throw new UnsupportedOperationException("Animation file " + animationFile.getPath() + " not supported.");
    }
    
    private static SkeletalAnimation loadSkeletalAnimation(File animationFile) throws IOException
    {
        Globals globals = new Globals();
        globals.load(new PackageLib());
        globals.load(new JseMathLib());
        LoadState.install(globals);
        LuaC.install(globals);
        
        LuaValue chunk = globals.load(new FileReader(animationFile), "SkeletalAnimation");
        chunk.call();
        
        SkeletalAnimation animation = new SkeletalAnimation();

        LuaTable rootT = globals.get("root").checktable();
        LuaTable animationT = globals.get("animation").checktable();

        loadAnimation(animation, animationT, rootT);
        animation.root = loadBone(animation, rootT);
        
        return animation;
    }
    
    @SuppressWarnings("unchecked")
    private static Bone loadBone(SkeletalAnimation animation, LuaTable boneT)
    {
        Bone bone = new Bone();
        bone.length = boneT.get("length").checknumber().tofloat();
        bone.width = boneT.get("width").checknumber().tofloat();
        
        LuaValue childrenV = boneT.get("children");
        if (!childrenV.isnil())
        {
            LuaTable childrenT = childrenV.checktable();
            
            Varargs childV = LuaValue.NONE;
            while (!(childV = childrenT.next(childV.arg(1))).isnil(1))
                bone.children.add(loadBone(animation, childV.checktable(2)));
        }
        
        LuaValue listV = boneT.get("__LIME_KEYFRAME_LIST__");
        
        List<Pair<Float, Float>> list = null;
        if (!listV.isnil())
            list = (List<Pair<Float, Float>>) listV.checkuserdata();
        
        if (list == null)
        {
            bone.frameDurations = new float[] { animation.totalDuration };
            bone.keyFrames = new float [] { 0.0f };
        }
        else
        {
            list.sort(new Comparator<Pair<Float, Float>>()
            {
                @Override
                public int compare(Pair<Float, Float> o1, Pair<Float, Float> o2)
                {
                    if (o1.first < o2.first) return -1;
                    if (o1.first > o2.first) return 1;
                    if (o1.second < o2.second) return -1;
                    if (o1.second > o2.second) return 1;
                    return 0;
                }
            });

            bone.frameDurations = new float[list.size()];
            bone.keyFrames = new float[list.size()];
            
            for (int i = 0; i < list.size(); i++)
            {
                float duration = 0.0f;
                if (i < (list.size() - 1))
                    duration = list.get(i + 1).first - list.get(i).first;
                else
                    duration = animation.totalDuration - list.get(i).first;
                bone.frameDurations[i] = duration;
                bone.keyFrames[i] = list.get(i).second;
            }
        }
        
        return bone;
    }

    @SuppressWarnings("unchecked")
    private static void loadAnimation(SkeletalAnimation animation, LuaTable animationT, LuaTable rootT)
    {
        animation.totalDuration = animationT.get("duration").checknumber().tofloat();
        
        Varargs keyframesV = animationT.get("keyframes").checktable().unpack();
        for (int i = 1; i <= keyframesV.narg(); i++)
        {
            LuaTable keyframeT = keyframesV.checktable(i);
            
            LuaTable body = keyframeT.get("body").checktable();
            float time = keyframeT.get("time").checknumber().tofloat();
            float angle = keyframeT.get("angle").checknumber().tofloat();
            
            LuaValue listV = body.get("__LIME_KEYFRAME_LIST__");
            if (listV.isnil())
            {
                listV = LuaValue.userdataOf(new ArrayList<Pair<Float, Float>>());
                body.set("__LIME_KEYFRAME_LIST__", listV);
            }
            
            List<Pair<Float, Float>> list = (List<Pair<Float, Float>>) listV.checkuserdata();
            list.add(new Pair<Float, Float>(time, angle));
        }
    }
    
    private static SkeletalAnimation loadSkeletalAnimationOLD(File animationFile) throws IOException
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
