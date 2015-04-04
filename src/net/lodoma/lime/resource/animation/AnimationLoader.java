package net.lodoma.lime.resource.animation;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import net.lodoma.lime.util.Vector2;

public class AnimationLoader
{
    public static void load(Animation animation, String animationName) throws IOException
    {
        File animationFile = new File(OsHelper.JARPATH + "res/textures/" + animationName + ".lua");
        loadSkeletalAnimation(animation, animationFile);
    }
    
    private static void loadSkeletalAnimation(Animation animation, File animationFile) throws IOException
    {
        Globals globals = new Globals();
        globals.load(new PackageLib());
        globals.load(new JseMathLib());
        LoadState.install(globals);
        LuaC.install(globals);
        
        LuaValue chunk = globals.load(new FileReader(animationFile), "SkeletalAnimation");
        chunk.call();

        LuaTable rootT = globals.get("root").checktable();
        LuaTable animationT = globals.get("animation").checktable();

        loadAnimation(animation, animationT, rootT);
        animation.root = loadBone(animation, rootT);
    }
    
    @SuppressWarnings("unchecked")
    private static Bone loadBone(Animation animation, LuaTable boneT)
    {
        Bone bone = new Bone();
        bone.offset = new Vector2();
        LuaTable offsetT = boneT.get("offset").checktable();
        bone.offset.x = offsetT.get("x").checknumber().tofloat();
        bone.offset.y = offsetT.get("y").checknumber().tofloat();
        
        LuaValue textureV = boneT.get("texture");
        if (!textureV.isnil())
        {
            bone.textureName = textureV.checkstring().tojstring();
            bone.textureSize = new Vector2();
            LuaTable textureSizeT = boneT.get("textureSize").checktable();
            bone.textureSize.x = textureSizeT.get("x").checknumber().tofloat();
            bone.textureSize.y = textureSizeT.get("y").checknumber().tofloat();
            bone.textureOffset = new Vector2();
            LuaTable textureOffsetT = boneT.get("textureOffset").checktable();
            bone.textureOffset.x = textureOffsetT.get("x").checknumber().tofloat();
            bone.textureOffset.y = textureOffsetT.get("y").checknumber().tofloat();
        }
        
        LuaValue childrenV = boneT.get("children");
        if (!childrenV.isnil())
        {
            LuaTable childrenT = childrenV.checktable();
            
            Varargs childV = LuaValue.NONE;
            while (!(childV = childrenT.next(childV.arg(1))).isnil(1))
                bone.childrenFront.add(loadBone(animation, childV.checktable(2)));
        }
        
        LuaValue childrenBackV = boneT.get("childrenBack");
        if (!childrenBackV.isnil())
        {
            LuaTable childrenBackT = childrenBackV.checktable();
            
            Varargs childV = LuaValue.NONE;
            while (!(childV = childrenBackT.next(childV.arg(1))).isnil(1))
                bone.childrenBack.add(loadBone(animation, childV.checktable(2)));
        }
        
        LuaValue mapV = boneT.get("__LIME_KEYFRAME_MAP__");
        Map<String, List<Pair<Float, Float>>> map = null;
        if (!mapV.isnil())
            map = (Map<String, List<Pair<Float, Float>>>) mapV.checkuserdata();
        
        Set<String> animationNames = animation.totalDuration.keySet();
        for (String animationName : animationNames)
        {
            List<Pair<Float, Float>> list = null;
            if (map != null)
                list = map.get(animationName);
            
            if (map == null || list == null)
            {
                bone.frameDurations.put(animationName, new float[] { animation.totalDuration.get(animationName) });
                bone.keyFrames.put(animationName, new float [] { 0.0f });
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

                float[] frameDurationArray = new float[list.size()];
                float[] keyFrameArray = new float[list.size()];
                bone.frameDurations.put(animationName, frameDurationArray);
                bone.keyFrames.put(animationName, keyFrameArray);
                
                for (int i = 0; i < list.size(); i++)
                {
                    float duration = 0.0f;
                    if (i < (list.size() - 1))
                        duration = list.get(i + 1).first - list.get(i).first;
                    else
                        duration = animation.totalDuration.get(animationName) - list.get(i).first;
                    frameDurationArray[i] = duration;
                    keyFrameArray[i] = list.get(i).second;
                }
            }
        }
        
        return bone;
    }

    @SuppressWarnings("unchecked")
    private static void loadAnimation(Animation animation, LuaTable animationT, LuaTable rootT)
    {
        Varargs animationV = LuaValue.NONE;
        while (!(animationV = animationT.next(animationV.arg(1))).isnil(1))
        {
            String animName = animationV.checkstring(1).tojstring();
            LuaTable animT = animationV.checktable(2);
            
            float duration = animT.get("duration").checknumber().tofloat();
            animation.totalDuration.put(animName, duration);
            
            Varargs keyframesV = animT.get("keyframes").checktable().unpack();
            for (int i = 1; i <= keyframesV.narg(); i++)
            {
                LuaTable keyframeT = keyframesV.checktable(i);
                
                LuaTable body = keyframeT.get("body").checktable();
                float time = keyframeT.get("time").checknumber().tofloat();
                float angle = keyframeT.get("angle").checknumber().tofloat();
                
                LuaValue mapV = body.get("__LIME_KEYFRAME_MAP__");
                if (mapV.isnil())
                {
                    mapV = LuaValue.userdataOf(new HashMap<String, List<Pair<Float, Float>>>());
                    body.set("__LIME_KEYFRAME_MAP__", mapV);
                }
                
                Map<String, List<Pair<Float, Float>>> map = (Map<String, List<Pair<Float, Float>>>) mapV.checkuserdata();
                List<Pair<Float, Float>> list = map.get(animName);
                if (list == null)
                {
                    list = new ArrayList<Pair<Float, Float>>();
                    map.put(animName, list);
                }
                list.add(new Pair<Float, Float>(time, angle));
            }
        }
    }
}
