
local strict = getStrict()
strictRequireJava("net.lodoma.lime.script.LuaEventListener")

local emanPool = strict.propertyPool:getProperty("emanPool")

local listenerFunctions = {}
local eventListeners = {}

local function setListener(hash, listenerFunction)
	strict.typecheck.lua(hash, "number", 1, "lime.listener.set")
	strict.typecheck.lua(listenerFunction, "function", 2, "lime.listener.set")
	assert(not eventListeners[hash], "listener not released before calling \"lime.listener.set\"")

	local eventManager = emanPool:get(hash)
	local eventListener = strict.java["net.lodoma.lime.script.LuaEventListener"]:new(hash, eventManager, strict.script)

	eventListeners[hash] = eventListener
	listenerFunctions[hash] = listenerFunction
end

local function releaseListener(hash)
	strict.typecheck.lua(hash, "number", 1, "lime.listener.release")
	assert(eventListeners[hash], "listener not set before calling \"lime.listener.release\"")

	eventListeners[hash]:destroy()
	
	eventListeners[hash] = nil
	listenerFunctions[hash] = nil
end

local function extractEventBundle(eventBundle)
	local luaSafe = eventBundle:isLuaSafe()
	assert(luaSafe, "event bundle is not safe")

	local keyList = eventBundle:getKeyList()
	local keyCount = keyList:size()

	local bundle = {}

	for index = 1, keyCount, 1 do
		local key = keyList:get(index - 1)
		bundle[key] = eventBundle:get(key)
	end

	return bundle
end

local function invokeListener(hash, eventBundle)
	strict.typecheck.lua(hash, "number", 1, "lime.listener.invoke")
	strict.typecheck.lua(eventBundle, "userdata", 2, "lime.listener.invoke")
	assert(listenerFunctions[hash], "listener not set before calling \"lime.listener.invoke\"")
	
	local bundle = extractEventBundle(eventBundle)

	listenerFunctions[hash](bundle)
end

addToLime({
	listener = {
		set = setListener,
		release = releaseListener,
		invoke = invokeListener,
	},
})