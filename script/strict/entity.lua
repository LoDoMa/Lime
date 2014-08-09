
local Vector2 = java.require("net.lodoma.lime.util.Vector2")
local HashHelper = java.require("net.lodoma.lime.util.HashHelper")
local NetworkSide = java.require("net.lodoma.lime.common.NetworkSide")
local LuaEventListener = java.require("net.lodoma.lime.script.LuaEventListener")
local Color = java.require("net.lodoma.lime.gui.Color")
local BasicLight = java.require("net.lodoma.lime.shader.light.BasicLight")

local entity = LIME_ENTITY
local script = LIME_SCRIPT

local entityID = entity:getID()
local entityName = entity:getName()
local entityVersion = entity:getVersion()

local entityWorld = entity:getEntityWorld()
local propertyPool = entity:getPropertyPool()

local networkSide = entityWorld:getNetworkSide():ordinal()
local serverSide = networkSide == NetworkSide:valueOf("SERVER"):ordinal() and true or nil
local clientSide = networkSide == NetworkSide:valueOf("CLIENT"):ordinal() and true or nil

local packetHandlerHashPool = propertyPool:getProperty(serverSide and "sphPool" or (clientSide and "cphPool" or nil))
local packetHashPool = propertyPool:getProperty(serverSide and "spPool" or (clientSide and "cpPool" or nil))

local workingEntity = nil
local workingBody = nil
local workingBodyHash = 0
local workingJoint = nil
local workingJointHash = 0
local workingMask = nil
local workingMaskHash = 0

local listenerFunctions = {}
local eventListeners = {}

local packetHandlerCache = {}
local packetCache = {}

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

local function checkColorType(value, argument, name)
	local argument_type = type(argument)
	local name_type = type(name)
	assert(argument_type == "number", "invalid argument #2 to \"local utility checkColorType\", expected number, got " .. argument_type)
	assert(name_type == "string", "invalid argument #3 to \"local utility checkColorType\", expected string, got " .. name_type)
	assert(lime.util.color.check(value), "invalid argument #" .. argument .. " to \"" .. name .. "\", expected color")
end

local function getPacketHandler(hash)
	checkType(hash, "number", 1, "local utility getPacketHandler")
	if packetHandlerCache[hash] then
		return packetHandlersCache[hash]
	end
	local packetHandler = packetHandlerHashPool:get(hash)
	packetHandlerCache[hash] = packetHandler
	return packetHandler
end

local function getPacket(hash)
	checkType(hash, "number", 1, "local utility getPacket")
	if packetCache[hash] then
		return packetCache[hash]
	end
	local packet = packetHashPool:get(hash)
	packetCache[hash] = packet
	return packet
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

local function buildColor(r, g, b, a)
	checkType(r, "number", 1, "lime.util.color.new")
	checkType(g, "number", 2, "lime.util.color.new")
	checkType(b, "number", 3, "lime.util.color.new")
	checkType(a, "number", 4, "lime.util.color.new")
	return {r = r, g = g, b = b, a = a}
end

local function isColor(colTable)
	if(type(colTable) ~= "table") then return false end
	if(type(colTable.r) ~= "number") then return false end
	if(type(colTable.g) ~= "number") then return false end
	if(type(colTable.b) ~= "number") then return false end
	if(type(colTable.a) ~= "number") then return false end
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

addHash32("Lime::EntityTransformModification")
addHash32("Lime::EntityForce")
addHash32("Lime::EntityLinearImpulse")
addHash32("Lime::EntityAngularImpulse")

-- entity

local function setWorkingEntity(entityID)
	checkType(entityID, "number", 1, "lime.entity.set")
	workingEntity = entityWorld:getEntity(entityID)
end

-- body

local function setWorkingBody(hash)
	checkWorkingElementSet(workingEntity, "entity", "lime.body.set")
	checkType(hash, "number", 1, "lime.body.set")
	workingBody = entity:getBody(hash)
	workingBodyHash = hash
end

local function getBodyTranslation()
	checkWorkingElementSet(workingBody, "body", "lime.body.transform.position.get")
	local v2_translation = workingBody:getPosition()
	return buildVector(v2_translation:getX(), v2_translation:getY())
end

local function setBodyTranslation(position)
	checkVectorType(position, 1, "lime.body.transform.position.set")
	workingBody:setPosition(Vector2:newInstance(position.x, position.y))
end

local function getBodyRotation()
	checkWorkingElementSet(workingBody, "body", "lime.body.transform.rotation.get")
	return workingBody:getAngle()
end

local function setBodyRotation(rotation)
	checkType(rotation, "number", 1, "lime.body.transform.rotation.set")
	workingBody:setAngle(rotation)
end

local function pushBodyTransformModification()
	checkWorkingElementSet(workingBody, "body", "lime.body.transform.push")
	if serverSide then
		getPacket(hashes32["Lime::EntityTransformModification"]):handleAll(entity, workingBodyHash)
	elseif clientSide then

	else
		assert(false, "internal problem: unknown network side")
	end
end

local function applyForceToBody(force, point)
	checkWorkingElementSet(workingBody, "body", "lime.body.force")
	checkVectorType(force, 1, "lime.body.force")
	checkVectorType(point, 2, "lime.body.force")
	javaForce = Vector2:newInstance(force.x, force.y)
	javaPoint = Vector2:newInstance(point.x, point.y)
	workingBody:applyForce(javaForce, javaPoint)

	if serverSide then
		getPacket(hashes32["Lime::EntityForce"]):handleAll(entity, workingBodyHash, javaForce, javaPoint)
	elseif clientSide then

	else
		assert(false, "internal problem: unknown network side")
	end
end

local function applyLinearImpulseToBody(impulse, point)
	checkWorkingElementSet(workingBody, "body", "lime.body.impulse.linear")
	checkVectorType(impulse, 1, "lime.body.impulse.linear")
	checkVectorType(point, 2, "lime.body.impulse.linear")
	javaImpulse = Vector2:newInstance(impulse.x, impulse.y)
	javaPoint = Vector2:newInstance(point.x, point.y)
	workingBody:applyLinearImpulse(javaImpulse, javaPoint)

	if serverSide then
		getPacket(hashes32["Lime::EntityLinearImpulse"]):handleAll(entity, workingBodyHash, javaImpulse, javaPoint)
	elseif clientSide then

	else
		assert(false, "internal problem: unknown network side")
	end
end

local function applyAngularImpulseToBody(impulse)
	checkWorkingElementSet(workingBody, "body", "lime.body.impulse.angular")
	checkType(impulse, "number", 1, "lime.body.impulse.angular")
	workingBody:applyAngularImpulse(impulse)

	if serverSide then
		getPacket(hashes32["Lime::EntityAngularImpulse"]):handleAll(entity, workingBodyHash, impulse)
	elseif clientSide then

	else
		assert(false, "internal problem: unknown network side")
	end
end

-- joint

local function setWorkingJoint(hash)
	checkWorkingElementSet(workingEntity, "entity", "lime.joint.set")
	checkType(hash, "number", 1, "lime.joint.set")
	workingJoint = entity:getJoint(hash)
	workingJointHash = hash
end

-- mask

local function setWorkingMask(hash)
	checkWorkingElementSet(workingEntity, "entity", "lime.mask.set")
	checkType(hash, "number", 1, "lime.mask.set")
	workingMask = entity:getMask(hash)
	workingMaskHash = hash
end

local function getMaskTranslation()
	checkWorkingElementSet(workingMask, "mask", "lime.mask.translation.get")
	local v2_translation = workingMask:getTranslation()
	return buildVector(v2_translation:getX(), v2_translation:getY())
end

local function setMaskTranslation(translation)
	checkWorkingElementSet(workingMask, "mask", "lime.mask.translation.set")
	checkVectorType(translation, 1, "lime.mask.translation.set")
	workingMask:setTranslation(translation.x, translation.y)
end

local function getMaskRotation()
	checkWorkingElementSet(workingMask, "mask", "lime.mask.rotation.get")
	return workingMask:getRotation()
end

local function setMaskRotation(angle)
	checkWorkingElementSet(workingMask, "mask", "lime.mask.rotation.set")
	checkType(angle, "number", 1, "lime.mask.rotation.set")
	workingMask:setRotation(angle)
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

-- light

local function addBasicLightToWorld(hash, position, radius, color, angleFrom, angleTo)
	checkType(hash, "number", 1, "lime.light.basic.add")
	checkVectorType(position, 2, "lime.light.basic.add")
	checkType(radius, "number", 3, "lime.light.basic.add")
	checkColorType(color, 4, "lime.light.basic.add")
	checkType(angleFrom, "number", 5, "lime.light.basic.add")
	checkType(angleTo, "number", 6, "lime.light.basic.add")

	assert(clientSide, "lights not supported on this side")

	local javaPosition = Vector2:new(position.x, position.y)
	local javaColor = Color:new(color.r, color.g, color.b, color.a)

	local javaLight = BasicLight:new(javaPosition, radius, javaColor, angleFrom, angleTo)

	entityWorld:addLight(hash, javaLight)
end

local function removeBasicLightFromWorld(hash)
	assert(clientSide, "lights not supported on this side")
	entityWorld:removeLight(hash)
end

-- lime table

lime = {
	network = {
		side = {
			server = serverSide,
			client = clientSide,
		},
	},
	this = {
		ID = entityID,
		name = entityName,
		version = entityVersion,
	},
	entity = {
		set = setWorkingEntity,
	},
	body = {
		set = setWorkingBody,
		transform = {
			position = {
				get = getBodyTranslation,
				set = setBodyTranslation,
			},
			rotation = {
				get = getBodyRotation,
				set = setBodyRotation,
			},
			push = pushBodyTransformModification
		},
		force = applyForceToBody,
		impulse = {
			linear = applyLinearImpulseToBody,
			angular = applyAngularImpulseToBody,
		},
	},
	joint = {
		set = setWorkingJoint,
	},
	mask = {
		set = setWorkingMask,
		transform = {
			position = {
				get = getMaskTranslation,
				set = setMaskTranslation,
			},
			rotation = {
				get = getMaskRotation,
				set = setMaskRotation,
			},
		},
	},
	listener = {
		set = setListener,
		release = releaseListener,
		invoke = invokeListener,
	},
	light = {
		basic = {
			add = addBasicLightToWorld,
			remove = removeBasicLightFromWorld,
		},
	},
	util = {
		round = round,
		hash32 = hash32,
		hash64 = hash64,
		vector = {
			new = buildVector,
			check = isVector,
		},
		color = {
			new = buildColor,
			check = isColor,
		},
	},
}

-- extra utility functions

local function follow(bodyEntityID, bodyHash, maskEntityID, maskHash)
	checkType(bodyEntityID, "number", 1, "limex.follow")
	checkType(bodyHash, "number", 2, "limex.follow")
	checkType(maskEntityID, "number", 3, "limex.follow")
	checkType(maskHash, "number", 4, "limex.follow")

	setWorkingEntity(bodyEntityID)
	setWorkingBody(bodyHash)
	local t = getBodyTranslation()
	local r = getBodyRotation()

	setWorkingEntity(maskEntityID)
	setWorkingMask(maskHash)
	setMaskTranslation(t)
	setMaskRotation(r)
end

-- limex table

limex = {
	follow = follow
}
