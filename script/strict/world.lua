
local Vector2 = java.require("net.lodoma.lime.util.Vector2")
local HashHelper = java.require("net.lodoma.lime.util.HashHelper")
local NetworkSide = java.require("net.lodoma.lime.common.NetworkSide")
local Platform = java.require("net.lodoma.lime.world.platform.Platform")
local Entity = java.require("net.lodoma.lime.physics.entity.Entity")
local LuaEventListener = java.require("net.lodoma.lime.script.LuaEventListener")

local world = LIME_WORLD
local script = LIME_SCRIPT

local propertyPool = world:getPropertyPool()
local entityLoader = propertyPool:getProperty("entityLoader")
local emanPool = propertyPool:getProperty("emanPool")

local networkSide = world:getNetworkSide():ordinal()
local serverSide = networkSide == NetworkSide:valueOf("SERVER"):ordinal() and true or nil
local clientSide = networkSide == NetworkSide:valueOf("CLIENT"):ordinal() and true or nil

local inputHandlerHashPool = propertyPool:getProperty(serverSide and "sphPool" or (clientSide and "cphPool" or nil))
local outputHashPool = propertyPool:getProperty(serverSide and "spPool" or (clientSide and "cpPool" or nil))

local listenerFunctions = {}
local eventListeners = {}

-- local utilities

local function checkType(value, etype, argument, name)
	local etype_type = type(etype)
	local argument_type = type(argument)
	local name_type = type(name)

	assert(etype_type == "string", "invalid argument #2 to \"local utility checkType\", expected string, got " .. etype_type)
	assert(argument_type == "number", "invalid argument #3 to \"local utility checkType\", expected number, got " .. argument_type)
	assert(name_type == "string", "invalid argument #4 to \"local utility checkType\", expected string, got " .. name_type)

	local gtype = type(value)
	assert(gtype == etype, "invalid argument #" .. argument .. " to \"" .. name .. "\", expected " .. etype .. ", got " .. gtype)
end

local function checkWorkingElementSet(element, name, call)
	local name_type = type(name)
	local call_type = type(call)
	assert(name_type == "string", "invalid argument #2 to \"local utility checkWorkingElementSet\", expected string, got " .. name_type)
	assert(call_type == "string", "invalid argument #3 to \"local utility checkWorkingElementSet\", expected string, got " .. call_type)
	assert(element ~= nil, "\"" .. name .. "\" not set before calling \"" .. call .. "\"")
end

local function checkVectorType(value, argument, name)
	local argument_type = type(argument)
	local name_type = type(name)
	assert(argument_type == "number", "invalid argument #2 to \"local utility checkVectorType\", expected number, got " .. argument_type)
	assert(name_type == "string", "invalid argument #3 to \"local utility checkVectorType\", expected string, got " .. name_type)
	assert(lime.util.vector.check(value), "invalid argument #" .. argument .. " to \"" .. name .. "\", expected vector2")
end

local function getInputHandler(hash)
	checkType(hash, "number", 1, "local utility getInputHandler")
	return inputHandlerHashPool:get(hash)
end

local function getOutput(hash)
	checkType(hash, "number", 1, "local utility getOutput")
	return outputHashPool:get(hash)
end

-- utilities

local function round(num, idp)
	checkType(num, "number", 1, "lime.util.round")
	checkType(idp, "number", 2, "lime.util.round")
	local mult = 10 ^ (idp or 0)
	return math.floor(num * mult + 0.5) / mult
end

local function hash32(str)
	checkType(str, "string", 1, "lime.util.hash32")
	return HashHelper:hash32(str)
end

local function hash64(str)
	checkType(str, "string", 1, "lime.util.hash64")
	return HashHelper.hash64(str)
end

local function buildVector(x, y)
	checkType(x, "number", 1, "lime.util.vector.new")
	checkType(y, "number", 2, "lime.util.vector.new")
	return {x = x, y = y}
end

local function isVector(vecTable)
	if(type(vecTable) ~= "table") then return false end
	if(type(vecTable.x) ~= "number") then return false end
	if(type(vecTable.y) ~= "number") then return false end
	return true;
end

-- hashes

local hashes32 = {}
local hashes64 = {}

local function addHash32(str)
	checkType(str, "string", 1, "local utility addHash32")
	hashes32[str] = hash32(str)
end

local function addHash64(str)
	checkType(str, "string", 1, "local utility addHash64")
	hashes64[str] = hash64(str)
end

-- platform

local function addPlatformToWorld(offset, ...)
	checkVectorType(offset, 1, "lime.platform.create")

	if serverSide then
		local javaOffset = Vector2:newInstance(offset.x, offset.y)

		local i = 1
		local vertices = {...}
		local javaVertices = {}
		while vertices[i] do
			local vertex = vertices[i]
			checkVectorType(vertex, i + 1, "lime.platform.create")
			javaVertices[i] = Vector2:newInstance(vertex.x, vertex.y)
			i = i + 1
		end

		local platform = Platform:newInstance(javaOffset, javaVertices)
		world:addPlatform(platform)
	elseif clientSide then

	else
		assert(false, "internal problem: unknown network side")
	end
end

-- entity

local function addEntityToWorld(hash)
	checkType(hash, "number", 1, "lime.entity.create")

	if serverSide then
		local file = entityLoader:getXMLFileByHash(hash)
		local entity = entityLoader:loadFromXML(file, world, propertyPool)
		world:addEntity(entity)
	elseif clientSide then

	else
		assert(false, "internal problem: unknown network side")
	end
end

-- listener

local function setListener(hash, listenerFunction)
	checkType(hash, "number", 1, "lime.listener.set")
	checkType(listenerFunction, "function", 2, "lime.listener.set")
	assert(not eventListeners[hash], "listener not released before calling \"lime.listener.set\"")

	local eventManager = emanPool:get(hash)
	local eventListener = LuaEventListener:new(hash, eventManager, script)

	eventListeners[hash] = eventListener
	listenerFunctions[hash] = listenerFunction
end

local function releaseListener(hash)
	checkType(hash, "number", 1, "lime.listener.release")
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
	checkType(hash, "number", 1, "lime.listener.invoke")
	checkType(eventBundle, "userdata", 2, "lime.listener.invoke")
	assert(listenerFunctions[hash], "listener not set before calling \"lime.listener.invoke\"")
	
	local bundle = extractEventBundle(eventBundle)

	listenerFunctions[hash](bundle)
end

-- lime table

lime = {
	network = {
		side = {
			server = serverSide,
			client = clientSide,
		},
	},
	platform = {
		create = addPlatformToWorld,
	},
	entity = {
		create = addEntityToWorld,
	},
	listener = {
		set = setListener,
		release = releaseListener,
		invoke = invokeListener,
	},
	util = {
		round = round,
		hash32 = hash32,
		hash64 = hash64,
		vector = {
			new = buildVector,
			check = isVector,
		},
	},
}