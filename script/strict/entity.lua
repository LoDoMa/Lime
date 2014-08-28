
local strict = getStrict()
strictRequireJava("net.lodoma.lime.shader.light.BasicLight")

require "/script/strict/vector"
require "/script/strict/color"

addToStrict({
	entity = LIME_ENTITY,
	entityID = LIME_ENTITY:getID(),
	entityName = LIME_ENTITY:getName(),
	entityVersion = LIME_ENTITY:getVersion(),
	entityWorld = LIME_ENTITY:getEntityWorld(),
	propertyPool = LIME_ENTITY:getPropertyPool(),
})

require "/script/strict/network"
require "/script/strict/listener"
require "/script/strict/input"

local workingEntity = nil
local workingBody = nil
local workingBodyHash = 0
local workingJoint = nil
local workingJointHash = 0
local workingMask = nil
local workingMaskHash = 0

-- local utilities

local function checkWorkingElementSet(element, name, call)
	local name_type = type(name)
	local call_type = type(call)
	assert(name_type == "string", "invalid argument #2 to \"local utility checkWorkingElementSet\", expected string, got " .. name_type)
	assert(call_type == "string", "invalid argument #3 to \"local utility checkWorkingElementSet\", expected string, got " .. call_type)
	assert(element ~= nil, "\"" .. name .. "\" not set before calling \"" .. call .. "\"")
end

-- hashes

local hashes32 = {}
local hashes64 = {}

local function addHash32(str)
	strict.typecheck.lua(str, "string", 1, "local utility addHash32")
	hashes32[str] = lime.util.hash32(str)
end

local function addHash64(str)
	strict.typecheck.lua(str, "string", 1, "local utility addHash64")
	hashes64[str] = lime.util.hash64(str)
end

addHash32("Lime::EntityTransformModification")
addHash32("Lime::EntityForce")
addHash32("Lime::EntityLinearImpulse")
addHash32("Lime::EntityAngularImpulse")

-- entity

local function setWorkingEntity(entityID)
	strict.typecheck.lua(entityID, "number", 1, "lime.entity.set")
	workingEntity = strict.entityWorld:getEntity(entityID)
end

-- body

local function setWorkingBody(hash)
	checkWorkingElementSet(workingEntity, "entity", "lime.body.set")
	strict.typecheck.lua(hash, "number", 1, "lime.body.set")
	workingBody = workingEntity:getBody(hash)
	workingBodyHash = hash
end

local function getBodyTranslation()
	checkWorkingElementSet(workingBody, "body", "lime.body.transform.position.get")
	local v2_translation = workingBody:getPosition()
	return lime.util.vector.new(v2_translation:getX(), v2_translation:getY())
end

local function setBodyTranslation(position)
	strict.typecheck.vector(position, 1, "lime.body.transform.position.set")
	workingBody:setPosition(strict.vector.toJava(position))
end

local function getBodyRotation()
	checkWorkingElementSet(workingBody, "body", "lime.body.transform.rotation.get")
	return workingBody:getAngle()
end

local function setBodyRotation(rotation)
	strict.typecheck.lua(rotation, "number", 1, "lime.body.transform.rotation.set")
	workingBody:setAngle(rotation)
end

local function pushBodyTransformModification()
	checkWorkingElementSet(workingBody, "body", "lime.body.transform.push")
	if lime.network.side.server then
		strict.network.getPacket(hashes32["Lime::EntityTransformModification"]):handleAll(workingEntity, workingBodyHash)
	elseif lime.network.side.client then

	else
		assert(false, "internal problem: unknown network side")
	end
end

local function applyForceToBody(force, point)
	checkWorkingElementSet(workingBody, "body", "lime.body.force")
	strict.typecheck.vector(force, 1, "lime.body.force")
	strict.typecheck.vector(point, 2, "lime.body.force")
	javaForce = strict.vector.toJava(force)
	javaPoint = strict.vector.toJava(point)
	workingBody:applyForce(javaForce, javaPoint)

	if lime.network.side.server then
		strict.network.getPacket(hashes32["Lime::EntityForce"]):handleAll(workingEntity, workingBodyHash, javaForce, javaPoint)
	elseif lime.network.side.client then

	else
		assert(false, "internal problem: unknown network side")
	end
end

local function applyLinearImpulseToBody(impulse, point)
	checkWorkingElementSet(workingBody, "body", "lime.body.impulse.linear")
	strict.typecheck.vector(impulse, 1, "lime.body.impulse.linear")
	strict.typecheck.vector(point, 2, "lime.body.impulse.linear")
	javaImpulse = strict.vector.toJava(impulse)
	javaPoint = strict.vector.toJava(point)
	workingBody:applyLinearImpulse(javaImpulse, javaPoint)

	if lime.network.side.server then
		strict.network.getPacket(hashes32["Lime::EntityLinearImpulse"]):handleAll(workingEntity, workingBodyHash, javaImpulse, javaPoint)
	elseif lime.network.side.client then

	else
		assert(false, "internal problem: unknown network side")
	end
end

local function applyAngularImpulseToBody(impulse)
	checkWorkingElementSet(workingBody, "body", "lime.body.impulse.angular")
	strict.typecheck.lua(impulse, "number", 1, "lime.body.impulse.angular")
	workingBody:applyAngularImpulse(impulse)

	if lime.network.side.server then
		strict.network.getPacket(hashes32["Lime::EntityAngularImpulse"]):handleAll(workingEntity, workingBodyHash, impulse)
	elseif lime.network.side.client then

	else
		assert(false, "internal problem: unknown network side")
	end
end

-- joint

local function setWorkingJoint(hash)
	checkWorkingElementSet(workingEntity, "entity", "lime.joint.set")
	strict.typecheck.lua(hash, "number", 1, "lime.joint.set")
	workingJoint = workingEntity:getJoint(hash)
	workingJointHash = hash
end

-- mask

local function setWorkingMask(hash)
	checkWorkingElementSet(workingEntity, "entity", "lime.mask.set")
	strict.typecheck.lua(hash, "number", 1, "lime.mask.set")
	workingMask = workingEntity:getMask(hash)
	workingMaskHash = hash
end

local function getMaskTranslation()
	checkWorkingElementSet(workingMask, "mask", "lime.mask.translation.get")
	local v2_translation = workingMask:getTranslation()
	return lime.util.vector.new(v2_translation:getX(), v2_translation:getY())
end

local function setMaskTranslation(translation)
	checkWorkingElementSet(workingMask, "mask", "lime.mask.translation.set")
	strict.typecheck.vector(translation, 1, "lime.mask.translation.set")
	workingMask:setTranslation(translation.x, translation.y)
end

local function getMaskRotation()
	checkWorkingElementSet(workingMask, "mask", "lime.mask.rotation.get")
	return workingMask:getRotation()
end

local function setMaskRotation(angle)
	checkWorkingElementSet(workingMask, "mask", "lime.mask.rotation.set")
	strict.typecheck.lua(angle, "number", 1, "lime.mask.rotation.set")
	workingMask:setRotation(angle)
end

-- light

local function addBasicLightToWorld(hash, position, radius, color, angleFrom, angleTo)
	strict.typecheck.lua(hash, "number", 1, "lime.light.basic.add")
	strict.typecheck.vector(position, 2, "lime.light.basic.add")
	strict.typecheck.lua(radius, "number", 3, "lime.light.basic.add")
	strict.typecheck.color(color, 4, "lime.light.basic.add")
	strict.typecheck.lua(angleFrom, "number", 5, "lime.light.basic.add")
	strict.typecheck.lua(angleTo, "number", 6, "lime.light.basic.add")

	assert(lime.network.side.client, "lights not supported on this side")

	local javaPosition = strict.vector.toJava(position)
	local javaColor = strict.color.toJava(color)

	local javaLight = strict.java["net.lodoma.lime.shader.light.BasicLight"]:new(javaPosition, radius, javaColor, angleFrom, angleTo)

	strict.entityWorld:addLight(hash, javaLight)
end

local function removeBasicLightFromWorld(hash)
	assert(lime.network.side.client, "lights not supported on this side")
	strict.entityWorld:removeLight(hash)
end

-- lime table

addToLime({
	this = {
		ID = strict.entityID,
		name = strict.entityName,
		version = strict.entityVersion,
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
	light = {
		basic = {
			add = addBasicLightToWorld,
			remove = removeBasicLightFromWorld,
		},
	},
})

-- extra utility functions

local function follow(bodyEntityID, bodyHash, maskEntityID, maskHash)
	strict.typecheck.lua(bodyEntityID, "number", 1, "limex.follow")
	strict.typecheck.lua(bodyHash, "number", 2, "limex.follow")
	strict.typecheck.lua(maskEntityID, "number", 3, "limex.follow")
	strict.typecheck.lua(maskHash, "number", 4, "limex.follow")

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

