
local strict = getStrict()
strictRequireJava("net.lodoma.lime.shader.light.BasicLight")

require "/script/strict/vector"
require "/script/strict/color"

local entityType = LIME_ENTITY_TYPE
local model = entityType:getModel()
local hitbox = entityType:getHitbox()
local world = entityType:getWorld()
local propertyPool = world:getPropertyPool()

addToStrict({
	entityType = entityType,
	model = model,
	hitbox = hitbox,
	world = world,
	propertyPool = propertyPool,

	entityName = entityType:getName(),
	entityVersion = entityType:getVersion(),
})

require "/script/strict/network"
require "/script/strict/listener"
require "/script/strict/input"

local workingEntity = nil
local workingHitboxData = nil
local workingModelData = nil

local workingCollider = nil
local workingColliderTransform = nil
local workingColliderHash = 0

local workingMask = nil
local workingMaskTransform = nil
local workingMaskHash = 0


-- local utilities

local function checkWorkingElementSet(element, name, call)
	local name_type = type(name)
	local call_type = type(call)
	assert(name_type == "string", "invalid argument #2 to \"local utility checkWorkingElementSet\", expected string, got " .. name_type)
	assert(call_type == "string", "invalid argument #3 to \"local utility checkWorkingElementSet\", expected string, got " .. call_type)
	assert(element ~= nil, "\"" .. name .. "\" not set before calling \"" .. call .. "\"")
end

-- entity

local function setWorkingEntity(entityID)
	strict.typecheck.lua(entityID, "number", 1, "lime.entity.set")

	workingEntity = world:getEntity(entityID)
	workingHitboxData = workingEntity:getHitboxData()
	workingModelData = workingEntity:getModelData()
end

-- collider

local function setWorkingCollider(hash)
	checkWorkingElementSet(workingEntity, "entity", "lime.collider.set")
	strict.typecheck.lua(hash, "number", 1, "lime.body.set")

	workingCollider = hitbox:getCollider(hash)
	workingColliderTransform = workingHitboxData:getTransform(hash)
	workingColliderHash = hash
end

-- mask

local function setWorkingMask(hash)
	checkWorkingElementSet(workingEntity, "entity", "lime.mask.set")
	strict.typecheck.lua(hash, "number", 1, "lime.mask.set")

	workingMask = model:getMask(hash)
	workingMaskTransform = workingModelData:getTransform(hash)
	workingMaskHash = hash
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
		name = strict.entityName,
		version = strict.entityVersion,
	},
	entity = {
		set = setWorkingEntity,
	},
	collider = {
		set = setWorkingCollider,
	},
	mask = {
		set = setWorkingMask,
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

