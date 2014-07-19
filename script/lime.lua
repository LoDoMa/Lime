local debug = true

local entity = LIME_INIT

local entityID = entity:getID()
local entityHash = entity:getHash()
local entityInternalName = entity:getInternalName()
local entityVisualName = entity:getVisualName()
local entityVersion = entity:getVersion()

local entityWorld = entity:getEntityWorld()

local HashHelper = java.require("net.lodoma.lime.util.HashHelper")

local workingEntity = nil
local workingBody = nil
local workingJoint = nil
local workingMask = nil

local properties = {}
local listeners = {}

-- local utilities

local function vector2toLuaVector(vector2)
	return {
		x = vector2:getX(),
		y = vector2:getY()
	}
end

-- entity

local function setWorkingEntity(entityID)
	workingEntity = entityWorld:getEntity(entityID)
end

-- body

local function setWorkingBody(bodyName)
	workingBody = entity:getBody(bodyName)
end

local function getBodyTranslation()
	local v2_translation = workingBody:getPosition()
	return vector2toLuaVector(v2_translation)
end

local function getBodyRotation()
	return workingBody:getAngle()
end

-- joint

local function setWorkingJoint(jointName)
	workingJoint = entity:getJoint(jointName)
end

-- mask

local function setWorkingMask(maskName)
	workingMask = entity:getMask(maskName)
end

local function getMaskTranslation()
	local v2_translation = workingMask:getTranslation()
	return vector2toLuaVector(v2_translation)
end

local function setMaskTranslation(translation)
	workingMask:setTranslation(translation.x, translation.y)
end

local function getMaskRotation()
	return workingMask:getRotation()
end

local function setMaskRotation(angle)
	workingMask:setRotation(angle)
end

-- property

local function getProperty(name)
	return properties[name]
end

local function setProperty(name, value)
	properties[name] = value
end

-- listener

local function setListener(limeType, listenerFunction)
	listeners[limeType] = listenerFunction
end

local function invokeListener(limeType, eventObject)
	listeners[limeType](eventObject)
end

-- utilities

local function round(num, idp)
  local mult = 10 ^ (idp or 0)
  return math.floor(num * mult + 0.5) / mult
end

local function hash32(str)
	return HashHelper:hash32(str)
end

local function hash64(str)
	return HashHelper.hash64(str)
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
		get = getListener,
		set = setListener,
	},
	util = {
		round = round,
		hash32 = hash32,
		hash64 = hash64,
	},
}

-- dangerous tables are removed

java = nil
os = nil

if not debug then
	io = nil
end