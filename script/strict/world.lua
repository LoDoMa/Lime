
local strict = getStrict()
strictRequireJava("net.lodoma.lime.world.platform.Platform")
strictRequireJava("net.lodoma.lime.shader.light.BasicLight")

require "/script/strict/vector"
require "/script/strict/color"

local world = LIME_WORLD
local propertyPool = world:getPropertyPool()
local physicsWorld = world:getPhysicsWorld()
local entityLoader = propertyPool:getProperty("entityLoader")

addToStrict({
	world = world,
	entityWorld = world,
	propertyPool = propertyPool,
	physicsWorld = physicsWorld,
	entityLoader = entityLoader,
	emanPool = emanPool,
})

require "/script/strict/network"
require "/script/strict/listener"

if lime.network.side.server then
	addToStrict({userManager = strict.propertyPool:getProperty("userManager")})
end

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
	hashes32[str] = hash32(str)
end

local function addHash64(str)
	strict.typecheck.lua(str, "string", 1, "local utility addHash64")
	hashes64[str] = hash64(str)
end

-- platform

local function addPlatformToWorld(offset, ...)
	strict.typecheck.vector(offset, 1, "lime.platform.create")

	if lime.network.side.server then
		local javaOffset = strict.vector.toJava(offset)

		local i = 1
		local vertices = {...}
		local javaVertices = {}
		while vertices[i] do
			local vertex = vertices[i]
			strict.typecheck.vector(vertex, i + 1, "lime.platform.create")
			javaVertices[i] = strict.vector.toJava(vertex)
			i = i + 1
		end

		local platform = strict.java["net.lodoma.lime.world.platform.Platform"]:newInstance(physicsWorld, javaOffset, javaVertices)
		world:addPlatform(platform)
	elseif lime.network.side.client then

	else
		assert(false, "internal problem: unknown network side")
	end
end

-- entity

local function addEntityToWorld(hash)
	strict.typecheck.lua(hash, "number", 1, "lime.entity.create")

	if lime.network.side.server then
		local entity = entityLoader:newEntity(world, physicsWorld, propertyPool, hash)
		world:addEntity(entity)
		return entity:getID()
	elseif lime.network.side.client then

	else
		assert(false, "internal problem: unknown network side")
	end
end

-- actor

local function setActorForUser(entityID, userID)
	
end

-- light

local function addBasicLightToWorld(hash, position, radius, color, angleFrom, angleTo)
	strict.typecheck.lua(hash, "number", 1, "lime.light.basic.add")
	strict.typecheck.vector(position, 2, "lime.light.basic.add")
	strict.typecheck.lua(radius, "number", 3, "lime.light.basic.add")
	strict.typecheck.color(color, 4, "lime.light.basic.add")
	strict.typecheck.lua(angleFrom, "number", 5, "lime.light.basic.add")
	strict.typecheck.lua(angleTo, "number", 6, "lime.light.basic.add")

	assert(clientSide, "lights not supported on this side")

	local javaPosition = strict.vector.toJava(position)
	local javaColor = strict.color.toJava(color)

	local javaLight = strict.java["net.lodoma.lime.shader.light.BasicLight"]:new(javaPosition, radius, javaColor, angleFrom, angleTo)

	world:addLight(hash, javaLight)
end

local function removeBasicLightFromWorld(hash)
	assert(clientSide, "lights not supported on this side")
	world:removeLight(hash)
end

-- lime table

addToLime({
	platform = {
		create = addPlatformToWorld,
	},
	entity = {
		create = addEntityToWorld,
	},
	actor = {
		set = setActorForUser,
	},
	light = {
		basic = {
			add = addBasicLightToWorld,
			remove = removeBasicLightFromWorld,
		},
	},
})