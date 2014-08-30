
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
local workingColliderVelocity = nil
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
	workingColliderVelocity = workingHitboxData:getVelocity(hash)
	workingColliderHash = hash
end

local function getColliderPosition()
	checkWorkingElementSet(workingCollider, "collider", "lime.collider.transform.position.get")
	local javaPosition = workingColliderTransform:getPosition()
	return strict.vector.toLua(javaPosition)
end

local function setColliderPosition(newPosition)
	checkWorkingElementSet(workingCollider, "collider", "lime.collider.transform.position.set")
	strict.typecheck.vector(newPosition, 1, "lime.collider.transform.position.set")
	workingColliderTransform:getPosition():set(newPosition.x, newPosition.y)
end

local function getColliderVelocity()
	checkWorkingElementSet(workingCollider, "collider", "lime.collider.velocity.get")
	return strict.vector.toLua(workingColliderVelocity)
end

local function setColliderVelocity(newPosition)
	checkWorkingElementSet(workingCollider, "collider", "lime.collider.velocity.set")
	strict.typecheck.vector(newPosition, 1, "lime.collider.velocity.set")
	workingColliderVelocity:set(newPosition.x, newPosition.y)
end

-- mask

local function setWorkingMask(hash)
	checkWorkingElementSet(workingEntity, "entity", "lime.mask.set")
	strict.typecheck.lua(hash, "number", 1, "lime.mask.set")

	workingMask = model:getMask(hash)
	workingMaskTransform = workingModelData:getTransform(hash)
	workingMaskHash = hash
end

local function getMaskPosition()
	checkWorkingElementSet(workingMask, "mask", "lime.mask.transform.position.get")
	local javaPosition = workingMaskTransform:getPosition()
	return strict.vector.toLua(javaPosition)
end

local function setMaskPosition(newPosition)
	checkWorkingElementSet(workingMask, "mask", "lime.mask.transform.position.set")
	strict.typecheck.vector(newPosition, 1, "lime.mask.transform.position.set")
	workingMaskTransform:getPosition():set(newPosition.x, newPosition.y)
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

	strict.world:addLight(hash, javaLight)
end

local function removeBasicLightFromWorld(hash)
	assert(lime.network.side.client, "lights not supported on this side")
	strict.world:removeLight(hash)
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
	hitbox = {
		collider = {
			set = setWorkingCollider,
			transform = {
				position = {
					get = getColliderPosition,
					set = setColliderPosition,
				},
			},
			velocity = {
				get = getColliderVelocity,
				set = setColliderVelocity,
			},
		},
	},
	model = {
		mask = {
			set = setWorkingMask,
			transform = {
				position = {
					get = getMaskPosition,
					set = setMaskPosition,
				},
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

local function follow(bodyEntityID, colliderHash, maskEntityID, maskHash)
	strict.typecheck.lua(bodyEntityID, "number", 1, "limex.follow")
	strict.typecheck.lua(colliderHash, "number", 2, "limex.follow")
	strict.typecheck.lua(maskEntityID, "number", 3, "limex.follow")
	strict.typecheck.lua(maskHash, "number", 4, "limex.follow")

	setWorkingEntity(bodyEntityID)
	setWorkingCollider(colliderHash)
	local position = getColliderPosition()

	setWorkingEntity(maskEntityID)
	setWorkingMask(maskHash)
	setMaskPosition(position)
end

-- limex table

limex = {
	follow = follow
}

