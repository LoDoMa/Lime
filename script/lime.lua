
local Vector2 = java.require("net.lodoma.lime.util.Vector2")
local HashHelper = java.require("net.lodoma.lime.util.HashHelper")

local entity = LIME_INIT

local entityID = entity:getID()
local entityHash = entity:getHash()
local entityInternalName = entity:getInternalName()
local entityVisualName = entity:getVisualName()
local entityVersion = entity:getVersion()

local entityWorld = entity:getEntityWorld()

local workingEntity = nil
local workingBody = nil
local workingJoint = nil
local workingMask = nil

local properties = {}
local listeners = {}

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
	assert(element ~= nil, name .. " not set before calling " .. call)
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

-- entity

local function setWorkingEntity(entityID)
	checkType(entityID, "number", 1, "lime.entity.set")
	workingEntity = entityWorld:getEntity(entityID)
end

-- body

local function setWorkingBody(bodyName)
	checkWorkingElementSet(workingEntity, "entity", "lime.body.set")
	checkType(bodyName, "string", 1, "lime.body.set")
	workingBody = entity:getBody(bodyName)
end

local function getBodyTranslation()
	checkWorkingElementSet(workingBody, "body", "lime.body.translation.get")
	local v2_translation = workingBody:getPosition()
	return buildVector(v2_translation:getX(), v2_translation:getY())
end

local function getBodyRotation()
	checkWorkingElementSet(workingBody, "body", "lime.body.rotation.get")
	return workingBody:getAngle()
end

local function applyLinearImpulseToBody(impulse, point)
	checkWorkingElementSet(workingBody, "body", "lime.body.impulse.linear")
	assert(isVector(impulse), "invalid argument #1 to \"lime.body.impulse.linear\", expected vector")
	assert(isVector(point), "invalid argument #2 to \"lime.body.impulse.linear\", expected vector")
	javaImpulse = Vector2:newInstance(impulse.x, impulse.y)
	javaPoint = Vector2:newInstance(point.x, point.y)
	workingBody:applyLinearImpulse(javaImpulse, javaPoint)
end

local function applyAngularImpulseToBody(impulse)
	checkWorkingElementSet(workingBody, "body", "lime.body.impulse.angular")
	checkType(impulse, "number", 1, "lime.body.impulse.angular")
	workingBody:applyAngularImpulse(impulse)
end

-- joint

local function setWorkingJoint(jointName)
	checkWorkingElementSet(workingEntity, "entity", "lime.joint.set")
	checkType(jointName, "string", 1, "lime.joint.set")
	workingJoint = entity:getJoint(jointName)
end

-- mask

local function setWorkingMask(maskName)
	checkWorkingElementSet(workingEntity, "entity", "lime.mask.set")
	checkType(maskName, "string", 1, "lime.mask.set")
	workingMask = entity:getMask(maskName)
end

local function getMaskTranslation()
	checkWorkingElementSet(workingMask, "mask", "lime.mask.translation.get")
	local v2_translation = workingMask:getTranslation()
	return buildVector(v2_translation:getX(), v2_translation:getY())
end

local function setMaskTranslation(translation)
	checkWorkingElementSet(workingMask, "mask", "lime.mask.translation.set")
	assert(isVector(translation), "invalid argument #1 to \"lime.mask.translation.set\", expected vector")
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

-- property

local function getProperty(name)
	checkType(name, "string", 1, "lime.property.get")
	return properties[name]
end

local function setProperty(name, value)
	checkType(name, "string", 1, "lime.property.set")
	properties[name] = value
end

-- listener

local function setListener(limeType, listenerFunction)
	checkType(limeType, "string", 1, "lime.listener.set")
	checkType(listenerFunction, "function", 2, "lime.listener.set")
	listeners[limeType] = listenerFunction
end

local function invokeListener(limeType, eventObject)
	checkType(limeType, "string", 1, "lime.listener.invoke")
	listeners[limeType](eventObject)
end

-- lime table

lime = {
	this = {
		ID = entityID,
		hash = entityHash,
		name = {
			internal = entityInternalName,
			visual = entityVisualName,
		},
		version = entityVersion,
	},
	entity = {
		set = setWorkingEntity,
	},
	body = {
		set = setWorkingBody,
		translation = {
			get = getBodyTranslation,
		},
		rotation = {
			get = getBodyRotation,
		},
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
		translation = {
			get = getMaskTranslation,
			set = setMaskTranslation,
		},
		rotation = {
			get = getMaskRotation,
			set = setMaskRotation,
		},
	},
	property = {
		get = getProperty,
		set = setProperty,
	},
	listener = {
		set = setListener,
		invoke = invokeListener,
	},
	util = {
		round = round,
		hash32 = hash32,
		hash64 = hash64,
		vector = {
			new = buildVector
		},
	},
}

-- dangerous tables are removed

java = nil
os = nil
