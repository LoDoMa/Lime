
local strict = getStrict()
strictRequireJava("net.lodoma.lime.shader.light.BasicLight")

require "/script/strict/vector"
require "/script/strict/color"

local entityFactory = LIME_ENTITY_FACTORY
local world = entityFactory.world
local propertyPool = world:getPropertyPool()

addToStrict({
	entityFactory = entityFactory,
	world = world,
	physicsWorld = world.physicsWorld,
	visualWorld = world.visualWorld,
	propertyPool = propertyPool,

	entityName = entityFactory.name,
	entityVersion = entityFactory.version,
})

require "/script/strict/network"
require "/script/strict/listener"
require "/script/strict/input"

local workingEntity = nil
local workingShapeComponent = nil

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

	workingEntity = world.entityPool:get(entityID)
end

-- body

local function getBodyPosition()
	checkWorkingElementSet(workingEntity, "entity", "lime.body.position.get")
	
	return strict.vector.toLua(workingEntity.body.position)
end

local function setBodyPosition(position)
	checkWorkingElementSet(workingEntity, "entity", "lime.body.position.set")
	strict.typecheck.vector(position, 1, "lime.body.position.set")

	workingEntity.body.position:set(position.x, position.y);
end

local function getBodyVelocity()
	checkWorkingElementSet(workingEntity, "entity", "lime.body.velocity.get")

	return strict.vector.toLua(workingEntity.body.velocity)
end

local function setBodyVelocity(velocity)
	checkWorkingElementSet(workingEntity, "entity", "lime.body.velocity.set")
	strict.typecheck.vector(velocity, 1, "lime.body.velocity.set")

	workingEntity.body.velocity:set(velocity.x, velocity.y);
end

-- shape

local function setWorkingShapeComponent(componentID)
	checkWorkingElementSet(workingEntity, "entity", "lime.shape.component.set")
	strict.typecheck.lua(componentID, "number", 1, "lime.shape.component.set")

	workingShapeComponent = workingEntity.shape.componentPool:get(componentID)
end

local function getShapeComponentPosition()
	checkWorkingElementSet(workingShapeComponent, "shape component", "lime.shape.component.position.get")
	
	return strict.vector.toLua(workingShapeComponent.position)
end

local function setShapeComponentPosition(position)
	checkWorkingElementSet(workingShapeComponent, "shape component", "lime.shape.component.position.set")
	strict.typecheck.vector(position, 1, "lime.shape.component.position.set")

	workingShapeComponent.position:set(position.x, position.y);
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
	body = {
		-- TODO: collect constant body info
		restitution = nil,
		density = nil,
		volume = nil,
		mass = nil,
		position = {
			get = getBodyPosition,
			set = setBodyPosition,
		},
		velocity = {
			get = getBodyVelocity,
			set = setBodyVelocity,
		},
	},
	shape = {
		component = {
			set = setWorkingShapeComponent,
			position = {
				get = getShapeComponentPosition,
				set = setShapeComponentPosition,
			},
			rotation = {
				get = getShapeComponentRotation,
				set = setShapeComponentRotation,
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

